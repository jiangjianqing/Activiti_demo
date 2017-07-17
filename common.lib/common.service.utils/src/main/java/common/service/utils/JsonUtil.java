package common.service.utils;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Collection;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

public class JsonUtil {
	
	protected final static Logger logger = LoggerFactory
			.getLogger(JsonUtil.class);

	public static String toJson(Collection<Object> collection) {

		String json = "[]";
		ObjectMapper mapper = new ObjectMapper();
		StringWriter sw = new StringWriter();
		try {
			JsonGenerator gen = new JsonFactory().createGenerator(sw);
			mapper.writeValue(gen, collection);
			gen.close();
			json = sw.toString();
		} catch (JsonGenerationException e) {
			json = "[]";
			logger.error("------1------ toJson JsonGenerationException error:\n", e);

		} catch (JsonMappingException e) {
			json = "[]";
			logger.error("------2------ toJson JsonMappingException error:\n", e);

		} catch (IOException e) {
			json = "[]";
			logger.error("------3------ toJson IOException error:\n", e);
		}

		return json;
	}

	public static String toJson(Object obj) {
		String json = "[]";
		ObjectMapper mapper = new ObjectMapper();
		StringWriter sw = new StringWriter();
		try {
			JsonGenerator gen = new JsonFactory().createGenerator(sw);
			mapper.writeValue(gen, obj);
			gen.close();

			json = sw.toString();
		} catch (JsonGenerationException e) {
			json = "[]";
			logger.error("------1------ toJson IOException error:\n", e);

		} catch (JsonMappingException e) {
			json = "[]";
			logger.error("------2------ toJson IOException error:\n", e);

		} catch (IOException e) {
			json = "[]";
			logger.error("------3------ toJson IOException error:\n", e);
		}

		return json;
	}

	public static String toJson(Map<String, Object> map) {
		String json = "[]";
		ObjectMapper mapper = new ObjectMapper();
		StringWriter sw = new StringWriter();
		try {
			JsonGenerator gen = new JsonFactory().createGenerator(sw);
			mapper.writeValue(gen, map);
			gen.close();

			json = sw.toString();
		} catch (JsonGenerationException e) {
			json = "[]";
			logger.error("----1------toJson JsonGenerationException:\n" + e);
		} catch (JsonMappingException e) {
			json = "[]";
			logger.error("----2------toJson JsonMappingException:\n" + e);
		} catch (IOException e) {
			json = "[]";
			logger.error("----3------toJson IOException:\n" + e);
		}

		return json;
	}

	public static Object jsonToObject(String jsonStr, Class<?> beanClass)
			throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.readValue(jsonStr.getBytes(), beanClass);
	}

	public static Object jsonToObject(String jsonStr, String encoding, Class<?> beanClass)
			throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.readValue(jsonStr.getBytes(encoding), beanClass);
	}

	public static Map<String, Object> jsonToMap(String jsonStr)
			throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.readValue(jsonStr, Map.class);
	}

	public static String toJson(Object object, String filterName) {
		ObjectMapper mapper = new ObjectMapper();
		String json = "[]";
		try {
			FilterProvider filterProvider = new SimpleFilterProvider().addFilter(filterName,
					SimpleBeanPropertyFilter.serializeAllExcept());
			json = mapper.writer(filterProvider).writeValueAsString(object);
		} catch (Exception e) {
			json = "[]";
			logger.error("----1------toJson Exception:\n" + e);
		}

		return json;
	}

	public static String toJson(Object object, String[] args, String filterName) {
		String json = "[]";
		ObjectMapper mapper = new ObjectMapper();
		try {
			FilterProvider filterProvider = new SimpleFilterProvider().addFilter(filterName,
					SimpleBeanPropertyFilter.serializeAllExcept(args));
			json = mapper.writer(filterProvider).writeValueAsString(object);
		} catch (Exception e) {
			json = "[]";
			logger.error("----1------toJson Exception:\n" + e);
		}

		return json;
	}

}

