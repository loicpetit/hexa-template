# Email API

## Generate jar

To generate the boot jar, from the project root directory, run the following command in the terminal:

```
gradle clean :email:api:build
```

Two jars will be generated in the `email/api/build/libs` directory:
* `email-api-VERSION.jar` - the boot jar, which can be executed with `java -jar`
* `email-api-VERSION-plain.jar` - the classic jar, which can be used as a dependency in another application

## Start the application

* Generate the boot jar with gradle build
* From the project root directory, run the following command in powershell:  
  ` & "$($env:JAVA_HOME)\bin\java.exe" -jar "-Dspring.profiles.active=local" "email/api/build/libs/email-api-0.0.1-SNAPSHOT.jar"`    
  Point to the correct JDK in your JAVA_HOME or change the command ;)  
  Adapt the version as well

## Requests

Tests requests are available with Intellij HTTP Client.
You can find them in `email/api/requests/http` directory.

You can copy the test resource `users.properties` in main resources to have test users available.
