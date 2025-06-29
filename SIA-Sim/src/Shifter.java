public class Shifter {
    public static void LeftShift(Word32 source, int amount, Word32 result) {
        for (int i = 0; i < 32; i++) {
            if (i - amount >= 0) {
                result.setBitN(i - amount, source.getBitAt(i));  // Shift bits left
            } else {
                result.setBitN(i, new Bit(false));
            }
        }
    }

    public static void RightShift(Word32 source, int amount, Word32 result) {
        for (int i = amount; i < 32; i++) {
            if (i + amount < 32 ) {
                result.setBitN(i + amount, source.getBitAt(i));  // Shift bits left
            }
        }
    }
}
