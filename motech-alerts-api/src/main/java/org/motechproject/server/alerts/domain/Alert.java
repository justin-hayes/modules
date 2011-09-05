package org.motechproject.server.alerts.domain;

import org.codehaus.jackson.annotate.JsonProperty;
import org.joda.time.DateTime;
import org.motechproject.model.MotechBaseDataObject;


public class Alert extends MotechBaseDataObject{
	private static final long serialVersionUID = 2783402492572161397L;
	
	@JsonProperty("type")
    String type = "Alert";
	
	String id;
	String externalId;
	String name;
	AlertType alertType;
	DateTime dateTime;
	int priority;
	AlertStatus status;
	String description;
	
	public String getId() {
		return id;
	}

	public String getExternalId() {
		return externalId;
	}

	public String getName() {
		return name;
	}

	public DateTime getDateTime() {
		return dateTime;
	}

	public int getPriority() {
		return priority;
	}

	public AlertStatus getStatus() {
		return status;
	}

	public String getDescription() {
		return description;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	public void setName(String name) {
		this.name = name;
	}
	public void setDateTime(DateTime dateTime) {
		this.dateTime = dateTime;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public void setStatus(AlertStatus status) {
		this.status = status;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	public AlertType getAlertType() {
		return alertType;
	}

	public void setAlertType(AlertType alertType) {
		this.alertType = alertType;
	}
	
}
