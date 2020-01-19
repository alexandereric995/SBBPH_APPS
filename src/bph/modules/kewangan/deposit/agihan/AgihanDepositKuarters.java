package bph.modules.kewangan.deposit.agihan;

public class AgihanDepositKuarters extends AgihanDeposit{

	private static final long serialVersionUID = 1L;
	
	@Override
	public String getPath() { return "bph/modules/kewangan/tuntutanDeposit/agihan/kuarters"; }
	
	public void filtering(){
		this.addFilter("jenisTuntutan.id = '01' ");
	}

}
