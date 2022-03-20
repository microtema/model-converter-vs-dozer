package de.microtema.dozer;

import org.dozer.CustomConverter;

public class Enum2StringConverter implements CustomConverter {
    @Override
    public Object convert(Object o, Object o1, Class<?> aClass, Class<?> aClass1) {
        return o1.toString();
    }
}
