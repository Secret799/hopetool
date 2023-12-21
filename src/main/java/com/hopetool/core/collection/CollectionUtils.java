package com.hopetool.core.collection;

import com.hopetool.core.array.ArraysUtils;
import com.hopetool.core.map.MapUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

/**
 * 集合工具类
 *
 * @author JunPzx
 * @since 2023/12/21 9:50
 */
public class CollectionUtils {

    /**
     * 集合是否为空
     *
     * @param collection 集合
     * @return 是否为空
     */
    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * 集合是否为非空
     *
     * @param collection 集合
     * @return 是否为非空
     */
    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }


    /**
     * Map是否为空
     *
     * @param map 集合
     * @return 是否为空
     * @see MapUtils#isEmpty(Map)
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return MapUtils.isEmpty(map);
    }


    /**
     * 判断给定的Map对象是否为空或者空集合
     *
     * @param map 要判断的Map对象
     * @return 如果Map对象不为空或者空集合，则返回true；否则返回false
     * @see MapUtils#isNotEmpty(Map)
     */
    public static boolean isNotEmpty(Map<?, ?> map) {
        return MapUtils.isNotEmpty(map);
    }


}
