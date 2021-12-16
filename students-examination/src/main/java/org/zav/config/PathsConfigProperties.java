package org.zav.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter @Setter
@ConfigurationProperties(prefix = "sources.path")
@Component
public class PathsConfigProperties {
    String answers;
    String questions;
    String users;
}
