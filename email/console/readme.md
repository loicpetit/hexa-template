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

A bat file is generated to facilitate the execution

* Generate the boot jar with gradle build
* From the project root directory, run the following command in powershell:  
  `& "email\console\build\email" -h`
* Follow the application instructions
