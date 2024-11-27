package Base;

import org.openqa.selenium.By;
import utilities.TestInfo;
import Amazon.Controls.TextLocator;
import Amazon.enumFile.LocatorType;

public class DashBoardPage extends BaseTest {
	
	private TextLocator spanText= new TextLocator(LocatorType.XPATH,"//span[text()='%']","Beauty & Toys Button");
	private TextLocator divText= new TextLocator(LocatorType.XPATH,"//div[text()='%']","Beauty & Toys Button");
	private TextLocator imageLinks= new TextLocator(LocatorType.XPATH,"//img[@alt='%']","Image Links");

	public DashBoardPage generalActions() {
		
		AmazonLog.CreateTestStep("First Steps");
		AmazonLog.LogSuccessWithImage("First Image");
		System.out.println(AmazonData.getInputData("Comments"));
//		System.out.println(spanText.replaceLocator("Beauty, Toys & More"));
//		spanText.replaceLocator("Beauty, Toys & More").Click();
//		spanText.replaceLocator("Fashion").Click();
		divText.replaceLocator("Beauty, Food, Toys & more").scrollAndTakeHighlightedScreenshot();
		AmazonLog.CreateTestStep("Fashion Page");
		imageLinks.replaceLocator("Mobiles").scrollAndTakeScreenshot();
		imageLinks.replaceLocator("Mobiles").HighLightElement();
		AmazonLog.LogSuccessWithImage("Mobiles Page Loaded Successfully");
		divText.replaceLocator("Camera");
		return this;
	}
	
	public DashBoardPage navigateToLogin() {
		getWebDriver().findElement(By.xpath("//span[text()='Login']")).click();
		return this;
	}
	

}
