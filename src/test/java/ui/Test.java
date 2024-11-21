package ui;

import Base.AmazonData;
import Base.BaseTest;
import Base.BaseTestAuto;
import Base.DashBoardPage;

public class Test extends BaseTest {
	
	
@org.testng.annotations.Test()
	public void test() {		
		//Perform General Actions
		dashBoardPage.generalActions();
		System.out.println(AmazonData.getInputData("Default_1","Comments"));
		
	}

}
