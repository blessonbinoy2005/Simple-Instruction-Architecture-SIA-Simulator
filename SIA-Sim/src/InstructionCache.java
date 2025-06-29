public class InstructionCache {

    private final Word32[] sram = new Word32[8];
    private Word32 address= new Word32();
    public Word32 value = new Word32();
    private int pointer = -1;
    private final l2Cache l2Cache = new l2Cache();


    public InstructionCache() {
        for (int i = 0; i < 8; i++) {
            sram[i] = new Word32();
        }
    }

    public void read(Memory memory, int addr, Processor cpu) {
        int temp = (addr / 8) * 8;
        if (temp != pointer) {
            cpu.currentClockCycle += 350;  //cache miss
            for (int i = 0; i < 8; i++) {
                int memAddr = temp + i;
                l2Cache.read(memory, memAddr, cpu);
                sram[i].copy(l2Cache.value);
            }
            pointer = temp;
        }
        cpu.currentClockCycle += 10;
        int point = addr - pointer;
        value.copy(sram[point]);
    }

//    public void read(Memory memory, int addr, Processor cpu) {
//        int temp = (addr / 8) * 8;
//        if (temp != pointer) {
//            cpu.currentClockCycle += 350;  //cache miss
//            for (int i = 0; i < 8; i++) {
//                int memAddr = temp + i;
//                Word32 result = new Word32();
//                fromInt(memAddr, result);
//                memory.address.copy(result);
//                memory.read();
//                sram[i].copy(memory.value);
//            }
//            pointer = temp;
//        } else { //cache hit
//            cpu.currentClockCycle += 10;
//        }
//
//        int point = addr - pointer;
//        value.copy(sram[point]);
//    }

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
