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
import bph.entities.integrasi.IntJPN;
import bph.utils.Util;
import db.persistence.MyPersistence;

public class JPNRecordModule extends LebahRecordTemplateModule <IntJPN>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static Logger myLogger = Logger.getLogger("JPNRecordModule");
	private MyPersistence mp;
	
	@Override
	public Class getIdType() {
		// TODO Auto-generated method stub
		return String.class;
	}

	@Override
	public void afterSave(IntJPN r) {
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
	public boolean delete(IntJPN r) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return "bph/modules/integrasi/JPN";
	}

	@Override
	public Class<IntJPN> getPersistenceClass() {
		// TODO Auto-generated method stub
		return IntJPN.class;
	}

	@Override
	public void getRelatedData(IntJPN r) {
		// TODO Auto-generated method stub
		context.remove("success");
	}

	@Override
	public void save(IntJPN r) throws Exception {
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
	
	@Command("semakJPN")
	public String semakJPN() throws Exception {
		
		IntJPN jpn = db.find(IntJPN.class, get("idJPN"));
		
		if (jpn != null) {			
			
			try {
				System.out.println("VALIDATE JPN USING BPH WEB SERVICE");
				org.tempuri.crsservice.JpnManager jpnManagerService = new org.tempuri.crsservice.JpnManager();
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
				
				jpnManagerService = bphService.retreiveCitizensData(jpn.getNoPengenalan(), jpn.getNama(), "T7");
				
				if (jpnManagerService.isFlagSemakanJPN()) {
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
			jpn = (IntJPN) mp.find(IntJPN.class, jpn.getId());
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null)
				mp.close();
		}
		context.put("r", jpn);
		return getPath() + "/entry_page.vm";
	}
}
