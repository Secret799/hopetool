package com.hopetool.core.statistics.support;

import com.hopetool.core.lang.AssertUtils;
import lombok.Data;

import java.util.List;

/**
 * 抽象统计配置
 *
 * @author JunPzx
 * @since 2023/11/28 18:38
 */
@Data
public abstract class AbstractStatisticsConfig<T> {

    /**
     * 统计方式
     */
    private DataValueStatisticsModeEnum statisticsMode;

    /**
     * 需要统计的数据
     */
    private List<T> data;


    public void checkSelf() {
        AssertUtils.notNull(statisticsMode, "统计方式不能为空");
        AssertUtils.notNull(data, "统计数据不能为空");
    }
}
