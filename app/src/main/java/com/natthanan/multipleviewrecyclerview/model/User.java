package com.natthanan.multipleviewrecyclerview.model;

import com.google.gson.annotations.Expose;

/**
 * Created by natthanan on 10/17/2017.
 */

public class User {
    @Expose
    String name;
    @Expose
    String blog;
    @Expose
    String company;

    public String getName() {
        return name;
    }

    public String getBlog() {
        return blog;
    }

    public String getCompany() {
        return company;
    }
}
