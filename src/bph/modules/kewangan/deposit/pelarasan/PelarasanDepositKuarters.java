package bph.modules.kewangan.deposit.pelarasan;

public class PelarasanDepositKuarters extends PelarasanDeposit{

	private static final long serialVersionUID = 1L;
	
	@Override
	public String getPath() { return "bph/modules/kewangan/tuntutanDeposit/pelarasan/kuarters"; }
	
	public void filtering(){
		this.addFilter("jenisTuntutan.id = '01' ");
	}
	
}
