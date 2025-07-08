package ru.ani.subscription.management.service.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.ani.subscription.management.service.domain.ClientSubscriptionDaoTestSamples.*;
import static ru.ani.subscription.management.service.domain.ExternalUserDaoTestSamples.*;
import static ru.ani.subscription.management.service.domain.SubscriptionDetailsDaoTestSamples.*;

import org.junit.jupiter.api.Test;
import ru.ani.subscription.management.service.web.rest.TestUtil;

class ClientSubscriptionDaoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ClientSubscriptionDao.class);
        ClientSubscriptionDao clientSubscriptionDao1 = getClientSubscriptionDaoSample1();
        ClientSubscriptionDao clientSubscriptionDao2 = new ClientSubscriptionDao();
        assertThat(clientSubscriptionDao1).isNotEqualTo(clientSubscriptionDao2);

        clientSubscriptionDao2.setId(clientSubscriptionDao1.getId());
        assertThat(clientSubscriptionDao1).isEqualTo(clientSubscriptionDao2);

        clientSubscriptionDao2 = getClientSubscriptionDaoSample2();
        assertThat(clientSubscriptionDao1).isNotEqualTo(clientSubscriptionDao2);
    }

    @Test
    void userTest() {
        ClientSubscriptionDao clientSubscriptionDao = getClientSubscriptionDaoRandomSampleGenerator();
        ExternalUserDao externalUserDaoBack = getExternalUserDaoRandomSampleGenerator();

        clientSubscriptionDao.setUser(externalUserDaoBack);
        assertThat(clientSubscriptionDao.getUser()).isEqualTo(externalUserDaoBack);

        clientSubscriptionDao.user(null);
        assertThat(clientSubscriptionDao.getUser()).isNull();
    }

    @Test
    void subscriptionDetailsTest() {
        ClientSubscriptionDao clientSubscriptionDao = getClientSubscriptionDaoRandomSampleGenerator();
        SubscriptionDetailsDao subscriptionDetailsDaoBack = getSubscriptionDetailsDaoRandomSampleGenerator();

        clientSubscriptionDao.setSubscriptionDetails(subscriptionDetailsDaoBack);
        assertThat(clientSubscriptionDao.getSubscriptionDetails()).isEqualTo(subscriptionDetailsDaoBack);

        clientSubscriptionDao.subscriptionDetails(null);
        assertThat(clientSubscriptionDao.getSubscriptionDetails()).isNull();
    }
}
