package org.ksug.json.test;

import static org.junit.jupiter.api.Assertions.*;

import javax.json.bind.spi.JsonbProvider;
import javax.json.spi.JsonProvider;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Created by mhyeon.lee on 2017. 11. 19..
 */
class JsonProviderTest {
	@Test
	@DisplayName("The object declared in META-INF is chosen as the implementation of JsonbProvider.")
	void jsonbProviderTest() {
		JsonbProvider jsonbProvider = JsonbProvider.provider();
		assertSame(org.apache.johnzon.jsonb.JohnzonProvider.class, jsonbProvider.getClass());
	}

	@Test
	@DisplayName("The object declared in META-INF is chosen as the implementation of JsonProvider.")
	void jsonProviderTest() {
		JsonProvider jsonProvider = JsonProvider.provider();
		assertSame(org.glassfish.json.JsonProviderImpl.class, jsonProvider.getClass());
	}
}
