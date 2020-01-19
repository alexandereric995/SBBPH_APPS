package bph.modules.bgs;


public class SenaraiPermohonanBGSPenyediaRecordModule extends SenaraiPermohonanBGSRecordModule {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doOverideFilterRecord() {
		clearFilter();
		
		this.addFilter("status.id in ('1423568441671', '1423568441688', '1423568441694', '1423568441691', '1423568441697', '1423568441700')"); 
		//PERMOHONAN BARU || PENYEDIAAN KERTAS PERTIMBANGAN || PINDAAN KERTAS PERTIMBANGAN || KEPUTUSAN SUB || LULUS || TOLAK
		
		this.setOrderBy("tarikhPermohonan");
		this.setOrderType("desc");
	}
}
