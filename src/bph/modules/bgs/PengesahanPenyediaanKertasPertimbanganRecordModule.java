package bph.modules.bgs;


public class PengesahanPenyediaanKertasPertimbanganRecordModule extends SenaraiPermohonanBGSRecordModule {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doOverideFilterRecord() {
		clearFilter();
		
		this.addFilter("status.id in ('1423822397722')"); //PENGESAHAN KERTAS PERTIMBANGAN
		
		this.setReadonly(true);
		
		this.setOrderBy("tarikhPermohonan");
		this.setOrderType("desc");
		
	}
}
