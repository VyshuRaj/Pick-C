package com.prts.pickcustomer.driver;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.SerializedName;

public class RootObject implements Serializable{

	@SerializedName("snappedPoints")
	private List<SnappedPointsItem> snappedPoints;

	public void setSnappedPoints(List<SnappedPointsItem> snappedPoints){
		this.snappedPoints = snappedPoints;
	}

	public List<SnappedPointsItem> getSnappedPoints(){
		return snappedPoints;
	}
}