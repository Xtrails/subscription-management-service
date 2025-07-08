package ru.ani.subscription.management.service.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * A DTO for the {@link ru.ani.subscription.management.service.domain.SubscriptionDetailsDao} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SubscriptionDetailsDto implements Serializable {

    private UUID id;

    @NotNull
    private String name;

    private String description;

    @NotNull
    private BigDecimal price;

    private BigDecimal priceByMonth;

    @NotNull
    private String duration;

    @NotNull
    private Boolean active;

    private SourceApplicationDto sourceApplication;

    private Set<SubscriptionAccessDto> subscriptionAccesses = new HashSet<>();

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
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

    public BigDecimal getPriceByMonth() {
        return priceByMonth;
    }

    public void setPriceByMonth(BigDecimal priceByMonth) {
        this.priceByMonth = priceByMonth;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public SourceApplicationDto getSourceApplication() {
        return sourceApplication;
    }

    public void setSourceApplication(SourceApplicationDto sourceApplication) {
        this.sourceApplication = sourceApplication;
    }

    public Set<SubscriptionAccessDto> getSubscriptionAccesses() {
        return subscriptionAccesses;
    }

    public void setSubscriptionAccesses(Set<SubscriptionAccessDto> subscriptionAccesses) {
        this.subscriptionAccesses = subscriptionAccesses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SubscriptionDetailsDto)) {
            return false;
        }

        SubscriptionDetailsDto subscriptionDetailsDto = (SubscriptionDetailsDto) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, subscriptionDetailsDto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SubscriptionDetailsDto{" +
            "id='" + getId() + "'" +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", price=" + getPrice() +
            ", priceByMonth=" + getPriceByMonth() +
            ", duration='" + getDuration() + "'" +
            ", active='" + getActive() + "'" +
            ", sourceApplication=" + getSourceApplication() +
            ", subscriptionAccesses=" + getSubscriptionAccesses() +
            "}";
    }
}
