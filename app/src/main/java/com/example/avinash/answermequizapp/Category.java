package com.example.avinash.answermequizapp;

import android.graphics.drawable.Drawable;

/**
 * Created by Avinash Barik on 31/10/2017.
 */

public class Category {
    private String name;
    private Drawable imageName;

    public Category(String name, Drawable imageName) {
        this.name = name;
        this.imageName = imageName;
    }

    public String getName() {
        return name;
    }

    public Drawable getImageName() {
        return imageName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImageName(Drawable imageName) {
        this.imageName = imageName;
    }

    @Override
    public String toString() {
        return "Category{" +
                "name='" + name + '\'' +
                ", imageName=" + imageName +
                '}';
    }
}
