package expression.exceptions;

import expression.ExpressionNode;
import expression.Subtract;

public class CheckedSubtract extends Subtract {
    public CheckedSubtract(ExpressionNode left, ExpressionNode right) {
        super(left, right);
    }

    @Override
    public int makeOperation(int x, int y) {
        if (y > 0 ? x < Integer.MIN_VALUE + y : x > Integer.MAX_VALUE + y) {
            throw new ArithmeticException("Overflow");
        }
        return x - y;
    }
}
