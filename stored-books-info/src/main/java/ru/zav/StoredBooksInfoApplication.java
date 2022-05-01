package ru.zav;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import ru.zav.storedbooksinfo.dao.GenreRepository;

import java.sql.SQLException;

@EnableMongoRepositories
@SpringBootApplication
public class StoredBooksInfoApplication {

	public static void main(String[] args) throws SQLException {
		final ConfigurableApplicationContext applicationContext = SpringApplication.run(StoredBooksInfoApplication.class, args);

		final GenreRepository genreRepository = applicationContext.getBean(GenreRepository.class);
		final long count = genreRepository.count();
	}

}
