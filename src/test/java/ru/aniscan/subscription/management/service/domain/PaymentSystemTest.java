package ru.aniscan.subscription.management.service.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.aniscan.subscription.management.service.domain.PaymentSystemTestSamples.*;
import static ru.aniscan.subscription.management.service.domain.SourceApplicationTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import ru.aniscan.subscription.management.service.web.rest.TestUtil;

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

    @Test
    void sourceApplicationsTest() {
        PaymentSystem paymentSystem = getPaymentSystemRandomSampleGenerator();
        SourceApplication sourceApplicationBack = getSourceApplicationRandomSampleGenerator();

        paymentSystem.addSourceApplications(sourceApplicationBack);
        assertThat(paymentSystem.getSourceApplications()).containsOnly(sourceApplicationBack);

        paymentSystem.removeSourceApplications(sourceApplicationBack);
        assertThat(paymentSystem.getSourceApplications()).doesNotContain(sourceApplicationBack);

        paymentSystem.sourceApplications(new HashSet<>(Set.of(sourceApplicationBack)));
        assertThat(paymentSystem.getSourceApplications()).containsOnly(sourceApplicationBack);

        paymentSystem.setSourceApplications(new HashSet<>());
        assertThat(paymentSystem.getSourceApplications()).doesNotContain(sourceApplicationBack);
    }
}
