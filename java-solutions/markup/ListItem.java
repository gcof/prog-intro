package markup;

import java.util.List;

public class ListItem extends ElementPrinter {

    public ListItem(List<ListElement> list) {
        super(list, "", "listitem", List.of());
    }

}
