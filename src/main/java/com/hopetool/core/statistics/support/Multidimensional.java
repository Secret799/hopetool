package com.hopetool.core.statistics.support;

import com.hopetool.core.column.support.SFunction;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

/**
 * 多维度统计配置
 *
 * @author JunPzx
 * @since 2023/11/29
 */
@Data
@AllArgsConstructor
public class Multidimensional<T, M> {
    /**
     * 多维度统计方法
     */
    private SFunction<T, M> multidimensionalFunction;

    /**
     * 多维度统计标签名和中文名
     */
    private Map<M, String> tagMemo;
}