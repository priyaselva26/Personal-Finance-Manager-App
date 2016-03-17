package com.example.pri.budget;

/**
 * Created by pri on 3/15/2016.
 */
public class SubCategory {

    private int id;
    private String catName;
    private String subCatName;

    public SubCategory() {
    }

    public SubCategory(int id, String cname, String sname) {
        this.id = id;
        this.catName = cname;
        this.subCatName = sname;
    }

    public SubCategory(String name) {
        this.subCatName = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return subCatName;
    }

    public void setName(String name) {
        this.catName = name;
    }
}
