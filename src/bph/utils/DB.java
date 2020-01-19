package bph.utils;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Vector;

import lebah.db.Db;
import lebah.template.DbPersistence;

import org.apache.log4j.Logger;

import portal.module.entity.Users;
import bph.entities.kod.Jawatan;
import bph.entities.kod.JenisBayaran;
import bph.entities.kod.KodHasil;
import bph.entities.kod.KodJenisPerolehan;

public class DB implements Serializable {
	
	private static final long serialVersionUID = 1L;
	static Logger myLogger = Logger.getLogger(DB.class);
	protected static DbPersistence dbPersist;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Vector getRujKodHasil() throws Exception {
		Db db = null;
		String sql = "Select id,kod,keterangan from ruj_kod_hasil";
		try {
			db = new Db();
			Statement stmt = db.getStatement();
			ResultSet rs = stmt.executeQuery(sql);
			Vector v = new Vector();
			KodHasil u = null;
			while (rs.next()) {
				u = new KodHasil();
				u.setId(rs.getString("id"));
				u.setKod(rs.getString("kod"));
				u.setKeterangan(rs.getString("keterangan"));
				v.addElement(u);
			}
			return v;
		} finally {
			if (db != null)
				db.close();
		}
	}
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Vector getRujJenisBayaran() throws Exception {
		Db db = null;
		String sql = "Select id,keterangan from ruj_jenis_bayaran";
		try {
			db = new Db();
			Statement stmt = db.getStatement();
			ResultSet rs = stmt.executeQuery(sql);
			Vector v = new Vector();
			JenisBayaran u = null;
			while (rs.next()) {
				u = new JenisBayaran();
				u.setId(rs.getString("id"));
				//u.setKod(rs.getString("kod"));
				u.setKeterangan(rs.getString("keterangan"));
				v.addElement(u);
			}
			return v;
		} finally {
			if (db != null)
				db.close();
		}
	}
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Vector getRujJenisPerolehan() throws Exception {
		Db db = null;
		String sql = "Select id,kod,keterangan from ruj_jenis_perolehan";
		try {
			db = new Db();
			Statement stmt = db.getStatement();
			ResultSet rs = stmt.executeQuery(sql);
			Vector v = new Vector();
			KodJenisPerolehan u = null;
			while (rs.next()) {
				u = new KodJenisPerolehan();
				u.setId(rs.getString("id"));
				u.setKod(rs.getString("kod"));
				u.setKeterangan(rs.getString("keterangan"));
				v.addElement(u);
			}
			return v;
		} finally {
			if (db != null)
				db.close();
		}
	}
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Vector getPegawaiRp() throws Exception {
		dbPersist = new DbPersistence();
		Db db = null;
		String sql = "Select user_login, user_name, emel, id_jawatan from users ";
		try {
			db = new Db();
			Statement stmt = db.getStatement();
			ResultSet rs = stmt.executeQuery(sql);
			Vector v = new Vector();
			Users u = null;
			while (rs.next()) {
				u = new Users();
				u.setId(rs.getString("user_login"));
				u.setUserName(rs.getString("user_name"));
				u.setEmel(rs.getString("emel"));
				u.setJawatan(dbPersist.find(Jawatan.class, (rs.getString("id_jawatan")!=null?rs.getString("id_jawatan"):"")));
				v.addElement(u);
			}
			return v;
		} finally {
			if (db != null)
				db.close();
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Vector getPegawaiSemakRp() throws Exception {
		dbPersist = new DbPersistence();
		Db db = null;
		String sql = "Select user_login, user_name, emel, id_jawatan from users ";
		try {
			db = new Db();
			Statement stmt = db.getStatement();
			ResultSet rs = stmt.executeQuery(sql);
			Vector v = new Vector();
			Users u = null;
			while (rs.next()) {
				u = new Users();
				u.setId(rs.getString("user_login"));
				u.setUserName(rs.getString("user_name"));
				u.setEmel(rs.getString("emel")!=null?rs.getString("emel"):"");
				u.setJawatan(dbPersist.find(Jawatan.class, (rs.getString("id_jawatan")!=null?rs.getString("id_jawatan"):"")));
				v.addElement(u);
			}
			return v;
		} finally {
			if (db != null)
				db.close();
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Vector getPegawaiLulusRp() throws Exception {
		dbPersist = new DbPersistence();
		Db db = null;
		String sql = "Select user_login, user_name, emel, id_jawatan from users ";
		try {
			db = new Db();
			Statement stmt = db.getStatement();
			ResultSet rs = stmt.executeQuery(sql);
			Vector v = new Vector();
			Users u = null;
			while (rs.next()) {
				u = new Users();
				u.setId(rs.getString("user_login"));
				u.setUserName(rs.getString("user_name"));
				u.setEmel(rs.getString("emel")!=null?rs.getString("emel"):"");
				u.setJawatan(dbPersist.find(Jawatan.class, (rs.getString("id_jawatan")!=null?rs.getString("id_jawatan"):"")));
				v.addElement(u);
			}
			return v;
		} finally {
			if (db != null)
				db.close();
		}
	}
	
}//close class