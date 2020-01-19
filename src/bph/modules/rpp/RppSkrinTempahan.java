package bph.modules.rpp;

public class RppSkrinTempahan extends TempahanRPRecordModule{

	private static final long serialVersionUID = 1L;

	public void redirectSkrinTempahan(){
		context.put("redirectSkrinTempahan", "true");
	}
	
}
