public class Word32 {

    private Bit[] Value;

    public Bit getBitAt(int index) {
        return Value[index];
    }

    public Word32() {
        Value = new Bit[32];
        for (int i = 0; i < 32; i++) {
            Value[i] = new Bit(false);
        }
    }

    public Word32(Bit[] in) {
        Value = new Bit[32];
        for (int i = 0; i < in.length; i++) {
            Value[i] = in[i];
        }
    }

    public void getTopHalf(Word16 result) { // sets result = bits 0-15 of this word. use bit.assign
//        for (int i = 0; i < 16; i++) {
//            Value[i].assign(result.getBitValue()[i].getValue());
//        }
        for (int i = 0; i < 16; i++) {
            result.setBitN(i, this.Value[i]);
//            this.Value[i].assign(result.getBitAt(i).getValue());
        }
    }

    public void getBottomHalf(Word16 result) { // sets result = bits 16-31 of this word. use bit.assign
//        for (int i = 0; i < 16; i++) {
//            Value[i+16].assign(result.getBitValue()[i].getValue());
//        }
        for (int i = 0; i < 16; i++) {
            result.setBitN(i, this.Value[i+16]);
        }
    }

    public void copy(Word32 result) { // sets result's bit to be the same as this. use bit.assign
        for (int i = 0; i < 32; i++) {
//            result.Value[i].assign(this.Value[i].getValue());
            this.Value[i].assign(result.Value[i].getValue());
        }
    }

    public boolean equals(Word32 other) {
        return equals(this, other);
    }

    public static boolean equals(Word32 a, Word32 b) {
        return a.toString().equals(b.toString());
    }

    public void getBitN(int n, Bit result) { // use bit.assign
        result.assign(Value[n].getValue());
    }



    public void setBitN(int n, Bit source) { //  use bit.assign
//        Value[n] = source;
        Value[n].assign(source.getValue());
    }

    public void and(Word32 other, Word32 result) {
        and(this, other, result);
    }

    public static void and(Word32 a, Word32 b, Word32 result) {
        for (int i = 0; i < 32; i++) {
            Bit.and(a.Value[i],b.Value[i],result.Value[i]);
        }
    }

    public void or(Word32 other, Word32 result) {
        or(this, other, result);
    }

    public static void or(Word32 a, Word32 b, Word32 result) {
        for (int i = 0; i < 32; i++) {
            Bit.or(a.Value[i],b.Value[i],result.Value[i]);
        }
    }

    public void xor(Word32 other, Word32 result) {
        xor(this, other, result);
    }

    public static void xor(Word32 a, Word32 b, Word32 result) {
        for (int i = 0; i < 32; i++) {
            Bit.xor(a.Value[i],b.Value[i],result.Value[i]);
        }
    }

    public void not( Word32 result) {
        not(this, result);
    }

    public static void not(Word32 a, Word32 result) {
        for (int i = 0; i < 32; i++) {
            Bit.not(a.Value[i],result.Value[i]);
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Bit bit : Value) {
            sb.append(bit.toString());
            sb.append(",");
        }
        return sb.toString();
    }
}
