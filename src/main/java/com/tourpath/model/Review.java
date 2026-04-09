package com.tourpath.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Document(collection = "reviews")
public class Review {

    @Id
    private String id;

    @NotBlank(message = "El lugar es obligatorio")
    private String placeId;

    @NotBlank(message = "El usuario es obligatorio")
    private String userId;

    private String username;

    @NotBlank(message = "El comentario es obligatorio")
    @Size(min = 10, max = 500, message = "El comentario debe tener entre 10 y 500 caracteres")
    private String comment;

    @Min(value = 1, message = "Mínimo 1 estrella")
    @Max(value = 5, message = "Máximo 5 estrellas")
    private int rating;

    private boolean active;
    private LocalDateTime createdAt;

    public Review() {
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String id, placeId, userId, username, comment;
        private int rating;
        private boolean active;
        private LocalDateTime createdAt;

        public Builder id(String v) {
            this.id = v;
            return this;
        }

        public Builder placeId(String v) {
            this.placeId = v;
            return this;
        }

        public Builder userId(String v) {
            this.userId = v;
            return this;
        }

        public Builder username(String v) {
            this.username = v;
            return this;
        }

        public Builder comment(String v) {
            this.comment = v;
            return this;
        }

        public Builder rating(int v) {
            this.rating = v;
            return this;
        }

        public Builder active(boolean v) {
            this.active = v;
            return this;
        }

        public Builder createdAt(LocalDateTime v) {
            this.createdAt = v;
            return this;
        }

        public Review build() {
            Review r = new Review();
            r.id = id;
            r.placeId = placeId;
            r.userId = userId;
            r.username = username;
            r.comment = comment;
            r.rating = rating;
            r.active = active;
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

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String v) {
        this.placeId = v;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String v) {
        this.userId = v;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String v) {
        this.username = v;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String v) {
        this.comment = v;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int v) {
        this.rating = v;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean v) {
        this.active = v;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime v) {
        this.createdAt = v;
    }
}
