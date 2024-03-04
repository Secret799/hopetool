package com.hopetool.core.datetime.support;

import com.hopetool.core.lang.AssertUtils;

import java.time.format.TextStyle;
import java.util.Calendar;
import java.util.Locale;

/**
 * 月份枚举<br>
 * 与Calendar中的月份int值对应
 *
 * @author Looly
 * @see Calendar#JANUARY
 * @see Calendar#FEBRUARY
 * @see Calendar#MARCH
 * @see Calendar#APRIL
 * @see Calendar#MAY
 * @see Calendar#JUNE
 * @see Calendar#JULY
 * @see Calendar#AUGUST
 * @see Calendar#SEPTEMBER
 * @see Calendar#OCTOBER
 * @see Calendar#NOVEMBER
 * @see Calendar#DECEMBER
 * @see Calendar#UNDECIMBER
 */
public enum Month {
    /**
     * 一月
     */
    JANUARY(Calendar.JANUARY),
    /**
     * 二月
     */
    FEBRUARY(Calendar.FEBRUARY),
    /**
     * 三月
     */
    MARCH(Calendar.MARCH),
    /**
     * 四月
     */
    APRIL(Calendar.APRIL),
    /**
     * 五月
     */
    MAY(Calendar.MAY),
    /**
     * 六月
     */
    JUNE(Calendar.JUNE),
    /**
     * 七月
     */
    JULY(Calendar.JULY),
    /**
     * 八月
     */
    AUGUST(Calendar.AUGUST),
    /**
     * 九月
     */
    SEPTEMBER(Calendar.SEPTEMBER),
    /**
     * 十月
     */
    OCTOBER(Calendar.OCTOBER),
    /**
     * 十一月
     */
    NOVEMBER(Calendar.NOVEMBER),
    /**
     * 十二月
     */
    DECEMBER(Calendar.DECEMBER),
    /**
     * 十三月，仅用于农历
     */
    UNDECIMBER(Calendar.UNDECIMBER);

    // ---------------------------------------------------------------
    /**
     * Months aliases.
     */
    private static final String[] ALIASES = {"jan", "feb", "mar", "apr", "may", "jun", "jul", "aug", "sep", "oct", "nov", "dec"};
    private static final Month[] ENUMS = Month.values();

    /**
     * 对应值，见{@link Calendar}
     */
    private final int value;

    /**
     * 构造
     *
     * @param value 对应值，见{@link Calendar}
     */
    Month(int value) {
        this.value = value;
    }

    /**
     * 将 {@link Calendar}月份相关值转换为Month枚举对象<br>
     * 未找到返回{@code null}
     *
     * @param calendarMonthIntValue Calendar中关于Month的int值，从0开始
     * @return Month
     * @see Calendar#JANUARY
     * @see Calendar#FEBRUARY
     * @see Calendar#MARCH
     * @see Calendar#APRIL
     * @see Calendar#MAY
     * @see Calendar#JUNE
     * @see Calendar#JULY
     * @see Calendar#AUGUST
     * @see Calendar#SEPTEMBER
     * @see Calendar#OCTOBER
     * @see Calendar#NOVEMBER
     * @see Calendar#DECEMBER
     * @see Calendar#UNDECIMBER
     */
    public static Month of(int calendarMonthIntValue) {
        if (calendarMonthIntValue >= ENUMS.length || calendarMonthIntValue < 0) {
            throw new IllegalArgumentException("Illegal month value, value range from 0 to 11");
        }
        return ENUMS[calendarMonthIntValue];
    }

    /**
     * {@link java.time.Month}转换为Month对象
     *
     * @param month {@link java.time.Month}
     * @return Month
     * @since 5.8.0
     */
    public static Month of(java.time.Month month) {
        return of(month.ordinal());
    }

    /**
     * 获得指定月的最后一天
     *
     * @param month      月份，从0开始
     * @param isLeapYear 是否为闰年，闰年只对二月有影响
     * @return 最后一天，可能为28,29,30,31
     * @since 5.4.7
     */
    public static int getLastDay(int month, boolean isLeapYear) {
        final Month of = of(month);
        AssertUtils.notNull(of, "Invalid Month base 0: " + month);
        return of.getLastDay(isLeapYear);
    }

    /**
     * 获取{@link Calendar}中的对应值<br>
     * 此值从0开始，即0表示一月
     *
     * @return {@link Calendar}中的对应月份值，从0开始计数
     */
    public int getValue() {
        return this.value;
    }

    /**
     * 获取月份值，此值与{@link java.time.Month}对应<br>
     * 此值从1开始，即1表示一月
     *
     * @return 月份值，对应{@link java.time.Month}，从1开始计数
     * @since 5.7.21
     */
    public int getValueBaseOne() {
        AssertUtils.isFalse(this == UNDECIMBER, "Unsupported UNDECIMBER Field");
        return getValue() + 1;
    }

    /**
     * 获取此月份最后一天的值，不支持的月份（例如UNDECIMBER）返回-1
     *
     * @param isLeapYear 是否闰年
     * @return 此月份最后一天的值
     */
    public int getLastDay(boolean isLeapYear) {
        switch (this) {
            case FEBRUARY:
                return isLeapYear ? 29 : 28;
            case APRIL:
            case JUNE:
            case SEPTEMBER:
            case NOVEMBER:
                return 30;
            default:
                return 31;
        }
    }

    /**
     * 转换为{@link java.time.Month}
     *
     * @return {@link java.time.Month}
     * @since 5.7.21
     */
    public java.time.Month toJdkMonth() {
        return java.time.Month.of(getValueBaseOne());
    }

    /**
     * 获取显示名称
     *
     * @param style 名称风格
     * @return 显示名称
     * @since 5.8.0
     */
    public String getDisplayName(TextStyle style) {
        return getDisplayName(style, Locale.getDefault());
    }

    /**
     * 获取显示名称
     *
     * @param style  名称风格
     * @param locale {@link Locale}
     * @return 显示名称
     * @since 5.8.0
     */
    public String getDisplayName(TextStyle style, Locale locale) {
        return toJdkMonth().getDisplayName(style, locale);
    }


    /**
     * Gets the length of this month in days.
     * <p>
     * This takes a flag to determine whether to return the length for a leap year or not.
     * <p>
     * February has 28 days in a standard year and 29 days in a leap year.
     * April, June, September and November have 30 days.
     * All other months have 31 days.
     *
     * @param leapYear true if the length is required for a leap year
     * @return the length of this month in days, from 28 to 31
     */
    public int length(boolean leapYear) {
        switch (this) {
            case FEBRUARY:
                return (leapYear ? 29 : 28);
            case APRIL:
            case JUNE:
            case SEPTEMBER:
            case NOVEMBER:
                return 30;
            default:
                return 31;
        }
    }

    /**
     * Returns the month-of-year that is the specified number of quarters after this one.
     * <p>
     * The calculation rolls around the end of the year from December to January.
     * The specified period may be negative.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param months the months to add, positive or negative
     * @return the resulting month, not null
     */
    public Month plus(long months) {
        int amount = (int) (months % 12);
        return ENUMS[(ordinal() + (amount + 12)) % 12];
    }
}