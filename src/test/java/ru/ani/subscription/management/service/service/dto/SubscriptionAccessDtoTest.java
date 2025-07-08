package ru.ani.subscription.management.service.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import org.junit.jupiter.api.Test;
import ru.ani.subscription.management.service.web.rest.TestUtil;

class SubscriptionAccessDtoTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SubscriptionAccessDto.class);
        SubscriptionAccessDto subscriptionAccessDto1 = new SubscriptionAccessDto();
        subscriptionAccessDto1.setId(UUID.randomUUID());
        SubscriptionAccessDto subscriptionAccessDto2 = new SubscriptionAccessDto();
        assertThat(subscriptionAccessDto1).isNotEqualTo(subscriptionAccessDto2);
        subscriptionAccessDto2.setId(subscriptionAccessDto1.getId());
        assertThat(subscriptionAccessDto1).isEqualTo(subscriptionAccessDto2);
        subscriptionAccessDto2.setId(UUID.randomUUID());
        assertThat(subscriptionAccessDto1).isNotEqualTo(subscriptionAccessDto2);
        subscriptionAccessDto1.setId(null);
        assertThat(subscriptionAccessDto1).isNotEqualTo(subscriptionAccessDto2);
    }
}
