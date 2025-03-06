package com.ar.edu.um.taccetta.cars;

import com.ar.edu.um.taccetta.cars.config.AsyncSyncConfiguration;
import com.ar.edu.um.taccetta.cars.config.EmbeddedSQL;
import com.ar.edu.um.taccetta.cars.config.JacksonConfiguration;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Base composite annotation for integration tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(classes = { CarDealershipApp.class, JacksonConfiguration.class, AsyncSyncConfiguration.class })
@EmbeddedSQL
public @interface IntegrationTest {
}
