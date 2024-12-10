package expression;

public class UnaryMinus extends UnaryOperation{

    public UnaryMinus(ExpressionNode expression) {
        super(expression);
    }

    public boolean isOperationPrefix() {
        return true;
    }

    @Override
    public boolean needWhitespaces() {
        return true;
    }

    @Override
    public String getOperationSymbol() {
        return "-";
    }

    @Override
    public int makeOperation(int operand) {
        return -operand;
    }

    @Override
    public float makeOperation(float operand) {
        return -operand;
    }

    @Override
    public boolean needBrackets(int priority, boolean isLeft, boolean isCommutative, boolean isAssociative) {
        return false;
    }
}
