package sbbph.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * <p>
 * Java class for users complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="users">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="avatar" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="css" type="{http://ws.sbbph/}css" minOccurs="0"/>
 *         &lt;element name="dateRegistered" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="lastLoginDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="role" type="{http://ws.sbbph/}role" minOccurs="0"/>
 *         &lt;element name="userAddress" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="userIPAddress" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="userLoginAlt" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="userName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="userPassword" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "users", propOrder = { "avatar", "css", "dateRegistered", "id",
		"lastLoginDate", "role", "userAddress", "userIPAddress",
		"userLoginAlt", "userName", "userPassword" })
public class Users {

	protected String avatar;
	protected Css css;
	@XmlSchemaType(name = "dateTime")
	protected XMLGregorianCalendar dateRegistered;
	protected String id;
	@XmlSchemaType(name = "dateTime")
	protected XMLGregorianCalendar lastLoginDate;
	protected Role role;
	protected String userAddress;
	protected String userIPAddress;
	protected String userLoginAlt;
	protected String userName;
	protected String userPassword;

	/**
	 * Gets the value of the avatar property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getAvatar() {
		return avatar;
	}

	/**
	 * Sets the value of the avatar property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setAvatar(String value) {
		this.avatar = value;
	}

	/**
	 * Gets the value of the css property.
	 * 
	 * @return possible object is {@link Css }
	 * 
	 */
	public Css getCss() {
		return css;
	}

	/**
	 * Sets the value of the css property.
	 * 
	 * @param value
	 *            allowed object is {@link Css }
	 * 
	 */
	public void setCss(Css value) {
		this.css = value;
	}

	/**
	 * Gets the value of the dateRegistered property.
	 * 
	 * @return possible object is {@link XMLGregorianCalendar }
	 * 
	 */
	public XMLGregorianCalendar getDateRegistered() {
		return dateRegistered;
	}

	/**
	 * Sets the value of the dateRegistered property.
	 * 
	 * @param value
	 *            allowed object is {@link XMLGregorianCalendar }
	 * 
	 */
	public void setDateRegistered(XMLGregorianCalendar value) {
		this.dateRegistered = value;
	}

	/**
	 * Gets the value of the id property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets the value of the id property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setId(String value) {
		this.id = value;
	}

	/**
	 * Gets the value of the lastLoginDate property.
	 * 
	 * @return possible object is {@link XMLGregorianCalendar }
	 * 
	 */
	public XMLGregorianCalendar getLastLoginDate() {
		return lastLoginDate;
	}

	/**
	 * Sets the value of the lastLoginDate property.
	 * 
	 * @param value
	 *            allowed object is {@link XMLGregorianCalendar }
	 * 
	 */
	public void setLastLoginDate(XMLGregorianCalendar value) {
		this.lastLoginDate = value;
	}

	/**
	 * Gets the value of the role property.
	 * 
	 * @return possible object is {@link Role }
	 * 
	 */
	public Role getRole() {
		return role;
	}

	/**
	 * Sets the value of the role property.
	 * 
	 * @param value
	 *            allowed object is {@link Role }
	 * 
	 */
	public void setRole(Role value) {
		this.role = value;
	}

	/**
	 * Gets the value of the userAddress property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getUserAddress() {
		return userAddress;
	}

	/**
	 * Sets the value of the userAddress property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setUserAddress(String value) {
		this.userAddress = value;
	}

	/**
	 * Gets the value of the userIPAddress property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getUserIPAddress() {
		return userIPAddress;
	}

	/**
	 * Sets the value of the userIPAddress property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setUserIPAddress(String value) {
		this.userIPAddress = value;
	}

	/**
	 * Gets the value of the userLoginAlt property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getUserLoginAlt() {
		return userLoginAlt;
	}

	/**
	 * Sets the value of the userLoginAlt property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setUserLoginAlt(String value) {
		this.userLoginAlt = value;
	}

	/**
	 * Gets the value of the userName property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * Sets the value of the userName property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setUserName(String value) {
		this.userName = value;
	}

	/**
	 * Gets the value of the userPassword property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getUserPassword() {
		return userPassword;
	}

	/**
	 * Sets the value of the userPassword property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setUserPassword(String value) {
		this.userPassword = value;
	}

}
