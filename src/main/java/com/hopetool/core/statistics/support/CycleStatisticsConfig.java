package com.hopetool.core.statistics.support;

import com.hopetool.core.column.support.SFunction;
import com.hopetool.core.datetime.LocalDateTimeUtils;
import com.hopetool.core.datetime.support.DateTimeUnit;
import com.hopetool.core.lang.AssertUtils;
import com.hopetool.core.statistics.DataStatisticsUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 周期统计配置
 * <p>
 * T: 统计数据-数据实体类型
 * V: 统计数据-实体获取数据get方法返回类型
 * M: 多维度统计-统计维度字段get方法返回类型
 *
 * @author JunPzx
 * @since 2023/11/28 18:35
 */
@SuppressWarnings("UnusedReturnValue")
@Getter
@NoArgsConstructor
public class CycleStatisticsConfig<T, V, M> extends AbstractStatisticsConfig<T> {

    /**
     * 周期统计-数据分割单位
     */
    private DateTimeUnit dateTimeUnit;

    /**
     * 开始时间
     */
    private LocalDateTime beginDateTime;

    /**
     * 结束数据
     */
    private LocalDateTime endDateTime;

    /**
     * 统计时,统计范围多个时间参数条件的拼接方式
     */
    private StatisticsArgumentMatchEnum argumentMatchEnum;

    /**
     * 统计时,统计范围时间参数的getFunction
     */
    private List<SFunction<T, LocalDateTime>> dateTimeGetFunction;

    /**
     * 需要统计的数据值的get方法
     */
    private SFunction<T, V> getValueFunction;

    /**
     * 是否是多维度的
     */
    private Boolean isMultidimensional = false;

    /**
     * 单维度信息
     */
    private SingleDimensional singleDimensional;

    /**
     * 多维度信息
     */
    private Multidimensional<T, M> multidimensional;


    /**
     * 根据指定的日期时间单位构建统计配置
     *
     * @param dateTimeUnit 日期时间单位
     * @return 循环统计配置
     */
    public CycleStatisticsConfig<T, V, M> buildStatisticsDateTime(DateTimeUnit dateTimeUnit) {
        LocalDateTime[] timePeriod = LocalDateTimeUtils.generateTimePeriodBasedOnTheCurrentTime(dateTimeUnit, 0);
        return buildStatisticsDateTime(dateTimeUnit, timePeriod[0], timePeriod[1]);
    }


    /**
     * 根据指定的日期时间单位构建统计配置
     *
     * @param dateTimeUnit  日期时间单位
     * @param beginDateTime 开始日期时间
     * @param endDateTime   结束日期时间
     * @return 周期统计配置对象
     */
    public CycleStatisticsConfig<T, V, M> buildStatisticsDateTime(DateTimeUnit dateTimeUnit, LocalDateTime beginDateTime, LocalDateTime endDateTime) {
        this.dateTimeUnit = dateTimeUnit;
        this.beginDateTime = beginDateTime;
        this.endDateTime = endDateTime;
        return this;
    }


    /**
     * 根据指定数据构建周期统计配置
     *
     * @param data               周期数据列表
     * @param statisticsModeEnum 统计模式
     * @param getValueFunction   获取值的函数
     * @return 周期统计配置对象
     */
    public CycleStatisticsConfig<T, V, M> buildStatisticsData(List<T> data, DataValueStatisticsModeEnum statisticsModeEnum, SFunction<T, V> getValueFunction) {
        super.setData(data);
        super.setStatisticsMode(statisticsModeEnum);
        this.getValueFunction = getValueFunction;
        return this;
    }


    /**
     * 根据指定的统计条件构建统计配置
     *
     * @param argumentMatchEnum   统计参数匹配类型
     * @param dateTimeGetFunction 获取日期时间的函数列表
     * @return 统计配置对象
     */
    public CycleStatisticsConfig<T, V, M> buildStatisticsCondition(StatisticsArgumentMatchEnum argumentMatchEnum, List<SFunction<T, LocalDateTime>> dateTimeGetFunction) {
        this.argumentMatchEnum = argumentMatchEnum;
        this.dateTimeGetFunction = dateTimeGetFunction;
        return this;
    }

    /**
     * 根据指定的统计条件构建统计配置
     *
     * @param argumentMatchEnum   统计参数匹配类型
     * @param dateTimeGetFunction 获取日期时间的函数列表
     * @return 统计配置对象
     */
    public CycleStatisticsConfig<T, V, M> buildStatisticsCondition(StatisticsArgumentMatchEnum argumentMatchEnum, SFunction<T, LocalDateTime> dateTimeGetFunction) {
        this.argumentMatchEnum = argumentMatchEnum;
        this.dateTimeGetFunction = Collections.singletonList(dateTimeGetFunction);
        return this;
    }


    /**
     * 构建单维的统计配置
     *
     * @param tagName        标签名
     * @param tagChineseName 标签中文名
     * @return 自定义的CycleStatisticsConfig对象
     */
    public CycleStatisticsConfig<T, V, M> buildSingleDimensional(String tagName, String tagChineseName) {
        this.isMultidimensional = false;
        this.multidimensional = null;
        this.singleDimensional = new SingleDimensional(tagName, tagChineseName);
        return this;
    }


    /**
     * 构建多维统计配置(标签名需要和多维度统统计数据中的标签名一致)
     *
     * @param multidimensionalFunction 多维函数
     * @param tagMemo                  标签备忘录
     * @return 带有多维循环统计配置的CycleStatisticsConfig对象
     */
    public CycleStatisticsConfig<T, V, M> buildMultidimensional(SFunction<T, M> multidimensionalFunction, Map<M, String> tagMemo) {
        this.isMultidimensional = true;
        this.singleDimensional = null;
        this.multidimensional = new Multidimensional<>(multidimensionalFunction, tagMemo);
        return this;
    }


    public void checkSelf() {
        super.checkSelf();
        AssertUtils.notNull(dateTimeUnit, "时间单位不能为空");
        AssertUtils.notNull(beginDateTime, "开始时间不能为空");
        AssertUtils.notNull(endDateTime, "结束时间不能为空");
        AssertUtils.notNull(argumentMatchEnum, "统计参数匹配类型不能为空");
        AssertUtils.notNull(dateTimeGetFunction, "统计参数获取函数不能为空");
        AssertUtils.notNull(getValueFunction, "统计值获取函数不能为空");
        if (isMultidimensional) {
            AssertUtils.notNull(multidimensional, "多维度统计配置不能为空");
            AssertUtils.notNull(multidimensional.getMultidimensionalFunction(), "多维度统计方法不能为空");
            AssertUtils.notEmpty(multidimensional.getTagMemo(), "多维度统计标签名和中文名不能为空");
        } else {
            AssertUtils.notNull(singleDimensional, "单维度统计配置不能为空");
        }
    }
}
