package ru.ani.subscription.management.service.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import org.junit.jupiter.api.Test;
import ru.ani.subscription.management.service.web.rest.TestUtil;

class PaymentDtoTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaymentDto.class);
        PaymentDto paymentDto1 = new PaymentDto();
        paymentDto1.setId(UUID.randomUUID());
        PaymentDto paymentDto2 = new PaymentDto();
        assertThat(paymentDto1).isNotEqualTo(paymentDto2);
        paymentDto2.setId(paymentDto1.getId());
        assertThat(paymentDto1).isEqualTo(paymentDto2);
        paymentDto2.setId(UUID.randomUUID());
        assertThat(paymentDto1).isNotEqualTo(paymentDto2);
        paymentDto1.setId(null);
        assertThat(paymentDto1).isNotEqualTo(paymentDto2);
    }
}
