package com.hopetool.core.statistics.support;

import com.hopetool.core.column.support.SFunction;
import com.hopetool.core.lang.AssertUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * 总数统计配置
 *
 * @author JunPzx
 * @since 2023/11/28 18:35
 */
@Getter
@NoArgsConstructor
public class TotalStatisticsConfig<T, V, M> extends AbstractStatisticsConfig<T> {

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
     * 根据指定数据构建周期统计配置
     *
     * @param data               周期数据列表
     * @param statisticsModeEnum 统计模式
     * @param getValueFunction   获取值的函数
     * @return 周期统计配置对象
     */
    public TotalStatisticsConfig<T, V, M> buildStatisticsData(List<T> data, DataValueStatisticsModeEnum statisticsModeEnum, SFunction<T, V> getValueFunction) {
        super.setData(data);
        super.setStatisticsMode(statisticsModeEnum);
        this.getValueFunction = getValueFunction;
        return this;
    }

    /**
     * 构建单维的统计配置
     *
     * @param tagName        标签名
     * @param tagChineseName 标签中文名
     * @return 自定义的CycleStatisticsConfig对象
     */
    public TotalStatisticsConfig<T, V, M> buildSingleDimensional(String tagName, String tagChineseName) {
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
    public TotalStatisticsConfig<T, V, M> buildMultidimensional(SFunction<T, M> multidimensionalFunction, Map<M, String> tagMemo) {
        this.isMultidimensional = true;
        this.singleDimensional = null;
        this.multidimensional = new Multidimensional<>(multidimensionalFunction, tagMemo);
        return this;
    }


    public void checkSelf() {
        super.checkSelf();
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
