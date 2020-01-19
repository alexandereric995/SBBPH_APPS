package bph.modules.kewangan.deposit.agihan;

public class AgihanDepositRK extends AgihanDeposit{

	private static final long serialVersionUID = 1L;
	
	@Override
	public String getPath() { return "bph/modules/kewangan/tuntutanDeposit/agihan/ruangKomersil"; }
	
	public void filtering(){
		this.addFilter("jenisTuntutan.id = '04' ");
	}

}
