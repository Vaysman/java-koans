package ru.javalux.koan;

public class Koan {
    private Class clazz;
    private String source;

    public Koan(Class clazz, String source) {
        this.clazz = clazz;
        this.source = source;
    }

    public Class getClazz() {
        return clazz;
    }

    public String getSource() {
        return source;
    }
}
