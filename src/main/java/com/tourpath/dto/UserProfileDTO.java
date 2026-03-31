package com.tourpath.dto;

import jakarta.validation.constraints.*;

public class UserProfileDTO {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 3, max = 100)
    private String fullName;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "Correo inválido")
    private String email;

    private String phone;

    @Size(min = 6, max = 50, message = "Mínimo 6 caracteres")
    private String newPassword;

    private String confirmNewPassword;
    private String currentPassword;

    public String getFullName()                  { return fullName; }
    public void   setFullName(String v)          { this.fullName = v; }
    public String getEmail()                     { return email; }
    public void   setEmail(String v)             { this.email = v; }
    public String getPhone()                     { return phone; }
    public void   setPhone(String v)             { this.phone = v; }
    public String getNewPassword()               { return newPassword; }
    public void   setNewPassword(String v)       { this.newPassword = v; }
    public String getConfirmNewPassword()        { return confirmNewPassword; }
    public void   setConfirmNewPassword(String v){ this.confirmNewPassword = v; }
    public String getCurrentPassword()           { return currentPassword; }
    public void   setCurrentPassword(String v)   { this.currentPassword = v; }
}
