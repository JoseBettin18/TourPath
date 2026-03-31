package com.tourpath.dto;

import jakarta.validation.constraints.*;

public class RegisterDTO {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 3, max = 100)
    private String fullName;

    @NotBlank(message = "El usuario es obligatorio")
    @Size(min = 3, max = 30)
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Solo letras, números y guiones bajos")
    private String username;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "Correo inválido")
    private String email;

    private String phone;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, max = 50)
    private String password;

    @NotBlank(message = "Confirma tu contraseña")
    private String confirmPassword;

    public String getFullName()               { return fullName; }
    public void   setFullName(String v)       { this.fullName = v; }
    public String getUsername()               { return username; }
    public void   setUsername(String v)       { this.username = v; }
    public String getEmail()                  { return email; }
    public void   setEmail(String v)          { this.email = v; }
    public String getPhone()                  { return phone; }
    public void   setPhone(String v)          { this.phone = v; }
    public String getPassword()               { return password; }
    public void   setPassword(String v)       { this.password = v; }
    public String getConfirmPassword()        { return confirmPassword; }
    public void   setConfirmPassword(String v){ this.confirmPassword = v; }
}
