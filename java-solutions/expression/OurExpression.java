package expression;

public interface OurExpression extends Expression, FloatMapExpression, TripleExpression {
    void toMiniString(StringBuilder sb, boolean inBrackets);
}
