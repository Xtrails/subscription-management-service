package ru.aniscan.subscription.management.service.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class ReferralAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertReferralAllPropertiesEquals(Referral expected, Referral actual) {
        assertReferralAutoGeneratedPropertiesEquals(expected, actual);
        assertReferralAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertReferralAllUpdatablePropertiesEquals(Referral expected, Referral actual) {
        assertReferralUpdatableFieldsEquals(expected, actual);
        assertReferralUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertReferralAutoGeneratedPropertiesEquals(Referral expected, Referral actual) {
        assertThat(expected)
            .as("Verify Referral auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertReferralUpdatableFieldsEquals(Referral expected, Referral actual) {
        assertThat(expected)
            .as("Verify Referral relevant properties")
            .satisfies(e -> assertThat(e.getReferralCode()).as("check referralCode").isEqualTo(actual.getReferralCode()))
            .satisfies(e -> assertThat(e.getStatus()).as("check status").isEqualTo(actual.getStatus()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertReferralUpdatableRelationshipsEquals(Referral expected, Referral actual) {
        assertThat(expected)
            .as("Verify Referral relationships")
            .satisfies(e -> assertThat(e.getReferrer()).as("check referrer").isEqualTo(actual.getReferrer()))
            .satisfies(e -> assertThat(e.getReferralProgram()).as("check referralProgram").isEqualTo(actual.getReferralProgram()));
    }
}
