package com.hopetool.core.statistics.support;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Arrays;
import java.util.List;

/**
 * 周期统计项详情
 *
 * @author JunPzx
 * @since 2023/11/28 19:18
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class CycleStatisticsItemDetail {

    /**
     * 日期标签
     */
    private String dateStr;

    /**
     * 日期中文标签
     */
    private String chineseDateStr;

    /**
     * 数据项详情
     */
    private List<DataStatisticsItemDetail> details;


    /**
     * 获取CycleStatisticsItemDetail实例
     *
     * @param dateStr        日期字符串
     * @param chineseDateStr 汉字日期字符串
     * @param detail         DataStatisticsItemDetail对象数组
     * @return CycleStatisticsItemDetail实例对象
     */
    public static CycleStatisticsItemDetail instance(String dateStr, String chineseDateStr, DataStatisticsItemDetail... detail) {
        return new CycleStatisticsItemDetail().setDetails(Arrays.asList(detail)).setDateStr(dateStr).setChineseDateStr(chineseDateStr);
    }


}
