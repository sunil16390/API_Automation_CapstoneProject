package JdbcConnections;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class CreditCardDB {
    Connection connection;
    public void connectDbServer(){
        try
        {            //URL, DB username and DB password
            connection= DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306","sunilk","password");
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }
    public void createCreditCardDB(String dbName){
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
    public void createCreditCardDetailsTable(String dbName, String tableName){
        try
        {
            connectDbServer();
            if (connection!=null) {
                Statement statement = connection.createStatement(); // creating statement obj
                if(!isTableExist(dbName,tableName)){
                    statement.execute(" use " + dbName);//using that statement obj, to use database
                    System.out.println("You are using " + dbName);

                    String tableCreateQuery = "CREATE TABLE IF NOT EXISTS " + tableName + "(\n" +
                            "         CreditCardNumber      BIGINT(16)             NOT NULL,  \n" +
                            "         Name  VARCHAR(60)     NOT NULL,\n" +
                            "         Year   VARCHAR(8)     NOT NULL,\n" +
                            "         CreditLimit   VARCHAR(10)     NOT NULL,\n" +
                            "         ExpDate   VARCHAR(12)            NOT NULL,\n" +
                            "         CardType   VARCHAR(25)     NOT NULL,\n" +
                            "         PRIMARY KEY (CreditCardNumber)        -- Index built automatically on primary-key column\n" +
                            "                                                -- INDEX (Name)\n" +
                            "                                                -- INDEX (Year)\n" +
                            "     );";
                    // statement.execute("DROP TABLE IF EXISTS "+tableName+";");
                    statement.execute(tableCreateQuery);
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
    public void createPanLookupTable(String dbName, String tableName){
        try
        {
            connectDbServer();
            if (connection!=null) {
                Statement statement = connection.createStatement(); // creating statement obj
                if(!isTableExist(dbName,tableName)){
                    statement.execute(" use " + dbName);//using that statement obj, to use database
                    System.out.println("You are using " + dbName);

                    String tableCreateQuery = "CREATE TABLE IF NOT EXISTS " + tableName + "(\n" +
                            "         CreditCardNumber      BIGINT(16)             NOT NULL,  \n" +
                            "         PanCardNumber  VARCHAR(10)     NOT NULL,\n" +
                            "         PRIMARY KEY (CreditCardNumber)        -- Index built automatically on primary-key column\n" +
                            "                                                -- INDEX (PanCardNumber)\n" +
                            "     );";
                    // statement.execute("DROP TABLE IF EXISTS "+tableName+";");
                    statement.execute(tableCreateQuery);
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
    public void insertRecordIntoTable(String dbName, String Name, String Year, String CreditCardNumber, String Limit, String ExpDate, String CardType){
        try {
            connectDbServer();
            if (connection!=null) {
                if(isCardNumberExist(CreditCardNumber,dbName,"CreditCardDetails")==true) {
                    System.out.println("This Credit Card Number already exist");
                }
                else {
                    Statement statement = connection.createStatement(); // creating statement obj
                    statement.execute(" use "+dbName+"");//using that statement obj, to use database
                    System.out.println("You are using " + dbName);
                    String insertIntoTable = "INSERT INTO CreditCardDetails (CreditCardNumber, Name, Year, CreditLimit, ExpDate, CardType)\n" +
                            "VALUES \n" +
                            "('" + CreditCardNumber + "', '" + Name + "', '" + Year + "', '" + Limit + "', '" + ExpDate + "', '" + CardType + "');";
                    statement.execute(insertIntoTable);
                    System.out.println("Values inserted into CreditCardDetails table successfully");
                }
            }
            else {System.out.println("Database server not connected");}
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }
    public void insertRecordIntoTable(String dbName, String CreditCardNumber, String PancardNumber){
        try {
            connectDbServer();
            if (connection!=null) {
                if(isCardNumberExist(CreditCardNumber,dbName,"PanLookup")==true) {
                    System.out.println("This CreditCardNumber already exist");
                }
                else {
                    Statement statement = connection.createStatement(); // creating statement obj
                    statement.execute(" use "+dbName+"");//using that statement obj, to use database
                    String insertIntoTable = "INSERT INTO PanLookup (CreditCardNumber, PanCardNumber)\n" +
                            "VALUES \n" +
                            "('" + CreditCardNumber + "', '" + PancardNumber + "');";
                    statement.execute(insertIntoTable);
                    System.out.println("Values inserted into PanLookup table successfully");
                }
            }
            else {System.out.println("Database server not connected");}
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }
    public boolean isCardNumberExist(String CreditCardNumber, String dbName, String tableName){
        boolean flag = false;
        try {
            connectDbServer();
            Statement statement = connection.createStatement(); // creating statement obj
            statement.execute(" use "+dbName+"");//using that statement obj, to use database
            ResultSet Sqlresult = statement.executeQuery("select count(*) as reowcount from "+tableName+" where CreditCardNumber =\""+CreditCardNumber+"\";");
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
    public String readTable(String dbName,String tableName, String CreditCardNumber, String fieldName){
        String result = null;
        try {
            connectDbServer();
            if (connection != null) {
                if(isCardNumberExist(CreditCardNumber,dbName,tableName)==true) {


                    Statement statement = connection.createStatement(); // creating statement obj
                    statement.execute(" use " + dbName + ";");//using that statement obj, to use database
                    ResultSet Sqlresult = statement.executeQuery("select " + fieldName + " from " + tableName + " where CreditCardNumber =\"" + CreditCardNumber + "\";");
                    while (Sqlresult.next()) {
                        result = Sqlresult.getString(fieldName);
                    }
                }
                else {
                    System.out.println("Credit card not exist");
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
