import java.util.ArrayList;
import java.util.Iterator;
import java.lang.StringBuilder;
import java.io.Reader;
import java.io.IOException;

// Turns the input into an iterator of tokens.
public class Tokenizer {
    private static final int FIRST_LINE = 1;
    private static final int FIRST_COL = 1;

    private ArrayList<Token> tokens;

    private Reader input;
    private char current;
    private char peek;

    private int line;
    private int col;
    private int startcol;

    public Tokenizer(Reader input) {
        tokens = new ArrayList<Token>();
        line = FIRST_LINE;
        col = FIRST_COL;
        startcol = FIRST_COL;

        try {
            this.input = input;

            step(); // initialize current and peek
            step();

            while (current != '\u0000') { // main loop
                startcol = col;
                handleChar();
                step();
            }

            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public Peekable<Token> getTokens() {
        return new Peekable<Token>() {
            private Token prev = null;
            private Iterator<Token> iter = tokens.iterator();

            public Token next() {
                if (prev == null) {
                    return iter.next();
                } else {
                    Token temp = prev;
                    prev = null;
                    return temp;
                }
            }

            public Token peek() {
                if (prev == null) {
                    prev = iter.next();
                    return prev;
                } else {
                    return prev;
                }
            }

            public boolean hasNext() {
                return iter.hasNext() || prev != null;
            }
        };
    }

    // A single step in tokenizing. Advances peek and current 1 character
    private void step() {
        current = peek;

        if (current == '\n') {
            line++;
            col = FIRST_COL - 1;
        } else {
            col++;
        }

        int ch = -1;
        
        try {
            ch = input.read();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (ch != -1)
            peek = (char) ch;
        else
            peek = '\u0000';
    }

    // Handles each character case
    private void handleChar() {
        switch (current) {
            case '(':
                addToken(Token.TokenType.LPARENS, "(");
                break;
            case ')':
                addToken(Token.TokenType.RPARENS, ")");
                break;
            case '*': case '=':
                addToken(Token.TokenType.SYMBOL,
                                new String(new char[] {current}));
                break;
           case ';':
                addToken(Token.TokenType.SEMICOLON, ";");
                break;
            case '<': case '>': case '!':
                if (peek == '=') {
                    addToken(Token.TokenType.SYMBOL,
                                new String(new char[] {current, '='}));
                    step();
                } else addToken(Token.TokenType.SYMBOL, current + "");
                break;
            case '/':
                if (peek == '*') {
                    handleComment();
                } else addToken(Token.TokenType.SYMBOL, "/");
                break;
            case '\'': case '"':
                handleString(current);
                break;
            default:
                if (Character.isWhitespace(current)) {
                    break;
                } else if (Character.isLetter(current)
                        || current == '_') {
                    handleIdent();
                    break;
                } else if (Character.isDigit(current)) {
                    handleNumber(false);
                    break;
                } else if (current == '.') {
                    handleNumber(true);
                    break;
                }
                break;
        }
    }

    // Skip comments
    private void handleComment() {
        step();
        step();

        while (current != '\u0000') {
            if (current == '*' && peek == '/') {
                step();
                break;
            }
            step();
        }
    }

    // Create a full identifier by creating a token up to the terminal symbol
    private void handleString(char terminal) {
        StringBuilder builder = new StringBuilder();

        step();

        while (current != terminal && current != '\u0000') {
            builder.append(current);
            step();
        }

        addToken(Token.TokenType.STRING, builder.toString());
    }

    // Everything else is an identifier
    private void handleIdent() {
        StringBuilder builder = new StringBuilder();
        builder.append(current);

        while (Character.isLetterOrDigit(peek) 
                || peek == '_' || peek == '.') {
            builder.append(peek);
            step();
        }

        addToken(Token.TokenType.IDENT, builder.toString());
    }

    private void handleNumber(boolean dotFlag) {
        boolean dateFlag = false;

        StringBuilder builder = new StringBuilder();
        builder.append(current);

        while (true) {
            if (Character.isDigit(peek)) {
                builder.append(peek);
            } else if (peek == '.') {
                if (dotFlag || dateFlag) {
                } else {
                    builder.append(peek);
                    dotFlag = true;
                }
            } else if (peek == ':') {
                if (dotFlag) {
                } else {
                    builder.append(peek);
                    dateFlag = true;
                }
            } else {
                break;
            }

            step();
        }

        if (dateFlag) {
            addToken(Token.TokenType.STRING, builder.toString());
        } else if (dotFlag) {
            addToken(Token.TokenType.FLOAT, builder.toString());
        } else {
            addToken(Token.TokenType.NUMBER, builder.toString());
        }
    }

    private void addToken(Token.TokenType type, String value) {
        tokens.add(new Token(type, value, line, startcol));
    }
}

