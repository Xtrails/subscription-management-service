package ru.aniscan.subscription.management.service.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.aniscan.subscription.management.service.domain.ExternalUserTestSamples.*;
import static ru.aniscan.subscription.management.service.domain.SourceApplicationTestSamples.*;

import org.junit.jupiter.api.Test;
import ru.aniscan.subscription.management.service.web.rest.TestUtil;

class ExternalUserTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExternalUser.class);
        ExternalUser externalUser1 = getExternalUserSample1();
        ExternalUser externalUser2 = new ExternalUser();
        assertThat(externalUser1).isNotEqualTo(externalUser2);

        externalUser2.setId(externalUser1.getId());
        assertThat(externalUser1).isEqualTo(externalUser2);

        externalUser2 = getExternalUserSample2();
        assertThat(externalUser1).isNotEqualTo(externalUser2);
    }

    @Test
    void sourceApplicationTest() {
        ExternalUser externalUser = getExternalUserRandomSampleGenerator();
        SourceApplication sourceApplicationBack = getSourceApplicationRandomSampleGenerator();

        externalUser.setSourceApplication(sourceApplicationBack);
        assertThat(externalUser.getSourceApplication()).isEqualTo(sourceApplicationBack);

        externalUser.sourceApplication(null);
        assertThat(externalUser.getSourceApplication()).isNull();
    }
}
