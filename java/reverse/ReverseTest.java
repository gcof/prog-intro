package reverse;

import base.Named;
import base.Selector;
import base.TestCounter;
import reverse.ReverseTester.Op;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.IntToLongFunction;
import java.util.function.LongBinaryOperator;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Tests for {@code Reverse} homework.
 *
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public final class ReverseTest {
    // === Transpose
    private static final Named<Op> TRANSPOSE = Named.of("Transp", ints -> {
        final List<int[]> rows = new ArrayList<>(List.of(ints));
        return IntStream.range(0, Arrays.stream(ints).mapToInt(r -> r.length).max().orElse(0))
                .mapToObj(c -> {
                    rows.removeIf(r -> r.length <= c);
                    return rows.stream().mapToLong(r -> r[c]).filter(v -> (v & 1) == 1).toArray();
                })
                .toArray(long[][]::new);
    });

    // === Odd

    /* package-private */ static final Named<Op> ODD = Named.of(
            "Odd",
            ints -> ReverseTester.transform(
                    Stream.of(ints)
                            .map(row -> Arrays.stream(row).filter(v -> (v & 1) == 1))
                            .map(IntStream::toArray)
                            .toArray(int[][]::new)
            )
    );

    // === SumMod

    /* package-private */ static final int M = 1_000_000_007;

    private static final Named<Op> SUM_MOD = cross(
            "SumMod",
            (a, b) -> (a + (b % M + M) % M) % M,
            (r, c, v) -> ((r + c) % M + (-v % M + M) % M) % M
    );

    // === SumAbsMod

    private static final Named<Op> SUM_ABS_MOD = cross(
            "SumAbsMod",
            (a, b) -> (a + Math.abs(b) % M) % M,
            (r, c, v) -> ((r + c) % M + ((M - Math.abs(v) % M) % M)) % M
    );

    // === SumAbs

    @FunctionalInterface
    /* package-private */ interface LongTernaryOperator {
        long applyAsLong(long a, long b, long c);
    }

    private static long[][] cross(
            final int[][] ints,
            final IntToLongFunction map,
            final LongBinaryOperator reduce,
            final LongTernaryOperator get
    ) {
        // This code is intentionally obscure
        final long[] rt = Arrays.stream(ints)
                .map(Arrays::stream)
                .mapToLong(row -> row.mapToLong(map).reduce(0, reduce))
                .toArray();
        final long[] ct = new long[Arrays.stream(ints).mapToInt(r -> r.length).max().orElse(0)];
        Arrays.fill(ct, 0);
        Arrays.stream(ints).forEach(r -> IntStream.range(0, r.length)
                .forEach(i -> ct[i] = reduce.applyAsLong(ct[i], map.applyAsLong(r[i]))));
        return IntStream.range(0, ints.length)
                .mapToObj(r -> IntStream.range(0, ints[r].length)
                        .mapToLong(c -> get.applyAsLong(rt[r], ct[c], ints[r][c]))
                        .toArray())
                .toArray(long[][]::new);
    }

    private static Named<Op> cross(final String name, final LongBinaryOperator reduce, final LongTernaryOperator get) {
        return Named.of(name, ints -> cross(ints, n -> n, reduce, get));
    }

    private static final Named<Op> SUM_ABS = cross(
            "SumAbs",
            (a, b) -> a + Math.abs(b),
            (r, c, v) -> r + c - Math.abs(v)
    );

    // === Base

    private static final Named<Op> REVERSE = Named.of("", ReverseTester::transform);

    // === Common

    public static final int MAX_SIZE = 10_000 / TestCounter.DENOMINATOR;

    public static final Selector SELECTOR = selector(ReverseTest.class, MAX_SIZE);

    private ReverseTest() {
        // Utility class
    }

    public static Selector selector(final Class<?> owner, final int maxSize) {
        return new Selector(owner)
                .variant("Base",        ReverseTester.variant(maxSize, REVERSE))
                .variant("SumAbs",      ReverseTester.variant(maxSize, SUM_ABS))
                .variant("SumMod",      ReverseTester.variant(maxSize, SUM_MOD))
                .variant("SumAbsMod",   ReverseTester.variant(maxSize, SUM_ABS_MOD))
                .variant("Odd",         ReverseTester.variant(maxSize, ODD))
                .variant("Transpose",   ReverseTester.variant(maxSize, TRANSPOSE))
                ;
    }

    public static void main(final String... args) {
        SELECTOR.main(args);
    }
}
