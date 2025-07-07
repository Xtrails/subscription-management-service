package ru.ani.subscription.management.service.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import ru.ani.subscription.management.service.web.rest.TestUtil;

class SubscriptionDetailsDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SubscriptionDetailsDTO.class);
        SubscriptionDetailsDTO subscriptionDetailsDTO1 = new SubscriptionDetailsDTO();
        subscriptionDetailsDTO1.setId(1L);
        SubscriptionDetailsDTO subscriptionDetailsDTO2 = new SubscriptionDetailsDTO();
        assertThat(subscriptionDetailsDTO1).isNotEqualTo(subscriptionDetailsDTO2);
        subscriptionDetailsDTO2.setId(subscriptionDetailsDTO1.getId());
        assertThat(subscriptionDetailsDTO1).isEqualTo(subscriptionDetailsDTO2);
        subscriptionDetailsDTO2.setId(2L);
        assertThat(subscriptionDetailsDTO1).isNotEqualTo(subscriptionDetailsDTO2);
        subscriptionDetailsDTO1.setId(null);
        assertThat(subscriptionDetailsDTO1).isNotEqualTo(subscriptionDetailsDTO2);
    }
}
