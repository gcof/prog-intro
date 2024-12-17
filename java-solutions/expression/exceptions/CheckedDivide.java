package expression.exceptions;

import expression.Divide;
import expression.ExpressionNode;

public class CheckedDivide extends Divide {
    public CheckedDivide(ExpressionNode left, ExpressionNode right) {
        super(left, right);
    }

    protected static int divide(int left, int right) {
        if (right == 0) {
            throw new DivisionByZeroException("Division by zero: " + left + " / " + right);
        }
        if (left == Integer.MIN_VALUE && right == -1) {
            throw new ArithmeticException("Overflow in divide operation: " + left + " / " + right);
        }
        return left / right;
    }

    @Override
    public int makeOperation(int left, int right) {
        return divide(left, right);
    }
}
