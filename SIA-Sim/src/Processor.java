import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class Processor {
    private Memory mem;
    public List<String> output = new LinkedList<>();
    private Word16 currentInstruction = new Word16();
    private Word16   nextInstruction = null;
    private Stack<Word32> stack = new Stack<>();
    private Stack<Integer> stackInt = new Stack<>();
    Word32 OP1 = new Word32();
    Word32 OP2 = new Word32();
    int format = 0;
    public Word32[] registers = new Word32[32];
    int pcCounter = 0;
    ALU alu = new ALU();
    int sourceRegister = 0;
    int destinationRegister  = 0;
    boolean flag = false;
    int currentClockCycle = 0; //cache
    private InstructionCache cache  = new InstructionCache();
    private l2Cache l2Cache = new l2Cache();

    public Processor(Memory m) {
        mem = m;
    }

    public static void createWord32Array(Word32[] array, int size) {
        for (int i = 0; i < size; i++) {
            array[i] = new Word32();
        }
    }

    public void run() {
        createWord32Array(registers, 32);
        Word16 halt = new Word16();
        Word16 temp = new Word16();

        mem.read();
        mem.value.getTopHalf(temp);
        currentInstruction.copy(temp);
        while (!flag && !currentInstruction.equals(halt)) {
            fetch();
            decode();
            execute();
            store();
        }
        System.out.println("Clock cycles: " + currentClockCycle);
    }

    private void fetch() {
//        currentClockCycle += 300;
        if (nextInstruction != null) {
            // We have a buffered “bottom half” from the previous read.
            currentInstruction.copy(nextInstruction);
            nextInstruction = null;
        } else {
            // Prepare to buffer the bottom half after reading.
            nextInstruction = new Word16();
            Word16 topHalf    = new Word16();
            Word16 bottomHalf = new Word16();

            //cache stuff here
            cache.read(mem, pcCounter, this);
            cache.value.getTopHalf   (topHalf);
            cache.value.getBottomHalf(bottomHalf);

            // 3) Advance to the next 32‑bit word for future fetches
            pcCounter++;

            // 5) Execute the top half now, and buffer the bottom half
            currentInstruction.copy(topHalf);
            nextInstruction.copy(bottomHalf);
        }
    }

//    private void fetch() {
//        currentClockCycle += 300;
//        if (nextInstruction != null) { //This is for to check if we did the decoded for the bottom half
//            currentInstruction.copy(nextInstruction);
//            nextInstruction = null;
//        } else {
//            nextInstruction = new Word16();
//            Word16 topHalf = new Word16();
//            Word16 bottomHalf = new Word16();
//
//            Word32 zero = new Word32(); //new
//            Word32 currentPCcounter = new Word32(); //new
//            fromInt(pcCounter, currentPCcounter); //new
////        mem.read();
//            Adder.add(zero, currentPCcounter , mem.address);  //new
//            mem.read();
//            pcCounter++; //new
//
//            mem.value.getTopHalf(topHalf);
//            mem.value.getBottomHalf(bottomHalf);
//
//            //for bottom half
//            currentInstruction.copy(topHalf);
//            nextInstruction.copy(bottomHalf);
//        }
//    }

    private void decode() {

        String currInstrucString = BitSubstring(currentInstruction, 0, 15);
        String opCode = currInstrucString.substring(0, 5);

        //checking for ble, blt, bge, bgt, beq
        if (opCode.equals("fttff") || opCode.equals("fttft") ||
            opCode.equals("ftttf") || opCode.equals("ftttt")) {
            format = 33;
        } else if (opCode.equals("tffff")) {
            int x = 0;
            currentClockCycle += x;
        } else if (opCode.equals("fffff")) {
            format = 100;
            flag = true;
        } else if (opCode.equals("fftft")) { //subtract
            currentClockCycle += 2;
            //TODO needs to do 2R format
            format = 5;
            OP1 = Word16Split(currentInstruction, 6, 10);
            OP2 = Word16Split(currentInstruction, 11, 15);

            destinationRegister = toInt(OP2);

            alu.instruction = currentInstruction;
            alu.op2.copy(OP1);
            alu.op1.copy(registers[destinationRegister]);
        } else if (opCode.equals("ftfff")) { //sysCall
            currentClockCycle += 1;
            format = 8;
            OP1 = Word16Split(currentInstruction, 11, 15);
            sourceRegister = toInt(OP1);
        } else if (opCode.equals("ftfft")) { //call
            currentClockCycle += 1;
            format = 9;
            OP1 = Word16Split(currentInstruction, 6, 15);
            sourceRegister = toInt(OP1);
        } else if (opCode.equals("ftftf")) { //return
            currentClockCycle += 1;
            format = 10;
        } else if (opCode.equals("ftftt")) { //compare
            format = 11;
            OP1 = Word16Split(currentInstruction, 6, 10);
            OP2 = Word16Split(currentInstruction, 11, 15);

            destinationRegister = toInt(OP2);

            alu.instruction = currentInstruction;
            alu.op1.copy(OP1);
            alu.op2.copy(registers[destinationRegister]);
        } else if (opCode.equals("tftff")) { //copy
            format = 20;
            OP1 = Word16Split(currentInstruction, 6, 10);
            OP2 = Word16Split(currentInstruction, 11, 15);
            sourceRegister = toInt(OP1);
            destinationRegister  = toInt(OP2);
        } else if (opCode.equals("tfftf")) { //load
            format = 18;
            OP1 = Word16Split(currentInstruction, 6, 10);
            OP2 = Word16Split(currentInstruction, 11, 15);
        } else if (opCode.equals("tfftt")) { //store
                format = 19;
                OP1 = Word16Split(currentInstruction, 6, 10);
                OP2 = Word16Split(currentInstruction, 11, 15);
        } else if (opCode.equals("tffft")) { //bne
            format = 17;
            OP1 = Word16Split2(currentInstruction, 6, 15, true);
        } else if (currentInstruction.getBitAt(5).getValue() == Bit.boolValues.TRUE) { //Immediate format
            if (opCode.equals("ffftt")) {
                currentClockCycle += 10;
            } else {
                currentClockCycle += 2;
            }
            format = 2;
            OP1 = Word16Split(currentInstruction, 6, 10);
            OP2 = Word16Split(currentInstruction, 11, 15);

            destinationRegister = toInt(OP2);

            alu.instruction = currentInstruction;
            alu.op2.copy(OP1);
            alu.op1.copy(registers[destinationRegister]);
        } else {    //2R format (2 registers)
            if (opCode.equals("ffftt")) {
                currentClockCycle += 10;
            } else {
                currentClockCycle += 2;
            }
            currentClockCycle += 2;
            format = 1;
            OP1 = Word16Split(currentInstruction, 6, 10);
            OP2 = Word16Split(currentInstruction, 11, 15);

            alu.instruction.copy(currentInstruction);

            sourceRegister = toInt(OP1);
            destinationRegister  = toInt(OP2);

            alu.op1.copy(registers[sourceRegister]);
            alu.op2.copy(registers[destinationRegister]);
        }

    }


    // (Opp code)   (format)      (5 bit)            (5 bit)
    // 0 1 2 3 4    5             6 7 8 9  10        11 12 13 14 15
    // 1 2 3 4 5    6             7 8 9 10 11        12 13 14 15 16


    private void execute() {
        alu.result = new Word32();
        if (format == 1) {
            alu.doInstruction();
        } else if (format == 2) {
            alu.doInstruction();
        } else if (format == 5) {   //subtract
            alu.doInstruction();
        } else if (format == 9) {  //call
            int temp = pcCounter;
            stackInt.push(temp);
            pcCounter += sourceRegister;  //current pc + immediate value
            pcCounter--;
        } else if (format == 10) {  //return
            pcCounter = stackInt.pop();
            nextInstruction = null;
        } else if (format == 11) {  //compare
            alu.doInstruction();
        } else if (format == 17) { //bne
            if (alu.equal.getValue() != Bit.boolValues.TRUE) {
                int SignedImmediateValue = toInt(OP1);
                pcCounter += SignedImmediateValue;
                pcCounter--;
                nextInstruction = null;
            }
        } else if (format == 18) { //load
//            currentClockCycle += 300;
            if (currentInstruction.getBitAt(5).getValue() == Bit.boolValues.FALSE) {  //load r1 r9 (2R)

            } else {       //load -1 r2 (immediate)
//                Word32 temp = new Word32();
                if (currentInstruction.getBitAt(5).getValue() == Bit.boolValues.TRUE) {
                    if (currentInstruction.getBitAt(6).getValue() == Bit.boolValues.TRUE) {
                        OP1 = Word16Split2(currentInstruction, 6, 10, true);
                    } else {
                        OP1 = Word16Split2(currentInstruction, 6, 10, false);
                    }
//                    OP1 = Word16Split2(currentInstruction, 6, 10, true);
                } else {
                    OP1 = Word16Split2(currentInstruction, 11, 15, false);
                }
                int destinationRegister  = toInt(OP2);
                Word32 tempRegister = new Word32();
                Adder.add(registers[destinationRegister], OP1 , tempRegister);
                int sourceRegister = toInt(OP1);
                sourceRegister = toInt(tempRegister);

//                mem.address.copy(tempRegister);
//                mem.read();
                int form = 0;
                Word32 source = new Word32();
                l2Cache.writeLoad(mem, tempRegister, this, form, source);
                registers[destinationRegister].copy(mem.value);
            }
        }
    }

    private void store() {
        if (format == 1 || format == 2 || format == 5) {
            registers[destinationRegister].copy(alu.result);
        } else if (format == 8) {   //sysCall
            if (sourceRegister == 1) {
                printMem();
            } else if (sourceRegister == 0) {
                printReg();
            }
        } else if (format == 20) { //copy
            if (currentInstruction.getBitAt(5).getValue() == Bit.boolValues.FALSE) { //copy r9 r2 (2R)
//                sourceRegister = toInt(OP1);
//                destinationRegister  = toInt(OP2);
                registers[destinationRegister].copy(registers[sourceRegister]);
            } else {  //copy 10 r9 (immediate)
//                int destinationRegister = toInt(OP2);
                registers[destinationRegister].copy(OP1);
            }
        } else if (format == 19) { //store
//            currentClockCycle += 300;
            if (currentInstruction.getBitAt(5).getValue() == Bit.boolValues.FALSE) {  //store r1 r9 (2R)
                //"store r3 r9" means that the processor takes the value currently in register r3
                // and writes (stores) it into memory at the address specified by register r9.
                int sourceRegister = toInt(OP1);
                int destinationRegister  = toInt(OP2);

//                mem.address.copy(registers[destinationRegister]); //getting the address from destinationRegister
//                mem.value.copy(registers[sourceRegister]);        //getting the value from sourceRegister
//                mem.write();                                //storing the value from sourceRegister into address from destinationRegister

                l2Cache.writeLoad(mem, registers[destinationRegister], this, 1, registers[sourceRegister]);
            } else {                          //store 1 r9 (Immediate)
                int destinationRegister = toInt(OP2);
//                mem.address.copy(registers[destinationRegister]);
//                mem.value.copy(OP1);
//                mem.write();

                l2Cache.writeLoad(mem, registers[destinationRegister], this, 1, OP1);

            }
        }

    }

    public Word32 Word16Split(Word16 input, int start, int end) {
        int count = 31;
        Word32 output = new Word32();
        for (int i = end; i >= start; i--) {
            output.setBitN(count, input.getBitAt(i));
            count--;
        }
        return output;
    }

    public Word32 Word16Split2(Word16 input, int start, int end, boolean flag) {
        int count = 31;
        Word32 output = new Word32();
        for (int i = end; i >= start; i--) {
            output.setBitN(count, input.getBitAt(i));
            count--;
        }
        if (flag) {
            for (int j = count; j >= 0; j--) {
                output.setBitN(j, new Bit(true));
            }
        }
        return output;
    }


    public String BitSubstring(Word16 bits, int start, int end) {
        StringBuilder sb = new StringBuilder();
        for (int i = start; i < end + 1; i++) {
            if (bits.getBitAt(i).getValue() == Bit.boolValues.TRUE) {
                sb.append("t");
            } else if (bits.getBitAt(i).getValue() == Bit.boolValues.FALSE) {
                sb.append("f");
            }
        }
        return sb.toString();
    }


    public static int toInt(Word32 value) {
        int ans = 0;
        Bit[] binary = new Bit[32];
        boolean bool = false;
        if (value.getBitAt(0).getValue() == Bit.boolValues.TRUE) {
            value.not(value);
            bool = true;
        }
        for (int j = 0; j < 32; j++) {
            binary[j] = value.getBitAt(31-j);
        }
        for (int j = 0; j < 32; j++) {
            if (binary[j].getValue() == Bit.boolValues.TRUE) {
                ans += Math.pow(2, j);
            }
        }
        if (bool) {
            return (ans + 1) * -1;
        } else {
            return ans;
        }
    }


    private void printReg() {
        for (int i = 0; i < 32; i++) {
            var line = "r"+ i + ":" + registers[i].toString(); // TODO: add the register value here...
            output.add(line);
            System.out.println(line);
        }
    }

    private void printMem() {
        for (int i = 0; i < 1000; i++) {
            Word32 addr = new Word32();
            Word32 value = new Word32();
            // Convert i to Word32 here...
            fromInt(i, addr);
//            addr.copy(mem.address);
            mem.address.copy(addr);
            mem.read();
//            mem.value.copy(value);
            value.copy(mem.value);
//            var line = i + ":" + value + "(" + TestConverter.toInt(value) + ")";
            var line = i + ":" + value;
            output.add(line);
            System.out.println(line);
        }
    }

    public static void fromInt(int value, Word32 result) {
        int[] binary = new int[32];
        int index = 0;
        int temp = value;
         if (value < 0) {
            value = (value * -1) - 1;
        }
        for (int i = 0; i < 32; i++) {
            result.setBitN(i, new Bit(false));
        }
        while (value != 0) {
            binary[31-index] = value % 2;  //givens 1 or 0
            if (binary[31-index] == 1) {
                result.setBitN(31-index, new Bit(true));
            } else {
                result.setBitN(31-index, new Bit(false));
            }
            value /= 2;                 //then divides by 2
            index++;
        }
        if (temp < 0) {
            result.not(result);
        }
    }


}