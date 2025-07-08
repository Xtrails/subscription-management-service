package ru.ani.subscription.management.service.service.mapper;

import static ru.ani.subscription.management.service.domain.PaymentSystemDaoAsserts.*;
import static ru.ani.subscription.management.service.domain.PaymentSystemDaoTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PaymentSystemMapperTest {

    private PaymentSystemMapper paymentSystemMapper;

    @BeforeEach
    void setUp() {
        paymentSystemMapper = new PaymentSystemMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPaymentSystemDaoSample1();
        var actual = paymentSystemMapper.toEntity(paymentSystemMapper.toDto(expected));
        assertPaymentSystemDaoAllPropertiesEquals(expected, actual);
    }
}
