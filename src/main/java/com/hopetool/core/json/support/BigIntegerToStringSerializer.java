package com.hopetool.core.json.support;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.math.BigInteger;

/**
 * 长字符串序列化器(防止Integer类型的字段传到前端时精度丢失)
 *
 * @author JunPZX
 * @version 1.0.0
 * @date 2022-07-01 14:30
 */
public class BigIntegerToStringSerializer extends JsonSerializer<BigInteger> {

    public final static BigIntegerToStringSerializer instance = new BigIntegerToStringSerializer();

    @Override
    public void serialize(BigInteger value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
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
