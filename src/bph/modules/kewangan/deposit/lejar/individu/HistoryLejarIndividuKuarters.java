package bph.modules.kewangan.deposit.lejar.individu;

public class HistoryLejarIndividuKuarters extends LejarIndividuKuarters{

	private static final long serialVersionUID = 1L;
	
	public void filtering(){
		//display yang dah terima pulangan deposit
		this.addFilter("jenisBayaran.id = '01' ");
		this.addFilter("kodHasil.id = '72310' and COALESCE(x.flagWarta,'T') <> 'Y' and COALESCE(x.flagPulangDeposit,'T') = 'Y'");
	}
	
}
