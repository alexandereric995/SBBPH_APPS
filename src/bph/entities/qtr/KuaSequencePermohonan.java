package bph.entities.qtr;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "kua_sequence_permohonan")
public class KuaSequencePermohonan {

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "bilangan")
	private int bilangan;

	public String getId() {
		return id;
	}

	public int getBilangan() {
		return bilangan;
	}

	public void setBilangan(int bilangan) {
		this.bilangan = bilangan;
	}

	public void setId(String id) {
		this.id = id;
	}
}
