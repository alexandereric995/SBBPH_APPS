package bph.modules.bgs;


public class SemakanPenyediaanKertasPertimbanganRecordModule extends SenaraiPermohonanBGSRecordModule {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doOverideFilterRecord() {
		clearFilter();
		
		this.addFilter("status.id = '1423568441682'"); //SEMAKAN KERTAS PERTIMBANGAN
		
		this.setReadonly(true);
		this.setOrderBy("tarikhPermohonan");
		this.setOrderType("desc");
		
	}
}
