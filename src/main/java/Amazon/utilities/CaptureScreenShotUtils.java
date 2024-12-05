package Amazon.utilities;

import java.util.Date;
import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import Base.BaseTestAuto;

public class CaptureScreenShotUtils extends BaseTestAuto {
	
	public void getScreenShot() throws IOException {
		Date currentDate =new Date();
		String screenshotFileName=currentDate.toString().replace(" ", "-").replace(":", "-");
		File screenShotFile=((TakesScreenshot)getWebDriver()).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(screenShotFile, new File(".//screenshot//" + screenshotFileName + ".png"));
		
	}

}
