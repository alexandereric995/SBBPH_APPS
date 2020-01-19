/**
 * @author muhdsyazreen
 */

package bph.modules.kewangan.terimaanHasil;

import java.util.ResourceBundle;

import bph.utils.Util;
import bph.utils.UtilKewangan;

public class SejarahPembatalanResitRecordModule extends PembatalanResitRecordModule{

	private static final long serialVersionUID = 1L;
	UtilKewangan utilKewangan = new UtilKewangan();
	
	@Override
	public void begin() {
		context.remove("showButtonBatal");
		
		userRole = (String) request.getSession().getAttribute("_portal_role");
		
		userId = (String) request.getSession().getAttribute("_portal_login");
		this.addFilter("noResit is not null and COALESCE(x.flagVoid,'T') = 'Y' ");
		defaultButtonOption();
		
		context.put("path", getPath());
		context.put("util", new Util());
		context.put("userRole",userRole);
		context.put("command", command);
		context.put("uploadDir", ResourceBundle.getBundle("dbconnection").getString("folder"));
	}
	
	public void defaultButtonOption() {
		this.setReadonly(true);
		this.setDisableDefaultButton(true);
		this.setDisableBackButton(true);
	}
}



