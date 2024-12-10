package expression;

public class FactorialAbs extends UnaryOperation {

    @Override
    public boolean isOperationPrefix() {
        return false;
    }

    @Override
    public boolean needWhitespaces() {
        return false;
    }

    @Override
    public String getOperationSymbol() {
        return "!";
    }

    @Override
    public int makeOperation(int operand) {
        operand = operand < 0 ? -operand : operand;
        if (operand > 32 || operand < 0) {
            return 0;
        }
        int result = 1;
        for (int i = 1; i <= operand; i++) {
            result *= i;
        }
        return result;
    }

    @Override
    public float makeOperation(float operand) {
        return makeOperation((int) operand);
    }

    @Override
    public int getPriority() {
        return 45;
    }

    public FactorialAbs(ExpressionNode operand) {
        super(operand);
    }
}
