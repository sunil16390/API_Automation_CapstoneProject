package StepDef;

import JdbcConnections.CreateDB;
import io.cucumber.java.en.*;

public class CreateAadharDB {
    @When("a database with name as {string} is created")
    public void a_database_with_name_as_is_created(String DBname) {
        CreateDB DBobj = new CreateDB();
        DBobj.createAadharDB(DBname);
    }

    @Given("using database {string} user creates table with name as {string}")
    public void using_database_user_creates_table_with_name_as(String DBname, String TableName) {
        CreateDB DBobj = new CreateDB();
        DBobj.createAadharRecordTable(DBname,TableName);
    }

    @Given("use database {string} to inserts records in table with values {string},{string},{string},{string},{string}")
    public void use_database_to_inserts_records_in_table_with_values(String dbName, String aadhar_no, String Fname, String Lname, String Address, String Phone) {
        CreateDB DBobj = new CreateDB();
        DBobj.insertRecordInTable(dbName,aadhar_no,Fname,Lname,Address,Phone);
    }
}
