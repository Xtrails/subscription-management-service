package ru.ani.subscription.management.service.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.ani.subscription.management.service.domain.PaymentSystemDaoTestSamples.*;

import org.junit.jupiter.api.Test;
import ru.ani.subscription.management.service.web.rest.TestUtil;

class PaymentSystemDaoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaymentSystemDao.class);
        PaymentSystemDao paymentSystemDao1 = getPaymentSystemDaoSample1();
        PaymentSystemDao paymentSystemDao2 = new PaymentSystemDao();
        assertThat(paymentSystemDao1).isNotEqualTo(paymentSystemDao2);

        paymentSystemDao2.setId(paymentSystemDao1.getId());
        assertThat(paymentSystemDao1).isEqualTo(paymentSystemDao2);

        paymentSystemDao2 = getPaymentSystemDaoSample2();
        assertThat(paymentSystemDao1).isNotEqualTo(paymentSystemDao2);
    }
}
