package org.example;

public class UserEdit {
    private String email;
    private String name;

    public UserEdit(String email, String name) {
        this.email = email;
        this.name = name;
    }

    public UserEdit() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
