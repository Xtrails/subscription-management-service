package ru.ani.subscription.management.service.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.ani.subscription.management.service.domain.ClientSubscriptionDaoTestSamples.*;
import static ru.ani.subscription.management.service.domain.ExternalUserDaoTestSamples.*;
import static ru.ani.subscription.management.service.domain.PaymentDaoTestSamples.*;
import static ru.ani.subscription.management.service.domain.PaymentSystemDaoTestSamples.*;

import org.junit.jupiter.api.Test;
import ru.ani.subscription.management.service.web.rest.TestUtil;

class PaymentDaoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaymentDao.class);
        PaymentDao paymentDao1 = getPaymentDaoSample1();
        PaymentDao paymentDao2 = new PaymentDao();
        assertThat(paymentDao1).isNotEqualTo(paymentDao2);

        paymentDao2.setId(paymentDao1.getId());
        assertThat(paymentDao1).isEqualTo(paymentDao2);

        paymentDao2 = getPaymentDaoSample2();
        assertThat(paymentDao1).isNotEqualTo(paymentDao2);
    }

    @Test
    void clientSubscriptionTest() {
        PaymentDao paymentDao = getPaymentDaoRandomSampleGenerator();
        ClientSubscriptionDao clientSubscriptionDaoBack = getClientSubscriptionDaoRandomSampleGenerator();

        paymentDao.setClientSubscription(clientSubscriptionDaoBack);
        assertThat(paymentDao.getClientSubscription()).isEqualTo(clientSubscriptionDaoBack);

        paymentDao.clientSubscription(null);
        assertThat(paymentDao.getClientSubscription()).isNull();
    }

    @Test
    void userTest() {
        PaymentDao paymentDao = getPaymentDaoRandomSampleGenerator();
        ExternalUserDao externalUserDaoBack = getExternalUserDaoRandomSampleGenerator();

        paymentDao.setUser(externalUserDaoBack);
        assertThat(paymentDao.getUser()).isEqualTo(externalUserDaoBack);

        paymentDao.user(null);
        assertThat(paymentDao.getUser()).isNull();
    }

    @Test
    void clietntSubscriptionTest() {
        PaymentDao paymentDao = getPaymentDaoRandomSampleGenerator();
        ClientSubscriptionDao clientSubscriptionDaoBack = getClientSubscriptionDaoRandomSampleGenerator();

        paymentDao.setClietntSubscription(clientSubscriptionDaoBack);
        assertThat(paymentDao.getClietntSubscription()).isEqualTo(clientSubscriptionDaoBack);

        paymentDao.clietntSubscription(null);
        assertThat(paymentDao.getClietntSubscription()).isNull();
    }

    @Test
    void paymentSystemTest() {
        PaymentDao paymentDao = getPaymentDaoRandomSampleGenerator();
        PaymentSystemDao paymentSystemDaoBack = getPaymentSystemDaoRandomSampleGenerator();

        paymentDao.setPaymentSystem(paymentSystemDaoBack);
        assertThat(paymentDao.getPaymentSystem()).isEqualTo(paymentSystemDaoBack);

        paymentDao.paymentSystem(null);
        assertThat(paymentDao.getPaymentSystem()).isNull();
    }
}
