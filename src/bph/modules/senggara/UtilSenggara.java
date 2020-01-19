package bph.modules.senggara;

import java.sql.ResultSet;

import lebah.db.Db;

public class UtilSenggara {
	
	public static String getPenyediaSecondary() {
		Db lebahDb = null;
		String sql = "";
		String pyName = "";
		try {
			lebahDb = new Db();
			sql = "SELECT user_id, role_id FROM user_role WHERE role_id = '(SENGGARA) Penyedia'";
			ResultSet rs = lebahDb.getStatement().executeQuery(sql);
			while (rs.next()) {
				if (pyName == "") {
					pyName = "'" + rs.getString("user_id") + "'";
				} else {
					pyName = pyName + ", '" + rs.getString("user_id") + "'";
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (lebahDb != null) { lebahDb.close(); }
		}
		return pyName;
	}
	
	public static String getPenyemakSecondary() {
		Db lebahDb = null;
		String sql = "";
		String pyName = "";
		try {
			lebahDb = new Db();
			sql = "SELECT user_id, role_id FROM user_role WHERE role_id = '(SENGGARA) Penyemak'";
			ResultSet rs = lebahDb.getStatement().executeQuery(sql);
			while (rs.next()) {
				if (pyName == "") {
					pyName = "'" + rs.getString("user_id") + "'";
				} else {
					pyName = pyName + ", '" + rs.getString("user_id") + "'";
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (lebahDb != null) { lebahDb.close(); }
		}
		return pyName;
	}
	
	public static String getPengesyorSecondary() {
		Db lebahDb = null;
		String sql = "";
		String pyName = "";
		try {
			lebahDb = new Db();
			sql = "SELECT user_id, role_id FROM user_role WHERE role_id = '(SENGGARA) Pengesyor'";
			ResultSet rs = lebahDb.getStatement().executeQuery(sql);
			while (rs.next()) {
				if (pyName == "") {
					pyName = "'" + rs.getString("user_id") + "'";
				} else {
					pyName = pyName + ", '" + rs.getString("user_id") + "'";
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (lebahDb != null) { lebahDb.close(); }
		}
		return pyName;
	}	
}
