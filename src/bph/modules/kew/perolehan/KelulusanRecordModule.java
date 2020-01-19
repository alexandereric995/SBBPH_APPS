/**
 * @author muhdsyazreen
 */

package bph.modules.kew.perolehan;

import java.util.Date;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.http.HttpSession;

import lebah.template.LebahRecordTemplateModule;
import portal.module.entity.Users;
import bph.entities.kew.KewPerolehan;
import bph.entities.kod.Status;
import bph.utils.HTML;
import bph.utils.Util;
import bph.utils.UtilKewangan;
import db.persistence.MyPersistence;

public class KelulusanRecordModule extends LebahRecordTemplateModule<KewPerolehan>{

	private static final long serialVersionUID = 1L;
	UtilKewangan utilKewangan = new UtilKewangan();
	MyPersistence mp;
	
	@SuppressWarnings("rawtypes")
	@Override
	public Class getIdType() {
		// TODO Auto-generated method stub
		return String.class;
	}

	@Override
	public void afterSave(KewPerolehan arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeSave() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void begin() {
		Status statusLulus = null;
		Status statusSokong = null;
		try {
			mp = new MyPersistence();
						
			statusLulus = utilKewangan.getStatus("6","07", mp); /**Status Permohonan Lulus*/
			statusSokong = utilKewangan.getStatus("3","07", mp); /**Status Permohonan Disokong*/			
			
		} catch (Exception e1) {
			System.out.println("Error getting status : "+e1.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		this.addFilter("status.id IN ('" + statusLulus.getId() + "','" + statusSokong.getId() + "')");
		this.setReadonly(true);
		this.setDisableDefaultButton(true);
		this.setDisableBackButton(true);
		this.setDisableKosongkanUpperButton(true);
		this.setDisableSaveAddNewButton(true);
		
		try {
			context.put("selectKodPerolehan", HTML.SelectJenisPerolehan("socKodPerolehan", null, "style=width:325px", null));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Error display dropdown : "+e.getMessage());
		}
		
		context.put("path", getPath());
		context.put("util", new Util());
		context.put("command", command);
		context.put("uploadDir", ResourceBundle.getBundle("dbconnection").getString("folder"));
	}

	@Override
	public boolean delete(KewPerolehan arg0) throws Exception {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return "bph/modules/kewangan/perolehan/kelulusan";
	}

	@Override
	public Class<KewPerolehan> getPersistenceClass() {
		// TODO Auto-generated method stub
		return KewPerolehan.class;
	}

	@Override
	public void getRelatedData(KewPerolehan r) {
		
	}

	@Override
	public void save(KewPerolehan r) throws Exception {
		
		HttpSession session = request.getSession();
		Users users = db.find(Users.class, session.getAttribute("_portal_login"));
		Date sysdate = new Date();
		
		Status status = null;
		
		/**Status by pilihan radio button
		 * L = LULUS 
		 * TL = TIDAK LULUS
		 * */
		
		try {
			mp = new MyPersistence();
			if(get("kelulusan").equalsIgnoreCase("L")){
				status = utilKewangan.getStatus("6", "07", mp);
			}else if(get("kelulusan").equalsIgnoreCase("TL")){
				status = utilKewangan.getStatus("7", "07", mp);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}	
		
		r.setFlagLulus(get("kelulusan"));
		r.setStatus(status); 
		r.setCatatanLulus(get("catatanLulus"));
		r.setIdKemaskini(users);
		r.setTarikhKemaskini(sysdate);
		
	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
