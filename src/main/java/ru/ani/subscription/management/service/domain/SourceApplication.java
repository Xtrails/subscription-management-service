package ru.ani.subscription.management.service.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SourceApplication.
 */
@Entity
@Table(name = "source_application")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SourceApplication implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "application_name", nullable = false, unique = true)
    private String applicationName;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "sourceApplication")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "externalUser", "sourceApplication" }, allowSetters = true)
    private Set<ReferralProgram> referralPrograms = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "sourceApplication")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "sourceApplication" }, allowSetters = true)
    private Set<SubscriptionDetails> subscriptionDetails = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "referralCreator", "referralProgram" }, allowSetters = true)
    private ExternalUser user;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SourceApplication id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getApplicationName() {
        return this.applicationName;
    }

    public SourceApplication applicationName(String applicationName) {
        this.setApplicationName(applicationName);
        return this;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public Set<ReferralProgram> getReferralPrograms() {
        return this.referralPrograms;
    }

    public void setReferralPrograms(Set<ReferralProgram> referralPrograms) {
        if (this.referralPrograms != null) {
            this.referralPrograms.forEach(i -> i.setSourceApplication(null));
        }
        if (referralPrograms != null) {
            referralPrograms.forEach(i -> i.setSourceApplication(this));
        }
        this.referralPrograms = referralPrograms;
    }

    public SourceApplication referralPrograms(Set<ReferralProgram> referralPrograms) {
        this.setReferralPrograms(referralPrograms);
        return this;
    }

    public SourceApplication addReferralPrograms(ReferralProgram referralProgram) {
        this.referralPrograms.add(referralProgram);
        referralProgram.setSourceApplication(this);
        return this;
    }

    public SourceApplication removeReferralPrograms(ReferralProgram referralProgram) {
        this.referralPrograms.remove(referralProgram);
        referralProgram.setSourceApplication(null);
        return this;
    }

    public Set<SubscriptionDetails> getSubscriptionDetails() {
        return this.subscriptionDetails;
    }

    public void setSubscriptionDetails(Set<SubscriptionDetails> subscriptionDetails) {
        if (this.subscriptionDetails != null) {
            this.subscriptionDetails.forEach(i -> i.setSourceApplication(null));
        }
        if (subscriptionDetails != null) {
            subscriptionDetails.forEach(i -> i.setSourceApplication(this));
        }
        this.subscriptionDetails = subscriptionDetails;
    }

    public SourceApplication subscriptionDetails(Set<SubscriptionDetails> subscriptionDetails) {
        this.setSubscriptionDetails(subscriptionDetails);
        return this;
    }

    public SourceApplication addSubscriptionDetails(SubscriptionDetails subscriptionDetails) {
        this.subscriptionDetails.add(subscriptionDetails);
        subscriptionDetails.setSourceApplication(this);
        return this;
    }

    public SourceApplication removeSubscriptionDetails(SubscriptionDetails subscriptionDetails) {
        this.subscriptionDetails.remove(subscriptionDetails);
        subscriptionDetails.setSourceApplication(null);
        return this;
    }

    public ExternalUser getUser() {
        return this.user;
    }

    public void setUser(ExternalUser externalUser) {
        this.user = externalUser;
    }

    public SourceApplication user(ExternalUser externalUser) {
        this.setUser(externalUser);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SourceApplication)) {
            return false;
        }
        return getId() != null && getId().equals(((SourceApplication) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SourceApplication{" +
            "id=" + getId() +
            ", applicationName='" + getApplicationName() + "'" +
            "}";
    }
}
