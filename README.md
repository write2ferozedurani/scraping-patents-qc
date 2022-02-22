# scraping-patents-qc

Technology Stack: Spring Boot, Java 8, Angular, AmazonS3 & DynamoDB

Presentation Layer:
  UX Components are being developed using Angular (verify "scraping-patents" for code) as a different application

Busines Layer:
  Backend code is being developed using Spring Boot + Java8 (verify "patents" folder for code)

Persisent Layer:
  Patents metadata saves to DynamoDB
  Patent Documents (OCR + Original PDF) saves to AmazonS3

------------------------------------------------------------------------------------------------------------------

CI/CD implementation is being performed to merge UX layer into Resources folder of "patents" application codebase.

Steps to run the application:

  Download the code
  Update application.properties file to connect with AmazonS3 & DynamoDB
  Set environment variable TESSDATA_PREFIX=/project-location/tessdata
  peform maven installation for executable file (pom.xml - Added "scraping-patents" & "patents" codebases as modules)
  Run jar file "java -jar .\patents-0.0.1.jar"

