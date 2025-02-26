package reverse;

import base.ExtendedRandom;
import base.Named;
import base.Selector;
import base.TestCounter;
import reverse.ReverseTester.Op;

import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.IntToLongFunction;
import java.util.function.LongBinaryOperator;
import java.util.function.LongUnaryOperator;
import java.util.stream.IntStream;

import static reverse.ReverseTest.M;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public final class FastReverseTest {
    // === Max

    private static final Named<Op> MAX = max("", a -> a, Integer.MIN_VALUE);
    private static final Named<Op> MAX_ABS = max("Abs", Math::abs, 0);
    private static final Named<Op> MAX_MOD = max("Mod", a -> (a % M + M) % M, 0);
    private static final Named<Op> MAX_ABS_MOD = max("AbsMod", a -> Math.abs(a) % M, 0);

    private static Named<Op> max(final String suffix, final LongUnaryOperator by, final int zero) {
        final LongBinaryOperator max = (a, b) -> by.applyAsLong(a) > by.applyAsLong(b) ? a : b;
        return Named.of("Max" + suffix, ints -> {
            final IntToLongFunction map = n -> n;
            // This code is intentionally obscure
            final long[] rt = Arrays.stream(ints)
                    .map(Arrays::stream)
                    .mapToLong(row -> row.mapToLong(map).reduce(zero, max))
                    .toArray();
            final long[] ct = new long[Arrays.stream(ints).mapToInt(r -> r.length).max().orElse(0)];
            Arrays.fill(ct, zero);
            Arrays.stream(ints).forEach(r1 -> IntStream.range(0, r1.length)
                    .forEach(i -> ct[i] = max.applyAsLong(ct[i], map.applyAsLong(r1[i]))));
            return IntStream.range(0, ints.length)
                    .mapToObj(r1 -> IntStream.range(0, ints[r1].length)
                            .mapToLong(c1 -> max.applyAsLong(rt[r1], ct[c1]))
                            .toArray())
                    .toArray(long[][]::new);
        });
    }

    // === Octal

    private static final Named<BiFunction<ExtendedRandom, Integer, String>> OCT_IN = Named.of("Oct", (r, i) -> Integer.toOctalString(i));
    private static final Named<BiFunction<ExtendedRandom, Integer, String>> OCT_OUT = Named.of("Oct", (r, i) -> Integer.toOctalString(i) + "o");
    private static final Named<BiFunction<ExtendedRandom, Integer, String>> OCT_DEC = Named.of("OctDec", (r, i) ->
            r.nextBoolean() ? Integer.toString(i) : toOctalString(r, i));

    private static String toOctalString(final ExtendedRandom r, final Integer i) {
        return Integer.toOctalString(i) + (r.nextBoolean() ? "o" : "O");
    }

    // === Common

    public static final int MAX_SIZE = 1_000_000 / TestCounter.DENOMINATOR;
    public static final Selector SELECTOR = ReverseTest.selector(FastReverseTest.class, MAX_SIZE)
            .variant("MaxAbsOctDec",    ReverseTester.variant(MAX_SIZE, "", MAX_ABS, OCT_DEC, OCT_OUT))
            .variant("MaxModOctDec",    ReverseTester.variant(MAX_SIZE, "", MAX_MOD, OCT_DEC, OCT_OUT))
            .variant("MaxAbsModOctDec", ReverseTester.variant(MAX_SIZE, "", MAX_ABS_MOD, OCT_DEC, OCT_OUT))
            .variant("OddOct",          ReverseTester.variant(MAX_SIZE, "", ReverseTest.ODD, OCT_IN, OCT_IN))
            .variant("MaxOct",          ReverseTester.variant(MAX_SIZE, "", MAX, OCT_IN, OCT_IN))
            ;

    private FastReverseTest() {
    }

    public static void main(final String... args) {
        SELECTOR.main(args);
    }
}
