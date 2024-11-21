package ui;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.time.Duration;
import java.util.List;

import org.apache.hc.core5.http.HttpConnection;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import Base.BaseTest;
import dev.failsafe.internal.util.Durations;
import io.appium.java_client.functions.ExpectedCondition;

public class Practice extends BaseTest{
	
	
	@DataProvider(name="TestData")
	public Object[][] getData(){
		return new Object[][]{
		{"user","pass"},
		{"user1","pass1"}
		};
		
	}
	
	
	
	
	
	@Test
	public void handleAlert() throws InterruptedException {
	amazonLog.CreateTestStep("Handling Alert");
//	getWebDriver().findElement(By.id("alertBtn")).click();
	
//	Alert alert=getWebDriver().switchTo().alert();
//	alert.accept();
	Actions action=new Actions(getWebDriver());
	WebElement source=getWebDriver().findElement(By.xpath("//p[text()='Drag me to my target']"));
	WebElement target=getWebDriver().findElement(By.id("droppable"));
	WebElement table=getWebDriver().findElement(By.id("taskTable"));
	Thread.sleep(3000);
	WebElement scrollable=getWebDriver().findElement(By.xpath("//p[text()='Drag me to my target']"));
    
	JavascriptExecutor js=(JavascriptExecutor)getWebDriver();
	js.executeScript("argument[0].scrollintoview(true)", scrollable);
	
	//To get some data from data provider
	
	//action.moveToElement(source).perform();
	action.scrollToElement(table).perform();
	 List<WebElement> header=getWebDriver().findElements(By.xpath("//table[@id='taskTable']//th"));
	 List<WebElement> rows=getWebDriver().findElements(By.xpath("//table[@id='taskTable']//tr"));
	 List<WebElement> tableData=getWebDriver().findElements(By.xpath("//table[@id='taskTable']//td"));
	 List<WebElement> allLinks= getWebDriver().findElements(By.tagName("a"));
	 System.out.println(allLinks.size());
	 for(WebElement ele: allLinks) {
		 String url=ele.getAttribute("href");
		 System.out.println("all links : "+ url);
		 try{
			 URL uri=new URL(url);
			 HttpURLConnection connection=(HttpURLConnection) uri.openConnection();
			 connection.setRequestMethod("HEAD");
			 connection.connect();
			 
			 int responseCode=connection.getResponseCode();
			 
			 if(responseCode>=400) {
				 System.out.println("The link is broken :" + url);
				 
			 }
		 }catch(Exception e) {
			 
		 }
	 }
	 
	 boolean status=false;
	for(WebElement ele: header) {
		String value=ele.getText();
		System.out.println("header : " + value);
		if(value.contains("CPU (%)")) {
			status=true;
			break;
		}
	}
	boolean datastatus=false;
	for(WebElement ele: tableData) {
		String value=ele.getText();
		System.out.println("header : " + value);
		if(value.contains("61.9 MB")) {
			datastatus=true;
			break;
		}
	}
	

	
//	action.dragAndDrop(source, target).perform();
	

	amazonLog.LogSuccessWithImage("Alert Succesfully Handled");
	
	}

}
