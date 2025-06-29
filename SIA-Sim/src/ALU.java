public class ALU {
    public Word16 instruction = new Word16();
    public Word32 op1 = new Word32();
    public Word32 op2 = new Word32();
    public Word32 result = new Word32();
    public Bit less = new Bit(false);
    public Bit equal = new Bit(false);

    public void doInstruction(){

//        Bit[] bits = new Bit[5];
//        int oppCode = 0;
//
//        for (int i = 0; i < 5; i++) {
//            bits[i] = new Bit(false);
//        }
//
//        for (int n = 0; n < 5; n++) {
//            instruction.getBitN(n, bits[n]);
//        }
//
//        for (int i = 0; i < 5; i++) {
//            if (bits[4-i].getValue() == Bit.boolValues.TRUE) {
//                oppCode += Math.pow(2, i);
//            }
//        }

        Bit bit4 = instruction.getBitAt(4);
        Bit bit3 = instruction.getBitAt(3);
        Bit bit2 = instruction.getBitAt(2);
        Bit bit1 = instruction.getBitAt(1);
        Bit bit0 = instruction.getBitAt(0);







//        if (instruction.getBitAt(4).getValue() == Bit.boolValues.TRUE && //number 1
//                instruction.getBitAt(3).getValue() == Bit.boolValues.FALSE &&
//                instruction.getBitAt(2).getValue() == Bit.boolValues.FALSE &&
//                instruction.getBitAt(1).getValue() == Bit.boolValues.FALSE &&
//                instruction.getBitAt(0).getValue() == Bit.boolValues.FALSE) {
//            Adder.add(op1, op2, result);
//        } else if (instruction.getBitAt(4).getValue() == Bit.boolValues.FALSE && //number 2
//                instruction.getBitAt(3).getValue() == Bit.boolValues.TRUE &&
//                instruction.getBitAt(2).getValue() == Bit.boolValues.FALSE &&
//                instruction.getBitAt(1).getValue() == Bit.boolValues.FALSE &&
//                instruction.getBitAt(0).getValue() == Bit.boolValues.FALSE) {
//            Word32.and(op1, op2, result);
//        } else if (instruction.getBitAt(4).getValue() == Bit.boolValues.TRUE &&  //if its number 3
//                instruction.getBitAt(3).getValue() == Bit.boolValues.TRUE &&
//                instruction.getBitAt(2).getValue() == Bit.boolValues.FALSE &&
//                instruction.getBitAt(1).getValue() == Bit.boolValues.FALSE &&
//                instruction.getBitAt(0).getValue() == Bit.boolValues.FALSE) {
//            Multiplier.multiply(op1, op2, result);
//        }



        //instruction.getBitAt(4).getValue() == Bit.boolValues.TRUE
        if (bit4.getValue() == Bit.boolValues.TRUE &&
                bit3.getValue() == Bit.boolValues.FALSE &&
                bit2.getValue() == Bit.boolValues.FALSE &&
                bit1.getValue() == Bit.boolValues.FALSE &&
                bit0.getValue() == Bit.boolValues.FALSE) {
            Adder.add(op1, op2, result);
        } else if (bit4.getValue() == Bit.boolValues.FALSE &&
                bit3.getValue() == Bit.boolValues.TRUE &&
                bit2.getValue() == Bit.boolValues.FALSE &&
                bit1.getValue() == Bit.boolValues.FALSE &&
                bit0.getValue() == Bit.boolValues.FALSE) {
            Word32.and(op1, op2, result);
        } else if (bit4.getValue() == Bit.boolValues.TRUE &&
                bit3.getValue() == Bit.boolValues.TRUE &&
                bit2.getValue() == Bit.boolValues.FALSE &&
                bit1.getValue() == Bit.boolValues.FALSE &&
                bit0.getValue() == Bit.boolValues.FALSE) {
            Multiplier.multiply(op1, op2, result);
        } else if (bit4.getValue() == Bit.boolValues.FALSE && // Left Shift
                bit3.getValue() == Bit.boolValues.FALSE &&
                bit2.getValue() == Bit.boolValues.TRUE &&
                bit1.getValue() == Bit.boolValues.FALSE &&
                bit0.getValue() == Bit.boolValues.FALSE) {
            int amount = 0;
            for (int i = 0; i < 32; i++) {
                if (op2.getBitAt(31-i).getValue() == Bit.boolValues.TRUE) {
                    amount += Math.pow(2, i);
                }
            }
            Shifter.LeftShift(op1, amount, result);
        } else if (bit4.getValue() == Bit.boolValues.TRUE &&   // Subtract
                bit3.getValue() == Bit.boolValues.FALSE &&
                bit2.getValue() == Bit.boolValues.TRUE &&
                bit1.getValue() == Bit.boolValues.FALSE &&
                bit0.getValue() == Bit.boolValues.FALSE) {
            Adder.subtract(op1, op2, result);
        } else if (bit4.getValue() == Bit.boolValues.FALSE &&
                bit3.getValue() == Bit.boolValues.TRUE &&
                bit2.getValue() == Bit.boolValues.TRUE &&
                bit1.getValue() == Bit.boolValues.FALSE &&
                bit0.getValue() == Bit.boolValues.FALSE) {
            Word32.or(op1, op2, result);
        } else if (bit4.getValue() == Bit.boolValues.TRUE &&
                bit3.getValue() == Bit.boolValues.TRUE &&
                bit2.getValue() == Bit.boolValues.TRUE &&
                bit1.getValue() == Bit.boolValues.FALSE &&
                bit0.getValue() == Bit.boolValues.FALSE) {
            int amount = 0;
            for (int i = 0; i < 32; i++) {
                if (op2.getBitAt(31-i).getValue() == Bit.boolValues.TRUE) {
                    amount += Math.pow(2, i);
                }
            }
            Shifter.RightShift(op1, amount, result);
        } else if (bit4.getValue() == Bit.boolValues.TRUE &&
                bit3.getValue() == Bit.boolValues.TRUE &&
                bit2.getValue() == Bit.boolValues.FALSE &&
                bit1.getValue() == Bit.boolValues.TRUE &&
                bit0.getValue() == Bit.boolValues.FALSE) {
            if (Word32.equals(op1, op2)) {
                equal = new Bit(true);
                less = new Bit(false);
            } else {
                equal = new Bit(false);
                Adder.subtract(op1, op2, result);
                if (result.getBitAt(0).getValue() == Bit.boolValues.TRUE) {
                    less = new Bit(true);
                }

//                Adder.subtract(op1, op2, result);
//                if (result.getBitAt(0).getValue() == Bit.boolValues.TRUE) {
//                    less = new Bit(true);
//                } else if (Word32.equals(op1, op2)) {
//                    equal = new Bit(true);
//                }
            }

        }
        //for the less and greater subtract using the binary subtraction thing and check the first index of the result
        //if its a negative value that means its less than or if its a positive value that means its greater than
        //for compare, compare using the compare function of Word32

        //As for the member variables for greater than and less than change the less member for compare equal change the
        //equal member value


    }


}
