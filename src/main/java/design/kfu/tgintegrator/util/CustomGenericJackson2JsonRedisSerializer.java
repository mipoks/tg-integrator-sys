package design.kfu.tgintegrator.util;



import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.cache.support.NullValue;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.io.IOException;

/**
 * Собственная реализация GenericJackson2JsonRedisSerializer тк стандартный
 * GenericJackson2JsonRedisSerializer не может сериализовать Instant. В случае проблем
 * расскоменьтить 39-ую строку и заккоментить следующую после неё
 */
public class CustomGenericJackson2JsonRedisSerializer implements RedisSerializer<Object> {


    protected GenericJackson2JsonRedisSerializer serializer = null;

    public CustomGenericJackson2JsonRedisSerializer() {
        PolymorphicTypeValidator validator =BasicPolymorphicTypeValidator.builder().build();
        ObjectMapper om = new ObjectMapper();
        om.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        om.registerModule(new JavaTimeModule());
        om.registerModule((new SimpleModule())
                .addSerializer(new NullValueSerializer()));
        /*om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);*/
        om.activateDefaultTyping(validator,ObjectMapper.DefaultTyping.NON_FINAL,JsonTypeInfo.As.PROPERTY);
        this.serializer = new GenericJackson2JsonRedisSerializer(om);
    }

    public CustomGenericJackson2JsonRedisSerializer(ObjectMapper om) {
        this.serializer = new GenericJackson2JsonRedisSerializer(om);
    }

    @Override
    public byte[] serialize(Object o) throws SerializationException {
        return serializer.serialize(o);
    }

    @Override
    public Object deserialize(byte[] bytes) throws SerializationException {
        return serializer.deserialize(bytes);
    }


    protected class NullValueSerializer extends StdSerializer<NullValue> {
        private static final long serialVersionUID = 1999052150548658807L;
        private final String classIdentifier="@class";

        NullValueSerializer() {
            super(NullValue.class);
        }

        public void serialize(NullValue value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
            jgen.writeStartObject();
            jgen.writeStringField(this.classIdentifier, NullValue.class.getName());
            jgen.writeEndObject();
        }
    }

}
