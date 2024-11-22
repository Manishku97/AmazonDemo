package Amazon.Controls;

import Amazon.enumFile.LocatorType;

public class TextLocator extends BaseLocator{

	public TextLocator(LocatorType locatorType, String locatorValue, String locatorDescription, String elementType) {
		super(locatorType, locatorValue, locatorDescription, "Text box");
		// TODO Auto-generated constructor stub
	}
	public TextLocator(LocatorType locatorType, String locatorValue, String locatorDescription) {
		super(locatorType, locatorValue, locatorDescription,"Text box");
		// TODO Auto-generated constructor stub
	}
	/**
	 * To Replace % symbol with Dynamic Value for Locator Field
	 * 
	 * 
	 **/
	
	public TextLocator replaceLocator(String replaceValue) {
		super.replaceLocator(replaceValue);
		return this;
		
	}
	public void ClearText() {
		super.ClearText();
	}
	public void TypeText(String value) {
		super.TypeText(value);
	}

}
