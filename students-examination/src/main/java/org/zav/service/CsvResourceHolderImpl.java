package org.zav.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CsvResourceHolderImpl implements ResourceHolder {
    private final LocaleHolder localeHolder;
    private final MessageSource messageSource;


    @Override
    @Nullable
    public Resource getResource(@NonNull String propertyName){
        /*собираю путь для файла ресурсов, соответствующего текущей локализации*/
        String resourcePath = messageSource.getMessage(propertyName, null, localeHolder.getLocale());
        Resource res;
        res = new ClassPathResource(resourcePath);
        return res;
    }
}
