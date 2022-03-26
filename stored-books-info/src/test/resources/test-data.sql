
INSERT INTO GENRE (id, DESCRIPTION) values('E181db60-9C0B-4EF8-BB6D-6BB9BD380A10', 'Мистика');
INSERT INTO GENRE (id, DESCRIPTION) values('E181db60-9C0B-4EF8-BB6D-6BB9BD380A11', 'Философия');
INSERT INTO GENRE (id, DESCRIPTION) values('E181db60-9C0B-4EF8-BB6D-6BB9BD380A12', 'Нонфикшн');

INSERT INTO AUTHOR (id, FIRST_NAME, LAST_NAME, FAMILY_NAME) values('A0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A10', 'Николай', 'Васильевич', 'Гоголь');
INSERT INTO AUTHOR (id, FIRST_NAME, LAST_NAME, FAMILY_NAME) values('A0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A11', 'Виктор', 'Олегович', 'Пелевин');
INSERT INTO AUTHOR (id, FIRST_NAME, LAST_NAME, FAMILY_NAME) values('A0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A12', 'Джа', 'Джанкоевич', 'Тряпкин');

INSERT INTO BOOK (id, TITLE, GENRE_ID, AUTHORS_SET_ID) values('B0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A10', 'Вечера на хуторе близ диканьки', 'E181db60-9C0B-4EF8-BB6D-6BB9BD380A10', 'ASEEBC99-9C0B-4EF8-BB6D-6BB9BD380A10');
INSERT INTO BOOK (id, TITLE, GENRE_ID, AUTHORS_SET_ID) values('B0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A11', 'Чапаев и Пустота', 'E181db60-9C0B-4EF8-BB6D-6BB9BD380A11', 'ASEEBC99-9C0B-4EF8-BB6D-6BB9BD380A11');


INSERT INTO AUTHORS_SET (ID, AUTHORS_SET_ID, AUTHOR_ID) values('ASIDBC99-9C0B-4EF8-BB6D-6BB9BD380A10', 'ASEEBC99-9C0B-4EF8-BB6D-6BB9BD380A10', 'A0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A10');
INSERT INTO AUTHORS_SET (ID, AUTHORS_SET_ID, AUTHOR_ID) values('ASIDBC99-9C0B-4EF8-BB6D-6BB9BD380A11', 'ASEEBC99-9C0B-4EF8-BB6D-6BB9BD380A11', 'A0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A11');
INSERT INTO AUTHORS_SET (ID, AUTHORS_SET_ID, AUTHOR_ID) values('ASIDBC99-9C0B-4EF8-BB6D-6BB9BD380A12', 'ASEEBC99-9C0B-4EF8-BB6D-6BB9BD380A11', 'A0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A12');

INSERT INTO BOOK_AUTHOR (BOOK_ID, AUTHOR_ID) values('B0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A10', 'A0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A10');
INSERT INTO BOOK_AUTHOR (BOOK_ID, AUTHOR_ID) values('B0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A11', 'A0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A11');
INSERT INTO BOOK_AUTHOR (BOOK_ID, AUTHOR_ID) values('B0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A11', 'A0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A12');
