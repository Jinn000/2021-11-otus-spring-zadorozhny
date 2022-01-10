INSERT INTO BOOK (id, TITLE, GENRE_ID, AUTHORS_SET_ID) values(0, 'Вечера на хуторе близ диканьки', 0, 0);
INSERT INTO BOOK (id, TITLE, GENRE_ID, AUTHORS_SET_ID) values(1, 'Чапаев и Пустота', 1, 1);

INSERT INTO GENRE (id, GENRE_DESCRIPTION) values(0, 'Мистика');
INSERT INTO GENRE (id, GENRE_DESCRIPTION) values(1, 'Философия');

INSERT INTO AUTHOR (id, FIRST_NAME, LAST_NAME, FAMILY_NAME) values(0, 'Николай', 'Васильевич', 'Гоголь');
INSERT INTO AUTHOR (id, FIRST_NAME, LAST_NAME, FAMILY_NAME) values(1, 'Виктор', 'Олегович', 'Пелевин');
INSERT INTO AUTHOR (id, FIRST_NAME, LAST_NAME, FAMILY_NAME) values(2, 'Джа', 'Джанкоевич', 'Тряпкин');

INSERT INTO AUTHORS_SET (id, AUTHORS_SET_ID, AUTHOR_ID) values(0, 0, 0);
INSERT INTO AUTHORS_SET (id, AUTHORS_SET_ID, AUTHOR_ID) values(1, 1, 1);
INSERT INTO AUTHORS_SET (id, AUTHORS_SET_ID, AUTHOR_ID) values(2, 1, 2);