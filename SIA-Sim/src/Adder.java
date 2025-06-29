public class Adder {

    public static void subtract(Word32 a, Word32 b, Word32 result) {

        Word32 negatedB = new Word32();
//        Word32.not(b, negatedB);
        b.not(negatedB);

        Word32 temp = new Word32();
        temp.setBitN(31, new Bit(true));

        Word32 temp3 = new Word32();
//        Word32.and(negatedB, temp, temp3);
        add(negatedB, temp, temp3);
        add(a, temp3, result);


    }

    public static void add(Word32 a, Word32 b, Word32 result) {
//        for (int i = 31; i >= 0; i--) {  // Start from the least significant bit (rightmost)
//            if (a.getBitAt(i).getValue() == Bit.boolValues.TRUE && b.getBitAt(i).getValue() == Bit.boolValues.TRUE) {
//                if (numofCarry > 0) {
//                    result.setBitN(i, new Bit(true)); // 1 + 1 + 1 = 1 (carry 1)
//                } else {
//                    result.setBitN(i, new Bit(false)); // 1 + 1 = 0 (carry 1)
//                }
//                numofCarry = 1;  // Always set carry to 1 when adding 1 + 1
//            }
//            else if (a.getBitAt(i).getValue() == Bit.boolValues.TRUE && b.getBitAt(i).getValue() == Bit.boolValues.FALSE) {
//                if (numofCarry > 0) {
//                    result.setBitN(i, new Bit(false)); // 1 + 0 + 1 = 0 (carry 1)
//                } else {
//                    result.setBitN(i, new Bit(true)); // 1 + 0 = 1
//                }
//            }
//            else if (a.getBitAt(i).getValue() == Bit.boolValues.FALSE && b.getBitAt(i).getValue() == Bit.boolValues.TRUE) {
//                if (numofCarry > 0) {
//                    result.setBitN(i, new Bit(false)); // 0 + 1 + 1 = 0 (carry 1)
//                } else {
//                    result.setBitN(i, new Bit(true)); // 0 + 1 = 1
//                }
//            }
//            else if (a.getBitAt(i).getValue() == Bit.boolValues.FALSE && b.getBitAt(i).getValue() == Bit.boolValues.FALSE) {
//                if (numofCarry > 0) {
//                    result.setBitN(i, new Bit(true)); // 0 + 0 + 1 = 1
//                    numofCarry = 0;  // Carry used up
//                } else {
//                    result.setBitN(i, new Bit(false)); // 0 + 0 = 0
//                }
//            }
//        }

        Bit Cin = new Bit(false);
        for (int i = 0; i < 32; i++) {
            Bit Cout = new Bit(false);


            // Sum = ((X xor Y) xor Cin)
            // Cout = X and Y or ((X xor Y) and Cin)

            Bit resultBit = new Bit(false);
            Bit sum = new Bit(false);

            //Sum = ((X xor Y)
            Bit.xor(a.getBitAt(31-i), b.getBitAt(31-i), resultBit);

            // Sum = ((X xor Y) xor Cin)
            Bit.xor(resultBit, Cin, sum);


            // Cout = X and Y
            Bit resultBit2 = new Bit(false);
            Bit.and(a.getBitAt(31-i), b.getBitAt(31-i), resultBit2);

            // Cout = ...... (X xor Y)
            Bit resultBit3 = new Bit(false);
            Bit.xor(a.getBitAt(31-i), b.getBitAt(31-i), resultBit3);

            // Count = .....((X xor Y) and Cin)
            Bit resultBit4 = new Bit(false);
            Bit.and(resultBit3, Cin, resultBit4);

            // Cout = X and Y or ((X xor Y) and Cin)
            Bit.or(resultBit2, resultBit4, Cout);

            Cin.assign(Cout.getValue());
            result.setBitN(31-i, sum);
        }
    }

        public static void main(String[] args) {
            Bit t = new Bit(true);
            Bit f = new Bit(false);

            // First Word32 (101101)
            Bit[] a = new Bit[32];
            for (int i = 0; i < 32; i++) {
                a[i] = f;  // Initialize all bits to false
            }
            a[26] = t;
            a[27] = f;
            a[28] = t;
            a[29] = t;
            a[30] = f;
            a[31] = t;
            Word32 word1 = new Word32(a);

            // Second Word32 (111101)
            Bit[] b = new Bit[32];
            for (int i = 0; i < 32; i++) {
                b[i] = f;  // Initialize all bits to false
            }
            b[26] = t;
            b[27] = t;
            b[28] = t;
            b[29] = t;
            b[30] = f;
            b[31] = t;
            Word32 word2 = new Word32(b);

            Word32 result = new Word32();
            add(word1, word2, result);

            System.out.println(result);

    }

}
