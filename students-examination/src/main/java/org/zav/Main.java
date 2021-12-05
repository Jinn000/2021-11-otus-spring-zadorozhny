package org.zav;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.zav.service.ConsoleExaminationServiceImpl;
import org.zav.service.ExaminationService;

@PropertySource("classpath:application.properties")
@SpringBootApplication
public class Main {

    public static void main(String[] args ) {
        String currentUserId;
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Main.class);

        ExaminationService examinationService = context.getBean(ConsoleExaminationServiceImpl.class);

        currentUserId = examinationService.askUserData();
        examinationService.runExamination(currentUserId);
    }
}
