package com.hopetool.core.json.support;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * 长字符串序列化器(防止Long类型的ID传到前端时精度丢失)
 *
 * @author JunPZX
 * @version 1.0.0
 * @date 2022-07-01 14:30
 */
public class LongToStringSerializer extends JsonSerializer<Long> {

    public final static LongToStringSerializer instance = new LongToStringSerializer();

    @Override
    public void serialize(Long value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value != null) {
            if (String.valueOf(value).length() > 10) {
                gen.writeString(String.valueOf(value));
            } else {
                gen.writeNumber(value);
            }
        } else {
            gen.writeNull();
        }
    }
}
