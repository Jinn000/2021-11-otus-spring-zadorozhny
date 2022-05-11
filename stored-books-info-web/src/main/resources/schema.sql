
DROP TABLE BOOK_AUTHOR IF EXISTS;
DROP TABLE BOOK_COMMENT IF EXISTS;
DROP TABLE BOOK IF EXISTS;
DROP TABLE GENRE IF EXISTS;
DROP TABLE AUTHOR IF EXISTS;
--Жанры
CREATE TABLE GENRE(
    ID VARCHAR(36) PRIMARY KEY,
    DESCRIPTION VARCHAR(256)
);

-- Авторы
CREATE TABLE AUTHOR(
    ID VARCHAR(36) PRIMARY KEY,
    FIRST_NAME VARCHAR(64),
    LAST_NAME VARCHAR(64),
    FAMILY_NAME VARCHAR(64)
);

CREATE TABLE BOOK(
    id VARCHAR(36) PRIMARY KEY,
    TITLE VARCHAR(256),
    GENRE_ID VARCHAR(36)
);

-- Комментарии к книгам
CREATE TABLE BOOK_COMMENT(
    ID VARCHAR(36) PRIMARY KEY,
    NAME VARCHAR(64),
    BOOK_ID VARCHAR(36),
    COMMENT VARCHAR(4000)
);

create table BOOK_AUTHOR(
    BOOK_ID VARCHAR(36) references BOOK(ID) ON DELETE CASCADE,
    AUTHOR_ID VARCHAR(36) references AUTHOR(ID) ON DELETE CASCADE,
    primary key (BOOK_ID, AUTHOR_ID)
);