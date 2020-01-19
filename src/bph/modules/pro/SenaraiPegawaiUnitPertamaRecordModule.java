package bph.modules.pro;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lebah.portal.action.Command;
import lebah.template.LebahRecordTemplateModule;

import org.apache.log4j.Logger;

import portal.module.entity.Users;
import bph.entities.kod.Bahagian;
import bph.entities.kod.Seksyen;
import bph.entities.pro.ProPegawaiUnitPertama;
import bph.utils.DataUtil;
import bph.utils.Util;
import db.persistence.MyPersistence;

public class SenaraiPegawaiUnitPertamaRecordModule extends LebahRecordTemplateModule<ProPegawaiUnitPertama>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static Logger myLogger = Logger.getLogger(ProPegawaiUnitPertama.class);
	private DataUtil dataUtil;
	private Util util = new Util();
	private MyPersistence mp;
	
	
	@Override
	public Class getIdType() {
		// TODO Auto-generated method stub
		return ProPegawaiUnitPertama.class;
	}

	@Override
	public void afterSave(ProPegawaiUnitPertama r) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeSave() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void begin() {
		// TODO Auto-generated method stub
		dataUtil = DataUtil.getInstance(db);
		this.setDisableSaveAddNewButton(true);
		
		try {			
			mp = new MyPersistence();
			
			context.remove("pegawai");
			context.put("selectSeksyen", dataUtil.getListSeksyen());	//Ini adalah UNIT
			context.put("selectBahagian", dataUtil.getListBahagian()); 	//Ini adalah SEKSYEN
			
		} catch (Exception ex) {
			System.out.println("ERROR INSERT DOKUMEN : " + ex.getMessage());
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
	}

	@Override
	public boolean delete(ProPegawaiUnitPertama delete) throws Exception {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		
		return "bph/modules/pro/senaraiPegawaiUnitPertama";
	}

	@Override
	public Class<ProPegawaiUnitPertama> getPersistenceClass() {
		// TODO Auto-generated method stub
		return ProPegawaiUnitPertama.class;
	}

	@Override
	public void getRelatedData(ProPegawaiUnitPertama r) {
		// TODO Auto-generated method stub
		
		//Start - Koding Dropdown Bahagian - dimana apabila pilih dropdown bahagian akan paparkan list Unit 
		if (r.getSeksyen() != null) {
			if (r.getSeksyen().getBahagian() != null) {
				context.put("selectSeksyen", dataUtil.getListSeksyen(r.getSeksyen().getBahagian().getId()));
				//System.out.println("PRINT ====:" + r.getSeksyen().getBahagian().getId());
			}
		}
		//End
	}

	@Override
	public void save(ProPegawaiUnitPertama simpan) throws Exception {
		// TODO Auto-generated method stub
			
		try {
			mp = new MyPersistence();			

			simpan.setPegawai((Users) mp.find(Users.class, getParam("pegawai")));
			simpan.setBahagian((Bahagian) mp.find(Bahagian.class, getParam("idBahagian"))); //adalah seksyen
			simpan.setSeksyen((Seksyen) mp.find(Seksyen.class, getParam("idSeksyen")));	//adalah Unit
			simpan.setFlagAktif("Y");	
				
		} catch (Exception ex) {
			System.out.println("ERROR INSERT DOKUMEN : " + ex.getMessage());
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		// TODO Auto-generated method stub
		
		String findIc = getParam("find_ic");
//		System.out.println("findIc ====" + findIc);
		HashMap<String, Object> cari = new HashMap<String, Object>();
		cari.put("pegawai.id", findIc);
		
		return cari;
		
	}
	
	
	/**** function apabila click button Cari ****/
	@Command("cariUser")
	public String cariUser() {
		
		String noKp = get("kadPengenalan");
		//myLogger.debug("String noKp ===== " + noKp);
		
		List<Users> userList = searchListUser(noKp);
		//myLogger.debug("ceking userList ===== " + userList.size());
		context.put("userList", userList);
		
		return getPath() + "/popupCarianUser.vm";
	}
	
	private List<Users> searchListUser(String noKp) {
		//myLogger.debug("No KP ===== " + noKp);
		
		List<Users> list = new ArrayList<Users>();
			
			String sql = "select x from Users x";
			//myLogger.debug("String sql ===== " + sql);
			
			if(!noKp.equalsIgnoreCase("")){
				sql = sql + " where x.id like '%" + noKp + "%'";
			}
			//myLogger.debug("ceking sql where id ===== " + sql);
			list = db.list(sql);
	
		return list;
	}
	/**** End function apabila click button Cari ****/
	
	//Function pilih pegawai agihan daripada popup yg dicari
	@Command("pilihPegawai")
	public String pilihPegawai() throws Exception {
		
		String strRadioPegawai = getParam("radioPegawai");
		//System.out.println("ceking radioPegawai =======" + getParam("radioPegawai"));
		
		try {			
			mp = new MyPersistence();
			
			Users objUser = (Users) mp.find(Users.class, strRadioPegawai);
			context.put("pegawai", objUser);
			//objuser = (Users)mp.find(Users.class, findPayerId);
			//System.out.println("ceking objUser =======" + objUser.getId());
			
		} catch (Exception ex) {
			System.out.println("ERROR INSERT DOKUMEN : " + ex.getMessage());
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}

		return getPath() + "/entry_page.vm";
	}
	
	
	//START dropdown Bahagian akan keluar unit dibawah bahagian yang dipilih
	@Command("selectSeksyen")
	public String selectSeksyen() throws Exception {	
		
		String strIdBahagian = "0";
		if (get("idBahagian").trim().length() > 0){
			strIdBahagian = get("idBahagian");
		}
			
		List<Seksyen> list = dataUtil.getListSeksyen(strIdBahagian);
		context.put("selectSeksyen", list);
		
		return getPath() + "/selectSeksyen.vm";
	}
	//END dropdown Bahagian
		
	
}
