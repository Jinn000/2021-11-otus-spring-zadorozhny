package ru.zav;

import lombok.Getter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.h2.tools.Console;
import org.springframework.context.ConfigurableApplicationContext;
import ru.zav.storedbooksinfo.dao.*;
import ru.zav.storedbooksinfo.domain.Author;
import ru.zav.storedbooksinfo.domain.Book;
import ru.zav.storedbooksinfo.domain.Genre;
import ru.zav.storedbooksinfo.utils.AppDaoException;
import ru.zav.storedbooksinfo.utils.UuidGeneratorNoDashes;

import java.sql.SQLException;
import java.util.Arrays;

@SpringBootApplication
public class StoredBooksInfoApplication {

	public static void main(String[] args) throws SQLException {
		/*final ConfigurableApplicationContext applicationContext = SpringApplication.run(StoredBooksInfoApplication.class, args);

		final AuthorDao authorDao = applicationContext.getBean("authorDaoJdbc", AuthorDaoJdbc.class);
		final GenreDao genreDao = applicationContext.getBean("genreDaoJdbc", GenreDaoJdbc.class);
		final BookDao bookDao = applicationContext.getBean("bookDaoJdbc", BookDaoJdbc.class);
		final AuthorSetDaoJdbc authorSetDaoJdbc = applicationContext.getBean("authorSetDaoJdbc", AuthorSetDaoJdbc.class);
		try {
			authorSetDaoJdbc.getById("ASIDBC99-9C0B-4EF8-BB6D-6BB9BD380A10");
		} catch (AppDaoException e) {
			e.printStackTrace();
		}

		Genre newGenre = new Genre(new UuidGeneratorNoDashes().generateUuid(), "Ужасы");
		try {
			genreDao.insert(newGenre);
		} catch (AppDaoException e) {
			e.printStackTrace();
		}

		Author newAuthor = new Author(new UuidGeneratorNoDashes().generateUuid(), "Акакий", "Акакиевич", "Фролов");
		Author newAuthor2 = new Author(new UuidGeneratorNoDashes().generateUuid(), "Акакий2", "Акакиевич2", "Фролов2");
		try {
			authorDao.insert(newAuthor);
			authorDao.insert(newAuthor2);
		} catch (AppDaoException e) {
			e.printStackTrace();
		}
		Book newBook = new Book(new UuidGeneratorNoDashes().generateUuid(), "Роно", newGenre, Arrays.asList(newAuthor, newAuthor2));
		try {
			bookDao.insert(newBook);
		} catch (AppDaoException e) {
			e.printStackTrace();
		}

		newBook.setAuthors(Arrays.asList(newAuthor));
		try {
			bookDao.insert(newBook);
		} catch (AppDaoException e) {
			e.printStackTrace();
		}*/

		Console.main(args);

		SpringApplication.run(StoredBooksInfoApplication.class, args);
	}

}
