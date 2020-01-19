package bph.mobile.module;

import lebah.portal.action.LebahModule;

public class MobileSubKesejahteraan extends LebahModule {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1588738227456438264L;

	@Override
	public String start() {
		// TODO Auto-generated method stub
		return getPath() + "/kesejahteraan-mobile.vm";
	}

	private String getPath() {
		return "bph/mobile/module/kuarters";
	}
	
}
