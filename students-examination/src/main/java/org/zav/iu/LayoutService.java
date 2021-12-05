package org.zav.iu;

@SuppressWarnings("unused")
public interface LayoutService<V,T> {
    void show(T content);
    V ask(T content);
}
