package bph.modules.qtr;

import bph.utils.DataUtil;

public class FrmKuaPenghuniHistoryRecord extends FrmKuaPenghuniRecord {
	private static final long serialVersionUID = 8652059520249054563L;
	private DataUtil dataUtil;

	@Override
	public void begin() {
		dataUtil = DataUtil.getInstance(db);
		setDisableKosongkanUpperButton(true);
		setDisableAddNewRecordButton(true);
		setHideDeleteButton(true);
		this.addFilter("tarikhKeluarKuarters IS NOT NULL");
		context.put("selectGelaran", dataUtil.getListGelaran());
		context.put("selectJenisPengenalan", dataUtil.getListJenisPengenalan());
		context.put("selectAgama", dataUtil.getListAgama());
		context.put("selectJantina", dataUtil.getListJantina());
		context.put("selectBangsa", dataUtil.getListBangsa());
		context.put("selectEtnik", dataUtil.getListEtnik());
		context.put("selectStatusPerkahwinan", dataUtil
				.getListStatusPerkahwinan());
		context.put("selectNegeri", dataUtil.getListNegeri());
		context.put("selectNegeriSemasa", dataUtil.getListNegeri());
		context.put("findLokasiKuarters", dataUtil.getListLokasiKuarters());
		context.put("findJantina", dataUtil.getListJantina());
		context.put("findBangsa", dataUtil.getListBangsa());
		context.put("findStatusPerkahwinan", dataUtil
				.getListStatusPerkahwinan());
		context.put("path", getPath());
	}
}
