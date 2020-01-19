package bph.modules.kewangan.deposit.lejar.individu;

import java.util.HashMap;
import java.util.Map;

public class LejarIndividuKuarters extends LejarIndividu{

	private static final long serialVersionUID = 1L;
	
	@Override
	public String getPath() { return "bph/modules/kewangan/deposit/lejar/individu/kuarters"; }
	
	public void filtering(){
		//display yang dah terima pulangan deposit
		this.addFilter("jenisBayaran.id = '01' ");
		this.addFilter("kodHasil.id = '72310' and COALESCE(x.flagWarta,'T') <> 'Y' and COALESCE(x.flagBayar,'T') = 'Y'");
	}
	
	public void disabledButton(){
		this.setDisableKosongkanUpperButton(true);
		this.setReadonly(true);
		this.setDisableBackButton(true);
	}
	
	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("pendeposit.userName", getParam("findUserName"));
		map.put("pendeposit.noKP", getParam("findNoKP"));
		map.put("flagPulangDeposit", getParam("findFlagPulangDeposit"));
		return map;
	}
	
}
