package org.zav.propertysource;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter @Setter
@Component
@ConfigurationProperties(prefix = "spring.messages")
public class LocalizationSourceFromProperty {
    private String basename;

}
