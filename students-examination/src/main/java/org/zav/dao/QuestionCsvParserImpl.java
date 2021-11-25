package org.zav.dao;

import com.opencsv.bean.CsvToBeanBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.zav.model.Question;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuestionCsvParserImpl implements QuestionsRepository{
    private String sourceName;

    Logger logger;

    {
        logger = Logger.getGlobal();
    }

    private static final String READING_QUESTIONS_FAILED = "Reading questions failed ";


    @Override
    public List<Question> readQuestions() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("/spring-context.xml");


        List<Question> result = null;
        Reader targetReader = null;
        try {
            Resource res = new ClassPathResource(sourceName);;
            File csv = res.getFile();
            targetReader = new FileReader(csv);

            result = new CsvToBeanBuilder<Question>(targetReader)
                    .withType(Question.class)
                    .build()
                    .parse();
            targetReader.close();

        } catch (IOException e) {
            e.printStackTrace();
            logger.log(Level.WARNING, READING_QUESTIONS_FAILED);
        }

        return result;
    }

    @Override
    public Question read(Integer id) {
        return null;
    }

    @Override
    public Integer write(Question question) {
        return null;
    }

    @Override
    public boolean delete(Integer id) {
        return false;
    }

}
