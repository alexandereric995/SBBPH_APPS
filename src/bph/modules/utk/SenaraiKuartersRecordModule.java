package bph.modules.utk;

import java.util.List;

import lebah.portal.action.Command;
import bph.entities.kod.StatusKuarters;
import bph.modules.qtr.FrmKuaKuartersRecord;
import bph.utils.DataUtil;

public class SenaraiKuartersRecordModule extends FrmKuaKuartersRecord {

	private static final long serialVersionUID = 7859853801709412089L;
	private DataUtil dataUtil;


//	@Override
//	public Class getIdType() {
//		return String.class;
//	}

//	@Override
//	public Class<KuaKuarters> getPersistenceClass() {
//		return KuaKuarters.class;
//	}

//	@Override
//	public String getPath() {
//		return "bph/modules/qtr/kuarters";
//	}

	@Override
	public void begin() {
		dataUtil = DataUtil.getInstance(db);
		context.put("selectNegeri", dataUtil.getListNegeri());
		context.put("selectKelasKuarters", dataUtil.getListKelasPeranginan());
		context.put("selectJenisKediaman", dataUtil.getListJenisKediaman());
		context.put("selectJenisKegunaanKuarters", dataUtil.getListJenisKegunaanKuarters());
		context.put("selectStatusKuarters", dataUtil.getListStatusKuartersBaru());
		context.put("selectLokasiKuarters", dataUtil.getListLokasiKuarters());
		context.put("selectLokasiDibenar", dataUtil.getListLokasiDibenar());
		context.put("selectFasa", dataUtil.getListFasa());
		context.put("findKelasKuarters", dataUtil.getListKelasPeranginan());
		context.put("findJenisKediaman", dataUtil.getListJenisKediaman());
		context.put("findStatusKuarters", dataUtil.getListStatusKuartersBaru());
		context.put("findFasa", dataUtil.getListFasa());
		context.put("findNegeri", dataUtil.getList6Negeri());
		context.put("path", getPath());
		this.setDisableSaveAddNewButton(true);
		this.setDisableKosongkanUpperButton(true);
		this.setDisableUpperBackButton(true);
		this.setHideDeleteButton(true);
		setDisableDefaultButton(true);
		setDisableAddNewRecordButton(true);
	}
	
	@Command("findLokasiKuarters")
	public String findLokasiKuarters() throws Exception {
		String idNegeri = "0";
		if (get("findNegeri").trim().length() > 0)
			idNegeri = get("findNegeri");
		context.put("findLokasiKuarters", dataUtil.getListLokasiKuarters(idNegeri));
		return getPath() + "/find/findLokasiKuarters.vm";
	}
	
	@Command("findJalan")
	public String findJalan() throws Exception {
		String idLokasi = "0";
		if (get("findLokasiKuarters").trim().length() > 0)
			idLokasi = get("findLokasiKuarters");
		context.put("findJalan", dataUtil.getListJalanKuarters(idLokasi));
		return getPath() + "/find/findJalan.vm";
	}
	
	@Command("findDetailKuarters")
	public String findDetailKuarters() throws Exception {
		String idStatus = "0";
		if (getParam("status_kuarters") != null) {
			idStatus = getParam("status_kuarters");
		}
		List<StatusKuarters> list = dataUtil.getListDetailKuarters(idStatus);
		context.put("findDetailKuarters", list);
		return getPath() + "/find/findDetailKuarters.vm";
	}

}
