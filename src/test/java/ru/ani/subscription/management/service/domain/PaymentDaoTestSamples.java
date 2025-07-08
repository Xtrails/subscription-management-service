package ru.ani.subscription.management.service.domain;

import java.util.UUID;

public class PaymentDaoTestSamples {

    public static PaymentDao getPaymentDaoSample1() {
        return new PaymentDao().id(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa")).hashSum("hashSum1");
    }

    public static PaymentDao getPaymentDaoSample2() {
        return new PaymentDao().id(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367")).hashSum("hashSum2");
    }

    public static PaymentDao getPaymentDaoRandomSampleGenerator() {
        return new PaymentDao().id(UUID.randomUUID()).hashSum(UUID.randomUUID().toString());
    }
}
