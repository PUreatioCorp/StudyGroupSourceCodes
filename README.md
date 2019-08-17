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
