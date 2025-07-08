package ru.ani.subscription.management.service.service.mapper;

import static ru.ani.subscription.management.service.domain.PaymentDaoAsserts.*;
import static ru.ani.subscription.management.service.domain.PaymentDaoTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PaymentMapperTest {

    private PaymentMapper paymentMapper;

    @BeforeEach
    void setUp() {
        paymentMapper = new PaymentMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPaymentDaoSample1();
        var actual = paymentMapper.toEntity(paymentMapper.toDto(expected));
        assertPaymentDaoAllPropertiesEquals(expected, actual);
    }
}
