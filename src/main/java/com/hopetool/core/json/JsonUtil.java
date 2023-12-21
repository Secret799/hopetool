package com.hopetool.core.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.hopetool.core.json.support.BigIntegerToStringSerializer;
import com.hopetool.core.json.support.LongToStringSerializer;
import lombok.SneakyThrows;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

/**
 * Json序列化反序列化工具
 *
 * @author JunPzx
 */
public class JsonUtil {
    /**
     * 定义jackson对象
     */
    private static final ObjectMapper OBJECT_MAPPER = instance();

    /**
     * 将对象转换成json字符串。
     * <p>Title: pojoToJson</p>
     * <p>Description: </p>
     *
     * @param data 需要转换的对象
     * @return 转换后的json串
     */
    @SneakyThrows
    public static String objectToJson(Object data) {
        return OBJECT_MAPPER.writeValueAsString(data);
    }

    /**
     * 将json结果集转化为对象
     *
     * @param jsonData json数据
     * @param beanType 对象中的object类型
     * @return T类型的对象
     */
    @SneakyThrows
    public static <T> T jsonToPojo(String jsonData, Class<T> beanType) {
        //将string转arraylist
        return OBJECT_MAPPER.readValue(jsonData, beanType);
    }

    /**
     * 将json数据转换成pojo对象list
     * <p>Title: jsonToList</p>
     * <p>Description: </p>
     *
     * @param jsonData json数据
     * @param beanType 对象类型
     * @return List集合数据
     */
    @SneakyThrows
    public static <T> List<T> jsonToList(String jsonData, Class<T> beanType) {
        JavaType javaType = OBJECT_MAPPER.getTypeFactory().constructParametricType(List.class, beanType);
        //将string转arraylist
        return OBJECT_MAPPER.readValue(jsonData, javaType);
    }

    @SneakyThrows
    public static <T> T objectToPojo(Object obj, Class<T> beanType) {
        String jsonStr = OBJECT_MAPPER.writeValueAsString(obj);
        return OBJECT_MAPPER.readValue(jsonStr, beanType);
    }

    @SneakyThrows
    public static <T> List<T> objectToList(Object obj, Class<T> beanType) {
        JavaType javaType = OBJECT_MAPPER.getTypeFactory().constructParametricType(List.class, beanType);
        //将string转arraylist
        String jsonStr = OBJECT_MAPPER.writeValueAsString(obj);
        return OBJECT_MAPPER.readValue(jsonStr, javaType);
    }


    public static ObjectMapper instance() {
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule simpleModule = new SimpleModule();
        // 序列换成json时,将所有的long变成string 因为js中得数字类型不能包含所有的java long值
        simpleModule.addSerializer(BigInteger.class, BigIntegerToStringSerializer.instance);
        simpleModule.addSerializer(Long.class, LongToStringSerializer.instance);
        simpleModule.addSerializer(Long.TYPE, LongToStringSerializer.instance);
        objectMapper.registerModule(simpleModule);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        DeserializationConfig dc = objectMapper.getDeserializationConfig();
        objectMapper
                .setConfig(dc.with(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"))
                        .without(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES));
        //反序列时, 字段的命名规则
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);

        // 日期格式
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

        //GMT+8
        objectMapper.setTimeZone(TimeZone.getTimeZone("GMT+8"));

        // Include.NON_NULL 属性为NULL 不序列化
        //ALWAYS // 默认策略，任何情况都执行序列化
        //NON_EMPTY // null、集合数组等没有内容、空字符串等，都不会被序列化
        //NON_DEFAULT // 如果字段是默认值，就不会被序列化
        //NON_ABSENT // null的不会序列化，但如果类型是AtomicReference，依然会被序列化
        objectMapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);

        //允许字段名没有引号（可以进一步减小json体积）：
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);

        //允许单引号：
        objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);

        // 允许出现特殊字符和转义符
        //objectMapper.configure(Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);这个已经过时。
        objectMapper.configure(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature(), true);

        //允许C和C++样式注释：
        objectMapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);

        //序列化结果格式化，美化输出
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        //枚举输出成字符串
        //WRITE_ENUMS_USING_INDEX：输出索引
        objectMapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);

        //空对象不要抛出异常：
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

        //Date、Calendar等序列化为时间格式的字符串(如果不执行以下设置，就会序列化成时间戳格式)：
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        //反序列化时，遇到未知属性不要抛出异常：
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        //反序列化时，遇到忽略属性不要抛出异常：
        objectMapper.disable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES);

        //反序列化时，空字符串对于的实例属性为null：
        objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        return objectMapper;
    }
}
