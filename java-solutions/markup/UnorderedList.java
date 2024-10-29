package markup;

import java.util.List;

public class UnorderedList extends ElementPrinter implements ListElement{

    public UnorderedList(List<ListItem> list) {
        super(list, "", "itemizedlist", List.of());
    }
}