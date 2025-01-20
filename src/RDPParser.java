public class RDPParser {
    private String input;
    private int index;

    public boolean parse(String input) {
        this.input = input + "$";
        this.index = 0;
        return E() && index == this.input.length() - 1;
    }

    private boolean E() {
        return T() && EPrime();
    }

    private boolean EPrime() {
        if (match('+')) {
            return T() && EPrime();
        }
        return true; // epsilon production
    }

    private boolean T() {
        return F() && TPrime();
    }

    private boolean TPrime() {
        if (match('*')) {
            return F() && TPrime();
        }
        return true; // epsilon production
    }

    private boolean F() {
        if (match('(')) {
            if (E() && match(')')) {
                return true;
            }
            return false;
        } else if (match('i') && match('d')) {
            return true;
        }
        return false;
    }

    private boolean match(char expected) {
        if (index < input.length() && input.charAt(index) == expected) {
            index++;
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        RDPParser parser = new RDPParser();
        System.out.println(parser.parse("id+id*id")); // true
        System.out.println(parser.parse("id+*id")); // false
    }
}