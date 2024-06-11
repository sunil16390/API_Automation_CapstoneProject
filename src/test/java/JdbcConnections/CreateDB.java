package JdbcConnections;
import java.sql.*;

public class CreateDB {
    Connection connection;
    public void connectDbServer(){
        try
        {            //URL, DB username and DB password
            connection=DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306","sunilk","password");
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }
    public void createAadharDB(String dbName){
        try
        {
            connectDbServer();
            if (connection!=null) {
                Statement statement = connection.createStatement(); // creating statement obj
                if(!isDatabaseExist(dbName)){
                // statement.execute("DROP DATABASE IF EXISTS " +dbName+";");
                statement.execute(" CREATE DATABASE IF NOT EXISTS  " + dbName);//using that statement obj, creating database
                System.out.println(dbName+" database created!");
            } else {
                    System.out.println("Database "+dbName+" already exist");
                }
            }
            else { System.out.println("Database server not connected"); }
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }
    public void createAadharRecordTable(String dbName, String tableName){
        try
        {
           connectDbServer();
            if (connection!=null) {
                Statement statement = connection.createStatement(); // creating statement obj
                if(!isTableExist(dbName,tableName)){
                    statement.execute(" use " + dbName);//using that statement obj, to use database
                    System.out.println("You are using " + dbName);

                    String aadharRecordTableCreateQuery = "CREATE TABLE IF NOT EXISTS " + tableName + "(\n" +
                            "         Aadhar_No      BIGINT(12)             NOT NULL,  -- UNSIGNED AUTO_INCREMENT??\n" +
                            "         first_name  VARCHAR(25)     NOT NULL,\n" +
                            "         last_name   VARCHAR(25)     NOT NULL,\n" +
                            "         Address   VARCHAR(200)     NOT NULL,\n" +
                            "         Phone   BIGINT(10)            NOT NULL,\n" +
                            "         PRIMARY KEY (Aadhar_No)                   -- Index built automatically on primary-key column\n" +
                            "                                                -- INDEX (first_name)\n" +
                            "                                                -- INDEX (last_name)\n" +
                            "     );";
                    // statement.execute("DROP TABLE IF EXISTS "+tableName+";");
                    statement.execute(aadharRecordTableCreateQuery);
                    System.out.println(tableName+ " table is created successfully");
                }
                else {
                    System.out.println("Table already exist");
                }
            }
            else { System.out.println("Database server not connected"); }
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }
    public void insertRecordInTable(String dbName, String AadharNo, String Fname, String Lname, String address, String phone){
        try {
           connectDbServer();
            if (connection!=null) {
                if(isAadharNumberCorrect(AadharNo,dbName)==true) {
                    System.out.println("This Aadhar Number already exist");
                }
                else {
                    Statement statement = connection.createStatement(); // creating statement obj
                    statement.execute(" use "+dbName+"");//using that statement obj, to use database
                    System.out.println("You are using AadharDB");
                    String insertIntoTable = "INSERT INTO AadharRecord (Aadhar_No, first_name, last_name, Address, Phone)\n" +
                            "VALUES \n" +
                            "('" + AadharNo + "', '" + Fname + "', '" + Lname + "', '" + address + "', '" + phone + "');";
                    statement.execute(insertIntoTable);
                    System.out.println("Values inserted into AadharRecord table successfully");
                }
            }
            else {System.out.println("Database server not connected");}
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }
    public boolean isAadharNumberCorrect(String aadharNo, String dbName){
        boolean flag = false;
        try {
           connectDbServer();
            Statement statement = connection.createStatement(); // creating statement obj
            statement.execute(" use "+dbName+"");//using that statement obj, to use database
            //System.out.println("You are using AadharDB");
            ResultSet Sqlresult = statement.executeQuery("select count(*) as reowcount from AadharRecord where Aadhar_No =\""+aadharNo+"\";");
            while (Sqlresult.next())
            {
                int runtimeValue = Sqlresult.getInt("reowcount");
                System.out.println(runtimeValue);
                if (runtimeValue!=0) {
                    flag = true;
                }
            }
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
        //System.out.println(flag);
        return flag;
    }

    public boolean isDatabaseExist(String dbname){
        boolean flag = false;
        try {
            connectDbServer();
            Statement statement = connection.createStatement(); // creating statement obj
            ResultSet Sqlresult = statement.executeQuery("SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME = \""+dbname+"\";");
            while (Sqlresult.next())
            {
                String runtimeValue = Sqlresult.getString("SCHEMA_NAME");
                if (runtimeValue!=null) {
                    flag = true;
                }
            }
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
        //System.out.println(flag);
        return flag;
    }

    public boolean isTableExist(String dbName,String tableName){
        boolean flag = false;
        try {
            connectDbServer();
            Statement statement = connection.createStatement(); // creating statement obj
            ResultSet Sqlresult = statement.executeQuery("SELECT TABLE_NAME FROM information_schema.tables WHERE table_schema = \""+dbName+"\" AND table_name = \""+tableName+"\";");

            while (Sqlresult.next())
            {
                String runtimeValue = Sqlresult.getString("TABLE_NAME");
                if (runtimeValue!=null) {
                    flag = true;
                }
            }
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
        //System.out.println(flag);
        return flag;
    }
    public String readAadharTable(String dbName, String aadharNo, String fieldName){
        String result = "";
        try {
            connectDbServer();
            if (connection != null) {
                Statement statement = connection.createStatement(); // creating statement obj
                statement.execute(" use "+dbName+";");//using that statement obj, to use database
                ResultSet Sqlresult = statement.executeQuery("select "+fieldName+" from AadharRecord where Aadhar_No =\""+aadharNo+"\";");
                while (Sqlresult.next()){
                    result = Sqlresult.getString(fieldName);
                }
            }
            else {System.out.println("Database server is not connected");}
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
        return result;
    }

}

