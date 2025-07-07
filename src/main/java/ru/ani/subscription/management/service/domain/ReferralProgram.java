package ru.ani.subscription.management.service.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import ru.ani.subscription.management.service.domain.enumeration.ReferralStatus;

/**
 * A ReferralProgram.
 */
@Entity
@Table(name = "referral_program")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReferralProgram implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "referral_code", nullable = false, unique = true)
    private String referralCode;

    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "start_dttm", nullable = false)
    private LocalDate startDttm;

    @NotNull
    @Column(name = "end_dttm", nullable = false)
    private LocalDate endDttm;

    @NotNull
    @Column(name = "reward_amount", precision = 21, scale = 2, nullable = false)
    private BigDecimal rewardAmount;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ReferralStatus status;

    @JsonIgnoreProperties(value = { "referralCreator", "referralProgram" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "referralCreator")
    private ExternalUser externalUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "referralPrograms", "subscriptionDetails", "user" }, allowSetters = true)
    private SourceApplication sourceApplication;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ReferralProgram id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public ReferralProgram name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReferralCode() {
        return this.referralCode;
    }

    public ReferralProgram referralCode(String referralCode) {
        this.setReferralCode(referralCode);
        return this;
    }

    public void setReferralCode(String referralCode) {
        this.referralCode = referralCode;
    }

    public String getDescription() {
        return this.description;
    }

    public ReferralProgram description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getStartDttm() {
        return this.startDttm;
    }

    public ReferralProgram startDttm(LocalDate startDttm) {
        this.setStartDttm(startDttm);
        return this;
    }

    public void setStartDttm(LocalDate startDttm) {
        this.startDttm = startDttm;
    }

    public LocalDate getEndDttm() {
        return this.endDttm;
    }

    public ReferralProgram endDttm(LocalDate endDttm) {
        this.setEndDttm(endDttm);
        return this;
    }

    public void setEndDttm(LocalDate endDttm) {
        this.endDttm = endDttm;
    }

    public BigDecimal getRewardAmount() {
        return this.rewardAmount;
    }

    public ReferralProgram rewardAmount(BigDecimal rewardAmount) {
        this.setRewardAmount(rewardAmount);
        return this;
    }

    public void setRewardAmount(BigDecimal rewardAmount) {
        this.rewardAmount = rewardAmount;
    }

    public ReferralStatus getStatus() {
        return this.status;
    }

    public ReferralProgram status(ReferralStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(ReferralStatus status) {
        this.status = status;
    }

    public ExternalUser getExternalUser() {
        return this.externalUser;
    }

    public void setExternalUser(ExternalUser externalUser) {
        if (this.externalUser != null) {
            this.externalUser.setReferralCreator(null);
        }
        if (externalUser != null) {
            externalUser.setReferralCreator(this);
        }
        this.externalUser = externalUser;
    }

    public ReferralProgram externalUser(ExternalUser externalUser) {
        this.setExternalUser(externalUser);
        return this;
    }

    public SourceApplication getSourceApplication() {
        return this.sourceApplication;
    }

    public void setSourceApplication(SourceApplication sourceApplication) {
        this.sourceApplication = sourceApplication;
    }

    public ReferralProgram sourceApplication(SourceApplication sourceApplication) {
        this.setSourceApplication(sourceApplication);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReferralProgram)) {
            return false;
        }
        return getId() != null && getId().equals(((ReferralProgram) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReferralProgram{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", referralCode='" + getReferralCode() + "'" +
            ", description='" + getDescription() + "'" +
            ", startDttm='" + getStartDttm() + "'" +
            ", endDttm='" + getEndDttm() + "'" +
            ", rewardAmount=" + getRewardAmount() +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
