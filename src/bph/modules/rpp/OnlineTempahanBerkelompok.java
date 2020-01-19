package bph.modules.rpp;

public class OnlineTempahanBerkelompok extends TempahanBerkelompok{

	private static final long serialVersionUID = 1L;

	public void filteringIndividu(String userId) {
		userId = (String) request.getSession().getAttribute("_portal_login");
		this.addFilter("pemohon.id = '"+userId+"'");
		this.addFilter("status.id not in ('1435093978588')");
	}

}
