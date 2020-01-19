package bph.dashboard.qtr;

import lebah.portal.action.LebahModule;

public class FrmDashboard extends LebahModule{

	private static final long serialVersionUID = 1L;

	@Override
	public String start() {
		
		String portal_role  = (String)request.getSession().getAttribute("_portal_role");
		context.put("portal_role", portal_role);
		
		context.put("path", getPath());
		return getPath() + "/start.vm";
	}
	
	private String getPath() {
		return "bph/dashboard/qtr";
	}
	
}
