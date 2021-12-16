package org.zav.iu;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

/**Сервис для вывода данных на экран*/
@Service
public class ConsoleLayoutServiceImpl implements LayoutService<String, String> {
    private final Scanner scanner;
    private final PrintStream printStream;

    public ConsoleLayoutServiceImpl(@Value("#{ T(java.lang.System).in}") InputStream inputStream, @Value("#{ T(java.lang.System).out}") PrintStream printStream) {
        this.scanner = new Scanner(inputStream);
        this.printStream = printStream;
    }

    @Override
    public void show(String content) {
        printStream.println(content);
    }

    @Override
    public String ask(String content) {
        printStream.println(content);
        return scanner.next();
    }
}
