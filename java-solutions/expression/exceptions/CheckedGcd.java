package expression.exceptions;

import expression.BinaryOperation;

public class CheckedGcd extends BinaryOperation {
    public CheckedGcd(expression.ExpressionNode left, expression.ExpressionNode right) {
        super(left, right);
    }

    protected static int gcd(int x, int y) {
        while (y != 0) {
            int t = y;
            y = x % y;
            x = t;
        }
        return x < 0 ? -x : x;
    }

    @Override
    public int makeOperation(int x, int y) {
        return gcd(x, y);
    }

    @Override
    public float makeOperation(float x, float y) {
        throw new ArithmeticException("GCD is not defined for float");
    }

    @Override
    public int getPriority() {
        return 5;
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
        return "gcd";
    }

}
