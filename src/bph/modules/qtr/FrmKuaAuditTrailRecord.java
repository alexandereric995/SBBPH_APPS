package bph.modules.qtr;

import java.text.SimpleDateFormat;

import lebah.portal.AuditTrailRecordModule;

public class FrmKuaAuditTrailRecord extends AuditTrailRecordModule {

	private static final long serialVersionUID = 837436079783085545L;

	@Override
	public void begin() {
		this.addFilter("moduleClass LIKE '%FrmKua%'");
		this.setOrderBy("masaAktiviti");
		this.setOrderType("desc");
		this.setReadonly(true);
		this.setRecordOnly(true);
		SimpleDateFormat shortDate = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat shortTime = new SimpleDateFormat("hh:mm a");
		context.put("shortDate", shortDate);
		context.put("shortTime", shortTime);
		context.put("path", getPath());
	}
}
