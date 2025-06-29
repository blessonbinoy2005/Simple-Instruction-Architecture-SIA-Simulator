public class l2Cache {
    private final Word32[] block0 = new Word32[8];
    private final Word32[] block1 = new Word32[8];
    private final Word32[] block2 = new Word32[8];
    private final Word32[] block3 = new Word32[8];

    private final int[] blockNumbers = new int[4];

    public Word32 value = new Word32();

    public l2Cache() {
        for (int i = 0; i < 8; i++) {
            block0[i] = new Word32();
            block1[i] = new Word32();
            block2[i] = new Word32();
            block3[i] = new Word32();
        }
        for (int i = 0; i < 4; i++) {
            blockNumbers[i] = -1;
        }
    }
    public void read(Memory memory, int addr, Processor cpu) {
        int blockAddrindex = (addr / 8) * 8;   //is the address of the first word in that block, basically base address of the 8-word chunk that contains instruction we want
        int blockNumber      = (addr / 8) % 4;
        int tags       = blockNumbers[blockNumber];

        if (tags != blockAddrindex) {  //when a miss happenes
            cpu.currentClockCycle += 350;
            if (blockNumber == 0) {
                for (int i = 0; i < 8; i++) {
                    int waddr = blockAddrindex + i;
                    Word32 wa = new Word32();
                    fromInt(waddr, wa);
                    memory.address.copy(wa);
                    memory.read();
                    block0[i].copy(memory.value);
                }
            } else if (blockNumber == 1) {
                for (int i = 0; i < 8; i++) {
                    int waddr = blockAddrindex + i;
                    Word32 wa = new Word32();
                    fromInt(waddr, wa);
                    memory.address.copy(wa);
                    memory.read();
                    block1[i].copy(memory.value);
                }
            } else if (blockNumber == 2) {
                for (int i = 0; i < 8; i++) {
                    int waddr = blockAddrindex + i;
                    Word32 wa = new Word32();
                    fromInt(waddr, wa);
                    memory.address.copy(wa);
                    memory.read();
                    block2[i].copy(memory.value);
                }
            } else {
                for (int i = 0; i < 8; i++) {
                    int waddr = blockAddrindex + i;
                    Word32 wa = new Word32();
                    fromInt(waddr, wa);
                    memory.address.copy(wa);
                    memory.read();
                    block3[i].copy(memory.value);
                }
            }
            blockNumbers[blockNumber] = blockAddrindex;
        } else {
            cpu.currentClockCycle += 20;
        }
        int newBlockNumber = addr - blockAddrindex;
        if (blockNumber == 0) {
            value.copy(block0[newBlockNumber]);
        } else if (blockNumber == 1) {
            value.copy(block1[newBlockNumber]);
        } else if (blockNumber == 2) {
            value.copy(block2[newBlockNumber]);
        } else {
            value.copy(block3[newBlockNumber]);
        }
    }

    public void writeLoad(Memory memory, Word32 addr, Processor cpu, int form, Word32 source) {
        cpu.currentClockCycle += 50;
        if (form == 0) {
            memory.address.copy(addr);
            memory.read();
        } else if (form == 1) {
            memory.address.copy(addr);
            memory.value.copy(source);
            memory.write();
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
