package expression.exceptions;

import expression.ExpressionNode;
import expression.Multiply;

public class CheckedMultiply extends Multiply {
    public CheckedMultiply(ExpressionNode left, ExpressionNode right) {
        super(left, right);
    }

    public static int multiply(int left, int right) {
        if (left == Integer.MIN_VALUE && right == -1 || right == Integer.MIN_VALUE && left == -1) {
            throw new ArithmeticException("Overflow in multiply with " + left + " * " + right);
        }

        if (left > 0) {
            if (right > 0 && left > Integer.MAX_VALUE / right || right < 0 && left > Integer.MIN_VALUE / right && right != -1) {
                throw new ArithmeticException("Overflow in multiply with " + left + " * " + right);
            }
        } else if (left < 0) {
            if (right > 0 && left < Integer.MIN_VALUE / right || right < 0 && left < Integer.MAX_VALUE / right) {
                throw new ArithmeticException("Overflow in multiply with " + left + " * " + right);
            }
        }
        return left * right;
    }


    @Override
    public int makeOperation(int left, int right) {
        return multiply(left, right);
    }
}
