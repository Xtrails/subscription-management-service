package ru.ani.subscription.management.service.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import org.junit.jupiter.api.Test;
import ru.ani.subscription.management.service.web.rest.TestUtil;

class PaymentSystemDtoTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaymentSystemDto.class);
        PaymentSystemDto paymentSystemDto1 = new PaymentSystemDto();
        paymentSystemDto1.setId(UUID.randomUUID());
        PaymentSystemDto paymentSystemDto2 = new PaymentSystemDto();
        assertThat(paymentSystemDto1).isNotEqualTo(paymentSystemDto2);
        paymentSystemDto2.setId(paymentSystemDto1.getId());
        assertThat(paymentSystemDto1).isEqualTo(paymentSystemDto2);
        paymentSystemDto2.setId(UUID.randomUUID());
        assertThat(paymentSystemDto1).isNotEqualTo(paymentSystemDto2);
        paymentSystemDto1.setId(null);
        assertThat(paymentSystemDto1).isNotEqualTo(paymentSystemDto2);
    }
}
