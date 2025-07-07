package ru.ani.subscription.management.service.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.ani.subscription.management.service.domain.PaymentSystemTestSamples.*;

import org.junit.jupiter.api.Test;
import ru.ani.subscription.management.service.web.rest.TestUtil;

class PaymentSystemTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaymentSystem.class);
        PaymentSystem paymentSystem1 = getPaymentSystemSample1();
        PaymentSystem paymentSystem2 = new PaymentSystem();
        assertThat(paymentSystem1).isNotEqualTo(paymentSystem2);

        paymentSystem2.setId(paymentSystem1.getId());
        assertThat(paymentSystem1).isEqualTo(paymentSystem2);

        paymentSystem2 = getPaymentSystemSample2();
        assertThat(paymentSystem1).isNotEqualTo(paymentSystem2);
    }
}
