package markup;

import java.util.Collections;
import java.util.List;

public class Paragraph extends ElementPrinter implements ListElement {

    public Paragraph(List<TextElement> list) {
        super(list, "", "para", List.of());
    }
}