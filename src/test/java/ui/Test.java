package ui;

import Amazon.utilities.TestInfo;
import Base.AmazonData;
import Base.BaseTest;

public class Test extends BaseTest {
	
	
@org.testng.annotations.Test()
@TestInfo(ExcelFileName = "AmazonUserData",SheetName = "Sample",DataKey = "Default_1")
	public void test() {
		//Perform General Actions
		dashBoardPage.generalActions();
		System.out.println(AmazonData.getInputData("Name"));
		System.out.println(AmazonData.getInputData("Password"));
		
	}

}
