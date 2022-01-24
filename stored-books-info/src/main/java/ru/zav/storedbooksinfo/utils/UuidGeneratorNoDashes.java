package ru.zav.storedbooksinfo.utils;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UuidGeneratorNoDashes implements UuidGenerator {

    @Override
    public String generateUuid() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replaceAll("-", "").toUpperCase();

    }
}
