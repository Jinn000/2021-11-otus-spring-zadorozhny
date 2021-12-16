package org.zav;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.zav.service.ConsoleExaminationServiceImpl;
import org.zav.service.ExaminationService;

@SpringBootApplication
public class Main {

    public static void main(String[] args ) {
        ExaminationService examinationService = SpringApplication.run(Main.class, args).getBean(ConsoleExaminationServiceImpl.class);

        examinationService.run();
    }
}
