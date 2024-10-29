public class Sum {

    static int evalSumInOneString(String arg) {
        int j = 0;
        int sum = 0;
        boolean nowInsideNumber = false;

        for (int i = 0; i < arg.length(); i++) {

            if (!nowInsideNumber && (Character.isDigit(arg.charAt(i)) || arg.charAt(i) == '-' || arg.charAt(i) == '+')) {
                nowInsideNumber = true;
                j = i;
            }
            if (nowInsideNumber && !Character.isDigit(arg.charAt(i)) && arg.charAt(i) != '-' && arg.charAt(i) != '+') {
                sum += Integer.parseInt(arg.substring(j, i));
                nowInsideNumber = false;
            }
        }


        if (nowInsideNumber) {
            sum += Integer.parseInt(arg.substring(j));
        }
        return sum;
    }

    public static void main(String[] args) {

        int sum = 0;

        for (String arg : args) {
            sum += evalSumInOneString(arg);
        }

        System.out.println(sum);
    }
}