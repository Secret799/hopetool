package com.hopetool.core.lang;

import com.hopetool.core.text.StrUtils;

import java.util.Map;
import java.util.function.Supplier;

/**
 * 断言工具类
 *
 * @author JunPzx
 * @since 2023/12/20 14:18
 */
public class AssertUtils {


    /**
     * 断言是否为真，如果为 {@code false} 抛出给定的异常<br>
     *
     * <pre class="code">
     * Assert.isTrue(i &gt; 0, IllegalArgumentException::new);
     * </pre>
     *
     * @param <X>        异常类型
     * @param expression 布尔值
     * @param supplier   指定断言不通过时抛出的异常
     * @throws X if expression is {@code false}
     */
    public static <X extends Throwable> void isTrue(boolean expression, Supplier<? extends X> supplier) throws X {
        if (!expression) {
            throw supplier.get();
        }
    }

    /**
     * 断言是否为真，如果为 {@code false} 抛出 {@code IllegalArgumentException} 异常<br>
     *
     * <pre class="code">
     * Assert.isTrue(i &gt; 0, "The value must be greater than zero");
     * </pre>
     *
     * @param expression       布尔值
     * @param errorMsgTemplate 错误抛出异常附带的消息模板，变量用{}代替
     * @param params           参数列表
     * @throws IllegalArgumentException if expression is {@code false}
     */
    public static void isTrue(boolean expression, String errorMsgTemplate, Object... params) throws IllegalArgumentException {
        isTrue(expression, () -> new IllegalArgumentException(StrUtils.format(errorMsgTemplate, params)));
    }

    /**
     * 断言是否为真，如果为 {@code false} 抛出 {@code IllegalArgumentException} 异常<br>
     *
     * <pre class="code">
     * Assert.isTrue(i &gt; 0);
     * </pre>
     *
     * @param expression 布尔值
     * @throws IllegalArgumentException if expression is {@code false}
     */
    public static void isTrue(boolean expression) throws IllegalArgumentException {
        isTrue(expression, "[Assertion failed] - this expression must be true");
    }

    /**
     * 断言是否为假，如果为 {@code true} 抛出指定类型异常<br>
     * 并使用指定的函数获取错误信息返回
     * <pre class="code">
     *  Assert.isFalse(i &gt; 0, ()-&gt;{
     *      // to query relation message
     *      return new IllegalArgumentException("relation message to return");
     *  });
     * </pre>
     *
     * @param <X>           异常类型
     * @param expression    布尔值
     * @param errorSupplier 指定断言不通过时抛出的异常
     * @throws X if expression is {@code false}
     * @since 5.4.5
     */
    public static <X extends Throwable> void isFalse(boolean expression, Supplier<X> errorSupplier) throws X {
        if (expression) {
            throw errorSupplier.get();
        }
    }

    /**
     * 断言是否为假，如果为 {@code true} 抛出 {@code IllegalArgumentException} 异常<br>
     *
     * <pre class="code">
     * Assert.isFalse(i &lt; 0, "The value must not be negative");
     * </pre>
     *
     * @param expression       布尔值
     * @param errorMsgTemplate 错误抛出异常附带的消息模板，变量用{}代替
     * @param params           参数列表
     * @throws IllegalArgumentException if expression is {@code false}
     */
    public static void isFalse(boolean expression, String errorMsgTemplate, Object... params) throws IllegalArgumentException {
        isFalse(expression, () -> new IllegalArgumentException(StrUtils.format(errorMsgTemplate, params)));
    }

    /**
     * 断言是否为假，如果为 {@code true} 抛出 {@code IllegalArgumentException} 异常<br>
     *
     * <pre class="code">
     * Assert.isFalse(i &lt; 0);
     * </pre>
     *
     * @param expression 布尔值
     * @throws IllegalArgumentException if expression is {@code false}
     */
    public static void isFalse(boolean expression) throws IllegalArgumentException {
        isFalse(expression, "[Assertion failed] - this expression must be false");
    }


    /**
     * 检查给定的对象是否为null，如果为null则抛出非法参数异常
     *
     * @param object   需要检查的对象
     * @param supplier 提供非法参数异常的供应商
     * @return 检查的对象
     * @throws IllegalArgumentException 如果对象为null
     */
    public static <T, E extends Exception> T notNull(T object, Supplier<E> supplier) throws E {
        if (object == null) {
            throw supplier.get();
        }
        return object;
    }

    /**
     * 检查给定的对象是否为非空，如果是空则抛出{@link IllegalArgumentException}异常。
     *
     * @param object           需要检查的对象
     * @param errorMsgTemplate 错误信息模板，用于生成错误信息的格式化字符串
     * @param params           用于格式化错误信息的参数
     * @return 检查的对象
     * @throws IllegalArgumentException 如果对象为null
     */
    public static <T> T notNull(T object, String errorMsgTemplate, Object... params) throws IllegalArgumentException {
        if (object == null) {
            throw new IllegalArgumentException(StrUtils.format(errorMsgTemplate, params));
        }
        return object;
    }


    /**
     * 检查给定的对象是否为null。
     * 如果对象为null，则抛出IllegalArgumentException异常。
     *
     * @param object 要检查的对象
     * @return 检查的对象
     * @throws IllegalArgumentException 如果对象为null
     */
    public static <T> T notNull(T object) throws IllegalArgumentException {
        return notNull(object, "[Assertion failed] - this argument is required; it must not be null");
    }


    /**
     * 检查给定的字符串是否为空，如果为空则抛出指定异常。
     *
     * @param text     要检查的字符串
     * @param supplier 用于生成非法参数异常的供应商
     * @return 检查的字符串
     * @throws E 如果给定的字符串为空则抛出该异常
     */
    public static <E extends Exception> String notBlank(String text, Supplier<E> supplier) throws E {
        if (StrUtils.isBlank(text)) {
            throw supplier.get();
        }
        return text;
    }


    /**
     * 检查字符串是否为空
     *
     * @param text             字符串
     * @param errorMsgTemplate 错误信息模板
     * @param params           参数
     * @return 检查的字符串
     * @throws IllegalArgumentException 如果字符串为空则抛出此异常
     */
    public static String notBlank(String text, String errorMsgTemplate, Object... params) throws IllegalArgumentException {
        if (StrUtils.isBlank(text)) {
            throw new IllegalArgumentException(StrUtils.format(errorMsgTemplate, params));
        }
        return text;
    }


    /**
     * 检查给定的字符串是否不为空
     * 如果字符串为空，将抛出IllegalArgumentException异常
     *
     * @param text 要检查的字符串
     * @return 检查的字符串
     * @throws IllegalArgumentException 如果字符串为空
     */
    public static String notBlank(String text) throws IllegalArgumentException {
        return notBlank(text, "[Assertion failed] - this String argument must have text; it must not be null, empty, or blank");
    }


    /**
     * 判断给定的Map是否为空或者为空Map，若是则抛出自定义异常
     *
     * @param map      需要判断的Map对象
     * @param supplier 提供异常信息的供应商
     * @return 若非空且非空Map，则返回原Map对象
     * @throws E 如果给定的Map为空或者为空Map，则抛出该异常
     */
    public static <K, V, E extends Exception> Map<K, V> notEmpty(Map<K, V> map, Supplier<E> supplier) throws E {
        if (map == null || map.isEmpty()) {
            throw supplier.get();
        }
        return map;
    }


    /**
     * 判断给定的Map是否为空或者为空Map，如果是则抛出参数异常。
     *
     * @param map              要判断的Map
     * @param errorMsgTemplate 错误消息的模板
     * @param params           错误消息的参数
     * @param <K>              Map的键类型
     * @param <V>              Map的值类型
     * @return 如果Map不为空且非空Map，则返回该Map
     * @throws IllegalArgumentException 如果Map为空或者为空Map，则抛出该异常
     */
    public static <K, V> Map<K, V> notEmpty(Map<K, V> map, String errorMsgTemplate, Object... params) throws IllegalArgumentException {
        if (map == null || map.isEmpty()) {
            throw new IllegalArgumentException(StrUtils.format(errorMsgTemplate, params));
        }
        return map;
    }


    /**
     * 静态方法，用于检查给定的Map是否为空，如果为空则抛出异常。
     *
     * @param map 需要检查的Map对象
     * @param <K> Map中键的类型
     * @param <V> Map中值的类型
     * @return 非空的Map对象
     * @throws IllegalArgumentException 如果给定的Map为空
     */
    public static <K, V> Map<K, V> notEmpty(Map<K, V> map) throws IllegalArgumentException {
        return notEmpty(map, "[断言失败] - 给定的Map不能为空：至少需要包含1个键值对");
    }

}
