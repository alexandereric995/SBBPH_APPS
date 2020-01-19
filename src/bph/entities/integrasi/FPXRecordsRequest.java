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
@Table(name = "fpx_records_request")
public class FPXRecordsRequest {

	@Id
	@Column(name = "id")
	private String id;	

	@Column(name = "sellerId")
	private String sellerId;
	
	@Column(name = "sellerExId")
	private String sellerExId;
	
	@Column(name = "sellerOrderNo")
	private String sellerOrderNo;
	
	@Column(name = "sellerExOrderNo")
	private String sellerExOrderNo;
	
	@Column(name = "txnAmount")
	private String txnAmount;
	
	@Column(name = "flagModul")
	private String flagModul;
	
	@Column(name = "productDesc")
	private String productDesc;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "requestDate")
	private Date requestDate;
	
	@Column(name = "fpxTxnId")
	private String fpxTxnId;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "respondDate")
	private Date respondDate;
	
	public FPXRecordsRequest() {
		setId(UID.getUID());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSellerId() {
		return sellerId;
	}

	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
	}

	public String getSellerExId() {
		return sellerExId;
	}

	public void setSellerExId(String sellerExId) {
		this.sellerExId = sellerExId;
	}

	public String getSellerOrderNo() {
		return sellerOrderNo;
	}

	public void setSellerOrderNo(String sellerOrderNo) {
		this.sellerOrderNo = sellerOrderNo;
	}

	public String getSellerExOrderNo() {
		return sellerExOrderNo;
	}

	public void setSellerExOrderNo(String sellerExOrderNo) {
		this.sellerExOrderNo = sellerExOrderNo;
	}

	public String getTxnAmount() {
		return txnAmount;
	}

	public void setTxnAmount(String txnAmount) {
		this.txnAmount = txnAmount;
	}

	public String getFlagModul() {
		return flagModul;
	}

	public void setFlagModul(String flagModul) {
		this.flagModul = flagModul;
	}

	public String getProductDesc() {
		return productDesc;
	}

	public void setProductDesc(String productDesc) {
		this.productDesc = productDesc;
	}

	public Date getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
	}

	public String getFpxTxnId() {
		return fpxTxnId;
	}

	public void setFpxTxnId(String fpxTxnId) {
		this.fpxTxnId = fpxTxnId;
	}

	public Date getRespondDate() {
		return respondDate;
	}

	public void setRespondDate(Date respondDate) {
		this.respondDate = respondDate;
	}
}
