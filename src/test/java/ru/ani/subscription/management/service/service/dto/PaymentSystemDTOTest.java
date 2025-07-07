package ru.ani.subscription.management.service.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import ru.ani.subscription.management.service.web.rest.TestUtil;

class PaymentSystemDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaymentSystemDTO.class);
        PaymentSystemDTO paymentSystemDTO1 = new PaymentSystemDTO();
        paymentSystemDTO1.setId(1L);
        PaymentSystemDTO paymentSystemDTO2 = new PaymentSystemDTO();
        assertThat(paymentSystemDTO1).isNotEqualTo(paymentSystemDTO2);
        paymentSystemDTO2.setId(paymentSystemDTO1.getId());
        assertThat(paymentSystemDTO1).isEqualTo(paymentSystemDTO2);
        paymentSystemDTO2.setId(2L);
        assertThat(paymentSystemDTO1).isNotEqualTo(paymentSystemDTO2);
        paymentSystemDTO1.setId(null);
        assertThat(paymentSystemDTO1).isNotEqualTo(paymentSystemDTO2);
    }
}
