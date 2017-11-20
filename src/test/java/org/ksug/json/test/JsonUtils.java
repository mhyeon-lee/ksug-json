package org.ksug.json.test;

import java.util.Map;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.bind.Jsonb;

/**
 * Created by mhyeon.lee on 2017. 11. 20..
 */
public class JsonUtils {
	public static JsonObject objectToJsonObject(Jsonb jsonb, Object object) {
		String json = jsonb.toJson(object);
		return jsonToJsonObject(jsonb, json);
	}

	@SuppressWarnings("unchecked")
	public static JsonObject jsonToJsonObject(Jsonb jsonb, String json) {
		Map<String, Object> map = jsonb.fromJson(json, Map.class);
		return Json.createObjectBuilder(map).build();
	}
}
