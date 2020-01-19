package bph.print;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import lebah.portal.velocity.VTemplate;
import lebah.template.DbPersistence;

import org.apache.velocity.Template;

import portal.module.Util;

public class PrintRekodKuarters extends VTemplate {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4349743529793407370L;
	
	String path = "bph/report/Kuarters";
	DbPersistence db = new DbPersistence();
	private Util util = new Util();
	
	public Template doTemplate() throws Exception {
		setShowVM(false);
		context.put("dateFormat", new SimpleDateFormat("dd-MM-yyyy"));
		context.put("timeFormat", new SimpleDateFormat("hh:mm a"));	
		context.put("numFormat", new DecimalFormat("#,###,###.00"));
		context.put("util", new lebah.util.Util());
		context.put("todayDate", new Date());
		context.put("path", path);
		Template template = engine.getTemplate(getForm());
		return template;
	}
	
	private String getForm() {
		return path + "/laporan_permohonan.html";
	}
	
}
