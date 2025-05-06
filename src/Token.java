public class Token {
    private final String value;
    private int nestingLevel;

    public Token(String value) {
        this.value = value;
    }

    public Token(String value, int nestingLevel) { this.value = value; this.nestingLevel = nestingLevel; }

    // Methods
    public boolean isOperator() {
        return this.value.matches("[+\\-*/]");
    }

    public int strength() {
        return (this.value.equals("*") || this.value.equals("/")) ? 2 : (this.value.equals("+") || this.value.equals("-")) ? 1 : 0;
    }

    @Override
    public String toString() {
        return "[" +
                "value='" + value + '\'' +
                ", nestingLevel=" + nestingLevel +
                ']';
    }

    // Getter
    public String getValue() {
        return value;
    }

    public int getNestingLevel() {
        return nestingLevel;
    }

    // Setter
    public void setNestingLevel(int nestingLevel) {
        this.nestingLevel = nestingLevel;
    }
}
