package bph.entities.rk;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lebah.template.UID;

@Entity
@Table(name = "rk_fail")
public class RkFail {

	@Id
	@Column(name = "id")
	private String id;
	
	@ManyToOne
	@JoinColumn(name = "id_ruang")
	private RkRuangKomersil ruang;

	@ManyToOne
	@JoinColumn(name = "id_pemohon")
	private RkPemohon pemohon;

	@Column(name = "no_fail")
	private String noFail;
	
	@Column(name = "flag_aktif_perjanjian")
	private String flagAktifPerjanjian;
	
	@Column(name = "flag_tunggakan")
	private String flagTunggakan;
	
	@Column(name = "nilai_tunggakan")
	private double nilaiTunggakan;
	
	@Column(name = "abt")
	private int abt;
	
	@Column(name = "flag_tunggakan_iwk")
	private String flagTunggakanIWK;
	
	@Column(name = "nilai_tunggakan_iwk")
	private double nilaiTunggakanIWK;
	
	@Column(name = "abt_iwk")
	private int abtIWK;
	
	@OneToMany(cascade=CascadeType.ALL, mappedBy="fail", fetch=FetchType.EAGER)
	private List<RkPerjanjian> listPerjanjian;

	public RkFail() {
		setId(UID.getUID());
		setFlagAktifPerjanjian("T");
		setFlagTunggakan("T");
		setFlagTunggakanIWK("T");
	}
	
	public RkPerjanjian getPerjanjianSemasa(){
		RkPerjanjian perjanjian = null;
		List<RkPerjanjian> listPerjanjian = this.listPerjanjian;
		if (listPerjanjian != null) {
			for (RkPerjanjian contract : listPerjanjian) {
				if (contract.getFlagPerjanjianSemasa().equals("Y")) {
					perjanjian = contract;
				}			
			}
		}				
		return perjanjian;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public RkRuangKomersil getRuang() {
		return ruang;
	}

	public void setRuang(RkRuangKomersil ruang) {
		this.ruang = ruang;
	}

	public RkPemohon getPemohon() {
		return pemohon;
	}

	public void setPemohon(RkPemohon pemohon) {
		this.pemohon = pemohon;
	}

	public String getNoFail() {
		return noFail;
	}

	public void setNoFail(String noFail) {
		this.noFail = noFail;
	}

	public String getFlagAktifPerjanjian() {
		return flagAktifPerjanjian;
	}

	public void setFlagAktifPerjanjian(String flagAktifPerjanjian) {
		this.flagAktifPerjanjian = flagAktifPerjanjian;
	}

	public String getFlagTunggakan() {
		return flagTunggakan;
	}

	public void setFlagTunggakan(String flagTunggakan) {
		this.flagTunggakan = flagTunggakan;
	}

	public double getNilaiTunggakan() {
		return nilaiTunggakan;
	}

	public void setNilaiTunggakan(double nilaiTunggakan) {
		this.nilaiTunggakan = nilaiTunggakan;
	}

	public int getAbt() {
		return abt;
	}

	public void setAbt(int abt) {
		this.abt = abt;
	}

	public String getFlagTunggakanIWK() {
		return flagTunggakanIWK;
	}

	public void setFlagTunggakanIWK(String flagTunggakanIWK) {
		this.flagTunggakanIWK = flagTunggakanIWK;
	}

	public double getNilaiTunggakanIWK() {
		return nilaiTunggakanIWK;
	}

	public void setNilaiTunggakanIWK(double nilaiTunggakanIWK) {
		this.nilaiTunggakanIWK = nilaiTunggakanIWK;
	}

	public int getAbtIWK() {
		return abtIWK;
	}

	public void setAbtIWK(int abtIWK) {
		this.abtIWK = abtIWK;
	}

	public List<RkPerjanjian> getListPerjanjian() {
		return listPerjanjian;
	}

	public void setListPerjanjian(List<RkPerjanjian> listPerjanjian) {
		this.listPerjanjian = listPerjanjian;
	}
}