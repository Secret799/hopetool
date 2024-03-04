package com.hopetool.core.datetime;


import com.hopetool.core.array.ArraysUtils;
import com.hopetool.core.datetime.support.DatePattern;
import com.hopetool.core.datetime.support.DateTimeUnit;
import com.hopetool.core.datetime.support.Month;
import com.hopetool.core.datetime.support.Quarter;
import com.hopetool.core.lang.AssertUtils;
import com.hopetool.core.text.StrUtils;
import com.hopetool.core.util.ObjectUtils;

import java.time.*;
import java.time.chrono.ChronoLocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalUnit;
import java.util.Date;
import java.util.TimeZone;

/**
 * 时间工具类，用于 {@link LocalDateTime}
 *
 * @author JunPzx
 */
public class LocalDateTimeUtils {

    /**
     * 空的 LocalDateTime 对象，主要用于 DB 唯一索引的默认值
     */
    public static LocalDateTime EMPTY = buildTime(1970, 1, 1);

    /**
     * 返回当前时间加上指定时间间隔后的时间
     *
     * @param duration 时间间隔
     * @return 加上指定时间间隔后的时间
     */
    public static LocalDateTime addTime(Duration duration) {
        return LocalDateTime.now().plus(duration);
    }


    /**
     * 判断给定的日期是否在当前时间之前
     *
     * @param date 要判断的日期
     * @return 如果给定的日期在当前时间之前，则返回true；否则返回false
     */
    public static boolean beforeNow(LocalDateTime date) {
        return date.isBefore(LocalDateTime.now());
    }


    /**
     * 判断给定的日期是否在当前时间之后
     *
     * @param date 给定的日期
     * @return 如果给定的日期在当前时间之后，则返回true；否则返回false
     */
    public static boolean afterNow(LocalDateTime date) {
        return date.isAfter(LocalDateTime.now());
    }


    /**
     * {@link Instant}转{@link LocalDateTime}，使用默认时区
     *
     * @param instant {@link Instant}
     * @return {@link LocalDateTime}
     */
    public static LocalDateTime of(Instant instant) {
        return of(instant, ZoneId.systemDefault());
    }


    /**
     * {@link Instant}转{@link LocalDateTime}，使用UTC时区
     *
     * @param instant {@link Instant}
     * @return {@link LocalDateTime}
     */
    public static LocalDateTime ofUTC(Instant instant) {
        return of(instant, ZoneId.of("UTC"));
    }

    /**
     * {@link ZonedDateTime}转{@link LocalDateTime}
     *
     * @param zonedDateTime {@link ZonedDateTime}
     * @return {@link LocalDateTime}
     */
    public static LocalDateTime of(ZonedDateTime zonedDateTime) {
        if (null == zonedDateTime) {
            return null;
        }
        return zonedDateTime.toLocalDateTime();
    }

    /**
     * {@link Instant}转{@link LocalDateTime}
     *
     * @param instant {@link Instant}
     * @param zoneId  时区
     * @return {@link LocalDateTime}
     */
    public static LocalDateTime of(Instant instant, ZoneId zoneId) {
        if (null == instant) {
            return null;
        }

        return LocalDateTime.ofInstant(instant, ObjectUtils.defaultIfNull(zoneId, ZoneId::systemDefault));
    }

    /**
     * {@link Instant}转{@link LocalDateTime}
     *
     * @param instant  {@link Instant}
     * @param timeZone 时区
     * @return {@link LocalDateTime}
     */
    public static LocalDateTime of(Instant instant, TimeZone timeZone) {
        if (null == instant) {
            return null;
        }

        return of(instant, ObjectUtils.defaultIfNull(timeZone, TimeZone::getDefault).toZoneId());
    }

    /**
     * 毫秒转{@link LocalDateTime}，使用默认时区
     *
     * <p>注意：此方法使用默认时区，如果非UTC，会产生时间偏移</p>
     *
     * @param epochMilli 从1970-01-01T00:00:00Z开始计数的毫秒数
     * @return {@link LocalDateTime}
     */
    public static LocalDateTime of(long epochMilli) {
        return of(Instant.ofEpochMilli(epochMilli));
    }

    /**
     * 毫秒转{@link LocalDateTime}，使用UTC时区
     *
     * @param epochMilli 从1970-01-01T00:00:00Z开始计数的毫秒数
     * @return {@link LocalDateTime}
     */
    public static LocalDateTime ofUTC(long epochMilli) {
        return ofUTC(Instant.ofEpochMilli(epochMilli));
    }

    /**
     * 毫秒转{@link LocalDateTime}，根据时区不同，结果会产生时间偏移
     *
     * @param epochMilli 从1970-01-01T00:00:00Z开始计数的毫秒数
     * @param zoneId     时区
     * @return {@link LocalDateTime}
     */
    public static LocalDateTime of(long epochMilli, ZoneId zoneId) {
        return of(Instant.ofEpochMilli(epochMilli), zoneId);
    }

    /**
     * 毫秒转{@link LocalDateTime}，结果会产生时间偏移
     *
     * @param epochMilli 从1970-01-01T00:00:00Z开始计数的毫秒数
     * @param timeZone   时区
     * @return {@link LocalDateTime}
     */
    public static LocalDateTime of(long epochMilli, TimeZone timeZone) {
        return of(Instant.ofEpochMilli(epochMilli), timeZone);
    }

    /**
     * {@link Date}转{@link LocalDateTime}，使用默认时区
     *
     * @param date Date对象
     * @return {@link LocalDateTime}
     */
    public static LocalDateTime of(Date date) {
        if (null == date) {
            return null;
        }
        return of(date.toInstant());
    }

    /**
     * {@link TemporalAccessor}转{@link LocalDateTime}，使用默认时区
     *
     * @param temporalAccessor {@link TemporalAccessor}
     * @return {@link LocalDateTime}
     */
    public static LocalDateTime of(TemporalAccessor temporalAccessor) {
        if (null == temporalAccessor) {
            return null;
        }
        if (temporalAccessor instanceof LocalDate) {
            return ((LocalDate) temporalAccessor).atStartOfDay();
        } else if (temporalAccessor instanceof Instant) {
            return LocalDateTime.ofInstant((Instant) temporalAccessor, ZoneId.systemDefault());
        } else if (temporalAccessor instanceof ZonedDateTime) {
            return ((ZonedDateTime) temporalAccessor).toLocalDateTime();
        }
        return LocalDateTime.of(TemporalAccessorUtil.get(temporalAccessor, ChronoField.YEAR), TemporalAccessorUtil.get(temporalAccessor, ChronoField.MONTH_OF_YEAR), TemporalAccessorUtil.get(temporalAccessor, ChronoField.DAY_OF_MONTH), TemporalAccessorUtil.get(temporalAccessor, ChronoField.HOUR_OF_DAY), TemporalAccessorUtil.get(temporalAccessor, ChronoField.MINUTE_OF_HOUR), TemporalAccessorUtil.get(temporalAccessor, ChronoField.SECOND_OF_MINUTE), TemporalAccessorUtil.get(temporalAccessor, ChronoField.NANO_OF_SECOND));
    }

    /**
     * 复制一个新的LocalDateTime
     *
     * @param benchmarkTime 时间
     * @return 新的时间
     */
    public static LocalDateTime copyLocalDateTime(LocalDateTime benchmarkTime) {
        return LocalDateTime.of(benchmarkTime.toLocalDate(), benchmarkTime.toLocalTime());
    }


    /**
     * 创建指定时间
     *
     * @param year  年
     * @param mouth 月
     * @param day   日
     * @return 指定时间
     */
    public static LocalDateTime buildTime(int year, int mouth, int day) {
        return LocalDateTime.of(year, mouth, day, 0, 0, 0);
    }

    /**
     * 生成一个LocalDateTime数组，数组中包含两个元素，分别是buildTime(year1, mouth1, day1)和buildTime(year2, mouth2, day2)返回的时间
     *
     * @param year1  起始时间的年份
     * @param mouth1 起始时间的月份
     * @param day1   起始时间的天数
     * @param year2  结束时间的年份
     * @param mouth2 结束时间的月份
     * @param day2   结束时间的天数
     * @return 包含两个元素的LocalDateTime数组，第一个元素为起始时间，第二个元素为结束时间
     */
    public static LocalDateTime[] buildBetweenTime(int year1, int mouth1, int day1, int year2, int mouth2, int day2) {
        return new LocalDateTime[]{buildTime(year1, mouth1, day1), buildTime(year2, mouth2, day2)};
    }

    /**
     * 判断当前时间（默认时区）是否在指定范围内<br>
     * 起始时间和结束时间可以互换<br>
     * 通过includeBegin, includeEnd参数控制时间范围区间是否为开区间，例如：传入参数：includeBegin=true, includeEnd=false，
     * 则本方法会判断 date ∈ (beginDate, endDate] 是否成立
     *
     * @param date         被判定的日期
     * @param beginDate    起始时间（包含）
     * @param endDate      结束时间（包含）
     * @param includeBegin 时间范围是否包含起始时间
     * @param includeEnd   时间范围是否包含结束时间
     * @return 是否在范围内
     */
    public static boolean isBetween(ChronoLocalDateTime<?> date, ChronoLocalDateTime<?> beginDate, ChronoLocalDateTime<?> endDate, boolean includeBegin, boolean includeEnd) {
        return TemporalAccessorUtil.isIn(date, beginDate, endDate, includeBegin, includeEnd);
    }

    /**
     * 判断当前时间是否在该时间范围内
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 是否
     */
    public static boolean isBetween(LocalDateTime startTime, LocalDateTime endTime) {
        if (startTime == null || endTime == null) {
            return false;
        }
        return TemporalAccessorUtil.isIn(LocalDateTime.now(), startTime, endTime);
    }


    /**
     * 判断当前时间是否在该时间范围内
     *
     * @param compareDateTime 待比较的时间
     * @param startTime       开始时间
     * @param endTime         结束时间
     * @return 如果待比较的时间在指定的时间范围内，则返回true；否则返回false
     */
    public static boolean isBetween(LocalDateTime compareDateTime, LocalDateTime startTime, LocalDateTime endTime) {
        if (startTime == null || endTime == null) {
            return false;
        }
        return TemporalAccessorUtil.isIn(compareDateTime, startTime, endTime);
    }


    /**
     * 判断给定的日期是否在指定的时间段内
     *
     * @param compareDateTime 待比较的日期时间对象
     * @param startTime       指定时间段的开始时间
     * @param endTime         指定时间段的结束时间
     * @return 若给定的日期在指定时间段内，则返回true；否则返回false
     */
    public static boolean isBetween(Date compareDateTime, LocalDateTime startTime, LocalDateTime endTime) {
        return TemporalAccessorUtil.isIn(of(compareDateTime), startTime, endTime);
    }

    /**
     * 判断当前时间是否在该时间范围内
     *
     * @param compareDateTime 待比较的时间
     * @param timeFrame       时间区间
     * @return 如果待比较的时间在指定的时间范围内，则返回true；否则返回false
     */
    public static boolean isBetween(LocalDateTime compareDateTime, LocalDateTime[] timeFrame) {
        if (ArraysUtils.isEmpty(timeFrame) || timeFrame.length < 2) {
            return false;
        }
        return TemporalAccessorUtil.isIn(compareDateTime, timeFrame[0], timeFrame[1]);
    }


    /**
     * 判断给定的日期是否在指定的时间段内
     *
     * @param compareDateTime 待比较的日期时间对象
     * @param timeFrame       时间区间
     * @return 若给定的日期在指定时间段内，则返回true；否则返回false
     */
    public static boolean isBetween(Date compareDateTime, LocalDateTime[] timeFrame) {
        return isBetween(of(compareDateTime), timeFrame);
    }


    /**
     * <p>
     * 根据基准时间,偏移量,时间单位进行时间偏移,然后将偏移后的时间根据时间单位获取开始时间
     * </p>
     * <p>
     * 例如: benchmark=2018-01-20 10:20:16, offset=1, dateTimeUnit=DAY
     * <li>第一步: 往后偏移一天 2018-01-21 10:20:16</li>
     * <li>第二步: 获取当天开始时间 2018-01-21 00:00:00</li>
     * </p>
     * <p>
     * 例如: benchmark=2018-01-20 10:20:16, offset=1, dateTimeUnit=MONTH
     * <li>第一步: 往后偏移一月 2018-02-20 10:20:16</li>
     * <li>第二步: 获取当月开始时间 2018-02-01 00:00:00</li>
     * </p>
     * <p>
     * 例如: benchmark=2018-01-20 10:20:16, offset=-1, dateTimeUnit=MONTH
     * <li>第一步: 往前偏移一月 2017-12-20 10:20:16</li>
     * <li>第二步: 获取当月开始时间 2017-12-01 00:00:00</li>
     *
     * @param benchmark    基准时间
     * @param offset       偏移量
     * @param dateTimeUnit 时间单位
     * @return 开始时间
     */
    public static LocalDateTime offsetAndBeginTimeByDateTimeUnit(LocalDateTime benchmark, int offset, DateTimeUnit dateTimeUnit) {
        LocalDateTime offsetDateTime = offset(benchmark, offset, dateTimeUnit);
        return beginTimeByDateTimeUnit(offsetDateTime, dateTimeUnit);
    }


    /**
     * <p>
     * 根据基准时间,偏移量,时间单位进行时间偏移,然后将偏移后的时间根据时间单位获取结束时间
     * </p>
     * <p>
     * 例如: benchmark=2018-01-20 10:20:16, offset=1, dateTimeUnit=DAY
     * <li>第一步: 往后偏移一天 2018-01-21 10:20:16</li>
     * <li>第二步: 获取当天结束时间 2018-01-21 23:59:59</li>
     * </p>
     * <p>
     * 例如: benchmark=2018-01-20 10:20:16, offset=1, dateTimeUnit=MONTH
     * <li>第一步: 往后偏移一月 2018-02-20 10:20:16</li>
     * <li>第二步: 获取当月结束时间 2018-02-28 23:59:59</li>
     * </p>
     * <p>
     * 例如: benchmark=2018-01-20 10:20:16, offset=-1, dateTimeUnit=MONTH
     * <li>第一步: 往前偏移一月 2017-12-20 10:20:16</li>
     * <li>第二步: 获取当月结束时间 2017-12-31 23:59:59</li>
     *
     * @param benchmark    基准时间
     * @param offset       偏移量
     * @param dateTimeUnit 时间单位
     * @return 结束时间
     */
    public static LocalDateTime offsetAndEndTimeByDateTimeUnit(LocalDateTime benchmark, int offset, DateTimeUnit dateTimeUnit) {
        LocalDateTime offsetDateTime = offset(benchmark, offset, dateTimeUnit);
        return endTimeByDateTimeUnit(offsetDateTime, dateTimeUnit);
    }


    /**
     * 根据指定的日期时间单位，返回指定日期时间的开始时间
     *
     * @param benchmark    基准日期时间
     * @param dateTimeUnit 日期时间单位
     * @return 开始时间
     */
    public static LocalDateTime beginTimeByDateTimeUnit(LocalDateTime benchmark, DateTimeUnit dateTimeUnit) {
        switch (dateTimeUnit) {
            case YEAR:
                return beginOfYear(benchmark);
            case QUARTER:
                return beginOfQuarter(benchmark);
            case MONTH:
                return beginOfMonth(benchmark);
            case DAY:
            case DAY_OF_WEEK:
                return beginOfDay(benchmark);
            case WEEK:
                return beginOfWeek(benchmark);
            case WEEK_OF_MONTH:
                return beginOfWeekOfMonth(benchmark);
            case HOUR:
                return beginOfHour(benchmark);
            default:
                throw new IllegalArgumentException("THE DATE TIME UNIT IS ILLEGAL");
        }
    }


    /**
     * 根据指定的日期时间单位，返回结束时间
     *
     * @param benchmark           基准时间
     * @param dateTimeUnit        日期时间单位
     * @param truncateMillisecond 是否截断毫秒数
     * @return 结束时间
     */
    public static LocalDateTime endTimeByDateTimeUnit(LocalDateTime benchmark, DateTimeUnit dateTimeUnit, boolean truncateMillisecond) {
        switch (dateTimeUnit) {
            case YEAR:
                return endOfYear(benchmark, truncateMillisecond);
            case QUARTER:
                return endOfQuarter(benchmark, truncateMillisecond);
            case MONTH:
                return endOfMonth(benchmark, truncateMillisecond);
            case DAY:
            case DAY_OF_WEEK:
                return endOfDay(benchmark, truncateMillisecond);
            case WEEK:
                return endOfWeek(benchmark, truncateMillisecond);
            case WEEK_OF_MONTH:
                return endOfWeekOfMonth(benchmark, truncateMillisecond);
            case HOUR:
                return endOfHour(benchmark, truncateMillisecond);
            default:
                throw new IllegalArgumentException("THE DATE TIME UNIT IS ILLEGAL");
        }
    }


    /**
     * 根据指定的日期时间单位，返回结束时间
     *
     * @param benchmark    基准时间
     * @param dateTimeUnit 日期时间单位
     * @return 结束时间
     */
    public static LocalDateTime endTimeByDateTimeUnit(LocalDateTime benchmark, DateTimeUnit dateTimeUnit) {
        return endTimeByDateTimeUnit(benchmark, dateTimeUnit, false);
    }

    /**
     * 获取小时开始时间
     *
     * @param time 时间
     * @return 结果
     */
    public static LocalDateTime beginOfHour(LocalDateTime time) {
        int hour = time.getHour();
        LocalDateTime temp = time.with(LocalTime.MIN);
        return temp.withHour(hour);
    }


    /**
     * 返回指定时间的小时结束时间。
     *
     * @param time 要处理的本地日期时间
     * @return 处理后的本地日期时间，即小时结束时间
     */
    public static LocalDateTime endOfHour(LocalDateTime time) {
        return endOfHour(time, false);
    }

    /**
     * 返回指定时间的小时末时间。
     *
     * @param time                要处理的时间
     * @param truncateMillisecond 是否截断毫秒级时间
     * @return 处理后的结果时间
     */
    public static LocalDateTime endOfHour(LocalDateTime time, boolean truncateMillisecond) {
        int hour = time.getHour();
        if (truncateMillisecond) {
            return time.with(LocalTime.of(hour, 59, 59));
        }
        LocalDateTime temp = time.with(LocalTime.MAX);
        return temp.withHour(hour);
    }


    /**
     * 修改为一天的开始时间，例如：2020-02-02 00:00:00,000
     *
     * @param time 日期时间
     * @return 一天的开始时间
     */
    public static LocalDateTime beginOfDay(LocalDateTime time) {
        return time.with(LocalTime.MIN);
    }


    /**
     * 修改为一天的结束时间
     *
     * @param time 日期时间
     * @return 一天的结束时间
     */
    public static LocalDateTime endOfDay(LocalDateTime time) {
        return endOfDay(time, false);
    }

    /**
     * 修改为一天的结束时间，例如：
     * <ul>
     * 	<li>毫秒不归零：2020-02-02 23:59:59,999</li>
     * 	<li>毫秒归零：2020-02-02 23:59:59,000</li>
     * </ul>
     *
     * @param time                日期时间
     * @param truncateMillisecond 是否毫秒归零
     * @return 一天的结束时间
     * @since 5.7.18
     */
    public static LocalDateTime endOfDay(LocalDateTime time, boolean truncateMillisecond) {
        if (truncateMillisecond) {
            return time.with(LocalTime.of(23, 59, 59));
        }
        return time.with(LocalTime.MAX);
    }

    /**
     * 修改为一年的开始时间
     *
     * @param time 日期时间
     * @return 一年的开始时间
     */
    public static LocalDateTime beginOfYear(LocalDateTime time) {
        LocalDateTime temp = time.with(LocalTime.MIN);
        return temp.withMonth(1).withDayOfMonth(1);
    }

    /**
     * 修改为一年的结束时间
     *
     * @param time 日期时间
     * @return 一年的结束时间
     */
    public static LocalDateTime endOfYear(LocalDateTime time) {
        return endOfYear(time, false);
    }

    /**
     * 返回指定时区的今年最后一天的时间
     *
     * @param time                指定的时间
     * @param truncateMillisecond 是否截断毫秒级别的时间
     * @return 今年最后一天的时间
     */
    public static LocalDateTime endOfYear(LocalDateTime time, boolean truncateMillisecond) {
        if (truncateMillisecond) {
            return time.withMonth(12).withDayOfMonth(31).with(LocalTime.of(23, 59, 59));
        }
        LocalDateTime temp = time.with(LocalTime.MAX);
        return temp.withMonth(12).withDayOfMonth(31);
    }


    /**
     * 修改为一月的开始间
     *
     * @param time 日期时间
     * @return 一月的开始时间
     */
    public static LocalDateTime beginOfMonth(LocalDateTime time) {
        LocalDateTime temp = time.with(LocalTime.MIN);
        return temp.withDayOfMonth(1);
    }

    /**
     * 修改为一月的结束时间
     *
     * @param time 日期时间
     * @return 一月的结束时间
     */
    public static LocalDateTime endOfMonth(LocalDateTime time) {
        return endOfMonth(time, false);
    }


    /**
     * 返回给定日期所在月份的最后一天的本地日期时间。
     *
     * @param time                本地日期时间
     * @param truncateMillisecond 是否截断毫秒级时间
     * @return 返回给定日期所在月份的最后一天的本地日期时间
     */
    public static LocalDateTime endOfMonth(LocalDateTime time, boolean truncateMillisecond) {
        LocalDateTime temp = time.with(LocalTime.MAX);
        // 如果需要截断毫秒级时间，则将时间截断为23点59分59秒
        if (truncateMillisecond) {
            temp = temp.with(LocalTime.of(23, 59, 59));
        }
        // 根据给定月份的天数计算最后一天的日期
        int lastDay = Month.getLastDay(temp.getMonthValue() - 1, Year.isLeap(temp.getYear()));
        return temp.withDayOfMonth(lastDay);
    }


    /**
     * 获取本周一的时间
     *
     * @param benchmarkTime 时间基准
     * @return 忽而故意
     */
    public static LocalDateTime beginOfWeek(LocalDateTime benchmarkTime) {
        int weekValue = benchmarkTime.getDayOfWeek().getValue();
        LocalDateTime beginWeekDateTime = offset(benchmarkTime, -(weekValue - 1), ChronoUnit.DAYS);
        return beginOfDay(beginWeekDateTime);
    }


    /**
     * 获取一周的结束时间
     *
     * @param benchmarkTime 基准时间
     * @return 一周的结束时间
     */
    public static LocalDateTime endOfWeek(LocalDateTime benchmarkTime) {
        return endOfWeek(benchmarkTime, false);
    }


    /**
     * 获取一周的结束时间
     *
     * @param benchmarkTime       基准时间
     * @param truncateMillisecond 是否截断毫秒级时间
     * @return 一周的结束时间
     */
    public static LocalDateTime endOfWeek(LocalDateTime benchmarkTime, boolean truncateMillisecond) {
        // 获取基准时间的星期几
        int weekValue = benchmarkTime.getDayOfWeek().getValue();
        // 根据基准时间计算一周的结束时间
        LocalDateTime endWeekDateTime = offset(benchmarkTime, 7 - weekValue, ChronoUnit.DAYS);
        // 获取一周的结束时间的一天的结束时间
        return endOfDay(endWeekDateTime, truncateMillisecond);
    }


    /**
     * 获取当月本周一的时间(注意如果传入的时间为第零周,也就是上一周的时间,那么可能存在返回结果大于传入参数的情况)
     * 例如: 传入时间 2023-09-02  周六
     * 返回结果: 2023-09-04 周一
     *
     * @param benchmarkTime 传入参数
     * @return 返回结果
     */
    public static LocalDateTime beginOfWeekOfMonth(LocalDateTime benchmarkTime) {
        LocalDateTime temp = beginOfWeek(benchmarkTime);
        LocalDateTime mondayOfFirstWeekOfMonth = mondayOfFirstWeekOfMonth(benchmarkTime);
        return temp.isBefore(mondayOfFirstWeekOfMonth) ? mondayOfFirstWeekOfMonth : temp;
    }

    public static LocalDateTime endOfWeekOfMonth(LocalDateTime benchmarkTime) {
        LocalDateTime temp = endOfWeek(benchmarkTime);
        LocalDateTime sundayOfLastWeekOfMonth = sundayOfLastWeekOfMonth(benchmarkTime);
        return temp.isAfter(sundayOfLastWeekOfMonth) ? sundayOfLastWeekOfMonth : temp;
    }

    public static LocalDateTime endOfWeekOfMonth(LocalDateTime benchmarkTime, boolean truncateMillisecond) {
        LocalDateTime temp = endOfWeek(benchmarkTime, truncateMillisecond);
        LocalDateTime sundayOfLastWeekOfMonth = sundayOfLastWeekOfMonth(benchmarkTime);
        return temp.isAfter(sundayOfLastWeekOfMonth) ? sundayOfLastWeekOfMonth : temp;
    }

    /**
     * 返回给定时间所在季度的开始时间
     *
     * @param benchmarkTime 给定的时间
     * @return 季度的开始时间
     */
    public static LocalDateTime beginOfQuarter(LocalDateTime benchmarkTime) {
        Month month = Month.of(benchmarkTime.getMonth().firstMonthOfQuarter());
        AssertUtils.notNull(month, "The month of the given time is invalid.");
        return LocalDateTime.of(LocalDate.of(benchmarkTime.getYear(), month.getValue(), 1), LocalTime.MIN);
    }


    /**
     * 返回给定基准时间所在季度的结束时间
     *
     * @param benchmarkTime 基准时间
     * @return 给定基准时间所在季度的结束时间
     */
    public static LocalDateTime endOfQuarter(LocalDateTime benchmarkTime) {
        return endOfQuarter(benchmarkTime, false);
    }


    /**
     * 返回给定基准时间所在季度的结束时间
     *
     * @param benchmarkTime       给定的基准时间
     * @param truncateMillisecond 是否舍弃结束时间的毫秒部分
     * @return 当给定基准时间所在季度的结束时间
     */
    public static LocalDateTime endOfQuarter(LocalDateTime benchmarkTime, boolean truncateMillisecond) {
        // 获取给定基准时间的日期部分
        LocalDate localDate = benchmarkTime.toLocalDate();
        // 获取给定基准时间所在季度的下一个月，即未来的两个月
        Month month = Month.of(localDate.getMonth().firstMonthOfQuarter()).plus(2L);
        // 通过给定季度的年份、月份和月份长度创建一个结束时间的LocalDateTime对象
        if (truncateMillisecond) {
            return LocalDateTime.of(LocalDate.of(localDate.getYear(), month.getValueBaseOne(), month.length(localDate.isLeapYear())), LocalTime.of(23, 59, 59));
        }
        return LocalDateTime.of(LocalDate.of(localDate.getYear(), month.getValueBaseOne(), month.length(localDate.isLeapYear())), LocalTime.MAX);
    }

    /**
     * 获取给定时间所在季度
     *
     * @param benchmarkTime 给定时间
     * @return 季度
     */
    public static Quarter queryQuarter(LocalDateTime benchmarkTime) {
        return Quarter.ofByMonth(benchmarkTime.getMonth());
    }

    /**
     * 获取某月第一周的周一
     *
     * @return 周一日期
     */
    public static LocalDateTime mondayOfFirstWeekOfMonth(LocalDateTime benchmarkTime) {
        // 获取日期的号数
        int dayOfMonth = benchmarkTime.getDayOfMonth();
        // 获取日期为周几
        int dayOfWeek = benchmarkTime.getDayOfWeek().getValue();
        // 本周一的xx号 例如 2023/9/4号为九月份第一周第一天
        int thisMondayDay = (dayOfMonth - dayOfWeek) + 1;
        // 如果当天为周一,且在7号以前,则直接返回
        if (thisMondayDay <= 7) {
            return beginOfDay(benchmarkTime.withDayOfMonth(thisMondayDay));
        }
        // 本月第一周第一天 xx号
        int monthFirstWeekMonday = thisMondayDay % 7;
        return beginOfDay(benchmarkTime.withDayOfMonth(monthFirstWeekMonday == 0 ? 7 : monthFirstWeekMonday));
    }


    /**
     * 获取某月最后一周的周天
     *
     * @return 周天日期
     */
    public static LocalDateTime sundayOfLastWeekOfMonth(LocalDateTime benchmarkTime) {
        return sundayOfLastWeekOfMonth(benchmarkTime, false);
    }

    /**
     * 返回给定日期的当前月最后一周的周日
     *
     * @param benchmarkTime       日期时间
     * @param truncateMillisecond 是否截断毫秒数
     * @return 返回上个月最后一周的周日
     */
    public static LocalDateTime sundayOfLastWeekOfMonth(LocalDateTime benchmarkTime, boolean truncateMillisecond) {
        // 拿到本月最后一天
        LocalDateTime lastDayOfThisMonth = endOfMonth(benchmarkTime, truncateMillisecond);
        // 如果本月的最后一天为周日,则直接返回
        if (lastDayOfThisMonth.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
            return lastDayOfThisMonth;
        }
        // 本月最后一周周日
        int sundayOfLastWeekOfMonth = lastDayOfThisMonth.getDayOfMonth() - lastDayOfThisMonth.getDayOfWeek().getValue();
        return endOfDay(benchmarkTime.withDayOfMonth(sundayOfLastWeekOfMonth), truncateMillisecond);
    }

    /**
     * 日期偏移,根据field不同加不同值（偏移会修改传入的对象）
     *
     * @param time   {@link LocalDateTime}
     * @param number 偏移量，正数为向后偏移，负数为向前偏移
     * @param field  偏移单位，见{@link ChronoUnit}，不能为null
     * @return 偏移后的日期时间
     */
    public static LocalDateTime offset(LocalDateTime time, long number, TemporalUnit field) {
        return TemporalUtil.offset(time, number, field);
    }

    /**
     * 偏移时间(注意,如果是DAY_OF_WEEK,偏移则是按照天偏移)
     *
     * @param localDateTime 时间
     * @param offset        偏移量
     * @param dateTimeUnit  单位
     * @return 偏移后时间
     */
    public static LocalDateTime offset(LocalDateTime localDateTime, long offset, DateTimeUnit dateTimeUnit) {
        switch (dateTimeUnit) {
            case YEAR:
                return offset(localDateTime, offset, ChronoUnit.YEARS);
            case QUARTER:
                return offset(localDateTime, offset * 3, ChronoUnit.MONTHS);
            case MONTH:
                return offset(localDateTime, offset, ChronoUnit.MONTHS);
            case DAY:
                return offset(localDateTime, offset, ChronoUnit.DAYS);
            case DAY_OF_WEEK:
                return offsetDayOfWeek(localDateTime, offset);
            case WEEK:
                return offset(localDateTime, offset, ChronoUnit.WEEKS);
            case WEEK_OF_MONTH:
                return offsetWeekOfMonth(localDateTime, offset);
            case HOUR:
                return offset(localDateTime, offset, ChronoUnit.HOURS);
            default:
                throw new IllegalArgumentException();
        }
    }


    /**
     * 根据指定的偏移量在一周内移动给定的日期
     * </p>
     * <li>例如: 当前时间为周三(2023-12-20) 偏移量为 1   结果  周四(2023-12-21)</li>
     * <li>例如: 当前时间为周三(2023-12-20) 偏移量为 -1 结果  周二(2023-12-19)</li>
     * <li>例如: 当前时间为周三(2023-12-20) 偏移量为 6   结果  周日(2023-12-24)</li>
     * <li>例如: 当前时间为周三(2023-12-20) 偏移量为 -6 结果  周一(2023-12-18)</li>
     * </p>
     *
     * @param localDateTime 本地日期时间
     * @param offset        偏移量，正数向后移动，负数向前移动
     * @return 移动后的日期时间
     */
    public static LocalDateTime offsetDayOfWeek(LocalDateTime localDateTime, long offset) {
        int dayOfWeek = localDateTime.getDayOfWeek().getValue();
        if (offset > (7 - dayOfWeek)) {
            return offset(localDateTime, 7 - dayOfWeek, ChronoUnit.DAYS);
        }
        if (offset < -dayOfWeek) {
            return offset(localDateTime, -(dayOfWeek - 1), ChronoUnit.DAYS);
        }
        return offset(localDateTime, offset, ChronoUnit.DAYS);
    }


    /**
     * 在一个月中的某一周偏移指定天数
     *
     * @param localDateTime 传入的日期时间
     * @param offset        偏移天数，正数表示向前偏移，负数表示向后偏移
     * @return 偏移后的日期时间
     */
    public static LocalDateTime offsetWeekOfMonth(LocalDateTime localDateTime, long offset) {
        if (offset > 0) {
            LocalDateTime temp = offset(localDateTime, offset, ChronoUnit.WEEKS);
            LocalDateTime sundayOfLastWeekOfMonth = LocalDateTimeUtils.sundayOfLastWeekOfMonth(localDateTime);
            return temp.isAfter(sundayOfLastWeekOfMonth) ? sundayOfLastWeekOfMonth : temp;
        } else if (offset < 0) {
            LocalDateTime temp = offset(localDateTime, offset, ChronoUnit.WEEKS);
            LocalDateTime mondayOfFirstWeekOfMonth = LocalDateTimeUtils.mondayOfFirstWeekOfMonth(localDateTime);
            return temp.isBefore(mondayOfFirstWeekOfMonth) ? mondayOfFirstWeekOfMonth : temp;
        }
        return localDateTime;
    }


    /**
     * 格式化日期时间为yyyy-MM-dd HH:mm:ss格式
     *
     * @param time {@link LocalDateTime}
     * @return 格式化后的字符串
     * @since 5.3.11
     */
    public static String formatNormal(LocalDateTime time) {
        return format(time, DatePattern.NORM_DATETIME_FORMATTER);
    }

    /**
     * 格式化日期时间为指定格式
     *
     * @param time      {@link LocalDateTime}
     * @param formatter 日期格式化器，预定义的格式见：{@link DateTimeFormatter}
     * @return 格式化后的字符串
     */
    public static String format(LocalDateTime time, DateTimeFormatter formatter) {
        return TemporalAccessorUtil.format(time, formatter);
    }

    /**
     * 格式化日期时间为指定格式
     *
     * @param time   {@link LocalDateTime}
     * @param format 日期格式，类似于yyyy-MM-dd HH:mm:ss,SSS
     * @return 格式化后的字符串
     */
    public static String format(LocalDateTime time, String format) {
        return TemporalAccessorUtil.format(time, format);
    }


    /**
     * 解析日期时间字符串为{@link LocalDateTime}，仅支持yyyy-MM-dd'T'HH:mm:ss格式，例如：2007-12-03T10:15:30<br>
     * 即{@link DateTimeFormatter#ISO_LOCAL_DATE_TIME}
     *
     * @param text 日期时间字符串
     * @return {@link LocalDateTime}
     */
    public static LocalDateTime parse(String text) {
        return parse(text, (DateTimeFormatter) null);
    }

    /**
     * 解析日期时间字符串为{@link LocalDateTime}，格式支持日期时间、日期、时间<br>
     * 如果formatter为{@code null}，则使用{@link DateTimeFormatter#ISO_LOCAL_DATE_TIME}
     *
     * @param text      日期时间字符串
     * @param formatter 日期格式化器，预定义的格式见：{@link DateTimeFormatter}
     * @return {@link LocalDateTime}
     */
    public static LocalDateTime parse(String text, DateTimeFormatter formatter) {
        if (StrUtils.isBlank(text)) {
            return null;
        }
        if (null == formatter) {
            return LocalDateTime.parse(text);
        }

        return of(formatter.parse(text));
    }

    /**
     * 解析日期时间字符串为{@link LocalDateTime}
     *
     * @param text   日期时间字符串
     * @param format 日期格式，类似于yyyy-MM-dd HH:mm:ss,SSS
     * @return {@link LocalDateTime}
     */
    public static LocalDateTime parse(String text, String format) {
        if (StrUtils.isBlank(text)) {
            return null;
        }
        DateTimeFormatter formatter = null;
        if (StrUtils.isNotBlank(format)) {
            // 修复yyyyMMddHHmmssSSS格式不能解析的问题
            //see https://stackoverflow.com/questions/22588051/is-java-time-failing-to-parse-fraction-of-second
            // jdk8 bug at: https://bugs.openjdk.java.net/browse/JDK-8031085
            if (StrUtils.startWith(format, DatePattern.PURE_DATETIME_PATTERN, false, true)) {
                final String fraction = StrUtils.removePrefix(format, DatePattern.PURE_DATETIME_PATTERN);
                if (fraction.contains("S") || fraction.contains("SS")) {
                    //将yyyyMMddHHmmssS、yyyyMMddHHmmssSS的日期统一替换为yyyyMMddHHmmssSSS格式，用0补
                    text += StrUtils.repeat('0', 3 - fraction.length());
                }
                formatter = new DateTimeFormatterBuilder()
                        .appendPattern(DatePattern.PURE_DATETIME_PATTERN)
                        .appendValue(ChronoField.MILLI_OF_SECOND, 3)
                        .toFormatter();
            } else {
                formatter = DateTimeFormatter.ofPattern(format);
            }
        }
        return parse(text, formatter);
    }

    /**
     * 根据当前时间(基准时间)以及偏移量和和单位获取起点时间和终点时间(以基准时间为主条件)
     * <p>
     * 使用场景: 需要获取近几年的时间段
     *
     * <li>1. 如果offset > 0,则"返回结果[0]->开始时间"为 now根据datetimeUnit
     * 调用 {@link LocalDateTimeUtils#beginTimeByDateTimeUnit}获取起点时间,
     * "返回结果[1]->结束时间"为 now先offset往未来偏移指定datetimeUnit时间,
     * 然后获取偏移后的时间根据datetimeUnit获取终点时间
     * {@link LocalDateTimeUtils#offsetAndEndTimeByDateTimeUnit}
     * <p>
     * 例如: now: 2024-03-01T10:00:00  offset: 1  unit: year
     * 返回: [2024-01-01T00:00, 2025-12-31T23:59:59.999999999]
     * <li>2. 如果offset < 0,则"返回结果[0]->开始时间"为 now
     * 先offset往过去偏移指定datetimeUnit时间,然后获取偏移后的时间根据datetimeUnit
     * 获取起点时间 {@link LocalDateTimeUtils#offsetAndBeginTimeByDateTimeUnit},
     * "返回结果[1]->结束时间"为 now根据datetimeUnit调用
     * {@link LocalDateTimeUtils#endTimeByDateTimeUnit}获取终点时间
     * <p>
     * 例如: now: 2024-03-01T10:00:00  offset: -1  unit: year
     * 返回: [2023-01-01T00:00, 2024-12-31T23:59:59.999999999]
     * <li>3. 如果offset = 0,则"返回结果[0]->开始时间"为 now根据datetimeUnit
     * 调用{@link LocalDateTimeUtils#beginTimeByDateTimeUnit}
     * 获取起点时间,"返回结果[1]->结束时间"为 now根据datetimeUnit
     * 调用{@link LocalDateTimeUtils#endTimeByDateTimeUnit}获取终点时间
     * <p>
     * 例如: now: 2024-03-01T10:00:00  offset: 0  unit: year
     * 返回: [2024-01-01T00:00, 2024-12-31T23:59:59.999999999]
     *
     * @param dateTimeUnit 单位
     * @param offset       偏移量
     * @return 开始时间和结束时间
     */
    public static LocalDateTime[] generateTimePeriodByBenchmarkDateTime(DateTimeUnit dateTimeUnit, int offset) {
        return generateTimePeriodByBenchmarkDateTime(LocalDateTime.now(), dateTimeUnit, offset);
    }


    /**
     * 根据基准时间以及偏移量和和单位获取起点时间和终点时间(以基准时间为主条件)
     * <p>使用场景: 需要获取基准时间近几年或者未来几年的时间段
     *
     * <li>1. 如果offset > 0,则"返回结果[0]->开始时间"为 now根据datetimeUnit
     * 调用 {@link LocalDateTimeUtils#beginTimeByDateTimeUnit}获取起点时间,
     * "返回结果[1]->结束时间"为 now先offset往未来偏移指定datetimeUnit时间,
     * 然后获取偏移后的时间根据datetimeUnit获取终点时间
     * {@link LocalDateTimeUtils#offsetAndEndTimeByDateTimeUnit}
     * <p>
     * 例如: now: 2024-03-01T10:00:00  offset: 1  unit: year
     * 返回: [2024-01-01T00:00, 2025-12-31T23:59:59.999999999]
     * <li>2. 如果offset < 0,则"返回结果[0]->开始时间"为 now
     * 先offset往过去偏移指定datetimeUnit时间,然后获取偏移后的时间根据datetimeUnit
     * 获取起点时间 {@link LocalDateTimeUtils#offsetAndBeginTimeByDateTimeUnit},
     * "返回结果[1]->结束时间"为 now根据datetimeUnit调用
     * {@link LocalDateTimeUtils#endTimeByDateTimeUnit}获取终点时间
     * <p>
     * 例如: now: 2024-03-01T10:00:00  offset: -1  unit: year
     * 返回: [2023-01-01T00:00, 2024-12-31T23:59:59.999999999]
     * <li>3. 如果offset = 0,则"返回结果[0]->开始时间"为 now根据datetimeUnit
     * 调用{@link LocalDateTimeUtils#beginTimeByDateTimeUnit}
     * 获取起点时间,"返回结果[1]->结束时间"为 now根据datetimeUnit
     * 调用{@link LocalDateTimeUtils#endTimeByDateTimeUnit}获取终点时间
     * <p>
     * 例如: now: 2024-03-01T10:00:00  offset: 0  unit: year
     * 返回: [2024-01-01T00:00, 2024-12-31T23:59:59.999999999]
     *
     * @param dateTimeUnit 单位
     * @param offset       偏移量
     * @return 开始时间和结束时间
     */
    public static LocalDateTime[] generateTimePeriodByBenchmarkDateTime(LocalDateTime benchmarkDateTime, DateTimeUnit dateTimeUnit, int offset) {
        // 过去
        if (offset < 0) {
            LocalDateTime beginDateTime = LocalDateTimeUtils.offsetAndBeginTimeByDateTimeUnit(benchmarkDateTime, offset, dateTimeUnit);
            LocalDateTime endDateTime = LocalDateTimeUtils.endTimeByDateTimeUnit(benchmarkDateTime, dateTimeUnit);
            return new LocalDateTime[]{beginDateTime, endDateTime};
        }
        // 未来
        if (offset > 0) {
            LocalDateTime beginDateTime = LocalDateTimeUtils.beginTimeByDateTimeUnit(benchmarkDateTime, dateTimeUnit);
            LocalDateTime endDateTime = LocalDateTimeUtils.offsetAndEndTimeByDateTimeUnit(benchmarkDateTime, offset, dateTimeUnit);
            return new LocalDateTime[]{beginDateTime, endDateTime};
        }
        // 当前
        return new LocalDateTime[]{LocalDateTimeUtils.beginTimeByDateTimeUnit(benchmarkDateTime, dateTimeUnit), LocalDateTimeUtils.endTimeByDateTimeUnit(benchmarkDateTime, dateTimeUnit)};
    }

    /**
     * 根据基准时间以及偏移量和和单位获取起点时间和终点时间(以偏移量为主条件)
     *
     * <p>使用场景: 需要获取基准时间去年或者前年时间段
     *
     * @param benchmarkDateTime 基准时间
     * @param dateTimeUnit      时间单位
     * @param offset            偏移量
     * @param step              偏移后的时间为基准划定时间范围
     * @return 起点时间和终点时间
     */
    public static LocalDateTime[] generateTimePeriodByDateTimeUnit(LocalDateTime benchmarkDateTime, DateTimeUnit dateTimeUnit, int offset, int step) {
        LocalDateTime offsetDateTime = offset(benchmarkDateTime, offset, dateTimeUnit);
        return generateTimePeriodByBenchmarkDateTime(offsetDateTime, dateTimeUnit, step);
    }

    /**
     * 根据基准时间(当前时间)以及偏移量和和单位获取起点时间和终点时间(以偏移量为主条件)
     *
     * <p>使用场景: 需要获取基准时间去年或者前年时间段
     *
     * @param dateTimeUnit 时间单位
     * @param offset       偏移量
     * @param step         偏移后的时间为基准划定时间范围
     * @return 起点时间和终点时间
     */
    public static LocalDateTime[] generateTimePeriodByDateTimeUnit(DateTimeUnit dateTimeUnit, int offset, int step) {
        LocalDateTime offsetDateTime = offset(LocalDateTime.now(), offset, dateTimeUnit);
        return generateTimePeriodByBenchmarkDateTime(offsetDateTime, dateTimeUnit, step);
    }
}
