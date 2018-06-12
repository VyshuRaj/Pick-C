package com.prts.pickcustomer.driver;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SnappedPointsItem implements Serializable{

	@SerializedName("placeId")
	private String placeId;

	@SerializedName("originalIndex")
	private int originalIndex;

	@SerializedName("location")
	private Location location;

	public void setPlaceId(String placeId){
		this.placeId = placeId;
	}

	public String getPlaceId(){
		return placeId;
	}

	public void setOriginalIndex(int originalIndex){
		this.originalIndex = originalIndex;
	}

	public int getOriginalIndex(){
		return originalIndex;
	}

	public void setLocation(Location location){
		this.location = location;
	}

	public Location getLocation(){
		return location;
	}
}