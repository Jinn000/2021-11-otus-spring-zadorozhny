package org.zav.iu.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.zav.dao.BaseRepository;
import org.zav.iu.LayoutService;
import org.zav.model.UserResult;
import org.zav.service.ExaminationService;
import org.zav.utils.exceptions.AppDaoException;

import java.util.ArrayList;
import java.util.List;

@ShellComponent
@RequiredArgsConstructor
public class ShellCommands {
    private final LayoutService<String, String> layoutService;
    private final ExaminationService examinationService;
    private final BaseRepository<UserResult> userResultRepository;

    @ShellMethodAvailability(value = "true")
    @ShellMethod(key = {"start", "s"}, value = "Запуск тестирования")
    public void startExamination(){
        examinationService.run();
    }

    @ShellMethodAvailability(value = "true")
    @ShellMethod(key = {"results", "rs"}, value = "Показать все результаты тестирования.")
    public List<UserResult> showUsersResult(){
        List<UserResult> userResults = new ArrayList<>();
        try {
            userResults = userResultRepository.readAll();
        } catch (AppDaoException e) {
            layoutService.show("ошибка. Не удалось прочитать данные пользователей. Причина: " + e.getLocalizedMessage());
            return userResults;
        }
        if(userResults.isEmpty()){
            layoutService.show("Результаты пока отсутствуют.");
        }else{
            userResults.forEach(result-> layoutService.show(result.toString()));
        }
        return userResults;
    }

}
