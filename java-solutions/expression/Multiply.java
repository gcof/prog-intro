package expression;

import java.util.Map;

public class Multiply extends BinaryOperation {
    public Multiply(ExpressionNode left, ExpressionNode right) {
        super(left, right);
    }

    @Override
    public int makeOperation(int left, int right) {
        return left * right;
    }

    @Override
    public float makeOperation(float left, float right) {
        return left * right;
    }

    @Override
    public String getOperationSymbol() {
        return "*";
    }

    @Override
    public int getPriority() {
        return 2;
    }

    @Override
    public boolean isCommutative() {
        return true;
    }

    @Override
    public boolean isAssociative() {
        return true;
    }
}
