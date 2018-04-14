package bit.minisys.minicc.scanner;

import java.io.*;
import java.util.ArrayList;

public class Scanner {
    public static void run(String inputFileName, String outputFileName) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(inputFileName));
        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFileName));
        ArrayList<TokenHelper> tokens = new ArrayList<TokenHelper>();

        String line;
        int lineNum = 0;
        int tokenNum = 0;
        int curIndex = 0;
        int preIndex = 0;
        int state = 0;
        while ((line = reader.readLine()) != null) {
            lineNum++;
            curIndex = 0;
            preIndex = 0;
            line += '$';
            state = 0;
            while (curIndex < line.length()) {
                char cur = line.charAt(curIndex);
                curIndex++;
                switch (state) {
                    case 0:
                        if (cur == 'u' || cur == 'U' || cur == 'L') {
                            if (curIndex < line.length() && line.charAt(curIndex) == '8') {
                                curIndex += 2;
                            } else {
                                curIndex += 1;
                            }
                            state = 250;
                        }
                        // identifier
                        else if((cur >= 'a' && cur <= 'z') || (cur >= 'A' && cur <= 'Z') || cur == '_') {
                            state = 10;
                        }
                        // decimal-constant
                        else if (cur >= '1' && cur <= '9') {
                            state = 20;
                        } else if (cur == '0') {
                            state = 30;
                        } else if (cur == '+') {
                            state = 100;
                        } else if (cur == '-') {
                            state = 110;
                        } else if (cur == '*') {
                            state = 120;
                        } else if (cur == '/') {
                            state = 130;
                        } else if (cur == '%') {
                            state = 140;
                        } else if (cur == '>') {
                            state = 150;
                        } else if (cur == '<') {
                            state = 160;
                        } else if (cur == '|') {
                            state = 170;
                        } else if (cur == '&') {
                            state = 180;
                        } else if (cur == '=') {
                            state = 190;
                        } else if (cur == '!') {
                            state = 200;
                        } else if (cur == '^') {
                            state = 210;
                        } else if (cur == ':' || cur == '~' || cur == '?') {
                            state = 0;
                            String value = line.substring(preIndex, curIndex);
                            preIndex = curIndex;
                            String type = "operator";
                            tokens.add(new TokenHelper(tokenNum, value, type, lineNum, true));
                        } else if (cur == '.') {
                            state = 51;
                        } else if (cur == '"') {
                            state = 251;
                        } else if (WordClassification.isBoundarySign(cur)) {
                            state = 0;
                            String value = line.substring(preIndex, curIndex);
                            preIndex = curIndex;
                            String type = "boundary";
                            tokens.add(new TokenHelper(tokenNum, value, type, lineNum, true));
                        } else {
                            state = 0;
                            preIndex = curIndex;
                        }
                        break;
                    case 10:
                        if ((cur >= 'a' && cur <= 'z') || (cur >= 'A' && cur <= 'Z') || cur == '_'
                                || (cur >= '0' && cur <= '9')) {
                            state = 10;
                        } else {
                            tokenNum++;
                            state = 0;
                            curIndex--;
                            String value = line.substring(preIndex, curIndex);
                            preIndex = curIndex;
                            String type;
                            if (WordClassification.isKey(value)) {
                                type = "key";
                            } else {
                                type = "identifier";
                            }
                            TokenHelper token = new TokenHelper(tokenNum, value, type, lineNum, true);
                            tokens.add(token);
                        }
                        break;
                    case 20:
                        if (cur >= '0' && cur <= '9') {
                            state = 20;
                        } else if (cur == 'e' || cur == 'E')  {
                            state = 53;
                        } else if (cur == '.') {
                            state = 52;
                        } else {
                            if (cur == 'u' || cur == 'l' || cur == 'L') {
                                if (curIndex + 1 < line.length()) {
                                    String tem1 = line.substring(curIndex - 1, curIndex + 1);
                                    if (tem1.equals("ul") || tem1.equals("uL") || tem1.equals("lu") || tem1.equals("Lu")) {
                                        if (curIndex + 2 < line.length()) {
                                            String tem2 = line.substring(curIndex - 1, curIndex + 2);
                                            if (tem2.equals("ull") || tem2.equals("uLL") || tem2.equals("llu") || tem2.equals("LLu")) {
                                                curIndex += 3;
                                            } else {
                                                curIndex += 2;
                                            }
                                        } else {
                                            curIndex += 2;
                                        }
                                    } else {
                                        curIndex += 1;
                                    }
                                } else {
                                    curIndex += 1;
                                }
                            }
                            tokenNum++;
                            state = 0;
                            curIndex--;
                            String value = line.substring(preIndex, curIndex);
                            preIndex = curIndex;
                            String type = "decimal_constant";
                            tokens.add(new TokenHelper(tokenNum, value, type, lineNum, true));
                        }
                        break;
                    case 51:
                        if (cur >= '0' && cur <= '9') {
                            state = 52;
                        } else {
                            tokenNum++;
                            state = 0;
                            curIndex--;
                            String value = line.substring(preIndex, curIndex);
                            preIndex = curIndex;
                            String type = "operator";
                            tokens.add(new TokenHelper(tokenNum, value, type, lineNum, true));
                        }
                        break;
                    case 52:
                        if (cur >= '0' && cur <= '9') {
                            state = 52;
                        } else if (cur == 'e' || cur == 'E') {
                            state = 53;
                        } else {
                            if (cur == 'f' || cur == 'F' || cur == 'l' || cur == 'L') {
                                curIndex += 1;
                            }
                            tokenNum++;
                            state = 0;
                            curIndex--;
                            String value = line.substring(preIndex, curIndex);
                            preIndex = curIndex;
                            String type = "decimal_floating_constant";
                            tokens.add(new TokenHelper(tokenNum, value, type, lineNum, true));
                        }
                        break;
                    case 53:
                        if (cur == '+' || cur == '-') {
                            state = 54;
                        } else if (cur >= '0' && cur <= '9') {
                            state = 55;
                        } else {
                            tokenNum++;
                            state = 0;
                            curIndex--;
                            String value = line.substring(preIndex, curIndex);
                            preIndex = curIndex;
                            String type = "unknown";
                            tokens.add(new TokenHelper(tokenNum, value, type, lineNum, true));
                        }
                        break;
                    case 54:
                        if (cur >= '0' && cur <= '9') {
                            state = 55;
                        } else {
                            tokenNum++;
                            state = 0;
                            curIndex--;
                            String value = line.substring(preIndex, curIndex);
                            preIndex = curIndex;
                            String type = "unknown";
                            tokens.add(new TokenHelper(tokenNum, value, type, lineNum, true));
                        }
                        break;
                    case 55:
                        if (cur >= '0' && cur <= '9') {
                            state = 55;
                        } else {
                            if (cur == 'f' || cur == 'F' || cur == 'l' || cur == 'L') {
                                curIndex += 1;
                            }
                            tokenNum++;
                            state = 0;
                            curIndex--;
                            String value = line.substring(preIndex, curIndex);
                            preIndex = curIndex;
                            String type = "decimal_floating_constant";
                            tokens.add(new TokenHelper(tokenNum, value, type, lineNum, true));
                        }
                        break;
                    case 30:
                        if (cur >= '0' && cur <= '7') {
                            state = 31;
                        } else if (cur == 'x' || cur == 'X') {
                            state = 40;
                        } else {
                            tokenNum++;
                            state = 0;
                            curIndex--;
                            String value = line.substring(preIndex, curIndex);
                            preIndex = curIndex;
                            String type = "decimal_constant";
                            tokens.add(new TokenHelper(tokenNum, value, type, lineNum, true));
                        }
                        break;
                    case 31:
                        if (cur >= '0' && cur <= '7') {
                            state = 31;
                        } else {
                            tokenNum++;
                            state = 0;
                            curIndex--;
                            String value = line.substring(preIndex, curIndex);
                            preIndex = curIndex;
                            String type = "octal_constant";
                            tokens.add(new TokenHelper(tokenNum, value, type, lineNum, true));
                        }
                        break;
                    case 40:
                        if ((cur >= '0' && cur <= '9') || (cur >= 'a' && cur <= 'f') || (cur >= 'A' && cur <= 'F')) {
                            state = 41;
                        } else if (cur == '.') {
                            state = 71;
                        } else {
                            tokenNum++;
                            state = 0;
                            curIndex--;
                            String value = line.substring(preIndex, curIndex);
                            preIndex = curIndex;
                            String type = "unknown";
                            tokens.add(new TokenHelper(tokenNum, value, type, lineNum, true));
                        }
                        break;
                    case 41:
                        if ((cur >= '0' && cur <= '9') || (cur >= 'a' && cur <= 'f') || (cur >= 'A' && cur <= 'F')) {
                            state = 41;
                        } else if (cur == '.') {
                            state = 72;
                        } else if (cur == 'p' || cur == 'P') {
                            state = 73;
                        } else {
                            tokenNum++;
                            state = 0;
                            curIndex--;
                            String value = line.substring(preIndex, curIndex);
                            preIndex = curIndex;
                            String type = "hexadecimal_constant";
                            tokens.add(new TokenHelper(tokenNum, value, type, lineNum, true));
                        }
                        break;
                    case 71:
                        if ((cur >= '0' && cur <= '9') || (cur >= 'a' && cur <= 'f') || (cur >= 'A' && cur <= 'F')) {
                            state = 72;
                        } else {
                            tokenNum++;
                            state = 0;
                            curIndex--;
                            String value = line.substring(preIndex, curIndex);
                            preIndex = curIndex;
                            String type = "unknown";
                            tokens.add(new TokenHelper(tokenNum, value, type, lineNum, true));
                        }
                        break;
                    case 72:
                        if ((cur >= '0' && cur <= '9') || (cur >= 'a' && cur <= 'f') || (cur >= 'A' && cur <= 'F')) {
                            state = 72;
                        } else if (cur == 'p' || cur == 'P') {
                            state = 73;
                        } else {
                            //判断可能有浮点后缀
                            if (cur == 'f' || cur == 'F' || cur == 'l' || cur == 'L') {
                                curIndex += 1;
                            }
                            tokenNum++;
                            state = 0;
                            curIndex--;
                            String value = line.substring(preIndex, curIndex);
                            preIndex = curIndex;
                            String type = "hexadecimal_floating_constant";
                            tokens.add(new TokenHelper(tokenNum, value, type, lineNum, true));
                        }
                        break;
                    case 73:
                        if (cur == '+' || cur == '-') {
                            state = 74;
                        } else if (cur >= '0' && cur <= '9') {
                            state = 75;
                        } else {
                            tokenNum++;
                            state = 0;
                            curIndex--;
                            String value = line.substring(preIndex, curIndex);
                            preIndex = curIndex;
                            String type = "unknown";
                            tokens.add(new TokenHelper(tokenNum, value, type, lineNum, true));
                        }
                        break;
                    case 74:
                        if (cur >= '0' && cur <= '9') {
                            state = 75;
                        } else {
                            tokenNum++;
                            state = 0;
                            curIndex--;
                            String value = line.substring(preIndex, curIndex);
                            preIndex = curIndex;
                            String type = "unknown";
                            tokens.add(new TokenHelper(tokenNum, value, type, lineNum, true));
                        }
                        break;
                    case 75:
                        if (cur >= '0' && cur <= '9') {
                            state = 75;
                        } else {
                            if (cur == 'f' || cur == 'F' || cur == 'l' || cur == 'L') {
                                curIndex += 1;
                            }
                            tokenNum++;
                            state = 0;
                            curIndex--;
                            String value = line.substring(preIndex, curIndex);
                            preIndex = curIndex;
                            String type = "hexadecimal_floating_constant";
                            tokens.add(new TokenHelper(tokenNum, value, type, lineNum, true));
                        }
                        break;
                    case 100:
                        if (cur == '+' || cur == '=') {
                            tokenNum++;
                            state = 0;
                            String value = line.substring(preIndex, curIndex);
                            preIndex = curIndex;
                            String type = "operator";
                            tokens.add(new TokenHelper(tokenNum, value, type, lineNum, true));
                        } else {
                            tokenNum++;
                            state = 0;
                            curIndex--;
                            String value = line.substring(preIndex, curIndex);
                            preIndex = curIndex;
                            String type = "operator";
                            tokens.add(new TokenHelper(tokenNum, value, type, lineNum, true));
                        }
                        break;
                    case 110:
                        if (cur == '>' || cur == '-' || cur == '=') {
                            tokenNum++;
                            state = 0;
                            String value = line.substring(preIndex, curIndex);
                            preIndex = curIndex;
                            String type = "operator";
                            tokens.add(new TokenHelper(tokenNum, value, type, lineNum, true));
                        } else {
                            tokenNum++;
                            state = 0;
                            curIndex--;
                            String value = line.substring(preIndex, curIndex);
                            preIndex = curIndex;
                            String type = "operator";
                            tokens.add(new TokenHelper(tokenNum, value, type, lineNum, true));
                        }
                        break;
                    case 120:
                        if (cur == '=') {
                            tokenNum++;
                            state = 0;
                            String value = line.substring(preIndex, curIndex);
                            preIndex = curIndex;
                            String type = "operator";
                            tokens.add(new TokenHelper(tokenNum, value, type, lineNum, true));
                        } else {
                            tokenNum++;
                            state = 0;
                            curIndex--;
                            String value = line.substring(preIndex, curIndex);
                            preIndex = curIndex;
                            String type = "operator";
                            tokens.add(new TokenHelper(tokenNum, value, type, lineNum, true));
                        }
                        break;
                    case 130:
                        if (cur == '=') {
                            tokenNum++;
                            state = 0;
                            String value = line.substring(preIndex, curIndex);
                            preIndex = curIndex;
                            String type = "operator";
                            tokens.add(new TokenHelper(tokenNum, value, type, lineNum, true));
                        } else {
                            tokenNum++;
                            state = 0;
                            curIndex--;
                            String value = line.substring(preIndex, curIndex);
                            preIndex = curIndex;
                            String type = "operator";
                            tokens.add(new TokenHelper(tokenNum, value, type, lineNum, true));
                        }
                        break;
                    case 140:
                        if (cur == '=') {
                            tokenNum++;
                            state = 0;
                            String value = line.substring(preIndex, curIndex);
                            preIndex = curIndex;
                            String type = "operator";
                            tokens.add(new TokenHelper(tokenNum, value, type, lineNum, true));
                        } else {
                            tokenNum++;
                            state = 0;
                            curIndex--;
                            String value = line.substring(preIndex, curIndex);
                            preIndex = curIndex;
                            String type = "operator";
                            tokens.add(new TokenHelper(tokenNum, value, type, lineNum, true));
                        }
                        break;
                    case 150:
                        if (cur == '=') {
                            tokenNum++;
                            state = 0;
                            String value = line.substring(preIndex, curIndex);
                            preIndex = curIndex;
                            String type = "operator";
                            tokens.add(new TokenHelper(tokenNum, value, type, lineNum, true));
                        } else if (cur == '>') {
                            state = 151;
                        } else {
                            tokenNum++;
                            state = 0;
                            curIndex--;
                            String value = line.substring(preIndex, curIndex);
                            preIndex = curIndex;
                            String type = "operator";
                            tokens.add(new TokenHelper(tokenNum, value, type, lineNum, true));
                        }
                        break;
                    case 151:
                        if (cur == '=') {
                            tokenNum++;
                            state = 0;
                            String value = line.substring(preIndex, curIndex);
                            preIndex = curIndex;
                            String type = "operator";
                            tokens.add(new TokenHelper(tokenNum, value, type, lineNum, true));
                        } else {
                            tokenNum++;
                            state = 0;
                            curIndex--;
                            String value = line.substring(preIndex, curIndex);
                            preIndex = curIndex;
                            String type = "operator";
                            tokens.add(new TokenHelper(tokenNum, value, type, lineNum, true));
                        }
                        break;
                    case 160:
                        if (cur == '=') {
                            tokenNum++;
                            state = 0;
                            String value = line.substring(preIndex, curIndex);
                            preIndex = curIndex;
                            String type = "operator";
                            tokens.add(new TokenHelper(tokenNum, value, type, lineNum, true));
                        } else if (cur == '<') {
                            state = 161;
                        } else {
                            tokenNum++;
                            state = 0;
                            curIndex--;
                            String value = line.substring(preIndex, curIndex);
                            preIndex = curIndex;
                            String type = "operator";
                            tokens.add(new TokenHelper(tokenNum, value, type, lineNum, true));
                        }
                        break;
                    case 161:
                        if (cur == '=') {
                            tokenNum++;
                            state = 0;
                            String value = line.substring(preIndex, curIndex);
                            preIndex = curIndex;
                            String type = "operator";
                            tokens.add(new TokenHelper(tokenNum, value, type, lineNum, true));
                        } else {
                            tokenNum++;
                            state = 0;
                            curIndex--;
                            String value = line.substring(preIndex, curIndex);
                            preIndex = curIndex;
                            String type = "operator";
                            tokens.add(new TokenHelper(tokenNum, value, type, lineNum, true));
                        }
                        break;
                    case 170:
                        if (cur == '|' || cur == '=') {
                            tokenNum++;
                            state = 0;
                            String value = line.substring(preIndex, curIndex);
                            preIndex = curIndex;
                            String type = "operator";
                            tokens.add(new TokenHelper(tokenNum, value, type, lineNum, true));
                        } else {
                            tokenNum++;
                            state = 0;
                            curIndex--;
                            // System.out.println("debug:" + line +"\n" + curIndex +
                            // " " + cur + ' ' + preIndex);
                            // if (curIndex >= line.length() || preIndex < 0) {
                            // System.out.println("debug:curIndex is too big");
                            // }
                            String value = line.substring(preIndex, curIndex);
                            preIndex = curIndex;
                            String type = "operator";
                            tokens.add(new TokenHelper(tokenNum, value, type, lineNum, true));
                        }
                        break;
                    case 180:
                        if (cur == '&' || cur == '=') {
                            tokenNum++;
                            state = 0;
                            String value = line.substring(preIndex, curIndex);
                            preIndex = curIndex;
                            String type = "operator";
                            tokens.add(new TokenHelper(tokenNum, value, type, lineNum, true));
                        } else {
                            tokenNum++;
                            state = 0;
                            curIndex--;
                            String value = line.substring(preIndex, curIndex);
                            preIndex = curIndex;
                            String type = "operator";
                            tokens.add(new TokenHelper(tokenNum, value, type, lineNum, true));
                        }
                        break;
                    case 190:
                        if (cur == '=') {
                            tokenNum++;
                            state = 0;
                            String value = line.substring(preIndex, curIndex);
                            preIndex = curIndex;
                            String type = "operator";
                            tokens.add(new TokenHelper(tokenNum, value, type, lineNum, true));
                        } else {
                            tokenNum++;
                            state = 0;
                            curIndex--;
                            String value = line.substring(preIndex, curIndex);
                            preIndex = curIndex;
                            String type = "operator";
                            tokens.add(new TokenHelper(tokenNum, value, type, lineNum, true));
                        }
                        break;
                    case 200:
                        if (cur == '=') {
                            tokenNum++;
                            state = 0;
                            String value = line.substring(preIndex, curIndex);
                            preIndex = curIndex;
                            String type = "operator";
                            tokens.add(new TokenHelper(tokenNum, value, type, lineNum, true));
                        } else {
                            tokenNum++;
                            state = 0;
                            curIndex--;
                            String value = line.substring(preIndex, curIndex);
                            preIndex = curIndex;
                            String type = "operator";
                            tokens.add(new TokenHelper(tokenNum, value, type, lineNum, true));
                        }
                        break;
                    case 210:
                        if (cur == '=') {
                            tokenNum++;
                            state = 0;
                            String value = line.substring(preIndex, curIndex);
                            preIndex = curIndex;
                            String type = "operator";
                            tokens.add(new TokenHelper(tokenNum, value, type, lineNum, true));
                        } else {
                            tokenNum++;
                            state = 0;
                            curIndex--;
                            String value = line.substring(preIndex, curIndex);
                            preIndex = curIndex;
                            String type = "operator";
                            tokens.add(new TokenHelper(tokenNum, value, type, lineNum, true));
                        }
                        break;
                    case 250:
                        if (cur == '"') {
                            state = 251;
                        } else {
                            state = 10;
                            curIndex --;
                        }
                        break;
                    case 251:
                        if (cur == '"') {
                            tokenNum++;
                            state = 0;
                            String value = line.substring(preIndex, curIndex);
                            preIndex = curIndex;
                            String type = "operator";
                            tokens.add(new TokenHelper(tokenNum, value, type, lineNum, true));
                        } else {
                            state = 251;
                        }
                        break;
                    default:

                }
            }
        }

        writeXML(writer, tokens);

        reader.close();
        writer.close();
    }

    private static void writeXML(BufferedWriter writer, ArrayList<TokenHelper> tokens) throws IOException {
        writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        writer.write("<project name=\"CFhM_R's Scanner\">\n");
        writer.write("  <tokens>\n");
        for (TokenHelper token : tokens) {
            token.writeSingleTokenToXML(writer);
        }
        writer.write("  </tokens>\n");
        writer.write("</project>\n");
    }
}
