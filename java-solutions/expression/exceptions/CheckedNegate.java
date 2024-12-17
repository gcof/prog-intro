package expression.exceptions;

import expression.ExpressionNode;
import expression.UnaryMinus;

public class CheckedNegate extends UnaryMinus {
    public CheckedNegate(ExpressionNode operand) {
        super(operand);
    }

    @Override
    public int makeOperation(int x) {
        if (x == Integer.MIN_VALUE) {
            throw new ArithmeticException("Overflow in unary minus");
        }
        return -x;
    }
}
