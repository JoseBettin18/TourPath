package com.tourpath.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;
import java.time.LocalDateTime;
import java.util.Set;

@Document(collection = "users")
public class User {

    @Id
    private String id;

    @Indexed(unique = true)
    private String username;

    @Indexed(unique = true)
    private String email;

    private String password;
    private String fullName;
    private String phone;
    private Set<Role> roles;
    private boolean enabled;
    private boolean accountNonLocked;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // ID del establecimiento si el usuario es OWNER
    private String ownedPlaceId;

    public enum Role {
        ROLE_USER,
        ROLE_OWNER,
        ROLE_SUPER_ADMIN
    }

    public User() {
    }

    // ── Builder ───────────────────────────────────────────────────
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String id;
        private String username;
        private String email;
        private String password;
        private String fullName;
        private String phone;
        private Set<Role> roles;
        private boolean enabled;
        private boolean accountNonLocked;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private String ownedPlaceId;

        public Builder id(String v) {
            this.id = v;
            return this;
        }

        public Builder username(String v) {
            this.username = v;
            return this;
        }

        public Builder email(String v) {
            this.email = v;
            return this;
        }

        public Builder password(String v) {
            this.password = v;
            return this;
        }

        public Builder fullName(String v) {
            this.fullName = v;
            return this;
        }

        public Builder phone(String v) {
            this.phone = v;
            return this;
        }

        public Builder roles(Set<Role> v) {
            this.roles = v;
            return this;
        }

        public Builder enabled(boolean v) {
            this.enabled = v;
            return this;
        }

        public Builder accountNonLocked(boolean v) {
            this.accountNonLocked = v;
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

        public Builder ownedPlaceId(String v) {
            this.ownedPlaceId = v;
            return this;
        }

        public User build() {
            User u = new User();
            u.id = id;
            u.username = username;
            u.email = email;
            u.password = password;
            u.fullName = fullName;
            u.phone = phone;
            u.roles = roles;
            u.enabled = enabled;
            u.accountNonLocked = accountNonLocked;
            u.createdAt = createdAt;
            u.updatedAt = updatedAt;
            u.ownedPlaceId = ownedPlaceId;
            return u;
        }
    }

    // ── Getters & Setters ─────────────────────────────────────────
    public String getId() {
        return id;
    }

    public void setId(String v) {
        this.id = v;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String v) {
        this.username = v;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String v) {
        this.email = v;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String v) {
        this.password = v;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String v) {
        this.fullName = v;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String v) {
        this.phone = v;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> v) {
        this.roles = v;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean v) {
        this.enabled = v;
    }

    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    public void setAccountNonLocked(boolean v) {
        this.accountNonLocked = v;
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

    public String getOwnedPlaceId() {
        return ownedPlaceId;
    }

    public void setOwnedPlaceId(String v) {
        this.ownedPlaceId = v;
    }
}
