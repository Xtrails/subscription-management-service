package ru.ani.subscription.management.service.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link ru.ani.subscription.management.service.domain.SubscriptionDetails} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SubscriptionDetailsDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private String description;

    @NotNull
    private BigDecimal price;

    @NotNull
    private Integer duration;

    private SourceApplicationDTO sourceApplication;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public SourceApplicationDTO getSourceApplication() {
        return sourceApplication;
    }

    public void setSourceApplication(SourceApplicationDTO sourceApplication) {
        this.sourceApplication = sourceApplication;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SubscriptionDetailsDTO)) {
            return false;
        }

        SubscriptionDetailsDTO subscriptionDetailsDTO = (SubscriptionDetailsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, subscriptionDetailsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SubscriptionDetailsDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", price=" + getPrice() +
            ", duration=" + getDuration() +
            ", sourceApplication=" + getSourceApplication() +
            "}";
    }
}
