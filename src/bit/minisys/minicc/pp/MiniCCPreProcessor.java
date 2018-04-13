package bit.minisys.minicc.pp;

public class MiniCCPreProcessor implements IMiniCCPreProcessor{
    @java.lang.Override
    public void run(String iFile, String oFile) {
        PreProcessor pp = new PreProcessor(iFile);
        pp.preProcess(oFile);
    }
}
