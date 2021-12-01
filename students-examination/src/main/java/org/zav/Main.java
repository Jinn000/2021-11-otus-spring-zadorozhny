package org.zav;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.zav.service.DataLayoutService;
import org.zav.service.ScreenLayoutServiceImpl;

@PropertySource("classpath:application.properties")
@SpringBootApplication
public class Main {

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfig(){
        return new PropertySourcesPlaceholderConfigurer();
    }

    public static void main(String[] args ){
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Main.class);
        DataLayoutService dataLayoutService = context.getBean(ScreenLayoutServiceImpl.class);

        dataLayoutService.showQuestionsWithAnswers();
    }
}
