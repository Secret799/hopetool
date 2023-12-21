package com.hopetool.core.statistics;


import com.hopetool.core.statistics.support.CalculationMethodEnum;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 数据计算工具类
 *
 * @author JunPzx
 * @since 2023/12/1 11:07
 */
public class DataCalculateUtils {

    /**
     * 根据给定的值创建一个新的BigDecimal对象。
     *
     * @param value 值对象
     * @param <V>   参数值类型
     * @return 新的BigDecimal对象
     * @throws IllegalArgumentException 如果不支持的类型
     */
    public static <V> BigDecimal newBigDecimal(V value) {
        if (value instanceof BigDecimal) {
            return (BigDecimal) value;
        }
        if (value instanceof String) {
            return new BigDecimal((String) value);
        }
        if (value instanceof Double || value instanceof Float || value instanceof Long || value instanceof Integer || value instanceof Short) {
            return BigDecimal.valueOf(Double.parseDouble(value.toString()));
        }
        throw new IllegalArgumentException("value UNSUPPORTED TYPES");
    }

    /**
     * 将给定的BigDecimal值转换为百分比
     *
     * @param value 要转换的BigDecimal值
     * @return 转换后的百分比BigDecimal值
     */
    public static BigDecimal valueToPercentageConversion(BigDecimal value) {
        return divide(value, newBigDecimal("100"), 2, RoundingMode.HALF_UP);
    }

    /**
     * 将给定的BigDecimal值转换为百分比
     *
     * @param value 要转换的BigDecimal值
     * @return 转换后的百分比BigDecimal值
     */
    public static BigDecimal valueToPercentageConversion(String value) {
        return valueToPercentageConversion(new BigDecimal(value));
    }

    /**
     * 将百分比转换为数值
     *
     * @param percentage 代表百分比的BigDecimal
     * @return 转换后的BigDecimal数值
     */
    public static BigDecimal percentageToValueConversion(BigDecimal percentage) {
        return multiply(percentage, newBigDecimal("100"));
    }

    /**
     * 将百分比转换为数值
     *
     * @param percentage 代表百分比的BigDecimal
     * @return 转换后的BigDecimal数值
     */
    public static BigDecimal percentageToValueConversion(String percentage) {
        return percentageToValueConversion(new BigDecimal(percentage));
    }

    /**
     * 加法运算。
     *
     * @param value1 加数
     * @param value2 被加数
     * @return 加法运算结果
     */
    public static BigDecimal add(BigDecimal value1, BigDecimal value2) {
        return value1.add(value2);
    }

    /**
     * 加法运算。
     *
     * @param value1 加数
     * @param value2 被加数
     * @return 加法运算结果
     */
    public static BigDecimal add(String value1, String value2) {
        return newBigDecimal(value1).add(newBigDecimal(value2));
    }


    /**
     * 加法运算。
     *
     * @param value1 加数
     * @param value2 被加数
     * @return 加法运算结果
     */
    public static BigDecimal add(BigDecimal value1, String value2) {
        return value1.add(newBigDecimal(value2));
    }

    /**
     * 加法运算。
     *
     * @param value1 加数
     * @param value2 被加数
     * @return 加法运算结果
     */
    public static BigDecimal add(String value1, BigDecimal value2) {
        return newBigDecimal(value1).add(value2);
    }


    /**
     * 减法运算
     *
     * @param value1 减数
     * @param value2 被减数
     * @return 减法操作的结果
     */
    public static BigDecimal subtract(BigDecimal value1, BigDecimal value2) {
        return value1.subtract(value2);
    }


    /**
     * 减法运算
     *
     * @param value1 减数
     * @param value2 被减数
     * @return 减法操作的结果
     */
    public static BigDecimal subtract(String value1, String value2) {
        return newBigDecimal(value1).subtract(newBigDecimal(value2));
    }


    /**
     * 减法运算
     *
     * @param value1 减数
     * @param value2 被减数
     * @return 减法操作的结果
     */
    public static BigDecimal subtract(BigDecimal value1, String value2) {
        return value1.subtract(newBigDecimal(value2));
    }


    /**
     * 乘法运算
     *
     * @param value1 乘数
     * @param value2 被乘数
     * @return 乘法运算结果
     */
    public static BigDecimal multiply(BigDecimal value1, BigDecimal value2) {
        return value1.multiply(value2);
    }


    /**
     * 乘法运算
     *
     * @param value1 乘数
     * @param value2 被乘数
     * @return 乘法运算结果
     */
    public static BigDecimal multiply(String value1, String value2) {
        return newBigDecimal(value1).multiply(newBigDecimal(value2));
    }


    /**
     * 乘法运算
     *
     * @param value1 乘数
     * @param value2 被乘数
     * @return 乘法运算结果
     */
    public static BigDecimal multiply(BigDecimal value1, String value2) {
        return value1.multiply(newBigDecimal(value2));
    }

    /**
     * 除法运算(如果除数为0,则直接返回0)
     *
     * @param value1       除数
     * @param value2       被除数
     * @param newScale     新的精度
     * @param roundingMode 要使用的舍入模式
     * @return 结果
     */
    public static BigDecimal divideIgnoreZero(BigDecimal value1, BigDecimal value2, int newScale, RoundingMode roundingMode) {
        if (value1.equals(BigDecimal.ZERO)) {
            return BigDecimal.ZERO;
        }
        return value1.divide(value2, newScale, roundingMode);
    }


    /**
     * 除法运算
     *
     * @param value1       除数
     * @param value2       被除数
     * @param newScale     新的精度
     * @param roundingMode 要使用的舍入模式
     * @return 结果
     */
    public static BigDecimal divide(BigDecimal value1, BigDecimal value2, int newScale, RoundingMode roundingMode) {
        return value1.divide(value2, newScale, roundingMode);
    }


    /**
     * 除法运算
     *
     * @param value1       除数
     * @param value2       被除数
     * @param newScale     新的精度
     * @param roundingMode 要使用的舍入模式
     * @return 结果
     */
    public static BigDecimal divide(String value1, String value2, int newScale, RoundingMode roundingMode) {
        return newBigDecimal(value1).divide(newBigDecimal(value2), newScale, roundingMode);
    }


    /**
     * 除法运算
     *
     * @param value1       除数
     * @param value2       被除数
     * @param newScale     新的精度
     * @param roundingMode 要使用的舍入模式
     * @return 结果
     */
    public static BigDecimal divide(BigDecimal value1, String value2, int newScale, RoundingMode roundingMode) {
        return value1.divide(newBigDecimal(value2), newScale, roundingMode);
    }


    /**
     * 除法运算，然后四舍五入保留两位小数。
     *
     * @param value1 被除数
     * @param value2 除数
     * @return 经过除法和四舍五入后的 BigDecimal 结果
     */
    public static BigDecimal divideAndScale(BigDecimal value1, BigDecimal value2) {
        return value1.divide(value2, 2, RoundingMode.HALF_UP);
    }


    /**
     * 除法运算，然后四舍五入保留两位小数。
     *
     * @param value1 除数
     * @param value2 被除数
     * @return 经过除法和四舍五入后的 BigDecimal 结果
     */
    public static BigDecimal divideAndScale(String value1, String value2) {
        return newBigDecimal(value1).divide(newBigDecimal(value2), 2, RoundingMode.HALF_UP);
    }


    /**
     * 除法运算，然后四舍五入保留两位小数。
     *
     * @param value1 除数
     * @param value2 被除数
     * @return 经过除法和四舍五入后的 BigDecimal 结果
     */
    public static BigDecimal divideAndScale(BigDecimal value1, String value2) {
        return value1.divide(newBigDecimal(value2), 2, RoundingMode.HALF_UP);
    }


    /**
     * 除法运算，然后四舍五入保留两位小数。
     *
     * @param value1 除数
     * @param value2 被除数
     * @return 经过除法和四舍五入后的 BigDecimal 结果
     */
    public static BigDecimal divideAndScale(String value1, BigDecimal value2) {
        return newBigDecimal(value1).divide(value2, 2, RoundingMode.HALF_UP);
    }


    /**
     * 计算两个BigDecimal对象
     *
     * @param calculationMethod 计算方法
     * @param newScale          小数保留位数
     * @param roundingMode      舍入方式
     * @param value1            计算值
     * @param value2            计算值2
     * @return 计算结果
     */
    public static BigDecimal calculate(CalculationMethodEnum calculationMethod, BigDecimal value1, BigDecimal value2, int newScale, RoundingMode roundingMode) {
        switch (calculationMethod) {
            case ADDITION:
                return value1.add(value2).setScale(newScale, roundingMode);
            case SUBTRACTION:
                return value1.subtract(value2).setScale(newScale, roundingMode);
            case MULTIPLICATION:
                return value1.multiply(value2).setScale(newScale, roundingMode);
            case DIVISION:
                return value1.divide(value2, newScale, roundingMode);
            default:
                return null;
        }
    }
}
