package bph.modules.kewangan.subsidiari.pelarasan;

public class PelarasanSubsidiariKuarters extends PelarasanSubsidiari{

	private static final long serialVersionUID = 1L;
	
	@Override
	public String getPath() { return "bph/modules/kewangan/subsidiari/pelarasan/kuarters"; }
	
	public void filtering(){
		this.addFilter("jenisSubsidiari.id = '01' ");
	}
	
}
