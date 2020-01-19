package bph.modules.kewangan.deposit.lejar.individu;

public class LejarIndividuRK extends LejarIndividu{

	private static final long serialVersionUID = 1L;
	
	@Override
	public String getPath() { return "bph/modules/kewangan/deposit/lejar/individu/ruangKomersil"; }
	
	public void filtering(){
		//display yang dah terima pulangan deposit
		this.addFilter("jenisBayaran.id = '04' ");
		this.addFilter("kodHasil.id = '79503' and COALESCE(x.flagWarta,'T') <> 'Y' and COALESCE(x.flagBayar,'T') = 'Y'");
	}
	
	public void disabledButton(){
		this.setDisableKosongkanUpperButton(true);
		this.setReadonly(true);
		this.setDisableBackButton(true);
	}

}
