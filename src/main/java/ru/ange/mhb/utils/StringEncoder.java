package ru.ange.mhb.utils;

import java.util.*;

/**
 * Various Javascript code utilities. The escape classes were taken from
 * jakarta-commons-lang which in turn borrowed from Turbine and other projects.
 * The list of authors below is almost certainly far too long, but I'm not sure
 * who really wrote these methods.
 *
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 * @author Henri Yandell
 * @author Alexander Day Chaffee
 * @author Antony Riley
 * @author Helge Tesgaard
 * @author Sean Brown
 * @author Gary Gregory
 * @author Phil Steitz
 * @author Pete Gieser
 *
 * @author Litvinenko Vasiliy :D
 * Не, ну а че, я сюда столько костылей впилил, чтобы это все работало как надо, так что заслужил :DDDD
 */

public class StringEncoder {
    public static Set number = new HashSet();

    /**
     * <p>
     * Escapes the characters in a <code>String</code> using JavaScript String
     * rules.
     * </p>
     * <p>
     * Escapes any values it finds into their JavaScript String form. Deals
     * correctly with quotes and control-chars (tab, backslash, cr, ff, etc.)
     * </p>
     *
     * <p>
     * So a tab becomes the characters <code>'\\'</code> and <code>'t'</code>.
     * </p>
     *
     * <p>
     * The only difference between Java strings and JavaScript strings is that
     * in JavaScript, a single quote must be escaped.
     * </p>
     *
     * <p>
     * Example:
     *
     * <pre>
     * input string: He didn't say, "Stop!"
     * output string: He didn\'t say, \"Stop!\"
     * </pre>
     *
     * </p>
     *
     * @param str
     *            String to escape values in, may be null
     * @return String with escaped values, <code>null</code> if null string
     *         input
     */
    public static String escapeJavaScript(String str) {

        if (str == null) {
            return null;
        }

        StringBuffer writer = new StringBuffer(str.length() * 2);

        int sz = str.length();
        for (int i = 0; i < sz; i++) {
            char ch = str.charAt(i);

            // handle unicode
            if (ch > 0xfff) {
                writer.append("\\u");
                writer.append(hex(ch));
            } else if (ch > 0xff) {
                writer.append("\\u0");
                writer.append(hex(ch));
            } else if (ch > 0x7f) {
                writer.append("\\u00");
                writer.append(hex(ch));
            } else if (ch < 32) {
                switch (ch) {
                    case '\b':
                        writer.append('\\');
                        writer.append('b');
                        break;
                    case '\n':
                        writer.append('\\');
                        writer.append('n');
                        break;
                    case '\t':
                        writer.append('\\');
                        writer.append('t');
                        break;
                    case '\f':
                        writer.append('\\');
                        writer.append('f');
                        break;
                    case '\r':
                        writer.append('\\');
                        writer.append('r');
                        break;
                    default:
                        if (ch > 0xf) {
                            writer.append("\\u00");
                            writer.append(hex(ch));
                        } else {
                            writer.append("\\u000");
                            writer.append(hex(ch));
                        }
                        break;
                }
            } else {
                switch (ch) {
                    case '\'':
                        // If we wanted to escape for Java strings then we would
                        // not need this next line.
                        writer.append('\\');
                        writer.append('\'');
                        break;
                    case '"':
                        writer.append('\\');
                        writer.append('"');
                        break;
                    case '\\':
                        writer.append('\\');
                        writer.append('\\');
                        break;
                    default:
                        writer.append(ch);
                        break;
                }
            }
        }

        return writer.toString();
    }

    /**
     * <p>
     * Returns an upper case hexadecimal <code>String</code> for the given
     * character.
     * </p>
     *
     * @param ch
     *            The character to convert.
     * @return An upper case hexadecimal <code>String</code>
     */
    private static String hex(char ch) {
        return Integer.toHexString(ch).toUpperCase(Locale.ENGLISH);
    }

    /**
     * <p>
     * Unescapes any JavaScript literals found in the <code>String</code>.
     * </p>
     * <p>
     * For example, it will turn a sequence of <code>'\'</code> and
     * <code>'n'</code> into a newline character, unless the <code>'\'</code> is
     * preceded by another <code>'\'</code>.
     * </p>
     *
     * @param str
     *            the <code>String</code> to unescape, may be null
     * @return A new unescaped <code>String</code>, <code>null</code> if null
     *         string input
     */
    public static String unescapeJavaScript(String str) {

        if (str == null) {
            return null;
        }

        char kav = '"';
        for (int j = 0; j < str.length();j++){
            str = str.replaceAll("%22", String.valueOf(kav));
            str = str.replaceAll("%28","(");
            str = str.replaceAll("%29", ")");
            str = str.replaceAll("%", "\\\\");
        }

        StringBuffer writer = new StringBuffer(str.length());
        int sz = str.length();
        StringBuffer unicode = new StringBuffer(4);
        boolean hadSlash = false;
        boolean inUnicode = false;

        for (int i = 0; i < sz; i++) {
            char ch = str.charAt(i);

            // System.out.println(ch);
            if (inUnicode) {
                // if in unicode, then we're reading unicode
                // values in somehow
                unicode.append(ch);

                if (unicode.length() == 4) {
                    // unicode now contains the four hex digits
                    // which represents our unicode character
                    try {
                        int value = Integer.parseInt(unicode.toString(), 16);

                        if (value == 20) {
                            writer.append(" ");
                        } else {
                            writer.append((char) value);
                        }

                        unicode.setLength(0);
                        inUnicode = false;
                        hadSlash = false;
                    } catch (NumberFormatException nfe) {
                        throw new IllegalArgumentException(
                                "Unable to parse unicode value: " + unicode
                                        + " cause: " + nfe);
                    }
                }
                continue;
            }

            if (hadSlash) {
                // handle an escaped value
                hadSlash = false;
                switch (ch) {
                    case '\\':
                        writer.append('\\');
                        break;
                    case '\'':
                        writer.append('\'');
                        break;
                    case '\"':
                        writer.append('"');
                        break;
                    case 'r':
                        writer.append('\r');
                        break;
                    case 'f':
                        writer.append('\f');
                        break;
                    case 't':
                        writer.append('\t');
                        break;
                    case 'n':
                        writer.append('\n');
                        break;
                    case '2':
                        writer.append(' ');
                        break;
                    case '0':
                        writer.append('0');
                        break;
                    case 'b':
                        writer.append('\b');
                        break;
                    case 'u':
                        // uh-oh, we're in unicode country....
                        inUnicode = true;
                        break;
                    default:
                        writer.append(ch);
                        break;
                }
                continue;
            } else if (ch == '\\') {
                hadSlash = true;
                continue;
            }
            writer.append(ch);
        }

        if (hadSlash) {
            // then we're in the weird case of a \ at the end of the
            // string, let's output it anyway.
            writer.append('\\');
        }

        for (int k = 0; k < 10; k++) {
            number.add(String.valueOf(k));

        }

        char[] rezultMas = new char[writer.toString().length()];
        rezultMas = writer.toString().toCharArray();
        //System.out.println(rezultMas);
        for (int cnt = 0; cnt < rezultMas.length - 1; cnt++) {
            if (rezultMas[cnt] == '0'
                    && number.contains(String.valueOf(rezultMas[cnt + 1])) == false
                    && rezultMas[cnt + 1] != ' ' && rezultMas[cnt + 1] != ',' && rezultMas[cnt + 1] != '/')
                rezultMas[cnt] = '=';
            if (cnt > 0) {
                if (rezultMas[cnt] == '0' && rezultMas[cnt - 1] == ' '
                        || rezultMas[cnt - 1] == ',')
                    rezultMas[cnt] = '=';
            }
        }

        String rezult = String.valueOf(rezultMas);
        rezult = rezult.replaceAll("C", ",");
        rezult = rezult.replaceAll("=", "");

        return rezult;
        // return writer.toString().replaceAll("C", ",");//.replaceAll("0", "");
    }

    /**
     * Check to see if the given word is reserved or a bad idea in any known
     * version of JavaScript.
     *
     * @param name
     *            The word to check
     * @return false if the word is not reserved
     */
    public static boolean isReservedWord(String name) {
        return reserved.contains(name);
    }

    /**
     * The array of javascript reserved words
     */
    private static final String[] RESERVED_ARRAY = new String[] {
            "as", "break", "case", "catch", "class", "const", "continue",
            "default", "delete", "do", "else", "export", "extends", "false",
            "finally", "for", "function", "if", "import", "in", "instanceof",
            "is", "namespace", "new", "null", "package", "private", "public",
            "return", "super", "switch", "this", "throw", "true", "try",
            "typeof", "use", "var", "void",
            "while",
            "with",
            "abstract", "debugger", "enum", "goto", "implements", "interface",
            "native", "protected", "synchronized", "throws", "transient",
            "volatile",
            "boolean", "byte", "char", "double", "final", "float", "int",
            "long", "short", "static",

    };

    /**
     * The list of reserved words
     */
    private static SortedSet<String> reserved = new TreeSet<String>();

    /**
     * For easy access ...
     */

    static {
        // The Javascript reserved words array so we don't generate illegal
        // javascript
        reserved.addAll(Arrays.asList(RESERVED_ARRAY));
    }

    public static void main(String[] args) {
        String a = "%u041F%u041E%u0421%20Balloon%2091-180%28%u041E%u0411%u0415%u0421%u041F%29";
        String b = "%u041F%u041E%u0421%20Balloon%2091-180%28%u041E%u0411%u0415%u0421%u041F%29";
        String c = unescapeJavaScript(b);

        System.out.println(b);
        System.out.println(c);
        //System.out.println(	a.replaceAll("%22", "\"" ));
    }
}
