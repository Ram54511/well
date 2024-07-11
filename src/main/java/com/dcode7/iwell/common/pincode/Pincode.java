package com.dcode7.iwell.common.pincode;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Pincode {

	@Id
	@GeneratedValue
	private UUID id;

	private String pincode;
	private String office;
	private String type;
	private String delivery;
	private String division;
	private String region;
	private String circle;
	private String district;
	private String state;

	public Pincode(String pincode, String office, String type, String delivery, String division, String region,
			String circle,  String district, String state) {
		this.pincode = pincode;
		this.office = office;
		this.type = type;
		this.delivery = delivery;
		this.division = division;
		this.region = region;
		this.circle = circle;
		this.district = district;
		this.state = state;
	}

	public Pincode() {
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getPincode() {
		return pincode;
	}

	public String getOffice() {
		return office;
	}

	public String getType() {
		return type;
	}

	public String getDelivery() {
		return delivery;
	}

	public String getDivision() {
		return division;
	}

	public String getRegion() {
		return region;
	}

	public String getCircle() {
		return circle;
	}

	public String getDistrict() {
		return district;
	}

	public String getState() {
		return state;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public void setOffice(String office) {
		this.office = office;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setDelivery(String delivery) {
		this.delivery = delivery;
	}

	public void setDivision(String division) {
		this.division = division;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public void setCircle(String circle) {
		this.circle = circle;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public void setState(String state) {
		this.state = state;
	}

}
