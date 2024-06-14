Feature: Verifying Aadhar records and creating bank accounts

@createDB
Scenario: Create Aadhar database and corresponding table
  When a database with name as "AadharDB" is created
  Then using database "AadharDB" user creates table with name as "AadharRecord"


@insertInDB
Scenario Outline: Insert the records in the database
  And use database "AadharDB" to inserts records in table with values "<Aadhar_No>","<Fname>","<Lname>","<Address>","<Phone>"
  Examples:
    |Aadhar_No|Fname|Lname|Address|Phone|
    |111122223333|sunil|kumar|#11, Richmond City, Bangalore|9080706050|
    |222233334444|amit|pandey|#22, Richmond City, Bangalore|8070605040|
    |333344445555|Rishabh|lala|#33, Richmond City, Bangalore|7060504030|
    |444455556666|Vishal|singh|#44, Richmond City, Bangalore|6050403020|
    |555566667777|Suman|Kumari|#55, Richmond City, Bangalore|5040302010|
    |666677778888|ShashiKant|Rai|#66, Richmond City, Bangalore|4030201090|
    |777788889999|Ratan|singh|#77, Richmond City, Bangalore|3020109080|
    |888899990000|Ram|Prasad|#88, Richmond City, Bangalore|2010908070|
    |999900001111|Soumya|Ranjan|#99, Richmond City, Bangalore|1090807060|
    |101010101111|Rahul|Nayak|#110, Richmond City, Bangalore|9988776655|

@AdharAuthAndAccountCreation
Scenario: After aadhar varification do API call to create new bank account
  When aadha_no from properties file matches with "AadharDB" database
  Then send post call to create bank account with "https://reqres.in/api/users"
  Then read and match firstname in response with DB
  And match lastname in response with DB
  And match AadharNo in response with DB
  And match Address in response with DB
  And match phone in response with DB
  And validate AccountID is created in response
  Then validate AccountID is numeric
  Then validate createAt in response and its date should be current date


