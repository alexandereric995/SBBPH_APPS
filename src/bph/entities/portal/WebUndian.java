package bph.entities.portal;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
@Table(name = "web_undian")
public class WebUndian {
	
	@Id
	@Column(name = "id")
	private String id;	
	
	@Column(name = "bulan")
	private int bulan;	
	
	@Column(name = "tahun")
	private int tahun;
	
	@Column(name = "tidak_pasti")
	private int tidakPasti;
	
	@Column(name = "tidak_puas")
	private int tidakPuas;
	
	@Column(name = "kurang_puas")
	private int kurangPuas;
	
	@Column(name = "puas")
	private int puas;
	
	@Column(name = "sangat_puas")
	private int sangatPuas;

	@Temporal(TemporalType.TIMESTAMP) 
	@Column(name="tarikh_kemaskini")
	private Date tarikhKemaskini;
	
	public int getJumlahUndian() {
		int jumlah = 0;
		jumlah = this.tidakPasti + this.tidakPuas + this.kurangPuas + this.puas + this.sangatPuas;
		return jumlah;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getBulan() {
		return bulan;
	}

	public void setBulan(int bulan) {
		this.bulan = bulan;
	}

	public int getTahun() {
		return tahun;
	}

	public void setTahun(int tahun) {
		this.tahun = tahun;
	}

	public int getTidakPasti() {
		return tidakPasti;
	}

	public void setTidakPasti(int tidakPasti) {
		this.tidakPasti = tidakPasti;
	}

	public int getTidakPuas() {
		return tidakPuas;
	}

	public void setTidakPuas(int tidakPuas) {
		this.tidakPuas = tidakPuas;
	}

	public int getKurangPuas() {
		return kurangPuas;
	}

	public void setKurangPuas(int kurangPuas) {
		this.kurangPuas = kurangPuas;
	}

	public int getPuas() {
		return puas;
	}

	public void setPuas(int puas) {
		this.puas = puas;
	}

	public int getSangatPuas() {
		return sangatPuas;
	}

	public void setSangatPuas(int sangatPuas) {
		this.sangatPuas = sangatPuas;
	}

	public Date getTarikhKemaskini() {
		return tarikhKemaskini;
	}

	public void setTarikhKemaskini(Date tarikhKemaskini) {
		this.tarikhKemaskini = tarikhKemaskini;
	}
	}