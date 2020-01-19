package bph.modules.kewangan.subsidiari.agihan;

public class AgihanSubsidiariKuarters extends AgihanSubsidiari{

	private static final long serialVersionUID = 1L;
	
	@Override
	public String getPath() { return "bph/modules/kewangan/subsidiari/agihan/kuarters"; }
	
	public void filtering(){
		this.addFilter("jenisSubsidiari.id = '01' ");
	}

}
