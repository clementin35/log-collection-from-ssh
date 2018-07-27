package application.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Helper class to wrap a list of persons. This is used for saving the
 * list of persons to XML.
 * 
 */

@XmlRootElement(name = "instances") /* Defines the name of root element */
public class InstanceListWrapper {

    private List<Addresses> addresses;

    @XmlElement(name = "address")	/* Optional name we can specify for the element */
    public List<Addresses> getInstances() {
        return addresses;
    }

    public void setInstances(List<Addresses> addresses) {
        this.addresses = addresses;
    }
}