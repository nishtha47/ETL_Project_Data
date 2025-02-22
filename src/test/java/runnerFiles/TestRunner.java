package runnerFiles;

import io.cucumber.testng.AbstractTestNGCucumberTests;

import io.cucumber.testng.CucumberOptions;


@CucumberOptions(features = {"src/test/java/cucumber_maven/featurestest"},
        glue = {"stepDefinitions"},
        tags = "@api",
        plugin = {"pretty", "html:target/cucumber-reports.html"},
       monochrome = true
)

public class TestRunner extends AbstractTestNGCucumberTests  {
	


}