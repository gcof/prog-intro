package expression;

public abstract class UnaryOperation implements ExpressionNode {
    private static final int HASH_CONST = 239;
    protected final ExpressionNode operand;

    abstract public boolean isOperationPrefix();

    abstract public boolean needWhitespaces();

    abstract public String getOperationSymbol();

    abstract public int makeOperation(int operand);

    abstract public float makeOperation(float operand);

    public UnaryOperation(ExpressionNode operand) {
        this.operand = operand;
    }

    @Override
    public int evaluate(int x) {
        return makeOperation(operand.evaluate(x));
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return makeOperation(operand.evaluate(x, y, z));
    }

    @Override
    public float evaluateF(java.util.Map<String, Float> x) {
        return makeOperation(operand.evaluateF(x));
    }

    @Override
    public int hashCode() {
        return (this.getClass().hashCode() * HASH_CONST + operand.hashCode()) * HASH_CONST;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj.getClass() == this.getClass()) {
            UnaryOperation that = (UnaryOperation) obj;
            return that.operand.equals(this.operand);
        }
        return false;
    }

    @Override
    public int getPriority() {
        return 50;
    }

    @Override
    public boolean isOperand() {
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        toString(sb);
//        System.err.println("String: " + sb.toString());
        return sb.toString();
    }

    @Override
    public void toString(StringBuilder sb) {
        sb.append(isOperationPrefix() ? getOperationSymbol() : "");
        sb.append("(");
        operand.toString(sb);
        sb.append(")");
        sb.append(!isOperationPrefix() ? getOperationSymbol() : "");
    }

    @Override
    public String toMiniString() {
        StringBuilder sb = new StringBuilder();
        toMiniString(sb, false);
//        System.err.println("MiniString: " + sb.toString());
        return sb.toString();
    }


    @Override
    public void toMiniString(StringBuilder sb, boolean inBrackets) {
        sb.append(inBrackets ? "(" : "");
        boolean needBrackets = operand.needBrackets(getPriority(), false, false, false);
        sb.append(isOperationPrefix() ? getOperationSymbol() : "");
        sb.append(isOperationPrefix() && needWhitespaces() && !needBrackets ? " " : "");
        operand.toMiniString(sb, needBrackets);
        sb.append(!isOperationPrefix() && needWhitespaces() && !needBrackets ? " " : "");
        sb.append(!isOperationPrefix() ? getOperationSymbol() : "");
        sb.append(inBrackets ? ")" : "");
    }


    @Override
    public boolean needBrackets(int priority, boolean isLeft, boolean isCommutative, boolean isAssociative) {
        return priority > getPriority() && !isOperationPrefix();
    }
}
