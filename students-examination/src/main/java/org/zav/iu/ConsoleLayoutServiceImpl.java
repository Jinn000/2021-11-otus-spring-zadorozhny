package org.zav.iu;

import org.springframework.stereotype.Service;

import java.util.Scanner;

/**Сервис для вывода данных на экран*/
@Service
public class ConsoleLayoutServiceImpl implements LayoutService<String, String> {
    private final Scanner scanner = new Scanner(System.in);


    @Override
    public void show(String content) {
        System.out.println(content);
    }

    @Override
    public String ask(String content) {
        System.out.println(content);
        return scanner.next();
    }
}
