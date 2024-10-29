package markup;

import java.util.List;

public abstract class ElementPrinter implements Printable {
    private final String markdownAddon;
    private final String docBookTag;
    private final List<String> docBookAttributes;
    private final List<? extends Printable> list;

    protected ElementPrinter(List<? extends Printable> list, String markdownAddon, String docBookTag,
                             List<String> docBookAttributes) {
        this.list = list;
        this.markdownAddon = markdownAddon;
        this.docBookTag = docBookTag;
        this.docBookAttributes = docBookAttributes;
    }

    @Override
    public void toMarkdown(StringBuilder sb) {
        sb.append(markdownAddon);
        for (Printable m : list) {
            m.toMarkdown(sb);
        }
        sb.append(markdownAddon);
    }

    @Override
    public void toDocBook(StringBuilder sb) {
        sb.append("<").append(docBookTag);
        for (String attr : docBookAttributes) {
            sb.append(" ").append(attr);
        }
        sb.append(">");
        for (Printable m : list) {
            m.toDocBook(sb);
        }
        sb.append("</").append(docBookTag).append(">");
    }
}
