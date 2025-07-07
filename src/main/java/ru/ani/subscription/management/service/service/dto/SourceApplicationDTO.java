package ru.ani.subscription.management.service.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link ru.ani.subscription.management.service.domain.SourceApplication} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SourceApplicationDTO implements Serializable {

    private Long id;

    @NotNull
    private String applicationName;

    private ExternalUserDTO user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public ExternalUserDTO getUser() {
        return user;
    }

    public void setUser(ExternalUserDTO user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SourceApplicationDTO)) {
            return false;
        }

        SourceApplicationDTO sourceApplicationDTO = (SourceApplicationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, sourceApplicationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SourceApplicationDTO{" +
            "id=" + getId() +
            ", applicationName='" + getApplicationName() + "'" +
            ", user=" + getUser() +
            "}";
    }
}
