package bph.entities.kod;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lebah.db.Db;
import lebah.template.UID;
import portal.module.entity.Users;
import bph.entities.rpp.RppPeranginan;
import bph.entities.rpp.RppPermohonan;
import bph.entities.rpp.RppUnit;
import db.persistence.MyPersistence;

@Entity
@Table(name = "ruj_jenis_unit_rpp")
public class JenisUnitRPP {

	@Id
	@Column(name = "id")
	private String id;

	@OneToOne
	@JoinColumn(name = "id_peranginan")
	private RppPeranginan peranginan;

	@Column(name = "keterangan")
	private String keterangan;

	@Column(name = "had_dewasa")
	private int hadDewasa;

	@Column(name = "had_kanak_kanak")
	private int hadKanakKanak;

	@Column(name = "had_kuantiti")
	private int hadKuantiti;

	@Column(name = "kadar_sewa")
	private Double kadarSewa;

	@Column(name = "gred_minimum_kelayakan")
	private int gredMinimumKelayakan;

	@Column(name = "kadar_sewa_waktu_puncak")
	private Double kadarSewaWaktuPuncak;

	@Column(name = "gred_kelayakan_waktu_puncak")
	private int gredKelayakanWaktuPuncak;

	@Column(name = "gred_maksimum_kelayakan")
	private int gredMaksimumKelayakan;

	@Column(name = "gred_maksimum_kelayakan_waktu_puncak")
	private int gredMaksimumKelayakanWaktuPuncak;

	// flag

	@Column(name = "flag_tiada_had_kelayakan")
	private String flagTiadaHadKelayakan;

	@Column(name = "flag_tiada_had_kelayakan_waktu_puncak")
	private String flagTiadaHadKelayakanWaktuPuncak;

	@Column(name = "julat_gred_kelayakan")
	private String flagJulatGredKelayakan;

	@Column(name = "julat_gred_kelayakan_waktu_puncak")
	private String flagJulatGredKelayakanWaktuPuncak;

	@ManyToOne
	@JoinColumn(name = "id_masuk")
	private Users idMasuk;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "tarikh_masuk")
	private Date tarikhMasuk;

	@ManyToOne
	@JoinColumn(name = "id_kemaskini")
	private Users idKemaskini;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "tarikh_kemaskini")
	private Date tarikhKemaskini;

	// @OneToMany(cascade=CascadeType.ALL, mappedBy="jenisUnitRpp",
	// fetch=FetchType.EAGER)
	// private List<RppKelompokUnit> listKelompokUnit;

	public JenisUnitRPP() {
		setId(UID.getUID());
		setTarikhMasuk(new Date());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public RppPeranginan getPeranginan() {
		return peranginan;
	}

	public void setPeranginan(RppPeranginan peranginan) {
		this.peranginan = peranginan;
	}

	public String getKeterangan() {
		return keterangan;
	}

	public void setKeterangan(String keterangan) {
		this.keterangan = keterangan;
	}

	public int getHadDewasa() {
		return hadDewasa;
	}

	public void setHadDewasa(int hadDewasa) {
		this.hadDewasa = hadDewasa;
	}

	public int getHadKanakKanak() {
		return hadKanakKanak;
	}

	public void setHadKanakKanak(int hadKanakKanak) {
		this.hadKanakKanak = hadKanakKanak;
	}

	public int getHadKuantiti() {
		return hadKuantiti;
	}

	public void setHadKuantiti(int hadKuantiti) {
		this.hadKuantiti = hadKuantiti;
	}

	public Double getKadarSewa() {
		return kadarSewa;
	}

	public void setKadarSewa(Double kadarSewa) {
		this.kadarSewa = kadarSewa;
	}

	public String getFlagTiadaHadKelayakan() {
		return flagTiadaHadKelayakan;
	}

	public void setFlagTiadaHadKelayakan(String flagTiadaHadKelayakan) {
		this.flagTiadaHadKelayakan = flagTiadaHadKelayakan;
	}

	public int getGredMinimumKelayakan() {
		return gredMinimumKelayakan;
	}

	public void setGredMinimumKelayakan(int gredMinimumKelayakan) {
		this.gredMinimumKelayakan = gredMinimumKelayakan;
	}

	// public List<RppKelompokUnit> getListKelompokUnit() {
	// return listKelompokUnit;
	// }
	//
	// public void setListKelompokUnit(List<RppKelompokUnit> listKelompokUnit) {
	// this.listKelompokUnit = listKelompokUnit;
	// }

	public Double getKadarSewaWaktuPuncak() {
		return kadarSewaWaktuPuncak;
	}

	public void setKadarSewaWaktuPuncak(Double kadarSewaWaktuPuncak) {
		this.kadarSewaWaktuPuncak = kadarSewaWaktuPuncak;
	}

	public int getGredKelayakanWaktuPuncak() {
		return gredKelayakanWaktuPuncak;
	}

	public void setGredKelayakanWaktuPuncak(int gredKelayakanWaktuPuncak) {
		this.gredKelayakanWaktuPuncak = gredKelayakanWaktuPuncak;
	}

	public Integer getTotalBilanganUnit() {
		MyPersistence mp = null;
		int val = 0;
		try {
			mp = new MyPersistence();
			if (this.id != null) {
				val = mp.list(
						"select x from RppUnit x where x.jenisUnit.id = '"
								+ this.id + "' ").size();
			}
		} catch (Exception e) {
			System.out
					.println("Error getTotalBilanganUnit : " + e.getMessage());
		} finally {
			if (mp != null) {
				mp.close();
			}
		}
		return val;
	}

	public void setGredMaksimumKelayakan(int gredMaksimumKelayakan) {
		this.gredMaksimumKelayakan = gredMaksimumKelayakan;
	}

	public int getGredMaksimumKelayakan() {
		return gredMaksimumKelayakan;
	}

	public void setGredMaksimumKelayakanWaktuPuncak(
			int gredMaksimumKelayakanWaktuPuncak) {
		this.gredMaksimumKelayakanWaktuPuncak = gredMaksimumKelayakanWaktuPuncak;
	}

	public int getGredMaksimumKelayakanWaktuPuncak() {
		return gredMaksimumKelayakanWaktuPuncak;
	}

	public String getFlagTiadaHadKelayakanWaktuPuncak() {
		return flagTiadaHadKelayakanWaktuPuncak;
	}

	public void setFlagTiadaHadKelayakanWaktuPuncak(
			String flagTiadaHadKelayakanWaktuPuncak) {
		this.flagTiadaHadKelayakanWaktuPuncak = flagTiadaHadKelayakanWaktuPuncak;
	}

	public String getFlagJulatGredKelayakan() {
		return flagJulatGredKelayakan;
	}

	public void setFlagJulatGredKelayakan(String flagJulatGredKelayakan) {
		this.flagJulatGredKelayakan = flagJulatGredKelayakan;
	}

	public String getFlagJulatGredKelayakanWaktuPuncak() {
		return flagJulatGredKelayakanWaktuPuncak;
	}

	public void setFlagJulatGredKelayakanWaktuPuncak(
			String flagJulatGredKelayakanWaktuPuncak) {
		this.flagJulatGredKelayakanWaktuPuncak = flagJulatGredKelayakanWaktuPuncak;
	}

	public String getKeteranganGredMinimumKelayakan() {
		MyPersistence mp = null;
		String str = Integer.toString(this.gredMinimumKelayakan);
		try {
			mp = new MyPersistence();
			if (this.gredMinimumKelayakan != 0) {
				String id = Integer.toString(this.gredMinimumKelayakan);
				GredPerkhidmatan obj = (GredPerkhidmatan) mp.find(
						GredPerkhidmatan.class, id);
				if (obj != null) {
					str = obj.getKeterangan();
				}
			}
		} catch (Exception e) {
			System.out.println("Error getKeteranganGredMinimumKelayakan : "
					+ e.getMessage());
		} finally {
			if (mp != null) {
				mp.close();
			}
		}
		return str;
	}

	public String getKeteranganGredKelayakanWaktuPuncak() {
		MyPersistence mp = null;
		String str = Integer.toString(this.gredKelayakanWaktuPuncak);
		try {
			mp = new MyPersistence();
			if (this.gredKelayakanWaktuPuncak != 0) {
				String id = Integer.toString(this.gredKelayakanWaktuPuncak);
				GredPerkhidmatan obj = (GredPerkhidmatan) mp.find(
						GredPerkhidmatan.class, id);
				if (obj != null) {
					str = obj.getKeterangan();
				}
			}
		} catch (Exception e) {
			System.out.println("Error getKeteranganGredKelayakanWaktuPuncak : "
					+ e.getMessage());
		} finally {
			if (mp != null) {
				mp.close();
			}
		}
		return str;
	}

	public int getBilAvailabilityKelompok(String idPermohonan) {

		MyPersistence mp = null;
		int bil = 0;
		RppPermohonan r = null;
		String dtIn = "";
		String dtOut = "";

		try {
			mp = new MyPersistence();
			if (idPermohonan != null && !idPermohonan.equalsIgnoreCase("")) {
				r = (RppPermohonan) mp.find(RppPermohonan.class, idPermohonan);
				dtIn = new SimpleDateFormat("yyyy-MM-dd").format(r
						.getTarikhMasukRpp());
				dtOut = new SimpleDateFormat("yyyy-MM-dd").format(r
						.getTarikhKeluarRpp());
			}

		} catch (Exception e) {
			System.out.println("Error getBilAvailabilityKelompok[get r] : "
					+ e.getMessage());
		} finally {
			if (mp != null) {
				mp.close();
			}
		}

		if (r != null) {

			Db db1 = null;
			String sql = "";

			try {

				db1 = new Db();

				sql = "select * from rpp_unit a "
						+ " LEFT JOIN rpp_jadual_tempahan b "
						+ " ON a.id = b.id_unit "
						+ " and ((tarikh_mula <= '"
						+ dtIn
						+ "' AND tarikh_tamat > '"
						+ dtIn
						+ "') "
						+ " OR (tarikh_mula < '"
						+ dtOut
						+ "' AND tarikh_tamat >= '"
						+ dtOut
						+ "') "
						+ " OR (tarikh_mula >= '"
						+ dtIn
						+ "' AND tarikh_tamat < '"
						+ dtOut
						+ "')) "
						+ " where a.id_jenis_unit = '"
						+ this.id
						+ "' "
						+ " and ifnull(b.flag_status_tempahan,'') not in ('CONFIRM') "
						+ " and ifnull(a.status,'') <> 'RESERVED' ";

				/** SELENGGARA */
				sql += " and a.id not in (select c.id_unit from rpp_selenggara_unit_lokasi c, rpp_selenggara d "
						+ " where c.id_selenggara = d.id "
						+ " and d.flag_jenis_selenggara = 'UNIT' "
						+ " and ((d.tarikh_mula <= '"
						+ dtIn
						+ "' AND d.tarikh_tamat > '"
						+ dtIn
						+ "') "
						+ " OR (d.tarikh_mula < '"
						+ dtOut
						+ "' AND d.tarikh_tamat >= '"
						+ dtOut
						+ "') "
						+ " OR (d.tarikh_mula >= '"
						+ dtIn
						+ "' AND d.tarikh_tamat < '"
						+ dtOut
						+ "'))) "
						+ " and b.id is null ";

				ResultSet rs = db1.getStatement().executeQuery(sql);
				int count = 0;
				while (rs.next()) {
					count = count + 1;
				}

				bil = count;

			} catch (Exception e) {
				System.out.println("error getBilAvailabilityKelompok : "
						+ e.getMessage());
				e.printStackTrace();
			} finally {
				if (db1 != null)
					db1.close();
			}
		}

		return bil;
	}

	public Integer getKelompokValue(String idPermohonan) {
		Db db1 = null;
		int val = 0;
		if (idPermohonan != null && this.id != null) {
			try {
				db1 = new Db();
				String sql = "SELECT x.bil_unit FROM rpp_kelompok_unit x WHERE x.id_permohonan = '"
						+ idPermohonan
						+ "' AND x.id_jenis_unit_rpp = '"
						+ this.id + "' ";
				ResultSet rs = db1.getStatement().executeQuery(sql);
				if (rs.next()) {
					val = rs.getInt("bil_unit");
				}
			} catch (Exception e) {
				System.out.println("Error getKelompokValue() : "
						+ e.getMessage());
			} finally {
				if (db1 != null)
					db1.close();
			}
		}
		return val;
	}

	public Integer getKelompokLulusValue(String idPermohonan) {
		Db db1 = null;
		int val = 0;
		if (idPermohonan != null && this.id != null) {
			try {
				db1 = new Db();
				String sql = "SELECT x.bil_unit_lulus FROM rpp_kelompok_unit x WHERE x.id_permohonan = '"
						+ idPermohonan
						+ "' AND x.id_jenis_unit_rpp = '"
						+ this.id + "' ";
				ResultSet rs = db1.getStatement().executeQuery(sql);
				if (rs.next()) {
					val = rs.getInt("bil_unit_lulus");
				}
			} catch (Exception e) {
				System.out.println("Error getKelompokLulusValue() : "
						+ e.getMessage());
			} finally {
				if (db1 != null)
					db1.close();
			}
		}
		return val;
	}

	@SuppressWarnings("unchecked")
	public List<RppUnit> listRppUnitSale() {
		MyPersistence mp = null;
		List<RppUnit> list = null;
		try {
			mp = new MyPersistence();
			list = mp.list("select x from RppUnit x where x.jenisUnit.id = '"
					+ this.id + "' and COALESCE(x.status,'') <> 'RESERVED' ");
		} catch (Exception e) {
			System.out.println("Error listRppUnitSale : " + e.getMessage());
		} finally {
			if (mp != null) {
				mp.close();
			}
		}
		return list;
	}

	public Users getIdMasuk() {
		return idMasuk;
	}

	public void setIdMasuk(Users idMasuk) {
		this.idMasuk = idMasuk;
	}

	public Date getTarikhMasuk() {
		return tarikhMasuk;
	}

	public void setTarikhMasuk(Date tarikhMasuk) {
		this.tarikhMasuk = tarikhMasuk;
	}

	public Users getIdKemaskini() {
		return idKemaskini;
	}

	public void setIdKemaskini(Users idKemaskini) {
		this.idKemaskini = idKemaskini;
	}

	public Date getTarikhKemaskini() {
		return tarikhKemaskini;
	}

	public void setTarikhKemaskini(Date tarikhKemaskini) {
		this.tarikhKemaskini = tarikhKemaskini;
	}
}
