package ru.ani.subscription.management.service.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * A DTO for the {@link ru.ani.subscription.management.service.domain.SubscriptionAccessDao} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SubscriptionAccessDto implements Serializable {

    private UUID id;

    @NotNull
    private String name;

    private String description;

    private Integer order;

    private String role;

    private String roleGroup;

    @NotNull
    private Boolean active;

    @NotNull
    private Set<SubscriptionDetailsDto> subscriptionDetails = new HashSet<>();

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

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getRoleGroup() {
        return roleGroup;
    }

    public void setRoleGroup(String roleGroup) {
        this.roleGroup = roleGroup;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Set<SubscriptionDetailsDto> getSubscriptionDetails() {
        return subscriptionDetails;
    }

    public void setSubscriptionDetails(Set<SubscriptionDetailsDto> subscriptionDetails) {
        this.subscriptionDetails = subscriptionDetails;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SubscriptionAccessDto)) {
            return false;
        }

        SubscriptionAccessDto subscriptionAccessDto = (SubscriptionAccessDto) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, subscriptionAccessDto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SubscriptionAccessDto{" +
            "id='" + getId() + "'" +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", order=" + getOrder() +
            ", role='" + getRole() + "'" +
            ", roleGroup='" + getRoleGroup() + "'" +
            ", active='" + getActive() + "'" +
            ", subscriptionDetails=" + getSubscriptionDetails() +
            "}";
    }
}
