package bph.laporan.kewangan;

import java.util.ArrayList;
import java.util.List;

import lebah.portal.action.Command;
import lebah.portal.action.LebahModule;
import lebah.template.DbPersistence;
import portal.module.entity.Users;
import bph.utils.DataUtil;

public class SenaraiLaporanLejarRecordModule extends LebahModule {

	private static final long serialVersionUID = 1L;
	private DbPersistence db = new DbPersistence();
	private DataUtil dataUtil = DataUtil.getInstance(db);

	@Override
	public String start() {
		context.put("path", getPath());
		return getPath() + "/start.vm";
	}

	private String getPath() {
		return "bph/laporan/kewangan/lejer";
	}

	@Command("paparLaporan")
	public String paparLaporan() throws Exception {

		String idLaporan = getParam("idLaporan");

		context.put("path", getPath());

		if (idLaporan.equals("3")) {
			return getPath() + "/lejerPenghuni/laporanLejarPenghuni.vm";
		}
		// else if(idLaporan.equals("2")){
		// return getPath() + "/laporanLejarRuangKomersil.vm";
		// }
		// else if(idLaporan.equals("3")){
		// return getPath() + "/laporanLejarRumahPeranginan.vm";
		// }
		else if (idLaporan.equals("1")) {
			return getPath() + "/lejerPenghuni/laporanMasterLejar.vm";
		} else if (idLaporan.equals("4")) {
			return getPath() + "/lejerPenghuni/laporanSenaraiPendeposit.vm";
		}
		/*
		 * else if(idLaporan.equals("6")){ return getPath() +
		 * "/lejerPenghuni/laporanSenaraiDepositTidakDituntut.vm"; }
		 */
		else if (idLaporan.equals("2")) {
			return getPath() + "/lejerPenghuni/laporanPenyataKiraanBayaran.vm";
		} else if (idLaporan.equals("5")) {
			return getPath() + "/lejerPenghuni/laporanSejarahLejarPenghuni.vm";
		} else {
			return "";
		}
	}

	/** START CARIAN PENGHUNI **/

//	@Command("cariPenghuni")
//	public String cariPenghuni() {
//
//		String namaPenghuni = getParam("namaPenghuni");
////		String ic = getParam("namaPenghuni");
//
//		List<KuaPenghuni> penghuniList = searchListPenghuni(namaPenghuni);
//		context.put("penghuniList", penghuniList);
//
//		return getPath() + "/lejerPenghuni/popupCarianPenghuni.vm";
//	}

	@Command("cariPenghuni")
	public String cariPenghuni() {

		String namaPenghuni = getParam("namaPenghuni");
//		String ic = getParam("namaPenghuni");

		List<Users> penghuniList = searchListPenghuni(namaPenghuni);
		context.put("penghuniList", penghuniList);

		return getPath() + "/lejerPenghuni/popupCarianPenghuni.vm";
	}
	private List<Users> searchListPenghuni(String namaPenghuni) {

		List<Users> list = new ArrayList<Users>();

		// String sql = "select x from Users x";
		System.out.println("NAMA ======= " + namaPenghuni);
//		System.out.println("IC ======= " + ic);

		if (!namaPenghuni.equalsIgnoreCase("")) {
			// sql = sql + " where x.userName like '%" + namaPenghuni + "%'";

			String sql = "select x from Users x where x.userName like ('%" + namaPenghuni + "%') OR x.id LIKE ('%"+ namaPenghuni + "%')";

			System.out.println("AYAMMMMMM ======= " + sql);
			list = db.list(sql);
		}

		

		return list;
	}

	@Command("savePilihanPenghuni")
	public String savePilihanPenghuni() throws Exception {

		context.remove("penghuni");
		context.remove("pekerjaan");
		context.remove("kuarters");

		String penghuniId = getParam("radPenghuni");

		Users penghuni = (Users) db.get("Select x from Users x where x.id='"
				+ penghuniId + "'");
		context.put("penghuni", penghuni);

		return getPath() + "/lejerPenghuni/maklumatPenghuni.vm";
	}

	/** END CARIAN PENGHUNI **/

}
