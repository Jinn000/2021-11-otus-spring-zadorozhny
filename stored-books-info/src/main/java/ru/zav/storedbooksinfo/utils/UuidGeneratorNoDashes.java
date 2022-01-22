package ru.zav.storedbooksinfo.utils;

import java.util.UUID;

public class UuidGeneratorNoDashes implements UuidGenerator {

    @Override
    public String generateUuid() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replaceAll("-", "").toUpperCase();

    }
}
