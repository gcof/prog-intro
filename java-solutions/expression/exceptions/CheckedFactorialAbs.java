package expression.exceptions;

import expression.ExpressionNode;
import expression.FactorialAbs;
import expression.UnaryOperation;

public class CheckedFactorialAbs extends FactorialAbs {
    @Override
    public int makeOperation(int operand) {
        operand = operand < 0 ? -operand : operand;
        if (operand > 11 || operand < 0) {
            throw new ArithmeticException("Factorial overflow");
        }
        int result = 1;
        for (int i = 1; i <= operand; i++) {
            result *= i;
        }
        return result;
    }

    public CheckedFactorialAbs(ExpressionNode operand) {
        super(operand);
    }
}
