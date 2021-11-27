package org.zav;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.zav.service.DataLayoutService;
import org.zav.service.ScreenLayoutServiceImpl;

public class Main {

    public static void main(String[] args ){
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("/spring-context.xml");

        DataLayoutService dataLayoutService = context.getBean("screenLayoutServiceImpl", ScreenLayoutServiceImpl.class);

        dataLayoutService.showQuestionsWithAnswers();
    }
}
