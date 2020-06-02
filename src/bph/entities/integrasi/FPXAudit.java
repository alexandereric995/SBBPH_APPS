package bph.entities.integrasi;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "fpx_audit")
public class FPXAudit {

	@Id
	@Column(name = "id")
	private Integer id;

	@Column(name = "user_login")
	private String userLogin;

	@Column(name = "fpxTxnId")
	private String fpxTxnId;

	@Column(name = "sellerOrderNo")
	private String sellerOrderNo;

	@Column(name = "info_transaksi")
	private String infoTransaksi;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "tarikh_transaksi")
	private Date tarikh_transaksi;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUserLogin() {
		return userLogin;
	}

	public void setUserLogin(String userLogin) {
		this.userLogin = userLogin;
	}

	public String getFpxTxnId() {
		return fpxTxnId;
	}

	public void setFpxTxnId(String fpxTxnId) {
		this.fpxTxnId = fpxTxnId;
	}

	public String getSellerOrderNo() {
		return sellerOrderNo;
	}

	public void setSellerOrderNo(String sellerOrderNo) {
		this.sellerOrderNo = sellerOrderNo;
	}

	public String getInfoTransaksi() {
		return infoTransaksi;
	}

	public void setInfoTransaksi(String infoTransaksi) {
		this.infoTransaksi = infoTransaksi;
	}

	public Date getTarikh_transaksi() {
		return tarikh_transaksi;
	}

	public void setTarikh_transaksi(Date tarikh_transaksi) {
		this.tarikh_transaksi = tarikh_transaksi;
	}
}
