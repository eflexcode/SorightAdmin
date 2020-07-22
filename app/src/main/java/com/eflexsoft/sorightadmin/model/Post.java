package com.eflexsoft.sorightadmin.model;

public class Post {

    private String text;
    private String imageUri;
    private String catergoryName;

    public Post() {
    }

    public Post(String text, String imageUri,String catergoryName) {
        this.text = text;
        this.imageUri = imageUri;
        this.catergoryName = catergoryName;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getCatergoryName() {
        return catergoryName;
    }

    public void setCatergoryName(String catergoryName) {
        this.catergoryName = catergoryName;
    }
}
