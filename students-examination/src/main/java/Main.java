import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.zav.dao.QuestionCsvParserImpl;
import org.zav.model.Question;

import java.util.List;

public class Main {

    public static void main(String[] args ){
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("/spring-context.xml");

        QuestionCsvParserImpl questionCsvParser = context.getBean("questionCsvParserImpl", QuestionCsvParserImpl.class);
        List<Question> questions = questionCsvParser.readQuestions();
        System.out.println("Прочитано " + questions.size() + " Вопросов");
    }
}
