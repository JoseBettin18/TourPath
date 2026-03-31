package com.tourpath.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "cities")
public class City {

    @Id
    private String id;

    @NotBlank(message = "El nombre de la ciudad es obligatorio")
    @Indexed(unique = true)
    private String name;

    @NotBlank(message = "El país es obligatorio")
    private String country;

    private String countryCode; // CO, US, ES, MX...
    private String continent;
    private String description;
    private String mainImage;
    private String flagEmoji;
    private boolean active;
    private boolean featured;
    private int totalPlaces;
    private double latitude;
    private double longitude;
    private List<String> tags;
    private LocalDateTime createdAt;

    public City() {}

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String id, name, country, countryCode, continent;
        private String description, mainImage, flagEmoji;
        private boolean active, featured;
        private int totalPlaces;
        private double latitude, longitude;
        private List<String> tags;
        private LocalDateTime createdAt;

        public Builder id(String v)            { this.id = v; return this; }
        public Builder name(String v)          { this.name = v; return this; }
        public Builder country(String v)       { this.country = v; return this; }
        public Builder countryCode(String v)   { this.countryCode = v; return this; }
        public Builder continent(String v)     { this.continent = v; return this; }
        public Builder description(String v)   { this.description = v; return this; }
        public Builder mainImage(String v)     { this.mainImage = v; return this; }
        public Builder flagEmoji(String v)     { this.flagEmoji = v; return this; }
        public Builder active(boolean v)       { this.active = v; return this; }
        public Builder featured(boolean v)     { this.featured = v; return this; }
        public Builder totalPlaces(int v)      { this.totalPlaces = v; return this; }
        public Builder latitude(double v)      { this.latitude = v; return this; }
        public Builder longitude(double v)     { this.longitude = v; return this; }
        public Builder tags(List<String> v)    { this.tags = v; return this; }
        public Builder createdAt(LocalDateTime v) { this.createdAt = v; return this; }

        public City build() {
            City c = new City();
            c.id = id; c.name = name; c.country = country; c.countryCode = countryCode;
            c.continent = continent; c.description = description; c.mainImage = mainImage;
            c.flagEmoji = flagEmoji; c.active = active; c.featured = featured;
            c.totalPlaces = totalPlaces; c.latitude = latitude; c.longitude = longitude;
            c.tags = tags; c.createdAt = createdAt;
            return c;
        }
    }

    public String getId()                       { return id; }
    public void   setId(String v)               { this.id = v; }
    public String getName()                     { return name; }
    public void   setName(String v)             { this.name = v; }
    public String getCountry()                  { return country; }
    public void   setCountry(String v)          { this.country = v; }
    public String getCountryCode()              { return countryCode; }
    public void   setCountryCode(String v)      { this.countryCode = v; }
    public String getContinent()                { return continent; }
    public void   setContinent(String v)        { this.continent = v; }
    public String getDescription()              { return description; }
    public void   setDescription(String v)      { this.description = v; }
    public String getMainImage()                { return mainImage; }
    public void   setMainImage(String v)        { this.mainImage = v; }
    public String getFlagEmoji()                { return flagEmoji; }
    public void   setFlagEmoji(String v)        { this.flagEmoji = v; }
    public boolean isActive()                   { return active; }
    public void   setActive(boolean v)          { this.active = v; }
    public boolean isFeatured()                 { return featured; }
    public void   setFeatured(boolean v)        { this.featured = v; }
    public int    getTotalPlaces()              { return totalPlaces; }
    public void   setTotalPlaces(int v)         { this.totalPlaces = v; }
    public double getLatitude()                 { return latitude; }
    public void   setLatitude(double v)         { this.latitude = v; }
    public double getLongitude()                { return longitude; }
    public void   setLongitude(double v)        { this.longitude = v; }
    public List<String> getTags()               { return tags; }
    public void   setTags(List<String> v)       { this.tags = v; }
    public LocalDateTime getCreatedAt()         { return createdAt; }
    public void   setCreatedAt(LocalDateTime v) { this.createdAt = v; }
}
