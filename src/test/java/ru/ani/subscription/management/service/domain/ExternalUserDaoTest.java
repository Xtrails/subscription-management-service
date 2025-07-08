package ru.ani.subscription.management.service.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.ani.subscription.management.service.domain.ExternalUserDaoTestSamples.*;
import static ru.ani.subscription.management.service.domain.ReferralProgramDaoTestSamples.*;

import org.junit.jupiter.api.Test;
import ru.ani.subscription.management.service.web.rest.TestUtil;

class ExternalUserDaoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExternalUserDao.class);
        ExternalUserDao externalUserDao1 = getExternalUserDaoSample1();
        ExternalUserDao externalUserDao2 = new ExternalUserDao();
        assertThat(externalUserDao1).isNotEqualTo(externalUserDao2);

        externalUserDao2.setId(externalUserDao1.getId());
        assertThat(externalUserDao1).isEqualTo(externalUserDao2);

        externalUserDao2 = getExternalUserDaoSample2();
        assertThat(externalUserDao1).isNotEqualTo(externalUserDao2);
    }

    @Test
    void referralCreatorTest() {
        ExternalUserDao externalUserDao = getExternalUserDaoRandomSampleGenerator();
        ReferralProgramDao referralProgramDaoBack = getReferralProgramDaoRandomSampleGenerator();

        externalUserDao.setReferralCreator(referralProgramDaoBack);
        assertThat(externalUserDao.getReferralCreator()).isEqualTo(referralProgramDaoBack);

        externalUserDao.referralCreator(null);
        assertThat(externalUserDao.getReferralCreator()).isNull();
    }

    @Test
    void referralProgramTest() {
        ExternalUserDao externalUserDao = getExternalUserDaoRandomSampleGenerator();
        ReferralProgramDao referralProgramDaoBack = getReferralProgramDaoRandomSampleGenerator();

        externalUserDao.setReferralProgram(referralProgramDaoBack);
        assertThat(externalUserDao.getReferralProgram()).isEqualTo(referralProgramDaoBack);

        externalUserDao.referralProgram(null);
        assertThat(externalUserDao.getReferralProgram()).isNull();
    }
}
