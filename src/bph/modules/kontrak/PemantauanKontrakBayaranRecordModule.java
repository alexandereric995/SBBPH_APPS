package bph.modules.kontrak;


public class PemantauanKontrakBayaranRecordModule extends SenaraiKontrakBayaranRecordModule {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public void doOverideFilterRecord() {
		
		clearFilter();
		this.addFilter("status in ('04', '05', '06')"); // 01=DAFTAR KONTRAK, 02=PENGESAHAN MAKLUMAT KONTRAK, 03=PINDAAN MAKLUMAT KONTRAK, 04=KONTRAK AKTIF, 05=KONTRAK TAMAT, 06=KONTRAK BATAL
		this.setOrderBy("id");
		this.setOrderType("asc");
		
		this.setReadonly(true);
		this.setDisableUpperBackButton(true);
		
		context.remove("flagAllowUpdate");
	}
		
}
