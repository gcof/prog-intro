package expression;

import java.util.Map;

public class Const implements ExpressionNode, Operand {
    private final Number value; // :NOTE: не эффективно

    public Const(int value) {
        this.value = value;
    }

    public Const(float value) {
        this.value = value;
    }

    @Override
    public int evaluate(int x) {
        return value.intValue();
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return value.intValue();
    }

    @Override
    public float evaluateF(Map<String, Float> x) {
        return value.floatValue();
    }

    @Override
    public boolean needBrackets(int priority, boolean isLeft, boolean isCommutative, boolean isAssociative) {
        return false;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public String toMiniString() {
        return value.toString();
    }

    @Override
    public void toMiniString(StringBuilder sb, boolean inBrackets) {
        sb.append(value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Const) {
            return value.equals(((Const) obj).value);
        }
        return false;
    }
}
