//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.02.26 at 11:24:36 AM PST 
//


package ihe.iti.csd._2013;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;


/**
 * <p>Java class for organization complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="organization">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:ihe:iti:csd:2013}uniqueID">
 *       &lt;sequence>
 *         &lt;element name="otherID" type="{urn:ihe:iti:csd:2013}otherID" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="codedType" type="{urn:ihe:iti:csd:2013}codedtype" maxOccurs="unbounded"/>
 *         &lt;element name="primaryName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="otherName" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *                 &lt;attribute ref="{http://www.w3.org/XML/1998/namespace}lang"/>
 *               &lt;/extension>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="address" type="{urn:ihe:iti:csd:2013}address" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="contact" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;choice>
 *                   &lt;element name="provider" type="{urn:ihe:iti:csd:2013}uniqueID"/>
 *                   &lt;element name="person" type="{urn:ihe:iti:csd:2013}person"/>
 *                 &lt;/choice>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="credential" type="{urn:ihe:iti:csd:2013}credential" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="language" type="{urn:ihe:iti:csd:2013}codedtype" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="specialization" type="{urn:ihe:iti:csd:2013}codedtype" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="contactPoint" type="{urn:ihe:iti:csd:2013}contactPoint" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="parent" type="{urn:ihe:iti:csd:2013}uniqueID" minOccurs="0"/>
 *         &lt;element name="extension" type="{urn:ihe:iti:csd:2013}extension" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="record" type="{urn:ihe:iti:csd:2013}record"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "organization", propOrder = {
    "otherID",
    "codedType",
    "primaryName",
    "otherName",
    "address",
    "contact",
    "credential",
    "language",
    "specialization",
    "contactPoint",
    "parent",
    "extension",
    "record"
})
public class Organization
    extends UniqueID
{

    protected List<OtherID> otherID;
    @XmlElement(required = true)
    protected List<Codedtype> codedType;
    @XmlElement(required = true)
    protected String primaryName;
    protected List<Organization.OtherName> otherName;
    protected List<Address> address;
    protected List<Organization.Contact> contact;
    protected List<Credential> credential;
    protected List<Codedtype> language;
    protected List<Codedtype> specialization;
    protected List<ContactPoint> contactPoint;
    protected UniqueID parent;
    protected List<Extension> extension;
    @XmlElement(required = true)
    protected Record record;

    /**
     * Gets the value of the otherID property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the otherID property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getOtherID().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link OtherID }
     * 
     * 
     */
    public List<OtherID> getOtherID() {
        if (otherID == null) {
            otherID = new ArrayList<OtherID>();
        }
        return this.otherID;
    }

    /**
     * Gets the value of the codedType property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the codedType property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCodedType().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Codedtype }
     * 
     * 
     */
    public List<Codedtype> getCodedType() {
        if (codedType == null) {
            codedType = new ArrayList<Codedtype>();
        }
        return this.codedType;
    }

    /**
     * Gets the value of the primaryName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPrimaryName() {
        return primaryName;
    }

    /**
     * Sets the value of the primaryName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPrimaryName(String value) {
        this.primaryName = value;
    }

    /**
     * Gets the value of the otherName property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the otherName property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getOtherName().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Organization.OtherName }
     * 
     * 
     */
    public List<Organization.OtherName> getOtherName() {
        if (otherName == null) {
            otherName = new ArrayList<Organization.OtherName>();
        }
        return this.otherName;
    }

    /**
     * Gets the value of the address property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the address property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAddress().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Address }
     * 
     * 
     */
    public List<Address> getAddress() {
        if (address == null) {
            address = new ArrayList<Address>();
        }
        return this.address;
    }

    /**
     * Gets the value of the contact property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the contact property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getContact().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Organization.Contact }
     * 
     * 
     */
    public List<Organization.Contact> getContact() {
        if (contact == null) {
            contact = new ArrayList<Organization.Contact>();
        }
        return this.contact;
    }

    /**
     * Gets the value of the credential property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the credential property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCredential().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Credential }
     * 
     * 
     */
    public List<Credential> getCredential() {
        if (credential == null) {
            credential = new ArrayList<Credential>();
        }
        return this.credential;
    }

    /**
     * Gets the value of the language property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the language property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLanguage().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Codedtype }
     * 
     * 
     */
    public List<Codedtype> getLanguage() {
        if (language == null) {
            language = new ArrayList<Codedtype>();
        }
        return this.language;
    }

    /**
     * Gets the value of the specialization property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the specialization property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSpecialization().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Codedtype }
     * 
     * 
     */
    public List<Codedtype> getSpecialization() {
        if (specialization == null) {
            specialization = new ArrayList<Codedtype>();
        }
        return this.specialization;
    }

    /**
     * Gets the value of the contactPoint property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the contactPoint property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getContactPoint().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ContactPoint }
     * 
     * 
     */
    public List<ContactPoint> getContactPoint() {
        if (contactPoint == null) {
            contactPoint = new ArrayList<ContactPoint>();
        }
        return this.contactPoint;
    }

    /**
     * Gets the value of the parent property.
     * 
     * @return
     *     possible object is
     *     {@link UniqueID }
     *     
     */
    public UniqueID getParent() {
        return parent;
    }

    /**
     * Sets the value of the parent property.
     * 
     * @param value
     *     allowed object is
     *     {@link UniqueID }
     *     
     */
    public void setParent(UniqueID value) {
        this.parent = value;
    }

    /**
     * Gets the value of the extension property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the extension property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getExtension().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Extension }
     * 
     * 
     */
    public List<Extension> getExtension() {
        if (extension == null) {
            extension = new ArrayList<Extension>();
        }
        return this.extension;
    }

    /**
     * Gets the value of the record property.
     * 
     * @return
     *     possible object is
     *     {@link Record }
     *     
     */
    public Record getRecord() {
        return record;
    }

    /**
     * Sets the value of the record property.
     * 
     * @param value
     *     allowed object is
     *     {@link Record }
     *     
     */
    public void setRecord(Record value) {
        this.record = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;choice>
     *         &lt;element name="provider" type="{urn:ihe:iti:csd:2013}uniqueID"/>
     *         &lt;element name="person" type="{urn:ihe:iti:csd:2013}person"/>
     *       &lt;/choice>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "provider",
        "person"
    })
    public static class Contact {

        protected UniqueID provider;
        protected Person person;

        /**
         * Gets the value of the provider property.
         * 
         * @return
         *     possible object is
         *     {@link UniqueID }
         *     
         */
        public UniqueID getProvider() {
            return provider;
        }

        /**
         * Sets the value of the provider property.
         * 
         * @param value
         *     allowed object is
         *     {@link UniqueID }
         *     
         */
        public void setProvider(UniqueID value) {
            this.provider = value;
        }

        /**
         * Gets the value of the person property.
         * 
         * @return
         *     possible object is
         *     {@link Person }
         *     
         */
        public Person getPerson() {
            return person;
        }

        /**
         * Sets the value of the person property.
         * 
         * @param value
         *     allowed object is
         *     {@link Person }
         *     
         */
        public void setPerson(Person value) {
            this.person = value;
        }

    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;simpleContent>
     *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
     *       &lt;attribute ref="{http://www.w3.org/XML/1998/namespace}lang"/>
     *     &lt;/extension>
     *   &lt;/simpleContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "value"
    })
    public static class OtherName {

        @XmlValue
        protected String value;
        @XmlAttribute(name = "lang", namespace = "http://www.w3.org/XML/1998/namespace")
        protected String lang;

        /**
         * Gets the value of the value property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getValue() {
            return value;
        }

        /**
         * Sets the value of the value property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setValue(String value) {
            this.value = value;
        }

        /**
         * Gets the value of the lang property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getLang() {
            return lang;
        }

        /**
         * Sets the value of the lang property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setLang(String value) {
            this.lang = value;
        }

    }

}
