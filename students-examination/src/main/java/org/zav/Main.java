package org.zav;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.zav.dao.UserCsvParserImpl;
import org.zav.model.User;
import org.zav.service.DataLayoutService;
import org.zav.service.ScreenLayoutServiceImpl;
import org.zav.utils.exceptions.AppDaoException;

import java.io.FileNotFoundException;
import java.rmi.server.UID;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@PropertySource("classpath:application.properties")
@SpringBootApplication
public class Main {

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfig(){
        return new PropertySourcesPlaceholderConfigurer();
    }

    public static void main(String[] args ) throws AppDaoException, FileNotFoundException {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Main.class);
        DataLayoutService dataLayoutService = context.getBean(ScreenLayoutServiceImpl.class);

        UserCsvParserImpl userCsvParser = context.getBean(UserCsvParserImpl.class);
        User user = userCsvParser.readById(9);
        Integer newId = userCsvParser.writeEntity(user.setId(10));
        dataLayoutService.showQuestionsWithAnswers();
    }
}
