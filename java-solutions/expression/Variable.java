package expression;

import java.util.Map;

public class Variable implements ExpressionNode, Operand {
    private final String name;

    public Variable(String name) {
        this.name = name;
    }

    @Override
    public int evaluate(int x) {
        return x;
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return switch (name) {
            case "x" -> x;
            case "y" -> y;
            case "z" -> z;
            default -> throw new IllegalArgumentException("Variable " + name + " not found");
        };
    }

    @Override
    public float evaluateF(Map<String, Float> x) {
        if (x.containsKey(name)) {
            return x.get(name);
        } else {
            throw new IllegalArgumentException("Variable " + name + " not found");
        }
    }

    @Override
    public boolean needBrackets(int priority, boolean isLeft, boolean isCommutative, boolean isAssociative) {
        return false;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public void toString(StringBuilder sb) {
        sb.append(name);
    }

    @Override
    public String toMiniString() {
        return name;
    }

    @Override
    public void toMiniString(StringBuilder sb, boolean inBrackets) {
        sb.append(name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Variable) {
            return name.equals(((Variable) obj).name);
        }
        return false;
    }
}
