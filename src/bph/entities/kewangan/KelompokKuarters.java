package bph.entities.kewangan;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lebah.template.DbPersistence;
import lebah.template.UID;

@Entity
@Table(name = "kelompok_kuarters")
public class KelompokKuarters {

	@Id
	@Column(name = "id")
	private String id;
	
	@Column(name = "nama_agensi")
	private String namaAgensi;
	
	@Column(name = "keterangan")
	private String keterangan;
	
	
	
	public KelompokKuarters() {
		setId(UID.getUID());
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNamaAgensi() {
		return namaAgensi;
	}

	public void setNamaAgensi(String namaAgensi) {
		this.namaAgensi = namaAgensi;
	}

	public String getKeterangan() {
		return keterangan;
	}

	public void setKeterangan(String keterangan) {
		this.keterangan = keterangan;
	}

	@SuppressWarnings("unchecked")
	public List<KelompokKuartersPenghuni> getSenaraiKuartersPenghuni() {
		DbPersistence db = new DbPersistence();
		List<KelompokKuartersPenghuni> list = null;
		if(this.id!=null){
			list = db.list("select x from KelompokKuartersPenghuni x where x.kelompokKuarters.id = '"+this.id+"' ");
		}
		return list;
	}
	
}
