import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ProcessorTest {
@Test
    public void testAdd() {
        String[] program = {
                "add 10 r0", //immediate
                "syscall 0"  //call/return
        };
    var p = runProgram(program);
    assertEquals("r0:f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,t,f,t,f,",p.output.getFirst());
    }

    @Test
    public void testFibonacci() {
        String[] program = {
                "copy 10 r9", // address  -- 0
                "leftshift 1 r9", // r9 = 20
                "copy 15 r0", // limit for fib -- 1
                "store 0 r9",  // first fib                  //store stores it into memory address
                "add 1 r9", // move to 2nd -- 2
                "store 1 r9",  // second fib
                "add 1 r9", // move to next spot in ram -- 3
                "copy r9 r2", // temporary
                "copy r9 r3", // temporary2 -- 4             ///passed up to here
                "load -1 r2", // load f(n-1) into r2            //TODO needs to change load
                "load -2 r3", // load f(n-2) into r3-- 5            //TODO needs to change load
                "add r2 r3", // create next fib
                "store r3 r9", // store fib -- 6
                "subtract 1 r0", // decrement counter      ///passed up to here
                "compare 0 r0", // compare counter to 0 -- 7
                "bne -4", // loop back to 3
                "syscall 1", // print out memory -- 8
                "halt" // done
        };
        var p = runProgram(program);
        assertEquals("35:f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,t,f,f,t,t,f,f,f,t,f,",p.output.get(35));
        assertEquals("36:f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,t,t,t,t,f,t,t,f,t,t,",p.output.get(36));
    }
    @Test
    public void testPower() {
        String[] program = {
                "copy 6 r0", // address 0
                "copy 2 r1",
                "copy 0 r2", // address 1
                "call 4", // call 1+4 = 5
                "syscall 0",// address 2
                "copy 7 r0",
                "copy 3 r1",// address 3
                "call 2", // call 3 + 2
                "syscall 0",// address 4       //passed up to here
                "halt",
                "copy r0 r2",// address 5
                "copy r0 r2", // waste a spot
                "multiply r0 r2", // address 6
                "subtract 1 r1",
                "compare 1 r1",// address 7
                "bne -1", // -- loop back to address 6
                "return", // address 8
        } ;
        var p = runProgram(program);
        assertEquals("r2:f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,t,f,f,t,f,f,",p.output.get(2));
        assertEquals("r2:f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,t,f,t,f,t,f,t,t,t,",p.output.get(34));
    }

    @Test
    public void testSumDefaultZeroMemory() {
        // This program simply sums 20 words starting at address 200,
        // but since memory is initialized to 0, the sum should remain 0.
        String[] program = {
                "copy 10 r3",        // r3 = 10                 address --0
                "leftshift 6 r3",    // r3 = 640 (base address)
                "copy 20 r0",        // r0 = 20 (counter)       address --1
                "copy 0 r2",         // r2 = 0 (accumulator)
                // sum‑loop (instructions 4..9)
                "copy r3 r4",        // r4 = pointer            address --2
                "load 0 r4",         // r4 = MEM[r4]  (always 0)
                "add r4 r2",         // r2 += r4     (still 0)  address --3
                "add 1 r3",          // r3++
                "subtract 1 r0",     // r0--                    address --4
                "compare 0 r0",      // set flags
                "bne -3",            // back to "copy r3 r4" until r0 hits 0    address --5
                // dump registers & halt
                "syscall 0",
                "halt"                                        //address --6
        };
        var p = runProgram(program);
        // r2 should still be 0 (32 zeros => all 'f')
        assertEquals("r2:f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,",p.output.get(2));
        assertEquals("r0:f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,",p.output.get(0));
    }

    @Test
    public void testSumDefaultZeroMemoryBackwards() {
        String[] program = {
                "copy 10 r3",      // r3 = 10                   address --0
                "leftshift 6 r3",  // r3 = 640 (base address)
                "add 19 r3",       // r3 = 219                  address --1
                // Phase 1: set up counter & accumulator
                "copy 20 r0",      // r0 = 20 (elements left)
                "copy 0 r2",      // r2 = 0  (sum)             address --2
                "copy 0 r2",      // r2 = 0  (sum)
                // Phase 2: loop 20 times, loading backward
                // (instructions 4..9), branch back -6
                "copy r3 r4",      // r4 = pointer              address --3
                "load 0 r4",       // r4 = MEM[r4]
                "add r4 r2",      // r2 += r4                  address --4
                "subtract 1 r3",   // r3--  (move pointer backward)
                "subtract 1 r0",   // r0--  (decrement count)   address --5
                "compare 0 r0",    // set flags
                "bne -3",          // back to “copy r3 r4”      address --6
                // done
                "syscall 0",
                "halt"                                    //    address --7
        };

        var p = runProgram(program);

        // r2 should still be 0 (memory was zero)
        assertEquals("r2:" + "f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,",p.output.get(2));
        assertEquals("r0:f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,",p.output.get(0));
    }

    @Test
    public void LinkedListSum() {
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
                // sum‑loop
                "copy r3 r4",   //8     // r4 = pointer            address --2
                "load 0 r4",         // r4 = MEM[r4]
                "add r4 r2",   //9      // r2 += r4       address --3
                "add 1 r3",          // r3++
                "subtract 1 r0",  //10   // r0--
                "compare 0 r0",      // set flags
                "bne -3",       //11     // back to "copy r3 r4" until r0 hits 0
                // dump registers & halt
                "syscall 0",
                "halt"        //12
        };
        var p = runProgram(program);
        assertEquals("r2:f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,t,t,f,t,f,f,t,f,", p.output.get(2));
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
