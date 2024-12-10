package expression;

public interface ExpressionNode extends OurExpression {

    boolean needBrackets(int priority, boolean isLeft, boolean isCommutative, boolean isAssociative);
    boolean isOperand();
    int getPriority();
}
