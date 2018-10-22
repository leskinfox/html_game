## Простая HTML игра, без использования JavaScript

###Требования:  
* JDK 8
* Maven 3
* MySQL

Для указания параметров MySQL сервера используйте файл **my_sql.ini** 

###Сборка и запуск:
```
mvn clean compile exec:java
```
Игра будет доступна по адресу **localhost:8080**

###Создание JAR-файла:

```
mvn compile assembly:single
java -jar html_game.jar
```