package bph.laporan.kewangan;

import lebah.portal.action.LebahModule;
import lebah.template.DbPersistence;
import bph.utils.DataUtil;

public class KEWPenyataPemungutRecordModule extends LebahModule{

	private static final long serialVersionUID = 1L;
	private DbPersistence db = new DbPersistence();
	private DataUtil dataUtil = DataUtil.getInstance(db);

	@Override
	public String start() {
		context.put("path", getPath());
		return getPath() + "/PenyataPemungut.vm";
	}
	
	private String getPath() {
		return "bph/laporan/kewangan";
	}
	
	
	
	
}
