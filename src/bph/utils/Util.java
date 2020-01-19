package bph.utils;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.TimeZone;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import lebah.template.DbPersistence;
import portal.module.entity.WebLog;
import bph.entities.kod.SebabHilangKelayakanUtk;
import bph.entities.pro.ProAgihanUnit;
import bph.entities.pro.ProKategoriTeknikal;
import bph.entities.qtr.KuaAgihan;
import bph.entities.qtr.KuaPenolakan;

/**
 * 
 * @author faizal
 * 
 * @version 0.1
 */
public class Util {
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
	}

	DbPersistence db = new DbPersistence();
	
	//AZAM ADD
	public static String checkIfStringNull(String txt) {
		if (txt == null || txt == "") {
			return "";
		} else {
			return txt;
		}
	}
	
	public static String getHTMLBR(String x) {
		if (x != null) {
			x = x.replaceAll("(\r\n|\n)", "<br />");
		}
		return x;
	}

	public static int getNumber(String num) {
		if (num == null || "".equals(num))
			return 0;
		try {
			return Integer.parseInt(num);
		} catch (Exception e) {
			return 0;
		}
	}

	public static double getDouble(String num) {
		if (num == null || "".equals(num))
			return 0;
		try {
			return Double.parseDouble(num);
		} catch (Exception e) {
			return 0;
		}
	}

	public static String displayText(String str) {
		if (str.length() > 300)
			return str.substring(0, 299);
		else
			return str;
	}

	public static String displayText(String str, int i) {
		if (str.length() > i)
			return str.substring(0, i - 1);
		else
			return str;
	}

	public static String putLineBreak(String str) {
		if (str != null) {
			StringBuffer txt = new StringBuffer(str);
			char c = '\n';
			while (txt.toString().indexOf(c) > -1) {
				int pos = txt.toString().indexOf(c);
				txt.replace(pos, pos + 1, "<br>");
			}
			return txt.toString();
		} else {
			return "";
		}
	}

	public static String getDateTime(Date date, String format) {
		String afterFormat = "";
		Locale malaysia = new Locale("ms","MY","MY");
		if (date != null) {
			if (date.toString().length() > 0) {
				afterFormat = new java.text.SimpleDateFormat(format,malaysia).format(date);
			} else {
				afterFormat = "";
			}
		} else {
			afterFormat = "";
		}
		return afterFormat;
	}

	public boolean isImageFile(String fileName) {
		return isTypeOfFile(fileName,
				".jpg .jpeg .gif .png .JPG .JPEG .GIF .PNG");
	}

	public boolean isHtmlFile(String fileName) {
		return isTypeOfFile(fileName, ".htm .html .HTM .HTML");
	}

	public boolean isXMLFile(String fileName) {
		return isTypeOfFile(fileName, ".xml .XML");
	}

	public boolean isFlashFile(String fileName) {
		return isTypeOfFile(fileName, ".swf .SWF");
	}

	private boolean isTypeOfFile(String fileName, String allowedExt) {
		try {
			String ext = fileName.substring(fileName.lastIndexOf('.'));
			if (allowedExt.indexOf(ext) > -1)
				return true;
			else
				return false;
		} catch (Exception e) {
			return false;
		}
	}

	public static String[] getTimeZoneIds() {
		return TimeZone.getAvailableIDs();
	}

	public static String formatLuas(float number) {
		try {
			return new java.text.DecimalFormat("#,###,##0.00000").format(number);
		} catch (Exception e) {
			return Float.toString(number);
		}
	}

	public static String formatLuas(Float number) {
		if (number == null)
			return "";
		try {
			return new java.text.DecimalFormat("#,###,##0.00000").format(number);
		} catch (Exception e) {
			return Float.toString(number);
		}
	}

	public static String formatLuas(double number) {
		try {
			return new java.text.DecimalFormat("#,###,##0.00000").format(number);
		} catch (Exception e) {
			return Double.toString(number);
		}
	}

	public static String formatLuas(Double number) {
		if (number == null)
			return "";
		try {
			return new java.text.DecimalFormat("#,###,##0.00000").format(number);
		} catch (Exception e) {
			return Double.toString(number);
		}
	}
	
	public static String formatLuas(Integer number) {
		if (number == null)
			return "";
		try {
			return new java.text.DecimalFormat("#,###,##0.00000").format(number);
		} catch (Exception e) {
			return Integer.toString(number);
		}
	}
	
	public static String formatLuas(String number) {
		if (number == null)
			return "";
		try {
			return new java.text.DecimalFormat("#,###,##0.00000").format(Double.valueOf(number));
		} catch (Exception e) {
			return number;
		}
	}
	
	public static String formatDecimal(float number) {
		try {
			return new java.text.DecimalFormat("#,###,##0.00").format(number);
		} catch (Exception e) {
			return Float.toString(number);
		}
	}

	public static String formatDecimal(Float number) {
		if (number == null)
			return "";
		try {
			return new java.text.DecimalFormat("#,###,##0.00").format(number);
		} catch (Exception e) {
			return Float.toString(number);
		}
	}

	public static String formatDecimal(double number) {
		try {
			return new java.text.DecimalFormat("#,###,##0.00").format(number);
		} catch (Exception e) {
			return Double.toString(number);
		}
	}

	public static String formatDecimal(Double number) {
		if (number == null)
			return "";
		try {
			return new java.text.DecimalFormat("#,###,##0.00").format(number);
		} catch (Exception e) {
			return Double.toString(number);
		}
	}
	
	public static String formatDecimal(Integer number) {
		if (number == null)
			return "";
		try {
			return new java.text.DecimalFormat("#,###,##0.00").format(number);
		} catch (Exception e) {
			return Integer.toString(number);
		}
	}
	
	public static String formatDecimal(String number) {
		if (number == null)
			return "";
		try {
			return new java.text.DecimalFormat("#,###,##0.00").format(Double.valueOf(number));
		} catch (Exception e) {
			return number;
		}
	}

	public static String capitalizedFirstCharacter(String str) {
		boolean cap = true;
		StringBuffer s = new StringBuffer();
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if (cap)
				s.append(String.valueOf(c).toUpperCase());
			else
				s.append(String.valueOf(c).toLowerCase());
			cap = false;
			if (c == ' ')
				cap = true;
		}
		return s.toString();
	}

	public static String capitalizedAllCharacter(String str) {
		String s = "";
		if (str != null) {
			if (str.length() > 0) {
				s = str.toUpperCase();
			}
		}
		return s;
	}

	public static String urlEncode(String str) {
		try {
			return java.net.URLEncoder.encode(str, "UTF-8");
		} catch (Exception e) {
			return str;
		}
	}

	public static boolean dateAfter(Date date1, Date date2) {
		return date1.after(date2);
	}

	public static boolean dateBefore(Date date1, Date date2) {
		return date1.before(date2);
	}

	public static String RemoveComma(String input) {
		if (input == null)
			return "";
		return input.replaceAll(",", "");
	}

	public static String getCurrentDate(String format) {
		String afterFormat = "";
		Date date = new Date();
		if (date != null) {
			if (date.toString().length() > 0) {
				afterFormat = new java.text.SimpleDateFormat(format)
						.format(date);
			} else {
				afterFormat = "";
			}
		}
		return afterFormat;
	}

	public static String replaceIfNull(String input, String replaceWith) {
		String str = "";
		if (input == null || input.trim() == "") {
			str = replaceWith;
		} else {
			str = input;
		}
		return str;
	}
	
	public static String removeNonNumeric(String input) {
		String str = "";
		str = input.replaceAll("[^0-9.]", "");
		return str;
	}

	/**
	 * <b>Delete File &copy; 2015 by Hazwan</b> <br />
	 * <br />
	 * <b>=> deleteFile(dir)</b><br />
	 * <br />
	 * 
	 * @param dir - Directory of File to be deleted
	 */
	public static void deleteFile(String dir) {
		if (dir != null) {
			File file = new File(ResourceBundle.getBundle("dbconnection").getString("folder") + dir);
			try {
				file.delete();
			} catch (Exception e) {
				System.err.println("Exception: " + e.getStackTrace());
			}
		}
	}
		
	/** TIME CONVERTER **/
	public static String getKeteranganMasa(int input) {
		String str = "";
		if (input==0) {
			// DO NOTHING
		} else {
			if (0==input || "24".equals(input)) {
				str = "12.00 AM";
			} else if (1==input) {
				str = "1.00 AM";
			} else if (2==input) {
				str = "2.00 AM";
			} else if (3==input) {
				str = "3.00 AM";
			} else if (4==input) {
				str = "4.00 AM";
			} else if (5==input) {
				str = "5.00 AM";
			} else if (6==input) {
				str = "6.00 AM";
			} else if (7==input) {
				str = "7.00 AM";
			} else if (8==input) {
				str = "8.00 AM";
			} else if (9==input) {
				str = "9.00 AM";
			} else if (10==input) {
				str = "10.00 AM";
			} else if (11==input) {
				str = "11.00 AM";
			} else if (12==input) {
				str = "12.00 PM";
			} else if (13==input) {
				str = "1.00 PM";
			} else if (14==input) {
				str = "2.00 PM";
			} else if (15==input) {
				str = "3.00 PM";
			} else if (16==input) {
				str = "4.00 PM";
			} else if (17==input) {
				str = "5.00 PM";
			} else if (18==input) {
				str = "6.00 PM";
			} else if (19==input) {
				str = "7.00 PM";
			} else if (20==input) {
				str = "8.00 PM";
			} else if (21==input) {
				str = "9.00 PM";
			} else if (22==input) {
				str = "10.00 PM";
			} else if (23==input) {
				str = "11.00 PM";
			}
		}
		return str;
	}
	
	public static boolean isValidEmailAddress(String email) {
	   boolean result = true;
	   try {
	      InternetAddress emailAddr = new InternetAddress(email);
	      emailAddr.validate();
	   } catch (AddressException ex) {
	      result = false;
	   }
	   return result;
	}
	
	public static boolean validateEmail(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
	}
	
	public static int daysBetween(Date d1, Date d2){
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		
		cal1.setTime(d1);
		Date date1 = cal1.getTime();
		cal2.setTime(d2);
		Date date2 = cal2.getTime();
		
        return (int)( (date2.getTime() - date1.getTime()) / (1000 * 60 * 60 * 24));
	}
	
	public static int daysBetween(String d1, String d2) throws ParseException {
		DateFormat format = new SimpleDateFormat("dd-MM-yyyy");
		
		Date date1 = format.parse(d1);
		Date date2 = format.parse(d2);
		
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		
		cal1.setTime(date1);
		Date dateAfter1 = cal1.getTime();
		cal2.setTime(date2);
		Date dateAfter2 = cal2.getTime();
		
        return (int)( (dateAfter2.getTime() - dateAfter1.getTime()) / (1000 * 60 * 60 * 24));
	}
	
	public static int monthBetween(String d1, String d2) throws ParseException {
		DateFormat format = new SimpleDateFormat("dd-MM-yyyy");
		int difInMonths = 0;
		
		if (!"".equals(d1) && !"".equals(d2)) {
			Date date1 = format.parse(d1);
			Date date2 = format.parse(d2);
			
			Calendar sDate = Calendar.getInstance();
			Calendar eDate = Calendar.getInstance();
			sDate.setTime(date1);
			eDate.setTime(date2);
			difInMonths = eDate.get(Calendar.MONTH) - sDate.get(Calendar.MONTH);
		}
		
        return (int)(difInMonths);
	}
	
	public static double sumList(List<Double> list){
		
		if(list==null || list.size() < 1){ return 0; }
		
		double sum = 0d;
		for(Double i: list){
			sum = sum + i;
		}
		return sum;
	}
	
	public static String getMonthNameByInt(int monthId){
		Calendar gc = new GregorianCalendar();
	    gc.set(Calendar.MONTH, monthId);
	    Date monthStart = gc.getTime();
		return getDateTime(monthStart, "MMMM");
	}
	
	public static String getMonthNameInBM(int month){
		String m = "";
		
		if (month == 1) {
			m = "Januari";
		} else if (month == 2) {
			m = "Februari";
		} else if (month == 3) {
			m = "March";
		} else if (month == 4) {
			m = "April";
		} else if (month == 5) {
			m = "Mei";
		} else if (month == 6) {
			m = "Jun";
		} else if (month == 7) {
			m = "Julai";
		} else if (month == 8) {
			m = "Ogos";
		} else if (month == 9) {
			m = "September";
		} else if (month == 10) {
			m = "Oktober";
		} else if (month == 11) {
			m = "November";
		} else if (month == 12) {
			m = "Disember";
		}
		
		return m;
	}
	
	public String getSebabPenolakan(String idAgihan){
		String sebabPenolakan = "";
		
		KuaPenolakan penolakan = (KuaPenolakan) db.get("SELECT p FROM KuaPenolakan p WHERE p.agihan.id = '" + idAgihan + "' AND p.statusTawaran = '02'");
		
		if ( penolakan != null ) {
			sebabPenolakan = penolakan.getSebabPenolakan().getKeterangan();
		}
		
		return sebabPenolakan;
	}

	public String getIdAgihan(String users){
		String idAgihan = "";
		
		KuaAgihan agihan = (KuaAgihan) db.get("SELECT a FROM KuaAgihan a WHERE a.pemohon.id = '" + users + "' AND a.kuarters.id IS NOT NULL AND a.noGiliran <> 0");
		
		if ( agihan != null ) {
			idAgihan = agihan.getId();
		}
		
		return idAgihan;
	}
	
	public Date getIdAgihanTarikhSurat(String users){
		Date tarikhSurat = new Date();
		
		KuaAgihan agihan = (KuaAgihan) db.get("SELECT a FROM KuaAgihan a WHERE a.pemohon.id = '" + users + "' AND a.kuarters.id IS NOT NULL AND a.noGiliran <> 0");
		
		KuaPenolakan penolakan = null; 
				
		if ( agihan != null ) penolakan = (KuaPenolakan) db.get("SELECT p FROM KuaPenolakan p WHERE p.agihan.id = '" + agihan.getId() + "' AND p.statusTawaran = '01'");
		
		if ( penolakan != null ) {
			tarikhSurat = penolakan.getTarikhSurat();
		}
		
		return tarikhSurat;
	}
	
	public Date getTarikhSurat(String idAgihan){
		Date tarikhSurat = new Date();
		
		KuaPenolakan penolakan = (KuaPenolakan) db.get("SELECT p FROM KuaPenolakan p WHERE p.agihan.id = '" + idAgihan + "' AND p.statusTawaran = '01'");
		
		if ( penolakan != null ) {
			tarikhSurat = penolakan.getTarikhSurat();
		}
		
		return tarikhSurat;
	}
	
	@SuppressWarnings("static-access")
	public static String getTarikhTamatSurat(Date date, String format) {
		String afterFormat = "";
		Locale malaysia = new Locale("ms","MY","MY");
		if (date != null) {
			if (date.toString().length() > 0) {
				Calendar cal1 = Calendar.getInstance();
				Calendar cal2 = Calendar.getInstance();
				
				cal1.setTime(date);
//				cal2.add(cal1.DAY_OF_MONTH, 14);
				cal2.add(cal1.DAY_OF_MONTH, 30);
				Date date1 = cal2.getTime();
				
				afterFormat = new java.text.SimpleDateFormat(format,malaysia).format(date1);
			} else {
				afterFormat = "";
			}
		} else {
			afterFormat = "";
		}
		return afterFormat;
	}
	
	@SuppressWarnings("static-access")
	public static String getTempohTamatPotongGaji(Date date, String format) {
		String afterFormat = "";
		Locale malaysia = new Locale("ms","MY","MY");
		if (date != null) {
			if (date.toString().length() > 0) {
				Calendar cal1 = Calendar.getInstance();
				Calendar cal2 = Calendar.getInstance();
				
				cal1.setTime(date);
				cal2.add(cal1.MONTH, 3);
				Date date1 = cal2.getTime();
				
				afterFormat = new java.text.SimpleDateFormat(format,malaysia).format(date1);
			} else {
				afterFormat = "";
			}
		} else {
			afterFormat = "";
		}
		return afterFormat;
	}
	
	
	public static String getTempohTamatPotongGaji(String tarikh, String dateFormat) throws ParseException {
		DateFormat formatDate = new SimpleDateFormat("dd-MM-yyyy");
		Date date = null;
		
		if (!"".equals(tarikh)) date = formatDate.parse(tarikh);
		
		String afterFormat = "";
		if (date != null) {
			if (date.toString().length() > 0) {
				Calendar cal = Calendar.getInstance();
				cal.clear();
				
				cal.setTime(date);
				cal.add(Calendar.MONTH, 3);
				
				afterFormat = new SimpleDateFormat(dateFormat).format(cal.getTime());
			} else {
				afterFormat = "";
			}
		} else {
			afterFormat = "";
		}
		return afterFormat;
	}
	
	public int getNoGiliran(int noGiliranAsal, String idLokasi, String kelasKuarters) {
		int noGiliran = 1;
		
		KuaAgihan agihan = (KuaAgihan) db.get("SELECT a FROM KuaAgihan a WHERE a.permohonan.lokasi.id = '" + idLokasi + "' AND a.kelasKuarters = '" + kelasKuarters + "' AND a.noGiliran <> 0 ORDER BY a.noGiliran ASC");
		
		if ( agihan != null ) {
			noGiliran = noGiliran + (noGiliranAsal - agihan.getNoGiliran());
		}
		
		return noGiliran;
	}
	
	public String getCountry(String ip) throws Exception {
		String countryCode = "";
		
		try {
			URL url = new URL("http://api.wipmania.com/" + ip);
		    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		    
		    int responseCode = connection.getResponseCode();
	        if (responseCode == HttpURLConnection.HTTP_OK) {
			    countryCode = convertStreamToString(connection.getInputStream());
	        } else {
	        	System.out.println("Check your internet connection....");
	        }
		} catch (Exception e) {
			System.out.println("ERROR >> " + e.getMessage());
		}
		
		return countryCode;
	}
	
	public String getPublicIP() {
		String publicIP = "";
		
		try {
			URL url = new URL("http://checkip.amazonaws.com");
		    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		    
		    int responseCode = connection.getResponseCode();
	        if (responseCode == HttpURLConnection.HTTP_OK) {
	        	publicIP = convertStreamToString(connection.getInputStream());
	        } else {
	        	System.out.println("Check your internet connection....");
	        }
		} catch (Exception e) {
			System.out.println("ERROR >> " + e.getMessage());
		}
		
		return publicIP;
	}
	
	public String getUserOS(String browserDetails) {
        String userAgent = browserDetails;
        String os = "";

         if (userAgent.toLowerCase().indexOf("windows") >= 0 ) {
             os = "Windows";
         } else if(userAgent.toLowerCase().indexOf("mac") >= 0) {
             os = "Mac";
         } else if(userAgent.toLowerCase().indexOf("x11") >= 0) {
             os = "Unix";
         } else if(userAgent.toLowerCase().indexOf("android") >= 0) {
             os = "Android";
         } else if(userAgent.toLowerCase().indexOf("iphone") >= 0) {
             os = "IPhone";
         }else{
             os = "UnKnown, More-Info: " + userAgent;
         }
        
        return os;
	}
	
	public String getUserBrowser(String browserDetails) {
        String userAgent = browserDetails;
        String user = userAgent.toLowerCase();

        String browser = "";

        if (user.contains("msie")) {
            String substring=userAgent.substring(userAgent.indexOf("MSIE")).split(";")[0];
            browser = substring.split(" ")[0].replace("MSIE", "IE") + "-" + substring.split(" ")[1];
            browser = "IE";
        } else if (user.contains("edge")) {
        	browser = (userAgent.substring(userAgent.indexOf("Edge")).split(" ")[0]).replace("/", "-");
            browser = "Edge";
        } else if (user.contains("safari") && user.contains("version")) {
            browser = (userAgent.substring(userAgent.indexOf("Safari")).split(" ")[0]).split("/")[0] + "-" + (userAgent.substring(userAgent.indexOf("Version")).split(" ")[0]).split("/")[1];
            browser = "Safari";
        } else if ( user.contains("opr") || user.contains("opera")) {
            if(user.contains("opera")) {
                browser = (userAgent.substring(userAgent.indexOf("Opera")).split(" ")[0]).split("/")[0] + "-" + (userAgent.substring(userAgent.indexOf("Version")).split(" ")[0]).split("/")[1];
            } else if(user.contains("opr")) {
                browser = ((userAgent.substring(userAgent.indexOf("OPR")).split(" ")[0]).replace("/", "-")).replace("OPR", "Opera");
            }
            browser="Opera";
        } else if (user.contains("chrome")) {
            browser = (userAgent.substring(userAgent.indexOf("Chrome")).split(" ")[0]).replace("/", "-");
            browser = "Chrome";
        } else if ((user.indexOf("mozilla/7.0") > -1) || (user.indexOf("netscape6") != -1)  || (user.indexOf("mozilla/4.7") != -1) || (user.indexOf("mozilla/4.78") != -1) || (user.indexOf("mozilla/4.08") != -1) || (user.indexOf("mozilla/3") != -1) ) {
            browser = "Netscape-?";
            browser = "Netscape";
        } else if (user.contains("waterfox")) {
            browser = (userAgent.substring(userAgent.indexOf("Waterfox")).split(" ")[0]).replace("/", "-");
            browser = "Waterfox";
        } else if (user.contains("firefox")) {
            browser = (userAgent.substring(userAgent.indexOf("Firefox")).split(" ")[0]).replace("/", "-");
            browser = "Firefox";
        } else if(user.contains("rv")) {
        	browser = (userAgent.substring(userAgent.indexOf("rv")).split(" ")[0]).replace(":", "-");
            browser = "IE";
        } else {
            browser = "UnKnown, More-Info: " + userAgent;
        }
        
        return browser;
	}
	
	@SuppressWarnings("unchecked")
	public int setWebCounterX() {
		int webCounter = 0;
		
		List<WebLog> webLog = db.list("SELECT x FROM WebLog x");
		
		if ( webLog.size() > 0 ) {
			webCounter = webLog.size() + 1;
		} else {
			webCounter = webCounter + 1;
		}
		
		return webCounter;
	}
	
	@SuppressWarnings("unchecked")
	public int getWebCounter() {
		int webCounter = 0;
		
		try {
			//AZAM CHANGE - Performance tuning
			Long x =  db.getRecordCount("SELECT count(x) FROM WebLog x");
			webCounter = x != null ? x.intValue() : 0;
			/*
			List<WebLog> webLog = db.list("SELECT x FROM WebLog x");
			if (webLog != null) {
				if ( webLog.size() > 0 ) {
					webCounter = webLog.size();
				}
			}*/
				
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return webCounter;
	}
	
	@SuppressWarnings({ "unchecked", "static-access" })
	public int getWebCounter(String sequence) {
		int webCounter = 0;
		String tarikh = "";
		List<WebLog> webLog = null;
		
		if (!"".equals(sequence)) {
			if ("today".equals(sequence)) {
				try {
					tarikh = getDateTime(new Date(), "yyyy-MM-dd");
					webLog = db.list("SELECT x FROM WebLog x WHERE x.firstLogDate LIKE '" + tarikh + "%'");
					
					webCounter = webLog.size();
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if ("yesterday".equals(sequence)) {
				try {
					Calendar cal1 = Calendar.getInstance();
					Calendar cal2 = Calendar.getInstance();
					
					cal1.setTime(new Date());
					cal2.add(cal1.DAY_OF_MONTH, -1);
					Date date1 = cal2.getTime();
					
					tarikh = getDateTime(date1, "yyyy-MM-dd");
					webLog = db.list("SELECT x FROM WebLog x WHERE x.firstLogDate LIKE '" + tarikh + "%'");
					
					webCounter = webLog.size();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		return webCounter;
	}
	
//	public Negara getKodNegara(String kod) {
//		Negara negara = null;
//		
//		Negara getKodNegara = (Negara) db.get("SELECT x FROM Negara x WHERE x.kod1 = '" + kod + "'");
//		
//		if (getKodNegara != null) kodNegara = db.find(Negara.class, getKodNegara.getId());
//        
//		return kodNegara;
//	}
	
	public void setPageView(String userAgent) {
//		String kodNegara = "";
		
//		try {
//			kodNegara = getCountry(getPublicIP());
//		} catch (Exception e1) {
//			e1.printStackTrace();
//		}
		
		//synchronized (this) {
			WebLog wLog = new WebLog();
			wLog.setIPAddress(getIpAddress());
			//AZAM REMARK setCounter
			//wLog.setCounter(setWebCounter());
			wLog.setFirstLogDate(new Date());
//			wLog.setPublicIPAddress(getPublicIP());
//			wLog.setKodNegara(getKodNegara(kodNegara));
			wLog.setOS(getUserOS(userAgent));
			wLog.setBrowser(getUserBrowser(userAgent));
			
			try {
				db.begin();
				db.persist(wLog);
				db.commit();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				//AZAM - 6/9/2015
				//db.rollback();
			}	
		//}
	}
	
	@SuppressWarnings("unused")
	private static boolean getx(int y, int m) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(y, m - 1, 1);
        long expireMillis = calendar.getTime().getTime();
        long currentMillis = System.currentTimeMillis();
        return expireMillis < currentMillis;
    }
    
    public static String getIpAddress() {
    	String ipAddress = "";
    	InetAddress ip;
    	try {
    		ip = InetAddress.getLocalHost();
    		ipAddress = ip.getHostAddress();
    	} catch (UnknownHostException e) {
    		e.printStackTrace();
    	} 
    	
        return ipAddress;
    }

	@SuppressWarnings("unused")
	private String getMacAddress() {
    	String macAddress = "";
    	InetAddress ip;
    	try {
    		ip = InetAddress.getLocalHost();
    		
    		NetworkInterface network = NetworkInterface.getByInetAddress(ip);
     
    		byte[] mac = network.getHardwareAddress();
     
    		StringBuilder sb = new StringBuilder();
    		for (int i = 0; i < mac.length; i++) {
    			sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));		
    		}
    		macAddress = sb.toString();
    	} catch (UnknownHostException e) {
    		e.printStackTrace();
    	} catch (SocketException e){
    		e.printStackTrace();
    	}
    	
        return macAddress;
    }
    
	@SuppressWarnings("resource")
	static String convertStreamToString(java.io.InputStream is) {
	    java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
	    return s.hasNext() ? s.next() : "";
	}
	
	public String removeAlphabet(String str) {
        return str.replaceAll("\\D", "");
    }
	
	public String removeNumber(String str) {
        return str.replaceAll("[0-9]","");
    }
	
	public static List<String> getFinalDateTime(String tarikhMula, String tarikhTamat, String masaMula, String masaTamat, String[] masa) {
		SimpleDateFormat formatDate = new SimpleDateFormat("dd-MM-yyyy");
		List<String> getFinalTime = new ArrayList<String>();
		Date date = null;
		
		int posMasaMula = 0;
		int posMasaTamat = 0;
		int bilHari = 0;
		
		try {
			date = formatDate.parse(tarikhMula);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		Calendar cal = Calendar.getInstance();
		cal.clear();
		
		try {
			 bilHari = Util.daysBetween(tarikhMula, tarikhTamat);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		if ( bilHari == 0 ) {
			for (int i = 0; i < masa.length; i++) {
				if ( masaMula.equals(masa[i]) ) {
					posMasaMula = i;
				}
			}
			
			for (int i = 0; i < masa.length; i++) {
				if ( masaTamat.equals(masa[i]) ) {
					posMasaTamat = i;
				}
			}
			
			for (int i = posMasaMula; i <= posMasaTamat; i++) {
				getFinalTime.add(tarikhMula + " " + masa[i]);
			}
		} else if ( bilHari == 1 ) {
			for (int i = 0; i < masa.length; i++) {
				if ( masaMula.equals(masa[i]) ) {
					posMasaMula = i;
				}
			}
			
			for (int i = 0; i < masa.length; i++) {
				if ( masaTamat.equals(masa[i]) ) {
					posMasaTamat = i;
				}
			}
			
			for (int i = posMasaMula; i < masa.length; i++) {
				getFinalTime.add(tarikhMula + " " + masa[i]);
			}
			
			for (int i = posMasaTamat; i > -1; i--) {
				getFinalTime.add(tarikhTamat + " " + masa[i]);
			}
		} else {
			for (int i = 0; i < masa.length; i++) {
				if ( masaMula.equals(masa[i]) ) {
					posMasaMula = i;
				}
			}
			
			for (int i = 0; i < masa.length; i++) {
				if ( masaTamat.equals(masa[i]) ) {
					posMasaTamat = i;
				}
			}
			
			for (int i = posMasaMula; i < masa.length; i++) {
				getFinalTime.add(tarikhMula + " " + masa[i]);
			}
			
			for (int x = 1; x < bilHari; x++) {
				cal.setTime(date);
				cal.add(Calendar.DATE, x);
				for (int i = 0; i < masa.length; i++) {
					getFinalTime.add(new SimpleDateFormat("dd-MM-yyyy").format(cal.getTime()) + " " + masa[i]);
				}
			}
			
			for (int i = posMasaTamat; i > -1; i--) {
				getFinalTime.add(tarikhTamat + " " + masa[i]);
			}
		}
		
        return getFinalTime;
    }
	
	public static List<String> getFinalDate(String tarikhMula, String tarikhTamat) {
		SimpleDateFormat formatDate = new SimpleDateFormat("dd-MM-yyyy");
		List<String> getFinalTime = new ArrayList<String>();
		Date date = null;
		
		int bilHari = 0;
		
		try {
			date = formatDate.parse(tarikhMula);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		Calendar cal = Calendar.getInstance();
		cal.clear();
		
		try {
			 bilHari = Util.daysBetween(tarikhMula, tarikhTamat);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		for (int x = 0; x < bilHari + 1; x++) {
			cal.setTime(date);
			cal.add(Calendar.DATE, x);
			getFinalTime.add(new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime()));
		}
		
        return getFinalTime;
    }
	
	public static double getDoubleRemoveComma(String num) {
		if (num == null || "".equals(num))
			return 0;
		try {
			return Double.parseDouble(RemoveComma(num));
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return 0;
		}
	}
	
	public List<SebabHilangKelayakanUtk> getListSebabHilangKelayakanUtk() {
		List<SebabHilangKelayakanUtk> list = db.list("select x from SebabHilangKelayakanUtk x");
		return list;
	}
	
	//*********************** START HECKBOX UNTUK SENARAI UNIT **************************
	public String getCheckUnit(int idCheckUnit, String idAduan) {
		String x = "";

		ProAgihanUnit unitCheck = (ProAgihanUnit) db.get("SELECT p FROM ProAgihanUnit p WHERE p.idSenaraiUnit.id = " + idCheckUnit + " AND p.aduan.id ='"+ idAduan +"'");
				
		if ( unitCheck != null ) {
			x = "checked";
		} else {
			x = "";
		}
		
		return x;
	}
	//*********************** END HECKBOX UNTUK SENARAI UNIT **************************
	
	//*********************** START HECKBOX UNTUK KETERANGAN TEKNIKAL PADA PRO PENGGUNA **************************
	public String getChecked(int idCheck, String idAduan) {
		String x = "";
		
		ProKategoriTeknikal pkt = (ProKategoriTeknikal) db.get("SELECT p FROM ProKategoriTeknikal p WHERE p.idKeteranganTeknikal.id = " + idCheck + " AND p.aduan.id = '" + idAduan + "'");
		
		if ( pkt != null ) {
			x = "checked";
		} else {
			x = "";
		}
		
		return x;
	}
	//*********************** END HECKBOX UNTUK KETERANGAN TEKNIKAL PADA PRO PENGGUNA **************************
	
	public static Date convertStrToDate(String strdate,String format) {
		Date date = null;
		if (strdate != null && format != null) {
			DateFormat sdf = new SimpleDateFormat(format,Locale.ENGLISH);
			try {
				date = sdf.parse(strdate);
			} catch (ParseException e) {
				System.out.println("error Util.convertStrToDate");
			}
		}
		return date;
	}
	
	public static Date getNextWorkingDate(Date startDate, int totalDays) {
		Date nextWorkingDate = null;
		try {
			Calendar cal = new GregorianCalendar();
			cal.setTime(startDate);
			
			int i = 0;
			while (i < totalDays) {
				cal.add(Calendar.DATE, 1);
				if (cal.get(Calendar.DAY_OF_WEEK) != 1 && cal.get(Calendar.DAY_OF_WEEK) != 7) {
					i++;
				}
			}
			nextWorkingDate = cal.getTime();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return nextWorkingDate;		
	}
	
}




