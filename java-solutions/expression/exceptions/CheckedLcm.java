package expression.exceptions;

import expression.BinaryOperation;
import expression.ExpressionNode;

public class CheckedLcm extends BinaryOperation {
    public CheckedLcm(ExpressionNode left, ExpressionNode right) {
        super(left, right);
    }

    @Override
    public int makeOperation(int x, int y) {
        if (x == 0 || y == 0) {
            return 0;
        }
        int gcd = CheckedGcd.gcd(x, y);
        int xDivGcd = CheckedDivide.divide(x, gcd);

        return CheckedMultiply.multiply(xDivGcd,y);
    }

    @Override
    public float makeOperation(float x, float y) {
        throw new ArithmeticException("LCM is not defined for float");
    }

    @Override
    public int getPriority() {
        return 4;
    }

    @Override
    public boolean isCommutative() {
        return true;
    }

    @Override
    public boolean isAssociative() {
        return true;
    }

    @Override
    public String getOperationSymbol() {
        return "lcm";
    }
}
