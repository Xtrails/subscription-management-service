package ru.ani.subscription.management.service.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link ru.ani.subscription.management.service.domain.SourceApplicationDao} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SourceApplicationDto implements Serializable {

    private UUID id;

    @NotNull
    private String applicationName;

    private ExternalUserDto user;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public ExternalUserDto getUser() {
        return user;
    }

    public void setUser(ExternalUserDto user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SourceApplicationDto)) {
            return false;
        }

        SourceApplicationDto sourceApplicationDto = (SourceApplicationDto) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, sourceApplicationDto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SourceApplicationDto{" +
            "id='" + getId() + "'" +
            ", applicationName='" + getApplicationName() + "'" +
            ", user=" + getUser() +
            "}";
    }
}
