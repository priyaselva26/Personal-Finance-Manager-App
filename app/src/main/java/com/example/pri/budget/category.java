package com.example.pri.budget;

/**
 * Created by pri on 2/1/2016.
 * Purpose : Store details of the category
 */

public class category {
    private int id;
    private String name;

    public category() {
    }

    public category(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public category(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

