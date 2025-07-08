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
 * A SourceApplicationDao.
 */
@Entity
@Table(name = "source_application")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SourceApplicationDao implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false, unique = true)
    private UUID id;

    @NotNull
    @Column(name = "application_name", nullable = false, unique = true)
    private String applicationName;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "sourceApplication")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "externalUser", "sourceApplication" }, allowSetters = true)
    private Set<ReferralProgramDao> referralPrograms = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "sourceApplication")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "sourceApplication" }, allowSetters = true)
    private Set<SubscriptionDetailsDao> subscriptionDetails = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "referralCreator", "referralProgram" }, allowSetters = true)
    private ExternalUserDao user;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public SourceApplicationDao id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getApplicationName() {
        return this.applicationName;
    }

    public SourceApplicationDao applicationName(String applicationName) {
        this.setApplicationName(applicationName);
        return this;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public Set<ReferralProgramDao> getReferralPrograms() {
        return this.referralPrograms;
    }

    public void setReferralPrograms(Set<ReferralProgramDao> referralPrograms) {
        if (this.referralPrograms != null) {
            this.referralPrograms.forEach(i -> i.setSourceApplication(null));
        }
        if (referralPrograms != null) {
            referralPrograms.forEach(i -> i.setSourceApplication(this));
        }
        this.referralPrograms = referralPrograms;
    }

    public SourceApplicationDao referralPrograms(Set<ReferralProgramDao> referralPrograms) {
        this.setReferralPrograms(referralPrograms);
        return this;
    }

    public SourceApplicationDao addReferralPrograms(ReferralProgramDao referralProgram) {
        this.referralPrograms.add(referralProgram);
        referralProgram.setSourceApplication(this);
        return this;
    }

    public SourceApplicationDao removeReferralPrograms(ReferralProgramDao referralProgram) {
        this.referralPrograms.remove(referralProgram);
        referralProgram.setSourceApplication(null);
        return this;
    }

    public Set<SubscriptionDetailsDao> getSubscriptionDetails() {
        return this.subscriptionDetails;
    }

    public void setSubscriptionDetails(Set<SubscriptionDetailsDao> subscriptionDetails) {
        if (this.subscriptionDetails != null) {
            this.subscriptionDetails.forEach(i -> i.setSourceApplication(null));
        }
        if (subscriptionDetails != null) {
            subscriptionDetails.forEach(i -> i.setSourceApplication(this));
        }
        this.subscriptionDetails = subscriptionDetails;
    }

    public SourceApplicationDao subscriptionDetails(Set<SubscriptionDetailsDao> subscriptionDetails) {
        this.setSubscriptionDetails(subscriptionDetails);
        return this;
    }

    public SourceApplicationDao addSubscriptionDetails(SubscriptionDetailsDao subscriptionDetails) {
        this.subscriptionDetails.add(subscriptionDetails);
        subscriptionDetails.setSourceApplication(this);
        return this;
    }

    public SourceApplicationDao removeSubscriptionDetails(SubscriptionDetailsDao subscriptionDetails) {
        this.subscriptionDetails.remove(subscriptionDetails);
        subscriptionDetails.setSourceApplication(null);
        return this;
    }

    public ExternalUserDao getUser() {
        return this.user;
    }

    public void setUser(ExternalUserDao externalUser) {
        this.user = externalUser;
    }

    public SourceApplicationDao user(ExternalUserDao externalUser) {
        this.setUser(externalUser);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SourceApplicationDao)) {
            return false;
        }
        return getId() != null && getId().equals(((SourceApplicationDao) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SourceApplicationDao{" +
            "id=" + getId() +
            ", applicationName='" + getApplicationName() + "'" +
            "}";
    }
}
