package expression.parser;

public interface CharSource {
    boolean hasNext();
    char next();
    char peek();
    String peek(int depth);
    int getPos();
    IllegalArgumentException error(String message);
}
