package ru.ani.subscription.management.service.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import org.junit.jupiter.api.Test;
import ru.ani.subscription.management.service.web.rest.TestUtil;

class SourceApplicationDtoTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SourceApplicationDto.class);
        SourceApplicationDto sourceApplicationDto1 = new SourceApplicationDto();
        sourceApplicationDto1.setId(UUID.randomUUID());
        SourceApplicationDto sourceApplicationDto2 = new SourceApplicationDto();
        assertThat(sourceApplicationDto1).isNotEqualTo(sourceApplicationDto2);
        sourceApplicationDto2.setId(sourceApplicationDto1.getId());
        assertThat(sourceApplicationDto1).isEqualTo(sourceApplicationDto2);
        sourceApplicationDto2.setId(UUID.randomUUID());
        assertThat(sourceApplicationDto1).isNotEqualTo(sourceApplicationDto2);
        sourceApplicationDto1.setId(null);
        assertThat(sourceApplicationDto1).isNotEqualTo(sourceApplicationDto2);
    }
}
