package com.hopetool.core.statistics.support;

import com.hopetool.core.collection.ListUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Arrays;
import java.util.List;

/**
 * 总数数据统计结果
 *
 * @author JunPzx
 * @since 2023/11/28 17:42
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class TotalDataStatisticsResult {

    /**
     * 数据项详情
     */
    private List<DataStatisticsItemDetail> details;


    public TotalDataStatisticsResult addDetail(DataStatisticsItemDetail... detail) {
        return addDetail(Arrays.asList(detail));
    }

    public TotalDataStatisticsResult addDetail(List<DataStatisticsItemDetail> detail) {
        if (ListUtils.isEmpty(details)) {
            details = ListUtils.newArrayList();
        }
        details.addAll(detail);
        return this;
    }

    @SuppressWarnings("UnusedReturnValue")
    public TotalDataStatisticsResult merge(TotalDataStatisticsResult other) {
        if (ListUtils.isEmpty(other.getDetails())) {
            return this;
        }
        if (ListUtils.isEmpty(details)) {
            setDetails(ListUtils.newArrayList(other.getDetails()));
            return this;
        }
        setDetails(ListUtils.newArrayList(getDetails(), other.getDetails()));
        return this;
    }

}
