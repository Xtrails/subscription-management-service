package ru.ani.subscription.management.service;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.context.SpringBootTest;
import ru.ani.subscription.management.service.config.AsyncSyncConfiguration;
import ru.ani.subscription.management.service.config.EmbeddedSQL;
import ru.ani.subscription.management.service.config.JacksonConfiguration;
import ru.ani.subscription.management.service.config.TestSecurityConfiguration;

/**
 * Base composite annotation for integration tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(
    classes = {
        SubscriptionManagementServiceApp.class, JacksonConfiguration.class, AsyncSyncConfiguration.class, TestSecurityConfiguration.class,
    }
)
@EmbeddedSQL
public @interface IntegrationTest {
}
