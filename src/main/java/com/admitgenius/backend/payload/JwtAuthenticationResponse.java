package com.admitgenius.backend.payload;

import com.admitgenius.backend.model.User;
import com.fasterxml.jackson.annotation.JsonProperty;

public class JwtAuthenticationResponse {
    @JsonProperty("token")
    private String accessToken;
    private String tokenType = "Bearer";
    
    @JsonProperty("user")
    private User user;

    public JwtAuthenticationResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    public JwtAuthenticationResponse(String accessToken, User user) {
        this.accessToken = accessToken;
        this.user = user;
    }

    // Getters and Setters
    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
} 