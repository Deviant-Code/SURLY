public class Attribute {

    public final Type type;
    public final String name;
    public final String parent;
    private boolean qualified;

    public Attribute(String name, Type type) {
        this.type = type;
        this.qualified = false;

        if (name.contains(".")) {
            String[] split = name.split("\\.");
            this.parent = split[0];
            this.name = split[1];
        }
        else {
            this.name = name;
            this.parent = "$error$";
        }
    }

    public Attribute(String name, String parent, Type type){
        this.type = type;
        this.name = name;
        this.parent = parent;
        this.qualified = false;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Attribute) {
            Attribute attr = (Attribute)other;

            return this.type.equals(attr.type)
                && this.name.equalsIgnoreCase(attr.name);
        }
        else return false;
    }

    public boolean nameEquals(String name) {
        String[] split = name.split("\\.");

        if (split.length > 1) {
            return split[0].equalsIgnoreCase(this.parent)
                && split[1].equalsIgnoreCase(this.name);
        }
        else {
            return name.equalsIgnoreCase(this.name);
        }
    }

    public void setQualifier() {
        qualified = true;
    }

    @Override
    public String toString() {
        if (qualified) {
            return parent + "." + name;
        }
        else {
            return name;
        }
    }
}
