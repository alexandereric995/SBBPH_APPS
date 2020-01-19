package bph.modules.kewangan.deposit.lejar.individu;

public class HistoryLejarIndividuIR extends LejarIndividuIR{

	private static final long serialVersionUID = 1L;
	
	@Override
	public String getPath() { return "bph/modules/kewangan/deposit/lejar/individu/rumahPeranginan"; }
	
	public void filtering(){
		//display yang dah terima pulangan deposit
		this.addFilter("jenisBayaran.id = '02' ");
		this.addFilter("kodHasil.id = '72311' and COALESCE(x.flagWarta,'T') <> 'Y' and COALESCE(x.flagPulangDeposit,'T') = 'Y'");
	}

}
