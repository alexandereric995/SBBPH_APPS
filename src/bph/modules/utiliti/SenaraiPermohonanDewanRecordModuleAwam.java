package bph.modules.utiliti;

import lebah.portal.action.LebahModule;

import org.apache.log4j.Logger;

public class SenaraiPermohonanDewanRecordModuleAwam extends LebahModule {
	private static final long serialVersionUID = 1779932252980602112L;
	static Logger myLogger = Logger.getLogger("bph/modules/util/SenaraiPermohonanDewanRecordModuleAwam");
	
	@Override
	public String start() {
		return getPath() + "/start.vm";
	}

	public String getPath() {
		return "bph/modules/utiliti/senaraiPermohonanDewanAwam";
	}
}
