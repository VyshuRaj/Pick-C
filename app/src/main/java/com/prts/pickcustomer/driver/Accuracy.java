package com.prts.pickcustomer.driver;

import com.google.gson.annotations.SerializedName;

public class Accuracy{

	@SerializedName("rootObject")
	private RootObject rootObject;

	public void setRootObject(RootObject rootObject){
		this.rootObject = rootObject;
	}

	public RootObject getRootObject(){
		return rootObject;
	}
}