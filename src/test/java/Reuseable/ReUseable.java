package Reuseable;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class ReUseable {
    public String getCurrentDate(){
        SimpleDateFormat ft    = new SimpleDateFormat("YYYY-MM-dd");
        String str = ft.format(new Date());
        return str;
    }

    public String read_Properties_file(String key)
    {
        Properties prop = new Properties();
        String value = null;
        try {
            prop.load(new FileInputStream(System.getProperty("user.dir") + "/AadharNo.properties"));
            value = prop.getProperty(key);

        }catch (IOException e)
        {
            e.printStackTrace();
        }
        return value;
    }

    public String createBankAccountJsonBody(String fname, String lname, String aadharNo, String address, String phone){
        String body = "{\n" +
                "    \"Fname\": \""+fname+"\",\n" +
                "    \"Lname\": \""+lname+"\",\n" +
                "    \"Aadhar_No\": \""+aadharNo+"\",\n" +
                "    \"Address\": \""+address+"\",\n" +
                "    \"Phone\": \""+phone+"\"\n" +
                "}";
        return body;
    }

    public String CreateCreditCardJsonBody(String name, String year, String cardNumber, String limit, String expDate, String cardType){
        String body = "{\n" +
                "    \"name\": \""+name+"\",\n" +
                "    \"data\": {\n" +
                "        \"year\": "+year+",\n" +
                "        \"Credit Card Number\": \""+cardNumber+"\",\n" +
                "        \"Limit\": \""+limit+"\",\n" +
                "        \"EXP Date\": \""+expDate+"\",\n" +
                "        \"Card Type\": \""+cardType+"\"\n" +
                "    }\n" +
                "}";
        return body;
    }

}
