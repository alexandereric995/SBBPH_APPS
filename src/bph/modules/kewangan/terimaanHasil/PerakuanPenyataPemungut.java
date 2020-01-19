package bph.modules.kewangan.terimaanHasil;

import bph.utils.DataUtil;

public class PerakuanPenyataPemungut extends PenyataPemungutRecordModule{

	private static final long serialVersionUID = 1L;
	private DataUtil dataUtil;
	
	public void defaultButtonOption() {
		this.setReadonly(true);
		this.setDisableDefaultButton(true);
		this.setDisableBackButton(true);
		this.setDisableKosongkanUpperButton(true);
		this.setDisableSaveAddNewButton(true);
		this.setHideDeleteButton(true);
		
		this.setOrderBy("tarikhPenyataPemungut");
		this.setOrderType("desc");
	}

}
