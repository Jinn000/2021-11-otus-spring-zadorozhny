package org.zav.propertysource;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter @Setter
@Component
@ConfigurationProperties
public class ClassUsingProperty {

    private final Sources sources = new Sources();
    private String answerDescription;
    private final Questions questions = new Questions();


    //------------------------------------------------------

    @Getter
    public class Sources{
        private final Path path = new Path();
    }

    @Getter @Setter
    public class Path{
        private String answers;
        private String questions;
        private String users;
    }

    @Getter @Setter
    public class Questions{
        private int count;
    }
}
