package bph.modules.utiliti;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import lebah.portal.action.Command;
import lebah.template.LebahRecordTemplateModule;
import lebah.template.OperatorDateBetween;
import lebah.template.OperatorEqualTo;
import bph.entities.kod.KodPetugas;
import bph.entities.utiliti.UtilDewan;
import bph.entities.utiliti.UtilGelanggang;
import bph.entities.utiliti.UtilJadualTempahan;
import bph.utils.DataUtil;
import bph.utils.Util;

public class SenaraiTutupGelanggangRecordModule extends
		LebahRecordTemplateModule<UtilJadualTempahan> {

	private static final long serialVersionUID = 1L;
	private DataUtil dataUtil;
//	private Util util = new Util();

	@SuppressWarnings("unchecked")
	@Override
	public Class getIdType() {
		return String.class;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void afterSave(UtilJadualTempahan r) {
		
		String tarikhMula = getParam("tarikhMula");
		String tarikhTamat = getParam("tarikhTamat");
		String idGelanggang = getParam("idGelanggang");
		Calendar dateMula = Calendar.getInstance();

		try {
			dateMula.setTime(new SimpleDateFormat("dd-MM-yyyy").parse(tarikhMula));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//dateMula.add(Calendar.DATE, 1);
		
		Calendar dateTamat = Calendar.getInstance();
		try {
			dateTamat.setTime(new SimpleDateFormat("dd-MM-yyyy")
					.parse(tarikhTamat));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dateTamat.add(Calendar.DATE, 1);
		
		int masaMula = getParamAsInteger("masaMula");
		int masaTamat = getParamAsInteger("masaTamat");

		while (dateMula.before(dateTamat)) {
			String hari=null;
			//SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
			SimpleDateFormat fday = new SimpleDateFormat("EEEE");
			Date date = null;
			//date = formatter.parse(tarikhMula);
			date = dateMula.getTime();		
			hari = fday.format(date.getTime());
			String[] hariPilihan=request.getParameterValues("flagHari");
			int size = hariPilihan.length;
			
			for (int i=0; i<size; i++)
		    {
				if(hariPilihan[i].toString().equalsIgnoreCase(hari)){
					Hashtable h = new Hashtable();
					h.put("idGelanggang", idGelanggang);
					h.put("date", dateMula.getTime());
					h.put("masa", masaMula);

					List<UtilJadualTempahan> list = db
							.list(
									"select t from UtilJadualTempahan t where t.gelanggang.id = :idGelanggang and t.tarikhTempahan = :date and t.masaMula=:masa",
									h);
					UtilJadualTempahan tempJadualTempahan = null;
					for (int x = 0; x < list.size(); x++) {
						tempJadualTempahan = list.get(x);
						// if ("O".equals(idStatus)){
						if ("C".equals(tempJadualTempahan.getStatus())) {
							try {
								removeJadualTempahan(tempJadualTempahan);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						// }
					}
					db.begin();
					UtilJadualTempahan jadual = new UtilJadualTempahan();
					jadual.setDewan(db.find(UtilDewan.class, get("idDewan")));
					jadual.setGelanggang(db.find(UtilGelanggang.class,
							get("idGelanggang")));
					jadual.setTarikhTempahan(dateMula.getTime());
					jadual.setMasaMula(masaMula);
					jadual.setMasaTamat(masaTamat);
					jadual.setStatus("C");
					db.persist(jadual);
					
					try {
						insertTempahanRelated(r.getDewan().getId(), dateMula.getTime() , r
								.getMasaMula(), r.getMasaTamat(), "C", r.getId());
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					try {
						db.commit();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					//dateMula.add(Calendar.DATE, 1);
					
				}
				  
		    }
			dateMula.add(Calendar.DATE, 1);	
		}
		
		//delete tarikh/masa semasa-rozai tambah 14/9/2016
				Calendar tarikhHari = Calendar.getInstance();
				try {
					tarikhHari.setTime(new SimpleDateFormat("dd-MM-yyyy").parse(tarikhMula));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Date tempDate = tarikhHari.getTime();
				SimpleDateFormat fday = new SimpleDateFormat("EEEE");
				String hariBetul = null;
				hariBetul = fday.format(tempDate.getTime());
				String[] hariPilihan = request.getParameterValues("flagHari");
				int sizePilihan = hariPilihan.length;
				for (int i = 0; i < sizePilihan; i++) {
					if (hariPilihan[i].toString().equalsIgnoreCase(hariBetul)) {
						
					}else
					{
						db.begin();
						db.remove(r);
						try {
							db.commit();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				paintJadualRelated(r);
	}

	private void removeJadualTempahan(UtilJadualTempahan tempJadualTempahan)
			throws Exception {
		db.begin();
		db.remove(tempJadualTempahan);
		db.commit();
	}

	@Override
	public void beforeSave() {
		// TODO Auto-generated method stub
	}

	@Override
	public void begin() {
		String idCawangan="";
		userId = (String) request.getSession().getAttribute("_portal_login");
		userRole = (String) request.getSession().getAttribute("_portal_role");
		if ("(UTILITI) Penyedia Cawangan".equalsIgnoreCase(userRole)|| "(UTILITI) Pelulus Cawangan".equalsIgnoreCase(userRole)) {
			KodPetugas petugas = (KodPetugas) db.get("select x from KodPetugas x where x.petugas.id = '" + userId + "'");
			if (petugas != null) {
				idCawangan=petugas.getCawangan().getId();
				this.addFilter("dewan.kodCawangan.id= '" + idCawangan + "'");
			} else {
				this.addFilter("dewan is null");
			}
		}
		defaultButtonOption();
		addfilter();
		dataUtil = DataUtil.getInstance(db);
		if ("(UTILITI) Penyedia Cawangan".equalsIgnoreCase(userRole)|| "(UTILITI) Pelulus Cawangan".equalsIgnoreCase(userRole)) {
			context.put("selectDewan", dataUtil.getListDewanSahaja(idCawangan));
		}else{
			context.put("selectDewan", dataUtil.getListDewanSahaja());	
		}
		context.put("path", getPath());
		context.put("util", new Util());
		context.put("command", command);
		context.put("uploadDir",
		ResourceBundle.getBundle("dbconnection").getString("folder"));
		paintJadualMula();
	}

	private void addfilter() {
		this.addFilter("gelanggang is not null");
		this.addFilter("permohonan is null");
		this.addFilter("tarikhTempahan >= '"+ Util.getDateTime(new Date(), "yyyy-MM-dd") + "'");
		setOrderBy("tarikhTempahan ASC");
	}

	private void defaultButtonOption() {
		this.setDisableSaveAddNewButton(true);
		if (!"add_new_record".equals(command)) {
			this.setDisableBackButton(true);
			this.setDisableDefaultButton(true);
		} else {
			this.setDisableBackButton(false);
			this.setDisableDefaultButton(false);
		}
	}

	@Override
	public boolean delete(UtilJadualTempahan r) throws Exception {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return "bph/modules/utiliti/senaraiBlockingGelanggang";
	}

	@Override
	public Class<UtilJadualTempahan> getPersistenceClass() {
		// TODO Auto-generated method stub
		return UtilJadualTempahan.class;
	}

	@Override
	public void getRelatedData(UtilJadualTempahan r) {
		String idDewan = "";
		if (r.getDewan().getId() != null
				&& r.getDewan().getId().trim().length() > 0)
			idDewan = r.getDewan().getId();
		List<UtilGelanggang> list = dataUtil.getListGelanggang(idDewan);
		context.put("selectGelanggang", list);
		paintJadualRelated(r);
	}

	@Override
	public void save(UtilJadualTempahan simpan) throws Exception {
		simpan.setDewan(db.find(UtilDewan.class, get("idDewan")));
		simpan
				.setGelanggang(db.find(UtilGelanggang.class,
						get("idGelanggang")));
		simpan.setTarikhTempahan(getDate("tarikhMula"));
		simpan.setMasaMula(getParamAsInteger("masaMula"));
		simpan.setMasaTamat(getParamAsInteger("masaTamat"));
		simpan.setStatus("C");
		simpan.setCatatan(getParam("catatan"));
	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String findGelanggang = get("find_gelanggang");
		String findDewan = get("find_dewan");
		String findLokasi = get("find_lokasi");
		map.put("gelanggang.dewan.nama", findGelanggang);
		map.put("dewan.nama", findDewan);
		map.put("dewan.lokasi", findLokasi);
		map.put("tarikhTempahan", new OperatorDateBetween(getDate("findTarikhMasuk"), getDate("findTarikhMasuk")));
		map.put("masaMula", new OperatorEqualTo(get("findMasaMula")));
		map.put("masaTamat", new OperatorEqualTo(get("findMasaTamat")));
		return map;
	}

	@Command("savePermohonan")
	public String savePermohonan() throws Exception {

		UtilJadualTempahan simpan = null;
		simpan = db.find(UtilJadualTempahan.class, get("idBlocking"));
		simpan.setDewan(db.find(UtilDewan.class, get("idDewan")));
		simpan
				.setGelanggang(db.find(UtilGelanggang.class,
						get("idGelanggang")));
		simpan.setTarikhTempahan(getDate("tarikhMula"));
		simpan.setMasaMula(getParamAsInteger("masaMula"));
		simpan.setMasaTamat(getParamAsInteger("masaTamat"));
		simpan.setStatus("C");
		simpan.setCatatan(getParam("catatan"));

		db.begin();

		try {
			db.commit();
			updateJadualTempahan(simpan.getDewan().getId(), simpan
					.getGelanggang().getId(), simpan.getTarikhTempahan(),
					simpan.getMasaMula(), simpan.getMasaTamat(), "C", simpan
							.getId());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return getPath() + "/entry_page.vm";
	}

	/** START JADUAL TEMPAHAN **/

	private void updateJadualTempahan(String idDewan, String idGelanggang,
			Date tarikhTempahan, int masaMula, int masaTamat, String idStatus,
			String idTempahan) throws Exception {
		db.begin();
		UtilJadualTempahan jadualTempahan = (UtilJadualTempahan) db
				.get("select x from UtilJadualTempahan x where x.id = '"
						+ idTempahan + "'");
		UtilDewan dewan = db.find(UtilDewan.class, idDewan);
		UtilGelanggang gelanggang = db.find(UtilGelanggang.class, idGelanggang);
		jadualTempahan.setDewan(dewan);
		jadualTempahan.setGelanggang(gelanggang);
		jadualTempahan.setTarikhTempahan(tarikhTempahan);
		jadualTempahan.setMasaMula(masaMula);
		jadualTempahan.setMasaTamat(masaTamat);
		db.commit();
		paintJadual();
	}

	@SuppressWarnings("unchecked")
	public String paintJadualRelated(UtilJadualTempahan tempahan) {
		String bgcolour = "#008800";
		Date tarikhTempahan = tempahan.getTarikhTempahan();
		String idGelanggang = tempahan.getGelanggang().getId();
		Hashtable h = new Hashtable();
		h.put("idAset", idGelanggang);
		h.put("date", tarikhTempahan);

		// List<TempJadualTempahan> list =
		// db.list("select t from TempJadualTempahan t where t.tempAsetPBT.id = :idAset and t.tarikhTempahan = :date",
		// h);
		List<UtilJadualTempahan> list = db
				.list(
						"select t from UtilJadualTempahan t where t.gelanggang.id = :idAset and t.tarikhTempahan = :date",
						h);
		UtilJadualTempahan tempJadualTempahan = null;

		// PAINT JADUAL TO GREEN
		for (int x = 7; x < 23; x++) {
			bgcolour = "#008800";
			context.put("hour" + (x + 1), bgcolour);
		}

		// PAINT JADUAL BASED ON STATUS
		for (int y = 0; y < list.size(); y++) {
			tempJadualTempahan = list.get(y);
			if ("C".equals(tempJadualTempahan.getStatus())) {
				bgcolour = "#999999";
			} else if ("B".equals(tempJadualTempahan.getStatus())) {
				bgcolour = "#bb0000";
			} else {
				bgcolour = "#008800";
			}

			// highlight masa tempahan yang sedang aktif
			if (tempJadualTempahan.getMasaMula() == tempahan.getMasaMula()) {
				bgcolour = "#e67400";
			} else if (tempJadualTempahan.getMasaTamat() == tempahan
					.getMasaTamat()) {
				bgcolour = "#e67400";
			}

			int start = tempJadualTempahan.getMasaMula();
			int stop = tempJadualTempahan.getMasaTamat();
			int range = stop - start;
			for (int k = 1; k <= range; k++) {
				context.put("hour" + start, bgcolour);
				start = start + 1;
			}
		}

		return getPath() + "/jadualTempahan.vm";
	}

	@SuppressWarnings("unchecked")
	@Command("paintJadual")
	public String paintJadual() {
		String bgcolour = "#008800";
		Date tarikhTempahan = getDate("tarikhMula");
		context.put("$dateMula", tarikhTempahan);
		String idGelanggang = get("idGelanggang");
		Hashtable h = new Hashtable();
		h.put("idAset", idGelanggang);
		h.put("date", tarikhTempahan);

		// List<TempJadualTempahan> list =
		// db.list("select t from TempJadualTempahan t where t.tempAsetPBT.id = :idAset and t.tarikhTempahan = :date",
		// h);
		List<UtilJadualTempahan> list = db
				.list(
						"select t from UtilJadualTempahan t where t.gelanggang.id = :idAset and t.tarikhTempahan = :date and t.gelanggang is not null",
						h);
		UtilJadualTempahan tempJadualTempahan = null;

		// PAINT JADUAL TO GREEN
		for (int x = 7; x < 23; x++) {
			bgcolour = "#008800";
			context.put("hour" + (x + 1), bgcolour);
		}

		// PAINT JADUAL BASED ON STATUS
		for (int y = 0; y < list.size(); y++) {
			tempJadualTempahan = list.get(y);
			if ("C".equals(tempJadualTempahan.getStatus())) {
				bgcolour = "#999999";
			} else if ("B".equals(tempJadualTempahan.getStatus())) {
				bgcolour = "#bb0000";
			} else {
				bgcolour = "#008800";
			}

			int start = tempJadualTempahan.getMasaMula();
			int stop = tempJadualTempahan.getMasaTamat();
			int range = stop - start;
			for (int k = 1; k <= range; k++) {
				context.put("hour" + start, bgcolour);
				start = start + 1;
			}
		}
		return getPath() + "/jadualTempahan.vm";
	}

	// papar default jadual
	private void paintJadualMula() {
		String bgcolour = "";
		for (int x = 7; x < 23; x++) {
			bgcolour = "#999999";
			context.put("hour" + (x + 1), bgcolour);
		}
	}

	/** END JADUAL TEMPAHAN **/

	@Command("selectGelanggang")
	public String selectGelanggang() throws Exception {

		String idDewan = "";
		if (get("idDewan").trim().length() > 0)
			idDewan = get("idDewan");
		List<UtilGelanggang> list = dataUtil.getListGelanggang(idDewan);
		context.put("selectGelanggang", list);
		return getPath() + "/selectGelanggang.vm";
	}
	
	public static String checkHari(String tarikhMasuk)
	{	
		String hari=null;
		try
		{	
//			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
			SimpleDateFormat fday = new SimpleDateFormat("EEEE");
			Date date = formatter.parse(tarikhMasuk);
			hari = fday.format(date.getTime());
			
			//Calendar cal = Calendar.getInstance();
			//SimpleDateFormat formatter = new SimpleDateFormat("EEE");
			//hari = formatter.format(cal.getTime());//monday;tuesday;wednesday;thursday;friday;saturday;sunday///
		}catch(Exception e){ 
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		return hari;
	}
	
	private void insertTempahanRelated(String idDewan,
			Date tarikhTempahan, int masaMula, int masaTamat, String idStatus,
			String idTempahan) throws Exception {
//		db.begin();
		UtilDewan dewan = db.find(UtilDewan.class, idDewan);
		UtilJadualTempahan tempJadualTempahan = new UtilJadualTempahan();
		tempJadualTempahan.setDewan(dewan);
		tempJadualTempahan.setTarikhTempahan(tarikhTempahan);
		tempJadualTempahan.setMasaMula(masaMula);
		tempJadualTempahan.setMasaTamat(masaTamat);
		tempJadualTempahan.setStatus("C");
		db.persist(tempJadualTempahan);
		db.commit();
	}
}
