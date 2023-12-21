package com.hopetool.core.util;

import com.hopetool.core.text.StrUtils;

import java.util.function.Function;
import java.util.function.Supplier;

public class ObjectUtils {
    /**
     * 检查对象是否为null<br>
     * 判断标准为：
     *
     * <pre>
     * 1. == null
     * 2. equals(null)
     * </pre>
     *
     * @param obj 对象
     * @return 是否为null
     */
    public static boolean isNull(Object obj) {
        //noinspection ConstantConditions
        return null == obj || obj.equals(null);
    }

    /**
     * 检查对象是否不为null
     * <pre>
     * 1. != null
     * 2. not equals(null)
     * </pre>
     *
     * @param obj 对象
     * @return 是否为非null
     */
    public static boolean isNotNull(Object obj) {
        //noinspection ConstantConditions
        return null != obj && !obj.equals(null);
    }

    /**
     * 如果给定对象为{@code null}返回默认值
     *
     * <pre>
     * ObjectUtil.defaultIfNull(null, null)      = null
     * ObjectUtil.defaultIfNull(null, "")        = ""
     * ObjectUtil.defaultIfNull(null, "zz")      = "zz"
     * ObjectUtil.defaultIfNull("abc", *)        = "abc"
     * ObjectUtil.defaultIfNull(Boolean.TRUE, *) = Boolean.TRUE
     * </pre>
     *
     * @param <T>          对象类型
     * @param object       被检查对象，可能为{@code null}
     * @param defaultValue 被检查对象为{@code null}返回的默认值，可以为{@code null}
     * @return 被检查对象为{@code null}返回默认值，否则返回原值
     * @since 3.0.7
     */
    public static <T> T defaultIfNull(final T object, final T defaultValue) {
        return isNull(object) ? defaultValue : object;
    }

    /**
     * 如果被检查对象为 {@code null}， 返回默认值（由 defaultValueSupplier 提供）；否则直接返回
     *
     * @param source               被检查对象
     * @param defaultValueSupplier 默认值提供者
     * @param <T>                  对象类型
     * @return 被检查对象为{@code null}返回默认值，否则返回自定义handle处理后的返回值
     * @throws NullPointerException {@code defaultValueSupplier == null} 时，抛出
     * @since 5.7.20
     */
    public static <T> T defaultIfNull(T source, Supplier<? extends T> defaultValueSupplier) {
        if (isNull(source)) {
            return defaultValueSupplier.get();
        }
        return source;
    }

    /**
     * 如果被检查对象为 {@code null}， 返回默认值（由 defaultValueSupplier 提供）；否则直接返回
     *
     * @param source               被检查对象
     * @param defaultValueSupplier 默认值提供者
     * @param <T>                  对象类型
     * @return 被检查对象为{@code null}返回默认值，否则返回自定义handle处理后的返回值
     * @throws NullPointerException {@code defaultValueSupplier == null} 时，抛出
     * @since 5.7.20
     */
    public static <T> T defaultIfNull(T source, Function<T, ? extends T> defaultValueSupplier) {
        if (isNull(source)) {
            return defaultValueSupplier.apply(null);
        }
        return source;
    }

    /**
     * 如果给定对象为{@code null} 返回默认值, 如果不为null 返回自定义handle处理后的返回值
     *
     * @param source       Object 类型对象
     * @param handle       非空时自定义的处理方法
     * @param defaultValue 默认为空的返回值
     * @param <T>          被检查对象为{@code null}返回默认值，否则返回自定义handle处理后的返回值
     * @return 处理后的返回值
     * @since 5.4.6
     * @deprecated 当str为{@code null}时，handle使用了str相关的方法引用会导致空指针问题
     */
    @Deprecated
    public static <T> T defaultIfNull(Object source, Supplier<? extends T> handle, final T defaultValue) {
        if (isNotNull(source)) {
            return handle.get();
        }
        return defaultValue;
    }

    /**
     * 如果给定对象为{@code null} 返回默认值, 如果不为null 返回自定义handle处理后的返回值
     *
     * @param <T>          被检查对象为{@code null}返回默认值，否则返回自定义handle处理后的返回值
     * @param <R>          被检查的对象类型
     * @param source       Object 类型对象
     * @param handle       非空时自定义的处理方法
     * @param defaultValue 默认为空的返回值
     * @return 处理后的返回值
     * @since 5.4.6
     */
    public static <T, R> T defaultIfNull(R source, Function<R, ? extends T> handle, final T defaultValue) {
        if (isNotNull(source)) {
            return handle.apply(source);
        }
        return defaultValue;
    }


    /**
     * 如果给定对象为{@code null}或者""或者空白符返回默认值
     *
     * <pre>
     * ObjectUtil.defaultIfBlank(null, null)      = null
     * ObjectUtil.defaultIfBlank(null, "")        = ""
     * ObjectUtil.defaultIfBlank("", "zz")      = "zz"
     * ObjectUtil.defaultIfBlank(" ", "zz")      = "zz"
     * ObjectUtil.defaultIfBlank("abc", *)        = "abc"
     * </pre>
     *
     * @param <T>          对象类型（必须实现CharSequence接口）
     * @param str          被检查对象，可能为{@code null}
     * @param defaultValue 被检查对象为{@code null}或者 ""或者空白符返回的默认值，可以为{@code null}或者 ""或者空白符
     * @return 被检查对象为{@code null}或者 ""或者空白符返回默认值，否则返回原值
     * @since 5.0.4
     */
    public static <T extends String> T defaultIfBlank(final T str, final T defaultValue) {
        return StrUtils.isBlank(str) ? defaultValue : str;
    }

    /**
     * 如果被检查对象为 {@code null} 或 "" 或 空白字符串时，返回默认值（由 defaultValueSupplier 提供）；否则直接返回
     *
     * @param str                  被检查对象
     * @param defaultValueSupplier 默认值提供者
     * @param <T>                  对象类型（必须实现CharSequence接口）
     * @return 被检查对象为{@code null}返回默认值，否则返回自定义handle处理后的返回值
     * @throws NullPointerException {@code defaultValueSupplier == null} 时，抛出
     * @since 5.7.20
     */
    public static <T extends String> T defaultIfBlank(T str, Supplier<? extends T> defaultValueSupplier) {
        if (StrUtils.isBlank(str)) {
            return defaultValueSupplier.get();
        }
        return str;
    }

    /**
     * 如果被检查对象为 {@code null} 或 "" 或 空白字符串时，返回默认值（由 defaultValueSupplier 提供）；否则直接返回
     *
     * @param str                  被检查对象
     * @param defaultValueSupplier 默认值提供者
     * @param <T>                  对象类型（必须实现CharSequence接口）
     * @return 被检查对象为{@code null}返回默认值，否则返回自定义handle处理后的返回值
     * @throws NullPointerException {@code defaultValueSupplier == null} 时，抛出
     * @since 5.7.20
     */
    public static <T extends String> T defaultIfBlank(T str, Function<T, ? extends T> defaultValueSupplier) {
        if (StrUtils.isBlank(str)) {
            return defaultValueSupplier.apply(null);
        }
        return str;
    }
}