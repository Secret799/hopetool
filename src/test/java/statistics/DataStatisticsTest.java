package statistics;

import com.hopetool.core.collection.ListUtils;
import com.hopetool.core.datetime.LocalDateTimeUtils;
import com.hopetool.core.datetime.support.DateTimeUnit;
import com.hopetool.core.json.JsonUtil;
import com.hopetool.core.map.MapUtils;
import com.hopetool.core.statistics.DataCalculateUtils;
import com.hopetool.core.statistics.DataStatisticsUtils;
import com.hopetool.core.statistics.support.*;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 数据统计测试
 *
 * @author JunPzx
 * @since 2023/12/21 11:16
 */
public class DataStatisticsTest {

    private List<Student> generatorData() {
        List<Student> students = ListUtils.newArrayList();
        for (int i = 0; i < 100000; i++) {
            Student student = new Student();
            student.setAge(i);
            student.setSex(i % 2 == 0 ? "男" : "女");
            student.setName("张三" + i);
            student.setBirthday(LocalDateTimeUtils.of(LocalDate.of(2000 + i % 20, 1, 1)));
            student.setTotalScore(DataCalculateUtils.newBigDecimal(600 + new Random().nextInt(100)));
            students.add(student);
        }
        return students;
    }

    private List<Student> generatorSameNameStudentData() {
        List<Student> students = ListUtils.newArrayList();
        for (int i = 0; i < 100000; i++) {
            Student student = new Student();
            student.setAge(i);
            student.setSex(i % 2 == 0 ? "男" : "女");
            student.setName("张三");
            student.setBirthday(LocalDateTimeUtils.of(LocalDate.of(2000 + i % 20, 1, 1)));
            student.setTotalScore(DataCalculateUtils.newBigDecimal(600 + new Random().nextInt(100)));
            students.add(student);
        }
        return students;
    }

    /**
     * 单维度周期统计测试
     * <p>
     * 统计生日在2000年到2020年之间的不同年份生日所有学生的平均成绩
     */
    @Test
    public void cycleSingleDimensionalStatistics() {
        List<Student> students = generatorData();
        // 设置统计时间
        LocalDateTime beginTime = LocalDateTimeUtils.of(LocalDate.of(2000, 1, 1));
        LocalDateTime endTime = LocalDateTimeUtils.afterOffsetEndTimeByDateTimeUnit(beginTime, 20,
                DateTimeUnit.YEAR);
        // 创建统计配置
        CycleStatisticsConfig<Student, BigDecimal, ?> config = new CycleStatisticsConfig<>();
        // 设置统计周期 2000年到2020年
        config.buildStatisticsDateTime(DateTimeUnit.YEAR, beginTime, endTime);
        // 设置统计维度
        config.buildSingleDimensional("score_avg", "平均成功成绩");
        // 设置统计数据,统计方式为计算所有学生的平均成绩
        config.buildStatisticsData(students, DataValueStatisticsModeEnum.AVG, Student::getTotalScore);
        // 设置统计条件,维度统计条件为学生的生日在2000年到2020年之间
        config.buildStatisticsCondition(StatisticsArgumentMatchEnum.AND, Student::getBirthday);
        // 根据以上条件构造,统计生日在2000年到2020年之间的不同年份生日所有学生的平均成绩
        CycleDataStatisticsResult result = DataStatisticsUtils.cycleStatistics(config);
        System.out.println(JsonUtil.objectToJson(result));
    }

    /**
     * 单维度周期统计测试
     * <p>
     * 统计生日在2000年到2020年之间的不同年份生日学生数量
     */
    @Test
    public void cycleSingleDimensionalStatistics2() {
        List<Student> students = generatorData();
        // 设置统计时间
        LocalDateTime beginTime = LocalDateTimeUtils.of(LocalDate.of(2000, 1, 1));
        LocalDateTime endTime = LocalDateTimeUtils.afterOffsetEndTimeByDateTimeUnit(beginTime, 20,
                DateTimeUnit.YEAR);
        // 创建统计配置
        CycleStatisticsConfig<Student, String, ?> config = new CycleStatisticsConfig<>();
        // 设置统计周期 2000年到2020年
        config.buildStatisticsDateTime(DateTimeUnit.YEAR, beginTime, endTime);
        // 设置统计维度
        config.buildSingleDimensional("count", "本年份生日学生人数");
        // 设置统计数据,统计方式为计算所有学生数量
        config.buildStatisticsData(students, DataValueStatisticsModeEnum.COUNT, Student::getName);
        // 设置统计条件,维度统计条件为学生的生日在2000年到2020年之间
        config.buildStatisticsCondition(StatisticsArgumentMatchEnum.AND, Student::getBirthday);
        // 根据以上条件构造,统计生日在2000年到2020年之间的不同年份生日学生数量
        CycleDataStatisticsResult result = DataStatisticsUtils.cycleStatistics(config);
        System.out.println(JsonUtil.objectToJson(result));
    }

    /**
     * 多维度周期统计测试
     * <p>
     * 统计生日在2000年到2020年之间的不同年份生日男学生数量和女学生数量
     */
    @Test
    public void cycleMultidimensionalStatistics() {
        List<Student> students = generatorData();
        // 设置统计时间
        LocalDateTime beginTime = LocalDateTimeUtils.of(LocalDate.of(2000, 1, 1));
        LocalDateTime endTime = LocalDateTimeUtils.afterOffsetEndTimeByDateTimeUnit(beginTime, 20,
                DateTimeUnit.YEAR);
        // 创建统计配置
        CycleStatisticsConfig<Student, String, String> config = new CycleStatisticsConfig<>();
        // 设置统计周期 2000年到2020年
        config.buildStatisticsDateTime(DateTimeUnit.YEAR, beginTime, endTime);
        // 设置统计维度
        Map<String, String> tagMemo = MapUtils.newHashMap();
        // 设置多维度统计中,不同维度唯一标识对应的标签名
        tagMemo.put("男", "男学生数量");
        tagMemo.put("女", "女学生数量");
        config.buildMultidimensional(Student::getSex, tagMemo);
        // 设置统计数据,统计方式为计算不同性别学生数量
        config.buildStatisticsData(students, DataValueStatisticsModeEnum.COUNT, Student::getName);
        // 设置统计条件,维度统计条件为学生的生日在2000年到2020年之间
        config.buildStatisticsCondition(StatisticsArgumentMatchEnum.AND, Student::getBirthday);
        // 根据以上条件构造,统计生日在2000年到2020年之间的不同年份生日不同性别学生数量
        CycleDataStatisticsResult result = DataStatisticsUtils.cycleStatistics(config);
        System.out.println(JsonUtil.objectToJson(result));
    }

    /**
     * 单维度总计统计测试
     * <p>
     * 统计所有学生的平均成绩
     */
    @Test
    public void totalSingleDimensionalStatistics() {
        // 生成测试数据
        List<Student> students = generatorData();
        // 创建统计配置
        TotalStatisticsConfig<Student, BigDecimal, ?> config = new TotalStatisticsConfig<>();
        // 设置统计数据,统计方式为计算所有学生的平均成绩
        config.buildStatisticsData(students, DataValueStatisticsModeEnum.AVG, Student::getTotalScore);
        config.buildSingleDimensional("score_avg", "平均成绩");
        // 根据以上条件构造,统计所有学生的平均成绩
        TotalDataStatisticsResult result = DataStatisticsUtils.totalStatistics(config);
        System.out.println(JsonUtil.objectToJson(result));
    }

    /**
     * 多维度总计统计测试
     * <p>
     * 统计不同性别的学生数量
     */
    @Test
    public void totalMultidimensionalStatistics() {
        // 生成测试数据
        List<Student> students = generatorData();
        // 创建统计配置
        TotalStatisticsConfig<Student, String, String> config = new TotalStatisticsConfig<>();
        // 设置统计数据,统计方式为计算所有不同性别的学生数量
        Map<String, String> tagMemo = MapUtils.newHashMap();
        tagMemo.put("男", "男学生数量");
        tagMemo.put("女", "女学生数量");
        config.buildMultidimensional(Student::getSex, tagMemo);
        config.buildStatisticsData(students, DataValueStatisticsModeEnum.COUNT, Student::getName);
        // 根据以上条件构造,统计不同性别的学生数量
        TotalDataStatisticsResult result = DataStatisticsUtils.totalStatistics(config);
        System.out.println(JsonUtil.objectToJson(result));
    }

    /**
     * 多维度总计统计测试
     * <p>
     * 统计相同性别中不同姓名的学生数量
     */
    @Test
    public void totalMultidimensionalStatistics2() {
        // 生成测试数据
        // 不同姓名学生数据
        //        List<Student> students = generatorData();
        // 相同姓名学生数据
        List<Student> students = generatorSameNameStudentData();
        // 创建统计配置
        TotalStatisticsConfig<Student, String, String> config = new TotalStatisticsConfig<>();
        // 设置统计数据,统计方式为统计相同性别中不同姓名的学生数量
        Map<String, String> tagMemo = MapUtils.newHashMap();
        tagMemo.put("男", "男学生不同姓名数量");
        tagMemo.put("女", "女学生不同姓名数量");
        config.buildMultidimensional(Student::getSex, tagMemo);
        // 统计相同性别中不同姓名的学生数量
        config.buildStatisticsData(students, DataValueStatisticsModeEnum.DISTINCT_COUNT, Student::getName);
        // 根据以上条件构造,统计不同性别的学生数量
        TotalDataStatisticsResult result = DataStatisticsUtils.totalStatistics(config);
        System.out.println(JsonUtil.objectToJson(result));
    }

}
