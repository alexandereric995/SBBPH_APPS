package bph.entities.integrasi;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lebah.template.UID;
import bph.entities.kod.JabatanPembayarJANM;

@Entity
@Table(name = "int_janm_rekod")
public class IntJANMRekod {

	@Id
	@Column(name = "id")
	private String id;

	@ManyToOne
	@JoinColumn(name = "id_int_janm")
	private IntJANM janm;

	@Column(name = "type")
	private String type;

	@Column(name = "department_code")
	private String departmentCode;

	@ManyToOne
	@JoinColumn(name = "department")
	private JabatanPembayarJANM department;

	@Column(name = "pay_center")
	private String payCenter;

	@Column(name = "region")
	private String region;

	@Column(name = "personnel_no")
	private String personnelNo;

	@Column(name = "ic")
	private String ic;

	@Column(name = "account_no")
	private String accountNo;

	@Column(name = "name")
	private String name;

	@Column(name = "deduction_amount")
	private double deductionAmount;

	@Column(name = "deduction_code")
	private String deductionCode;

	public IntJANMRekod() {
		setId(UID.getUID());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public IntJANM getJanm() {
		return janm;
	}

	public void setJanm(IntJANM janm) {
		this.janm = janm;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDepartmentCode() {
		return departmentCode;
	}

	public void setDepartmentCode(String departmentCode) {
		this.departmentCode = departmentCode;
	}

	public JabatanPembayarJANM getDepartment() {
		return department;
	}

	public void setDepartment(JabatanPembayarJANM department) {
		this.department = department;
	}

	public String getPayCenter() {
		return payCenter;
	}

	public void setPayCenter(String payCenter) {
		this.payCenter = payCenter;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getPersonnelNo() {
		return personnelNo;
	}

	public void setPersonnelNo(String personnelNo) {
		this.personnelNo = personnelNo;
	}

	public String getIc() {
		return ic;
	}

	public void setIc(String ic) {
		this.ic = ic;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getDeductionAmount() {
		return deductionAmount;
	}

	public void setDeductionAmount(double deductionAmount) {
		this.deductionAmount = deductionAmount;
	}

	public String getDeductionCode() {
		return deductionCode;
	}

	public void setDeductionCode(String deductionCode) {
		this.deductionCode = deductionCode;
	}
}
