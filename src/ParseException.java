public class ParseException extends Exception {
    private Token.TokenType last;

    public ParseException(Token.TokenType expected, Token.TokenType last,
            int line, int col) {
        this("Expected " + expected.toString() + ", got " + last.toString(),
                last, line, col);
    }

    public ParseException(String msg, Token.TokenType last,
            int line, int col) {
        super("Line " + line +", Char " + col + ": " + msg);
        this.last = last;
    }

    public Token.TokenType lastToken() {
        return this.last;
    }
}
