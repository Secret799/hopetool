package com.hopetool.core.column.support;

import java.io.Serializable;
import java.util.function.Function;

/**
 * 自定义序列化对象
 *
 * @param <T>
 * @param <R>
 * @author JunPzx
 */
@FunctionalInterface
public interface SFunction<T, R> extends Function<T, R>, Serializable {

}