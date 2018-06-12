package com.prts.pickcustomer.payment.paytm;

import com.google.gson.annotations.SerializedName;

public class TransactionStatus {

	@SerializedName("GATEWAYNAME")
	private String gATEWAYNAME;

	@SerializedName("RESPMSG")
	private String rESPMSG;

	@SerializedName("BANKNAME")
	private String bANKNAME;

	@SerializedName("PAYMENTMODE")
	private String pAYMENTMODE;

	@SerializedName("MID")
	private String mID;

	@SerializedName("RESPCODE")
	private String rESPCODE;

	@SerializedName("TXNTYPE")
	private String tXNTYPE;

	@SerializedName("TXNID")
	private String tXNID;

	@SerializedName("TXNAMOUNT")
	private String tXNAMOUNT;

	@SerializedName("ORDERID")
	private String oRDERID;

	@SerializedName("BANKTXNID")
	private String bANKTXNID;

	@SerializedName("STATUS")
	private String sTATUS;

	@SerializedName("REFUNDAMT")
	private String rEFUNDAMT;

	@SerializedName("TXNDATE")
	private String tXNDATE;

	public void setGATEWAYNAME(String gATEWAYNAME){
		this.gATEWAYNAME = gATEWAYNAME;
	}

	public String getGATEWAYNAME(){
		return gATEWAYNAME;
	}

	public void setRESPMSG(String rESPMSG){
		this.rESPMSG = rESPMSG;
	}

	public String getRESPMSG(){
		return rESPMSG;
	}

	public void setBANKNAME(String bANKNAME){
		this.bANKNAME = bANKNAME;
	}

	public String getBANKNAME(){
		return bANKNAME;
	}

	public void setPAYMENTMODE(String pAYMENTMODE){
		this.pAYMENTMODE = pAYMENTMODE;
	}

	public String getPAYMENTMODE(){
		return pAYMENTMODE;
	}

	public void setMID(String mID){
		this.mID = mID;
	}

	public String getMID(){
		return mID;
	}

	public void setRESPCODE(String rESPCODE){
		this.rESPCODE = rESPCODE;
	}

	public String getRESPCODE(){
		return rESPCODE;
	}

	public void setTXNTYPE(String tXNTYPE){
		this.tXNTYPE = tXNTYPE;
	}

	public String getTXNTYPE(){
		return tXNTYPE;
	}

	public void setTXNID(String tXNID){
		this.tXNID = tXNID;
	}

	public String getTXNID(){
		return tXNID;
	}

	public void setTXNAMOUNT(String tXNAMOUNT){
		this.tXNAMOUNT = tXNAMOUNT;
	}

	public String getTXNAMOUNT(){
		return tXNAMOUNT;
	}

	public void setORDERID(String oRDERID){
		this.oRDERID = oRDERID;
	}

	public String getORDERID(){
		return oRDERID;
	}

	public void setBANKTXNID(String bANKTXNID){
		this.bANKTXNID = bANKTXNID;
	}

	public String getBANKTXNID(){
		return bANKTXNID;
	}

	public void setSTATUS(String sTATUS){
		this.sTATUS = sTATUS;
	}

	public String getSTATUS(){
		return sTATUS;
	}

	public void setREFUNDAMT(String rEFUNDAMT){
		this.rEFUNDAMT = rEFUNDAMT;
	}

	public String getREFUNDAMT(){
		return rEFUNDAMT;
	}

	public void setTXNDATE(String tXNDATE){
		this.tXNDATE = tXNDATE;
	}

	public String getTXNDATE(){
		return tXNDATE;
	}
}