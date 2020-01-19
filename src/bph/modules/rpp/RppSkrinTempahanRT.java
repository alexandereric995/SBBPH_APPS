package bph.modules.rpp;

public class RppSkrinTempahanRT extends TempahanRT{

	private static final long serialVersionUID = 1L;

	public void redirectSkrinTempahan(){
		context.put("redirectSkrinTempahan", "true");
	}
	
}
