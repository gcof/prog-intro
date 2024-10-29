public class SumLongPunctHex {

    static boolean isWhitespace(Character c) {
        return (Character.isWhitespace(c) || Character.getType(c) == Character.START_PUNCTUATION 
        || Character.getType(c) == Character.END_PUNCTUATION);
    }
    
    static long parseNum(String arg) {
        arg = arg.toLowerCase();
        if (arg.startsWith("0x")) {
            return Long.parseUnsignedLong(arg.substring(2), 16);
        } else {
            return Long.parseLong(arg);
        }
    }

    static long evalSumInOneString(String arg) {
        int j = 0;
        long sum = 0;
        boolean nowInsideNumber = false;

        for (int i = 0; i < arg.length(); i++) {
            if (!nowInsideNumber && !isWhitespace(arg.charAt(i))) {
                nowInsideNumber = true;
                j = i;
            }
            if (nowInsideNumber && isWhitespace(arg.charAt(i))) {
                sum += parseNum(arg.substring(j, i));
                nowInsideNumber = false;
            }
        }

        if (nowInsideNumber) {
            sum += parseNum(arg.substring(j));
        }
        return sum;
    }

    public static void main(String[] args) {
        
        long sum = 0;

        for (String arg : args) {
            sum += evalSumInOneString(arg);
        }

        System.out.println(sum);
    }
}