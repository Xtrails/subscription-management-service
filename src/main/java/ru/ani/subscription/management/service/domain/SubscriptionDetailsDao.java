package ru.ani.subscription.management.service.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SubscriptionDetailsDao.
 */
@Entity
@Table(name = "subscription_details", schema = "subscription_management_service")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SubscriptionDetailsDao implements Serializable {

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

    @NotNull
    @Column(name = "price", precision = 21, scale = 2, nullable = false)
    private BigDecimal price;

    @Column(name = "price_by_month", precision = 21, scale = 2)
    private BigDecimal priceByMonth;

    @NotNull
    @Column(name = "duration", nullable = false)
    private String duration;

    @NotNull
    @Column(name = "active", nullable = false)
    private Boolean active;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "referralPrograms", "subscriptionDetails", "user" }, allowSetters = true)
    private SourceApplicationDao sourceApplication;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "subscriptionDetails", cascade = CascadeType.MERGE)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "subscriptionDetails" }, allowSetters = true)
    private Set<SubscriptionAccessDao> subscriptionAccesses = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public SubscriptionDetailsDao id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public SubscriptionDetailsDao name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public SubscriptionDetailsDao description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public SubscriptionDetailsDao price(BigDecimal price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getPriceByMonth() {
        return this.priceByMonth;
    }

    public SubscriptionDetailsDao priceByMonth(BigDecimal priceByMonth) {
        this.setPriceByMonth(priceByMonth);
        return this;
    }

    public void setPriceByMonth(BigDecimal priceByMonth) {
        this.priceByMonth = priceByMonth;
    }

    public String getDuration() {
        return this.duration;
    }

    public SubscriptionDetailsDao duration(String duration) {
        this.setDuration(duration);
        return this;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Boolean getActive() {
        return this.active;
    }

    public SubscriptionDetailsDao active(Boolean active) {
        this.setActive(active);
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public SourceApplicationDao getSourceApplication() {
        return this.sourceApplication;
    }

    public void setSourceApplication(SourceApplicationDao sourceApplication) {
        this.sourceApplication = sourceApplication;
    }

    public SubscriptionDetailsDao sourceApplication(SourceApplicationDao sourceApplication) {
        this.setSourceApplication(sourceApplication);
        return this;
    }

    public Set<SubscriptionAccessDao> getSubscriptionAccesses() {
        return this.subscriptionAccesses;
    }

    public void setSubscriptionAccesses(Set<SubscriptionAccessDao> subscriptionAccesses) {
        if (this.subscriptionAccesses != null) {
            this.subscriptionAccesses.forEach(i -> i.removeSubscriptionDetails(this));
        }
        if (subscriptionAccesses != null) {
            subscriptionAccesses.forEach(i -> i.addSubscriptionDetails(this));
        }
        this.subscriptionAccesses = subscriptionAccesses;
    }

    public SubscriptionDetailsDao subscriptionAccesses(Set<SubscriptionAccessDao> subscriptionAccesses) {
        this.setSubscriptionAccesses(subscriptionAccesses);
        return this;
    }

    public SubscriptionDetailsDao addSubscriptionAccess(SubscriptionAccessDao subscriptionAccess) {
        this.subscriptionAccesses.add(subscriptionAccess);
        subscriptionAccess.getSubscriptionDetails().add(this);
        return this;
    }

    public SubscriptionDetailsDao removeSubscriptionAccess(SubscriptionAccessDao subscriptionAccess) {
        this.subscriptionAccesses.remove(subscriptionAccess);
        subscriptionAccess.getSubscriptionDetails().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SubscriptionDetailsDao)) {
            return false;
        }
        return getId() != null && getId().equals(((SubscriptionDetailsDao) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SubscriptionDetailsDao{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", price=" + getPrice() +
            ", priceByMonth=" + getPriceByMonth() +
            ", duration='" + getDuration() + "'" +
            ", active='" + getActive() + "'" +
            "}";
    }
}
