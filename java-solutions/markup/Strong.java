package markup;

import java.util.List;

public class Strong extends ElementPrinter implements TextElement {

    public Strong(List<TextElement> list) {
        super(list, "__", "emphasis", List.of("role='bold'"));
    }
}
