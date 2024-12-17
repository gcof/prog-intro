package expression.exceptions;

import expression.Add;
import expression.ExpressionNode;

public class CheckedAdd extends Add {
    public CheckedAdd(ExpressionNode left, ExpressionNode right) {
        super(left, right);
    }

    @Override
    public int makeOperation(int x, int y) {
        if (y > 0 ? x > Integer.MAX_VALUE - y : x < Integer.MIN_VALUE - y) {
            throw new OveflowException("Overflow in addition: " + x + " + " + y);
        }
        return x + y;
    }
}
