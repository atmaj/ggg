package com.framework;

import com.core.ExecutorServiceTest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;

public class MyThread implements Runnable {
	
	private String myName;
	private String TagList;
	private String TestName;
	private String iternum;
	private String DataSheet;
	private String DateStamp;
	private String buildName;
	private int threadNumber;
	
	public MyThread(String name,String newTagList,String testName,String Iternum,String DataSheet,String DateStamp,String buildName) {
		this.myName=name;
		this.TagList= newTagList;
		this.TestName=testName;
		this.iternum=Iternum;
		this.DataSheet=DataSheet;
		this.DateStamp=DateStamp;
		this.buildName=buildName;
		
	}

	public void run() {
		try {
			System.out.println("Started execution of Test Case :"+TestName);
			try {
				String cmd="/c cd "+System.getProperty("user.dir")+"\\ &mvn test -Dtest=com.core.TestRunner -DtagTestData=\""+TestName+"\" -DTs=\""+ ExecutorServiceTest.DateStamp+"\" -DBuildName=\""+buildName+"\" -DThreadNumber=\""+threadNumber+"\" -Dcucumber.options=\" --tags "+ TestName + "\" & exit";
				ProcessBuilder pb=new ProcessBuilder ("cmd.exe",cmd);
				Process process=pb.start();
				ArrayList<String> Output=null;
				Output =output(process.getInputStream());
				boolean TestRun=false;
				if(Output.toString().contains("0 Scenarios")){
					TestRun=false;
				}else {
					TestRun=true;
				}
				
				Iterator<String> MyIter=Output.iterator();
				if(TestRun) {
					while(MyIter.hasNext()) {
						String ChkStr =(String)MyIter.next();
					}
				}
				Output.clear();	
			}catch(Exception e) {
				e.getMessage();
			}
		}catch(Exception e) {
			e.getMessage();
		}
	}
	
	
	static ArrayList<String> output(InputStream inputStream) throws IOException{
		BufferedReader br=null;
		ArrayList<String> ResultValues =new ArrayList<String>();
		try {
			br=new BufferedReader(new InputStreamReader(inputStream));
			String line=null;
			while (null != (line=br.readLine())) {
				ResultValues.add(line.toString());
				System.out.println(line.toString());
			}
		}finally {
			br.close();
		}
		return ResultValues;
	}
}
