package utilities;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// Define custom annotation
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD) // Applicable only to methods
public @interface TestInfo {
    String ExcelFileName();
    String SheetName();
    String DataKey();
}
