# bike-service – Zależności Maven (pom.xml)

## Koordynaty projektu
```xml
groupId: pl.javasolutions.apps
artifactId: bike-service
version: 1.0-SNAPSHOT
parent: spring-boot-starter-parent 3.4.5
Java: 25
```

## Aktywne zależności

## Zarządzanie wersjami (dependencyManagement – nadpisanie wersji Spring Boot)
- spring-core: 6.2.11
- jackson-core: 2.21.1
- spring-context: 6.2.7
- tomcat-embed-core: 11.0.21
- spring-beans: 6.2.10
- commons-lang3: 3.18.0
- logback-core/classic: 1.5.25
- assertj-core: 3.27.7

## Build
- Plugin: `spring-boot-maven-plugin` (repackage, layered JAR)
