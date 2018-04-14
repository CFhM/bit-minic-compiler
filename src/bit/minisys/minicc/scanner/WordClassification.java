package bit.minisys.minicc.scanner;

import java.util.ArrayList;

public class WordClassification {
    public static ArrayList<String> key = new ArrayList();
    public static ArrayList<String> boundarySign = new ArrayList();
    public static ArrayList<String> operator = new ArrayList();
    public int id;
    public String value;
    public String type;
    public int line;

    static {
        operator.add("+");
        operator.add("++");
        operator.add("+=");
        operator.add("-");
        operator.add("->");
        operator.add("--");
        operator.add("-=");
        operator.add("*");
        operator.add("*=");
        operator.add("/");
        operator.add("/=");
        operator.add("%");
        operator.add("%=");
        operator.add(">");
        operator.add(">=");
        operator.add(">>");
        operator.add(">>=");
        operator.add("<");
        operator.add("<=");
        operator.add("<<");
        operator.add("<<=");
        operator.add("|");
        operator.add("||");
        operator.add("|=");
        operator.add("&");
        operator.add("&&");
        operator.add("&=");
        operator.add("=");
        operator.add("==");
        operator.add("!");
        operator.add("!=");
        operator.add("^");
        operator.add("^=");
        operator.add(":");
        operator.add("~");
        operator.add(".");
        operator.add("?");


        boundarySign.add("'");
        boundarySign.add("\"");
        boundarySign.add("(");
        boundarySign.add(")");
        boundarySign.add("[");
        boundarySign.add("]");
        boundarySign.add("{");
        boundarySign.add("}");
        boundarySign.add(";");
        boundarySign.add(",");

        key.add("auto");
        key.add("break");
        key.add("case");
        key.add("char");
        key.add("const");
        key.add("continue");
        key.add("default");
        key.add("do");
        key.add("double");
        key.add("else");
        key.add("enum");
        key.add("extern");
        key.add("float");
        key.add("for");
        key.add("goto");
        key.add("if");
        key.add("inline");
        key.add("int");
        key.add("long");
        key.add("register");
        key.add("restrict");
        key.add("return");
        key.add("short");
        key.add("singed");
        key.add("sizeof");
        key.add("static");
        key.add("struct");
        key.add("switch");
        key.add("typedef");
        key.add("union");
        key.add("unsigned");
        key.add("void");
        key.add("volatile");
        key.add("while");
        key.add("_Alignas");
        key.add("_Alignof");
        key.add("_Atomic");
        key.add("_Bool");
        key.add("_Complex");
        key.add("_Generic");
        key.add("_Imaginary");
        key.add("_Noreturn");
        key.add("_Static_assert");
        key.add("_Thread_local");
    }

    public WordClassification() {
    }

    public WordClassification(int id, String value, String type, int line) {
        this.id = id;
        this.value = value;
        this.type = type;
        this.line = line;
    }

    public static boolean isKey(String word) {
        return key.contains(word);
    }

    public static boolean isOperator(String word) {
        return operator.contains(word);
    }

    public static boolean isBoundarySign(char cur) {
        return boundarySign.contains(cur);
    }

    public static boolean isArOP(String word) {
        return (word.equals("+")) || (word.equals("-")) || (word.equals("*")) || (word.equals("/"));
    }

    public static boolean isBoolOP(String word) {
        return (word.equals(">")) || (word.equals("<")) || (word.equals("==")) || (word.equals("!=")) || (word.equals("!")) || (word.equals("&&")) || (word.equals("||"));
    }
}
