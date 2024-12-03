package expression;

public class Divide extends BinaryOperation {
    public Divide(ExpressionNode left, ExpressionNode right) {
        super(left, right);
    }

    @Override
    public int makeOperation(int left, int right) {
        return left / right;
    }

    @Override
    public float makeOperation(float left, float right) {
        return left / right;
    }

    @Override
    public String getOperationSymbol() {
        return "/";
    }

    @Override
    public int getPriority() {
        return 2;
    }

    @Override
    public boolean isCommutative() {
        return false;
    }

    @Override
    public boolean isAssociative() {
        return false;
    }
}

