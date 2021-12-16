package org.zav;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.zav.service.ConsoleExaminationServiceImpl;
import org.zav.service.ExaminationService;

@SpringBootApplication
public class Main {

    public static void main(String[] args ) {
        String currentUserId;

        ConfigurableApplicationContext context = SpringApplication.run(Main.class, args);

        ExaminationService examinationService = context.getBean(ConsoleExaminationServiceImpl.class);

        currentUserId = examinationService.askUserData();
        examinationService.runExamination(currentUserId);

    }
}
