package expression;

public class Log extends BinaryOperation {
    public Log(ExpressionNode left, ExpressionNode right) {
        super(left, right);
    }

    @Override
    public int makeOperation(int left, int right) {
        if (left == 0 && right > 0) {
            return Integer.MIN_VALUE;
        }
        if (right == 1) {
            return (left == 1 || left <= -1) ? 0 : Integer.MAX_VALUE;
        }
        if (left <= 0 || right <= 0) {
            return 0;
        }
        int count;
        for (count = 0; left >= right; count++) {
            left /= right;
        }
        return count;
    }

    @Override
    public float makeOperation(float left, float right) {
        throw new IllegalArgumentException("Logarithm is not defined for float numbers");
    }

    @Override
    public String getOperationSymbol() {
        return "//";
    }

    @Override
    public int getPriority() {
        return 25;
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
