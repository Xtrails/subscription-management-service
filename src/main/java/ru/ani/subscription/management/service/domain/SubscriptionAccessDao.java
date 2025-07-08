package ru.ani.subscription.management.service.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SubscriptionAccessDao.
 */
@Entity
@Table(name = "subscription_access", schema = "subscription_management_service")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SubscriptionAccessDao implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id", unique = true)
    private UUID id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "jhi_order")
    private Integer order;

    @Column(name = "role")
    private String role;

    @Column(name = "role_group")
    private String roleGroup;

    @NotNull
    @Column(name = "active", nullable = false)
    private Boolean active;

    @ManyToMany(fetch = FetchType.LAZY)
    @NotNull
    @JoinTable(
        name = "rel_subscription_access__subscription_details",
        schema = "subscription_management_service",
        joinColumns = @JoinColumn(name = "subscription_access_id"),
        inverseJoinColumns = @JoinColumn(name = "subscription_details_id")
    )
    //    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "sourceApplication", "subscriptionAccesses" }, allowSetters = true)
    private Set<SubscriptionDetailsDao> subscriptionDetails = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public SubscriptionAccessDao id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public SubscriptionAccessDao name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public SubscriptionAccessDao description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getOrder() {
        return this.order;
    }

    public SubscriptionAccessDao order(Integer order) {
        this.setOrder(order);
        return this;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getRole() {
        return this.role;
    }

    public SubscriptionAccessDao role(String role) {
        this.setRole(role);
        return this;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getRoleGroup() {
        return this.roleGroup;
    }

    public SubscriptionAccessDao roleGroup(String roleGroup) {
        this.setRoleGroup(roleGroup);
        return this;
    }

    public void setRoleGroup(String roleGroup) {
        this.roleGroup = roleGroup;
    }

    public Boolean getActive() {
        return this.active;
    }

    public SubscriptionAccessDao active(Boolean active) {
        this.setActive(active);
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Set<SubscriptionDetailsDao> getSubscriptionDetails() {
        return this.subscriptionDetails;
    }

    public void setSubscriptionDetails(Set<SubscriptionDetailsDao> subscriptionDetails) {
        this.subscriptionDetails = subscriptionDetails;
    }

    public SubscriptionAccessDao subscriptionDetails(Set<SubscriptionDetailsDao> subscriptionDetails) {
        this.setSubscriptionDetails(subscriptionDetails);
        return this;
    }

    public SubscriptionAccessDao addSubscriptionDetails(SubscriptionDetailsDao subscriptionDetails) {
        this.subscriptionDetails.add(subscriptionDetails);
        return this;
    }

    public SubscriptionAccessDao removeSubscriptionDetails(SubscriptionDetailsDao subscriptionDetails) {
        this.subscriptionDetails.remove(subscriptionDetails);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SubscriptionAccessDao)) {
            return false;
        }
        return getId() != null && getId().equals(((SubscriptionAccessDao) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SubscriptionAccessDao{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", order=" + getOrder() +
            ", role='" + getRole() + "'" +
            ", roleGroup='" + getRoleGroup() + "'" +
            ", active='" + getActive() + "'" +
            "}";
    }
}
