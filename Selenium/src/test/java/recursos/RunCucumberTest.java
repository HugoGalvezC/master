package recursos;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.junit.runner.RunWith;
import io.cucumber.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/prueba",
        glue = "prueba",
        tags = "@test",
        plugin = {"pretty", "html:target/cucumber-reports/CucumberTests.html",
                "json:target/cucumber-reports/CucumberTests.json",
                "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"}
)

public class RunCucumberTest extends AbstractTestNGCucumberTests {
}
