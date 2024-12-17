package expression.parser;

import expression.*;

public class ExpressionParser implements TripleParser {
    @Override
    public TripleExpression parse(String expression) {
        return parse(new StringSource(expression));
    }

    private static ExpressionNode parse(CharSource source) {
        return new ExpressionParserSupport(source).parseExpression(true);
    }

    private static class ExpressionParserSupport extends BaseParser {
        public ExpressionParserSupport(CharSource source) {
            super(source);
        }

        public ExpressionNode parseExpression(boolean isTop) {
            skipWhitespaces();
            final ExpressionNode result = parseAddSub();
            skipWhitespaces();
            if (!isTop || eof()) {
                return result;
            }
            throw error("End of expression expected");
        }

        private ExpressionNode parseAddSub() {
            skipWhitespaces();
            ExpressionNode result = parseMulDiv();
            while (true) {
                skipWhitespaces();
                if (take('+')) {
                    result = new Add(result, parseMulDiv());
                } else if (take('-')) {
                    result = new Subtract(result, parseMulDiv());
                } else {
                    return result;
                }
                skipWhitespaces();
            }
        }

        private ExpressionNode parseMulDiv() {
            skipWhitespaces();
            ExpressionNode result = parsePowLog();
            while (true) {
                skipWhitespaces();
                if (!peek(2).equals("**") && peek(1).equals("*")) {
                    take('*');
                    result = new Multiply(result, parsePowLog());
                } else if (!peek(2).equals("//") && peek(1).equals("/")) {
                    take('/');
                    result = new Divide(result, parsePowLog());
                } else {
                    return result;
                }
                skipWhitespaces();
            }
        }

        private ExpressionNode parsePowLog() {
            skipWhitespaces();
            ExpressionNode result = parseMultiplier(false);
            while (true) {
                skipWhitespaces();
                if (take("//")) {
                    result = new Log(result, parseMultiplier(false));
                } else if (take("**")) {
                    result = new Pow(result, parseMultiplier(false));
                } else {
                    return result;
                }
                skipWhitespaces();
            }
        }


        private ExpressionNode parseMultiplier(boolean inRecursion) {
            ExpressionNode result;
            skipWhitespaces();
            if (between('a', 'z') || between('A', 'Z')) {
                result = parseVariable();
            } else if (take('-')) {
                if (between('0', '9')) {
                    result = parseConst(true);
                } else {
                    result = new UnaryMinus(parseMultiplier(true));
                }
            } else if (between('0', '9')) {
                result = parseConst(false);
            } else if (take('(')) {
                result = parseExpression(false);
                expect(')');
            } else {
                throw error("Invalid expression in multiplier");
            }
            if (!inRecursion) {
                skipWhitespaces();
                while (take('!')) {
                    result = new FactorialAbs(result);
                    skipWhitespaces();
                }
                skipWhitespaces();
            }
            return result;
        }

        private ExpressionNode parseConst(boolean negative) {
            final StringBuilder sb = new StringBuilder(negative ? "-" : "");
            takeInteger(sb);
            return new Const(Integer.parseInt(sb.toString()));
        }

        private void takeInteger(final StringBuilder sb) {
            if (take('-')) {
                sb.append('-');
            }
            if (take('0')) {
                sb.append('0');
            } else if (between('1', '9')) {
                takeDigits(sb);
            } else {
                throw error("Invalid number");
            }
        }

        private void takeDigits(final StringBuilder sb) {
            while (between('0', '9')) {
                sb.append(take());
            }
        }

        private ExpressionNode parseVariable() {
            final String name = parseName();
            return new Variable(name);
        }

        private String parseName() {
            final StringBuilder sb = new StringBuilder();
            takeLetters(sb);
            if (sb.isEmpty()) {
                throw error("Variable expected");
            }
            return sb.toString();
        }

        private void takeLetters(final StringBuilder sb) {
            while (between('a', 'z') || between('A', 'Z')) {
                sb.append(take());
            }
        }

        private void skipWhitespaces() {
            while (takeWhitespace()) {
                // skip, take skips whitespaces by himself
            }
        }

    }

}
