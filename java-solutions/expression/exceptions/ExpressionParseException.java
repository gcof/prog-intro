package expression.exceptions;

public class ExpressionParseException extends IllegalArgumentException {
    public ExpressionParseException(String message) {
        super(message);
    }
}
