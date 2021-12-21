package org.zav.propertysource;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter @Setter
@Component
@ConfigurationProperties(prefix = "ui.message-code")
public class UiMessagesFromProperty {

    private String enterYourNameCode;
    private String enterYourFamilyNameCode;
    private String cannotSaveDataSorryCode;
    private String questionIdMissingCode;
    private String cantGetUserDataErrorCode;
    private String yourResultIsCode;
    private String goodCode;
    private String youAreMistakenTheCorrectAnswerCode;
}
