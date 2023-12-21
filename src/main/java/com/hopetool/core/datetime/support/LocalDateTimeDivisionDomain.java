package com.hopetool.core.datetime.support;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 日期-时间划分域
 *
 * @author JunPzx
 * @since 2023/8/25 20:02
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class LocalDateTimeDivisionDomain<T> {

    /**
     * 开始时间
     */
    private T beginTime;

    /**
     * 结束时间
     */
    private T endTime;

    /**
     * 中文时间
     */
    private String chinese;

    /**
     * 日期字符串
     */
    private String dateStr;
}
