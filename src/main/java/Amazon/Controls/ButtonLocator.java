package Amazon.Controls;

import Amazon.enumFile.LocatorType;

public class ButtonLocator extends BaseLocator {

	public ButtonLocator(LocatorType locatorType, String locatorValue, String locatorDescription, String elementType) {
		super(locatorType, locatorValue, locatorDescription, "Button");
		// TODO Auto-generated constructor stub
	}
	public ButtonLocator replaceLocator(String replaceValue) {
		super.replaceLocator(replaceValue);
		return this;
	}

}
