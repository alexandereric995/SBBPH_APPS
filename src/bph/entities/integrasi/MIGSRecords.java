package bph.entities.integrasi;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import portal.module.entity.Users;

@Entity
@Table(name = "migs_records")
public class MIGSRecords {

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "vpc_Amount")
	private String vpcAmount;

	@Column(name = "vpc_Locale")
	private String vpcLocale;

	@Column(name = "vpc_BatchNo")
	private String vpcBatchNo;

	@Column(name = "vpc_Command")
	private String vpcCommand;

	@Column(name = "vpc_Message")
	private String vpcMessage;

	@Column(name = "vpc_Version")
	private String vpcVersion;

	@Column(name = "vpc_Card")
	private String vpcCard;

	@Column(name = "vpc_OrderInfo")
	private String vpcOrderInfo;

	@Column(name = "vpc_ReceiptNo")
	private String vpcReceiptNo;

	@Column(name = "vpc_Merchant")
	private String vpcMerchant;

	@Column(name = "vpc_MerchTxnRef")
	private String vpcMerchTxnRef;

	@Column(name = "vpc_AuthorizeId")
	private String vpcAuthorizeId;

	@Column(name = "vpc_TransactionNo")
	private String vpcTransactionNo;

	@Column(name = "vpc_AcqResponseCode")
	private String vpcAcqResponseCode;

	@Column(name = "vpc_TxnResponseCode")
	private String vpcTxnResponseCode;

	@Column(name = "idPermohonan")
	private String idPermohonan;

	@Column(name = "flagModul")
	private String flagModul;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "createdDate")
	private Date createdDate;

	@Column(name = "flagManagePayment")
	private String flagManagePayment;

	@ManyToOne
	@JoinColumn(name = "id_masuk")
	private Users idMasuk;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "tarikh_masuk")
	private Date tarikhMasuk;

	@ManyToOne
	@JoinColumn(name = "id_kemaskini")
	private Users idKemaskini;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "tarikh_kemaskini")
	private Date tarikhKemaskini;

	public Double getAmaunBayaran() {
		Double amaun = 0D;
		if (this.vpcAmount != null) {
			amaun = Double.valueOf(this.vpcAmount) / 100;
		}
		return amaun;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getVpcAmount() {
		return vpcAmount;
	}

	public void setVpcAmount(String vpcAmount) {
		this.vpcAmount = vpcAmount;
	}

	public String getVpcLocale() {
		return vpcLocale;
	}

	public void setVpcLocale(String vpcLocale) {
		this.vpcLocale = vpcLocale;
	}

	public String getVpcBatchNo() {
		return vpcBatchNo;
	}

	public void setVpcBatchNo(String vpcBatchNo) {
		this.vpcBatchNo = vpcBatchNo;
	}

	public String getVpcCommand() {
		return vpcCommand;
	}

	public void setVpcCommand(String vpcCommand) {
		this.vpcCommand = vpcCommand;
	}

	public String getVpcMessage() {
		return vpcMessage;
	}

	public void setVpcMessage(String vpcMessage) {
		this.vpcMessage = vpcMessage;
	}

	public String getVpcVersion() {
		return vpcVersion;
	}

	public void setVpcVersion(String vpcVersion) {
		this.vpcVersion = vpcVersion;
	}

	public String getVpcCard() {
		return vpcCard;
	}

	public void setVpcCard(String vpcCard) {
		this.vpcCard = vpcCard;
	}

	public String getVpcOrderInfo() {
		return vpcOrderInfo;
	}

	public void setVpcOrderInfo(String vpcOrderInfo) {
		this.vpcOrderInfo = vpcOrderInfo;
	}

	public String getVpcReceiptNo() {
		return vpcReceiptNo;
	}

	public void setVpcReceiptNo(String vpcReceiptNo) {
		this.vpcReceiptNo = vpcReceiptNo;
	}

	public String getVpcMerchant() {
		return vpcMerchant;
	}

	public void setVpcMerchant(String vpcMerchant) {
		this.vpcMerchant = vpcMerchant;
	}

	public String getVpcMerchTxnRef() {
		return vpcMerchTxnRef;
	}

	public void setVpcMerchTxnRef(String vpcMerchTxnRef) {
		this.vpcMerchTxnRef = vpcMerchTxnRef;
	}

	public String getVpcAuthorizeId() {
		return vpcAuthorizeId;
	}

	public void setVpcAuthorizeId(String vpcAuthorizeId) {
		this.vpcAuthorizeId = vpcAuthorizeId;
	}

	public String getVpcTransactionNo() {
		return vpcTransactionNo;
	}

	public void setVpcTransactionNo(String vpcTransactionNo) {
		this.vpcTransactionNo = vpcTransactionNo;
	}

	public String getVpcAcqResponseCode() {
		return vpcAcqResponseCode;
	}

	public void setVpcAcqResponseCode(String vpcAcqResponseCode) {
		this.vpcAcqResponseCode = vpcAcqResponseCode;
	}

	public String getVpcTxnResponseCode() {
		return vpcTxnResponseCode;
	}

	public void setVpcTxnResponseCode(String vpcTxnResponseCode) {
		this.vpcTxnResponseCode = vpcTxnResponseCode;
	}

	public String getIdPermohonan() {
		return idPermohonan;
	}

	public void setIdPermohonan(String idPermohonan) {
		this.idPermohonan = idPermohonan;
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

	public Users getIdMasuk() {
		return idMasuk;
	}

	public void setIdMasuk(Users idMasuk) {
		this.idMasuk = idMasuk;
	}

	public Date getTarikhMasuk() {
		return tarikhMasuk;
	}

	public void setTarikhMasuk(Date tarikhMasuk) {
		this.tarikhMasuk = tarikhMasuk;
	}

	public Users getIdKemaskini() {
		return idKemaskini;
	}

	public void setIdKemaskini(Users idKemaskini) {
		this.idKemaskini = idKemaskini;
	}

	public Date getTarikhKemaskini() {
		return tarikhKemaskini;
	}

	public void setTarikhKemaskini(Date tarikhKemaskini) {
		this.tarikhKemaskini = tarikhKemaskini;
	}

}
