# 2021-11-otus-spring-zadorozhny

## Домашнее задание 5:

Цель: Создать приложение хранящее информацию о книгах в библиотеке

Цель: использовать возможности Spring JDBC и spring-boot-starter-jdbc для подключения к реляционным базам данных
Результат: приложение с хранением данных в реляционной БД, которое в дальнейшем будем развивать

Это домашнее задание выполняется НЕ на основе предыдущего.

    Использовать Spring JDBC и реляционную базу (H2 или настоящую реляционную БД). Настоятельно рекомендуем использовать NamedParametersJdbcTemplate
    Предусмотреть таблицы авторов, книг и жанров.
    Предполагается отношение многие-к-одному (у книги один автор и жанр). Опциональное усложнение - отношения многие-ко-многим (у книги может быть много авторов и/или жанров).
    Интерфейс выполняется на Spring Shell (CRUD книги обязателен, операции с авторами и жанрами - как будет удобно).
    Скрипт создания таблиц и скрипт заполнения данными должны автоматически запускаться с помощью spring-boot-starter-jdbc.
    Написать тесты для всех методов DAO и сервиса работы с книгами.

Рекомендации к выполнению работы:

    НЕ делать AbstractDao.
    НЕ делать наследования в тестах

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.6.2/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.6.2/maven-plugin/reference/html/#build-image)

