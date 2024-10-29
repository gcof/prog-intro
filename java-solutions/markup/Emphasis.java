package markup;

import java.util.Collections;
import java.util.List;

public class Emphasis extends ElementPrinter implements TextElement{

    public Emphasis(List<TextElement> list) {
        super(list, "*", "emphasis", Collections.emptyList());
    }
}