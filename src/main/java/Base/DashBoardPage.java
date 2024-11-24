package Base;

import org.openqa.selenium.By;

import Amazon.Controls.TextLocator;
import Amazon.enumFile.LocatorType;

public class DashBoardPage extends BaseTest {
	
	public TextLocator spanText= new TextLocator(LocatorType.XPATH,"//span[text()='%']","Beauty & Toys Button");
	public TextLocator divText= new TextLocator(LocatorType.XPATH,"//span[text()='%']","Beauty & Toys Button");
	
	public DashBoardPage generalActions() {
		
		AmazonLog.CreateTestStep("First Steps");
		AmazonLog.LogSuccessWithImage("First Image");
		System.out.println(spanText.replaceLocator("Beauty, Toys & More"));
		spanText.replaceLocator("Beauty, Toys & More").Click();
		AmazonLog.CreateTestStep("Second Step");
		divText.replaceLocator("Camera");
		return this;
	}
	
	public DashBoardPage navigateToLogin() {
		getWebDriver().findElement(By.xpath("//span[text()='Login']")).click();
		return this;
	}
	

}
