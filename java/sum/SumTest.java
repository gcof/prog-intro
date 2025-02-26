package sum;

import base.*;

import java.math.BigInteger;
import java.util.Locale;
import java.util.function.*;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public final class SumTest {
    // === Base

    @FunctionalInterface
    /* package-private */ interface Op<T extends Number> extends UnaryOperator<SumTester<T>> {}

    private static final BiConsumer<Number, String> TO_STRING = (expected, out) -> Asserts.assertEquals("Sum", expected.toString(), out);

    private static final Named<Supplier<SumTester<Integer>>> BASE = Named.of("", () -> new SumTester<>(
            Integer::sum, n -> (int) n, (r, max) -> r.nextInt() % max, TO_STRING,
            10, 100, Integer.MAX_VALUE
    ));

    /* package-private */ static <T extends Number> Named<Op<T>> plain() {
        return Named.of("", test -> test);
    }

    // === Long

    private static final Named<Supplier<SumTester<Long>>> LONG = Named.of("Long", () -> new SumTester<>(
            Long::sum, n -> n, (r, max) -> r.getRandom().nextLong() % max, TO_STRING,
            10L, 100L, (long) Integer.MAX_VALUE, Long.MAX_VALUE)
            .test(12345678901234567L, " +12345678901234567 ")
            .test(0L, " +12345678901234567 -12345678901234567")
            .test(0L, " +12345678901234567 -12345678901234567"));

    // === Punct

    private static <T extends Number> Named<Op<T>> compose(
            final String prefix,
            final Named<Op<T>> inner,
            final Op<T> outer
    ) {
        return Named.of(prefix + inner.name(), t -> outer.apply(inner.value().apply(t)));
    }

    private static <T extends Number> Named<Op<T>> punct(final Named<Op<T>> inner) {
        //noinspection UnnecessaryUnicodeEscape
        return compose("Punct", inner, t -> t.addSpaces(
                "([{)]}",
                "\u0F3A\u0F3C\u169B\u201A\u201E\u2045\u207D\u208D\u2308\u230A\u2329\u2768\u276A" +
                        "\u276C\u276E\u2770\u2772\u2774\u27C5\u27E6\u27E8\u27EA\u27EC\u27EE\u2983\u2985" +
                        "\u2987\u2989\u298B\u298D\u298F\u2991\u2993\u2995\u2997\u29D8\u29DA\u29FC\u2E22" +
                        "\u2E24\u2E26\u2E28\u2E42\u3008\u300A\u300C\u300E\u3010\u3014\u3016\u3018\u301A" +
                        "\u301D\uFD3F\uFE17\uFE35\uFE37\uFE39\uFE3B\uFE3D\uFE3F\uFE41\uFE43\uFE47\uFE59" +
                        "\uFE5B\uFE5D\uFF08\uFF3B\uFF5B\uFF5F\uFF62",
                "\u0F3B\u0F3D\u169C\u2046\u207E\u208E\u2309\u230B\u232A\u2769\u276B\u276D\u276F" +
                        "\u2771\u2773\u2775\u27C6\u27E7\u27E9\u27EB\u27ED\u27EF\u2984\u2986\u2988\u298A" +
                        "\u298C\u298E\u2990\u2992\u2994\u2996\u2998\u29D9\u29DB\u29FD\u2E23\u2E25\u2E27" +
                        "\u2E29\u3009\u300B\u300D\u300F\u3011\u3015\u3017\u3019\u301B\u301E\u301F\uFD3E" +
                        "\uFE18\uFE36\uFE38\uFE3A\uFE3C\uFE3E\uFE40\uFE42\uFE44\uFE48\uFE5A\uFE5C\uFE5E" +
                        "\uFF09\uFF3D\uFF5D\uFF60\uFF63"
        ));
    }

    // === BigInteger

    private static final Named<Supplier<SumTester<BigInteger>>> BIG_INTEGER = Named.of("BigInteger", () -> new SumTester<>(
            BigInteger::add, BigInteger::valueOf, (r, max) -> new BigInteger(max.bitLength(), r.getRandom()), TO_STRING,
            BigInteger.TEN, BigInteger.TEN.pow(10), BigInteger.TEN.pow(100), BigInteger.TWO.pow(1000))
            .test(0, "10000000000000000000000000000000000000000 -10000000000000000000000000000000000000000"));


    // === Hex

    private static <T extends Number> Named<Op<T>> hex(final Function<T, String> toHex) {
        //noinspection StringConcatenationMissingWhitespace
        return Named.of("Hex", test -> test
                .test(1, "0x1")
                .test(0x1a, "0x1a")
                .test(0xA2, "0xA2")
                .testSpaces(62, " 0X0 0X1 0XF 0XF 0x0 0x1 0xF 0xf")
                .test(0x12345678, "0x12345678")
                .test(0x09abcdef, "0x09abcdef")
                .test(0x3CafeBab, "0x3CafeBab")
                .test(0x3DeadBee, "0x3DeadBee")

                .test(Integer.MAX_VALUE, "0" + Integer.MAX_VALUE)
                .test(Integer.MIN_VALUE, "" + Integer.MIN_VALUE)
                .test(Integer.MAX_VALUE, "0x" + Integer.toHexString(Integer.MAX_VALUE))
                .setToString(number -> {
                    final int hashCode = number.hashCode();
                    if ((hashCode & 1) == 0) {
                        return number.toString();
                    }

                    final String lower = "0x" + toHex.apply(number).toLowerCase(Locale.ROOT);
                    return (hashCode & 2) == 0 ? lower : lower.toUpperCase(Locale.ROOT);
                })
        );
    }


    // === Float

    private static BiConsumer<Number, String> approximate(final Function<String, Number> parser, final double precision) {
        return (expected, out) ->
                Asserts.assertEquals("Sum", expected.doubleValue(), parser.apply(out).doubleValue(), precision);
    }

    private static final Named<Supplier<SumTester<Float>>> FLOAT = Named.of("Float", () -> new SumTester<>(
            Float::sum, n -> (float) n, (r, max) -> (r.getRandom().nextFloat() - 0.5f) * 2 * max,
            approximate(Float::parseFloat, 1e-5),
            10.0f, 0.01f, 1e20f, Float.MAX_VALUE / 10000)
            .test(5, "2.5 2.5")
            .test(0, "1e10 -1e10")
            .testT(2e10f, "1.5e10 0.5E10"));

    // === Common

    /* package-private */ static <T extends Number> Consumer<TestCounter> variant(
            final Named<Function<String, Runner>> runner,
            final Named<Supplier<SumTester<T>>> test,
            final Named<? extends Function<? super SumTester<T>, ? extends SumTester<?>>> modifier
    ) {
        return counter -> modifier.value().apply(test.value().get())
                .test("Sum" + test.name() + modifier.name() + runner.name(), counter, runner.value());
    }

    /* package-private */ static final Named<Function<String, Runner>> RUNNER =
            Named.of("", Runner.packages("", "sum")::args);

    public static final Selector SELECTOR = selector(SumTest.class, RUNNER);

    private SumTest() {
        // Utility class
    }

    public static Selector selector(final Class<?> owner, final Named<Function<String, Runner>> runner) {
        return new Selector(owner)
                .variant("Base",            variant(runner, BASE, plain()))
                .variant("LongPunct",       variant(runner, LONG, punct(plain())))
                .variant("BigIntegerPunct", variant(runner, BIG_INTEGER, punct(plain())))
                .variant("LongPunctHex",    variant(runner, LONG, punct(hex(Long::toHexString))))
                .variant("Float",           variant(runner, FLOAT, plain()))
                .variant("FloatPunct",      variant(runner, FLOAT, punct(plain())))
                ;
    }

    public static void main(final String... args) {
        SELECTOR.main(args);
    }
}
