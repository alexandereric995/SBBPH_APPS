package bph.modules.rpp;

public class SemakanKekosonganOlehPenyeliaRecordModule extends TempahanRPRecordModule{

	private static final long serialVersionUID = 1L;

	public void redirectSkrinTempahan(){
		context.put("redirectSkrinTempahan", "true");
	}
	
}
