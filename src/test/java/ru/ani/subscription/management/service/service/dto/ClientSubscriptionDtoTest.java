package ru.ani.subscription.management.service.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import org.junit.jupiter.api.Test;
import ru.ani.subscription.management.service.web.rest.TestUtil;

class ClientSubscriptionDtoTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ClientSubscriptionDto.class);
        ClientSubscriptionDto clientSubscriptionDto1 = new ClientSubscriptionDto();
        clientSubscriptionDto1.setId(UUID.randomUUID());
        ClientSubscriptionDto clientSubscriptionDto2 = new ClientSubscriptionDto();
        assertThat(clientSubscriptionDto1).isNotEqualTo(clientSubscriptionDto2);
        clientSubscriptionDto2.setId(clientSubscriptionDto1.getId());
        assertThat(clientSubscriptionDto1).isEqualTo(clientSubscriptionDto2);
        clientSubscriptionDto2.setId(UUID.randomUUID());
        assertThat(clientSubscriptionDto1).isNotEqualTo(clientSubscriptionDto2);
        clientSubscriptionDto1.setId(null);
        assertThat(clientSubscriptionDto1).isNotEqualTo(clientSubscriptionDto2);
    }
}
