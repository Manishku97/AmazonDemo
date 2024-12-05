package Amazon.Controls;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;

import Amazon.enumFile.LocatorType;
import Base.AmazonBrowser;
import Base.AmazonLog;

public class BaseLocator {
	public LocatorType locatorType;
	public String locatorValue;
	public String locatorDescription;
	public By byLocator;
	public String elementType;
	public String _locatorValue;
	public String _locatorDescription;

	public BaseLocator(LocatorType locatorType, String locatorValue, String locatorDescription, String elementType) {
		this.locatorType = locatorType;
		this.locatorValue = locatorValue;
		this.locatorDescription = locatorDescription;
		this.elementType = elementType;
		this.byLocator = getLocator(this.locatorType, this.locatorValue);
		this._locatorValue = locatorValue;
		this._locatorDescription = locatorDescription;

	}

	public BaseLocator replaceLocator(String replaceValue) {
		this._locatorValue = locatorValue.replace("%", replaceValue);
		this._locatorDescription = locatorDescription.replace("%", replaceValue);
		this.byLocator = getLocator(this.locatorType, _locatorValue);
		return this;

	}

	public BaseLocator replaceLocator(String replaceValue, String replaceValue1) {
		this._locatorValue = locatorValue.replace("%", replaceValue).replace("$", replaceValue1);
		this._locatorDescription = locatorDescription.replace("%", replaceValue).replace("$", replaceValue1);
		this.byLocator = getLocator(this.locatorType, _locatorValue);
		return this;

	}

	protected By getLocator(LocatorType locatorType, String locatorValue) {
		By byLocator = null;

		switch (locatorType) {
		case XPATH:
			byLocator = By.xpath(locatorValue);
			break;

		case CLASSNAME:
			byLocator = By.className(locatorValue);
			break;

		case ID:
			byLocator = By.id(locatorValue);
			break;

		case LINKTEXT:
			byLocator = By.linkText(locatorValue);
			break;

		}
		return byLocator;
	}

	public void waitForElement(boolean isClickAction) {
		if (isClickAction) {
			AmazonBrowser.getWait().until(ExpectedConditions.elementToBeClickable(this.byLocator));
		} else {
			AmazonBrowser.getWait().until(ExpectedConditions.visibilityOfElementLocated(this.byLocator));
		}
	}

	public WebElement FindElement() {
		return FindElement(false);
	}

	public WebElement FindElement(boolean isClickAction) {
		waitForElement(isClickAction);
		return AmazonBrowser.getWebDriver().findElement(this.byLocator);
	}

	public boolean isElementPresent() {
		boolean result = false;
		try {
			AmazonBrowser.getMiniWait().until(ExpectedConditions.visibilityOfElementLocated(this.byLocator));
			result = true;
		} catch (Exception e) {
			result = false;
		}
		return result;
	}

	public void Click() {
		FindElement(true).click();
		AmazonLog.LogInfo("Clicked on " + this._locatorDescription + " " + this.elementType);
	}

	public void ClearText() {
		FindElement().clear();
	}

	public String GetText() {
		return GetTextMain(false);
	}

	public void TypeText(String value) {
		TypeText(value, true);
	}

	public void TypeText(String value, boolean isClearReq) {
		if (isClearReq) {
			ClearText();
			FindElement().sendKeys(value);
			if (this._locatorDescription.contains("password")) {
				value = "*******";
			}
			System.out.println(
					"Entered text for the field " + this._locatorDescription + " " + this.elementType + " as " + value);
		}

	}

	public String GetTextMain(boolean isHighLightRequired) {
		String result = FindElement().getText();
		boolean outVal = null != result && !result.isEmpty();
		if (!outVal) {
			for (int i = 1; i <= 5 & !outVal; i++) {
				if (i > 1)
					System.out.println("Get Text Call count" + i);
				result = getAttributeValue("value");
				outVal = null != result && !result.isEmpty();
			}
		}
		if (isHighLightRequired) {
			HighLightElement();
			System.out.println(this._locatorDescription + " " + this.elementType + " value is "
					+ (result.isEmpty() ? "Empty" : result));
			ResetHighLightElement();
		} else
			AmazonLog.LogSuccess(this._locatorDescription + " " + this.elementType + " Value is "
					+ (result.isEmpty() ? "Empty" : result));
		return result;
	}

	public String getAttributeValue(String attributeName) {
		return FindElement().getAttribute(attributeName);
	}

	public void HighLightElement() {
		((JavascriptExecutor) AmazonBrowser.getWebDriver()).executeScript(
				"arguments[0].setAttribute('style', arguments[1]);", FindElement(),
				"border: 2px solid red; background-color: yellow;");
	}

	public void ResetHighLightElement() {
		((JavascriptExecutor) AmazonBrowser.getWebDriver()).executeScript("arguments[0].style.border=''",
				FindElement());
	}

	// Scroll to the bottom of the page
	public void scrollToBottom() {
		JavascriptExecutor jsExecutor = (JavascriptExecutor) AmazonBrowser.getWebDriver();
		jsExecutor.executeScript("window.scrollTo(0, document.body.scrollHeight);");
		AmazonLog.LogInfo("Scrolled to the bottom of the page");
	}

	// Scroll to a specific element
	public void scrollToElement() {
		WebElement locator = FindElement();
		JavascriptExecutor jsExecutor = (JavascriptExecutor) AmazonBrowser.getWebDriver();
		jsExecutor.executeScript("arguments[0].scrollIntoView(true);", locator);
		AmazonLog.LogInfo("Scrolled to : " + this._locatorDescription);
	}

	// Mouse hover over a specific element
	public void mouseHover() {
		Actions actions = new Actions(AmazonBrowser.getWebDriver());
		WebElement element = FindElement();
		actions.moveToElement(element).perform();
		AmazonLog.LogInfo("Mouse hovered on element: " + this._locatorDescription);
	}

	// Scroll and take a screenshot
	public void scrollAndTakeScreenshot() {
//        String screenshotPath = AmazonLog.captureScreenShot();
		scrollToElement();
		AmazonLog.LogInfoWithImage("Scrolled to " + this._locatorDescription + " and took a screenshot");
	}

	// Scroll and take a highlighted screenshot
	public void scrollAndTakeHighlightedScreenshot() {
		scrollToElement();
		HighLightElement();
//        AmazonLog.getStatusNode().addScreenCaptureFromPath(screenshotPath,this._locatorDescription);
		AmazonLog.LogInfoWithImage(
				"Scrolled, highlighted,to " + this._locatorDescription + " and took a screenshot");
		ResetHighLightElement();
	}

}
