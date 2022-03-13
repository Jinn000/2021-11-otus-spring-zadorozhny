package ru.zav.storedbooksinfo.ui;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

@Service
public class ConsoleLayoutServiceImpl implements LayoutService<String, String> {
    private final Scanner scanner;
    private final PrintStream printStream;

    public ConsoleLayoutServiceImpl(@Value("#{ T(java.lang.System).in}") InputStream inputStream, @Value("#{ T(java.lang.System).out}") PrintStream printStream) {
        this.scanner = new Scanner(inputStream);
        this.printStream = printStream;
    }

    @Override
    public void show(String message) {
        printStream.println(message);
    }

    @Override
    public String ask(String message) {
        if(StringUtils.isNoneBlank(message)) show(message);
        return scanner.nextLine();
    }
}
