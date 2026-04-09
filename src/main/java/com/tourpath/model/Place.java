package com.tourpath.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "places")
public class Place {

    @Id
    private String id;

    @NotBlank(message = "El nombre es obligatorio")
    private String name;

    @NotBlank(message = "La descripción es obligatoria")
    private String description;

    @NotNull(message = "La categoría es obligatoria")
    private Category category;

    // Vinculado a la ciudad
    @NotBlank(message = "La ciudad es obligatoria")
    private String cityId;
    private String cityName;
    private String countryName;

    private String address;
    private String phone;
    private String website;
    private String email;

    // ── Link de reserva directa ───────────────────────────────────
    private String bookingUrl; // URL externa de reservas
    private String bookingLabel; // Texto del botón: "Reservar", "Ver disponibilidad", etc.

    private Double latitude;
    private Double longitude;
    private String mainImage;
    private List<String> images;
    private double averageRating;
    private int totalReviews;
    private boolean featured;
    private boolean active;
    private String openingHours;
    private String priceRange;
    private List<String> tags;

    // ── Dueño del establecimiento ─────────────────────────────────
    private String ownerId;
    private String ownerName;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public enum Category {
        SITIO_TURISTICO("Sitio Turístico", "🏛️"),
        RESTAURANTE("Restaurante", "🍽️"),
        BAR_DISCOTECA("Bar / Discoteca", "🎵"),
        ENTRETENIMIENTO("Entretenimiento", "🎭"),
        HOTEL("Hotel / Hospedaje", "🏨"),
        PLAYA("Playa", "🏖️"),
        MUSEO("Museo / Cultura", "🎨"),
        COMERCIO("Comercio / Tienda", "🛍️"),
        SPA_BIENESTAR("Spa / Bienestar", "💆"),
        AVENTURA("Aventura / Deportes", "🧗"),
        GASTRONOMIA("Gastronomía Local", "🥘"),
        TRANSPORTE("Transporte / Tours", "🚌");

        private final String displayName;
        private final String emoji;

        Category(String displayName, String emoji) {
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

    public Place() {
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String id, name, description, cityId, cityName, countryName;
        private Category category;
        private String address, phone, website, email;
        private String bookingUrl, bookingLabel;
        private Double latitude, longitude;
        private String mainImage;
        private List<String> images;
        private double averageRating;
        private int totalReviews;
        private boolean featured, active;
        private String openingHours, priceRange;
        private List<String> tags;
        private String ownerId, ownerName;
        private LocalDateTime createdAt, updatedAt;

        public Builder id(String v) {
            this.id = v;
            return this;
        }

        public Builder name(String v) {
            this.name = v;
            return this;
        }

        public Builder description(String v) {
            this.description = v;
            return this;
        }

        public Builder category(Category v) {
            this.category = v;
            return this;
        }

        public Builder cityId(String v) {
            this.cityId = v;
            return this;
        }

        public Builder cityName(String v) {
            this.cityName = v;
            return this;
        }

        public Builder countryName(String v) {
            this.countryName = v;
            return this;
        }

        public Builder address(String v) {
            this.address = v;
            return this;
        }

        public Builder phone(String v) {
            this.phone = v;
            return this;
        }

        public Builder website(String v) {
            this.website = v;
            return this;
        }

        public Builder email(String v) {
            this.email = v;
            return this;
        }

        public Builder bookingUrl(String v) {
            this.bookingUrl = v;
            return this;
        }

        public Builder bookingLabel(String v) {
            this.bookingLabel = v;
            return this;
        }

        public Builder latitude(Double v) {
            this.latitude = v;
            return this;
        }

        public Builder longitude(Double v) {
            this.longitude = v;
            return this;
        }

        public Builder mainImage(String v) {
            this.mainImage = v;
            return this;
        }

        public Builder images(List<String> v) {
            this.images = v;
            return this;
        }

        public Builder averageRating(double v) {
            this.averageRating = v;
            return this;
        }

        public Builder totalReviews(int v) {
            this.totalReviews = v;
            return this;
        }

        public Builder featured(boolean v) {
            this.featured = v;
            return this;
        }

        public Builder active(boolean v) {
            this.active = v;
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

        public Builder tags(List<String> v) {
            this.tags = v;
            return this;
        }

        public Builder ownerId(String v) {
            this.ownerId = v;
            return this;
        }

        public Builder ownerName(String v) {
            this.ownerName = v;
            return this;
        }

        public Builder createdAt(LocalDateTime v) {
            this.createdAt = v;
            return this;
        }

        public Builder updatedAt(LocalDateTime v) {
            this.updatedAt = v;
            return this;
        }

        public Place build() {
            Place p = new Place();
            p.id = id;
            p.name = name;
            p.description = description;
            p.category = category;
            p.cityId = cityId;
            p.cityName = cityName;
            p.countryName = countryName;
            p.address = address;
            p.phone = phone;
            p.website = website;
            p.email = email;
            p.bookingUrl = bookingUrl;
            p.bookingLabel = bookingLabel;
            p.latitude = latitude;
            p.longitude = longitude;
            p.mainImage = mainImage;
            p.images = images;
            p.averageRating = averageRating;
            p.totalReviews = totalReviews;
            p.featured = featured;
            p.active = active;
            p.openingHours = openingHours;
            p.priceRange = priceRange;
            p.tags = tags;
            p.ownerId = ownerId;
            p.ownerName = ownerName;
            p.createdAt = createdAt;
            p.updatedAt = updatedAt;
            return p;
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String v) {
        this.id = v;
    }

    public String getName() {
        return name;
    }

    public void setName(String v) {
        this.name = v;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String v) {
        this.description = v;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category v) {
        this.category = v;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String v) {
        this.cityId = v;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String v) {
        this.cityName = v;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String v) {
        this.countryName = v;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String v) {
        this.address = v;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String v) {
        this.phone = v;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String v) {
        this.website = v;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String v) {
        this.email = v;
    }

    public String getBookingUrl() {
        return bookingUrl;
    }

    public void setBookingUrl(String v) {
        this.bookingUrl = v;
    }

    public String getBookingLabel() {
        return bookingLabel;
    }

    public void setBookingLabel(String v) {
        this.bookingLabel = v;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double v) {
        this.latitude = v;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double v) {
        this.longitude = v;
    }

    public String getMainImage() {
        return mainImage;
    }

    public void setMainImage(String v) {
        this.mainImage = v;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> v) {
        this.images = v;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double v) {
        this.averageRating = v;
    }

    public int getTotalReviews() {
        return totalReviews;
    }

    public void setTotalReviews(int v) {
        this.totalReviews = v;
    }

    public boolean isFeatured() {
        return featured;
    }

    public void setFeatured(boolean v) {
        this.featured = v;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean v) {
        this.active = v;
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

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> v) {
        this.tags = v;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String v) {
        this.ownerId = v;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String v) {
        this.ownerName = v;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime v) {
        this.createdAt = v;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime v) {
        this.updatedAt = v;
    }
}
