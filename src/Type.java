public class Type {
    private final Primitive type;
    public final int size;

    private Type(Primitive type, int size) {
        this.type = type;
        this.size = size;
    }

    public static Type fromString(String str, int size) {
        if (str.toUpperCase().equals("NUM"))
            return new Type(Primitive.NUM, size);
        else if (str.toUpperCase().equals("CHAR"))
            return new Type(Primitive.CHAR, size);
        else
            return null; // TODO make this better
    }

    public boolean isNum() {
        return type == Primitive.NUM;
    }

    public boolean isChar() {
        return type == Primitive.CHAR;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof Type && ((Type)other).type == this.type;
    }

    @Override
    public String toString() {
        return this.type + "(" + this.size + ")";
    }

    private static enum Primitive {
        CHAR,
        NUM
    } 
}

