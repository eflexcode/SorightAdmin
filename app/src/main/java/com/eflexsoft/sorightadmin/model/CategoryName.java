package com.eflexsoft.sorightadmin.model;

import java.util.List;

public class CategoryName {

    String name;
    List<Post> posts;

    public CategoryName(String name, List<Post> posts) {
        this.name = name;
        this.posts = posts;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }
}
