public class Token {
    private final TokenType type;
    private final String value;

    private final int line;
    private final int col;
    
    public Token(TokenType type, String value, int line, int col) {
        this.type = type;
        this.value = value;
        this.line = line;
        this.col = col;
    }

    public static enum TokenType {
        LPARENS, RPARENS,
        SEMICOLON,
        STRING,
        IDENT,
        NUMBER,
        FLOAT,
        SYMBOL
    }

    public boolean is(String str) {
        return this.value.equalsIgnoreCase(str);
    }

    public boolean is(TokenType type) {
        return this.type == type;
    }

    public boolean isValue() {
        return this.type == TokenType.STRING
            || this.type == TokenType.IDENT
            || this.type == TokenType.NUMBER
            || this.type == TokenType.FLOAT;
    }

    public String expect(String str) throws ParseException {
        if (this.is(str))
            return value;
        else
            throw new ParseException("Expected '" + str + "', got '"
                    + value + "'", type, line, col);
    }

    // Used in the parser, expects a specific type of token, general case
    public String expect(TokenType type) throws ParseException {
        if (this.is(type))
            return this.value;
        else
            throw new ParseException(type, this.type, line, col);
    }

    // Expect a value (STRING, IDENT, or Number)
    public String expectValue() throws ParseException {
        if (isValue())
            return value;
        else
            throw new ParseException("Expected VALUE, got " + type, type,
                    line, col);
    }

    // Throw a ParseException on this token
    public ParseException panic(String msg) {
        return new ParseException(msg, type, line, col);
    }

    @Override
    public String toString() {
        return value + "  -  " + type;
    }
}
