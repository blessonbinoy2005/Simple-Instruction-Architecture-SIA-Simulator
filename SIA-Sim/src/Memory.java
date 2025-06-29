public class Memory {
    public Word32 address= new Word32();
    public Word32 value = new Word32();

    private final Word32[] dram= new Word32[1000];

    public int addressAsInt() {
        Bit bit = new Bit(false);
        int ans = 0;
        for (int k = 0; k < 32; k++) {
            address.getBitN(31-k, bit);
            if (bit.getValue() == Bit.boolValues.TRUE) {
                ans += Math.pow(2, k);
            }
        }
        if (ans > 999) {
            throw new IndexOutOfBoundsException("Memory address is out of range " + ans);
        } else {
            return ans;
        }
    }

    public Memory() {
        for (int i = 0; i < 1000; i++) {
            dram[i] = new Word32();
        }
    }

    public void read() {
        int addressAsInt = addressAsInt();
        value.copy(dram[addressAsInt]);             //getting the 32Bit from dram and putting it into value
    }

    public void write() {
        int addressAsInt = addressAsInt();
           dram[addressAsInt].copy(value);             //getting the 32Bit from value and putting it into write
    }

    public void load(String[] data) {
        for (int i = 0; i < data.length; i++) {
            if (data[i] != null && data[i].length() > 32) {
                throw new IndexOutOfBoundsException("Memory address is out of range" + data[i]);
            } else if (data[i] != null) {
                Word32 word32 = new Word32();
                for (int j = 0; j < data[i].length(); j++) {
                    Bit bit = new Bit(false);
                    if (data[i].charAt(j) == 't') {
                        bit.assign(Bit.boolValues.TRUE);
                    } else {
                        bit.assign(Bit.boolValues.FALSE);
                    }
                    word32.setBitN(j, bit);
                }
                dram[i].copy(word32);
            }
        }
    }
}
