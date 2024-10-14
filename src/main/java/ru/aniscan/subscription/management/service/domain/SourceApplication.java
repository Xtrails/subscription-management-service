package ru.aniscan.subscription.management.service.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A SourceApplication.
 */
@Entity
@Table(name = "source_application")
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
    @JsonIgnoreProperties(value = { "sourceApplication" }, allowSetters = true)
    private Set<ReferralProgram> referralPrograms = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "sourceApplication")
    @JsonIgnoreProperties(value = { "sourceApplication" }, allowSetters = true)
    private Set<SubscriptionType> subscriptionTypes = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "sourceApplication")
    @JsonIgnoreProperties(value = { "sourceApplication" }, allowSetters = true)
    private Set<ExternalUser> users = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "sourceApplications")
    @JsonIgnoreProperties(value = { "sourceApplications" }, allowSetters = true)
    private Set<PaymentSystem> paymentSystems = new HashSet<>();

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

    public Set<SubscriptionType> getSubscriptionTypes() {
        return this.subscriptionTypes;
    }

    public void setSubscriptionTypes(Set<SubscriptionType> subscriptionTypes) {
        if (this.subscriptionTypes != null) {
            this.subscriptionTypes.forEach(i -> i.setSourceApplication(null));
        }
        if (subscriptionTypes != null) {
            subscriptionTypes.forEach(i -> i.setSourceApplication(this));
        }
        this.subscriptionTypes = subscriptionTypes;
    }

    public SourceApplication subscriptionTypes(Set<SubscriptionType> subscriptionTypes) {
        this.setSubscriptionTypes(subscriptionTypes);
        return this;
    }

    public SourceApplication addSubscriptionTypes(SubscriptionType subscriptionType) {
        this.subscriptionTypes.add(subscriptionType);
        subscriptionType.setSourceApplication(this);
        return this;
    }

    public SourceApplication removeSubscriptionTypes(SubscriptionType subscriptionType) {
        this.subscriptionTypes.remove(subscriptionType);
        subscriptionType.setSourceApplication(null);
        return this;
    }

    public Set<ExternalUser> getUsers() {
        return this.users;
    }

    public void setUsers(Set<ExternalUser> externalUsers) {
        if (this.users != null) {
            this.users.forEach(i -> i.setSourceApplication(null));
        }
        if (externalUsers != null) {
            externalUsers.forEach(i -> i.setSourceApplication(this));
        }
        this.users = externalUsers;
    }

    public SourceApplication users(Set<ExternalUser> externalUsers) {
        this.setUsers(externalUsers);
        return this;
    }

    public SourceApplication addUsers(ExternalUser externalUser) {
        this.users.add(externalUser);
        externalUser.setSourceApplication(this);
        return this;
    }

    public SourceApplication removeUsers(ExternalUser externalUser) {
        this.users.remove(externalUser);
        externalUser.setSourceApplication(null);
        return this;
    }

    public Set<PaymentSystem> getPaymentSystems() {
        return this.paymentSystems;
    }

    public void setPaymentSystems(Set<PaymentSystem> paymentSystems) {
        if (this.paymentSystems != null) {
            this.paymentSystems.forEach(i -> i.removeSourceApplications(this));
        }
        if (paymentSystems != null) {
            paymentSystems.forEach(i -> i.addSourceApplications(this));
        }
        this.paymentSystems = paymentSystems;
    }

    public SourceApplication paymentSystems(Set<PaymentSystem> paymentSystems) {
        this.setPaymentSystems(paymentSystems);
        return this;
    }

    public SourceApplication addPaymentSystems(PaymentSystem paymentSystem) {
        this.paymentSystems.add(paymentSystem);
        paymentSystem.getSourceApplications().add(this);
        return this;
    }

    public SourceApplication removePaymentSystems(PaymentSystem paymentSystem) {
        this.paymentSystems.remove(paymentSystem);
        paymentSystem.getSourceApplications().remove(this);
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
