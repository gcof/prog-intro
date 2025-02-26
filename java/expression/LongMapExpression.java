package expression;

import base.Asserts;
import base.ExtendedRandom;
import base.Pair;
import base.TestCounter;
import expression.common.ExpressionKind;
import expression.common.Type;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
@FunctionalInterface
@SuppressWarnings("ClassReferencesSubclass")
public interface LongMapExpression extends ToMiniString {
    long evaluateL(Map<String, Long> variables);

    // Tests follow. You may temporarily remove everything til the end.

    Add EXAMPLE = new Add(
            new Subtract(new Variable("xx"), new Const(1L)),
            new Multiply(new Variable("yy"), new Const(10L))
    );

    Type<Long> TYPE = new Type<>(Long::valueOf, random -> random.getRandom().nextLong(), long.class);
    String LETTERS = ExtendedRandom.ENGLISH + ExtendedRandom.ENGLISH.toUpperCase().substring(1, 20);
    ExpressionKind<LongMapExpression, Long> KIND = new ExpressionKind<>(
            TYPE,
            LongMapExpression.class,
            (r, c) -> Stream.generate(() -> r.randomString(LETTERS))
                    .filter(name -> name.chars().anyMatch(Character::isLowerCase))
                    .filter(name -> name.chars().anyMatch(Character::isUpperCase))
                    .distinct()
                    .limit(c)
                    .map(name -> Pair.<String, LongMapExpression>of(name, new Variable(name)))
                    .collect(Collectors.toUnmodifiableList()),
            (expr, variables, values) -> expr.evaluateL(
                    IntStream.range(0, variables.size())
                            .boxed()
                            .collect(Collectors.toMap(variables::get, values::get, (a, b) -> a, LinkedHashMap::new)))
    );

    @SuppressWarnings({"PointlessArithmeticExpression", "Convert2MethodRef"})
    static ExpressionTester<?, ?> tester(final TestCounter counter) {
        Asserts.assertEquals("Example toString()", "((xx - 1) + (yy * 10))", EXAMPLE.toString());
        Asserts.assertEquals(EXAMPLE + " at (2, 3)", 31L, EXAMPLE.evaluateL(Map.of("xx", 2L, "yy", 3L)));

        return new ExpressionTester<>(
                counter, KIND, c -> v -> c,
                (op, a, b) -> v -> op.apply(a.evaluateL(v), b.evaluateL(v)),
                (a, b) -> a + b, (a, b) -> a - b, (a, b) -> a * b, (a, b) -> a / b
        )
                .basicF("10", "10", v -> 10, v -> c(10))
                .basicF("$x", "$x", LongMapExpression::x, v -> vx(v))
                .basicF("$y", "$y", LongMapExpression::y, v -> vy(v))
                .basicF("($x + $y)", "$x + $y", v -> x(v) + y(v), v -> new Add(vx(v), vy(v)))
                .basicF("($x + 2)", "$x + 2", v -> x(v) + 2, v -> new Add(vx(v), c(2)))
                .basicF("(2 - $x)", "2 - $x", v -> 2 - x(v), v -> new Subtract(c(2), vx(v)))
                .basicF("(3 * $x)", "3 * $x", v -> 3 * x(v), v -> new Multiply(c(3), vx(v)))
                .basicF("($x + $x)", "$x + $x", v -> x(v) + x(v), v -> new Add(vx(v), vx(v)))
                .basicF("($x / -2)", "$x / -2", v -> -x(v) / 2, v -> new Divide(vx(v), c(-2)))
                .basicF("(2 + $x)", "2 + $x", v -> 2 + x(v), v -> new Add(c(2), vx(v)))
                .basicF("((1 + 2) + 3)", "1 + 2 + 3", v -> 6, v -> new Add(new Add(c(1), c(2)), c(3)))
                .basicF("(1 + (2 + 3))", "1 + 2 + 3", v -> 6, v -> new Add(c(1), new Add(c(2), c(3))))
                .basicF("((1 - 2) - 3)", "1 - 2 - 3", v -> -4, v -> new Subtract(new Subtract(c(1), c(2)), c(3)))
                .basicF("(1 - (2 - 3))", "1 - (2 - 3)", v -> 2, v -> new Subtract(c(1), new Subtract(c(2), c(3))))
                .basicF("((1 * 2) * 3)", "1 * 2 * 3", v -> 6, v -> new Multiply(new Multiply(c(1), c(2)), c(3)))
                .basicF("(1 * (2 * 3))", "1 * 2 * 3", v -> 6, v -> new Multiply(c(1), new Multiply(c(2), c(3))))
                .basicF("((10 / 2) / 3)", "10 / 2 / 3", v -> 10 / 2 / 3, v -> new Divide(new Divide(c(10), c(2)), c(3)))
                .basicF("(10 / (3 / 2))", "10 / (3 / 2)", v -> 10 / (3 / 2), v -> new Divide(c(10), new Divide(c(3), c(2))))
                .basicF("(10 * (3 / 2))", "10 * (3 / 2)", v -> 10 * (3 / 2), v -> new Multiply(c(10), new Divide(c(3), c(2))))
                .basicF("(10 + (3 - 2))", "10 + 3 - 2", v -> 10 + (3 - 2), v -> new Add(c(10), new Subtract(c(3), c(2))))
                .basicF(
                        "(($x * $x) + (($x - 1) / 10))",
                        "$x * $x + ($x - 1) / 10",
                        v -> x(v) * x(v) + (x(v) - 1) / 10,
                        v -> new Add(new Multiply(vx(v), vx(v)), new Divide(new Subtract(vx(v), c(1)), c(10)))
                )
                .basicF(
                        "($x * -1000000000)",
                        "$x * -1000000000",
                        v -> x(v) * -1_000_000_000,
                        v -> new Multiply(vx(v), c(-1_000_000_000))
                )
                .basicF("(10 / $x)", "10 / $x", v -> 10 / x(v), v -> new Divide(c(10), vx(v)))
                .basicF("($x / $x)", "$x / $x", v -> x(v) / x(v), v -> new Divide(vx(v), vx(v)))

                .advancedF("(2 + 1)", "2 + 1", v -> 2 + 1, v -> new Add(c(2), c(1)))
                .advancedF("($x - 1)", "$x - 1", v -> x(v) - 1, v -> new Subtract(vx(v), c(1)))
                .advancedF("(1 * 2)", "1 * 2", v -> 1 * 2, v -> new Multiply(c(1), c(2)))
                .advancedF("($x / 1)", "$x / 1", v -> x(v) / 1, v -> new Divide(vx(v), c(1)))
                .advancedF("(1 + (2 + 1))", "1 + 2 + 1", v -> 1 + 2 + 1, v -> new Add(c(1), new Add(c(2), c(1))))
                .advancedF(
                        "($x - ($x - 1))",
                        "$x - ($x - 1)",
                        v -> x(v) - (x(v) - 1),
                        v -> new Subtract(vx(v), new Subtract(vx(v), c(1)))
                )
                .advancedF(
                        "(2 * ($x / 1))",
                        "2 * ($x / 1)",
                        v -> 2 * (x(v) / 1),
                        v -> new Multiply(c(2), new Divide(vx(v), c(1)))
                )
                .advancedF(
                        "(2 / ($x - 1))",
                        "2 / ($x - 1)",
                        v -> 2 / (x(v) - 1),
                        v -> new Divide(c(2), new Subtract(vx(v), c(1)))
                )
                .advancedF(
                        "((1 * 2) + $x)",
                        "1 * 2 + $x",
                        v -> 1 * 2 + x(v),
                        v -> new Add(new Multiply(c(1), c(2)), vx(v))
                )
                .advancedF(
                        "(($x - 1) - 2)",
                        "$x - 1 - 2",
                        v -> x(v) - 1 - 2,
                        v -> new Subtract(new Subtract(vx(v), c(1)), c(2))
                )
                .advancedF(
                        "(($x / 1) * 2)",
                        "$x / 1 * 2",
                        v -> x(v) / 1 * 2,
                        v -> new Multiply(new Divide(vx(v), c(1)), c(2))
                )
                .advancedF("((2 + 1) / 1)", "(2 + 1) / 1", v -> (2 + 1) / 1, v -> new Divide(new Add(c(2), c(1)), c(1)))
                .advancedF(
                        "(1 + (1 + (2 + 1)))",
                        "1 + 1 + 2 + 1",
                        v -> 1 + 1 + 2 + 1,
                        v -> new Add(c(1), new Add(c(1), new Add(c(2), c(1))))
                )
                .advancedF(
                        "($x - ((1 * 2) + $x))",
                        "$x - (1 * 2 + $x)",
                        v -> x(v) - (1 * 2 + x(v)),
                        v -> new Subtract(vx(v), new Add(new Multiply(c(1), c(2)), vx(v)))
                )
                .advancedF(
                        "($x * (2 / ($x - 1)))",
                        "$x * (2 / ($x - 1))",
                        v -> x(v) * (2 / (x(v) - 1)),
                        v -> new Multiply(vx(v), new Divide(c(2), new Subtract(vx(v), c(1))))
                )
                .advancedF(
                        "($x / (1 + (2 + 1)))",
                        "$x / (1 + 2 + 1)",
                        v -> x(v) / (1 + 2 + 1),
                        v -> new Divide(vx(v), new Add(c(1), new Add(c(2), c(1))))
                )
                .advancedF(
                        "((1 * 2) + (2 + 1))",
                        "1 * 2 + 2 + 1",
                        v -> 1 * 2 + 2 + 1,
                        v -> new Add(new Multiply(c(1), c(2)), new Add(c(2), c(1)))
                )
                .advancedF(
                        "((2 + 1) - (2 + 1))",
                        "2 + 1 - (2 + 1)",
                        v -> 2 + 1 - (2 + 1),
                        v -> new Subtract(new Add(c(2), c(1)), new Add(c(2), c(1)))
                )
                .advancedF(
                        "(($x - 1) * ($x / 1))",
                        "($x - 1) * ($x / 1)",
                        v -> (x(v) - 1) * (x(v) / 1),
                        v -> new Multiply(new Subtract(vx(v), c(1)), new Divide(vx(v), c(1)))
                )
                .advancedF(
                        "(($x - 1) / (1 * 2))",
                        "($x - 1) / (1 * 2)",
                        v -> (x(v) - 1) / (1 * 2),
                        v -> new Divide(new Subtract(vx(v), c(1)), new Multiply(c(1), c(2)))
                )
                .advancedF(
                        "((($x - 1) - 2) + $x)",
                        "$x - 1 - 2 + $x",
                        v -> x(v) - 1 - 2 + x(v),
                        v -> new Add(new Subtract(new Subtract(vx(v), c(1)), c(2)), vx(v))
                )
                .advancedF(
                        "(((1 * 2) + $x) - 1)",
                        "1 * 2 + $x - 1",
                        v -> 1 * 2 + x(v) - 1,
                        v -> new Subtract(new Add(new Multiply(c(1), c(2)), vx(v)), c(1))
                )
                .advancedF(
                        "(((2 + 1) / 1) * $x)",
                        "(2 + 1) / 1 * $x",
                        v -> (2 + 1) / 1 * x(v),
                        v -> new Multiply(new Divide(new Add(c(2), c(1)), c(1)), vx(v))
                )
                .advancedF(
                        "((2 / ($x - 1)) / 2)",
                        "2 / ($x - 1) / 2",
                        v -> 2 / (x(v) - 1) / 2,
                        v -> new Divide(new Divide(c(2), new Subtract(vx(v), c(1))), c(2))
                );
    }

    private static Variable vx(final List<String> vars) {
        return new Variable(vars.get(0));
    }

    private static Variable vy(final List<String> vars) {
        return new Variable(vars.get(1));
    }

    private static long x(final Map<String, Long> vars) {
        return vars.values().iterator().next();
    }

    private static long y(final Map<String, Long> vars) {
        return vars.values().stream().skip(1).findFirst().orElseThrow();
    }


    private static Const c(final long v) {
        return TYPE.constant(v(v));
    }

    private static long v(final long v) {
        return v;
    }

    static void main(final String... args) {
        TripleExpression.SELECTOR
                .variant("LongMap", ExpressionTest.v(LongMapExpression::tester))
                .main(args);
    }
}
