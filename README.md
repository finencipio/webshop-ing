# Webshop demo spring project

## Instalacija spring projekta

```shell
mvn clean install
```

## Povlaćenje docker imagea

```shell
docker pull finencipio/pgimg:latest
docker run -p 5438:5432  finencipio/pgimg:latest
```

## Pokretanje aplikacije

```shell
java -jar target/Webshop*.jar
```
dovoljno je i
```shell
java -jar target/*.jar
```
