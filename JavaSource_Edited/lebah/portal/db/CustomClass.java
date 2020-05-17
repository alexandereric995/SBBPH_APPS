package lebah.portal.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import lebah.db.Db;
import lebah.db.DbException;
import lebah.db.SQLRenderer;

public class CustomClass {
	public static String getName(String module) throws DbException {
		Db db = null;
		Connection conn = null;
		String sql = "";
		try {
			db = new Db();
			Statement stmt = db.getStatement();
			SQLRenderer r = new SQLRenderer();
			r.add("module_class");
			r.add("module_id", module);
			sql = r.getSQLSelect("module");
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next()) {
				String name = rs.getString("module_class");
				return name;
			}
			return null;
		} catch (SQLException ex) {
			throw new DbException(String.valueOf(ex.getMessage()) + ": " + sql);
		} finally {
			if (db != null)
				db.close();
		}
	}

	public static String getName(String module, String role) throws DbException {
		Db db = null;
		Connection conn = null;
		String sql = "";
		try {
			db = new Db();
			Statement stmt = db.getStatement();
			SQLRenderer r = new SQLRenderer();
			r.add("m.module_class");
			r.add("m.module_id", module);
			r.add("r.user_role", role);
			r.relate("m.module_id", "r.module_id");
			sql = r.getSQLSelect("module m, role_module r");
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next()) {
				String name = rs.getString("module_class");
				return name;
			}
			return null;
		} catch (SQLException ex) {
			throw new DbException(String.valueOf(ex.getMessage()) + ": " + sql);
		} finally {
			if (db != null)
				db.close();
		}
	}

	public static String getCustomTitle(String moduleId) throws DbException {
		Db db = null;
		Connection conn = null;
		String sql = "";
		try {
			db = new Db();
			Statement stmt = db.getStatement();
			SQLRenderer r = new SQLRenderer();
			r.add("module_title");
			r.add("module_id", moduleId);
			sql = r.getSQLSelect("module");
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next()) {
				String name = rs.getString("module_title");
				return name;
			}
			return null;
		} catch (SQLException ex) {
			throw new DbException(String.valueOf(ex.getMessage()) + ": " + sql);
		} finally {
			if (db != null)
				db.close();
		}
	}
}
