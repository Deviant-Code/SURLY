import java.nio.ByteBuffer;
import java.lang.Math;

public class Value {
    private static final int NUMSIZE = 8;
    private static final int DEFAULT_DEC = 4;

    public final Type type;
    private ByteBuffer buf;

    public Value(Type type, String value) throws QueryException {
        this.type = type;

        if (type.isChar()) {
            buf = ByteBuffer.allocate(type.size);
            setChar(value);
        } else if (type.isNum()) {
            buf = ByteBuffer.allocate(NUMSIZE);
            setNum(value);
        } else {
            throw new QueryException("Unsupported type: " + type.toString());
        }
    }

    public Value(Attribute attr, String value) throws QueryException {
        this(attr.type, value);
    }

    public Value(double value) throws QueryException {
        this.type = Type.fromString("NUM", DEFAULT_DEC);

        buf = ByteBuffer.allocate(NUMSIZE);
        buf.putDouble(0, value);
    }

    // Set the internal value, assuming it is a NUM
    public void setChar(String value) throws QueryException {
        assertChar();

        byte chars[] = value.getBytes(Interpreter.CHARSET);

        int len = chars.length > buf.capacity() ? buf.capacity() : chars.length;

        buf.put(chars, 0, len);
    }

    // Get the internal value, assuming it is a CHAR
    public String getChar() throws QueryException {
        assertChar();

        buf.position(0);
        int len = 0;
        for (; len < buf.capacity() && buf.get(len) != 0; len++);

        return new String(buf.array(), 0, len, Interpreter.CHARSET);
    }

    // Set the internal value, assuming it is a NUM
    public void setNum(String value) throws QueryException {
        assertNum();

        try {
            buf.putDouble(0, Double.parseDouble(value));
        } catch (NumberFormatException e) {
            throw new QueryException("Used " + value + " as number");
        }
    }

    // Get the internal value, assuming it is a NUM
    public double getNum() throws QueryException {
        assertNum();

        return buf.getDouble(0);
    }

    // Get the type information
    public Type getType() {
        return type;
    }

    // Used to throw an exception if any method is used with the wrong type
    private void assertChar() throws QueryException {
        if (!type.isChar())
            throw new QueryException("Used " + type.toString() + " as CHAR");
    }

    private void assertNum() throws QueryException {
        if (!type.isNum()) {
            throw new QueryException("Used " + type.toString() + " as NUM");
        }
    }

    // Checks if one value is equal to another, throws an exception if the
    // types are incapatible
    public boolean valueEq(Value other) throws QueryException {
        if (type.isNum() && other.type.isNum()) {
            return this.getNum() == other.getNum();
        }
        else if (type.isChar() && other.type.isChar())
            return this.getChar().equalsIgnoreCase(other.getChar());
        else
            throw new QueryException("Attempted comparison of "
                    + type.toString() + " and " + other.type.toString());
    }

    public boolean valueNeq(Value other) throws QueryException {
        if (type.isNum() && other.type.isNum())
            return this.getNum() != other.getNum();
        else if (type.isChar() && other.type.isChar())
            return !this.getChar().equalsIgnoreCase(other.getChar());
        else
            throw new QueryException("Attempted comparison of "
                    + type.toString() + " and " + other.type.toString());
    }

    public boolean valueGt(Value other) throws QueryException {
        if (type.isNum() && other.type.isNum())
            return this.getNum() > other.getNum();
        else if (type.isChar() && other.type.isChar())
            return this.getChar().compareToIgnoreCase(other.getChar()) < 0;
        else
            throw new QueryException("Attempted comparison of "
                    + type.toString() + " and " + other.type.toString());
    }

    public boolean valueLt(Value other) throws QueryException {
        if (type.isNum() && other.type.isNum())
            return this.getNum() < other.getNum();
        else if (type.isChar() && other.type.isChar())
            return this.getChar().compareToIgnoreCase(other.getChar()) > 0;
        else
            throw new QueryException("Attempted comparison of "
                    + type.toString() + " and " + other.type.toString());
    }

    public boolean valueGteq(Value other) throws QueryException {
        if (type.isNum() && other.type.isNum())
            return this.getNum() >= other.getNum();
        else if (type.isChar() && other.type.isChar())
            return this.getChar().compareToIgnoreCase(other.getChar()) <= 0;
        else
            throw new QueryException("Attempted comparison of "
                    + type.toString() + " and " + other.type.toString());
    }

    public boolean valueLteq(Value other) throws QueryException {
        if (type.isNum() && other.type.isNum())
            return this.getNum() <= other.getNum();
        else if (type.isChar() && other.type.isChar())
            return this.getChar().compareToIgnoreCase(other.getChar()) >= 0;
        else
            throw new QueryException("Attempted comparison of "
                    + type.toString() + " and " + other.type.toString());
    }

    public Value add(Value other) throws QueryException {
        return new Value(this.getNum() + other.getNum());
    }

    public Value div(Value other) throws QueryException {
        return new Value(this.getNum() / other.getNum());
    }

    @Override
    public String toString() {
        String str = "VALUE_CORRUPTED";
        try {
            if (type.isNum()) {
                double mov = Math.round(Math.pow(10, type.size));
                double num = getNum();
                num = (Math.round(num * mov) / mov);

                if (num % 1 == 0)
                    str = (long)num + "";
                else
                    str = num + "";
            }
            else if (type.isChar())
                str = getChar();
        } catch (QueryException _e) {
            System.out.println("Error internal to Value");
        }

        return str;
    }
}
