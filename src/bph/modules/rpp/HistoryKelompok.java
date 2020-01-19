package bph.modules.rpp;

import bph.utils.DataUtil;


public class HistoryKelompok extends RppHistoryRecordModule{

	private static final long serialVersionUID = 1L;
	private DataUtil dataUtil;
	
	public void addfilter() {
		dataUtil = DataUtil.getInstance(db);
		this.addFilter("jenisPemohon in ('KELOMPOK') ");
		context.put("listPeranginan", dataUtil.getListPeranginanKelompok());
	}

}
