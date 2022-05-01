package ru.zav.storedbooksinfo.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.List;

@ChangeLog
public class DatabaseChangelog {
    @ChangeSet(order = "0001", id = "dropDb_0105220207", author = "zav", runAlways = true)
    public void dropDb_0105220207(MongoDatabase db) {
        db.drop();
    }

    @ChangeSet(order = "0002", id = "fillDb_0105220208", author = "zav")
    public void fillDb_0105220208(MongoDatabase db) {
        MongoCollection<Document> genreCollection = db.getCollection("genre");
        List<Document> docs = List.of(
                new Document().append("_id", "E181db60-9C0B-4EF8-BB6D-6BB9BD380A10").append("description", "Мистика")
                ,new Document().append("_id", "E181db60-9C0B-4EF8-BB6D-6BB9BD380A11").append("description", "Философия")
                ,new Document().append("_id", "E181db60-9C0B-4EF8-BB6D-6BB9BD380A12").append("description", "Нонфикшн"));
        genreCollection.insertMany(docs);
    }

    /*
INSERT INTO AUTHOR (id, FIRST_NAME, LAST_NAME, FAMILY_NAME) values('A0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A10', 'Николай', 'Васильевич', 'Гоголь');
INSERT INTO AUTHOR (id, FIRST_NAME, LAST_NAME, FAMILY_NAME) values('A0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A11', 'Виктор', 'Олегович', 'Пелевин');
INSERT INTO AUTHOR (id, FIRST_NAME, LAST_NAME, FAMILY_NAME) values('A0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A12', 'Джа', 'Джанкоевич', 'Тряпкин');
INSERT INTO AUTHOR (id, FIRST_NAME, LAST_NAME, FAMILY_NAME) values('A0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A13', 'Стивен', '', 'Кинг');
INSERT INTO AUTHOR (id, FIRST_NAME, LAST_NAME, FAMILY_NAME) values('A0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A14', 'Михаил', 'Афанасьевич', 'Булгаков');
INSERT INTO AUTHOR (id, FIRST_NAME, LAST_NAME, FAMILY_NAME) values('A0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A15', 'Эдгар Аллан', '', 'По');
INSERT INTO AUTHOR (id, FIRST_NAME, LAST_NAME, FAMILY_NAME) values('A0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A16', 'Питер', '', 'Страуб');
* */
    @ChangeSet(order = "0003", id = "fillDb_0105220220", author = "zav")
    public void fillDb_0105220220(MongoDatabase db) {
        MongoCollection<Document> authorCollection = db.getCollection("author");
        List<Document> docs = List.of(
                new Document().append("_id", "A0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A10").append("firstName", "Николай").append("lastName", "Васильевич").append("familyName", "Гоголь")
                ,new Document().append("_id", "A0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A11").append("firstName", "Виктор").append("lastName", "Олегович").append("familyName", "Пелевин")
                ,new Document().append("_id", "A0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A12").append("firstName", "Джа").append("lastName", "Джанкоевич").append("familyName", "Тряпкин")
                ,new Document().append("_id", "A0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A13").append("firstName", "Стивен").append("lastName", "").append("familyName", "Кинг")
                ,new Document().append("_id", "A0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A14").append("firstName", "Михаил").append("lastName", "Афанасьевич").append("familyName", "Булгаков")
                ,new Document().append("_id", "A0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A15").append("firstName", "Эдгар Аллан").append("lastName", "").append("familyName", "По")
                ,new Document().append("_id", "A0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A16").append("firstName", "Питер").append("lastName", "").append("familyName", "Страуб"));
        authorCollection.insertMany(docs);
    }

    /*
INSERT INTO BOOK_COMMENT (id, NAME, BOOK_ID, COMMENT) values('BCEEBC99-9C0B-4EF8-BB6D-6BB9BD380A10', 'Николай', 'B0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A10', 'Not bad.');
INSERT INTO BOOK_COMMENT (id, NAME, BOOK_ID, COMMENT) values('BCEEBC99-9C0B-4EF8-BB6D-6BB9BD380A11', 'Сергей', 'B0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A10', 'Не читал, но осуждаю.');
* */
    @ChangeSet(order = "0004", id = "fillDb_0105220230", author = "zav")
    public void fillDb_0105220230(MongoDatabase db) {
        MongoCollection<Document> commentCollection = db.getCollection("book_comment");
        List<Document> docs = List.of(
                new Document().append("_id", "BCEEBC99-9C0B-4EF8-BB6D-6BB9BD380A10").append("name", "Николай").append("bookId", "B0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A10").append("comment", "Not bad.")
                ,new Document().append("_id", "BCEEBC99-9C0B-4EF8-BB6D-6BB9BD380A11").append("name", "Сергей").append("bookId", "B0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A10").append("comment", "Не читал, но осуждаю."));
        commentCollection.insertMany(docs);
    }

    /*
INSERT INTO BOOK (id, TITLE, GENRE_ID) values('B0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A10', 'Вечера на хуторе близ диканьки', 'E181db60-9C0B-4EF8-BB6D-6BB9BD380A10');
INSERT INTO BOOK (id, TITLE, GENRE_ID) values('B0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A11', 'Чапаев и Пустота', 'E181db60-9C0B-4EF8-BB6D-6BB9BD380A11');
INSERT INTO BOOK (id, TITLE, GENRE_ID) values('B0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A12', 'Сияние', 'E181db60-9C0B-4EF8-BB6D-6BB9BD380A10');
INSERT INTO BOOK (id, TITLE, GENRE_ID) values('B0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A13', 'Мастер и Маргарита', 'E181db60-9C0B-4EF8-BB6D-6BB9BD380A10');
INSERT INTO BOOK (id, TITLE, GENRE_ID) values('B0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A14', 'Зеленая миля', 'E181db60-9C0B-4EF8-BB6D-6BB9BD380A10');
INSERT INTO BOOK (id, TITLE, GENRE_ID) values('B0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A15', 'Оно', 'E181db60-9C0B-4EF8-BB6D-6BB9BD380A10');
INSERT INTO BOOK (id, TITLE, GENRE_ID) values('B0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A16', 'Доктор Сон', 'E181db60-9C0B-4EF8-BB6D-6BB9BD380A10');
INSERT INTO BOOK (id, TITLE, GENRE_ID) values('B0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A17', 'Буря столетия', 'E181db60-9C0B-4EF8-BB6D-6BB9BD380A10');
INSERT INTO BOOK (id, TITLE, GENRE_ID) values('B0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A18', 'Заживо погребенные', 'E181db60-9C0B-4EF8-BB6D-6BB9BD380A10');
INSERT INTO BOOK (id, TITLE, GENRE_ID) values('B0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A19', 'Талисман', 'E181db60-9C0B-4EF8-BB6D-6BB9BD380A10');
* */

    @ChangeSet(order = "0005", id = "fillDb_0105220235", author = "zav")
    public void fillCollectionBook(MongoDatabase db) {
        MongoCollection<Document> bookCollection = db.getCollection("book");
        List<Document> docs = List.of(
                new Document().append("_id", "B0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A10").append("title", "Вечера на хуторе близ диканьки").append("GENRE_ID", "E181db60-9C0B-4EF8-BB6D-6BB9BD380A10")
                ,new Document().append("_id", "B0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A11").append("title", "Чапаев и Пустота").append("GENRE_ID", "E181db60-9C0B-4EF8-BB6D-6BB9BD380A11")
                ,new Document().append("_id", "B0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A12").append("title", "Сияние").append("GENRE_ID", "E181db60-9C0B-4EF8-BB6D-6BB9BD380A10")
                ,new Document().append("_id", "B0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A13").append("title", "Мастер и Маргарита").append("GENRE_ID", "E181db60-9C0B-4EF8-BB6D-6BB9BD380A10")
                ,new Document().append("_id", "B0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A14").append("title", "Зеленая миля").append("GENRE_ID", "E181db60-9C0B-4EF8-BB6D-6BB9BD380A10")
                ,new Document().append("_id", "B0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A15").append("title", "Оно").append("GENRE_ID", "E181db60-9C0B-4EF8-BB6D-6BB9BD380A10")
                ,new Document().append("_id", "B0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A16").append("title", "Доктор Сон").append("GENRE_ID", "E181db60-9C0B-4EF8-BB6D-6BB9BD380A10")
                ,new Document().append("_id", "B0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A17").append("title", "Буря столетия").append("GENRE_ID", "E181db60-9C0B-4EF8-BB6D-6BB9BD380A10")
                ,new Document().append("_id", "B0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A18").append("title", "Заживо погребенные").append("GENRE_ID", "E181db60-9C0B-4EF8-BB6D-6BB9BD380A10")
                ,new Document().append("_id", "B0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A19").append("title", "Талисман").append("GENRE_ID", "E181db60-9C0B-4EF8-BB6D-6BB9BD380A10"));
        bookCollection.insertMany(docs);
    }
}
