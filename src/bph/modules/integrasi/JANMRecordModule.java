package bph.modules.integrasi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import lebah.portal.action.Command;
import lebah.template.DbPersistence;
import lebah.template.LebahRecordTemplateModule;
import lebah.template.OperatorEqualTo;
import lebah.template.UID;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import portal.module.entity.Users;
import bph.entities.integrasi.IntJANM;
import bph.entities.integrasi.IntJANMRekod;
import bph.entities.kod.CawanganJANM;
import bph.entities.kod.JabatanPembayarJANM;
import bph.utils.DataUtil;
import bph.utils.Util;
import db.persistence.MyPersistence;

public class JANMRecordModule extends LebahRecordTemplateModule<IntJANM> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private MyPersistence mp;
	private DataUtil dataUtil;

	@SuppressWarnings("rawtypes")
	@Override
	public Class getIdType() {
		// TODO Auto-generated method stub
		return String.class;
	}

	@Override
	public void afterSave(IntJANM r) {
		// TODO Auto-generated method stub
		File file = new File(ResourceBundle.getBundle("dbconnection").getString("folder") + r.getFileDir());
		try {
			readFile(file, r, db);	
			file.delete();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		List<IntJANMRekod> listRekod = db.list("select x from IntJANMRekod x where x.janm.id = '" + r.getId() + "' order by x.id asc");
		context.put("listRekod", listRekod);
	}
	
	private static void readFile(File file, IntJANM r, DbPersistence db) throws IOException {
		// Construct BufferedReader from FileReader
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line = null;
		line = br.readLine(); // HEADER
		updateHeader(line, file, r, db);
		
		// RECORD
		br.skip(0);
		db.begin();
		while ((line = br.readLine()) != null) {
			IntJANMRekod rekod = new IntJANMRekod();
			rekod.setJanm(r);
			rekod.setType(line.substring(0, 2).trim());
			rekod.setDepartmentCode(line.substring(2, 10).trim());
			rekod.setDepartment(db.find(JabatanPembayarJANM.class, line.substring(2, 10).trim()));
			rekod.setPayCenter(line.substring(10, 14).trim());
			rekod.setRegion(line.substring(14, 16).trim());
			rekod.setPersonnelNo(line.substring(16, 24).trim().toUpperCase());
			rekod.setIc(line.substring(24, 36).trim().toUpperCase());
			rekod.setAccountNo(line.substring(36, 56).trim().toUpperCase());
			rekod.setName(line.substring(56, 96).trim().toUpperCase());
			Double deductionAmount = Double.valueOf(line.substring(96, 105).trim()) / 100;
			rekod.setDeductionAmount(deductionAmount);
			rekod.setDeductionCode(line.substring(105, 109).trim());
			db.persist(rekod);
		}
		try {
			db.commit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		br.close();
	}

	private static void updateHeader(String line, File file, IntJANM r, DbPersistence db) {
		try {
			InputStream inStream = null;
			OutputStream outStream = null;
			
			String uploadDir = "integrasi/janm/";
			File oldFile = new File(ResourceBundle.getBundle("dbconnection").getString("folder") + r.getFileDir());
			String imgName = uploadDir + line.substring(0, 28).trim() + file.getName().substring(file.getName().lastIndexOf("."));
			File newFile = new File(ResourceBundle.getBundle("dbconnection").getString("folder") + imgName);
			if (newFile.exists()) {
				imgName = uploadDir + line.substring(0, 28) + "_" + UID.getUID() + file.getName().substring(file.getName().lastIndexOf("."));
				newFile = new File(ResourceBundle.getBundle("dbconnection").getString("folder") + imgName);
			}
			inStream = new FileInputStream(oldFile);
    	    outStream = new FileOutputStream(newFile);
    	    
    	    byte[] buffer = new byte[1024];

    	    int length;
    	    //copy the file content in bytes
    	    while ((length = inStream.read(buffer)) > 0){
    	    	outStream.write(buffer, 0, length);
    	    }    	    
    	    inStream.close();
    	    outStream.close();
    	    
			
			db.begin();
			r.setFileName(line.substring(0, 28).trim());
			r.setFileDir(imgName);
			r.setType(line.substring(0, 2).trim());
			r.setDate(line.substring(2, 8).trim());
			r.setAgBranch(db.find(CawanganJANM.class, line.substring(8, 12).trim()));
			r.setTotalRecord(Integer.parseInt(line.substring(12, 17).trim()));
			Double totalAmount = Double.valueOf(line.substring(17, 28).trim()) / 100;
			r.setTotalAmount(Double.valueOf(totalAmount));
			r.setAgencyName(line.substring(28, 48).trim().toUpperCase());
			db.commit();
						
		} catch (Exception ex) {
			ex.printStackTrace();
		}		
	}

	@Override
	public void beforeSave() {
		// TODO Auto-generated method stub
	}

	@Override
	public void begin() {
		dataUtil = DataUtil.getInstance(db);
		context.put("selectAgBrancCode", dataUtil.getListCawanganJANM());
		
		defaultButtonOption();
		addfilter();
				
		//TODO IMPLEMENT BILA ADA SUBCLASS		
		doOverideFilterRecord();
	}
	
	private void defaultButtonOption() {
		this.setDisableSaveAddNewButton(true);
		this.setDisableKosongkanUpperButton(true);
		if (!"add_new_record".equals(command)){
			this.setDisableBackButton(true);
			this.setDisableDefaultButton(true);
		}		
	}
	
	private void addfilter() {
		
		this.setOrderBy("fileName");
		this.setOrderType("asc");
	
	}
	
	//TODO TO BE OVERIDE BY SUB-CLASSESS
	public void doOverideFilterRecord() {
				
	}

	@Override
	public boolean delete(IntJANM r) throws Exception {
		boolean allowDelete = false;
		try {
			if (r.getFlagMigrate().equals("T")) {
				mp = new MyPersistence();
				List<IntJANMRekod> listRekod = mp.list("select x from IntJANMRekod x where x.janm.id = '" + r.getId() + "'");
				mp.begin();
				for (int i = 0; i < listRekod.size(); i++) {
					mp.remove(listRekod.get(i));
				}
				mp.commit();
				Util.deleteFile(r.getFileDir());
				allowDelete = true;
			}			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		return allowDelete;
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return "bph/modules/integrasi/JANM";
	}

	@Override
	public Class<IntJANM> getPersistenceClass() {
		// TODO Auto-generated method stub
		return IntJANM.class;
	}

	@Override
	public void getRelatedData(IntJANM r) {
		try {
			mp = new MyPersistence();	
			List<IntJANMRekod> listRekod = mp.list("select x from IntJANMRekod x where x.janm.id = '" + r.getId() + "' order by x.id asc");
			context.put("listRekod", listRekod);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
	}

	@Override
	public void save(IntJANM r) throws Exception {
		Users pendaftar = (Users) db.find(Users.class, userId);
		
		//LAMPIRAN JANM
		if (getParam("updateLampiranJANMReload").trim().length() > 0) {
			r.setFileDir(getParam("updateLampiranJANMReload"));
		}
		r.setDaftarOleh(pendaftar);
	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		String findFileName = get("findFileName").trim();
		String findType = get("findType").trim();
		String findDate = get("findDate").trim();
		String findAgBranchCode = get("findAgBranchCode").trim();

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("fileName", findFileName);
		map.put("type", findType);
		map.put("date", findDate);
		map.put("agBranch.id", new OperatorEqualTo(findAgBranchCode));

		return map;
	}
	
	@SuppressWarnings("rawtypes")
	@Command("uploadLampiranJANM")
	public String uploadLampiranJANM() throws Exception {
				
		String uploadDir = "integrasi/janm/";
		File dir = new File(ResourceBundle.getBundle("dbconnection").getString("folder") + uploadDir);
		if (!dir.exists())
			dir.mkdir();
	
		DiskFileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		List items = upload.parseRequest(request);
		Iterator itr = items.iterator();
		List<FileItem> files = new ArrayList<FileItem>();
		while (itr.hasNext()) {
			FileItem item = (FileItem) itr.next();
			if ((!(item.isFormField())) && (item.getName() != null)
					&& (!("".equals(item.getName())))) {
				files.add(item);
			}
		}
		
		for (FileItem item : files) {
			String fileName = item.getName();
			String imgName = uploadDir + UID.getUID() + fileName.substring(fileName.lastIndexOf("."));
			Util.deleteFile(imgName);
			item.write(new File(ResourceBundle.getBundle("dbconnection").getString("folder") + imgName));
			
			context.put("lampiranJANMReload", imgName);
		}
		return getPath() + "/uploadLampiranJANM.vm";
	}

	@Command("refreshLampiranJANM")
	public String refreshLampiranJANM() throws Exception {
		context.put("updateLampiranJANMReload", getParam("updateLampiranJANMReload"));
		return getPath() + "/updateLampiranJANM.vm";
	}
}
