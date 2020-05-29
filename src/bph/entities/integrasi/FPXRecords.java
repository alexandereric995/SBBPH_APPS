package bph.entities.integrasi;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import db.persistence.MyPersistence;

@Entity
@Table(name = "fpx_records")
public class FPXRecords {

	@Id
	@Column(name = "fpxTxnId")
	private String id;

	@Column(name = "buyerBankBranch")
	private String buyerBankBranch;

	@Column(name = "buyerBankId")
	private String buyerBankId;

	@Column(name = "buyerIban")
	private String buyerIban;

	@Column(name = "buyerId")
	private String buyerId;

	@Column(name = "buyerName")
	private String buyerName;

	@Column(name = "creditAuthCode")
	private String creditAuthCode;

	@Column(name = "creditAuthNo")
	private String creditAuthNo;

	@Column(name = "debitAuthCode")
	private String debitAuthCode;

	@Column(name = "debitAuthNo")
	private String debitAuthNo;

	@Column(name = "fpxTxnTime")
	private String fpxTxnTime;

	@Column(name = "makerName")
	private String makerName;

	@Column(name = "msgToken")
	private String msgToken;

	@Column(name = "msgType")
	private String msgType;

	@Column(name = "sellerExId")
	private String sellerExId;

	@Column(name = "sellerExOrderNo")
	private String sellerExOrderNo;

	@Column(name = "sellerId")
	private String sellerId;

	@Column(name = "sellerOrderNo")
	private String sellerOrderNo;

	@Column(name = "sellerTxnTime")
	private String sellerTxnTime;

	@Column(name = "txnAmount")
	private String txnAmount;

	@Column(name = "txnCurrency")
	private String txnCurrency;

	@Column(name = "flagModul")
	private String flagModul;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "createdDate")
	private Date createdDate;

	@Column(name = "flagManagePayment")
	private String flagManagePayment;

	public FPXRecords() {
		setFlagManagePayment("T");
		setCreatedDate(new Date());
	}

	public String getKeteranganCreditAuthCode() {
		MyPersistence mp = null;
		String keterangan = "";

		try {
			mp = new MyPersistence();
			if (this.creditAuthCode != null) {
				FPXCodes fpxCodes = (FPXCodes) mp
						.get("select x from FPXCodes x where x.code = '"
								+ this.creditAuthCode + "'");
				if (fpxCodes != null) {
					keterangan = fpxCodes.getDescription().toUpperCase();
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) {
				mp.close();
			}
		}

		return keterangan;
	}

	public String getKeteranganDebitAuthCode() {
		MyPersistence mp = null;
		String keterangan = "";

		try {
			mp = new MyPersistence();
			if (this.debitAuthCode != null) {
				FPXCodes fpxCodes = (FPXCodes) mp
						.get("select x from FPXCodes x where x.code = '"
								+ this.debitAuthCode + "'");
				if (fpxCodes != null) {
					keterangan = fpxCodes.getDescription().toUpperCase();
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (mp != null) {
				mp.close();
			}
		}

		return keterangan;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBuyerBankBranch() {
		return buyerBankBranch;
	}

	public void setBuyerBankBranch(String buyerBankBranch) {
		this.buyerBankBranch = buyerBankBranch;
	}

	public String getBuyerBankId() {
		return buyerBankId;
	}

	public void setBuyerBankId(String buyerBankId) {
		this.buyerBankId = buyerBankId;
	}

	public String getBuyerIban() {
		return buyerIban;
	}

	public void setBuyerIban(String buyerIban) {
		this.buyerIban = buyerIban;
	}

	public String getBuyerId() {
		return buyerId;
	}

	public void setBuyerId(String buyerId) {
		this.buyerId = buyerId;
	}

	public String getBuyerName() {
		return buyerName;
	}

	public void setBuyerName(String buyerName) {
		this.buyerName = buyerName;
	}

	public String getCreditAuthCode() {
		return creditAuthCode;
	}

	public void setCreditAuthCode(String creditAuthCode) {
		this.creditAuthCode = creditAuthCode;
	}

	public String getCreditAuthNo() {
		return creditAuthNo;
	}

	public void setCreditAuthNo(String creditAuthNo) {
		this.creditAuthNo = creditAuthNo;
	}

	public String getDebitAuthCode() {
		return debitAuthCode;
	}

	public void setDebitAuthCode(String debitAuthCode) {
		this.debitAuthCode = debitAuthCode;
	}

	public String getDebitAuthNo() {
		return debitAuthNo;
	}

	public void setDebitAuthNo(String debitAuthNo) {
		this.debitAuthNo = debitAuthNo;
	}

	public String getFpxTxnTime() {
		return fpxTxnTime;
	}

	public void setFpxTxnTime(String fpxTxnTime) {
		this.fpxTxnTime = fpxTxnTime;
	}

	public String getMakerName() {
		return makerName;
	}

	public void setMakerName(String makerName) {
		this.makerName = makerName;
	}

	public String getMsgToken() {
		return msgToken;
	}

	public void setMsgToken(String msgToken) {
		this.msgToken = msgToken;
	}

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	public String getSellerExId() {
		return sellerExId;
	}

	public void setSellerExId(String sellerExId) {
		this.sellerExId = sellerExId;
	}

	public String getSellerExOrderNo() {
		return sellerExOrderNo;
	}

	public void setSellerExOrderNo(String sellerExOrderNo) {
		this.sellerExOrderNo = sellerExOrderNo;
	}

	public String getSellerId() {
		return sellerId;
	}

	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
	}

	public String getSellerOrderNo() {
		return sellerOrderNo;
	}

	public void setSellerOrderNo(String sellerOrderNo) {
		this.sellerOrderNo = sellerOrderNo;
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

	public String getTxnCurrency() {
		return txnCurrency;
	}

	public void setTxnCurrency(String txnCurrency) {
		this.txnCurrency = txnCurrency;
	}

	public String getFlagModul() {
		return flagModul;
	}

	public void setFlagModul(String flagModul) {
		this.flagModul = flagModul;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getFlagManagePayment() {
		return flagManagePayment;
	}

	public void setFlagManagePayment(String flagManagePayment) {
		this.flagManagePayment = flagManagePayment;
	}
}
