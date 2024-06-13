package StepDef;

import JdbcConnections.CreateDB;
import Reuseable.ReUseable;
import io.cucumber.java.en.*;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Assert;
import static io.restassured.RestAssured.*;


public class CreateBankAccount {

    String databaseName = "";
    boolean isAadharMatching;
    String fname = "";
    String lname = "";
    String adharNoFromPropertyFile = "";
    String address = "";
    String phone = "";
    String requestBody = "";
    Response response;
    CreateDB DBobj = new CreateDB();


    @When("aadha_no from properties file matches with {string} database")
    public void aadha_no_from_properties_file_matches_with_database(String dbName) {
        databaseName = dbName;
        ReUseable readProprtyFileObj = new ReUseable();
        adharNoFromPropertyFile = readProprtyFileObj.read_Properties_file("AadharId");
        isAadharMatching = DBobj.isAadharNumberCorrect(adharNoFromPropertyFile,dbName);
        if (isAadharMatching == true) {
            System.out.println("Aadha_no matches!");
        }
        else {
            System.out.println("Aadhar_no not matching!");
            System.exit(0);
        }
    }

    @Given("applicant firstname {string}, lastname {string}, address {string} and phone {string}")
    public void applicant_firstname_lastname_address_and_phone(String firstname, String lastname, String applicant_address, String applicant_phone) {
      fname = firstname;
      lname = lastname;
      address = applicant_address;
      phone = applicant_phone;
    }

    @Then("create request body for API call")
    public void create_request_body_for_api_call() {
        requestBody = "{\n" +
                "    \"Fname\": \""+fname+"\",\n" +
                "    \"Lname\": \""+lname+"\",\n" +
                "    \"Aadhar_No\": \""+adharNoFromPropertyFile+"\",\n" +
                "    \"Address\": \""+address+"\",\n" +
                "    \"Phone\": \""+phone+"\"\n" +
                "}";
    }
    @Then("send post call to create bank account with {string}")
    public void send_post_call_to_create_bank_account_with(String endpointURL) {
        if (isAadharMatching == true) {
            response = given().contentType(ContentType.JSON).body(requestBody).when().post(endpointURL);
            System.out.println("API call to create new bank account is completed");
            System.out.println("Response body: " + response.getBody().asString());
        }
    }
    @Then("read and match firstname in response with DB")
    public void read_and_match_firstname_in_response_with_db() {
        String res_Fname = response.getBody().jsonPath().getString("Fname");
        String db_Fname = DBobj.readAadharTable(databaseName,adharNoFromPropertyFile,"first_name");
        Assert.assertEquals(res_Fname,db_Fname);
        System.out.println("First name in response is: " +res_Fname);
        System.out.println("First name in database is: " +db_Fname);

    }
    @Then("match lastname in response with DB")
    public void match_lastname_in_response_with_db() {
        String res_Lname = response.getBody().jsonPath().getString("Lname");
        String db_Lname = DBobj.readAadharTable(databaseName,adharNoFromPropertyFile,"last_name");
        Assert.assertEquals(res_Lname,db_Lname);
        System.out.println("Last name in response is: " +res_Lname);
        System.out.println("Last name in database is: " +db_Lname);
    }
    @Then("match AadharNo in response with DB")
    public void match_aadhar_no_in_response_with_db() {
        String res_aadharNo = response.getBody().jsonPath().getString("Aadhar_No");
        String db_aadharNo = DBobj.readAadharTable(databaseName,adharNoFromPropertyFile,"Aadhar_No");
        Assert.assertEquals(res_aadharNo,db_aadharNo);
        System.out.println("AadharNo in response is: " +res_aadharNo);
        System.out.println("AadharNo in Database is: " +db_aadharNo);
    }
    @Then("match Address in response with DB")
    public void match_address_in_response_with_db() {
        String res_Address = response.getBody().jsonPath().getString("Address");
        String db_Address = DBobj.readAadharTable(databaseName,adharNoFromPropertyFile,"Address");
        Assert.assertEquals(res_Address,db_Address);
        System.out.println("Address in response is: " +res_Address);
        System.out.println("Address in Database is: " +db_Address);
    }
    @Then("match phone in response with DB")
    public void match_phone_in_response_with_db() {
        String res_Phone = response.getBody().jsonPath().getString("Phone");
        String db_Phone = DBobj.readAadharTable(databaseName,adharNoFromPropertyFile,"Phone");
        Assert.assertEquals(res_Phone,db_Phone);
        System.out.println("Phone in response is: " +res_Phone);
        System.out.println("Phone in Database is: " +db_Phone);
    }
    @Then("validate AccountID is created in response")
    public void validate_account_id_is_created_in_response() {
        boolean isAccountIdCreated = response.getBody().asString().contains("id");
        System.out.println("Is account id created: "+isAccountIdCreated);
        Assert.assertTrue(isAccountIdCreated);
    }
    @Then("validate AccountID is numeric")
    public void validate_account_id_is_numeric() {
        String res_id = response.getBody().jsonPath().getString("id");
        boolean idIsNumeric = res_id.chars().allMatch( Character::isDigit );
        System.out.println("Is Account Id numeric: "+idIsNumeric);
        Assert.assertTrue(idIsNumeric);

    }
    @Then("validate createAt in response and its date should be current date")
    public void validate_create_at_in_response_and_its_date_should_be_current_date() {
        String createAt = response.getBody().jsonPath().getString("createdAt");
        ReUseable ReUseable= new ReUseable();
        String expectedDate = ReUseable.getCurrentDate();
        Assert.assertTrue(createAt.contains(expectedDate));
        System.out.println("Account created at: "+createAt);
    }
}
