# Email Console

## Generate jar

To generate the boot jar, from the project root directory, run the following command in the terminal:

```
gradle clean :email:console:build
```

Two jars will be generated in the `email/console/build/libs` directory:
* `email-console-VERSION.jar` - the boot jar, which can be executed with `java -jar`
* `email-console-VERSION-plain.jar` - the classic jar, which can be used as a dependency in another application

## Run the application

* Generate the boot jar with gradle build
* From the project root directory, run the following command in powershell:  
  `& "$($env:JAVA_HOME)\bin\java.exe" -jar "email/console/build/libs/email-console-1.0.0-SNAPSHOT.jar"`    
  Point to the correct JDK in your JAVA_HOME or change the command ;)  
  Adapt the version as well
* Follow the application instructions
