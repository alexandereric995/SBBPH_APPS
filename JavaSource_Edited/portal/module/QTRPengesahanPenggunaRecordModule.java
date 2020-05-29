package portal.module;

public class QTRPengesahanPenggunaRecordModule extends
		PengesahanPenggunaRecordModule {

	private static final long serialVersionUID = 1L;

	public void doOverideFilterRecord() {
		this.setReadonly(true);
		this.setDisableDefaultButton(true);
		this.setDisableBackButton(true);

		this.addFilter("flagDaftarSBBPH = 'Y'");
		this.addFilter("flagAktif != 'Y'");
		this.addFilter("dokumenSokongan != null");
		this.addFilter("flagMenungguPengesahan = 'Y'");
		this.addFilter("flagUrusanPemohon in ('1', '3')");
		this.addFilter("role.name in ('(AWAM) Badan Berkanun', '(AWAM) Polis / Tentera', '(AWAM) Pesara Polis / Tentera')");

		this.setOrderBy("dateRegistered");
		this.setOrderType("asc");
	}
}
