package testing;

import lebah.portal.action.LebahModule;

public class PluginTutor extends LebahModule {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2672383490912651329L;

	@Override
	public String start() {
		// TODO Auto-generated method stub
		return getPath() + "/start.vm";
	}

	public String getPath() {
		// TODO Auto-generated method stub
		return "bph/testing/pluginTutor";
	}
	
}
