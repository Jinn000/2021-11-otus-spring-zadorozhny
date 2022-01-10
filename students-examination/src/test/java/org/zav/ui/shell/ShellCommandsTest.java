package org.zav.ui.shell;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.shell.Shell;
import org.zav.Main;
import org.zav.dao.BaseRepository;
import org.zav.model.UserResult;
import org.zav.utils.exceptions.AppDaoException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest(classes = Main.class)
public class ShellCommandsTest {
    @MockBean
    private final BaseRepository<UserResult> userResultRepositoryMock;
    private final Shell shell;

    @Autowired
    public ShellCommandsTest(BaseRepository<UserResult> userResultRepositoryMock, Shell shell) {
        this.userResultRepositoryMock = userResultRepositoryMock;
        this.shell = shell;
    }

    @DisplayName("Тестирование отображения результатов экзамена")
    @Test
    public void getResultsCommandTest() throws AppDaoException {
        Mockito.when(userResultRepositoryMock.readAll()).thenReturn(
                List.of(UserResult.builder().id("0").name("Igor").familyName("Riurikovich").validAnswerCount("0").build(),
                        UserResult.builder().id("1").name("Ingvar").familyName("Riurikovich").validAnswerCount("4").build())
        );

        List<UserResult> res =  (List<UserResult>) shell.evaluate(() -> "rs");
        assertThat(res.get(1)).isEqualTo(UserResult.builder()
                .id("1")
                .name("Ingvar")
                .familyName("Riurikovich")
                .validAnswerCount("4")
                .build()
        );

        verify(userResultRepositoryMock, times(1)).readAll();
    }
}
