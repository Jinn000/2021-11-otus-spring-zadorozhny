package org.zav.service;

import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;
import org.springframework.lang.Nullable;

public interface LocalizedMessageSource {

    @Nullable
    String getLocalizedMessage(String code, @Nullable Object[] args, @Nullable String defaultMessage);

    String getLocalizedMessage(String code, @Nullable Object[] args) throws NoSuchMessageException;

    String getLocalizedMessage(MessageSourceResolvable resolvable) throws NoSuchMessageException;
}
