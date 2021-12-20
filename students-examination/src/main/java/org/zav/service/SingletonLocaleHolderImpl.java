package org.zav.service;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Getter
@Service
@Scope("singleton")
public class SingletonLocaleHolderImpl implements LocaleHolder {
    private static SingletonLocaleHolderImpl instance;
    @Setter
    @Accessors(chain = true)
    private Locale locale;

    private SingletonLocaleHolderImpl(@Value("${default-locale}") String localeCode){
        this.locale = Locale.forLanguageTag(localeCode);
    }

    public static SingletonLocaleHolderImpl getInstance() {
        SingletonLocaleHolderImpl localInstance = instance;
        if (localInstance == null) {
            synchronized (SingletonLocaleHolderImpl.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new SingletonLocaleHolderImpl("${default-locale}");
                }
            }
        }
        return localInstance;
    }
}
