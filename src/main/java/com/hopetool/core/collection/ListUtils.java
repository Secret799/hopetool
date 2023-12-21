package com.hopetool.core.collection;

import com.hopetool.core.array.ArraysUtils;

import java.util.*;

/**
 * list 相关工具类
 *
 * @author JunPzx
 * @since 2023/12/21 10:25
 */
public class ListUtils extends CollectionUtils {

    /**
     * 创建一个新的ArrayList对象
     *
     * @param <T> 泛型类型
     * @return 新的ArrayList对象
     */
    public static <T> ArrayList<T> newArrayList() {
        return new ArrayList<>();
    }

    /**
     * 创建一个新的ArrayList对象，并使用指定的Collection来初始化该对象
     *
     * @param collection 要初始化ArrayList的Collection
     * @param <T>        泛型类型
     * @return 初始化后的ArrayList对象
     */
    public static <T> ArrayList<T> newArrayList(Collection<T> collection) {
        return new ArrayList<>(collection);
    }

    /**
     * 创建一个新的ArrayList，其中包含给定的数组中的元素。
     * 如果给定的数组为空，则返回一个空的ArrayList。
     *
     * @param array 给定的数组
     * @param <T>   泛型类型
     * @return 一个新的ArrayList
     */
    @SafeVarargs
    public static <T> ArrayList<T> newArrayList(T... array) {
        if (ArraysUtils.isEmpty(array)) {
            return new ArrayList<>();
        }
        return newArrayList(Arrays.asList(array));
    }

    /**
     * 创建一个新的ArrayList列表，将提供的集合合并到新的列表中(根据collection顺序合并)
     * <p>
     * 例如: [1,2,3]  [4,5,6] 结果[1,2,3,4,5,6]
     * <p>
     * 例如: [1,3,5]  [2,4,6] 结果[1,3,5,2,4,6]
     *
     * @param collection 要合并到新列表中的集合
     * @return 合并后的ArrayList列表
     */
    @SafeVarargs
    public static <T> List<T> newArrayList(List<T>... collection) {
        if (collection.length == 0) {
            return new ArrayList<>();
        }
        List<T> result = new ArrayList<>();
        Arrays.stream(collection).forEach(result::addAll);
        return result;
    }


    /**
     * 获取一个空List，这个空List不可变
     *
     * @param <T> 元素类型
     * @return 空的List
     * @see Collections#emptyList()
     */
    public static <T> List<T> empty() {
        return Collections.emptyList();
    }


    /**
     * 针对List排序，排序会修改原List
     *
     * @param <T>  元素类型
     * @param list 被排序的List
     * @param c    {@link Comparator}
     * @return 原list
     * @see Collections#sort(List, Comparator)
     */
    public static <T> List<T> sort(List<T> list, Comparator<? super T> c) {
        if (isEmpty(list)) {
            return list;
        }
        list.sort(c);
        return list;
    }
}
