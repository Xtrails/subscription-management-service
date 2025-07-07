package ru.ani.subscription.management.service.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import ru.ani.subscription.management.service.web.rest.TestUtil;

class SourceApplicationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SourceApplicationDTO.class);
        SourceApplicationDTO sourceApplicationDTO1 = new SourceApplicationDTO();
        sourceApplicationDTO1.setId(1L);
        SourceApplicationDTO sourceApplicationDTO2 = new SourceApplicationDTO();
        assertThat(sourceApplicationDTO1).isNotEqualTo(sourceApplicationDTO2);
        sourceApplicationDTO2.setId(sourceApplicationDTO1.getId());
        assertThat(sourceApplicationDTO1).isEqualTo(sourceApplicationDTO2);
        sourceApplicationDTO2.setId(2L);
        assertThat(sourceApplicationDTO1).isNotEqualTo(sourceApplicationDTO2);
        sourceApplicationDTO1.setId(null);
        assertThat(sourceApplicationDTO1).isNotEqualTo(sourceApplicationDTO2);
    }
}
