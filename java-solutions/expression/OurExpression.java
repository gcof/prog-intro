package expression;

public interface OurExpression extends Expression, FloatMapExpression, TripleExpression{
    String toString();
    int hashCode();
    boolean equals(Object obj);
    void toMiniString(StringBuilder sb, boolean needBrackets);
}
