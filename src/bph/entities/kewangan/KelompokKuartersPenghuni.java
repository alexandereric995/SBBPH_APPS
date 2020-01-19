package bph.entities.kewangan;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lebah.template.UID;
import bph.entities.qtr.KuaPenghuni;

@Entity
@Table(name = "kelompok_kuarters_penghuni")
public class KelompokKuartersPenghuni {

	@Id
	@Column(name = "id")
	private String id;
	
	@ManyToOne
	@JoinColumn(name = "id_kelompok_kuarters")
	private KelompokKuarters kelompokKuarters;
	
	@ManyToOne
	@JoinColumn(name = "id_kua_penghuni")
	private KuaPenghuni kuaPenghuni;
	
	
	public KelompokKuartersPenghuni() {
		setId(UID.getUID());
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public KelompokKuarters getKelompokKuarters() {
		return kelompokKuarters;
	}

	public void setKelompokKuarters(KelompokKuarters kelompokKuarters) {
		this.kelompokKuarters = kelompokKuarters;
	}

	public KuaPenghuni getKuaPenghuni() {
		return kuaPenghuni;
	}

	public void setKuaPenghuni(KuaPenghuni kuaPenghuni) {
		this.kuaPenghuni = kuaPenghuni;
	}
	
}
