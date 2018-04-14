package bit.minisys.minicc.scanner;


// Author: CFhM_R
// Date: 2018-04-14


import java.io.BufferedWriter;
import java.io.IOException;

public class TokenHelper {
    private int number;
    private String value;
    private String type;
    private int line;
    private boolean valid;

    public TokenHelper(int aNumber, String aValue,String aType, int aLine, boolean aValid) {
        number = aNumber;
        value = aValue;
        type = aType;
        line = aLine;
        valid = aValid;
    }

    public void writeSingleTokenToXML(BufferedWriter writer) throws IOException {
        writer.write("    <token>\n");
        writer.write("      <number>" + number + "</number>\n");
        writer.write("      <value>" + value + "</value>\n");
        writer.write("      <type>" + type + "</type>\n");
        writer.write("      <line>" + line + "</line>\n");
        writer.write("      <valid>" + valid + "</valid>\n");
        writer.write("    </token>\n");
    }
}
