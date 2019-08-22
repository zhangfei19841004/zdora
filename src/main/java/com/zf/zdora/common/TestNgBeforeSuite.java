package com.zf.zdora.common;

import org.testng.annotations.*;

public class TestNgBeforeSuite {

	@BeforeSuite
	@Parameters({"eid"})
	public void beforeSuite(@Optional("") String eid){
		ZTestReport.name = eid;
	}



}
