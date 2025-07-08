package ru.ani.subscription.management.service.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SubscriptionDetailsDao.
 */
@Entity
@Table(name = "subscription_details")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SubscriptionDetailsDao implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false, unique = true)
    private UUID id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "price", precision = 21, scale = 2, nullable = false)
    private BigDecimal price;

    @NotNull
    @Column(name = "duration", nullable = false)
    private Integer duration;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "referralPrograms", "subscriptionDetails", "user" }, allowSetters = true)
    private SourceApplicationDao sourceApplication;

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

    public Integer getDuration() {
        return this.duration;
    }

    public SubscriptionDetailsDao duration(Integer duration) {
        this.setDuration(duration);
        return this;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
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
            ", duration=" + getDuration() +
            "}";
    }
}
