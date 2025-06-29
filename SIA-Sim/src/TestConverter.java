public class TestConverter {

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
            //
            if (binary[31-index] == 1) {
                result.setBitN(31-index, new Bit(true));
            } else {
                result.setBitN(31-index, new Bit(false));
            }
            //
            value /= 2;                 //then divides by 2
            index++;
        }
        if (temp < 0) {
            result.not(result);
        }
//        index = 32;
//        for (int i = index - 1; i >= 0; i--) {
//            if (binary[i] == 1) {
//                result.setBitN(j, new Bit(true));
//            } else {
//                result.setBitN(j, new Bit(false));
//            }
//            j++;
//        }
    }

    public static int toInt(Word32 value) {
        int i = 0;
        int ans = 0;
        Bit[] binary = new Bit[32];
        boolean bool = false;

        if (value.getBitAt(0).getValue() == Bit.boolValues.TRUE) {
            value.not(value);
            bool = true;
        }

        for (int j = 0; j < 32; j++) {
            binary[j] = value.getBitAt(31-j);
//            System.out.println(binary[j].getValue());
        }

        for (int j = 0; j < 32; j++) {
            if (binary[j].getValue() == Bit.boolValues.TRUE) {
                ans += Math.pow(2, j);
//                ans = ans + (2 * j);
            }
        }

        if (bool) {
            return (ans + 1) * -1;
        } else {
            return ans;
        }

    }

    public static void main(String[] args) {
        int num = 45;
        Word32 result = new Word32();
        fromInt(num, result);
        System.out.println(result);



        System.out.println(toInt(result));


        System.out.println("----------------");
        int num2 = -45;
        Word32 result2 = new Word32();
        fromInt(num2, result2);
        System.out.println(result2);
        System.out.println(toInt(result2));

    }
}