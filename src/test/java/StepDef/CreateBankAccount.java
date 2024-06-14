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
    String aadharNoFromDB = "";
    String requestBody = "";
    boolean isAadharMatching;
    Response response;
    CreateDB DBobj = new CreateDB();

    @When("aadha_no from properties file matches with {string} database")
    public void aadha_no_from_properties_file_matches_with_database(String dbName) {
        databaseName = dbName;
        ReUseable readProprtyFileObj = new ReUseable();
        String adharNoFromPropertyFile = readProprtyFileObj.read_Properties_file("AadharId");
         aadharNoFromDB = DBobj.readAadharTable(dbName,adharNoFromPropertyFile,"Aadhar_No");
        try{
            //Check if aadhar number from properties file is correct
            Assert.assertEquals(aadharNoFromDB, adharNoFromPropertyFile);
            String fname = DBobj.readAadharTable(dbName,aadharNoFromDB,"first_name");
            String lname = DBobj.readAadharTable(dbName,aadharNoFromDB,"last_name");
            String address = DBobj.readAadharTable(dbName,aadharNoFromDB,"Address");
            String phone = DBobj.readAadharTable(dbName,aadharNoFromDB,"Phone");
            requestBody = readProprtyFileObj.createBankAccountJsonBody(fname,lname,aadharNoFromDB,address,phone);
        }catch(AssertionError e)
        {
            System.out.println("Aadhar not present in database ");
            System.exit(0);
        }
        System.out.println("Aadhar Number matches");
         isAadharMatching = true;
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
        String db_Fname = DBobj.readAadharTable(databaseName,aadharNoFromDB,"first_name");
        Assert.assertEquals(res_Fname,db_Fname);
        System.out.println("First name in response is: " +res_Fname);
        System.out.println("First name in database is: " +db_Fname);

    }
    @Then("match lastname in response with DB")
    public void match_lastname_in_response_with_db() {
        String res_Lname = response.getBody().jsonPath().getString("Lname");
        String db_Lname = DBobj.readAadharTable(databaseName,aadharNoFromDB,"last_name");
        Assert.assertEquals(res_Lname,db_Lname);
        System.out.println("Last name in response is: " +res_Lname);
        System.out.println("Last name in database is: " +db_Lname);
    }
    @Then("match AadharNo in response with DB")
    public void match_aadhar_no_in_response_with_db() {
        String res_aadharNo = response.getBody().jsonPath().getString("Aadhar_No");
        String db_aadharNo = DBobj.readAadharTable(databaseName,aadharNoFromDB,"Aadhar_No");
        Assert.assertEquals(res_aadharNo,db_aadharNo);
        System.out.println("AadharNo in response is: " +res_aadharNo);
        System.out.println("AadharNo in Database is: " +db_aadharNo);
    }
    @Then("match Address in response with DB")
    public void match_address_in_response_with_db() {
        String res_Address = response.getBody().jsonPath().getString("Address");
        String db_Address = DBobj.readAadharTable(databaseName,aadharNoFromDB,"Address");
        Assert.assertEquals(res_Address,db_Address);
        System.out.println("Address in response is: " +res_Address);
        System.out.println("Address in Database is: " +db_Address);
    }
    @Then("match phone in response with DB")
    public void match_phone_in_response_with_db() {
        String res_Phone = response.getBody().jsonPath().getString("Phone");
        String db_Phone = DBobj.readAadharTable(databaseName,aadharNoFromDB,"Phone");
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

/* Output:

@createDB
Scenario: Create Aadhar database and corresponding table                        # src/test/Feature/CreateBankAccount.feature:4
AadharDB database created!
  When a database with name as "AadharDB" is created                            # StepDef.CreateAadharDB.a_database_with_name_as_is_created(java.lang.String)
You are using AadharDB
AadharRecord table is created successfully
  Then using database "AadharDB" user creates table with name as "AadharRecord" # StepDef.CreateAadharDB.using_database_user_creates_table_with_name_as(java.lang.String,java.lang.String)

@insertInDB
Scenario Outline: Insert the records in the database                                                                                              # src/test/Feature/CreateBankAccount.feature:14
0
You are using AadharDB
Values inserted into AadharRecord table successfully
  And use database "AadharDB" to inserts records in table with values "111122223333","sunil","kumar","#11, Richmond City, Bangalore","9080706050" # StepDef.CreateAadharDB.use_database_to_inserts_records_in_table_with_values(java.lang.String,java.lang.String,java.lang.String,java.lang.String,java.lang.String,java.lang.String)

@insertInDB
Scenario Outline: Insert the records in the database                                                                                              # src/test/Feature/CreateBankAccount.feature:15
0
You are using AadharDB
Values inserted into AadharRecord table successfully
  And use database "AadharDB" to inserts records in table with values "222233334444","amit","pandey","#22, Richmond City, Bangalore","8070605040" # StepDef.CreateAadharDB.use_database_to_inserts_records_in_table_with_values(java.lang.String,java.lang.String,java.lang.String,java.lang.String,java.lang.String,java.lang.String)

@insertInDB
Scenario Outline: Insert the records in the database                                                                                               # src/test/Feature/CreateBankAccount.feature:16
0
You are using AadharDB
Values inserted into AadharRecord table successfully
  And use database "AadharDB" to inserts records in table with values "333344445555","Rishabh","lala","#33, Richmond City, Bangalore","7060504030" # StepDef.CreateAadharDB.use_database_to_inserts_records_in_table_with_values(java.lang.String,java.lang.String,java.lang.String,java.lang.String,java.lang.String,java.lang.String)

@insertInDB
Scenario Outline: Insert the records in the database                                                                                               # src/test/Feature/CreateBankAccount.feature:17
0
You are using AadharDB
Values inserted into AadharRecord table successfully
  And use database "AadharDB" to inserts records in table with values "444455556666","Vishal","singh","#44, Richmond City, Bangalore","6050403020" # StepDef.CreateAadharDB.use_database_to_inserts_records_in_table_with_values(java.lang.String,java.lang.String,java.lang.String,java.lang.String,java.lang.String,java.lang.String)

@insertInDB
Scenario Outline: Insert the records in the database                                                                                               # src/test/Feature/CreateBankAccount.feature:18
0
You are using AadharDB
Values inserted into AadharRecord table successfully
  And use database "AadharDB" to inserts records in table with values "555566667777","Suman","Kumari","#55, Richmond City, Bangalore","5040302010" # StepDef.CreateAadharDB.use_database_to_inserts_records_in_table_with_values(java.lang.String,java.lang.String,java.lang.String,java.lang.String,java.lang.String,java.lang.String)

@insertInDB
Scenario Outline: Insert the records in the database                                                                                                 # src/test/Feature/CreateBankAccount.feature:19
0
You are using AadharDB
Values inserted into AadharRecord table successfully
  And use database "AadharDB" to inserts records in table with values "666677778888","ShashiKant","Rai","#66, Richmond City, Bangalore","4030201090" # StepDef.CreateAadharDB.use_database_to_inserts_records_in_table_with_values(java.lang.String,java.lang.String,java.lang.String,java.lang.String,java.lang.String,java.lang.String)

@insertInDB
Scenario Outline: Insert the records in the database                                                                                              # src/test/Feature/CreateBankAccount.feature:20
0
You are using AadharDB
Values inserted into AadharRecord table successfully
  And use database "AadharDB" to inserts records in table with values "777788889999","Ratan","singh","#77, Richmond City, Bangalore","3020109080" # StepDef.CreateAadharDB.use_database_to_inserts_records_in_table_with_values(java.lang.String,java.lang.String,java.lang.String,java.lang.String,java.lang.String,java.lang.String)

@insertInDB
Scenario Outline: Insert the records in the database                                                                                             # src/test/Feature/CreateBankAccount.feature:21
0
You are using AadharDB
Values inserted into AadharRecord table successfully
  And use database "AadharDB" to inserts records in table with values "888899990000","Ram","Prasad","#88, Richmond City, Bangalore","2010908070" # StepDef.CreateAadharDB.use_database_to_inserts_records_in_table_with_values(java.lang.String,java.lang.String,java.lang.String,java.lang.String,java.lang.String,java.lang.String)

@insertInDB
Scenario Outline: Insert the records in the database                                                                                                # src/test/Feature/CreateBankAccount.feature:22
0
You are using AadharDB
Values inserted into AadharRecord table successfully
  And use database "AadharDB" to inserts records in table with values "999900001111","Soumya","Ranjan","#99, Richmond City, Bangalore","1090807060" # StepDef.CreateAadharDB.use_database_to_inserts_records_in_table_with_values(java.lang.String,java.lang.String,java.lang.String,java.lang.String,java.lang.String,java.lang.String)

@insertInDB
Scenario Outline: Insert the records in the database                                                                                               # src/test/Feature/CreateBankAccount.feature:23
0
You are using AadharDB
Values inserted into AadharRecord table successfully
  And use database "AadharDB" to inserts records in table with values "101010101111","Rahul","Nayak","#110, Richmond City, Bangalore","9988776655" # StepDef.CreateAadharDB.use_database_to_inserts_records_in_table_with_values(java.lang.String,java.lang.String,java.lang.String,java.lang.String,java.lang.String,java.lang.String)

@AdharAuthAndAccountCreation
Scenario: After aadhar varification do API call to create new bank account      # src/test/Feature/CreateBankAccount.feature:26
Aadhar Number matches
  When aadha_no from properties file matches with "AadharDB" database           # StepDef.CreateBankAccount.aadha_no_from_properties_file_matches_with_database(java.lang.String)
API call to create new bank account is completed
Response body: {"Fname":"sunil","Lname":"kumar","Aadhar_No":"111122223333","Address":"#11, Richmond City, Bangalore","Phone":"9080706050","id":"317","createdAt":"2024-06-14T18:12:24.902Z"}
  Then send post call to create bank account with "https://reqres.in/api/users" # StepDef.CreateBankAccount.send_post_call_to_create_bank_account_with(java.lang.String)
First name in response is: sunil
First name in database is: sunil
  Then read and match firstname in response with DB                             # StepDef.CreateBankAccount.read_and_match_firstname_in_response_with_db()
Last name in response is: kumar
Last name in database is: kumar
  And match lastname in response with DB                                        # StepDef.CreateBankAccount.match_lastname_in_response_with_db()
AadharNo in response is: 111122223333
AadharNo in Database is: 111122223333
  And match AadharNo in response with DB                                        # StepDef.CreateBankAccount.match_aadhar_no_in_response_with_db()
Address in response is: #11, Richmond City, Bangalore
Address in Database is: #11, Richmond City, Bangalore
  And match Address in response with DB                                         # StepDef.CreateBankAccount.match_address_in_response_with_db()
Phone in response is: 9080706050
Phone in Database is: 9080706050
  And match phone in response with DB                                           # StepDef.CreateBankAccount.match_phone_in_response_with_db()
Is account id created: true
  And validate AccountID is created in response                                 # StepDef.CreateBankAccount.validate_account_id_is_created_in_response()
Is Account Id numeric: true
  Then validate AccountID is numeric                                            # StepDef.CreateBankAccount.validate_account_id_is_numeric()
Account created at: 2024-06-14T18:12:24.902Z
  Then validate createAt in response and its date should be current date        # StepDef.CreateBankAccount.validate_create_at_in_response_and_its_date_should_be_current_date()

Process finished with exit code 0

*/

