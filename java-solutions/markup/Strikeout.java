package markup;

import java.util.List;

public class Strikeout extends ElementPrinter implements TextElement {

    public Strikeout(List<TextElement> list) {
        super(list, "~", "emphasis", List.of("role='strikeout'"));
    }
}