package bph.scheduler.dev;

import java.sql.ResultSet;
import java.util.ArrayList;

import lebah.db.Db;
import lebah.template.DbPersistence;
import bph.entities.pembangunan.DevCadangan;

public class DevScheduler {

	public void sample() {
		System.out.println("Sample DEV Scheduler");
	}

	@SuppressWarnings("unchecked")
	public void autoUpdateProjekDalamPembinaan() throws Exception {
		System.out.println("running autoUpdateProjekDalamPembinaan...");

		DbPersistence db = new DbPersistence();
		Db db1 = null;
		String sql = "";
		String idStatus = "1"; //status dalam perancangan
		String idStatusBaru = "2"; //status dalam pembinaan
		String idTelahDisahkan = "4"; //status projek telah disahkan
		ArrayList<DevCadangan> list = new ArrayList<DevCadangan>();

		// get list projek dalam perancangan (equal today date)
		// CRITERIA : disahkan_oleh is not null (telah disahkan)
		try {

			db1 = new Db();

			sql = "select x.* from dev_cadangan x where x.status_cadangan ='" + idStatus 
			+ "' and x.id_status ='" + idTelahDisahkan + "'"
			+ " and x.tarikh_milik_tapak <= current_date()";

			ResultSet rs = db1.getStatement().executeQuery(sql);

			while (rs.next()) {
				DevCadangan dc = db.find(DevCadangan.class, rs.getString("id"));
				list.add(dc);
			}

		} finally {
			if (db1 != null)
				db1.close();
		}

		// find and update/create record by devcadangan
		db.begin();
		System.out.println("bil projek dalam perancangan : " + list.size());
		for (int i = 0; i < list.size(); i++) {

			// update status projek
			list.get(i).setStatusCadangan(idStatusBaru); //update menjadi projek dalam pembinaan

		}

		try {
			db.commit();
		} catch (Exception ex) {
			System.out.println("Error saving/update record after autobatal : "
					+ ex.getMessage());
			ex.printStackTrace();
		}

	}

	@SuppressWarnings("unchecked")
	public void autoUpdateProjekTelahSiap() throws Exception {
		System.out.println("running autoUpdateProjekTelahSiap...");

		DbPersistence db = new DbPersistence();
		Db db1 = null;
		String sql = "";
		String idStatus = "2"; //status dalam perancangan
		String idStatusBaru = "3"; //status dalam pembinaan
		ArrayList<DevCadangan> list = new ArrayList<DevCadangan>();

		// get list projek dalam perancangan (equal today date)
		// CRITERIA : disahkan_oleh is not null (telah disahkan)
		try {

			db1 = new Db();

			sql = "select x.* from dev_cadangan x where x.status_cadangan ='" + idStatus + "'"
			+ " and x.tarikh_siap_sebenar <= current_date()";;

			ResultSet rs = db1.getStatement().executeQuery(sql);

			while (rs.next()) {
				DevCadangan dc = db.find(DevCadangan.class, rs.getString("id"));
				list.add(dc);
			}

		} finally {
			if (db1 != null)
				db1.close();
		}

		// find and update/create record by devcadangan
		db.begin();
		System.out.println("bil projek dalam pembinaan : " + list.size());
		for (int i = 0; i < list.size(); i++) {

			// update status projek
			list.get(i).setStatusCadangan(idStatusBaru); //update menjadi projek dalam pembinaan

		}

		try {
			db.commit();
		} catch (Exception ex) {
			System.out.println("Error saving/update record after autobatal : "
					+ ex.getMessage());
			ex.printStackTrace();
		}

	}
}
