package com.qtpselenium.hybrid.BaseTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.support.ui.Select;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import com.qtpselenium.hybrid.util.*;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

@SuppressWarnings("unused")
public class GenericKeywords {
	public static  WebDriver driver;
	public Properties prop;
	public	ExtentReports rep=com.qtpselenium.hybrid.util.ExtentManager.getInstance();
	public ExtentTest test;
	public com.qtpselenium.hybrid.util.XLS_reader xls;	
	public static Actions a;
	public static ChromeOptions opt;
	public String mainWindow;
	public static Set<String> s1;
	public  Iterator<String> iterator;
	
	public  GenericKeywords(ExtentTest test) throws IOException{
		this.test=test;
		if(prop==null){
			prop=new Properties();
			FileInputStream input=new FileInputStream(System.getProperty("user.dir")+"//src//test/resources//ProjectConfig.properties");
			prop.load(input);
		}
	}
	public void openBrowser(String browser) throws IOException{
		//System.out.println(prop.getProperty("AppURL"));
		//System.out.println(System.getProperty("user.dir"));
		if(browser.equals("Mozilla")){
			System.setProperty("webdriver.gecko.driver",prop.getProperty("GeckoDriver"));
			driver=new FirefoxDriver();
		}else if(browser.equals("IE")){
			System.setProperty("webdriver.ie.driver", prop.getProperty("IE_DriverExe"));
			driver=new InternetExplorerDriver();
		}else if(browser.equals("Chrome")){
			System.setProperty("webdriver.chrome.driver",prop.getProperty("ChromeDriver"));
			opt=new ChromeOptions();
			opt.addArguments("--disable-notifications");
			driver=new ChromeDriver(opt);
		}
	driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	driver.manage().window().maximize();
	mainWindow=driver.getWindowHandle();
		//initiate the prop file
	 
	}
	public void SelectFromList(String locator,String elementToSelect) throws Exception{
		WebElement list=getElement(locator);
		List<WebElement> li=list.findElements(By.tagName("option"));
		Select s= new Select(list);
		s.selectByVisibleText(elementToSelect);
		Thread.sleep(2000);
		test.log(LogStatus.INFO, "Selected on "+elementToSelect+" from List");
	}
	public void navigatetoURL(String url){
		test.log(LogStatus.INFO, "Navigating to URL : "+url);
		driver.navigate().to(prop.getProperty(url));
	}
	public void click(String locator){
		test.log(LogStatus.INFO, "Clicking on Link : "+locator);
		getElement(locator).click();
	}
	public void moveToElementAndClick(String locator){
		test.log(LogStatus.INFO, "Move and click on  : "+locator);
		a=new Actions(driver);
		a.moveToElement(getElement(locator)).click().build().perform();
	}
	public void typeinField(String locator ,String data){
		test.log(LogStatus.INFO, "Typing int the input field : "+locator+" value : "+data);
		getElement(locator).sendKeys(data);
	}
	public void getElementInsideboxAndClick(String boxLocator,String linkLocator) throws Exception{
		Thread.sleep(3000);
		WebElement box1=getElement((boxLocator)); 
		a=new Actions(driver);
		a.moveToElement(box1.findElement(By.xpath(prop.getProperty(linkLocator)))).click().build().perform();
		//test.log(LogStatus.INFO, "Clicked on Delete Link");
		Thread.sleep(5000);
	}
	public WebElement getElement(String locator){
		WebElement elem=null;
		try{
		if(locator.endsWith("_xpath")){
			elem=driver.findElement(By.xpath(prop.getProperty(locator)));
		}else if(locator.endsWith("_id")){
			elem=driver.findElement(By.id(prop.getProperty(locator)));
		}else if(locator.endsWith("_name")){
			elem=driver.findElement(By.name(prop.getProperty(locator)));
		}else if(locator.endsWith("_css")){
			elem=driver.findElement(By.cssSelector(prop.getProperty(locator)));
		}else if(locator.endsWith("_linkText")){
			elem=driver.findElement(By.linkText(prop.getProperty(locator)));
		}else{
			reportFail("Element locator incorrect."+ locator);
			Assert.fail();
		}
	
		}catch(Exception e){
			reportFail(e.getMessage());
			e.printStackTrace();
			Assert.fail("Failed the test : "+ e.getMessage());
		}
		return elem;
	}
	public void quitBrowser(){
		driver.quit();
	}
	public void selectDate(String MonthYearinCalendar,String date) throws ParseException, Exception{
		SimpleDateFormat df=new SimpleDateFormat("MM/dd/yyyy");
		Date datetobeSelected=df.parse(date);
		Date currentDate=new Date();
		String monthyearDisplayed= getElement(MonthYearinCalendar).getText();
		String month=new SimpleDateFormat("MMMM").format(datetobeSelected);
		String year=new SimpleDateFormat("yyyy").format(datetobeSelected);
		String day=new SimpleDateFormat("d").format(datetobeSelected);
		String monthYeartobeSelected=month+" "+year;
		System.out.println(monthYeartobeSelected);
		while(true){
			if(monthYeartobeSelected.equalsIgnoreCase(monthyearDisplayed)){		
				Thread.sleep(2000);
				driver.findElement(By.xpath(prop.getProperty("calendarTable_selectDate1_xpath")+day+prop.getProperty("calendarTable_selectDate2_xpath"))).click();
				test.log(LogStatus.INFO, "Selected the date "+date);
				break;
			}else{
				if(datetobeSelected.after(currentDate)){
					driver.findElement(By.id("nm")).click();
					Thread.sleep(2000);
				}else{
					driver.findElement(By.id("pm")).click();
					Thread.sleep(2000);
				}
			}
			monthyearDisplayed=getElement(MonthYearinCalendar).getText();
		}
	}
	
	/******Validations***************/
	public boolean isElementPresent(String locator){
		List<WebElement> elemList=null;
		if(locator.endsWith("_xpath")){
			elemList=driver.findElements(By.xpath(prop.getProperty(locator)));
		}else if(locator.endsWith("_id")){
			elemList=driver.findElements(By.id(prop.getProperty(locator)));
		}else if(locator.endsWith("_name")){
			elemList=driver.findElements(By.name(prop.getProperty(locator)));
		}else if(locator.endsWith("_css")){
			elemList=driver.findElements(By.cssSelector(prop.getProperty(locator)));
		}else if(locator.endsWith("_linkText")){
			elemList=driver.findElements(By.linkText(prop.getProperty(locator)));
		}else{
			reportFail("Element locator incorrect."+ locator);
			Assert.fail("Locator incorrect : "+ locator);
		}
		
		if(elemList.size()==0)
			return false;
		else
			return true;
	}
	public boolean verifyText(String locator,String texttobeVerified){
	 String actualText= getElement(locator).getText().trim();
	 String tobeVerified=prop.getProperty(texttobeVerified);
	 if(actualText.equalsIgnoreCase(tobeVerified))
		return true;
	else
		return false;
	}
	public boolean verifyTitle(){
		return false;
		
	}
	/******Reporting***************/
	public void reportPass(String msg){
		test.log(LogStatus.PASS, msg);
	}
	public void reportFail(String msg){
		takeScreenshot();	
		test.log(LogStatus.FAIL,msg);	
			
		Assert.fail(msg);
		}
	public void reportSkip(String msg){
		test.log(LogStatus.SKIP, msg);
	}
	public void takeScreenshot(){
		Date d=new Date();
		String fileName=d.toString().replace(":", "_").replace(" ", "_")+".png";
		
		File screen=((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		try{
		FileUtils.copyFile(screen, new File(System.getProperty("user.dir")+"//Screenshots//"+fileName));
		}catch(Exception e){
			e.printStackTrace();
		}
		//put screenshots in report
		test.log(LogStatus.INFO, "Screenshot : "+test.addScreenCapture(System.getProperty("user.dir")+"//Screenshots//"+fileName));
	}
	/************************* Appliation related functions 
	 * @return *****************/
	public boolean doLogin(String userName,String password){
		test.log(LogStatus.INFO, "Loggin in with id : "+userName+" and password : "+password);
		click("Userid_id");
		typeinField("Userid_id",userName);
		typeinField("Password_id",password);
		click("SignInButton_id");
		if(isElementPresent("CRMLink_xpath")){
			test.log(LogStatus.PASS, "Login Successful");
			return true;
			
		}else{
			test.log(LogStatus.FAIL, "Login Failed");
			return false;
		
		}
		
	}
	
	public int getRowNumofLead(String leadLastName){
		List<WebElement> leads=driver.findElements(By.xpath(prop.getProperty("leadTableLeadNameCol_xpath")));
		for(int i=0;i<leads.size();i++){
			String leadName=leads.get(i).getText().trim();
			if(leadName.equals(leadLastName)){
				test.log(LogStatus.INFO, "Lead : "+leadLastName+" is found at row num :"+ (i+1));
				return (i+1);
			}
		}
		//test.log(LogStatus.FAIL, "Lead Name not found in table");
		return -1;
	}
	public void clickLeadName(String leadName){
		test.log(LogStatus.INFO, "Clicking on lead Name	");
		int rnum=getRowNumofLead(leadName);
		if(rnum==-1){
			test.log(LogStatus.FAIL, "Lead Name not found in table");}
		driver.findElement(By.xpath(prop.getProperty("leadName1_xpath")+rnum+prop.getProperty("leadName2_xpath"))).click();
	}
	public int getRowNumofAccount(String companyName){
		List<WebElement> accounts=driver.findElements(By.xpath(prop.getProperty("AccountTable_AccNameCol_xpath")));
		for(int i=0;i<accounts.size();i++){
			String accName=accounts.get(i).getText().trim();
			if(accName.equals(companyName)){
				test.log(LogStatus.INFO, "Company : "+companyName+" is found at row num :"+ (i+1));
				return (i+1);
			}
		}
		return -1;
	}
	public int getRowNumofDeal(String dealName){
		List<WebElement> deals=driver.findElements(By.xpath(prop.getProperty("DealTable_dealName_xpath")));
		for(int i=0;i<deals.size();i++){
			String accName=deals.get(i).getText().trim();
			if(accName.equals(dealName)){
				test.log(LogStatus.INFO, "Deal : "+dealName+" is found at row num :"+ (i+1));
				return (i+1);
			}
		}
		return -1;
	}
	
}
