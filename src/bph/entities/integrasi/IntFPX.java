package bph.entities.integrasi;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "int_fpx")
public class IntFPX {

	@Id
	@Column(name = "id")
	private String id;	

	@Column(name = "fpxTxnId")
	private String fpxTxnId;
	
	@Column(name = "sellerOrderNo")
	private String sellerOrderNo;
	
	@Column(name = "fpxTxnTime")
	private String fpxTxnTime;
	
	@Column(name = "sellerExOrderNo")
	private String sellerExOrderNo;
	
	@Column(name = "sellerTxnTime")
	private String sellerTxnTime;
	
	@Column(name = "txnAmount")
	private String txnAmount;
	
	@Column(name = "creditAuthNo")
	private String creditAuthNo;
	
	@Column(name = "debitAuthNo")
	private String debitAuthNo;
	
	@Column(name = "flagSync")
	private String flagSync;	
	
	@Column(name = "msg")
	private String msg;	
	
	@Column(name = "data")
	private String data;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getFpxTxnTime() {
		return fpxTxnTime;
	}

	public void setFpxTxnTime(String fpxTxnTime) {
		this.fpxTxnTime = fpxTxnTime;
	}

	public String getSellerExOrderNo() {
		return sellerExOrderNo;
	}

	public void setSellerExOrderNo(String sellerExOrderNo) {
		this.sellerExOrderNo = sellerExOrderNo;
	}

	public String getSellerTxnTime() {
		return sellerTxnTime;
	}

	public void setSellerTxnTime(String sellerTxnTime) {
		this.sellerTxnTime = sellerTxnTime;
	}

	public String getTxnAmount() {
		return txnAmount;
	}

	public void setTxnAmount(String txnAmount) {
		this.txnAmount = txnAmount;
	}

	public String getCreditAuthNo() {
		return creditAuthNo;
	}

	public void setCreditAuthNo(String creditAuthNo) {
		this.creditAuthNo = creditAuthNo;
	}

	public String getDebitAuthNo() {
		return debitAuthNo;
	}

	public void setDebitAuthNo(String debitAuthNo) {
		this.debitAuthNo = debitAuthNo;
	}

	public String getFlagSync() {
		return flagSync;
	}

	public void setFlagSync(String flagSync) {
		this.flagSync = flagSync;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
}
