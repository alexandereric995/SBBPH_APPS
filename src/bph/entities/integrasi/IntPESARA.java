package bph.entities.integrasi;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lebah.template.UID;


@Entity
@Table(name = "int_pesara")
public class IntPESARA {
	
	@Id
	@Column(name = "id")
	private String id;	
	
	@Column(name = "tarikh_hantar")
	@Temporal(TemporalType.TIMESTAMP)
	private Date tarikhHantar;
	
	@Column(name = "no_pengenalan")
	private String noPengenalan;
	
	@Column(name = "tarikh_terima")
	@Temporal(TemporalType.TIMESTAMP)
	private Date tarikhTerima;
	
	@Column(name = "no_pengenalan_lama")
	private String noPengenalanLama;
	
	@Column(name = "no_akaun_pencen")
	private String noAkaunPencen;
	
	@Column(name = "tarikh_pencen")
	@Temporal(TemporalType.DATE)
	private Date tarikhPencen;	
	
	@Column(name = "id_jenis_persaraan")
	private String idJenisPersaraan;
	
	@Column(name = "jenis_persaraan")
	private String jenisPersaraan;	
	
	@Column(name = "nama")
	private String nama;	
	
	@Column(name = "jawatan_terakhir")
	private String jawatanTerakhir;
	
	@Column(name = "kod_jawatan")
	private String kodJawatan;
	
	@Column(name = "kod_gaji_terakhir")
	private String kodGajiTerakhir;
	
	@Column(name = "id_kelas_perkhidmatan")
	private String kelasPerkhidmatan;
	
	@Column(name = "id_gred_perkhidmatan")
	private String gredPerkhidmatan;
	
	@Column(name = "kod_cawangan")
	private String kodCawangan;
	
	@Column(name = "cawangan")
	private String cawangan;
	
	@Column(name = "alamat1")
	private String alamat1;
	
	@Column(name = "alamat2")
	private String alamat2;
	
	@Column(name = "alamat3")
	private String alamat3;
	
	@Column(name = "poskod")
	private String poskod;	
	
	@Column(name = "kod_bandar")
	private String kodBandar;
	
	@Column(name = "bandar")
	private String bandar;
	
	@Column(name = "kod_negeri")
	private String kodNegeri;
	
	@Column(name = "negeri")
	private String negeri;
	
	@Column(name = "status_hidup")
	private String statusHidup;

	public IntPESARA() {
		setId(UID.getUID());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getTarikhHantar() {
		return tarikhHantar;
	}

	public void setTarikhHantar(Date tarikhHantar) {
		this.tarikhHantar = tarikhHantar;
	}

	public String getNoPengenalan() {
		return noPengenalan;
	}

	public void setNoPengenalan(String noPengenalan) {
		this.noPengenalan = noPengenalan;
	}

	public Date getTarikhTerima() {
		return tarikhTerima;
	}

	public void setTarikhTerima(Date tarikhTerima) {
		this.tarikhTerima = tarikhTerima;
	}

	public String getNoPengenalanLama() {
		return noPengenalanLama;
	}

	public void setNoPengenalanLama(String noPengenalanLama) {
		this.noPengenalanLama = noPengenalanLama;
	}

	public String getNoAkaunPencen() {
		return noAkaunPencen;
	}

	public void setNoAkaunPencen(String noAkaunPencen) {
		this.noAkaunPencen = noAkaunPencen;
	}

	public Date getTarikhPencen() {
		return tarikhPencen;
	}

	public void setTarikhPencen(Date tarikhPencen) {
		this.tarikhPencen = tarikhPencen;
	}

	public String getIdJenisPersaraan() {
		return idJenisPersaraan;
	}

	public void setIdJenisPersaraan(String idJenisPersaraan) {
		this.idJenisPersaraan = idJenisPersaraan;
	}

	public String getJenisPersaraan() {
		return jenisPersaraan;
	}

	public void setJenisPersaraan(String jenisPersaraan) {
		this.jenisPersaraan = jenisPersaraan;
	}

	public String getNama() {
		return nama;
	}

	public void setNama(String nama) {
		this.nama = nama;
	}

	public String getJawatanTerakhir() {
		return jawatanTerakhir;
	}

	public void setJawatanTerakhir(String jawatanTerakhir) {
		this.jawatanTerakhir = jawatanTerakhir;
	}

	public String getKodJawatan() {
		return kodJawatan;
	}

	public void setKodJawatan(String kodJawatan) {
		this.kodJawatan = kodJawatan;
	}

	public String getKodGajiTerakhir() {
		return kodGajiTerakhir;
	}

	public void setKodGajiTerakhir(String kodGajiTerakhir) {
		this.kodGajiTerakhir = kodGajiTerakhir;
	}

	public String getKelasPerkhidmatan() {
		return kelasPerkhidmatan;
	}

	public void setKelasPerkhidmatan(String kelasPerkhidmatan) {
		this.kelasPerkhidmatan = kelasPerkhidmatan;
	}

	public String getGredPerkhidmatan() {
		return gredPerkhidmatan;
	}

	public void setGredPerkhidmatan(String gredPerkhidmatan) {
		this.gredPerkhidmatan = gredPerkhidmatan;
	}

	public String getKodCawangan() {
		return kodCawangan;
	}

	public void setKodCawangan(String kodCawangan) {
		this.kodCawangan = kodCawangan;
	}

	public String getCawangan() {
		return cawangan;
	}

	public void setCawangan(String cawangan) {
		this.cawangan = cawangan;
	}

	public String getAlamat1() {
		return alamat1;
	}

	public void setAlamat1(String alamat1) {
		this.alamat1 = alamat1;
	}

	public String getAlamat2() {
		return alamat2;
	}

	public void setAlamat2(String alamat2) {
		this.alamat2 = alamat2;
	}

	public String getAlamat3() {
		return alamat3;
	}

	public void setAlamat3(String alamat3) {
		this.alamat3 = alamat3;
	}

	public String getPoskod() {
		return poskod;
	}

	public void setPoskod(String poskod) {
		this.poskod = poskod;
	}

	public String getKodBandar() {
		return kodBandar;
	}

	public void setKodBandar(String kodBandar) {
		this.kodBandar = kodBandar;
	}

	public String getBandar() {
		return bandar;
	}

	public void setBandar(String bandar) {
		this.bandar = bandar;
	}

	public String getKodNegeri() {
		return kodNegeri;
	}

	public void setKodNegeri(String kodNegeri) {
		this.kodNegeri = kodNegeri;
	}

	public String getNegeri() {
		return negeri;
	}

	public void setNegeri(String negeri) {
		this.negeri = negeri;
	}

	public String getStatusHidup() {
		return statusHidup;
	}

	public void setStatusHidup(String statusHidup) {
		this.statusHidup = statusHidup;
	}
}
