package com.codetosalvation.mongo.services.util;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.ser.CustomSerializerFactory;
import org.codehaus.jackson.type.JavaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JSONUtil {

	// Apparently this is thread safe...
	private static ObjectMapper objectMapper = JSONUtil.initMapper();

	private final static Logger logger = LoggerFactory.getLogger(JSONUtil.class);

	public static String serialize(Object bean) {
		final Writer writer = new StringWriter();
		try {
			JSONUtil.objectMapper.writeValue(writer, bean);
		} catch (final IOException e) {
			JSONUtil.logger.error("Could not serialize " + bean.toString(), e);
			return null;
		}

		return writer.toString();
	}

	public static <T> T parse(String json, Class<T> clazz) {
		try {
			return JSONUtil.objectMapper.readValue(json, clazz);
		} catch (final IOException e) {
			JSONUtil.logger.error("Could not parse " + json + " to " + clazz, e);
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T parse(String json, JavaType javaType) {
		try {
			return (T) JSONUtil.objectMapper.readValue(json, javaType);
		} catch (final IOException e) {
			JSONUtil.logger.error("Could not parse " + json + " to " + javaType, e);
			return null;
		}
	}

	@SuppressWarnings("deprecation")
	public static ObjectMapper initMapper() {
		final ObjectMapper m = new ObjectMapper();
		m.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS, false);
		m.configure(SerializationConfig.Feature.WRITE_NULL_PROPERTIES, false);
		m.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
		m.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		// m.configure(SerializationConfig.Feature.WRITE_NULL_MAP_VALUES,
		// false);
		final CustomSerializerFactory sf = new CustomSerializerFactory();

		m.setSerializerFactory(sf);

		return m;
	}

}
