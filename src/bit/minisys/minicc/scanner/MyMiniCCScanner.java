package bit.minisys.minicc.scanner;


// Author: CFhM_R
// Date: 2018-04-14


import java.io.IOException;

public class MyMiniCCScanner implements IMiniCCScanner {
    public void run(String iFile, String oFile) throws IOException, Exception {
        System.out.println("called");
        Scanner.run(iFile, oFile);
    }
}
