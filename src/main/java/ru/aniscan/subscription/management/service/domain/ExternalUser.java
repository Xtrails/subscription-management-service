package ru.aniscan.subscription.management.service.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * A ExternalUser.
 */
@Entity
@Table(name = "external_user")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ExternalUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "external_user_id", nullable = false, unique = true)
    private String externalUserId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "referralPrograms", "subscriptionTypes", "paymentSystems" }, allowSetters = true)
    private SourceApplication sourceApplication;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ExternalUser id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExternalUserId() {
        return this.externalUserId;
    }

    public ExternalUser externalUserId(String externalUserId) {
        this.setExternalUserId(externalUserId);
        return this;
    }

    public void setExternalUserId(String externalUserId) {
        this.externalUserId = externalUserId;
    }

    public SourceApplication getSourceApplication() {
        return this.sourceApplication;
    }

    public void setSourceApplication(SourceApplication sourceApplication) {
        this.sourceApplication = sourceApplication;
    }

    public ExternalUser sourceApplication(SourceApplication sourceApplication) {
        this.setSourceApplication(sourceApplication);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExternalUser)) {
            return false;
        }
        return getId() != null && getId().equals(((ExternalUser) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExternalUser{" +
            "id=" + getId() +
            ", externalUserId='" + getExternalUserId() + "'" +
            "}";
    }
}
