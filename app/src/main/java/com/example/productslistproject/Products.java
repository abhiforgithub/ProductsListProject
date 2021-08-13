package com.example.productslistproject;

public class Products {
    private int id;
    private String proctName,productPrice,productDescription;

    public Products(String proctName, String productPrice, String productDescription) {
        this.proctName = proctName;
        this.productPrice = productPrice;
        this.productDescription = productDescription;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProctName() {
        return proctName;
    }

    public void setProctName(String proctName) {
        this.proctName = proctName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public Products(int id, String proctName, String productPrice, String productDescription) {
        this.id = id;
        this.proctName = proctName;
        this.productPrice = productPrice;
        this.productDescription = productDescription;
    }
}
