package com.hopetool.core.datetime;

import com.hopetool.core.datetime.support.DateTimeUnit;
import com.hopetool.core.datetime.support.LocalDateTimeDivisionDomain;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 时间拆分工具类
 *
 * @author JunPzx
 * @since 2023/12/20 9:48
 */
public class LocalDateTimeDivisionUtils {


    /**
     * 对两个 LocalDateTime 对象之间的区间进行分段
     *
     * @param beginDateTime        开始时间
     * @param endDateTime          结束时间
     * @param dateTimeUnit         时间单位
     * @param ignoringMilliseconds 是否忽略毫秒数
     * @return 分段结果列表
     */
    public static List<LocalDateTimeDivisionDomain<LocalDateTime>> division(LocalDateTime beginDateTime,
                                                                            LocalDateTime endDateTime,
                                                                            DateTimeUnit dateTimeUnit,
                                                                            boolean ignoringMilliseconds) {
        // 结果列表
        List<LocalDateTimeDivisionDomain<LocalDateTime>> resultList = new ArrayList<>();
        // 全局开始时间定义
        LocalDateTime overallSituationBeginTime = ignoringMilliseconds ?
                LocalDateTime.of(beginDateTime.toLocalDate(), LocalTime.of(0, 0, 0)) : LocalDateTimeUtils.copyLocalDateTime(beginDateTime);
        // 全局结束时间定义
        LocalDateTime overallSituationEndTime = ignoringMilliseconds ?
                LocalDateTime.of(endDateTime.toLocalDate(), LocalTime.of(23, 59, 59)) : LocalDateTimeUtils.copyLocalDateTime(endDateTime);
        // 根据全局开始时间构建一个临时开始时间
        LocalDateTime tempBeginTime = LocalDateTimeUtils.copyLocalDateTime(overallSituationBeginTime);
        // 判断当前时间段的开始时间是否在全局结束时间之前,如果不在全局结束时间之前则进行分割时间段
        while (tempBeginTime.isBefore(overallSituationEndTime)) {
            // 构建一个当前时间段返回结果集
            LocalDateTimeDivisionDomain<LocalDateTime> dateTimeDivision = new LocalDateTimeDivisionDomain<>();
            // 计算当前时间段结束时间
            LocalDateTime tempEndTime = LocalDateTimeUtils.endTimeByDateTimeUnit(tempBeginTime, dateTimeUnit, ignoringMilliseconds);
            // 判断当前时间段结束时间是否在全局结束时间之前,如果不在,则把当前时间段的结束时间设置为全局结束时间
            tempEndTime = tempEndTime.isBefore(overallSituationEndTime) ? tempEndTime : LocalDateTimeUtils.copyLocalDateTime(overallSituationEndTime);
            // 构建当前时间段返回结果集
            dateTimeDivision.setBeginTime(tempBeginTime)
                    .setEndTime(tempEndTime)
                    .setChinese(dateTimeUnit.getChineseName(tempBeginTime))
                    .setDateStr(LocalDateTimeUtils.format(tempBeginTime, dateTimeUnit.getDateFormat()));
            // 计算下一个时间段开始时间
            tempBeginTime = LocalDateTimeUtils.offset(tempBeginTime, 1, dateTimeUnit);
            // 添加当前时间段返回结果集
            resultList.add(dateTimeDivision);
        }
        // 返回结果列表
        return resultList;
    }


    /**
     * 对两个 LocalDateTime 对象之间的区间进行分段
     *
     * @param beginDateTime 开始时间
     * @param endDateTime   结束时间
     * @param dateTimeUnit  时间单位
     * @return 分段结果列表
     */
    public static List<LocalDateTimeDivisionDomain<LocalDateTime>> division(LocalDateTime beginDateTime,
                                                                            LocalDateTime endDateTime,
                                                                            DateTimeUnit dateTimeUnit) {
        return division(beginDateTime, endDateTime, dateTimeUnit, false);
    }


    /**
     * 根据时间单位以及当前时间进行时间切分
     *
     * @param dateTimeUnit 时间单位
     * @return 切分结果
     */
    public static List<LocalDateTimeDivisionDomain<LocalDateTime>> division(DateTimeUnit dateTimeUnit) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime beginDateTime = LocalDateTimeUtils.beginTimeByDateTimeUnit(now, dateTimeUnit);
        LocalDateTime endDateTime = LocalDateTimeUtils.endTimeByDateTimeUnit(now, dateTimeUnit);
        return division(beginDateTime, endDateTime, dateTimeUnit);
    }
}
