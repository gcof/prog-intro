package expression.exceptions;

import expression.ExpressionNode;

public class CheckedPrefixFactorial extends CheckedFactorialAbs {
    private static final int MAX_FACTORIAL = 12;
    private static final int MAX_FACTORIAL_VALUE = 479001600;

    public CheckedPrefixFactorial(ExpressionNode operand) {
        super(operand);
    }

    @Override
    public boolean isOperationPrefix() {
        return true;
    }

    @Override
    public int makeOperation(int operand) {
        if (operand <= 0) {
            return 0;
        }
        if (operand > MAX_FACTORIAL_VALUE) {
            return MAX_FACTORIAL;
        }
        int result = 1, answer = 1;
        while(result <= operand) {
            result *= ++answer;
        }
        return answer - 1;
    }
}
