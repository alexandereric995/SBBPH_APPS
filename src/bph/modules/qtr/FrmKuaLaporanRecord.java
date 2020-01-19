package bph.modules.qtr;

import lebah.portal.action.Command;
import lebah.portal.action.LebahModule;
import lebah.template.DbPersistence;
import bph.utils.DataUtil;

public class FrmKuaLaporanRecord extends LebahModule {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2955583795630057484L;
	private DataUtil dataUtil;
	DbPersistence db = new DbPersistence();

	@Override
	public String start() {
		// TODO Auto-generated method stub
		context.put("path", getPath());

		return getPath() + "/start.vm";
	}

	public String getPath() {
		return "bph/modules/qtr/laporan";
	}

	@Command("laporanSenaraiKuarters")
	public String laporanSenaraiKuarters() {
		dataUtil = DataUtil.getInstance(db);

		context.put("path", getPath());

		context.put("selectNegeri", dataUtil.getListNegeri());
		context.put("selectKelasKuarters", dataUtil.getListKelasPeranginan());
		context.put("selectJenisKediaman", dataUtil.getListJenisKediaman());
		context.put("selectStatusKuarters", dataUtil
				.getListStatusKuartersBaru());

		return getPath() + "/paramLaporan/laporanSenaraiKuarters.vm";
	}

	@Command("laporanSenaraiPenghuniKuarters")
	public String laporanSenaraiPenghuniKuarters() {
		dataUtil = DataUtil.getInstance(db);

		context.put("selectNegeri", dataUtil.getListNegeri());

		context.put("path", getPath());
		return getPath() + "/paramLaporan/laporanSenaraiPenghuniKuarters.vm";
	}

	@Command("laporanSenaraieTemujanji")
	public String laporanSenaraieTemujanji() {
		dataUtil = DataUtil.getInstance(db);

		context.put("path", getPath());

		return getPath() + "/paramLaporan/laporanSenaraieTemujanji.vm";
	}

	@Command("laporanSenaraiPenghuniKeluarKuarters")
	public String laporanSenaraiPenghuniKeluarKuarters() {
		dataUtil = DataUtil.getInstance(db);

		context.put("path", getPath());

		context.put("selectNegeri", dataUtil.getListNegeri());
		context.put("selectGredJawatan", dataUtil.getListGredPerkhidmatan());
		context.put("selectJawatan", dataUtil.getListJawatan());

		return getPath()
				+ "/paramLaporan/laporanSenaraiPenghuniKeluarKuarters.vm";
	}

	@Command("laporanSenaraiPenghuniMenunggu")
	public String laporanSenaraiPenghuniMenunggu() {
		dataUtil = DataUtil.getInstance(db);

		context.put("path", getPath());

		context.put("selectGredJawatan", dataUtil.getListGredPerkhidmatan());
		context.put("selectJawatan", dataUtil.getListJawatan());
		context.put("selectAgama", dataUtil.getListAgama());
		context.put("selectBangsa", dataUtil.getListBangsa());
		context.put("selectJantina", dataUtil.getListJantina());

		return getPath() + "/paramLaporan/laporanSenaraiPenghuniMenunggu.vm";
	}

	@Command("laporanStatistikStatusPermohonanMengikutTahun")
	public String laporanStatistikStatusPermohonanMengikutTahun() {
		dataUtil = DataUtil.getInstance(db);

		context.put("path", getPath());

		return getPath()
				+ "/paramLaporan/laporanStatistikStatusPermohonanMengikutTahun.vm";
	}

	@Command("laporanStatistikStatusKuartersMengikutTahun")
	public String laporanStatistikStatusKuartersMengikutTahun() {
		dataUtil = DataUtil.getInstance(db);

		context.put("path", getPath());

		return getPath()
				+ "/paramLaporan/laporanStatistikStatusKuartersMengikutTahun.vm";
	}

	@Command("laporanSenaraiPermohonanKuarters")
	public String laporanSenaraiPermohonanKuarters() {
		dataUtil = DataUtil.getInstance(db);

		context.put("path", getPath());

		return getPath() + "/paramLaporan/laporanSenaraiPermohonanKuarters.vm";
	}

	@Command("selectBandar")
	public String selectBandar() throws Exception {
		dataUtil = DataUtil.getInstance(db);

		String idNegeri = "0";

		if (getParam("idNegeri").trim().length() > 0)
			idNegeri = getParam("idNegeri");

		context.put("selectBandar", dataUtil.getListBandar(idNegeri));

		return getPath() + "/paramLaporan/select/selectBandar.vm";
	}
}