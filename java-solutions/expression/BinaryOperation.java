package expression;

import java.util.Map;

public abstract class BinaryOperation implements ExpressionNode {
    private static final int HASH_CONST = 239;
    protected final ExpressionNode left;
    protected final ExpressionNode right;


    abstract public String getOperationSymbol();

    abstract public int makeOperation(int left, int right);

    abstract public float makeOperation(float left, float right);

    abstract public int getPriority();

    abstract public boolean isCommutative();

    abstract public boolean isAssociative();

    public BinaryOperation(ExpressionNode left, ExpressionNode right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public int evaluate(int x) {
        return makeOperation(left.evaluate(x), right.evaluate(x));
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return makeOperation(left.evaluate(x, y, z), right.evaluate(x, y, z));
    }

    public float evaluateF(Map<String, Float> x) {
        return makeOperation(left.evaluateF(x), right.evaluateF(x));
    }

    @Override
    public int hashCode() {
        int leftHash = 0;
        int rightHash = 0;
        if (left != null) {
            leftHash = left.hashCode();
        }
        if (right != null) {
            rightHash = right.hashCode();
        }
        return (((this.getClass().hashCode() * HASH_CONST) + leftHash) * HASH_CONST + rightHash) * HASH_CONST;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj.getClass() == this.getClass()) {
            BinaryOperation that = (BinaryOperation) obj;
            boolean leftEquals = that.left.equals(this.left);
            boolean rightEquals = that.right.equals(this.right);
            return leftEquals && rightEquals;
        }
        return false;
    }

    @Override
    public String toString() {
        return "(" + left.toString() + " " + getOperationSymbol() + " " + right.toString() + ")";
    }

    @Override
    public String toMiniString() {
        StringBuilder sb = new StringBuilder();
        toMiniString(sb, false);
        return sb.toString();
    }

    @Override
    public void toMiniString(StringBuilder sb, boolean inBrackets) {
        if (inBrackets) {
            sb.append("(");
        }
        boolean leftBrackets = left.needBrackets(getPriority(), true, isCommutative(), isAssociative());
        left.toMiniString(sb, leftBrackets);
        sb.append(" ").append(getOperationSymbol()).append(" ");
        boolean rightBrackets = right.needBrackets(getPriority(), false, isCommutative(), isAssociative());
        right.toMiniString(sb, rightBrackets);
        if (inBrackets) {
            sb.append(")");
        }
    }

    @Override
    public boolean needBrackets(int priority, boolean isLeft, boolean isCommutative, boolean isAssociative) {
        return getPriority() < priority || (!isLeft && getPriority() == priority && (!isCommutative || !isAssociative()));
    }
}
