package markup;

import java.util.List;

public class OrderedList extends ElementPrinter implements ListElement {

    public OrderedList(List<ListItem> list) {
        super(list, "", "orderedlist", List.of());
    }
}
