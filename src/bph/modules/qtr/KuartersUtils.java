package bph.modules.qtr;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

import lebah.db.UniqueID;
import lebah.template.DbPersistence;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import portal.module.entity.Users;
import bph.entities.integrasi.IntHRMIS;
import bph.entities.kod.Status;
import bph.entities.qtr.KuaAgihan;
import bph.entities.qtr.KuaLog;

public class KuartersUtils {

	private DbPersistence db = new DbPersistence();
	static Logger myLogger = Logger.getLogger("bph/modules/qtr/KuartersUtils");

	public Status getStatus(String turutan, String seksyen, DbPersistence db)
			throws Exception {
		@SuppressWarnings("unchecked")
		List<Status> listStatus = db
				.list("SELECT x FROM Status x WHERE x.turutan ='" + turutan
						+ "' AND x.seksyen.id = '" + seksyen + "' ");
		Status status = db.find(Status.class,
				(!listStatus.isEmpty() ? listStatus.get(0).getId() : ""));
		myLogger.debug("value status =" + status.getId());
		return status;
	}

	public static Double getRange(String poskod_awal, String poskod_akhir)
			throws Exception {
		String jarak = "0.0";

		try {
			URL url = new URL(
					"http://maps.googleapis.com/maps/api/distancematrix/json?origins="
							+ poskod_awal
							+ "%Malaysia&destinations="
							+ poskod_akhir.trim().toLowerCase().replaceAll(" ",
									"%20")
							+ "%Malaysia&language=en-EN&sensor=true");
			// myLogger.debug("GET RANGE URL :::: " + url);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();

			int responseCode = connection.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				JSONObject json = new JSONObject(
						convertStreamToString(connection.getInputStream()));

				JSONArray rows = json.getJSONArray("rows");

				for (int i = 0; i < rows.length(); i++) {
					JSONObject obj = rows.getJSONObject(i);
					JSONArray elements = obj.getJSONArray("elements");

					// myLogger.debug("ELEMENTS LENGTH ::::: " +
					// elements.length());

					for (int j = 0; j < elements.length(); j++) {
						JSONObject elem = elements.getJSONObject(j);

						try {
							JSONObject distance = elem
									.getJSONObject("distance");
							jarak = distance.getString("text").replaceAll(
									" km", "");

							// myLogger.debug("JARAK SEBENAR ::::::: " + jarak);
						} catch (Exception e1) {
							myLogger.debug("STATUS ::::::: "
									+ elem.getString("status"));
							myLogger.debug("ERROR ::::::: " + e1.getMessage());
						}
					}
				}
			} else {
				myLogger.debug("Check your internet connection....");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return Double.parseDouble(jarak);
	}

	@SuppressWarnings("unchecked")
	public static HashMap getRangeMap(String poskod_awal, String poskod_akhir)
			throws Exception {
		HashMap jarak = new HashMap();

		try {
			URL url = new URL(
					"http://maps.googleapis.com/maps/api/distancematrix/json?origins="
							+ poskod_awal
							+ "%Malaysia&destinations="
							+ poskod_akhir.trim().toLowerCase().replaceAll(" ",
									"%20")
							+ "%Malaysia&language=en-EN&sensor=true");
			myLogger.debug("GET RANGE URL :::: " + url);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();

			int responseCode = connection.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				JSONObject json = new JSONObject(
						convertStreamToString(connection.getInputStream()));

				JSONArray rows = json.getJSONArray("rows");

				for (int i = 0; i < rows.length(); i++) {
					JSONObject obj = rows.getJSONObject(i);
					JSONArray elements = obj.getJSONArray("elements");

					myLogger
							.debug("ELEMENTS LENGTH ::::: " + elements.length());

					for (int j = 0; j < elements.length(); j++) {
						JSONObject elem = elements.getJSONObject(j);

						myLogger.debug("" + elem.toString());

						// if (elem.toString().contains("distance")) {
						try {
							JSONObject distance = elem
									.getJSONObject("distance");
							jarak.put(j, distance.getString("text").replaceAll(
									" km", ""));

							myLogger.debug("JARAK SEBENAR ::::::: "
									+ jarak.get(j));
						} catch (Exception e1) {
							jarak.put(j, "0");
							myLogger.debug("STATUS ::::::: "
									+ elem.getString("status"));
							myLogger.debug("ERROR ::::::: " + e1.getMessage());
						}
						// } else {
						// jarak.put(j, "0");
						// }
					}
				}
			} else {
				myLogger.debug("Check your internet connection....");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		myLogger.debug("JARAK SIZE :: " + jarak.size());

		return jarak;
	}

	@SuppressWarnings("unchecked")
	public static List getListRangeMap(String poskod_awal, String poskod_akhir)
			throws Exception {
		List jarak = new ArrayList();

		try {
			URL url = new URL(
					"http://maps.googleapis.com/maps/api/distancematrix/json?origins="
							+ poskod_awal.trim().toLowerCase().replaceAll(" ",
									"%20")
							+ "%Malaysia&destinations="
							+ poskod_akhir.trim().toLowerCase().replaceAll(" ",
									"%20")
							+ "%Malaysia&language=en-EN&sensor=true");
			myLogger.debug("GET RANGE URL :::: " + url);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();

			int responseCode = connection.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				JSONObject json = new JSONObject(
						convertStreamToString(connection.getInputStream()));

				JSONArray rows = json.getJSONArray("rows");

				for (int i = 0; i < rows.length(); i++) {
					JSONObject obj = rows.getJSONObject(i);
					JSONArray elements = obj.getJSONArray("elements");

					for (int j = 0; j < elements.length(); j++) {
						JSONObject elem = elements.getJSONObject(j);

						try {
							JSONObject distance = elem
									.getJSONObject("distance");
							Double realDist = Double.parseDouble(distance
									.getString("value")) / 1000;
							myLogger.debug("REAL DESTINATION " + j
									+ " ::::::: " + realDist);
							jarak.add(realDist);
						} catch (Exception e1) {
							myLogger.debug("ERROR ::::::: " + e1.getMessage());
						}
					}
				}
			} else {
				myLogger.debug("CHECK YOUR INTERNET CONNECTION....");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return jarak;
	}

	@SuppressWarnings("unchecked")
	public static List getDistanceBetweenLocation(String alamatRumah,
			String mercuTanda) throws Exception {
		List point = new ArrayList();

		try {
			URL url = new URL(
					"http://maps.google.com/maps/api/geocode/json?address="
							+ alamatRumah);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();

			int responseCode = connection.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				JSONObject json = new JSONObject(
						convertStreamToString(connection.getInputStream()));

				JSONArray results = json.getJSONArray("results");

				for (int i = 0; i < results.length(); i++) {
					JSONObject obj = results.getJSONObject(i);
					JSONObject geometry = obj.getJSONObject("geometry");
					JSONObject location = geometry.getJSONObject("location");
					// for (int j = 0; j < location.length(); j++) {
					point.add(location.getString("lat"));
					point.add(location.getString("lng"));
					// }
				}
			} else {
				myLogger.debug("CHECK YOUR INTERNET CONNECTION....");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return point;
	}

	static String convertStreamToString(java.io.InputStream is) {
		java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
		return s.hasNext() ? s.next() : "";
	}

	@SuppressWarnings("unchecked")
	public double autoFixNoGiliran(String idLokasi) {
		double percent = 0;
		myLogger.debug("Running AutoFix NoGiliran...");
		List<KuaAgihan> x = db.list("SELECT x FROM KuaAgihan x WHERE x.permohonan.lokasi.id = '"+ idLokasi + "' GROUP BY x.kelasKuarters");
		myLogger.debug("KUA AGIHAN SIZE : " + x.size());
		for (int f = 0; f < x.size(); f++) {
			myLogger.debug("LOKASI : " + idLokasi + " | KELAS KUARTERS : "+ x.get(f).getKelasKuarters());
			
			List<KuaAgihan> agihan = db
			.list("SELECT x FROM KuaAgihan x WHERE x.permohonan.lokasi.id = '"+ idLokasi
				+ "' AND x.kelasKuarters = '"+ x.get(f).getKelasKuarters()
				+ "' AND x.status.id = '1432614959825' GROUP BY x.permohonan.id ORDER BY x.permohonan.tarikhPermohonan ASC");
			
			// List<KuaAgihan> agihan =
			// db.list("SELECT x FROM KuaAgihan x WHERE x.permohonan.lokasi.id = '"
			// + idLokasi +
			// "' AND x.kelasKuarters = 'B' AND x.status.id = '1432614959825' GROUP BY x.permohonan.id ORDER BY x.permohonan.tarikhPermohonan ASC");
			
			myLogger.debug("START AUTOFIX");
			for (int i = 0; i < agihan.size(); i++) {
				KuaAgihan a = db.find(KuaAgihan.class, agihan.get(i).getId());
				int n = i + 1;
				myLogger.debug("ID PERMOHONAN : "+ agihan.get(i).getPermohonan().getId()+ " | No. Giliran Baru : " + n);
				a.setNoGiliran(n);
				db.begin();
				try {
					db.commit();
				} catch (Exception e) {
					e.printStackTrace();
				}
				percent = ((n) * 100) / agihan.size();
				myLogger.debug("COMPLETION : " + n + "/" + agihan.size() + " "+ percent + "%");
			}
			myLogger.debug("COMPLETE AUTOFIX (Total Data Fix) : "+ agihan.size());
		}
		return percent;
	}

	public void kuartersLog(int action, String tableName, Users idLog,
			String idReference) {
		// Users usersLog = db.find(Users.class, idLog);
		KuaLog log = new KuaLog();

		log.setAction(action);
		log.setTimeStamp(new Date());
		log.setTableName(tableName);
		log.setUsersLog(idLog);
		log.setIdReference(idReference);

		// db.begin();
		db.persist(log);
		// try {
		// db.commit();
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
	}

	@SuppressWarnings("unchecked")
	public String uploadFile(HttpServletRequest request, String folderUpload,
			String idItem) throws Exception {
		String fileName = "";

		String dirUpload = ResourceBundle.getBundle("dbconnection").getString(
				"folder")
				+ "qtr/" + folderUpload + "/" + idItem + "/";

		String[] newDir = dirUpload.split("/");
		String createDir = newDir[0];

		for (int i = 1; i < newDir.length; i++) {
			createDir = createDir + "/" + newDir[i];
			File dir = new File(createDir);
			if (!dir.exists())
				dir.mkdir();
		}

		DiskFileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		List items = upload.parseRequest(request);
		Iterator itr = items.iterator();
		List<FileItem> files = new ArrayList<FileItem>();
		while (itr.hasNext()) {
			FileItem item = (FileItem) itr.next();
			if ((!(item.isFormField())) && (item.getName() != null)
					&& (!("".equals(item.getName())))) {
				files.add(item);
			}
		}

		for (FileItem item : files) {
			String avatarName = "";

			System.out.println("ITEM NAME ::: " + item.getName());

			String fileExt = FilenameUtils.getExtension(item.getName());
			fileName = UniqueID.getUID() + "." + fileExt;
			String imgName = dirUpload + fileName;

			imgName = imgName.replaceAll(" ", "_");
			item.write(new File(imgName));

			String mimetype = item.getContentType();
			String type = mimetype.split("/")[0];
			if (type.equals("image")) {
				avatarName = imgName.substring(0, imgName.lastIndexOf("."))
						+ "_thumbnail"
						+ imgName.substring(imgName.lastIndexOf("."));
				avatarName = avatarName.replaceAll(" ", "_");
				lebah.repository.Thumbnail.create(imgName, imgName, 600, 560,
						100);
				lebah.repository.Thumbnail.create(imgName, avatarName, 150, 90,
						100);
			}

		}

		return fileName;
	}

	public Date getTarikhLahir(String myKad) throws ParseException {
		Date date = new Date();
		DateFormat formatDate = new SimpleDateFormat("dd-MM-yy");
		String tarikhLahir = myKad.substring(4, 6) + "-"
				+ myKad.substring(2, 4) + "-" + myKad.substring(0, 2);

		if (myKad.trim().length() > 0) {
			// myLogger.debug("MYKAD ::: " + myKad.substring(0, 6));
			// myLogger.debug("Tarikh Lahir ::: " + tarikhLahir);
			date = formatDate.parse(tarikhLahir);
		}

		return date;
	}

	public double tarikDataHRMIS(String noMyKad) throws Exception {
		double percent = 0;
		myLogger.debug("Running Data HRMIS...");

		String[] kelompokMyKad = noMyKad.split(",");

		DbPersistence db = new DbPersistence();

		myLogger.debug("START TARIK DATA HRMIS");

		for (int i = 0; i < kelompokMyKad.length; i++) {
			IntHRMIS hrmis = (IntHRMIS) db
					.get("SELECT h FROM IntHRMIS h WHERE h.noPengenalan = '"
							+ kelompokMyKad[i] + "'");

			if (hrmis != null) {
				// Users users = (Users)
				// db.get("SELECT u FROM Users u WHERE u.noKP = '"+
				// kelompokMyKad[i] + "'");
				// UsersJob usersJob = (UsersJob)
				// db.get("SELECT uj FROM UsersJob uj WHERE uj.users.id = '"+
				// users.getId() + "'");
				// UsersSpouse usersSpouse = (UsersSpouse)
				// db.get("SELECT us FROM UsersSpouse us WHERE us.users.id = '"+
				// users.getId() + "'");

				// Users u = db.find(Users.class, users.getId());
				// UsersJob uj = db.find(UsersJob.class, usersJob.getId());
				// UsersSpouse us = db.find(UsersSpouse.class,
				// usersSpouse.getId());

				// for (int i = 0; i < agihan.size(); i++) {
				// KuaAgihan a = db.find(KuaAgihan.class,
				// agihan.get(i).getId());
				// int n = i + 1;
				//					
				// a.setNoGiliran(n);
				//					
				// db.begin();
				// db.commit();
				//					
				// percent = ((n) * 100) / agihan.size();
				//					
				// myLogger.debug("COMPLETION : " + n + "/" + agihan.size() +
				// " " + percent + "%");
				// }
			} else {
				myLogger.debug("BAGI NO MYKAD : " + kelompokMyKad[i]
						+ " DATA TIDAK DAPAT DITARIK...");
			}
		}

		myLogger.debug("COMPLETE TARIK DATA HRMIS (Total Data Tarik) : "
				+ kelompokMyKad.length);

		return percent;
	}

	/*
	 * public void createRecordBayaran(KuaPermohonan r,String pemohonId){ Users
	 * user = db.find(Users.class, pemohonId); String catatan = null;
	 * 
	 * KuaAkaun_1 mn = new KuaAkaun_1(); mn.setPermohonan(r);
	 * mn.setAmaunBayaranSeunit(kadarSewa); mn.setBilanganUnit(1);
	 * mn.setDebit(r.getAmaun()); mn.setKredit(0d); mn.setFlagBayar("T");
	 * mn.setFlagVoid("T");
	 * mn.setKeterangan("DEPOSIT KUARTERS KERAJAAN (BPH) - KELAS "+r.);
	 * mn.setCatatan(catatan); mn.setKodHasil(db.find(KodHasil.class, "74299"));
	 * //BAYARAN-BAYARAN SEWA YANG LAIN mn.setNoInvois(r.getIdTempahan());
	 * //TEMPORARY SET TO ID TEMPAHAN PERMOHONAN mn.setTarikhInvois(new Date());
	 * mn.setIdMasuk(user); mn.setIdKemaskini(user); mn.setTarikhMasuk(new
	 * Date()); mn.setTarikhKemaskini(new Date()); db.persist(mn);
	 * createInvoisInFinance(mn,user);
	 * 
	 * }
	 * 
	 * public void createInvoisInFinance(UtilAkaun ak,Users user){ //push to
	 * invois kewangan KewInvois inv = new KewInvois(); inv.setFlagBayar("T");
	 * inv.setDebit(ak.getDebit()); //inv.setKredit(ak.getDebit());
	 * inv.setFlagBayaran("SEWA"); inv.setFlagQueue("T");
	 * inv.setIdLejar(ak.getId());
	 * inv.setJenisBayaran(db.find(KewJenisBayaran.class,"03")); // 03 - UTILITI
	 * inv.setKeteranganBayaran(ak.getKeterangan().toUpperCase());
	 * inv.setKodHasil(ak.getKodHasil()); inv.setNoInvois(ak.getNoInvois());
	 * //inv.setNoRujukan(ak.getPermohonan().getNoTempahan().toUpperCase());
	 * inv.setNoRujukan(ak.getPermohonan().getIdTempahan());
	 * inv.setPembayar(ak.getPermohonan().getPemohon());
	 * inv.setTarikhInvois(ak.getTarikhInvois()); inv.setUserPendaftar(user);
	 * inv.setTarikhDaftar(new Date()); inv.setUserKemaskini(user);
	 * inv.setTarikhKemaskini(new Date());
	 * inv.setTarikhDari(ak.getPermohonan().getTarikhMula());
	 * inv.setTarikhHingga(ak.getPermohonan().getTarikhTamat());
	 * db.persist(inv); }
	 */

}
