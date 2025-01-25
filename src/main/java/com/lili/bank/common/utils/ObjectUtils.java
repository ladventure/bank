package com.lili.bank.common.utils;

import org.springframework.beans.BeanUtils;

import java.lang.reflect.InvocationTargetException;

public class ObjectUtils {
    /**
     * convert object to target class, clazz must have default constructor.
     * It's a simple implementation, ignore reflection performance , just for test.If you want to use it in production,
     * please use BeanUtils.copyProperties() instead or add cache.
     *
     * @param obj
     * @param clazz
     * @return
     * @param <T>
     * @throws RuntimeException
     */
    public static <T> T convertObject(Object obj, Class<T> clazz) throws RuntimeException {
        if (obj == null) {
            return null;
        }
        try {
            T newObj=clazz.getConstructor().newInstance();
            BeanUtils.copyProperties(obj,newObj);
            return newObj;
        }catch (Exception e){
            throw new RuntimeException(e);
        }

    }
}
