# StudyGroupSourceCodes
Study Group Source Codes.

## 19. Azure Functions Sample
This is a sample of the 19th issue on Azure Functions.  
The sample is roughly divided into the following two.  
1. DemoApplication --- Angular8 + SpringBoot2 sample application.
2. DemoAzureFunctions --- sample Azure Functions for Java.

Please note that the test code is not implemented.

To run the sample, you need to modify the following two points.  
1. [`Application` application.properties](https://github.com/PUreatioCorp/StudyGroupSourceCodes/blob/master/19_AzureFunctions/DemoApplication/src/main/resources/application.properties)
   - You need to enter the Azure Functions connection URL.
2. [`Azure Functions` application.properties](https://github.com/PUreatioCorp/StudyGroupSourceCodes/blob/master/19_AzureFunctions/DemoAzureFunctions/src/main/resources/application.properties)
   - You need to enter the database URL to connect to.
   - Azure Database (MSSQLSERVER) is used for the verification database.

The operation procedure is described below (Environment construction should be performed by each person).  
1. Move to the Angular source folder (`main/angular/demoapp`) and execute the following command.
   - `ng build`
2. Start DemoApplication as Spring Boot App.
3. Start DemoAzureFunctions as Azure Functions.
4. Connect to the DemoApplication screen.

## 20. LINE Messaging API Sample
This is a sample of the 20th issue on LINE Messaging API.  
In LINE Messaging API, it is a sample using the broadcast message transmission API.

To run the sample, you need to modify one point.
- [`API Access Token`](https://github.com/PUreatioCorp/StudyGroupSourceCodes/blob/a4c4b80b9fab03a761db42cf67fc5ab1620e171b/20_LineMessagingAPISample/src/main/java/com/pureatio/line/api/sample/LineAPISample.java#L28)
  - You need to enter the API Token for your LINE official account.

When executing, there is no problem if you can execute the main method of LineAPISample class.

## 21. LINE Chatbot Sample
This is a sample of the 21th issue on LINE Chatbot.  
If you enter a Japanese postal code, the address will be returned to the bot.

The following API is used for address search.
- http://zipcloud.ibsnet.co.jp/doc/api

Since it is created in the AWS Lambda project, please deploy to your own environment when actually using it.

## 22. LINE Chatbot Sample2
This is a sample of the 22th issue on LINE Chatbot.  
Performs conversion between character strings and QR codes and ZXing is used for QR code conversion.
- https://github.com/zxing/zxing

Since it is created in the AWS Lambda project, please deploy to your own environment when actually using it.  
Since Amazon S3 and RDS are used for data storage, preparation is also required for the left.

The environment variables that need to be set are as follows.
- QRCODE_URL_BASE
  - URL that is the base when putting files to Amazon S3.
- S3_BUCKET_NAME
  - Amazon S3 bucket name.
- S3_KEY_FORMAT
  - Format for generating Amazon S3 key names. If you place the file in the data folder, it will be `data/%s`.
- CONNECTION_STRING
  - Amazon RDS connection string. Using MySQL5.X.
- USER_ID
  - Amazon RDS login user name.
- PASSWORD
  - Amazon RDS login password.
- LINE_ACCESS_TOKEN
  - You need to enter the API Token for your LINE official account.

## 24. Amazon Lex Sample
This is sample of the 24th issue on Amazon Lex.  
Enter the Japanese postal code in LexBotSample and press the send button to call the bot in Amazon Lex.

The following API is used for address search.
- http://zipcloud.ibsnet.co.jp/doc/api

The sample is roughly divided into the following two.  
1. LexBotSample --- C# desktop sample application.
2. LexChatBotSample --- sample AWS Lambda project for Java.

In order to run LexBotSample, AWS environment settings such as region are required in the App.config file, so please set appropriately.

## 25. Nadeshiko Sample
This is sample of the 25th issue on Nadeshiko.  
Enter the Japanese postal code in Nadeshiko and press the send button to call the bot in Amazon Lex.

Please refer to the following page for Nadeshiko.
- https://nadesi.com/top/

The sample is roughly divided into the following two.  
1. Nadeshiko --- Nadeshiko desktop sample application and group sample.
2. LexChatBotSample --- sample AWS Lambda project for Java. It is almost the same as the 24th one.

In order to run LexBotSample, AWS environment settings such as region are required in the Demo.nako file, so please set appropriately.
