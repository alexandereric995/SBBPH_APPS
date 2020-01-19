
package sbbph.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for hrmisManager complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="hrmisManager">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="flagBukanPenjawatAwam" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="flagSemakanHRMIS" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="penjawatAwam" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
@XmlType(name = "hrmisManager", propOrder = {
    "flagBukanPenjawatAwam",
    "flagSemakanHRMIS",
    "penjawatAwam",
    "replyMsg",
    "validIc"
})
public class HrmisManager {

    protected boolean flagBukanPenjawatAwam;
    protected boolean flagSemakanHRMIS;
    protected boolean penjawatAwam;
    protected String replyMsg;
    protected boolean validIc;

    /**
     * Gets the value of the flagBukanPenjawatAwam property.
     * 
     */
    public boolean isFlagBukanPenjawatAwam() {
        return flagBukanPenjawatAwam;
    }

    /**
     * Sets the value of the flagBukanPenjawatAwam property.
     * 
     */
    public void setFlagBukanPenjawatAwam(boolean value) {
        this.flagBukanPenjawatAwam = value;
    }

    /**
     * Gets the value of the flagSemakanHRMIS property.
     * 
     */
    public boolean isFlagSemakanHRMIS() {
        return flagSemakanHRMIS;
    }

    /**
     * Sets the value of the flagSemakanHRMIS property.
     * 
     */
    public void setFlagSemakanHRMIS(boolean value) {
        this.flagSemakanHRMIS = value;
    }

    /**
     * Gets the value of the penjawatAwam property.
     * 
     */
    public boolean isPenjawatAwam() {
        return penjawatAwam;
    }

    /**
     * Sets the value of the penjawatAwam property.
     * 
     */
    public void setPenjawatAwam(boolean value) {
        this.penjawatAwam = value;
    }

    /**
     * Gets the value of the replyMsg property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReplyMsg() {
        return replyMsg;
    }

    /**
     * Sets the value of the replyMsg property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
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
