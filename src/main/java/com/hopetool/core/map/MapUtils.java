package com.hopetool.core.map;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * map工具类
 *
 * @author JunPzx
 * @since 2023/12/20 11:34
 */
public class MapUtils {

    /**
     * 可以表示为{@code int}的二的最大幂。
     */
    private static final int MAX_POWER_OF_TWO = 1 << (Integer.SIZE - 2);

    /**
     * Map是否为空
     *
     * @param map 集合
     * @return 是否为空
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return null == map || map.isEmpty();
    }

    /**
     * Map是否为非空
     *
     * @param map 集合
     * @return 是否为非空
     */
    public static boolean isNotEmpty(Map<?, ?> map) {
        return null != map && !map.isEmpty();
    }


    /**
     * 创建一个新的HashMap实例
     *
     * @param <K> 键的类型
     * @param <V> 值的类型
     * @return 新的HashMap实例
     */
    public static <K, V> HashMap<K, V> newHashMap() {
        return new HashMap<>();
    }

    /**
     * 使用给定的 Map 对象，创建一个新的 HashMap 对象。
     *
     * @param map 给定的 Map 对象
     * @param <K> Key 的类型
     * @param <V> Value 的类型
     * @return 新创建的 HashMap 对象
     */
    public static <K, V> HashMap<K, V> newHashMap(Map<? extends K, ? extends V> map) {
        return new HashMap<>(map);
    }

    /**
     * 创建一个具有期望大小的HashMap
     *
     * @param expectedSize 期望大小
     * @param <K>          键的类型
     * @param <V>          值的类型
     * @return 新建的HashMap对象
     */
    public static <K, V> HashMap<K, V> newHashMapWithExpectedSize(int expectedSize) {
        return new HashMap<>(capacity(expectedSize));
    }


    /**
     * 计算哈希表的容量
     *
     * @param expectedSize 期望的大小
     * @return 哈希表的容量
     */
    public static int capacity(int expectedSize) {
        if (expectedSize < 3) {
            checkNonnegative(expectedSize, "expectedSize");
            return expectedSize + 1;
        }
        if (expectedSize < MAX_POWER_OF_TWO) {
            return (int) Math.ceil(expectedSize / 0.75);
        }
        return Integer.MAX_VALUE;
    }

    @SuppressWarnings("all")
    private static int checkNonnegative(int value, String name) {
        if (value < 0) {
            throw new IllegalArgumentException(name + " cannot be negative but was: " + value);
        }
        return value;
    }

    /**
     * 如果 key 对应的 value 不存在，则使用获取 mappingFunction 重新计算后的值，并保存为该 key 的 value，否则返回 value。<br>
     * 解决使用ConcurrentHashMap.computeIfAbsent导致的死循环问题。（issues#2349）<br>
     * A temporary workaround for Java 8 specific performance issue JDK-8161372 .<br>
     * This class should be removed once we drop Java 8 support.
     *
     * <p>
     * 注意此方法只能用于JDK8
     * </p>
     *
     * @param <K>             键类型
     * @param <V>             值类型
     * @param map             Map，一般用于线程安全的Map
     * @param key             键
     * @param mappingFunction 值计算函数
     * @return 值
     * @see <a href="https://bugs.openjdk.java.net/browse/JDK-8161372">https://bugs.openjdk.java.net/browse/JDK-8161372</a>
     */
    public static <K, V> V computeIfAbsentForJdk8(final Map<K, V> map, final K key, final Function<? super K, ? extends V> mappingFunction) {
        V value = map.get(key);
        if (null == value) {
            value = mappingFunction.apply(key);
            final V res = map.putIfAbsent(key, value);
            if (null != res) {
                // issues#I6RVMY
                // 如果旧值存在，说明其他线程已经赋值成功，putIfAbsent没有执行，返回旧值
                return res;
            }
            // 如果旧值不存在，说明赋值成功，返回当前值

            // Dubbo的解决方式，判空后调用依旧无法解决死循环问题
            // 见：Issue2349Test
            //value = map.computeIfAbsent(key, mappingFunction);
        }
        return value;
    }


    /**
     * 合并两个HashMap对象，并返回合并后的结果(如果两个HashMap对象中有相同的key,则以main HashMap的value为准)
     *
     * @param main      主HashMap对象
     * @param secondary 次HashMap对象
     * @param <K>       主键类型
     * @param <V>       值类型
     * @return 合并后的HashMap对象
     */
    public static <K, V> HashMap<K, V> merge(HashMap<K, V> main, HashMap<K, V> secondary) {
        if (isEmpty(main)) {
            return newHashMap(secondary);
        }
        if (isEmpty(secondary)) {
            return new HashMap<>(main);
        }
        HashMap<K, V> res = newHashMap(main);
        secondary.forEach((key, value) -> {
            if (!res.containsKey(key)) {
                res.put(key, value);
            }
        });
        return res;
    }

}
