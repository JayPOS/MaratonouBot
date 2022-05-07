package com.jaypos.maratonouBot.utils;

public class Env {
    private String token;
    private String key;
    private String secret;
    private String testingGuildName;
    private String testingGuildId;
    public Env(){
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getTestingGuildName() {
        return testingGuildName;
    }

    public void setTestingGuildName(String testingGuildName) {
        this.testingGuildName = testingGuildName;
    }

    public String getTestingGuildId() {
        return testingGuildId;
    }

    public void setTestingGuildId(String testingGuildId) {
        this.testingGuildId = testingGuildId;
    }
}
