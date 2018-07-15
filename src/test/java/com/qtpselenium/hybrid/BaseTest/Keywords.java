package com.qtpselenium.hybrid.BaseTest;

import com.qtpselenium.hybrid.util.Constants;
import com.qtpselenium.hybrid.util.XLS_reader;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;



public class Keywords {
	
ExtentTest test;
GenericKeywords keys;
	public Keywords(ExtentTest test) {
		
	this.test=test;
	}
	
	public void executeKeywords(String testUnderExecution,XLS_reader read) throws Exception {
		 keys= new GenericKeywords(test);
		//XLS_reader read= new XLS_reader(Constants.suiteA_XLS);
		int rows=read.getRowCount(Constants.Testcases_Sheet);
		for(int rnum=2;rnum<=rows;rnum++){
			String tcid=read.getCellData(Constants.Testcases_Sheet,Constants.TCID_Col, rnum);
			if(tcid.equalsIgnoreCase(testUnderExecution)){
				String keyword=read.getCellData(Constants.Testcases_Sheet, Constants.Keyword_Col, rnum);
				String locator1=read.getCellData(Constants.Testcases_Sheet,Constants.ObjLoc_Col, rnum);
				String data1=read.getCellData(Constants.Data_Sheet, Constants.data_Col, rnum);
				
			test.log(LogStatus.INFO, keyword+"____________"+locator1+"___________"+data1);
				if(keyword.equalsIgnoreCase("OpenBrowser")){
					keys.openBrowser(data1);
				}else if(keyword.equalsIgnoreCase("NavigateToURL")){
					keys.navigatetoURL(locator1);
				}else if(keyword.equalsIgnoreCase("Input")){
					keys.typeinField(locator1, data1);
				}else if(keyword.equalsIgnoreCase("Click")){
					keys.click(locator1);
				}else if(keyword.equalsIgnoreCase("QuitBrowser")){
					keys.quitBrowser();
				}
			}
		}
	
	
	}
	public GenericKeywords getgenericKeywords(){
		return keys;	
	}
}
