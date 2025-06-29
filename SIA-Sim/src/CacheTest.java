import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CacheTest {

    @Test
    public void SumIntegersTest  () {
        String[] program = {
                "copy 10 r3",        // r3 = 10                 address --0
                "leftshift 6 r3",    // r3 = 640 (base address)
                "copy 20 r0",        // r0 = 20 (counter)       address --1
                "copy 0 r2",         // r2 = 0 (accumulator)
                // adding loop
                "copy r3 r4",        // r4 = pointer            address --2
                "load 0 r4",         // r4 =   (always 0)
                "add r4 r2",         // r2 += r4     (still 0)  address --3
                "add 1 r3",          // r3++
                "subtract 1 r0",     // r0--                    address --4
                "compare 0 r0",      // set flags
                "bne -3",            // back to "copy r3 r4" until r0 hits 0    address --5
                "syscall 0",
                "halt"                                        //address --6
        };
        var p = runProgram(program);
        // r2 should still be 0 (32 zeros => all 'f')
        assertEquals("r2:f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,",p.output.get(2));
        assertEquals("r0:f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,",p.output.get(0));
    }

    @Test
    public void LinkedListSumTest() {
        String[] program = {
                "copy 10 r3",    //0
                "leftshift 6 r3",
                "copy 1 r5",     //1
                "copy 20 r0",
                "store r5 r3",   //2    store-loop (4..9) → bne -2 back to index 2
                "add 1 r5",          // r5++
                "add 1 r3",      //3    r3++
                "subtract 1 r0",     // r0--
                "compare 0 r0",  //4    r0 == 0?
                "bne -2",            //  if r0≠0 goto 2
                "beq -2",        //5 //space

                "copy 10 r3",       // r3 = 10                 address --0
                "leftshift 6 r3", //6    // r3 = 640 (base address)
                "copy 20 r0",        // r0 = 20 (counter)       address --1
                "copy 0 r2",     //7    // r2 = 0 (accumulator)
                "copy 0 r2",
                // adding loop
                "copy r3 r4",   //8     // r4 = pointer            address --2
                "load 0 r4",         // r4
                "add r4 r2",   //9      // r2 += r4       address --3
                "add 1 r3",          // r3++
                "subtract 1 r0",  //10   // r0--
                "compare 0 r0",      // set flags
                "bne -3",       //11     // back to "copy r3 r4" until r0 hits 0
                "syscall 0",
                "halt"        //12
        };
        var p = runProgram(program);
        assertEquals("r2:f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,t,t,f,t,f,f,t,f,", p.output.get(2));
    }

    @Test
    public void testSumBackwards () {
        String[] program = {
                "copy 10 r3",      // r3 = 10                   address --0
                "leftshift 6 r3",  // r3 = 640 (base address)
                "add 19 r3",       // r3 = 219                  address --1
                // set up counter & accumulator
                "copy 20 r0",      // r0 = 20 (elements left)
                "copy 0 r2",      // r2 = 0  (sum)             address --2
                "copy 0 r2",      // r2 = 0  (sum)
                // loop 20 times, loading backward
                //  branch back -6
                "copy r3 r4",      // r4 = pointer              address --3
                "load 0 r4",       // r4
                "add r4 r2",      // r2 += r4                  address --4
                "subtract 1 r3",   // r3--  (move pointer backward)
                "subtract 1 r0",   // r0--  (decrement count)   address --5
                "compare 0 r0",    // set flags
                "bne -3",          // back to “copy r3 r4”      address --6
                "syscall 0",
                "halt"                                    //    address --7
        };
        var p = runProgram(program);
        assertEquals("r2:f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,",p.output.get(2));
        assertEquals("r0:f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,",p.output.get(0));
    }

    private static Processor runProgram(String[] program) {
        var assembled = Assembler.assemble(program);
        var merged = Assembler.finalOutput(assembled);
        var m = new Memory();
        m.load(merged);
        var p = new Processor(m);
        p.run();
        return p;
    }

}
