package bph.modules.qtr;
import java.util.List;

import org.apache.log4j.Logger;

import portal.module.entity.Users;
import bph.entities.qtr.KuaPermohonan;
import bph.utils.DataUtil;
import db.persistence.MyPersistence;

public class FrmKuaPermohonanSubRecordHistory extends FrmKuaPermohonanRecord {

	private static final long serialVersionUID = -302245884484195171L;
	static Logger myLogger = Logger.getLogger("bph/modules/qtr/FrmKuaPermohonanSubRecord");
	private DataUtil dataUtil;
	private MyPersistence mp;

	@SuppressWarnings("unchecked")
	@Override
	public void begin() {
		dataUtil = DataUtil.getInstance(db);
		userId = (String) request.getSession().getAttribute("_portal_login");
		setRecordOnly(true);
		setHideDeleteButton(true);
		setDisableDefaultButton(true);
		setDisableBackButton(true);
		setDisableKosongkanUpperButton(true);
		setDisableUpperBackButton(true);
		setDisableSaveAddNewButton(true);
		setDisableAddNewRecordButton(true);
		this.setDisableInfoPaparLink(true);
		userRole = (String) request.getSession().getAttribute("_portal_role");
		addFilter("pemohon.id = '" + userId + "'");
		addFilter("status.id in ('1431903258428','1419601227598','1431327994521','1431327994524')");
		context.remove("blacklisted");
		context.remove("currentRoleQTR");
		context.put("Awam", true);
		try {
			mp = new MyPersistence();
			userId = (String) request.getSession().getAttribute("_portal_login");
			Users u = (Users) mp.find(Users.class, userId);
			context.put("users", u);
			List<KuaPermohonan> kpList = mp.list("SELECT kp FROM KuaPermohonan kp WHERE kp.pemohon.id = '"+ userId + "'");
			boolean a = false;
			// AZAM CHANGE ON 16/1/2016
			for (KuaPermohonan r : kpList) {
				if (r.getStatus() != null) {
					if ("1431903258428".equals(r.getStatus().getId()) // KELUAR
							// KUARTERS
							|| "1419601227598".equals(r.getStatus().getId())// PERMOHONAN
							// DITOLAK
							|| "1431327994521".equals(r.getStatus().getId())// KUARTERS
							// DITOLAK
							|| "1431327994524".equals(r.getStatus().getId()) // PERMOHONAN
					// DIBATALKAN
					) {
						a = false;
					} else {
						a = true;
						break;
					}
				}
			}
			
			List<KuaPermohonan> listPermohonan = mp.list("SELECT kp FROM KuaPermohonan kp WHERE kp.pemohon.id = '"+ userId + "'");
			context.put("listPermohonan", listPermohonan);
			context.put("selectNegeriPekerjaan", dataUtil.getListNegeri());
			context.put("selectNegeriPinjaman", dataUtil.getListNegeri());
			context.put("selectLokasiPermohonan", dataUtil.getListLokasiPermohonan());
			context.put("newRecord", "true");
		} catch (Exception e) {
			System.out.println("Error begin : " + e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}
	}
}
