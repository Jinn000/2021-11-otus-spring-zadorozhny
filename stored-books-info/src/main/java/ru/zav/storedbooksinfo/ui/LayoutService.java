package ru.zav.storedbooksinfo.ui;

public interface LayoutService <T, V>{
    void show(T message);
    V ask(T message);
}
