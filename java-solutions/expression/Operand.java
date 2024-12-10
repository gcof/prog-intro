package expression;

public interface Operand {
    default boolean isOperand(){return true;};
    default int getPriority(){return 50;};
}
