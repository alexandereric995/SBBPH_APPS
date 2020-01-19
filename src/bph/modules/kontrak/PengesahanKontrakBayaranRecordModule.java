package bph.modules.kontrak;


public class PengesahanKontrakBayaranRecordModule extends SenaraiKontrakBayaranRecordModule {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public void doOverideFilterRecord() {
		
		clearFilter();
		this.addFilter("status in ('02')");
		this.setOrderBy("id");
		this.setOrderType("asc");
		
		this.setReadonly(true);
		this.setDisableUpperBackButton(true);
	}
		
}
