# AcmeBank - Account Manager

## Prerequisites

For building and running the application you need: 

- [JDK 1.8](https://www.oracle.com/java/technologies/javase/javase-jdk8-downloads.html)
- [Maven 3](https://maven.apache.com)

## Installing
    mvn clean install

## Deployment and Configuration
This is a Springboot micro service application using embedded Tomcat server and H2 DB.<br/>
Execute main class Application.java to launch this account-manager micro-service.<br/>
No argument and property are required <br/>
All configuration are defined in /resources/application.yml
Port 8081 

## Services
This micro-service provides the following REST WS.  All services consumes and produces JSON format. 

######Get Account Balance
Method: GET<br/>
Endpoint: /v0/account-manager/getAccountBalance/{accountId}<br/>
Request: header accountId<br/>
Response: AmountResponse.java

######Transfer Money
Method: POST<br/>
Endpoint: /v0/account-manager/transferMoney<br/>
Request: TransferMoneyRequest.java<br/>
Response: boolean

## Swagger
Swagger UI is enabled for REST WS testing 

URL: http://localhost:8081/swagger-ui.html

## H2 DB 
H2 DB is stored in file:/db/acmebank<br/>
 
#####Table ACCOUNT
 - Store account balance with Account ID as key. 
 
#####Table AUDIT_TRAIL
 - Store all actions from REST WS
 
H2 DB console is enabled for testing.  Credential is defined in /resources/application.yml<br/>
URL: http://localhost:8081/h2-console





