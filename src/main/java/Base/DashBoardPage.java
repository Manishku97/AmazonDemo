package Base;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

public class DashBoardPage extends BaseTest {
	
	
		
	
	public DashBoardPage generalActions() {
		
	//	amazonLog.createTestStep("1st step");
		amazonLog.CreateTestStep("First Steps");
		amazonLog.LogSuccessWithImage("First Image");
		amazonLog.CreateTestStep("Second Step");
		amazonLog.LogFailureWithImage("Fail ScreenShot");
		WebElement element =getWebDriver().findElement(By.xpath("//span[text()='Beauty, Toys & More']"));
		WebElement camera =getWebDriver().findElement(By.xpath("//div[text()='Cameras']"));
		Actions action =new Actions(getWebDriver());	
		WebElement food = getWebDriver().findElement(By.xpath("//a[text()='Food & Drinks']"));
		action.moveToElement(food).click().perform();
		return this;
	}
	
	

}
