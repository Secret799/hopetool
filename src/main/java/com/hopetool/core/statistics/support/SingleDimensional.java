package com.hopetool.core.statistics.support;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 单维度统计配置
 *
 * @author JunPzx
 * @since 2023/11/29
 */
@Data
@AllArgsConstructor
public class SingleDimensional {
    /**
     * 标签Code
     */
    private String tagCode;

    /**
     * 标签名称
     */
    private String tagName;
}