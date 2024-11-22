package Base;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import Amazon.Controls.TextLocator;

public class DashBoardPage extends BaseTest {
	
	public DashBoardPage generalActions() {
		
	//	amazonLog.createTestStep("1st step");
		amazonLog.CreateTestStep("First Steps");
		amazonLog.LogSuccessWithImage("First Image");
		amazonLog.CreateTestStep("Second Step");
//		amazonLog.LogFailureWithImage("Fail ScreenShot");
		WebElement element =getWebDriver().findElement(By.xpath("//span[text()='Beauty, Toys & More']"));
		WebElement camera =getWebDriver().findElement(By.xpath("//div[text()='Camera']"));
		camera.click();
		Actions action =new Actions(getWebDriver());	
		
//		WebElement food = getWebDriver().findElement(By.xpath("//a[text()='Food & Drinks']"));
//		action.moveToElement(food).click().perform();
		return this;
	}
	public DashBoardPage navigateToLogin() {
		getWebDriver().findElement(By.xpath("//span[text()='Login']")).click();
		return this;
	}
	public void abc() {
		System.out.println("example");
	}
	
	

}
