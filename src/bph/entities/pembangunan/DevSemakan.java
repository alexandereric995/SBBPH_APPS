package bph.entities.pembangunan;

import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lebah.template.UID;
import portal.module.entity.Users;

@Entity
@Table(name = "dev_semakan")
public class DevSemakan {

	@Id
	@Column(name = "id")
	private String id;	
	
	@ManyToOne
	@JoinColumn(name = "id_hakmilik")
	private DevHakmilik hakmilik;

	@Column(name = "perkara")
	private String perkara;
	
	@Column(name = "keterangan")
	private String keterangan;

	@ManyToOne
	@JoinColumn(name = "id_penyedia")
	private Users penyedia;

	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_penyediaan")
	private Date tarikhPenyediaan;
	
	@Column(name = "status")
	private String status;

	@ManyToOne
	@JoinColumn(name = "id_masuk")
	private Users daftarOleh;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "tarikh_masuk")
	private Date tarikhMasuk;
	
	@ManyToOne
	@JoinColumn(name = "id_kemaskini")
	private Users kemaskiniOleh;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "tarikh_kemaskini")
	private Date tarikhKemaskini;
	
	@OneToMany(cascade=CascadeType.ALL, mappedBy="semakan", fetch=FetchType.EAGER)
	private List<DevLogSemakan> listLogSemakan;
	
	public DevSemakan() {
		setId(UID.getUID());
		setStatus("B");
		setTarikhMasuk(new Date());
	}
	
	public String getKeteranganStatus(){
		
		String status = this.status;
		String keterangan = "";
		
		if("B".equals(status)){
			keterangan = "DALAM SEMAKAN";
		} else if("P".equals(status)){
			keterangan = "PINDAAN";
		} else if("S".equals(status)){
			keterangan = "SELESAI";
		}
		
		return keterangan;
	}
	
	public DevLogSemakan getLogSemakanSemasa(){
		DevLogSemakan logSemakan = null;
		List<DevLogSemakan> listLogSemakan = this.listLogSemakan;
		if (listLogSemakan != null) {
			for (DevLogSemakan log : listLogSemakan) {
				if (log.getFlagAktif().equals("Y")) {
					logSemakan = log;
				}			
			}
		}				
		return logSemakan;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public DevHakmilik getHakmilik() {
		return hakmilik;
	}

	public void setHakmilik(DevHakmilik hakmilik) {
		this.hakmilik = hakmilik;
	}

	public String getPerkara() {
		return perkara;
	}

	public void setPerkara(String perkara) {
		this.perkara = perkara;
	}

	public String getKeterangan() {
		return keterangan;
	}

	public void setKeterangan(String keterangan) {
		this.keterangan = keterangan;
	}

	public Users getPenyedia() {
		return penyedia;
	}

	public void setPenyedia(Users penyedia) {
		this.penyedia = penyedia;
	}

	public Date getTarikhPenyediaan() {
		return tarikhPenyediaan;
	}

	public void setTarikhPenyediaan(Date tarikhPenyediaan) {
		this.tarikhPenyediaan = tarikhPenyediaan;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Users getDaftarOleh() {
		return daftarOleh;
	}

	public void setDaftarOleh(Users daftarOleh) {
		this.daftarOleh = daftarOleh;
	}

	public Date getTarikhMasuk() {
		return tarikhMasuk;
	}

	public void setTarikhMasuk(Date tarikhMasuk) {
		this.tarikhMasuk = tarikhMasuk;
	}

	public Users getKemaskiniOleh() {
		return kemaskiniOleh;
	}

	public void setKemaskiniOleh(Users kemaskiniOleh) {
		this.kemaskiniOleh = kemaskiniOleh;
	}

	public Date getTarikhKemaskini() {
		return tarikhKemaskini;
	}

	public void setTarikhKemaskini(Date tarikhKemaskini) {
		this.tarikhKemaskini = tarikhKemaskini;
	}

	public List<DevLogSemakan> getListLogSemakan() {
		return listLogSemakan;
	}

	public void setListLogSemakan(List<DevLogSemakan> listLogSemakan) {
		this.listLogSemakan = listLogSemakan;
	}
}
