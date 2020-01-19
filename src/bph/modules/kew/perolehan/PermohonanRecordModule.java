/**
 * @author muhdsyazreen
 */

package bph.modules.kew.perolehan;

import java.util.Date;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.http.HttpSession;

import lebah.portal.action.Command;
import lebah.template.DbPersistence;
import lebah.template.LebahRecordTemplateModule;
import portal.module.entity.Users;
import bph.entities.kew.KewPerolehan;
import bph.entities.kod.KodJenisPerolehan;
import bph.entities.kod.Status;
import bph.utils.HTML;
import bph.utils.Util;
import bph.utils.UtilKewangan;
import db.persistence.MyPersistence;

public class PermohonanRecordModule extends LebahRecordTemplateModule<KewPerolehan>{

	private static final long serialVersionUID = 1L;
	UtilKewangan utilKewangan = new UtilKewangan();
	private DbPersistence db = new DbPersistence();
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
		
		Status status = null;
		Status status2 = null;
		Status status3 = null;
		
		try {
			mp = new MyPersistence();
			
			/**Status Pendaftaran*/
			status = utilKewangan.getStatus("1","07", mp);
			/**Status Permohonan Tidak Disokong*/
			status2 = utilKewangan.getStatus("4","07", mp);
			/**Status Permohonan Tidak Lulus*/
			status3 = utilKewangan.getStatus("7","07", mp);
		} catch (Exception e1) {
			System.out.println("Error getting status : "+e1.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		this.addFilter("status.id IN ('"+status.getId()+"','"+status2.getId()+"','"+status3.getId()+"')");
		
		try {
			context.put("selectKodPerolehan", HTML.SelectJenisPerolehan("socKodPerolehan", null, "id=\"socKodPerolehan\" style=\"width:325px\"", "onchange=\" $('err_perolehan').innerHTML=''; at(this, event);\""));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Error display dropdown : "+e.getMessage());
		}
		
		this.setDisableSaveAddNewButton(true);
				
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
		return "bph/modules/kewangan/perolehan/permohonan";
	}

	@Override
	public Class<KewPerolehan> getPersistenceClass() {
		// TODO Auto-generated method stub
		return KewPerolehan.class;
	}

	@Override
	public void getRelatedData(KewPerolehan r) {
		
		context.put("lbl", r);
		
		try {
			context.put("selectKodPerolehan", HTML.SelectJenisPerolehan("socKodPerolehan", r.getKodJenisPerolehan().getId(), "id=\"socKodPerolehan\" style=\"width:325px\"", "onchange=\" $('err_perolehan').innerHTML=''; at(this, event);\""));
		} catch (Exception e) {
			System.out.println("Error display dropdown : "+e.getMessage());
		}		
	}

	@Override
	public void save(KewPerolehan r) throws Exception {
		
		HttpSession session = request.getSession();
		Users users = db.find(Users.class, session.getAttribute("_portal_login"));
		Date sysdate = new Date();
		
		/**Status Pendaftaran*/
		Status status = null;
		try {
			mp = new MyPersistence();
			status = utilKewangan.getStatus("1","07", mp);
		} catch (Exception ex) {
			System.out.println("Error getting status : " + ex.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		r.setStatus(status); 
		r.setKodJenisPerolehan(db.find(KodJenisPerolehan.class, get("socKodPerolehan")));
		r.setJustifikasi(get("justifikasi"));
		r.setTajuk(get("tajuk"));
		r.setUnit(getParam("unit"));
		
		//reset value
		r.setFlagLulus(null);
		r.setFlagSokong(null);
		r.setCatatanLulus(null);
		r.setCatatanSokongan(null);
		
		if(r.getIdMasuk()==null){
			r.setIdMasuk(users);
			r.setTarikhMasuk(sysdate);
		}else{
			r.setIdKemaskini(users);
			r.setTarikhKemaskini(sysdate);
		}
		
		context.put("selectKodPerolehan", HTML.SelectJenisPerolehan("socKodPerolehan", get("socKodPerolehan"), "id=\"socKodPerolehan\" style=\"width:325px\"", "onchange=\" $('err_perolehan').innerHTML=''; at(this, event);\""));
	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
	@Command("doHantarSokongan")
	public void doHantarSokongan() throws Exception {

		HttpSession session = request.getSession();
		Users users = db.find(Users.class, session.getAttribute("_portal_login"));
		Date sysdate = new Date();
		
		/**Status Permohonan Baru*/
		Status status = null;
		try {
			mp = new MyPersistence();
			status = utilKewangan.getStatus("2","07", mp);
		} catch (Exception ex) {
			System.out.println("Error getting status : " + ex.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		KewPerolehan r = db.find(KewPerolehan.class, get("id"));
		db.begin();
		r.setStatus(status);
		r.setIdKemaskini(users);
		r.setTarikhKemaskini(sysdate);
		db.persist(r);
		db.commit();
		
		listPage();
	
	}

}
