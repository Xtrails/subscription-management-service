package ru.ani.subscription.management.service.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.ani.subscription.management.service.domain.ExternalUserDaoTestSamples.*;
import static ru.ani.subscription.management.service.domain.ReferralProgramDaoTestSamples.*;
import static ru.ani.subscription.management.service.domain.SourceApplicationDaoTestSamples.*;

import org.junit.jupiter.api.Test;
import ru.ani.subscription.management.service.web.rest.TestUtil;

class ReferralProgramDaoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReferralProgramDao.class);
        ReferralProgramDao referralProgramDao1 = getReferralProgramDaoSample1();
        ReferralProgramDao referralProgramDao2 = new ReferralProgramDao();
        assertThat(referralProgramDao1).isNotEqualTo(referralProgramDao2);

        referralProgramDao2.setId(referralProgramDao1.getId());
        assertThat(referralProgramDao1).isEqualTo(referralProgramDao2);

        referralProgramDao2 = getReferralProgramDaoSample2();
        assertThat(referralProgramDao1).isNotEqualTo(referralProgramDao2);
    }

    @Test
    void externalUserTest() {
        ReferralProgramDao referralProgramDao = getReferralProgramDaoRandomSampleGenerator();
        ExternalUserDao externalUserDaoBack = getExternalUserDaoRandomSampleGenerator();

        referralProgramDao.setExternalUser(externalUserDaoBack);
        assertThat(referralProgramDao.getExternalUser()).isEqualTo(externalUserDaoBack);
        assertThat(externalUserDaoBack.getReferralCreator()).isEqualTo(referralProgramDao);

        referralProgramDao.externalUser(null);
        assertThat(referralProgramDao.getExternalUser()).isNull();
        assertThat(externalUserDaoBack.getReferralCreator()).isNull();
    }

    @Test
    void sourceApplicationTest() {
        ReferralProgramDao referralProgramDao = getReferralProgramDaoRandomSampleGenerator();
        SourceApplicationDao sourceApplicationDaoBack = getSourceApplicationDaoRandomSampleGenerator();

        referralProgramDao.setSourceApplication(sourceApplicationDaoBack);
        assertThat(referralProgramDao.getSourceApplication()).isEqualTo(sourceApplicationDaoBack);

        referralProgramDao.sourceApplication(null);
        assertThat(referralProgramDao.getSourceApplication()).isNull();
    }
}
