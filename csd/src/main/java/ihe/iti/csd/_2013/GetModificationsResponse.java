//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.02.26 at 11:24:36 AM PST 
//


package ihe.iti.csd._2013;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{urn:ihe:iti:csd:2013}CSD"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "csd"
})
@XmlRootElement(name = "getModificationsResponse")
public class GetModificationsResponse {

    @XmlElement(name = "CSD", required = true)
    protected CSD csd;

    /**
     * Gets the value of the csd property.
     * 
     * @return
     *     possible object is
     *     {@link CSD }
     *     
     */
    public CSD getCSD() {
        return csd;
    }

    /**
     * Sets the value of the csd property.
     * 
     * @param value
     *     allowed object is
     *     {@link CSD }
     *     
     */
    public void setCSD(CSD value) {
        this.csd = value;
    }

}
