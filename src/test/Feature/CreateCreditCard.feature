Feature: Create credit card and validate PAN card mapping and responses


  Scenario: Create CreditCard  database and corresponding tables
    Given  a name as "CreditCardDB" create a database
    Then using this db "CreditCardDB" create a table with name as "CreditCardDetails"
    Then using same db "CreditCardDB" create a lookup table named as "PanLookup"


  Scenario Outline: Insert records into the tables
    And use "CreditCardDB" db to insert records into CreditCardDetails with values "<Name>","<Year>","<CardNumber>","<Limit>","<ExpDate>","<CardType>"
    And use "CreditCardDB" db to insert records into PanLookup with values "<CardNumber>","<PanCardNumber>"
    Examples:
      | Name | Year | CardNumber | Limit | ExpDate | CardType | PanCardNumber |
      |SUNIL |2021  |11112223331111|3.4L |07-07-24    |DINNER CLUB|AABBK1122A    |
      |MITHUN |2019  |112233445566|5L |05-05-2025|MASTER|AABBK1122B    |
      |AMIT |2020  |11112223331113|2.4L |01-02-27    |VIAS|AABBK1122C    |
      |VIPUL |2024  |11112223331114|3.3L |08-07-31    |DINNER CLUB|AABBK1122D    |
      |ARPIT |2022  |11112223331115|3.8L |07-07-28    |MASTER|AABBK1122E    |
      |VISHAL |2021  |11112223331116|6.4L |01-01-27    |VISA|AABBK1122F    |
      |RATAN |2019  |11112223331117|1.1L |09-06-24    |DINNER CLUB|AABBK1122G    |
      |RAM |2023  |11112223331118|2.8L |07-07-29    |MASTER|AABBK1122H    |
      |SUMAN |2021  |11112223331119|1.4L |07-07-25    |VISA|AABBK1122I    |
      |DURGA |2023  |11112223331120|5.5L |13-12-29    |DINNER CLUB|AABBK1122J    |

@validateresponse
  Scenario Outline: Read the card number from input file to fetch details from DB and do API post call to create credit cards and validate the responses
    Given cards numbers in XL, read the XL to get card numbers for each "<TestCase>"
    And get the required details from DB and create JSON body
    Then do API post call to create credit cards
    Then read responses and compare details with DB
    And validate each responses card number is mapped with a PAN card in DB
  Examples:
    | TestCase |
    |TC001     |
    |TC002     |
    |TC003     |
    |TC004     |
    |TC005     |
    |TC006     |
    |TC007     |
    |TC008     |
    |TC009     |
    |TC010     |
