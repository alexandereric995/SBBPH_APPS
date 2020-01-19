package bph.modules.bgs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lebah.portal.action.Command;
import lebah.template.LebahRecordTemplateModule;
import lebah.template.OperatorEqualTo;
import bph.entities.kod.Agensi;
import bph.entities.kod.Bandar;
import bph.entities.kod.Daerah;
import bph.entities.kod.Mukim;
import bph.entities.kod.Zon;
import bph.entities.pembangunan.DevAras;
import bph.entities.pembangunan.DevBangunan;
import bph.entities.pembangunan.DevPremis;
import bph.entities.pembangunan.DevRuang;
import bph.utils.DataUtil;

public class DaftarBangunanRecordModule extends LebahRecordTemplateModule<DevRuang> {

	private static final long serialVersionUID = 1L;
	private DataUtil dataUtil;

	@Override
	public Class getIdType() {
		// TODO Auto-generated method stub
		return String.class;
	}

	@Override
	public void afterSave(DevRuang ruang) {
		// TODO Auto-generated method stub
		DevAras aras=new DevAras();
		aras.setKodDAK(get("kodDAK"));
		aras.setNamaAras(get("namaAras"));
		aras.setJenisAras(get("jenisAras"));
		aras.setLuasAras(getParamAsDouble("luasAras"));
		aras.setFlagAktif("Y");
		db.persist(aras);
		
		DevBangunan bangunan= new DevBangunan();
		bangunan.setKodDPA(get("kodDPA"));
		bangunan.setKodDAK(get("kodDAK"));
		bangunan.setNamaBangunan(get("namaBangunan"));
		bangunan.setLuas(getParamAsDouble("luas"));
		bangunan.setKegunaanBangunan(get("kegunaanBangunan"));
		bangunan.setNoPendaftaran(get("noPendaftaran"));
		bangunan.setNamaKontraktor(get("namaKontraktor"));
		bangunan.setBidangKerjaKontraktor(get("bidangKerjaKontraktor"));
		bangunan.setNamaPerunding(get("namaPerunding"));
		bangunan.setBidangKerjaPerunding(get("bidangKerjaPerunding"));
		bangunan.setTahunSiapBina(getParamAsInteger("tahunSiapBina"));
		bangunan.setKosBinaan(getParamAsDouble("kosBinaan"));
		bangunan.setFlagAktif("Y");
		db.persist(bangunan);
		
		// TODO Auto-generated method stub
		DevPremis premis=new DevPremis();
		premis.setKodDPA(get("kodDPA"));
		premis.setNamaPremis(get("namaPremis"));
		premis.setZon(db.find(Zon.class, get("zon")));
		premis.setMukim(db.find(Mukim.class, get("idMukim")));
		premis.setAlamat1(get("alamat1"));
		premis.setAlamat2(get("alamat2"));
		premis.setAlamat3(get("alamat3"));
		premis.setBandar(db.find(Bandar.class, get("idBandar")));
		premis.setCatatan(get("catatan"));
		premis.setPemilikPremis(get("pemilikPremis"));
		premis.setAgensi(db.find(Agensi.class, get("idAgensi")));
		premis.setKategoriPremis(get("kategoriPremis"));
		premis.setSubkategoriPremis(get("subKategoriPremis"));
		premis.setTahunSiapBina(getParamAsInteger("tahunSiapBina"));
		premis.setLuasPpremis(getParamAsDouble("luasPremis"));
		premis.setLuasBangunan(getParamAsDouble("luasBangunan"));
		premis.setLuasBinaanLuar(getParamAsDouble("luasBinaanLuar"));
		premis.setKosAsal(getParamAsDouble("kosAsal"));
		premis.setKosTambahan(getParamAsDouble("kosTambahan"));
		premis.setJumlahKos(getParamAsDouble("jumlahKos"));
		db.persist(premis);
		
		aras.setBangunan(db.find(DevBangunan.class, bangunan.getId()));
		ruang.setAras(db.find(DevAras.class, aras.getId()));
		bangunan.setPremis(db.find(DevPremis.class, premis.getId()));
		db.begin();
		try {
			db.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void beforeSave() {
		// TODO Auto-generated method stub
	}

	@Override
	public void begin() {
		dataUtil = DataUtil.getInstance(db);	
		context.put("selectNegeri", dataUtil.getListNegeri());
		context.put("selectNegeriBandar", dataUtil.getListNegeri());
		//this.setReadonly(true);
		//this.setDisableBackButton(true);
		//this.setDisableDefaultButton(true);
		this.setDisableSaveAddNewButton(true);
		
		context.remove("selectZon");
		context.remove("selectNegeri");
		context.remove("selectNegeriBandar");
		context.remove("selectKementerian");
		context.remove("selectJenisHakmilik");
		context.remove("selectJenisLot");
		context.remove("info");
		context.remove("statusInfo");
		context.put("selectJenisHakmilik", dataUtil.getListJenisHakMilik());
		context.put("selectJenisLot", dataUtil.getListLot());
		context.put("selectNegeri", dataUtil.getListNegeri());
		context.put("selectNegeriBandar", dataUtil.getListNegeri());
		context.put("selectZon", dataUtil.getListZon());
		context.put("selectKementerian", dataUtil.getListKementerian()); //LIST DROPDOWN KEMENTERIAN
		context.put("selectJenisLuas", dataUtil.getListLuas());
		context.put("selectKategoriTanah", dataUtil.getListKategoriTanah());
		context.put("selectSubKategoriTanah", dataUtil.getListSubkategori());
		context.put("selectJenisHakmilik", dataUtil.getListJenisHakMilik());
		context.put("selectLot", dataUtil.getListLot());

	}

	@Override
	public boolean delete(DevRuang ruang) throws Exception {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return "bph/modules/bgs/daftarBangunan";
	}

	@Override
	public Class<DevRuang> getPersistenceClass() {
		// TODO Auto-generated method stub
		return DevRuang.class;
	}

	@Override
	public void getRelatedData(DevRuang ruang) {
		context.put("selectNegeri", dataUtil.getListNegeri());
		context.put("selectNegeriBandar", dataUtil.getListBandar());
		context.put("selectBandar",dataUtil.getListNegeri());		
		context.put("selectJenisHakmilik", dataUtil.getListJenisHakMilik());
		context.put("selectJenisLot", dataUtil.getListLot());		
		context.put("selectZon", dataUtil.getListZon());
		context.put("selectKementerian", dataUtil.getListKementerian()); //LIST DROPDOWN KEMENTERIAN
		context.put("selectJenisLuas", dataUtil.getListLuas());
		context.put("selectKategoriTanah", dataUtil.getListKategoriTanah());
		context.put("selectSubKategoriTanah", dataUtil.getListSubkategori());
		context.put("selectJenisHakmilik", dataUtil.getListJenisHakMilik());
		context.put("selectLot", dataUtil.getListLot());
	}

	@Override
	public void save(DevRuang ruang) throws Exception {
		// TODO Auto-generated method stub
		ruang.setKodDAK(get("kodDAK"));
		ruang.setNamaRuang(get("namaRuang"));
		ruang.setFungsiRuang(get("fungsiRuang"));
		ruang.setLuasRuang(getParamAsDouble("luasRuang"));
		ruang.setTinggiSiling(getParamAsDouble("tinggiSiling"));
		ruang.setFlagAktif("Y");
	}

	@Override
	public Map<String, Object> searchCriteria() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("aras.bangunan.premis.namaPremis", getParam("findPremis"));
		map.put("aras.bangunan.namaBangunan", getParam("findBangunan"));
		map.put("aras.namaAras", getParam("findAras"));		
		map.put("aras.bangunan.premis.mukim.daerah.negeri.id", new OperatorEqualTo(getParam("findNegeri")));
		map.put("aras.bangunan.premis.mukim.daerah.id",  new OperatorEqualTo(getParam("findDaerah")));
		map.put("aras.bangunan.premis.mukim.id",  new OperatorEqualTo(getParam("findMukim")));
		return map;
	}
	
	@Command("selectAgensi")
	public String selectAgensi() throws Exception {
		String idKementerian = "0";	
		if (get("idKementerian").trim().length() > 0){
			idKementerian = get("idKementerian");
		}
		List<Agensi> list = dataUtil.getListAgensi(idKementerian);
		context.put("selectAgensi", list);
		return getPath() + "/selectAgensi.vm";
	}
	
	@Command("selectDaerah")
	public String selectDaerah() throws Exception {
		
		String idNegeri = "0";
		if (get("idNegeri").trim().length() > 0){
			idNegeri = get("idNegeri");
		}
		List<Daerah> list = dataUtil.getListDaerah(idNegeri);
		context.put("selectDaerah", list);

		return getPath() + "/selectDaerah.vm";
	}
	
	@Command("selectMukim")
	public String selectMukim() throws Exception {

		String idDaerah = "0";
		if (get("idDaerah").trim().length() > 0){
			idDaerah = get("idDaerah");
		}
		List<Mukim> list = dataUtil.getListMukim(idDaerah);
		context.put("selectMukim", list);

		return getPath() + "/selectMukim.vm";
	}
	
	@Command("selectBandar")
	public String selectBandar() throws Exception {
		String idNegeri = "0";
		if (get("idNegeriBandar").trim().length() > 0)
			idNegeri = get("idNegeriBandar");
		List<Bandar> list = dataUtil.getListBandar(idNegeri);
		context.put("selectBandar", list);
		return getPath() + "/selectBandar.vm";
	}
}
