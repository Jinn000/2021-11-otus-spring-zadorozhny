package ru.zav;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.h2.tools.Console;

import java.sql.SQLException;

@SpringBootApplication
public class StoredBooksInfoApplication {

	public static void main(String[] args) throws SQLException {
		SpringApplication.run(StoredBooksInfoApplication.class, args);

		Console.main(args);
	}

}
