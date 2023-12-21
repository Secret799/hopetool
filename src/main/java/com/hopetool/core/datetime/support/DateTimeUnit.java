package com.hopetool.core.datetime.support;

import com.hopetool.core.datetime.LocalDateTimeUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author JunPzx
 * @since 2023/9/6 17:44
 */
@AllArgsConstructor
public enum DateTimeUnit {
    /**
     * 年,例如2023,2022,2011
     */
    YEAR("year", "yyyy", "%s年"),

    /**
     * 季度,例如1季度,2季度,3季度,4季度
     */
    QUARTER("quarter", "yyyy-q", "%s季度"),

    /**
     * 月,例如1月,2月,3月,4月
     */
    MONTH("month", "yyyy-MM", "%s月"),
    /**
     * 天,例如1日,2日,3日
     */
    DAY("day", "yyyy-MM-dd", "%s日"),

    /**
     * 天,例如,周一,周二,周三
     */
    DAY_OF_WEEK("dayOfWeek", "yyyy-MM-dd", null),

    /**
     * 周几
     */
    WEEK("week", "yyyy-MM-dd", "%s月第%s周"),

    /**
     * 本月第几周
     */
    WEEK_OF_MONTH("weekOfMonth", "yyyy-MM-dd", "%s月第%s周"),

    /**
     * 时,例如,1点,2点,3点
     */
    HOUR("hour", "yyyy-MM-dd HH", "%s时");

    @Getter
    private final String code;
    @Getter
    private final String dateFormat;
    @Getter
    private final String chineseNameFormat;

    /**
     * 通过{@link LocalDateTime#getDayOfWeek()} {@link DayOfWeek#getValue()}获取星期int值,因为int值从1开始,所以将index为0的值置空
     */
    private final List<String> weekChineseNameMemo =
            Arrays.asList("", "周一", "周二", "周三", "周四", "周五", "周六", "周日");

    public static boolean contain(String code) {
        return Arrays.stream(values()).anyMatch(t -> Objects.equals(code, t.getCode()));
    }

    public static DateTimeUnit getByCode(String code) {
        return Arrays.stream(values()).filter(t -> Objects.equals(t.getCode(), code)).findFirst().orElse(null);
    }

    /**
     * 根据给定的日期时间获取对应的中文名称
     *
     * @param dateTime 传入的日期时间
     * @return 对应的中文名称
     */
    public String getChineseName(LocalDateTime dateTime) {
        String chineseName = "";
        switch (this) {
            case YEAR:
                chineseName = String.format(this.getChineseNameFormat(), dateTime.getYear());
                break;
            case QUARTER:
                Quarter quarter = LocalDateTimeUtils.queryQuarter(dateTime);
                chineseName = String.format(this.getChineseNameFormat(), quarter.getValue());
                break;
            case MONTH:
                chineseName = String.format(this.getChineseNameFormat(), dateTime.getMonth().getValue());
                break;
            case WEEK:
            case WEEK_OF_MONTH:
                LocalDateTime mondayOfFirstWeekOfMonth = LocalDateTimeUtils.mondayOfFirstWeekOfMonth(dateTime);
                int weekNumber = (dateTime.getDayOfMonth() - mondayOfFirstWeekOfMonth.getDayOfMonth()) / 7 + 1;
                chineseName = String.format(this.getChineseNameFormat(), dateTime.getMonth().getValue(), weekNumber);
                break;
            case DAY:
                chineseName = String.format(this.getChineseNameFormat(), dateTime.getDayOfMonth());
                break;
            case DAY_OF_WEEK:
                chineseName = weekChineseNameMemo.get(dateTime.getDayOfWeek().getValue());
                break;
            case HOUR:
                chineseName = String.format(this.getChineseNameFormat(), dateTime.getHour());
                break;
            default:
                break;
        }
        return chineseName;
    }

}
