package markup;

public class Text implements TextElement {
    private final String text;

    public Text(String text) {
        this.text = text;
    }

    @Override
    public void toMarkdown(StringBuilder str) {
        str.append(text);
    }

    @Override
    public void toDocBook(StringBuilder str) {
        str.append(text);
    }
}
