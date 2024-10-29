package markup;

public interface Printable {
    void toMarkdown(StringBuilder sb);
    void toDocBook(StringBuilder sb);
}
