package bph.modules.rpp;

import bph.utils.DataUtil;


public class HistoryRumahTransit extends RppHistoryRecordModule{

	private static final long serialVersionUID = 1L;
	private DataUtil dataUtil;
	
	public void addfilter() {
		dataUtil = DataUtil.getInstance(db);
		this.addFilter("jenisPemohon in ('INDIVIDU','TRANSIT') ");
		this.addFilter("rppPeranginan.jenisPeranginan.id = 'RT' ");
		context.put("listPeranginan", dataUtil.getListPeranginanRTByGred(null));
	}

}
