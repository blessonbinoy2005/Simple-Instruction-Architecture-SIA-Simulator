public class Word16 {

    private Bit[] value;

    //Accessor
    public Bit[] getBitValue() {
        return this.value;
    }

    public Bit getBitAt(int index) {
        return value[index];
    }

    //should be default constructor initialize every index to 0?
    public Word16() {
        value = new Bit[16];
        for (int i = 0; i < 16; i++) {
            value[i] = new Bit(false);
        }
    }

    public Word16(Bit[] in) {
        value = new Bit[16];
        for (int i = 0; i < in.length; i++) {
            value[i] = in[i];
        }
    }

    // sets the values in "result" to be the same as the values in this instance; use "bit.assign"
    public void copy(Word16 result) {
//        value = new Bit[16];
//        for (int i = 0; i < value.length; i++) {
//            result.value[i].assign(value[i].getValue());
//        }
        for (int i = 0; i < 16; i++) {
            this.value[i].assign(result.value[i].getValue());
        }
    }

    public void setBitN(int n, Bit source) { // sets the nth bit of this word to "source"
//        value[n] = source;
        value[n].assign(source.getValue());
    }

    public void getBitN(int n, Bit result) { // sets result to be the same value as the nth bit of this word
        result.assign(value[n].getValue());
    }

    public boolean equals(Word16 other) { // is other equal to this
        return equals(this, other);
    }

    public static boolean equals(Word16 a, Word16 b) {
        return a.toString().equals(b.toString());
    }

    public void and(Word16 other, Word16 result) {
        and (this, other, result);
    }

    public static void and(Word16 a, Word16 b, Word16 result) {
        for (int i = 0; i < 16; i++) {
            Bit.and(a.value[i],b.value[i],result.value[i]);
        }
    }

    public void or(Word16 other, Word16 result) {
        or (this, other, result);
    }

    public static void or(Word16 a, Word16 b, Word16 result) {
        for (int i = 0; i < 16; i++) {
            Bit.or(a.value[i],b.value[i],result.value[i]);
        }
    }

    public void xor(Word16 other, Word16 result) {
        xor (this, other, result);
    }

    public static void xor(Word16 a, Word16 b, Word16 result) {
        for (int i = 0; i < 16; i++) {
            Bit.xor(a.value[i],b.value[i],result.value[i]);
        }
    }

    public void not( Word16 result) {
        not(this, result);
    }

    public static void not(Word16 a, Word16 result) {
        for (int i = 0; i < 16; i++) {
            Bit.not(a.value[i],result.value[i]);
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Bit bit : value) {
            sb.append(bit.toString());
            sb.append(",");
        }
        return sb.toString();
    }
}