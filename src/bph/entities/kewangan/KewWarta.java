package bph.entities.kewangan;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lebah.template.DbPersistence;
import lebah.template.UID;

/**
 * @author muhdsyazreen
 */

@Entity
@Table(name = "kew_warta")
public class KewWarta {

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "catatan_hantar_warta")
	private String catatanHantarWarta;
	
	@Column(name = "no_warta")
	private String noWarta;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_hantar_warta")
	private Date tarikhHantarWarta;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_warta")
	private Date tarikhWarta;

	
	public KewWarta() {
		setId(UID.getUID());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCatatanHantarWarta() {
		return catatanHantarWarta;
	}

	public void setCatatanHantarWarta(String catatanHantarWarta) {
		this.catatanHantarWarta = catatanHantarWarta;
	}

	public String getNoWarta() {
		return noWarta;
	}

	public void setNoWarta(String noWarta) {
		this.noWarta = noWarta;
	}

	public Date getTarikhHantarWarta() {
		return tarikhHantarWarta;
	}

	public void setTarikhHantarWarta(Date tarikhHantarWarta) {
		this.tarikhHantarWarta = tarikhHantarWarta;
	}

	public Date getTarikhWarta() {
		return tarikhWarta;
	}

	public void setTarikhWarta(Date tarikhWarta) {
		this.tarikhWarta = tarikhWarta;
	}

	@SuppressWarnings("unchecked")
	public List<KewDepositWarta> getSenaraiDepositWarta() {
		DbPersistence db = new DbPersistence();
		List<KewDepositWarta> list = null;
		if(this.id!=null){
			list = db.list("select x from KewDepositWarta x where x.warta.id = '"+this.id+"' ");
		}
		return list;
	}

}
