package com.hopetool.core.statistics;


import com.hopetool.core.collection.ListUtils;
import com.hopetool.core.column.support.SFunction;
import com.hopetool.core.datetime.LocalDateTimeDivisionUtils;
import com.hopetool.core.datetime.LocalDateTimeUtils;
import com.hopetool.core.datetime.support.DateTimeUnit;
import com.hopetool.core.datetime.support.LocalDateTimeDivisionDomain;
import com.hopetool.core.lang.AssertUtils;
import com.hopetool.core.set.SetUtils;
import com.hopetool.core.statistics.support.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 数据统计工具类
 *
 * @author JunPzx
 * @since 2023/11/28 17:48
 */
@SuppressWarnings("DuplicatedCode")
public class DataStatisticsUtils {

    /**
     * 根据偏移量和单位获取开始时间和结束时间
     *
     * @param dateTimeUnit 单位
     * @param offset       偏移量
     * @return 开始时间和结束时间
     */
    public static LocalDateTime[] generateTimePeriodBasedOnTheCurrentTime(DateTimeUnit dateTimeUnit, int offset) {
        LocalDateTime now = LocalDateTime.now();
        // 过去
        if (offset < 0) {
            LocalDateTime beginDateTime = LocalDateTimeUtils.afterOffsetBeginTimeByDateTimeUnit(now, offset, dateTimeUnit);
            LocalDateTime endDateTime = LocalDateTimeUtils.endTimeByDateTimeUnit(now, dateTimeUnit);
            return new LocalDateTime[]{beginDateTime, endDateTime};
        }
        // 未来
        if (offset > 0) {
            LocalDateTime beginDateTime = LocalDateTimeUtils.beginTimeByDateTimeUnit(now, dateTimeUnit);
            LocalDateTime endDateTime = LocalDateTimeUtils.afterOffsetEndTimeByDateTimeUnit(now, offset, dateTimeUnit);
            return new LocalDateTime[]{beginDateTime, endDateTime};
        }
        // 当前
        return new LocalDateTime[]{LocalDateTimeUtils.beginTimeByDateTimeUnit(now, dateTimeUnit),
                LocalDateTimeUtils.endTimeByDateTimeUnit(now, dateTimeUnit)};
    }


    /**
     * 周期统计方法
     *
     * @param config 周期统计配置
     * @return 周期数据统计结果
     */
    public static <T, V, M> CycleDataStatisticsResult cycleStatistics(CycleStatisticsConfig<T, V, M> config) {
        if (Boolean.TRUE.equals(config.getIsMultidimensional())) {
            // 如果配置为多维统计，则调用多维统计方法
            return cycleMultidimensionalStatistics(config);
        }
        // 如果配置为单维统计，则调用单维统计方法
        return cycleSingleDimensionalStatistics(config);
    }


    /**
     * 单维度统计周期数据的函数
     *
     * @param config 周期统计配置对象
     * @param <T>    数据类型
     * @param <V>    数据值类型
     * @return 周期数据统计结果对象
     */
    public static <T, V, M> CycleDataStatisticsResult cycleSingleDimensionalStatistics(CycleStatisticsConfig<T, V, M> config) {
        // 校验配置对象
        AssertUtils.notNull(config, "配置对象不能为空").checkSelf();
        AssertUtils.isFalse(config.getIsMultidimensional(), "统计配置种维度配置有误,请确定维度配置是否为一维配置");
        // 根据配置的起始时间和结束时间，将时间范围划分为多个时间段
        List<LocalDateTimeDivisionDomain<LocalDateTime>> timeFrames = LocalDateTimeDivisionUtils.division(config.getBeginDateTime(), config.getEndDateTime(), config.getDateTimeUnit());
        // 创建周期数据统计结果对象
        CycleDataStatisticsResult result = new CycleDataStatisticsResult();
        // 遍历每个时间段
        timeFrames.forEach(timeFrame -> {
            // 根据时间范围和配置的数据，筛选需要统计的数据
            List<V> values = config.getData().stream().filter(data -> filterCycleStatisticalDataBasedOnTimeRange(timeFrame, data, config))
                    // 根据配置的获取数值函数，获取数值并转换为BigDecimal类型
                    .map(t -> config.getGetValueFunction().apply(t))
                    // 将获取到的数值存储到列表中
                    .collect(Collectors.toList());
            // 根据统计模式计算数据
            String cycleTotalExpenditure = calculateDataBasedOnStatisticalPatterns(values, config.getStatisticsMode());
            // 将周期统计结果添加到周期数据统计结果对象中
            result.addDetail(CycleStatisticsItemDetail.instance(timeFrame.getDateStr(), timeFrame.getChinese(), new DataStatisticsItemDetail().setTagCode(config.getSingleDimensional().getTagCode()).setTagName(config.getSingleDimensional().getTagName()).setValue(cycleTotalExpenditure)));
        });
        // 返回周期数据统计结果对象
        return result;
    }


    /**
     * 多维统计数据周期结果(多维度的标签名需要在统计配置中指定)
     *
     * @param config 周期统计配置
     * @return 周期统计数据结果
     */
    public static <T, V, M> CycleDataStatisticsResult cycleMultidimensionalStatistics(CycleStatisticsConfig<T, V, M> config) {
        // 校验配置对象
        AssertUtils.notNull(config, "配置对象不能为空").checkSelf();
        AssertUtils.isTrue(config.getIsMultidimensional(), "统计配置中维度配置有误,请确定维度配置是否为多维配置");
        // 根据配置的起始时间、结束时间和时间单位，获取时间周期切割后的时间范围
        List<LocalDateTimeDivisionDomain<LocalDateTime>> timeFrames = LocalDateTimeDivisionUtils.division(config.getBeginDateTime(), config.getEndDateTime(), config.getDateTimeUnit());
        // 创建周期统计数据结果对象
        CycleDataStatisticsResult result = new CycleDataStatisticsResult();
        // 遍历时间范围列表
        timeFrames.forEach(timeFrame -> {
            // 创建当前时间周期的统计数据细节对象
            CycleStatisticsItemDetail currTimeFrameStatisticsResult = new CycleStatisticsItemDetail().setDateStr(timeFrame.getDateStr()).setChineseDateStr(timeFrame.getChinese());
            // 根据多维度函数对数据进行分组
            Map<M, List<T>> dataGroupByMultidimensional = config.getData().stream().collect(Collectors.groupingBy(config.getMultidimensional().getMultidimensionalFunction()));
            // 创建统计数据细节列表
            List<DataStatisticsItemDetail> details = new ArrayList<>();
            // 定义未处理标签备忘录
            HashSet<M> unprocessedTagMemo = SetUtils.newHashSet(config.getMultidimensional().getTagMemo().keySet());
            // 遍历分组后的数据
            dataGroupByMultidimensional.forEach((dimensionCode, dimensionalData) -> {
                // 如果配置中没有对应的标签Code，则跳过
                if (!config.getMultidimensional().getTagMemo().containsKey(dimensionCode)) {
                    return;
                }
                // 根据时间范围过滤出需要统计的数据
                List<V> values = dimensionalData.stream()
                        .filter(data -> filterCycleStatisticalDataBasedOnTimeRange(timeFrame, data, config))
                        .map(t -> config.getGetValueFunction().apply(t))
                        .collect(Collectors.toList());
                // 根据统计模式计算统计结果
                String cycleTotalExpenditure = calculateDataBasedOnStatisticalPatterns(values, config.getStatisticsMode());
                // 创建统计数据细节对象并添加到细节列表中
                details.add(new DataStatisticsItemDetail().setTagCode(dimensionCode.toString()).setTagName(config.getMultidimensional().getTagMemo().get(dimensionCode)).setValue(cycleTotalExpenditure));
                unprocessedTagMemo.remove(dimensionCode);
            });
            // 如果数据遍历完毕后,但是tagMemo中还存在对应的标签没有统计,那么则补充默认数据
            if (!unprocessedTagMemo.isEmpty()) {
                unprocessedTagMemo.forEach(dimensionCode ->
                        details.add(new DataStatisticsItemDetail().setTagCode(dimensionCode.toString())
                        .setTagName(config.getMultidimensional().getTagMemo().get(dimensionCode)).setValue("0")));
            }
            // 将统计数据细节列表添加到当前时间周期的统计数据结果对象中，并添加到结果对象中
            result.addDetail(currTimeFrameStatisticsResult.setDetails(details));
        });
        // 返回周期统计数据结果对象
        return result;
    }

    /**
     * 总数统计方法
     *
     * @param config 总数统计配置
     * @return 总数数据统计结果
     */
    public static <T, V, M> TotalDataStatisticsResult totalStatistics(TotalStatisticsConfig<T, V, M> config) {
        if (Boolean.TRUE.equals(config.getIsMultidimensional())) {
            // 如果配置为多维统计，则调用多维统计方法
            return totalMultidimensionalStatistics(config);
        }
        // 如果配置为单维统计，则调用单维统计方法
        return totalSingleDimensionalStatistics(config);
    }


    public static <T, V, M> TotalDataStatisticsResult totalSingleDimensionalStatistics(TotalStatisticsConfig<T, V, M> config) {
        // 校验配置对象
        AssertUtils.notNull(config, "配置对象不能为空").checkSelf();
        AssertUtils.isFalse(config.getIsMultidimensional(), "统计配置种维度配置有误,请确定维度配置是否为一维配置");
        // 获取需要统计的值
        List<V> values = config.getData().stream()
                .map(t -> config.getGetValueFunction().apply(t))
                .collect(Collectors.toList());
        String statisticsValue = calculateDataBasedOnStatisticalPatterns(values, config.getStatisticsMode());
        return new TotalDataStatisticsResult()
                .addDetail(new DataStatisticsItemDetail(config.getSingleDimensional().getTagCode(),
                        config.getSingleDimensional().getTagName(), statisticsValue));
    }

    public static <T, V, M> TotalDataStatisticsResult totalMultidimensionalStatistics(TotalStatisticsConfig<T, V, M> config) {
        // 校验配置对象
        AssertUtils.notNull(config, "配置对象不能为空").checkSelf();
        AssertUtils.isTrue(config.getIsMultidimensional(), "统计配置中维度配置有误,请确定维度配置是否为多维配置");
        // 根据多维度函数对数据进行分组
        Map<M, List<T>> dataGroupByMultidimensional = config.getData().stream().collect(Collectors.groupingBy(config.getMultidimensional().getMultidimensionalFunction()));
        // 创建统计数据细节列表
        List<DataStatisticsItemDetail> details = new ArrayList<>();
        // 定义未处理标签备忘录
        HashSet<M> unprocessedTagMemo = SetUtils.newHashSet(config.getMultidimensional().getTagMemo().keySet());
        // 遍历分组后的数据
        dataGroupByMultidimensional.forEach((dimensionCode, dimensionalData) -> {
            // 如果配置中没有对应的标签名，则跳过
            if (!config.getMultidimensional().getTagMemo().containsKey(dimensionCode)) {
                return;
            }
            // 获取需要统计的值
            List<V> values = dimensionalData.stream()
                    .map(t -> config.getGetValueFunction().apply(t))
                    .collect(Collectors.toList());
            // 根据统计模式计算统计结果
            String cycleTotalExpenditure = calculateDataBasedOnStatisticalPatterns(values, config.getStatisticsMode());
            // 创建统计数据细节对象并添加到细节列表中
            details.add(new DataStatisticsItemDetail().setTagCode(dimensionCode.toString()).setTagName(config.getMultidimensional().getTagMemo().get(dimensionCode)).setValue(cycleTotalExpenditure));
            unprocessedTagMemo.remove(dimensionCode);
        });
        // 如果数据遍历完毕后,但是tagMemo中还存在对应的标签没有统计,那么则补充默认数据
        if (!unprocessedTagMemo.isEmpty()) {
            unprocessedTagMemo.forEach(dimensionCode -> {
                details.add(new DataStatisticsItemDetail().setTagCode(dimensionCode.toString())
                        .setTagName(config.getMultidimensional().getTagMemo().get(dimensionCode)).setValue("0"));
            });
        }
        return new TotalDataStatisticsResult().addDetail(details);
    }


    /**
     * 根据给定的时间段和数据，基于时间范围的过滤周期统计数据
     *
     * @param currentTimeFrame 当前时间段
     * @param data             数据
     * @param config           计量周期统计配置
     * @param <T>              数据类型
     * @param <V>              计量值类型
     * @return 如果数据的时间在指定的时间范围内，则返回true；否则返回false
     */
    private static <T, V, M> boolean filterCycleStatisticalDataBasedOnTimeRange(LocalDateTimeDivisionDomain<LocalDateTime> currentTimeFrame, T data, CycleStatisticsConfig<T, V, M> config) {
        // 如果只有一个时间段，则直接判断时间范围即可
        if (config.getDateTimeGetFunction().size() <= 1) {
            return LocalDateTimeUtils.isBetween(config.getDateTimeGetFunction().get(0).apply(data), currentTimeFrame.getBeginTime(), currentTimeFrame.getEndTime());
        } else {
            // 如果有多个时间段，则判断是否满足条件
            Stream<SFunction<T, LocalDateTime>> stream = config.getDateTimeGetFunction().stream();
            // 如果是OR，则只要满足一个时间段即可
            if (StatisticsArgumentMatchEnum.OR.equals(config.getArgumentMatchEnum())) {
                return stream.anyMatch(tempGetTimeFunction -> LocalDateTimeUtils.isBetween(tempGetTimeFunction.apply(data), currentTimeFrame.getBeginTime(), currentTimeFrame.getEndTime()));
            } else {
                // 如果是AND，则所有时间段都要满足才行
                return stream.allMatch(tempGetTimeFunction -> LocalDateTimeUtils.isBetween(tempGetTimeFunction.apply(data), currentTimeFrame.getBeginTime(), currentTimeFrame.getEndTime()));
            }
        }
    }


    /**
     * 根据统计模式计算数据
     *
     * @param values         数据值列表
     * @param statisticsMode 数据值统计模式
     * @return 计算后的数据
     */
    private static <V> String calculateDataBasedOnStatisticalPatterns(List<V> values, DataValueStatisticsModeEnum statisticsMode) {
        if (ListUtils.isEmpty(values)) {
            return "0";
        }
        switch (statisticsMode) {
            case SUM:
                return values.stream().map(DataCalculateUtils::newBigDecimal)
                        .reduce(DataCalculateUtils::add)
                        .orElse(new BigDecimal(0))
                        .setScale(2, RoundingMode.HALF_UP)
                        .stripTrailingZeros()
                        .toPlainString();
            case AVG:
                BigDecimal sum = values.stream().map(DataCalculateUtils::newBigDecimal)
                        .reduce(DataCalculateUtils::add)
                        .orElse(new BigDecimal(0));
                return DataCalculateUtils.divideIgnoreZero(sum, DataCalculateUtils.newBigDecimal(values.size()),
                                2, RoundingMode.HALF_UP)
                        .stripTrailingZeros().toPlainString();
            case COUNT:
                return String.valueOf(values.size());
            case DISTINCT_COUNT:
                return String.valueOf(values.stream().distinct().count());
        }
        return "0";
    }


}
