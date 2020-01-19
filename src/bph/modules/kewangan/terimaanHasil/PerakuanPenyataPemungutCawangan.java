package bph.modules.kewangan.terimaanHasil;

import java.util.List;

import bph.entities.kod.KodJuruwang;
import bph.utils.DataUtil;

public class PerakuanPenyataPemungutCawangan extends PenyataPemungutRecordModule{

	private static final long serialVersionUID = 1L;
	private DataUtil dataUtil;
	
	public void doOverideFilterRecord() {
		String listKodPusatTerima = "''";
		clearFilter();
		this.setReadonly(true);
		this.setDisableDefaultButton(true);
		this.setDisableBackButton(true);
		this.setDisableKosongkanUpperButton(true);
		this.setDisableSaveAddNewButton(true);
		this.setHideDeleteButton(true);
		
		userId = (String) request.getSession().getAttribute("_portal_login");
		List<KodJuruwang> listJuruwang = db.list("select x from KodJuruwang x where x.juruwang.id = '" + userId + "' and x.flagAktif = 'Y'");
		for (int i = 0; i < listJuruwang.size(); i++) {
			KodJuruwang juruwang = listJuruwang.get(i);
			if (i == 0) {
				listKodPusatTerima = "'" + juruwang.getKodPusatTerima() + "'";
			} else {
				listKodPusatTerima = listKodPusatTerima + ", '" + juruwang.getKodPusatTerima() + "'";
			}
		}
		this.addFilter("kodPusat in (" + listKodPusatTerima + ")");
		this.setOrderBy("tarikhPenyataPemungut");
		this.setOrderType("desc");
	}

}
