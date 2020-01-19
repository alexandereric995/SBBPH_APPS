/**
 * 
 */
package dataCleaning.kew;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.GregorianCalendar;
import java.util.List;

import lebah.template.DbPersistence;
import bph.entities.kewangan.KewBayaranResit;
import bph.entities.kewangan.SeqNoResit;

/**
 * @author Mohd Faizal
 *
 */
public class RegenerateKewSeqResitOnline {

	private static DbPersistence db;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
		
			repairSeqResitOnline();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	

	private static void repairSeqResitOnline() throws IOException {
		System.out.println("START repairSeqResitOnline");
		try {
			db = new DbPersistence();
			db.begin();
			
			GregorianCalendar cal = (GregorianCalendar) GregorianCalendar.getInstance();
			
			List<SeqNoResit> listSeqResit = db.list("select x from SeqNoResit x where x.kodJuruwang is null");
			for (int h = 0; h < listSeqResit.size(); h++) {
				db.remove(listSeqResit.get(h));
			}
			
			for (int year = 2015; year <= 2019; year++) {
				for (int month = 1; month <= 12; month++) {
					for (int day = 1; day <= 31; day++) {
						System.out.println(day + "-" + month + "-" + year);
						if (month == 2) {
							if (cal.isLeapYear(year)) {
								if (day > 29) 
									break;
							} else {
								if (day > 28) 
									break;
							}
						}
						String date = year + "-" + new DecimalFormat("00").format(month) + "-" + new DecimalFormat("00").format(day);
						KewBayaranResit resit = (KewBayaranResit) db.get("select x from KewBayaranResit x where x.flagJenisBayaran = 'ONLINE' and x.tarikhBayaran = '" + date + "' and x.noResit like '%" + year + "09%' order by x.noResit desc");
						if (resit != null) {
							SeqNoResit seq = new SeqNoResit();
							String id = year + new DecimalFormat("00").format(month) + new DecimalFormat("00").format(day) + "09";
							seq.setId(id);
							seq.setDay(day);
							seq.setMonth(month);
							seq.setYear(year);
							seq.setKodJuruwang(null);
							seq.setBil(Integer.parseInt(resit.getNoResit().substring(resit.getNoResit().length() - 5)));
							db.persist(seq);
						}
					}
				}
			}			
			
			db.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		System.out.println("END repairSeqResitOnline");
	}
}
