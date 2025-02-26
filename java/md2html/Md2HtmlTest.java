package md2html;

import base.Selector;

import java.util.function.Consumer;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public final class Md2HtmlTest {
    // === Underline

    private static final Consumer<Md2HtmlTester> UNDERLINE = tester -> tester
            .test("++подчеркивание++", "<p><u>подчеркивание</u></p>")
            .test("Это ++подчеркивание++, вложенное в текст", "<p>Это <u>подчеркивание</u>, вложенное в текст</p>")
            .spoiled("Это не ++подчеркивание++", "<p>Это не ++подчеркивание++</p>", "+")
            .spoiled("Это не ++подчеркивание++ 2", "<p>Это не ++подчеркивание++ 2</p>", "+")
            .addElement("u", "++");

    // === Mark

    private static final Consumer<? super Md2HtmlTester> MARK = tester -> tester
            .test("~выделение~", "<p><mark>выделение</mark></p>")
            .test("Это ~выделение~, вставленное в текст", "<p>Это <mark>выделение</mark>, вставленное в текст</p>")
            .spoiled("Это не ~выделение~", "<p>Это не ~выделение~</p>", "~")
            .spoiled("Это не ~выделение~ 2", "<p>Это не ~выделение~ 2</p>", "~")
            .addElement("mark", "~");

    // === InsDel

    private static final Consumer<? super Md2HtmlTester> INS = tester -> tester
            .test("<<вставка>>", "<p><ins>вставка</ins></p>")
            .test("Это <<вставка>>, вложенная в текст", "<p>Это <ins>вставка</ins>, вложенная в текст</p>")
            .spoiled("Это не <<вставка>>", "<p>Это не &lt;&lt;вставка&gt;&gt;</p>", "<", ">")
            .spoiled("Это не <<вставка>> 2", "<p>Это не &lt;&lt;вставка&gt;&gt; 2</p>", "<", ">")
            .addElement("ins", "<<", ">>");

    private static final Consumer<? super Md2HtmlTester> DEL = tester -> tester
            .test("}}удаление{{", "<p><del>удаление</del></p>")
            .test("Это }}удаление{{, вложенное в текст", "<p>Это <del>удаление</del>, вложенное в текст</p>")
            .spoiled("Это не }}удаление{{", "<p>Это не }}удаление{{</p>", "{")
            .addElement("del", "}}", "{{");

    // === Image

    private static final Consumer<? super Md2HtmlTester> IMAGE = tester -> tester
            .test("![картинок](http://www.ifmo.ru/images/menu/small/p10.jpg)", "<p><img alt='картинок' src='http://www.ifmo.ru/images/menu/small/p10.jpg'></p>")
            .test("![картинка](https://kgeorgiy.info)", "<p><img alt='картинка' src='https://kgeorgiy.info'></p>")
            .test("![картинка с __псевдо-выделением__](https://kgeorgiy.info)", "<p><img alt='картинка с __псевдо-выделением__' src='https://kgeorgiy.info'></p>")
            .spoiled("![не картинка](https://kgeorgiy.info)", "<p>![не картинка](https://kgeorgiy.info)</p>", "!", "[", "]", "(", ")")
            .spoiled("![не картинка с __выделением__](https://kgeorgiy.info)", "<p>![не картинка с <strong>выделением</strong>](https://kgeorgiy.info)</p>", "!", "[", "]", "(", ")")
            .addElement("img", "![", (checker, markup, input, output) -> {
                final StringBuilder alt = new StringBuilder();
                final StringBuilder src = new StringBuilder();

                checker.generate(markup, alt, new StringBuilder());
                checker.generate(markup, src, new StringBuilder());

                input.append("![").append(alt).append("](").append(src).append(')');
                output.append("<img alt='").append(alt).append("' src='").append(src).append("'>");
            });

    // === Link

    private static final Consumer<? super Md2HtmlTester> LINK = tester -> tester
            .test("[ссылок с _выделением_]<https://kgeorgiy.info>", "<p><a href='https://kgeorgiy.info'>ссылок с <em>выделением</em></a></p>")
            .test("[ссылка с __выделением__]<https://kgeorgiy.info>", "<p><a href='https://kgeorgiy.info'>ссылка с <strong>выделением</strong></a></p>")
            .test("[ссылка без выделения]<https://kgeorgiy.info>", "<p><a href='https://kgeorgiy.info'>ссылка без выделения</a></p>")
            .test("[ссылка без выделения]<https://hello__kgeorgiy.info>", "<p><a href='https://hello__kgeorgiy.info'>ссылка без выделения</a></p>")
            .test("_выделение [ссылка с __выделением__]<https://kgeorgiy.info>_", "<p><em>выделение <a href='https://kgeorgiy.info'>ссылка с <strong>выделением</strong></a></em></p>")
            .spoiled("Это не [__ссылка__]<https://hello_kgeorgiy_.info>", "<p>Это не [<strong>ссылка</strong>]&lt;https://hello<em>kgeorgiy</em>.info&gt;</p>", "[", "]", "<", ">")
            .addElement("a", "[", (checker, markup, input, output) -> {
                final StringBuilder in = new StringBuilder();
                final StringBuilder out = new StringBuilder();
                checker.generate(markup, in, out);

                final StringBuilder href = new StringBuilder();
                checker.generate(markup, href, new StringBuilder());

                input.append("[").append(in).append("]<").append(href).append('>');
                output.append("<a href='").append(href).append("'>").append(out).append("</a>");
            });

    // === Common

    public static final Selector SELECTOR = Selector.composite(Md2HtmlTest.class, c -> new Md2HtmlTester(), Md2HtmlTester::test)
            .variant("Base")
            .variant("Image", IMAGE)
            .variant("Link", LINK)
            .variant("InsDel", INS, DEL)
            .variant("Underline", UNDERLINE)
            .variant("Mark", MARK)
            .selector();

    private Md2HtmlTest() {
    }

    public static void main(final String... args) {
        SELECTOR.main(args);
    }
}
