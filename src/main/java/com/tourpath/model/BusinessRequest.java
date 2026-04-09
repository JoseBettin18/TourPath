package com.tourpath.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import java.time.LocalDateTime;

@Document(collection = "business_requests")
public class BusinessRequest {

    @Id
    private String id;

    // ── Datos del solicitante ─────────────────────────────────────
    @NotBlank(message = "Tu nombre es obligatorio")
    private String ownerName;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "Correo inválido")
    private String ownerEmail;

    private String ownerPhone;

    // ── Datos del establecimiento ─────────────────────────────────
    @NotBlank(message = "El nombre del establecimiento es obligatorio")
    private String businessName;

    @NotBlank(message = "La descripción es obligatoria")
    private String businessDescription;

    private String businessCategory;
    private String businessAddress;
    private String businessCityId;
    private String businessCityName;
    private String businessCountry;
    private String businessPhone;
    private String businessEmail;
    private String businessWebsite;
    private String businessBookingUrl;
    private String businessBookingLabel;
    private String businessImage;
    private String openingHours;
    private String priceRange;

    // ── Estado de la solicitud ────────────────────────────────────
    private Status status;
    private String rejectionReason;
    private String reviewedBy;
    private LocalDateTime reviewedAt;
    private LocalDateTime createdAt;

    // ID del usuario que hizo la solicitud (si estaba autenticado)
    private String requestingUserId;

    public enum Status {
        PENDING("Pendiente", "⏳"),
        APPROVED("Aprobado", "✅"),
        REJECTED("Rechazado", "❌");

        private final String displayName;
        private final String emoji;

        Status(String displayName, String emoji) {
            this.displayName = displayName;
            this.emoji = emoji;
        }

        public String getDisplayName() {
            return displayName;
        }

        public String getEmoji() {
            return emoji;
        }
    }

    public BusinessRequest() {
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String id, ownerName, ownerEmail, ownerPhone;
        private String businessName, businessDescription, businessCategory;
        private String businessAddress, businessCityId, businessCityName;
        private String businessCountry, businessPhone, businessEmail;
        private String businessWebsite, businessBookingUrl, businessBookingLabel;
        private String businessImage, openingHours, priceRange;
        private Status status;
        private String rejectionReason, reviewedBy, requestingUserId;
        private LocalDateTime reviewedAt, createdAt;

        public Builder id(String v) {
            this.id = v;
            return this;
        }

        public Builder ownerName(String v) {
            this.ownerName = v;
            return this;
        }

        public Builder ownerEmail(String v) {
            this.ownerEmail = v;
            return this;
        }

        public Builder ownerPhone(String v) {
            this.ownerPhone = v;
            return this;
        }

        public Builder businessName(String v) {
            this.businessName = v;
            return this;
        }

        public Builder businessDescription(String v) {
            this.businessDescription = v;
            return this;
        }

        public Builder businessCategory(String v) {
            this.businessCategory = v;
            return this;
        }

        public Builder businessAddress(String v) {
            this.businessAddress = v;
            return this;
        }

        public Builder businessCityId(String v) {
            this.businessCityId = v;
            return this;
        }

        public Builder businessCityName(String v) {
            this.businessCityName = v;
            return this;
        }

        public Builder businessCountry(String v) {
            this.businessCountry = v;
            return this;
        }

        public Builder businessPhone(String v) {
            this.businessPhone = v;
            return this;
        }

        public Builder businessEmail(String v) {
            this.businessEmail = v;
            return this;
        }

        public Builder businessWebsite(String v) {
            this.businessWebsite = v;
            return this;
        }

        public Builder businessBookingUrl(String v) {
            this.businessBookingUrl = v;
            return this;
        }

        public Builder businessBookingLabel(String v) {
            this.businessBookingLabel = v;
            return this;
        }

        public Builder businessImage(String v) {
            this.businessImage = v;
            return this;
        }

        public Builder openingHours(String v) {
            this.openingHours = v;
            return this;
        }

        public Builder priceRange(String v) {
            this.priceRange = v;
            return this;
        }

        public Builder status(Status v) {
            this.status = v;
            return this;
        }

        public Builder rejectionReason(String v) {
            this.rejectionReason = v;
            return this;
        }

        public Builder reviewedBy(String v) {
            this.reviewedBy = v;
            return this;
        }

        public Builder requestingUserId(String v) {
            this.requestingUserId = v;
            return this;
        }

        public Builder reviewedAt(LocalDateTime v) {
            this.reviewedAt = v;
            return this;
        }

        public Builder createdAt(LocalDateTime v) {
            this.createdAt = v;
            return this;
        }

        public BusinessRequest build() {
            BusinessRequest r = new BusinessRequest();
            r.id = id;
            r.ownerName = ownerName;
            r.ownerEmail = ownerEmail;
            r.ownerPhone = ownerPhone;
            r.businessName = businessName;
            r.businessDescription = businessDescription;
            r.businessCategory = businessCategory;
            r.businessAddress = businessAddress;
            r.businessCityId = businessCityId;
            r.businessCityName = businessCityName;
            r.businessCountry = businessCountry;
            r.businessPhone = businessPhone;
            r.businessEmail = businessEmail;
            r.businessWebsite = businessWebsite;
            r.businessBookingUrl = businessBookingUrl;
            r.businessBookingLabel = businessBookingLabel;
            r.businessImage = businessImage;
            r.openingHours = openingHours;
            r.priceRange = priceRange;
            r.status = status;
            r.rejectionReason = rejectionReason;
            r.reviewedBy = reviewedBy;
            r.requestingUserId = requestingUserId;
            r.reviewedAt = reviewedAt;
            r.createdAt = createdAt;
            return r;
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String v) {
        this.id = v;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String v) {
        this.ownerName = v;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public void setOwnerEmail(String v) {
        this.ownerEmail = v;
    }

    public String getOwnerPhone() {
        return ownerPhone;
    }

    public void setOwnerPhone(String v) {
        this.ownerPhone = v;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String v) {
        this.businessName = v;
    }

    public String getBusinessDescription() {
        return businessDescription;
    }

    public void setBusinessDescription(String v) {
        this.businessDescription = v;
    }

    public String getBusinessCategory() {
        return businessCategory;
    }

    public void setBusinessCategory(String v) {
        this.businessCategory = v;
    }

    public String getBusinessAddress() {
        return businessAddress;
    }

    public void setBusinessAddress(String v) {
        this.businessAddress = v;
    }

    public String getBusinessCityId() {
        return businessCityId;
    }

    public void setBusinessCityId(String v) {
        this.businessCityId = v;
    }

    public String getBusinessCityName() {
        return businessCityName;
    }

    public void setBusinessCityName(String v) {
        this.businessCityName = v;
    }

    public String getBusinessCountry() {
        return businessCountry;
    }

    public void setBusinessCountry(String v) {
        this.businessCountry = v;
    }

    public String getBusinessPhone() {
        return businessPhone;
    }

    public void setBusinessPhone(String v) {
        this.businessPhone = v;
    }

    public String getBusinessEmail() {
        return businessEmail;
    }

    public void setBusinessEmail(String v) {
        this.businessEmail = v;
    }

    public String getBusinessWebsite() {
        return businessWebsite;
    }

    public void setBusinessWebsite(String v) {
        this.businessWebsite = v;
    }

    public String getBusinessBookingUrl() {
        return businessBookingUrl;
    }

    public void setBusinessBookingUrl(String v) {
        this.businessBookingUrl = v;
    }

    public String getBusinessBookingLabel() {
        return businessBookingLabel;
    }

    public void setBusinessBookingLabel(String v) {
        this.businessBookingLabel = v;
    }

    public String getBusinessImage() {
        return businessImage;
    }

    public void setBusinessImage(String v) {
        this.businessImage = v;
    }

    public String getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(String v) {
        this.openingHours = v;
    }

    public String getPriceRange() {
        return priceRange;
    }

    public void setPriceRange(String v) {
        this.priceRange = v;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status v) {
        this.status = v;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String v) {
        this.rejectionReason = v;
    }

    public String getReviewedBy() {
        return reviewedBy;
    }

    public void setReviewedBy(String v) {
        this.reviewedBy = v;
    }

    public String getRequestingUserId() {
        return requestingUserId;
    }

    public void setRequestingUserId(String v) {
        this.requestingUserId = v;
    }

    public LocalDateTime getReviewedAt() {
        return reviewedAt;
    }

    public void setReviewedAt(LocalDateTime v) {
        this.reviewedAt = v;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime v) {
        this.createdAt = v;
    }
}
