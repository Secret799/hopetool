package com.hopetool.core.collection;

import com.hopetool.core.lang.AssertUtils;

import java.util.Collection;
import java.util.Iterator;

/**
 * {@link Iterator} 相关工具类
 *
 * @author JunPzx
 * @since 2023/12/21 10:47
 */
public class IteratorUtils {

    /**
     * 将迭代器中的元素全部添加到指定的集合中。
     *
     * @param addTo    要添加元素的集合
     * @param iterator 迭代器，用于获取要添加的元素
     * @param <T>      元素的类型
     * @return 如果集合被修改则返回true，否则返回false
     */
    public static <T> boolean addAll(Collection<T> addTo, Iterator<? extends T> iterator) {
        AssertUtils.notNull(addTo, NullPointerException::new);
        AssertUtils.notNull(iterator, NullPointerException::new);
        boolean wasModified = false;
        while (iterator.hasNext()) {
            wasModified |= addTo.add(iterator.next());
        }
        return wasModified;
    }

}
