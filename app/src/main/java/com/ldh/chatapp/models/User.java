package com.ldh.chatapp.models;

import java.io.Serializable;

public class User implements Serializable {
    public String name, image, email, token, id;

    public User(String name, String image, String email, String token, String id) {
        this.name = name;
        this.image = image;
        this.email = email;
        this.token = token;
        this.id = id;
    }
}
