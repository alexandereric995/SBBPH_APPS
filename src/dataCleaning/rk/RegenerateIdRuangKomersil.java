/**
 * 
 */
package dataCleaning.rk;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

import lebah.template.DbPersistence;
import bph.entities.kod.JenisKegunaanRuang;
import bph.entities.kod.Negeri;
import bph.entities.rk.RkRuangKomersil;

/**
 * @author Mohd Faizal
 *
 */
public class RegenerateIdRuangKomersil {

	private static DbPersistence db;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
		
			regenerateIdRuangKomersil();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	

	private static void regenerateIdRuangKomersil() throws IOException {
		System.out.println("START regenerateIdRuangKomersil");
		try {
			db = new DbPersistence();
			db.begin();
						
			List<Negeri> listNegeri = db.list("select x from Negeri x where x.abbrev is not null order by x.id asc");
			for (int i = 0; i < listNegeri.size(); i++) {
				Negeri negeri = listNegeri.get(i);
				List<JenisKegunaanRuang> listPerkhidmatan = db.list("select x from JenisKegunaanRuang x where x.kod is not null order by x.id asc");
				for (int j = 0; j < listPerkhidmatan.size(); j++) {
					JenisKegunaanRuang perkhidmatan = listPerkhidmatan.get(j);					
					if (negeri != null && perkhidmatan != null) {
						//LIST RUANG BK
						List<RkRuangKomersil> listRuangBK = db.list("select x from RkRuangKomersil x where x.seksyen.id = '06'"
								+ " and x.bandar.negeri.id = '" + negeri.getId() + "' and x.jenisKegunaanRuang.id = '" + perkhidmatan.getId() + "' order by x.id asc");
						for (int k = 0; k < listRuangBK.size(); k++) {
							RkRuangKomersil ruangBK = listRuangBK.get(k);
							int counter = k + 1;
							String idRuang = "BGS/" + negeri.getAbbrev() + "/" + perkhidmatan.getKod() + "/" + new DecimalFormat("000").format(counter);
							ruangBK.setIdRuangLama(ruangBK.getIdRuang());
							ruangBK.setIdRuang(idRuang);
							ruangBK.setTurutan(counter);
						}
						
						//LIST RUANG UKK
						List<RkRuangKomersil> listRuangUKK = db.list("select x from RkRuangKomersil x where x.seksyen.id = '07'"
								+ " and x.bandar.negeri.id = '" + negeri.getId() + "' and x.jenisKegunaanRuang.id = '" + perkhidmatan.getId() + "' order by x.id asc");
						for (int k = 0; k < listRuangUKK.size(); k++) {
							RkRuangKomersil ruangUKK = listRuangUKK.get(k);
							int counter = k + 1;
							String idRuang = "UKK/" + negeri.getAbbrev() + "/" + perkhidmatan.getKod() + "/" + new DecimalFormat("000").format(counter);
							ruangUKK.setIdRuangLama(ruangUKK.getIdRuang());
							ruangUKK.setIdRuang(idRuang);
							ruangUKK.setTurutan(counter);
						}
					}
				}
			}			
			db.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		System.out.println("END regenerateIdRuangKomersil");
	}
}
