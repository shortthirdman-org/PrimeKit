package com.shortthirdman.primekit.essentials.generics.webmvc;

import com.shortthirdman.primekit.essentials.generics.annotations.Email;
import com.shortthirdman.primekit.essentials.generics.annotations.Required;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.ValidationException;
import java.lang.reflect.Field;

/**
 * @author ShortThirdMan
 * @version 1.0.0
 */
public final class FormBinder {

    private FormBinder() {}

    /**
     * @param request the {@link HttpServletRequest} object
     * @param clazz the {@link Class}
     * @param <T> the generic datatype
     * @return the instance
     * @throws Exception
     */
    public static <T> T bind(HttpServletRequest request, Class<T> clazz) throws Exception {
        T instance = clazz.getDeclaredConstructor().newInstance();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            String value = request.getParameter(field.getName());

            if (value == null) continue;

            Class<?> type = field.getType();

            if (type == String.class) {
                field.set(instance, value);
            } else if (type == int.class || type == Integer.class) {
                field.set(instance, Integer.parseInt(value));
            } else if (type == boolean.class || type == Boolean.class) {
                field.setBoolean(instance, "on".equalsIgnoreCase(value));
                field.set(instance, "on".equalsIgnoreCase(value) || "true".equalsIgnoreCase(value));
            } else if (type == double.class || type == Double.class) {
                field.set(instance, Double.parseDouble(value));
            } else {
                field.set(instance, value);
            }
        }
        return instance;
    }

    /**
     * @param obj the object to validate
     * @throws ValidationException
     */
    public static void validate(Object obj) throws ValidationException {
        for (Field field : obj.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            Object value;
            try {
                value = field.get(obj);
            } catch (IllegalAccessException e) {
                continue;
            }

            if (field.isAnnotationPresent(Required.class)) {
                if (value == null || (value instanceof String s && s.trim().isEmpty())) {
                    throw new ValidationException(field.getName() + " is required");
                }
            }

            if (field.isAnnotationPresent(Email.class)) {
                if (value instanceof String s && !s.contains("@")) {
                    throw new ValidationException(field.getName() + " must be a valid email");
                }
            }
        }
    }
}
