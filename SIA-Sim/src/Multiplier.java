public class Multiplier {
    public static void multiply(Word32 a, Word32 b, Word32 result) {

        for (int i = 0; i < 32; i++) {
            result.setBitN(i, new Bit(false));
        }
//        Word32 PartialSum = new Word32();
        for (int i = 0; i < 32; i++) {  //outer for loop is b
            Word32 temp = new Word32();
            Word32 ShiftedTemp = new Word32();
            for (int j = 0; j < 32; j++) {  //inner for loop is a
                if (b.getBitAt(31-i).getValue() == Bit.boolValues.TRUE) {
                    temp.copy(a);
                }
//                else {
//                    temp.copy(new Word32());
//                }

                for (int k = 0; k < 32; k++) {  //for loop to left shift
                    if (31 - k - i >= 0) {
//                        ShiftedTemp.setBitN(31 - k - i, temp.getBitAt(31 - k));
                        ShiftedTemp.setBitN(31 - k - i, temp.getBitAt(31 - k));

                    }

//                    else {
//                        // If shifting goes out of bounds, setting it to false
////                        ShiftedTemp.setBitN(k, new Bit(false));
//                    }
                }
            }
//            Adder.add(ShiftedTemp, PartialSum, PartialSum);
            Adder.add(ShiftedTemp, result, result);

        }
//        result.copy(PartialSum);
    }
}



