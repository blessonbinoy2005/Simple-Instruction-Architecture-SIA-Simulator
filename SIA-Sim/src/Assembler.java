import java.util.HashMap;
import java.util.LinkedList;
import java.util.regex.Pattern;

public class Assembler {

    private static final HashMap <String, String> opCode = new HashMap<>();
    private static final HashMap <String, String> registers = new HashMap<>();

    static {
        registers.put("r0", "fffff"); //0
        registers.put("r1", "fffft"); //1
        registers.put("r2", "ffftf"); //2
        registers.put("r3", "ffftt"); //3
        registers.put("r4", "fftff"); //4
        registers.put("r5", "fftft"); //5
        registers.put("r6", "ffttf"); //6
        registers.put("r7", "ffttt"); //7
        registers.put("r8", "ftfff"); //8
        registers.put("r9", "ftfft"); //9
        registers.put("r10", "ftftf"); //10
        registers.put("r11", "ftftt"); //11
        registers.put("r12", "fttff"); //12
        registers.put("r13", "fttft"); //13
        registers.put("r14", "ftttf"); //14
        registers.put("r15", "ftttt"); //15
        registers.put("r16", "tffff"); //16
        registers.put("r17", "tffft"); //17
        registers.put("r18", "tfftf"); //18
        registers.put("r19", "tfftt"); //19
        registers.put("r20", "tftff"); //20
        registers.put("r21", "tftft"); //21
        registers.put("r22", "tfttf"); //22
        registers.put("r23", "tfttt"); //23
        registers.put("r24", "ttfff"); //24
        registers.put("r25", "ttfft"); //25
        registers.put("r26", "ttftf"); //26
        registers.put("r27", "ttftt"); //27
        registers.put("r28", "tttff"); //28
        registers.put("r29", "tttft"); //29
        registers.put("r30", "ttttf"); //30
        registers.put("r31", "ttttt"); //31
    }

    static {
        opCode.put("halt", "fffff"); //0
        opCode.put("add", "fffft"); //1
        opCode.put("and", "ffftf"); //2
        opCode.put("multiply", "ffftt"); //3
        opCode.put("leftshift", "fftff"); //4
        opCode.put("subtract", "fftft"); //5
        opCode.put("or", "ffttf"); //6
        opCode.put("rightshift", "ffttt"); //7
        opCode.put("syscall", "ftfff"); //8
        opCode.put("call", "ftfft"); //9
        opCode.put("return", "ftftf"); //10
        opCode.put("compare", "ftftt"); //11
        opCode.put("ble", "fttff"); //12
        opCode.put("blt", "fttft"); //13
        opCode.put("bge", "ftttf"); //14
        opCode.put("bgt", "ftttt"); //15
        opCode.put("beq", "tffff"); //16
        opCode.put("bne", "tffft"); //17
        opCode.put("load", "tfftf"); //18
        opCode.put("store", "tfftt"); //19
        opCode.put("copy", "tftff"); //20
    }


    public static String[] assemble(String[] input) {
        String[] array = new String[input.length];
        String output = " ";

        for (int i = 0; i < input.length; i++) {
            output = "";
            String[] code = input[i].split(" ");
            int index = 0;
                if (opCode.containsKey(code[index])) { //getting the first oppCode
                    output += opCode.get(code[index]);
                    index++;
                }
                //checking if next string is a number
                if (index < code.length - 1 && isNumericRegex(code[index])) { //Character.isDigit(code[index].charAt(0))
                    int value = Integer.parseInt(code[index]);
                    output += "t";
                    output += toBinary(value, 4);
                    index++;
                    if (registers.containsKey(code[index])) {
                        output += registers.get(code[index]);
                    }
                } else if (index < code.length - 1 && !Character.isDigit(code[index].charAt(0))) { //!Character.isDigit(code[index].charAt(0))
                    output += "f";
                    if (registers.containsKey(code[index])) {
                        output += registers.get(code[index]);
                        index++;
                        if (registers.containsKey(code[index])) {
                            output += registers.get(code[index]);
//                        index++;
                        }
                    }
                } else {
                    if (index > code.length - 1) { //checking if there is anything after the oppCode //ex "return"
                        output += "fffffffffff";
                    } else { //this is for "syscall 100" oppCode and a number
                        int value = Integer.parseInt(code[index]);
                        output += toBinary(value, 10);
                    }
                }
                array[i] = output;
            }
        return array;
    }

    public static boolean isNumericRegex(String str) {
        return Pattern.matches("-?\\d+(\\.\\d+)?", str);
    }

    public static String toBinary (int input, int size) {
        String output = "";

        if (size == 4) {
            output = "fffff";
        } else if (size == 10) {
            output = "fffffffffff";
        }

        int index = 0;
        int value = input;
        StringBuilder sb = new StringBuilder(output);

//        if (input == -1) {
//            input = (input * -1);
//        } else if (input < 0) {
//            input = (input * -1) - 1;
//        }

        if (input < 0) {
            input = (input * -1) - 1;
        }

        while (input != 0) {
            int bit = input % 2;
            if (bit == 1) {
                sb.setCharAt(size - index, 't');
            } else if (bit == 0) {
                sb.setCharAt(size - index, 'f');
            }
            input /= 2;
            index++;
        }
        output = sb.toString();

        //If the original number was a negative number
        if (value < 0) {
            for (int i = 0; i < output.length(); i++) {
                if (output.charAt(i) == 't') {
                    sb.setCharAt(i, 'f');
                } else if (output.charAt(i) == 'f') {
                    sb.setCharAt(i, 't');
                }
            }
            output = sb.toString();
        }
        return output;
    }

    public static String[] finalOutput(String[] input) {
        if (input.length % 2 != 0) {
            String[] extendedInput = new String[input.length + 1];
            System.arraycopy(input, 0, extendedInput, 0, input.length);
            extendedInput[input.length] = "ffffffffffffffff";
            input = extendedInput;
        }
        String[] array = new String[100];
        int index = 0;
        int outputIndex = 0;
        while (index < input.length - 1) {
            array[outputIndex] = input[index] + input[index + 1];
            outputIndex += 1;
            index += 2;
        }
        return array;
    }
}
