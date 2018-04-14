package bit.minisys.minicc.pp;


// Author: CFhM_R
// Date: 2018-04-14


public class MiniCCPreProcessor implements IMiniCCPreProcessor{
    @java.lang.Override
    public void run(String iFile, String oFile) {
        PreProcessor pp = new PreProcessor(iFile);
        pp.preProcess(oFile);
    }
}
