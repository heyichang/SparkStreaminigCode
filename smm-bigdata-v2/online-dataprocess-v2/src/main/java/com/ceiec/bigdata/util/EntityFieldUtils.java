package com.ceiec.bigdata.util;

import java.lang.reflect.Field;
import java.util.Map;

public class EntityFieldUtils {

    public static void getEntityMap(Object o, Map<String, Object> map) throws IllegalAccessException {
        Class clazz = o.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.get(o) != null) {
                map.put(field.getName(), field.get(o));
            }

        }
    }

}
