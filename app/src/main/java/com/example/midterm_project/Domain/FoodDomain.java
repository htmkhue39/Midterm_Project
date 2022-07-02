package com.example.midterm_project.Domain;

import java.io.Serializable;

public class FoodDomain implements Serializable {
    private String name;
    private String image;
    private String description;
    private Double price;

    public FoodDomain() {

    }

    public FoodDomain(String name, String image, String description, Double price) {
        this.name = name;
        this.image = image;
        this.description = description;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}

