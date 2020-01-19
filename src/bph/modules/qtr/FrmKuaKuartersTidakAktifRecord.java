package bph.modules.qtr;

import bph.entities.qtr.KuaKuarters;
import bph.utils.DataUtil;
import db.persistence.MyPersistence;

public class FrmKuaKuartersTidakAktifRecord extends FrmKuaKuartersRecord {

	private static final long serialVersionUID = 6987901684221218467L;
	private DataUtil dataUtil;
	private MyPersistence mp;

	@Override
	public void begin() {
		dataUtil = DataUtil.getInstance(db);
		addFilter("flagAktif = '1'");
		this.setOrderBy("kelas.id ASC");
		context.put("selectNegeri", dataUtil.getListNegeri());
		context.put("selectKelasKuarters", dataUtil.getListKelasPeranginan());
		context.put("selectJenisKediaman", dataUtil.getListJenisKediaman());
		context.put("selectJenisKegunaanKuarters", dataUtil
				.getListJenisKegunaanKuarters());
		context.put("selectStatusKuarters", dataUtil
				.getListStatusKuartersBaru());
		context.put("selectLokasiKuarters", dataUtil.getListLokasiKuarters());
		context.put("selectLokasiDibenar", dataUtil.getListLokasiDibenar());
		context.put("selectFasa", dataUtil.getListFasa());
		context.put("findLokasiKuarters", dataUtil.getListLokasiKuarters());
		context.put("findKelasKuarters", dataUtil.getListKelasPeranginan());
		context.put("findJenisKediaman", dataUtil.getListJenisKediaman());
		context.put("findStatusKuarters", dataUtil.getListStatusKuartersBaru());
		context.put("findNegeri", dataUtil.getListNegeri());
		context.put("path", getPath());
	}

	@Override
	public void getRelatedData(KuaKuarters r) {
		try {
			mp = new MyPersistence();
			context.put("penghuni", mp
					.list("SELECT p FROM KuaPenghuni p WHERE p.kuarters.id = '"
							+ r.getId() + "' ORDER BY p.tarikhMasuk DESC"));
		} catch (Exception e) {
			System.out.println("Error getRelatedData : " + e.getMessage());
		} finally {
			if (mp != null) { mp.close(); }
		}
	}
}
