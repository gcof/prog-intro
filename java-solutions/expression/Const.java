package expression;

import java.awt.datatransfer.FlavorEvent;
import java.util.Map;

public class Const implements ExpressionNode, Operand {
    private final int value;
    private final boolean isFloat;


    public Const(int value) {
        this.value = value;
        isFloat = false;
    }

    public Const(float value) {
        this.value = Float.floatToIntBits(value);
        isFloat = true;
    }

    @Override
    public int evaluate(int x) {
        return value;
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return value;
    }

    @Override
    public float evaluateF(Map<String, Float> x) {
        return Float.intBitsToFloat(value);
    }

    @Override
    public boolean needBrackets(int priority, boolean isLeft, boolean isCommutative, boolean isAssociative) {
        return false;
    }

    @Override
    public String toString() {
        if (isFloat) {
            return (Float.toString(Float.intBitsToFloat(value)));
        }
        return Integer.toString(value);
    }

    @Override
    public String toMiniString() {
        return this.toString();
    }

    @Override
    public void toMiniString(StringBuilder sb, boolean inBrackets) {
        sb.append(this);
    }

    @Override
    public int hashCode() {
        if(isFloat) {
            return Float.hashCode(Float.floatToIntBits(value));
        }
        return Integer.hashCode(value);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Const) {
            return value == (((Const) obj).value);
        }
        return false;
    }
}
