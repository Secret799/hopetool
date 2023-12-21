package com.hopetool.core.statistics.support;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 数据值统计方式
 *
 * @author JunPzx
 * @since 2023/11/28 18:27
 */
@NoArgsConstructor
@Getter
public enum DataValueStatisticsModeEnum {
    /**
     * 总和
     */
    SUM,
    /**
     * 总数
     */
    COUNT,
    /**
     * 去重总数
     */
    DISTINCT_COUNT,
    /**
     * 平均值
     */
    AVG
}
