package com.hopetool.core.statistics.support;

import com.hopetool.core.collection.CollectionUtils;
import com.hopetool.core.collection.ListUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 周期数据统计结果
 *
 * @author JunPzx
 * @since 2023/11/28 17:37
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class CycleDataStatisticsResult {

    private List<CycleStatisticsItemDetail> details;

    @SuppressWarnings("UnusedReturnValue")
    public CycleDataStatisticsResult addDetail(CycleStatisticsItemDetail... detail) {
        if (CollectionUtils.isEmpty(details)) {
            details = ListUtils.newArrayList();
        }
        details.addAll(Arrays.asList(detail));
        return this;
    }

    /**
     * 合并其它周期统计数据到当前周期统计数据中(合并相同时间的数据 {@link CycleStatisticsItemDetail#getDateStr()})
     *
     * @param other 要合并的周期统计数据
     * @return 合并后的周期统计数据
     */
    @SuppressWarnings("UnusedReturnValue")
    public CycleDataStatisticsResult merge(CycleDataStatisticsResult other) {
        if (ListUtils.isEmpty(other.getDetails())) {
            return this;
        }
        if (ListUtils.isEmpty(this.details)) {
            setDetails(ListUtils.newArrayList(other.getDetails()));
            return this;
        }
        ArrayList<CycleStatisticsItemDetail> finalDetails = ListUtils.newArrayList(this.details);
        // 将需要合并的周期统计数据进行处理,以合并条件-日期为索引
        Map<String, CycleStatisticsItemDetail> otherDetailsMemo = other.getDetails().stream()
                .collect(Collectors.toMap(CycleStatisticsItemDetail::getDateStr, Function.identity()));
        // 遍历当前周期统计数据,将需要合并的周期统计数据添加到当前周期统计数据中
        finalDetails.forEach(detail -> {
            // 如果其它周期统计数据中与当前数据相同日期的数据存在,则将其添加到当前日期的详细信息中
            if (otherDetailsMemo.containsKey(detail.getDateStr())) {
                detail.setDetails(ListUtils.newArrayList(detail.getDetails(), otherDetailsMemo.get(detail.getDateStr()).getDetails()));
            }
        });
        setDetails(finalDetails);
        return this;
    }
}
