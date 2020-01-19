package bph.modules.rpp;

public class SkrinTempahanWalkin extends TempahanWalkIn{

	private static final long serialVersionUID = 1L;

	public void redirectSkrinTempahan(){
		context.put("redirectSkrinTempahan", "true");
	}
	
}
