package org.tempuri.crsservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for jpnManager complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="jpnManager">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="flagSemakanJPN" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="replyMsg" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="validIc" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "jpnManager", propOrder = { "flagSemakanJPN", "replyMsg",
		"validIc" })
public class JpnManager {

	protected boolean flagSemakanJPN;
	protected String replyMsg;
	protected boolean validIc;

	/**
	 * Gets the value of the flagSemakanJPN property.
	 * 
	 */
	public boolean isFlagSemakanJPN() {
		return flagSemakanJPN;
	}

	/**
	 * Sets the value of the flagSemakanJPN property.
	 * 
	 */
	public void setFlagSemakanJPN(boolean value) {
		this.flagSemakanJPN = value;
	}

	/**
	 * Gets the value of the replyMsg property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getReplyMsg() {
		return replyMsg;
	}

	/**
	 * Sets the value of the replyMsg property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setReplyMsg(String value) {
		this.replyMsg = value;
	}

	/**
	 * Gets the value of the validIc property.
	 * 
	 */
	public boolean isValidIc() {
		return validIc;
	}

	/**
	 * Sets the value of the validIc property.
	 * 
	 */
	public void setValidIc(boolean value) {
		this.validIc = value;
	}

}
