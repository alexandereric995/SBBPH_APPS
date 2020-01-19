package bph.modules.qtr.agihan;

import java.util.List;

import portal.module.entity.Users;
import bph.entities.kod.KelasKuarters;
import bph.entities.kod.SebabPenolakan;
import bph.entities.qtr.KuaAgihan;
import bph.modules.qtr.FrmKuaAgihanRecord;
import bph.utils.DataUtil;
import db.persistence.MyPersistence;

public class FrmKuaBersyaratRecord extends FrmKuaAgihanRecord {
	private static final long serialVersionUID = -8134008276663362859L;
	private DataUtil dataUtil;
	private MyPersistence mp;

	@Override
	public void begin() {
		dataUtil = DataUtil.getInstance(db);
		setDisableKosongkanUpperButton(true);
		setDisableSaveAddNewButton(true);
		setDisableDefaultButton(true);
		setDisableUpperBackButton(true);
		setDisableAddNewRecordButton(true);
		setDisableBackButton(true);
		setHideDeleteButton(true);
		userId = (String) request.getSession().getAttribute("_portal_login");
		userRole = (String) request.getSession().getAttribute("_portal_role");
		try {
			mp = new MyPersistence();
			context.put("currentRoleQTR", userRole);
			addFilter("status.id = '1431405647231'");// status tolak bersyarat
			addFilter("noGiliran > 0");
			addFilter("flagSemakanPelulus = 2");
			addFilter("flagMenungguBersyarat = 1");
			setOrderBy("noGiliran ASC");
			context.put("bersyarat", "ya");
			context.put("personal", "tidak");
			context.put("findKelasKuarters", dataUtil.getListKelasPeranginan());
			context.put("findLokasiKuarters", dataUtil.getListLokasiPermohonan());
			context.put("path", getPath());
		} catch (Exception e) {
			System.out.println("Error begin : " + e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}
	}

	// @SuppressWarnings("unchecked")
	// @Override
//	public void getRelatedData(KuaAgihan r) {
//		try {
//			mp = new MyPersistence();
//			KuaAgihan rr = (KuaAgihan) mp.find(KuaAgihan.class, r.getId());
//			context.put("r", rr);
//			String kelas1 = "";
//			KelasKuarters k1 = (KelasKuarters) mp.find(KelasKuarters.class, r.getKelasKuarters());
//			String idLokasi = r.getPermohonan().getLokasi().getId();
//			if (k1 != null) {
//				kelas1 = k1.getId();
//			} else {
//				kelas1 = r.getKelasKuarters();
//			}
//
//			context.put("kuarters", dataUtil.getListKuarters1(idLokasi, kelas1.replaceAll("1", "")));
//			context.put("kelas1", kelas1);
//			if ("".equals(r.getPermohonan().getFlagDowngrade()))
//				context.put("kelas2", dataUtil.getKelasDowngrade(r.getKelasKuarters()));
//
//			if (r.getKuarters() != null)
//				context.put("kuarters", mp.find(KuaKuarters.class, r.getKuarters().getId()));
//
//			List<Users> users = mp.list("SELECT u FROM Users u WHERE u.id IN (" + getPT()
//					+ ") OR u.role.description = '(QTR) Penyedia' ORDER BY u.userName ASC");
//			context.put("selectPetugas", users);
//
//			List<SebabPenolakan> sp = mp.list("SELECT u FROM SebabPenolakan u");
//			context.put("selectPenolakan", sp);
//
//		} catch (Exception e) {
//			System.out.println("Error getRelatedData : " + e.getMessage());
//		} finally {
//			if (mp != null) { mp.close(); }
//		}
//
//	}
	
//	@SuppressWarnings("unchecked")
//	@Override
//	public void getRelatedData(KuaAgihan r) {
//		try {
//			mp = new MyPersistence();
//			KuaAgihan rr = (KuaAgihan) mp.find(KuaAgihan.class, r.getId());
//			context.put("r", rr);
//			String kelas1 = "";
//			KelasKuarters k1 = (KelasKuarters) mp.find(KelasKuarters.class, r.getKelasKuarters());
//			String idLokasi = r.getPermohonan().getLokasi().getId();
//			if (k1 != null) {
//				kelas1 = k1.getId();
//			} else {
//				kelas1 = r.getKelasKuarters();
//			}
//			context.put("kuarters", dataUtil.getListKuarters1(idLokasi, kelas1.replaceAll("1", "")));
//			context.put("kelas1", kelas1);
//			if ("".equals(r.getPermohonan().getFlagDowngrade()))
//				context.put("kelas2", dataUtil.getKelasDowngrade(r.getKelasKuarters()));
//			if (r.getKuarters() != null)
//				context.put("kuarters", mp.find(KuaKuarters.class, r.getKuarters().getId()));
//			List<Users> users = mp.list("SELECT u FROM Users u WHERE u.id IN (" + getPT()
//					+ ") OR u.role.description = '(QTR) Penyedia' ORDER BY u.userName ASC");
//			context.put("selectPetugas", users);
//			List<SebabPenolakan> sp = mp.list("SELECT u FROM SebabPenolakan u");
//			context.put("selectPenolakan", sp);
//		} catch (Exception e) {
//			System.out.println("Error getRelatedData : " + e.getMessage());
//		} finally {
//			if (mp != null) { mp.close(); }
//		}
//	}
	

	@SuppressWarnings("unchecked")
	@Override
	public void getRelatedData(KuaAgihan r) {
		try {
			mp = new MyPersistence();
			KuaAgihan rr = (KuaAgihan) mp.find(KuaAgihan.class, r.getId());
			context.put("r", rr);
			String kelas1 = "";
			KelasKuarters k1 = (KelasKuarters) mp.find(KelasKuarters.class, r.getKelasKuarters());
			String idLokasi = r.getPermohonan().getLokasi().getId();
			if (k1 != null) {
				kelas1 = k1.getId();
			} else {
				kelas1 = r.getKelasKuarters();
			}
			if(idLokasi.equalsIgnoreCase("02"))
			{
				context.put("kuarters", dataUtil.getListKuartersKLTawaran(idLokasi));
			}
			else
			{
				context.put("kuarters", dataUtil.getListKuartersTawaran(idLokasi, kelas1.replaceAll("1", "")));
			}
			context.put("kelas1", kelas1);
			if ("".equals(r.getPermohonan().getFlagDowngrade())){
				context.put("kelas2", dataUtil.getKelasDowngrade(r.getKelasKuarters()));
			}	
//			if (r.getKuarters() != null){
//				context.put("kuarters", mp.find(KuaKuarters.class, r.getKuarters().getId()));
//			}
			List<Users> users = mp.list("SELECT u FROM Users u WHERE u.id IN (" + getPT()+ ") OR u.role.description = '(QTR) Penyedia' ORDER BY u.userName ASC");
			context.put("selectPetugas", users);
			List<SebabPenolakan> sp = mp.list("SELECT u FROM SebabPenolakan u");
			context.put("selectPenolakan", sp);
		} catch (Exception e) {
			System.out.println("Error getRelatedData : " + e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}
	}

}
