package ru.ani.subscription.management.service.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import org.junit.jupiter.api.Test;
import ru.ani.subscription.management.service.web.rest.TestUtil;

class SubscriptionDetailsDtoTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SubscriptionDetailsDto.class);
        SubscriptionDetailsDto subscriptionDetailsDto1 = new SubscriptionDetailsDto();
        subscriptionDetailsDto1.setId(UUID.randomUUID());
        SubscriptionDetailsDto subscriptionDetailsDto2 = new SubscriptionDetailsDto();
        assertThat(subscriptionDetailsDto1).isNotEqualTo(subscriptionDetailsDto2);
        subscriptionDetailsDto2.setId(subscriptionDetailsDto1.getId());
        assertThat(subscriptionDetailsDto1).isEqualTo(subscriptionDetailsDto2);
        subscriptionDetailsDto2.setId(UUID.randomUUID());
        assertThat(subscriptionDetailsDto1).isNotEqualTo(subscriptionDetailsDto2);
        subscriptionDetailsDto1.setId(null);
        assertThat(subscriptionDetailsDto1).isNotEqualTo(subscriptionDetailsDto2);
    }
}
