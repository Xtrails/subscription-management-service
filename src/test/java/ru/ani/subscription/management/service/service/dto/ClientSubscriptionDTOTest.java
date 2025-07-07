package ru.ani.subscription.management.service.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import ru.ani.subscription.management.service.web.rest.TestUtil;

class ClientSubscriptionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ClientSubscriptionDTO.class);
        ClientSubscriptionDTO clientSubscriptionDTO1 = new ClientSubscriptionDTO();
        clientSubscriptionDTO1.setId(1L);
        ClientSubscriptionDTO clientSubscriptionDTO2 = new ClientSubscriptionDTO();
        assertThat(clientSubscriptionDTO1).isNotEqualTo(clientSubscriptionDTO2);
        clientSubscriptionDTO2.setId(clientSubscriptionDTO1.getId());
        assertThat(clientSubscriptionDTO1).isEqualTo(clientSubscriptionDTO2);
        clientSubscriptionDTO2.setId(2L);
        assertThat(clientSubscriptionDTO1).isNotEqualTo(clientSubscriptionDTO2);
        clientSubscriptionDTO1.setId(null);
        assertThat(clientSubscriptionDTO1).isNotEqualTo(clientSubscriptionDTO2);
    }
}
