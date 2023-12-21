package com.hopetool.core.set;

import com.hopetool.core.collection.IteratorUtils;
import com.hopetool.core.map.MapUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Set工具类
 *
 * @author JunPzx
 * @since 2023/12/21 10:42
 */
public class SetUtils {

    /**
     * 创建一个新的HashSet对象，并返回该对象。
     *
     * @param <E> 泛型类型参数，表示集合中元素的类型
     * @return 返回一个新的HashSet对象
     */
    public static <E> HashSet<E> newHashSet() {
        return new HashSet<E>();
    }


    /**
     * 创建一个新的HashSet对象，其中包含传入的参数元素。
     *
     * @param elements 传入的参数元素
     * @param <E>      泛型类型参数，元素的类型
     * @return 返回一个新的HashSet对象
     */
    @SafeVarargs
    public static <E> HashSet<E> newHashSet(E... elements) {
        HashSet<E> set = newHashSetWithExpectedSize(elements.length);
        Collections.addAll(set, elements);
        return set;
    }


    /**
     * 创建一个新的HashSet对象，并将给定的元素添加到该集合中。
     *
     * @param elements 给定的元素，可以是一个Collection对象或者一个可迭代的对象
     * @param <E>      元素的类型
     * @return 返回一个包含给定元素的新的HashSet对象
     */
    public static <E> HashSet<E> newHashSet(Iterable<? extends E> elements) {
        return (elements instanceof Collection)
                ? new HashSet<E>((Collection<? extends E>) elements)
                : newHashSet(elements.iterator());
    }


    public static <E> HashSet<E> newHashSet(Iterator<? extends E> elements) {
        HashSet<E> set = newHashSet();
        IteratorUtils.addAll(set, elements);
        return set;
    }


    /**
     * 创建一个具有预期容量的新的HashSet对象。
     *
     * @param expectedSize 预期的容量
     * @param <E>          泛型类型
     * @return 新的HashSet对象
     */
    public static <E> HashSet<E> newHashSetWithExpectedSize(int expectedSize) {
        return new HashSet<E>(MapUtils.capacity(expectedSize));
    }

}
