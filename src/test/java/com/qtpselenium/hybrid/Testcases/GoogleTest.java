package com.qtpselenium.hybrid.Testcases;

import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;

import com.qtpselenium.hybrid.BaseTest.Keywords;
import com.qtpselenium.hybrid.util.Constants;
import com.qtpselenium.hybrid.util.ExtentManager;
import com.qtpselenium.hybrid.util.XLS_reader;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class GoogleTest {
	ExtentReports rep=ExtentManager.getInstance();
	ExtentTest test;
	String testName="GoogleTest";


	@Test
	public void doSearch() throws Exception{
	test=rep.startTest(testName) ;
	test.log(LogStatus.INFO, "Starting test");
	XLS_reader read=new XLS_reader(Constants.suiteA_XLS)	;
	test.log(LogStatus.INFO, "Executing keywords");
	Keywords keys=new Keywords(test);
	keys.executeKeywords(testName, read);
	test.log(LogStatus.PASS, "Test run successfully");
 //   keys.getgenericKeywords().reportFail("Test Failed");
	}
	@AfterTest
	public void quit(){
		rep.endTest(test);
		rep.flush();
	}
}
