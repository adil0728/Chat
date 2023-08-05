package com.example.chat;

public class User {
    public String name;
    public String email;
    public String id;
    public int avatarMockUpResource;

    public User() {
    }

    public User(String name, String email, String id, int avatarMockUpResource) {
        this.name = name;
        this.email = email;
        this.id = id;
        this.avatarMockUpResource = avatarMockUpResource;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getAvatarMockUpResource() {
        return avatarMockUpResource;
    }

    public void setAvatarMockUpResource(int avatarMockUpResource) {
        this.avatarMockUpResource = avatarMockUpResource;
    }
}
