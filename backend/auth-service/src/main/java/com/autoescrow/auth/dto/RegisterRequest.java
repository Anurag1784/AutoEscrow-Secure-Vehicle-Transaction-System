package com.autoescrow.auth.dto;

public class RegisterRequest {

    private String fullName;
    private String email;
    private String password;
    private String phone;

    // ✅ ROLE FIELD ADDED
    private String role;

    // ---------- Getters & Setters ----------

    public String getFullName() {
        return fullName;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }

    // ✅ ROLE GETTER / SETTER
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
}
