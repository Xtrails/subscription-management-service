package ru.ani.subscription.management.service.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import ru.ani.subscription.management.service.domain.enumeration.ReferralStatus;

/**
 * A ReferralProgramDao.
 */
@Entity
@Table(name = "referral_program", schema = "subscription_management_service")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReferralProgramDao implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id", unique = true)
    private UUID id;

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

    @ManyToOne(fetch = FetchType.LAZY)
    private ExternalUserDao referralCreator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "referralPrograms", "subscriptionDetails", "user" }, allowSetters = true)
    private SourceApplicationDao sourceApplication;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public ReferralProgramDao id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public ReferralProgramDao name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReferralCode() {
        return this.referralCode;
    }

    public ReferralProgramDao referralCode(String referralCode) {
        this.setReferralCode(referralCode);
        return this;
    }

    public void setReferralCode(String referralCode) {
        this.referralCode = referralCode;
    }

    public String getDescription() {
        return this.description;
    }

    public ReferralProgramDao description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getStartDttm() {
        return this.startDttm;
    }

    public ReferralProgramDao startDttm(LocalDate startDttm) {
        this.setStartDttm(startDttm);
        return this;
    }

    public void setStartDttm(LocalDate startDttm) {
        this.startDttm = startDttm;
    }

    public LocalDate getEndDttm() {
        return this.endDttm;
    }

    public ReferralProgramDao endDttm(LocalDate endDttm) {
        this.setEndDttm(endDttm);
        return this;
    }

    public void setEndDttm(LocalDate endDttm) {
        this.endDttm = endDttm;
    }

    public BigDecimal getRewardAmount() {
        return this.rewardAmount;
    }

    public ReferralProgramDao rewardAmount(BigDecimal rewardAmount) {
        this.setRewardAmount(rewardAmount);
        return this;
    }

    public void setRewardAmount(BigDecimal rewardAmount) {
        this.rewardAmount = rewardAmount;
    }

    public ReferralStatus getStatus() {
        return this.status;
    }

    public ReferralProgramDao status(ReferralStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(ReferralStatus status) {
        this.status = status;
    }

    public ExternalUserDao getReferralCreator() {
        return this.referralCreator;
    }

    public void setReferralCreator(ExternalUserDao externalUser) {
        this.referralCreator = externalUser;
    }

    public ReferralProgramDao referralCreator(ExternalUserDao externalUser) {
        this.setReferralCreator(externalUser);
        return this;
    }

    public SourceApplicationDao getSourceApplication() {
        return this.sourceApplication;
    }

    public void setSourceApplication(SourceApplicationDao sourceApplication) {
        this.sourceApplication = sourceApplication;
    }

    public ReferralProgramDao sourceApplication(SourceApplicationDao sourceApplication) {
        this.setSourceApplication(sourceApplication);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReferralProgramDao)) {
            return false;
        }
        return getId() != null && getId().equals(((ReferralProgramDao) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReferralProgramDao{" +
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
