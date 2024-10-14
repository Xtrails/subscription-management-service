package ru.aniscan.subscription.management.service.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import ru.aniscan.subscription.management.service.domain.enumeration.ReferralStatus;

/**
 * A Referral.
 */
@Entity
@Table(name = "referral")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Referral implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "referral_code", nullable = false, unique = true)
    private String referralCode;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ReferralStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "sourceApplication" }, allowSetters = true)
    private ExternalUser referrer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "sourceApplication" }, allowSetters = true)
    private ReferralProgram referralProgram;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "referralPrograms", "subscriptionTypes", "paymentSystems" }, allowSetters = true)
    private SourceApplication sourceApplication;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Referral id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReferralCode() {
        return this.referralCode;
    }

    public Referral referralCode(String referralCode) {
        this.setReferralCode(referralCode);
        return this;
    }

    public void setReferralCode(String referralCode) {
        this.referralCode = referralCode;
    }

    public ReferralStatus getStatus() {
        return this.status;
    }

    public Referral status(ReferralStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(ReferralStatus status) {
        this.status = status;
    }

    public ExternalUser getReferrer() {
        return this.referrer;
    }

    public void setReferrer(ExternalUser externalUser) {
        this.referrer = externalUser;
    }

    public Referral referrer(ExternalUser externalUser) {
        this.setReferrer(externalUser);
        return this;
    }

    public ReferralProgram getReferralProgram() {
        return this.referralProgram;
    }

    public void setReferralProgram(ReferralProgram referralProgram) {
        this.referralProgram = referralProgram;
    }

    public Referral referralProgram(ReferralProgram referralProgram) {
        this.setReferralProgram(referralProgram);
        return this;
    }

    public SourceApplication getSourceApplication() {
        return this.sourceApplication;
    }

    public void setSourceApplication(SourceApplication sourceApplication) {
        this.sourceApplication = sourceApplication;
    }

    public Referral sourceApplication(SourceApplication sourceApplication) {
        this.setSourceApplication(sourceApplication);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Referral)) {
            return false;
        }
        return getId() != null && getId().equals(((Referral) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Referral{" +
            "id=" + getId() +
            ", referralCode='" + getReferralCode() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
