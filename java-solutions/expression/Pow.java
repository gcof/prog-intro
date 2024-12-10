package expression;

public class Pow extends BinaryOperation {
    public Pow(ExpressionNode left, ExpressionNode right) {
        super(left, right);
    }

    private int binPow(int a, int p) {
        if (p <= 0) {
            return 1;
        }
        if (p % 2 == 0) {
            int t = binPow(a, p / 2);
            return t * t;
        }
        return a * binPow(a, p - 1);
    }

    @Override
    public int makeOperation(int left, int right) {
        return binPow(left, right);
    }

    @Override
    public float makeOperation(float left, float right) {
        throw new IllegalArgumentException("Pow is not defined for float numbers");
    }

    @Override
    public String getOperationSymbol() {
        return "**";
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
