package com.eflexsoft.sorightadmin.model;

public class User {

    private String name;
    private String imageUrl;
    private String id;
    private String lastMessage;

    public User(String name, String imageUrl, String id,String lastMessage) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.id = id;
        this.lastMessage = lastMessage;
    }

    public User() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }
}
