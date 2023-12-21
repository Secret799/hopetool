package com.hopetool.core.statistics.support;

import com.hopetool.core.datetime.support.DateTimeUnit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 基础周期统计参数
 *
 * @author JunPzx
 * @since 2023/11/28 17:28
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class CycleDataStatisticsParam {
    /**
     * 开始时间,如果为空,则根据类型判断
     */
    private LocalDateTime beginTime;

    /**
     * 结束时间,如果为空,则根据类型判断
     */
    private LocalDateTime endTime;

    /**
     * 统计类型
     */
    private DateTimeUnit statisticsDimension;

    /**
     * 统计步长(用于不传时间,想统计近三年,近半年这类场景使用)
     */
    private Integer step;

}
