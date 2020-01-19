package bph.entities.kew;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lebah.template.DbPersistence;
import lebah.template.UID;
import portal.module.entity.Users;
import bph.entities.kewangan.KewDeposit;
import bph.entities.kewangan.KewDepositAgihan;
import bph.entities.kod.Status;
import bph.entities.rpp.RppAkaun;
import bph.entities.rpp.RppPermohonan;

@Entity
@Table(name = "kew_tuntutan_deposit")
public class KewTuntutanDeposit {
	
	@Id
	@Column(name = "id")
	private String id;

	@OneToOne
	@JoinColumn(name = "id_kew_deposit")
	private KewDeposit deposit;
	
	@ManyToOne
	@JoinColumn(name = "id_jenis_tuntutan")
	private KewJenisBayaran jenisTuntutan; /** 01=kua / 02=ir / dll */
	
	@ManyToOne
	@JoinColumn(name = "id_penuntut")
	private Users penuntut;
	
	@ManyToOne
	@JoinColumn(name = "id_status")
	private Status status;
	
	@OneToOne
	@JoinColumn(name = "id_agihan")
	private KewDepositAgihan agihan;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "tarikh_permohonan")
	private Date tarikhPermohonan;
	
	/**Senarai semak*/
	@Column(name = "resit_bayaran_deposit")
	private String resitBayaranDeposit;

	@Column(name = "sijil_akaun_masuk")
	private String sijilAkaunMasuk;
	
	@Column(name = "sijil_akaun_keluar")
	private String sijilAkaunKeluar;
	
	@Column(name = "salinan_akaun_bank")
	private String salinanAkaunBank;
	
	@Column(name = "surat_pengesahan_deposit")
	private String suratPengesahanDeposit;
	
	/**Senarai semak*/
	
	@Column(name = "no_baucer_pulangan_deposit")
	private String noBaucerPulanganDeposit;
	
	@Column(name = "amaun_pelarasan_deposit")
	private Double amaunPelarasanDeposit;
	
	@Column(name = "catatan_pelarasan_deposit")
	private String catatanPelarasanDeposit;
	
	@Column(name = "no_eft")
	private String noEft;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "tarikh_eft")
	private Date tarikhEft;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "tarikh_baucer")
	private Date tarikhBaucer;
	
	@Column(name = "catatan_penyelia_rpp")
	private String catatanPenyeliaRpp;
	
	@ManyToOne
	@JoinColumn(name = "id_daftar")
	private Users idDaftar;
	
	@ManyToOne
	@JoinColumn(name = "id_kemaskini")
	private Users idKemaskini;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "tarikh_daftar")
	private Date tarikhDaftar;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "tarikh_kemaskini")
	private Date tarikhKemaskini;
	
	
	
	public KewTuntutanDeposit(){
		setId(UID.getUID());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public KewDeposit getDeposit() {
		return deposit;
	}

	public void setDeposit(KewDeposit deposit) {
		this.deposit = deposit;
	}

	public KewJenisBayaran getJenisTuntutan() {
		return jenisTuntutan;
	}

	public void setJenisTuntutan(KewJenisBayaran jenisTuntutan) {
		this.jenisTuntutan = jenisTuntutan;
	}

	public Users getPenuntut() {
		return penuntut;
	}

	public void setPenuntut(Users penuntut) {
		this.penuntut = penuntut;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Date getTarikhPermohonan() {
		return tarikhPermohonan;
	}

	public void setTarikhPermohonan(Date tarikhPermohonan) {
		this.tarikhPermohonan = tarikhPermohonan;
	}

	public String getResitBayaranDeposit() {
		return resitBayaranDeposit;
	}

	public void setResitBayaranDeposit(String resitBayaranDeposit) {
		this.resitBayaranDeposit = resitBayaranDeposit;
	}

	public String getSijilAkaunMasuk() {
		return sijilAkaunMasuk;
	}

	public void setSijilAkaunMasuk(String sijilAkaunMasuk) {
		this.sijilAkaunMasuk = sijilAkaunMasuk;
	}

	public String getSijilAkaunKeluar() {
		return sijilAkaunKeluar;
	}

	public void setSijilAkaunKeluar(String sijilAkaunKeluar) {
		this.sijilAkaunKeluar = sijilAkaunKeluar;
	}

	public String getSalinanAkaunBank() {
		return salinanAkaunBank;
	}

	public void setSalinanAkaunBank(String salinanAkaunBank) {
		this.salinanAkaunBank = salinanAkaunBank;
	}

	public KewDepositAgihan getAgihan() {
		return agihan;
	}

	public void setAgihan(KewDepositAgihan agihan) {
		this.agihan = agihan;
	}

	public String getNoBaucerPulanganDeposit() {
		return noBaucerPulanganDeposit;
	}

	public void setNoBaucerPulanganDeposit(String noBaucerPulanganDeposit) {
		this.noBaucerPulanganDeposit = noBaucerPulanganDeposit;
	}

	public Double getAmaunPelarasanDeposit() {
		return amaunPelarasanDeposit;
	}

	public void setAmaunPelarasanDeposit(Double amaunPelarasanDeposit) {
		this.amaunPelarasanDeposit = amaunPelarasanDeposit;
	}

	public String getCatatanPelarasanDeposit() {
		return catatanPelarasanDeposit;
	}

	public void setCatatanPelarasanDeposit(String catatanPelarasanDeposit) {
		this.catatanPelarasanDeposit = catatanPelarasanDeposit;
	}

	public String getSuratPengesahanDeposit() {
		return suratPengesahanDeposit;
	}

	public void setSuratPengesahanDeposit(String suratPengesahanDeposit) {
		this.suratPengesahanDeposit = suratPengesahanDeposit;
	}

	public String getNoEft() {
		return noEft;
	}

	public void setNoEft(String noEft) {
		this.noEft = noEft;
	}

	public Date getTarikhEft() {
		return tarikhEft;
	}

	public void setTarikhEft(Date tarikhEft) {
		this.tarikhEft = tarikhEft;
	}

	public Date getTarikhBaucer() {
		return tarikhBaucer;
	}

	public void setTarikhBaucer(Date tarikhBaucer) {
		this.tarikhBaucer = tarikhBaucer;
	}

	public String getCatatanPenyeliaRpp() {
		return catatanPenyeliaRpp;
	}

	public void setCatatanPenyeliaRpp(String catatanPenyeliaRpp) {
		this.catatanPenyeliaRpp = catatanPenyeliaRpp;
	}
	
	public RppPermohonan getDataPermohonan() {
		RppPermohonan obj = null;
		if(this.id != null){
			DbPersistence db = new DbPersistence();
			RppAkaun ak = null;
			if(this.getDeposit().getIdLejar() != null){
				ak = (RppAkaun)db.get("select x from RppAkaun x where x.id = '"+this.getDeposit().getIdLejar()+"' ");
				if(ak != null){
					obj = ak.getPermohonan();
				}
			}
		}
		return obj;
	}

	public Users getIdDaftar() {
		return idDaftar;
	}

	public void setIdDaftar(Users idDaftar) {
		this.idDaftar = idDaftar;
	}

	public Users getIdKemaskini() {
		return idKemaskini;
	}

	public void setIdKemaskini(Users idKemaskini) {
		this.idKemaskini = idKemaskini;
	}

	public Date getTarikhDaftar() {
		return tarikhDaftar;
	}

	public void setTarikhDaftar(Date tarikhDaftar) {
		this.tarikhDaftar = tarikhDaftar;
	}

	public Date getTarikhKemaskini() {
		return tarikhKemaskini;
	}

	public void setTarikhKemaskini(Date tarikhKemaskini) {
		this.tarikhKemaskini = tarikhKemaskini;
	}

}
