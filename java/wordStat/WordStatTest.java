package wordStat;

import base.Named;
import base.Pair;
import base.Selector;

import java.util.Comparator;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Tests for <a href="https://www.kgeorgiy.info/courses/prog-intro/homeworks.html#wordstat">Word Statistics</a> homework
 * of <a href="https://www.kgeorgiy.info/courses/prog-intro/">Introduction to Programming</a> course.
 *
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public final class WordStatTest {
    // === Base
    private static final Named<Function<String, Stream<String>>> ID  = Named.of("", Stream::of);
    private static final Named<Comparator<Pair<String, Integer>>> INPUT = Named.of("Input", Comparator.comparingInt(p -> 0));


    // === Words
    private static final Named<Comparator<Pair<String, Integer>>> WORDS =
            Named.of("Words", Comparator.comparing(Pair<String, Integer>::first).reversed());

    // === Suffix
    public static final int SIZE = 3;

    static Named<Function<String, Stream<String>>> length(
            final String name,
            final int length,
            final Function<String, Stream<String>> lng,
            final Function<String, Stream<String>> shrt
    ) {
        return Named.of(name, s -> (s.length() >= length ? lng : shrt).apply(s));
    }

    private static final Named<Function<String, Stream<String>>> SUFFIX =
            length("Suffix", SIZE, s -> Stream.of(s.substring(s.length() - SIZE)), Stream::of);


    // === Shingles
    private static final Named<Function<String, Stream<String>>> SHINGLES =
            length("Shingles", SIZE, s -> IntStream.rangeClosed(0, s.length() - SIZE)
                    .mapToObj(i -> s.substring(i, i + SIZE)), Stream::of);


    // === Middle
    private static final Named<Function<String, Stream<String>>> MIDDLE =
            length("Middle", SIZE * 2 + 1, s -> Stream.of(s.substring(SIZE, s.length() - SIZE)), Stream::of);

    // === Prefix
    private static final Named<Function<String, Stream<String>>> PREFIX =
            length("Prefix", SIZE, s -> Stream.of(s.substring(0, SIZE)), Stream::of);


    // === Common
    public static final Selector SELECTOR = new Selector(WordStatTester.class)
            .variant("Base",            WordStatTester.variant(INPUT, ID))
            .variant("WordsSuffix",     WordStatTester.variant(WORDS, SUFFIX))
            .variant("WordsShingles",   WordStatTester.variant(WORDS, SHINGLES))
            .variant("WordsMiddle",     WordStatTester.variant(WORDS, MIDDLE))
            .variant("Words",           WordStatTester.variant(WORDS, ID))
            .variant("WordsPrefix",     WordStatTester.variant(WORDS, PREFIX))
            ;

    private WordStatTest() {
        // Utility class
    }

    public static void main(final String... args) {
        SELECTOR.main(args);
    }
}
