package bph.modules.senggara;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lebah.portal.action.Command;
import lebah.template.LebahRecordTemplateModule;
import lebah.template.OperatorEqualTo;
import bph.entities.kod.Fasa;
import bph.entities.kod.LokasiKuarters;
import bph.entities.senggara.MtnIndenKerja;
import bph.entities.senggara.MtnJKH;
import bph.utils.DataUtil;
import bph.utils.Util;
import db.persistence.MyPersistence;

public class SenaraiJKHLulusRecordModule extends LebahRecordTemplateModule<MtnJKH>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DataUtil dataUtil;
	private Util util = new Util();
	private MyPersistence mp;

	@Override
	public Class getIdType() {
		// TODO Auto-generated method stub
		return String.class;
	}

	@Override
	public Class<MtnJKH> getPersistenceClass() {
		// TODO Auto-generated method stub
		return MtnJKH.class;
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return "bph/modules/senggara/jkhLulus";
	}

	@Override
	public void begin() {
		dataUtil = DataUtil.getInstance(db);
		
		userId = (String) request.getSession().getAttribute("_portal_login");
		userName = (String) request.getSession().getAttribute("_portal_username");
		userRole = (String) request.getSession().getAttribute("_portal_role");
		context.put("userRole", userRole);
		
		context.put("selectKawasan", dataUtil.getListLokasiPermohonan());
		context.put("selectKelasKuarters", dataUtil.getListKelasKuarters());
		context.put("selectJenisKuarters", dataUtil.getListJenisKediaman());
		context.put("selectStatus", dataUtil.getListStatusModulSenggara());
		
		defaultButtonOption();
		addfilter();
		//TODO IMPLEMENT BILA ADA SUBCLASS		
		doOverideFilterRecord();
	}
	
	//TODO TO BE OVERIDE BY SUB-CLASSESS
	public void doOverideFilterRecord() {
		
	}

	private void defaultButtonOption() {
		
		this.setReadonly(true);
		this.setDisableDefaultButton(true);
		this.setDisableBackButton(true);
	}
	
	private void addfilter() {
		this.addFilter("flagKeputusanPelulus = 'Y'");
		this.addFilter("indenKerja is null");
		this.setOrderBy("tarikhJKH");
		this.setOrderType("asc");
	}

	@Override
	public void save(MtnJKH jkh) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean delete(MtnJKH jkh) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("kuartersSenggara.kuarters.noRujukan", getParam("findNoRujukan").trim());
		map.put("kuartersSenggara.kuarters.noUnit", getParam("findNoUnit").trim());
		map.put("kuartersSenggara.kuarters.alamat1", getParam("findAlamat").trim());	
		map.put("kuartersSenggara.kuarters.alamat2", getParam("findAlamat").trim());	
		map.put("kuartersSenggara.kuarters.alamat3", getParam("findAlamat").trim());	
		
		map.put("kuartersSenggara.kuarters.lokasi.lokasi.id", new OperatorEqualTo(get("findKawasan")));
		map.put("kuartersSenggara.kuarters.lokasi.id", new OperatorEqualTo(get("findLokasi")));
		map.put("kuartersSenggara.kuarters.fasa.id", new OperatorEqualTo(get("findFasa")));
		map.put("kuartersSenggara.kuarters.kelas.id", new OperatorEqualTo(get("findKelasKuarters")));
		map.put("kuartersSenggara.kuarters.jenisKediaman.id", new OperatorEqualTo(get("findJenisKuarters")));
		
		return map;
	}

	@Override
	public void beforeSave() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterSave(MtnJKH jkh) {
		// TODO Auto-generated method stub
	}

	@Override
	public void getRelatedData(MtnJKH jkh) {		
		try {
			mp = new MyPersistence();
			MtnJKH jadualKadarHarga = (MtnJKH) mp.find(MtnJKH.class, jkh.getId());
			context.put("r", jadualKadarHarga);
			
			List<MtnIndenKerja> list = mp.list("select x from MtnIndenKerja x where x.lokasi.id = '" 
					+ jkh.getKuartersSenggara().getKuarters().getLokasi().getLokasi().getId() + "' and x.status.id = '1426130691711'" ); //PENYEDIAAN INDEN KERJA
			context.put("selectIndenKerja", list);
			context.remove("indenKerja");
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
	}
	
	/** START PILIH INDEN KERJA **/	
	@Command("doChangeIndenKerja")
	public String doChangeIndenKerja() throws Exception {

		try {
			mp = new MyPersistence();
			MtnJKH jadualKadarHarga = (MtnJKH) mp.find(MtnJKH.class, getParam("idJKH"));
			context.put("r", jadualKadarHarga);
			
			MtnIndenKerja indenKerja = (MtnIndenKerja) mp.find(MtnIndenKerja.class, getParam("idIndenKerja"));			
			context.put("indenKerja", indenKerja);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		return getPath() + "/entry_page.vm";
	}
	
	@Command("simpanIndenKerja")
	public String simpanIndenKerja() throws Exception {

		try {
			mp = new MyPersistence();
			
			MtnIndenKerja indenKerja = (MtnIndenKerja) mp.find(MtnIndenKerja.class, getParam("idIndenKerja"));
			MtnJKH jkh = (MtnJKH) mp.find(MtnJKH.class, getParam("idJKH"));
			
			mp.begin();
			jkh.setIndenKerja(indenKerja);
			
			Double total = 0D;
			total = indenKerja.getJumlah() + jkh.getJumlahKeseluruhan();
			indenKerja.setJumlah(total);
			mp.commit();
			
			context.put("r", jkh);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) { mp.close(); }
		}
		
		return getPath() + "/entry_page.vm";
	}
	/** END PILIH INDEN KERJA **/
	
	/** START DROP DOWN  */
	@Command("findLokasi")
	public String findLokasi() throws Exception {
		String idKawasan = "0";
		if (getParam("findKawasan").trim().length() > 0)
			idKawasan = getParam("findKawasan");
		
		List<LokasiKuarters> list = dataUtil.getListLokasiKuartersByLokasiPermohonan(idKawasan);
		context.put("selectLokasi", list);

		return getPath() + "/findLokasi.vm";
	}
	
	@Command("findFasa")
	public String findFasa() throws Exception {
		String idKawasan = "0";
		if (getParam("findKawasan").trim().length() > 0)
			idKawasan = getParam("findKawasan");
		
		List<Fasa> list = null;
		if (idKawasan.equals("01")) {
			list = dataUtil.getListFasa();
		}
		context.put("selectFasa", list);

		return getPath() + "/findFasa.vm";
	}
	/** END DROP DOWN  */
}