package RunnerFile;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;
@RunWith(Cucumber.class)

@CucumberOptions(
        features = {
                "src/test/Feature/CreateBankAccount.feature"
               // "src/test/Feature/"
        } ,
        glue = "StepDef" ,
        tags = "" ,
        plugin = {
                "pretty",
                "json:target/cucumber-report/cucumber.json",
                "html:target/cucumber-report/cucumber.html"}
)

public class Runner {

}
