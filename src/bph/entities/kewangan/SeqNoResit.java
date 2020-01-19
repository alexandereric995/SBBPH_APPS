package bph.entities.kewangan;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lebah.template.UID;
import bph.entities.kod.KodJuruwang;

@Entity
@Table(name = "kew_seq_resit")
public class SeqNoResit {

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "day")
	private Integer day;
	
	@Column(name = "month")
	private Integer month;
	
	@Column(name = "year")
	private Integer year;

	@Column(name = "bil")
	private Integer bil;
	
	@OneToOne
	@JoinColumn(name = "id_kod_juruwang")
	private KodJuruwang kodJuruwang;
	
	public SeqNoResit() {
		setId(UID.getUID());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getBil() {
		return bil;
	}

	public void setBil(Integer bil) {
		this.bil = bil;
	}

	public Integer getDay() {
		return day;
	}

	public void setDay(Integer day) {
		this.day = day;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	public Integer getMonth() {
		return month;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Integer getYear() {
		return year;
	}

	public KodJuruwang getKodJuruwang() {
		return kodJuruwang;
	}

	public void setKodJuruwang(KodJuruwang kodJuruwang) {
		this.kodJuruwang = kodJuruwang;
	}

}
