# GraphQL module

Ce module expose une API GraphQL qui orchestre les micro services.

## Build et démarrer l'application

* Depuis la racine du projet
* Build `gradle :graphql:clean :graphql:build`
* Démarrer `& "$($env:JAVA_HOME)\bin\java.exe" -jar "-Dspring.profiles.active=local" "graphql/build/libs/graphql-1.0.0-SNAPSHOT.jar"`
