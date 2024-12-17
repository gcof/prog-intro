package expression.exceptions;

import expression.*;
import expression.parser.BaseParser;
import expression.parser.CharSource;
import expression.parser.StringSource;

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
            final ExpressionNode result = parseGcdLcm();
            skipWhitespaces();
            if (!isTop || eof()) {
                return result;
            }
            throw new ExpressionParseException("End of expression expected, but found " + (peek(1).equals("\0") ? "EOF" : peek(1)));
        }

        private ExpressionNode parseGcdLcm() {
            skipWhitespaces();
            ExpressionNode result;
            result = parseAddSub();
            while (true) {
                skipWhitespaces();
                if (takeIdentifier("gcd")) {
                    result = new CheckedGcd(result, parseAddSub());
                } else if (takeIdentifier("lcm")) {
                    result = new CheckedLcm(result, parseAddSub());
                } else {
                    return result;
                }
                skipWhitespaces();
            }
        }

        private ExpressionNode parseAddSub() {
            skipWhitespaces();
            ExpressionNode result = parseMulDiv();
            while (true) {
                skipWhitespaces();
                if (take('+')) {
                    result = new CheckedAdd(result, parseMulDiv());
                } else if (take('-')) {
                    result = new CheckedSubtract(result, parseMulDiv());
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
                    result = new CheckedMultiply(result, parsePowLog());
                } else if (!peek(2).equals("//") && peek(1).equals("/")) {
                    take('/');
                    result = new CheckedDivide(result, parsePowLog());
                }  else {
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
                    result = new CheckedNegate(parseMultiplier(true));
                }
            } else if (take('!')) {
                result = new CheckedPrefixFactorial(parseMultiplier(true));
            } else if (between('0', '9')) {
                result = parseConst(false);
            } else if (take('(')) {
                result = parseExpression(false);
                expect(')');
            } else {
                throw new ExpressionParseException("Expected argument in expression, but found " + (peek(1).equals("\0") ? "EOF" : peek(1)));
            }
            if(!inRecursion) {
                skipWhitespaces();
                while (take('!')) {
                    result = new CheckedFactorialAbs(result);
                    skipWhitespaces();
                }
                skipWhitespaces();
            }
            return result;
        }

        private ExpressionNode parseConst(boolean negative) {
            final StringBuilder sb = new StringBuilder(negative ? "-" : "");
            takeInteger(sb);
            try {
                return new Const(Integer.parseInt(sb.toString()));
            } catch (NumberFormatException e) {
                throw new ConstParseException("Invalid number, can't parse " + sb.toString());
            }
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
                throw new ConstParseException("Invalid number, digit expected after '-', found " + peek(1));
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
            char lastChar = sb.charAt(sb.length() - 1);
            if (!(lastChar == 'x' || lastChar == 'y' || lastChar == 'z')) {
                throw new VariableParseException("Incorrect variable name. Last character should be x, y or z, but was " + lastChar);
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
