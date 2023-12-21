package com.hopetool.core.column;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hopetool.core.column.support.SFunction;

import java.lang.annotation.Annotation;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author JunPzx
 * @since 2023/2/20 16:29
 */
public class ColumnUtil {

    /**
     * 默认配置
     */
    static String defaultSplit = "";

    static Integer defaultToType = 0;

    /**
     * 获取实体类的字段名称
     *
     * @param fn     实体get方法
     * @param split  分隔符，多个字母自定义分隔符
     * @param toType 转换方式，多个字母以大小写方式返回 0.不做转换 1.大写 2.小写
     * @param <T>    T
     */
    public static <T> String getJsonPropertyName(SFunction<T, ?> fn, String split, Integer toType) {
        if (split == null) {
            split = defaultSplit;
        }
        if (toType == null) {
            toType = defaultToType;
        }
        Field field = getFieldByGetFunction(fn);
        return getJsonPropertyName(field, split, toType);
    }

    public static <T extends Annotation> T getAnnotationByGetFunction(SFunction<?, ?> fn, Class<T> annotationClazz) {
        Field field = getFieldByGetFunction(fn);
        field.setAccessible(true);
        return field.getAnnotation(annotationClazz);
    }


    public static <T extends Annotation, V> V getAnnotationByGetFunction(SFunction<?, ?> fn, Class<T> annotationClazz, SFunction<T, V> annotationFn) {
        Field field = getFieldByGetFunction(fn);
        field.setAccessible(true);
        T annotation = field.getAnnotation(annotationClazz);
        return annotationFn.apply(annotation);
    }

    /**
     * 通过get函数获取字段
     *
     * @param fn get函数
     * @return 字段
     */
    public static Field getFieldByGetFunction(SFunction<?, ?> fn) {
        SerializedLambda serializedLambda = getSerializedLambda(fn);
        // 从lambda信息取出method、field、class等
        String fieldName = serializedLambda.getImplMethodName().substring("get".length());
        fieldName = fieldName.replaceFirst(fieldName.charAt(0) + "", (fieldName.charAt(0) + "").toLowerCase());
        Field field;
        try {
            field = Class.forName(serializedLambda.getImplClass().replace("/", ".")).getDeclaredField(fieldName);
        } catch (ClassNotFoundException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        return field;
    }


    /**
     * 获取实体类的字段名称
     *
     * @param field  字段
     * @param split  分隔符，多个字母自定义分隔符
     * @param toType 转换方式，多个字母以大小写方式返回 0.不做转换 1.大写 2.小写
     * @return 字段名称
     */
    public static String getJsonPropertyName(Field field, String split, Integer toType) {
        // 从field取出字段名，可以根据实际情况调整
        JsonProperty jsonProperty = field.getAnnotation(JsonProperty.class);
        if (jsonProperty != null && jsonProperty.value().length() > 0) {
            return jsonProperty.value();
        } else {
            //0.不做转换 1.大写 2.小写
            switch (toType) {
                case 1:
                    return field.getName().replaceAll("[A-Z]", split + "$0").toUpperCase();
                case 2:
                    return field.getName().replaceAll("[A-Z]", split + "$0").toLowerCase();
                default:
                    return field.getName().replaceAll("[A-Z]", split + "$0");
            }
        }
    }

    public static <T> String getFieldName(SFunction<T, ?> fn) {
        SerializedLambda serializedLambda = getSerializedLambda(fn);
        // 从lambda信息取出method、field、class等
        String fieldName = serializedLambda.getImplMethodName().substring("get".length());
        fieldName = fieldName.replaceFirst(fieldName.charAt(0) + "", (fieldName.charAt(0) + "").toLowerCase());
        return fieldName;
    }

    private static <T> SerializedLambda getSerializedLambda(SFunction<T, ?> fn) {
        // 从function取出序列化方法
        Method writeReplaceMethod;
        try {
            writeReplaceMethod = fn.getClass().getDeclaredMethod("writeReplace");
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        // 从序列化方法取出序列化的lambda信息
        boolean isAccessible = writeReplaceMethod.isAccessible();
        writeReplaceMethod.setAccessible(true);
        SerializedLambda serializedLambda;
        try {
            serializedLambda = (SerializedLambda) writeReplaceMethod.invoke(fn);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        writeReplaceMethod.setAccessible(isAccessible);
        return serializedLambda;
    }
}