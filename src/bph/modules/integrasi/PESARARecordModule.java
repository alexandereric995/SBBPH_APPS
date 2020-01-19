package bph.modules.integrasi;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.xml.ws.BindingProvider;

import lebah.portal.action.Command;
import lebah.template.LebahRecordTemplateModule;

import org.apache.log4j.Logger;

import sbbph.ws.BPHServices;
import sbbph.ws.BPHServicesImplService;
import bph.entities.integrasi.IntPESARA;
import bph.utils.Util;
import db.persistence.MyPersistence;

public class PESARARecordModule extends LebahRecordTemplateModule <IntPESARA>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static Logger myLogger = Logger.getLogger("PESARARecordModule");
	private MyPersistence mp;
	
	@Override
	public Class getIdType() {
		// TODO Auto-generated method stub
		return String.class;
	}

	@Override
	public void afterSave(IntPESARA r) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeSave() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void begin() {
		// TODO Auto-generated method stub		
		this.setReadonly(true);
		this.setDisableBackButton(true);
		
		this.setOrderBy("tarikhTerima");
		this.setOrderType("desc");

		context.put("util", new Util());
		context.put("path", getPath());
		context.put("command", command);
		context.put("uploadDir", ResourceBundle.getBundle("dbconnection").getString("folder"));

	}

	@Override
	public boolean delete(IntPESARA r) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return "bph/modules/integrasi/PESARA";
	}

	@Override
	public Class<IntPESARA> getPersistenceClass() {
		// TODO Auto-generated method stub
		return IntPESARA.class;
	}

	@Override
	public void getRelatedData(IntPESARA r) {
		// TODO Auto-generated method stub
		context.remove("success");
	}

	@Override
	public void save(IntPESARA r) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		// TODO Auto-generated method stub
		Map<String, Object> m = new HashMap<String, Object>();
		
		m.put("noPengenalan", getParam("findNoPengenalan"));
		m.put("nama", getParam("findNama"));
		
		return m;
	}
	
	@Command("semakPESARA")
	public String semakPESARA() throws Exception {
		
		IntPESARA pesara = db.find(IntPESARA.class, get("idPESARA"));
		
		if (pesara != null) {
			try {
				System.out.println("VALIDATE PESARA USING BPH WEB SERVICE");
				
				BPHServicesImplService service = new BPHServicesImplService();
				BPHServices bphService = service.getPort(BPHServices.class);
				// EXTRA CONTEXT
				String WS_URL = ResourceBundle.getBundle("dbconnection").getString("WS_URL");
				int connectionTimeOutInMs = 10000;// 10 Second
				BindingProvider p = (BindingProvider) bphService;
				p.getRequestContext().put(
						BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
						WS_URL);
				myLogger.debug("requesting service from=> :" + bphService.toString());
				
				sbbph.ws.PesaraManager pesaraManagerService = null;
				
				
				pesaraManagerService = bphService.semakanPesara(pesara.getNoPengenalan());
				
				if (pesaraManagerService.isFlagSemakanPESARA()) {
					context.put("success", "Y");
				} else {
					context.put("success", "T");
				}
			} catch (Exception ex) {
				System.out.println("ERROR ACCESS BPH WEB SERVICE : " + ex.getMessage());
			}
		}
		
		try {
			mp = new MyPersistence();
			pesara = (IntPESARA) mp.find(IntPESARA.class, pesara.getId());
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null)
				mp.close();
		}
		context.put("r", pesara);
		return getPath() + "/entry_page.vm";
	}
}
