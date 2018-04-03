package com.lolski;

public class User {
    private String id;
    private int age;

    public User(String id, int age) {
        this.id = id;
        this.age = age;
    }

    public String getId() {
        return id;
    }

    public int getAge() {
        return age;
    }
}
