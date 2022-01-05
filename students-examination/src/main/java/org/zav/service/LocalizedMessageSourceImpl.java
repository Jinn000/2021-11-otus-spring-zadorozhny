package org.zav.service;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;
import org.zav.propertysource.LocalizationSourceFromProperty;

@Getter
@Service
public class LocalizedMessageSourceImpl extends ResourceBundleMessageSource implements LocalizedMessageSource {

    private final LocaleHolder localeHolder;
    private final LocalizationSourceFromProperty localizationSourceFromProperty;


    @Autowired
    public LocalizedMessageSourceImpl(LocaleHolder localeHolder, LocalizationSourceFromProperty localizationSourceFromProperty) {
        this.localeHolder = localeHolder;
        this.localizationSourceFromProperty = localizationSourceFromProperty;

        super.setBasename(localizationSourceFromProperty.getBasename());
    }

    @Override
    public String getLocalizedMessage(String code, Object[] args, String defaultMessage) {
        return super.getMessage(code, args, defaultMessage, localeHolder.getLocale());
    }

    @Override
    public String getLocalizedMessage(String code, Object[] args) throws NoSuchMessageException {
        return super.getMessage(code, args, localeHolder.getLocale());
    }

    @Override
    public String getLocalizedMessage(MessageSourceResolvable resolvable) throws NoSuchMessageException {
        return super.getMessage(resolvable, localeHolder.getLocale());
    }

    public final void setBasename(String basename){
        super.setBasename(basename);
    }
}
