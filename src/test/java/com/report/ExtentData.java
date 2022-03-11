package com.report;

import java.util.Date;

public class ExtentData {
	private int tblidextentreports;
	public int getTblidextentreports() {
		return tblidextentreports;
	}
	public void setTblidextentreports(int tblidextentreports) {
		this.tblidextentreports = tblidextentreports;
	}
	public String getTblreportid() {
		return tblreportid;
	}
	public void setTblreportid(String tblreportid) {
		this.tblreportid = tblreportid;
	}
	public String getTbltestcaseid() {
		return tbltestcaseid;
	}
	public void setTbltestcaseid(String tbltestcaseid) {
		this.tbltestcaseid = tbltestcaseid;
	}
	public int getTblreststepnum() {
		return tblreststepnum;
	}
	public void setTblreststepnum(int tblreststepnum) {
		this.tblreststepnum = tblreststepnum;
	}
	public String getTblresultdescription() {
		return tblresultdescription;
	}
	public void setTblresultdescription(String tblresultdescription) {
		this.tblresultdescription = tblresultdescription;
	}
	public String getTblresstepstatus() {
		return tblresstepstatus;
	}
	public void setTblresstepstatus(String tblresstepstatus) {
		this.tblresstepstatus = tblresstepstatus;
	}
	public Date getTbltimestamp() {
		return tbltimestamp;
	}
	public void setTbltimestamp(Date tbltimestamp) {
		this.tbltimestamp = tbltimestamp;
	}
	private String tblreportid;
	private String tbltestcaseid;
	private int tblreststepnum;
	private String tblresultdescription;
	private String tblresstepstatus;
	private Date tbltimestamp;
	

}
