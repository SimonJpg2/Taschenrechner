public class Token {
    private final String value;
    private int nestingLevel;

    public Token(String value) {
        this.value = value;
    }

    // Methods
    public boolean isOperator() {
        return this.value.matches("[+\\-*/]");
    }

    public int strength() {
        return (this.value.equals("*") || this.value.equals("/")) ? 2 : (this.value.equals("+") || this.value.equals("-")) ? 1 : 0;
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
