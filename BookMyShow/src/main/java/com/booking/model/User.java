package com.booking.model;

import com.booking.enums.UserRole;

public class User {
    private final String id;
    private final String name;
    private final String email;
    private final String phone;
    private final UserRole role;

    public User(String id, String name, String email, String phone, UserRole role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.role = role;
    }

    public boolean isAdmin() { return role == UserRole.ADMIN; }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public UserRole getRole() { return role; }
}
