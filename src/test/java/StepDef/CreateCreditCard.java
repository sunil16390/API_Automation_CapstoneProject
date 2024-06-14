package StepDef;

import JdbcConnections.CreditCardDB;
import Reuseable.ReUseable;
import Reuseable.ReadFromXL;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.*;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Assert;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;


import static io.restassured.RestAssured.given;

public class CreateCreditCard {
    private static ExtentSparkReporter spark;
    private static ExtentReports extent;
    private static ExtentTest logger;
    CreditCardDB DbObj = new CreditCardDB();
    String creditCardNumber = "";
    String requestBody = "";
    String endpoinURL = "https://api.restful-api.dev/objects";
    Response response;

    @Before
    public static void setUpExtent(){
        if(extent == null) {
            extent = new ExtentReports();
        }
        spark = new ExtentSparkReporter(System.getProperty("user.dir")+"/ExtentReport/TestReport.html"); //current path of the project using System.getProperty
        spark.config().setDocumentTitle("Create Credit Card API Post call validation");
        spark.config().setReportName("Test Report of Credit Card Creation POST call");
        spark.config().setTheme(Theme.STANDARD);
        extent.attachReporter(spark);
        extent.setSystemInfo("QA Name", "Sunil");
        extent.setSystemInfo("Build name", "16.3.2");
        extent.setSystemInfo("ENV Name", "NAM UAT-2");
        logger = extent.createTest("Read card number from excel, read corresponding card details from database, do POST call to create the credit cards and validate");
    }
    @Given("a name as {string} create a database")
    public void a_name_as_create_a_database(String dbName) {

        DbObj.createCreditCardDB(dbName);
    }
    @Then("using this db {string} create a table with name as {string}")
    public void using_this_db_create_a_table_with_name_as(String dbName, String tableName) {

        logger.info("This is for creating table to hold credit card details");
        DbObj.createCreditCardDetailsTable(dbName,tableName);

    }
    @Then("using same db {string} create a lookup table named as {string}")
    public void using_same_db_create_a_lookup_table_named_as(String dbName, String tableName) {
        logger.info("This is for creating table to store credit card and and PAN mapping records");
        DbObj.createPanLookupTable(dbName,tableName);
    }
    @Given("use {string} db to insert records into CreditCardDetails with values {string},{string},{string},{string},{string},{string}")
    public void use_db_to_insert_records_into_credit_card_details_with_values(String dbName, String CreditCardNumber, String Name, String Year, String Limit, String ExpDate, String CardType) {
        logger.info("This is for inserting credit cards details to the table via feature file");
        DbObj.insertRecordIntoTable(dbName,CreditCardNumber,Name,Year,Limit,ExpDate,CardType);
    }
    @Given("use {string} db to insert records into PanLookup with values {string},{string}")
    public void use_db_to_insert_records_into_pan_lookup_with_values(String dbName, String CreditCardNumber, String PancardNumber) {
        logger.info("This is for inserting credit cards and PAN card mapping into a lookup table via feature file");
        DbObj.insertRecordIntoTable(dbName,CreditCardNumber,PancardNumber);
    }

    @Given("cards numbers in XL, read the XL to get card numbers for each {string}")
    public void cards_numbers_in_xl_read_the_xl_to_get_card_numbers_for_each(String testCase) {
        ReadFromXL ReadXlObj = new ReadFromXL();
        logger.info("This is for reading the test data (credit card number) from excel when testcase number is provided in feature file and get the required details from DB");
        creditCardNumber = ReadXlObj.read_And_Print_XL_AsPerTestData(testCase,"Credit Card Number");
    }
    @Given("get the required details from DB and create JSON body")
    public void get_the_required_details_from_db_and_create_json_body() {
        boolean isCardExistInDB = DbObj.isCardNumberExist(creditCardNumber,"CreditCardDB","CreditCardDetails");
        if(isCardExistInDB==true) {
            String name = DbObj.readTable("CreditCardDB", "CreditCardDetails", creditCardNumber, "Name");
            String year = DbObj.readTable("CreditCardDB", "CreditCardDetails", creditCardNumber, "Year");
            String limit = DbObj.readTable("CreditCardDB", "CreditCardDetails", creditCardNumber, "CreditLimit");
            String expDate = DbObj.readTable("CreditCardDB", "CreditCardDetails", creditCardNumber, "ExpDate");
            String cardType = DbObj.readTable("CreditCardDB", "CreditCardDetails", creditCardNumber, "CardType");
            ReUseable ReUseable = new ReUseable();
            requestBody = ReUseable.CreateCreditCardJsonBody(name,year,creditCardNumber,limit,expDate,cardType);
        }
        else {
              System.out.println("Credit card number \""+creditCardNumber+"\" not exist in database");

        }
    }
    @Then("do API post call to create credit cards")
    public void do_api_post_call_to_create_credit_cards() {

        logger.info("POST API Url is: "+"https://api.restful-api.dev/objects");

            response = given().contentType(ContentType.JSON).body(requestBody).when().post(endpoinURL);
            System.out.println("API call to create new credit card is completed");
            System.out.println("Response body: " + response.getBody().asString());

    }
    @Then("read responses and compare details with DB")
    public void read_responses_and_compare_details_with_db() {
        logger.info("This is for validating responses got on POST call and comparing with DB");
        // validate name
        String res_name = response.getBody().jsonPath().getString("name");
        String db_name = DbObj.readTable("CreditCardDB", "CreditCardDetails", creditCardNumber, "Name");
        Assert.assertEquals(res_name,db_name);
        logger.pass("Name in response is: " +res_name);
        logger.pass("Name in database is: " +db_name);

        // validate year
        String res_year = response.getBody().jsonPath().getString("data.year");
        String db_year = DbObj.readTable("CreditCardDB", "CreditCardDetails", creditCardNumber, "Year");
        Assert.assertEquals(res_year,db_year);
        logger.pass("year in response is: " +res_year);
        logger.pass("year in database is: " +db_year);

        // validate Credit Card Number
        String res_cardNumber = response.getBody().jsonPath().getString("data[\"Credit Card Number\"]");
        String db_cardNumber = DbObj.readTable("CreditCardDB", "CreditCardDetails", creditCardNumber, "CreditCardNumber");
        Assert.assertEquals(res_cardNumber,db_cardNumber);
        logger.pass("Credit Card Number in response is: " +res_cardNumber);
        logger.pass("Credit Card Number in database is: " +db_cardNumber);

        // validate Limit
        String res_Limit = response.getBody().jsonPath().getString("data.Limit");
        String db_Limit = DbObj.readTable("CreditCardDB", "CreditCardDetails", creditCardNumber, "CreditLimit");
        Assert.assertEquals(res_Limit,db_Limit);
        logger.pass("Limit in response is: " +res_Limit);
        logger.pass("Limit in database is: " +db_Limit);

        // validate EXP Date
        String res_ExpDate = response.getBody().jsonPath().getString("data[\"EXP Date\"]");
        String db_ExpDate = DbObj.readTable("CreditCardDB", "CreditCardDetails", creditCardNumber, "ExpDate");
        Assert.assertEquals(res_ExpDate,db_ExpDate);
        logger.pass("EXP Date in response is: " +res_ExpDate);
        logger.pass("EXP Date in database is: " +db_ExpDate);

        // validate Card Type
        String res_CardType = response.getBody().jsonPath().getString("data[\"Card Type\"]");
        String db_CardType = DbObj.readTable("CreditCardDB", "CreditCardDetails", creditCardNumber, "CardType");
        logger.pass("Card Type in response is: " +res_CardType);
        logger.pass("Card Type in database is: " +db_CardType);
        Assert.assertEquals(res_CardType,db_CardType);

    }
    @Then("validate each responses card number is mapped with a PAN card in DB")
    public void validate_each_responses_card_number_is_mapped_with_a_pan_card_in_db() {

        String res_cardNumber = response.getBody().jsonPath().getString("data[\"Credit Card Number\"]");
        if(res_cardNumber!=null) {
            String db_PanCardNumber = DbObj.readTable("CreditCardDB", "PanLookup", res_cardNumber, "PanCardNumber");
            if (db_PanCardNumber != null) {
                logger.pass("PAN card number of credit card " + res_cardNumber + " is: " + db_PanCardNumber);
                Assert.assertNotNull(db_PanCardNumber);
            } else {
                System.out.println("PAN card is not mapped");
            }
        }
    }
    @After
    public void closeExtent(){
        extent.flush();
    }
}
