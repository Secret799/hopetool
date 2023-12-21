package com.hopetool.core.statistics.support;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 数据统计项详情
 *
 * @author JunPzx
 * @since 2023/11/28 17:39
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class DataStatisticsItemDetail {

    /**
     * 标签Code
     */
    private String tagCode;

    /**
     * 标签名称
     */
    private String tagName;

    /**
     * 统计值
     */
    private String value;
}
