package bit.minisys.minicc.pp;


// Author: CFhM_R
// Date: 2018-04-14


import bit.minisys.minicc.util.MiniCCUtil;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class PreProcessor {
    private String path;

    public PreProcessor(String filePath)
    {
        this.path = filePath;
    }
    public void preProcess(String output) {
        if (!MiniCCUtil.checkFile(this.path)) {
            return;
        }
        String processed = "";
        File file = new File(this.path);

        // comment process
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = "";
            while ((line = reader.readLine()) != null)
            {
                int start_0 = line.indexOf("//");
                int start_1 = line.indexOf("/*");
                int end = line.indexOf("*/");
                if (start_0 == 0) {
                    continue;
                }
                if ((start_0 > 0) && (start_0 > start_1)) {
                    line = line.substring(0, start_0);
                }
                else if ((start_1 >= 0) && (end >= 0) && (end - start_1 >= 2)) {
                    line = line.substring(0, start_1);
                }
                else if ((start_1 >= 0) && (end == -1)) {
                    line = line.substring(0, start_1);
                    do
                    {
                        String l = reader.readLine();
                        end = l.indexOf("*/");
                    }while (end == -1);
                }

                processed = processed + line;
                processed = processed + "\r\n";
            }

            reader.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        MiniCCUtil.createAndWriteFile(output, processed);

        // process spaces, tabs, newlines
        file = new File(output);
        processed = "";
        try {
            BufferedReader reader_1 = new BufferedReader(new FileReader(file));
            String line = "";
            while ((line = reader_1.readLine()) != null)
            {
                for (int i = 0; i < line.length(); i++) {
                    if (line.charAt(i) == '"') {
                        i++;
                        while (line.charAt(i) != '"') {
                            i++;
                        }
                    }
                    else if (line.charAt(i) == '\'') {
                        i++;
                        while (line.charAt(i) != '\'') {
                            i++;
                        }

                    }
                    else if ((line.charAt(i) == '(') || (line.charAt(i) == ')') || (line.charAt(i) == '{') ||
                            (line.charAt(i) == '}') || (line.charAt(i) == '[') || (line.charAt(i) == ']') ||
                            (line.charAt(i) == '?') || (line.charAt(i) == ',') || (line.charAt(i) == ':') ||
                            (line.charAt(i) == '%') || (line.charAt(i) == '~') || (line.charAt(i) == '!') ||
                            (line.charAt(i) == ';') ||
                            ((line.charAt(i) == '+') && (line.charAt(i + 1) != '+') && (line.charAt(i + 1) != '=')) ||
                            ((line.charAt(i) == '-') && (line.charAt(i + 1) != '-') && (line.charAt(i + 1) != '=') && (line.charAt(i + 1) != '>')) ||
                            ((line.charAt(i) == '*') && (line.charAt(i + 1) != '=')) ||
                            ((line.charAt(i) == '/') && (line.charAt(i + 1) != '=')) ||
                            ((line.charAt(i) == '^') && (line.charAt(i + 1) != '=')) ||
                            ((line.charAt(i) == '=') && (line.charAt(i + 1) != '=')) ||
                            ((line.charAt(i) == ':') && (line.charAt(i + 1) != ':')) ||
                            ((line.charAt(i) == '>') && (line.charAt(i + 1) != '=')) ||
                            ((line.charAt(i) == '<') && (line.charAt(i + 1) != '=')) ||
                            ((line.charAt(i) == '|') && (line.charAt(i + 1) != '|')) ||
                            ((line.charAt(i) == '&') && (line.charAt(i + 1) != '&')) ||
                            ((line.charAt(i) == '\\') && (line.charAt(i + 1) != '\\')) || (
                            (line.charAt(i) == '/') && (line.charAt(i + 1) != '/')))
                    {
                        line = line.substring(0, i) + " " + line.charAt(i) + " " + line.substring(i + 1);
                        i += 2;
                    }
                    else {
                        if (((line.charAt(i) != '+') || (line.charAt(i + 1) != '+')) &&
                                ((line.charAt(i) != '+') || (line.charAt(i + 1) != '=')) &&
                                ((line.charAt(i) != '-') || (line.charAt(i + 1) != '-')) &&
                                ((line.charAt(i) != '-') || (line.charAt(i + 1) != '=')) &&
                                ((line.charAt(i) != '-') || (line.charAt(i + 1) != '>')) &&
                                ((line.charAt(i) != '/') || (line.charAt(i + 1) != '=')) &&
                                ((line.charAt(i) != '*') || (line.charAt(i + 1) != '=')) &&
                                ((line.charAt(i) != '^') || (line.charAt(i + 1) != '=')) &&
                                ((line.charAt(i) != '>') || (line.charAt(i + 1) != '=')) &&
                                ((line.charAt(i) != '<') || (line.charAt(i + 1) != '=')) &&
                                ((line.charAt(i) != '|') || (line.charAt(i + 1) != '|')) &&
                                ((line.charAt(i) != '&') || (line.charAt(i + 1) != '&')) &&
                                ((line.charAt(i) != '=') || (line.charAt(i + 1) != '=')) &&
                                ((line.charAt(i) != '\\') || (line.charAt(i + 1) != '\\')) && (
                                (line.charAt(i) != '/') || (line.charAt(i + 1) != '/')))
                            continue;
                        i++;
                        line = line.substring(0, i - 1) + " " + line.substring(i - 1, i + 1) + " " + line.substring(i + 1);
                        i += 2;
                    }
                }

                processed = processed + line;
                processed = processed + "\r\n";
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        MiniCCUtil.createAndWriteFile(output, processed);

        // substitute macros
        file = new File(output);
        processed = "";
        try {
            BufferedReader reader_0 = new BufferedReader(new FileReader(file));
            String line = "";
            Map map = new HashMap();
            while ((line = reader_0.readLine()) != null)
            {
                int defineIndex = line.indexOf("#define");
                String[] strarray = line.split("\\s+");
                if (defineIndex == 0) {
                    map.put(strarray[1], strarray[2]);
                }
                else
                {
                    for (int i = 0; i < strarray.length; i++) {
                        if (map.containsKey(strarray[i])) {
                            strarray[i] = ((String)map.get(strarray[i]));
                        }
                        processed = processed + strarray[i];
                        processed = processed + " ";
                    }
                    processed = processed + "\r\n";
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        MiniCCUtil.createAndWriteFile(output, processed);
    }

    public static void main(String[] args) {
        PreProcessor pp = new PreProcessor("test.c");
        pp.preProcess("gg.pp.c");
        System.out.println("PreProcess finished!");
    }
}
