package com.projects.taskservice.entity;

/**
 * @author ubaid khanzada
 * @since 1.0
 */
public class LoginResponse {
    private String token;

    public LoginResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
