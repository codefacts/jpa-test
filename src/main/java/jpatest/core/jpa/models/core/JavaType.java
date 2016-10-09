package jpatest.core.jpa.models.core;

/**
 * Created by Jango on 9/15/2016.
 */
public enum JavaType {
    INTEGER(Integer.class),
    LONG(Long.class),
    FLOAT(Float.class),
    DOUBLE(Double.class),
    BOOLEAN(Boolean.class),
    STRING(String.class);

    private final Class aClass;

    JavaType(Class aClass) {
        this.aClass = aClass;
    }

    public Class getaClass() {
        return aClass;
    }

    public static JavaType of(Class aClass) {
        return aClass == Integer.class ? INTEGER
            : aClass == Long.class ? LONG
            : aClass == Float.class ? FLOAT
            : aClass == Double.class ? DOUBLE
            : aClass == Boolean.class ? BOOLEAN
            : aClass == String.class ? STRING : null;
    }
}
