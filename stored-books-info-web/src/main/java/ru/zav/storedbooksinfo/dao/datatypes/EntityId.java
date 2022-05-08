package ru.zav.storedbooksinfo.dao.datatypes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
@Setter
public class EntityId implements Serializable {
    private String idValue;
}
