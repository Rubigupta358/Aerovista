 package Servlets.model;

import java.sql.Date;

public class Product {
    private int productId;
    private String name;
    private String category;
    private double price;
    private Date launchDate;  // ✅ java.sql.Date

    // Default constructor
    public Product() {}

    // All-args constructor
    public Product(int productId, String name, String category, double price, Date launchDate) {
        this.productId = productId;
        this.name = name;
        this.category = category;
        this.price = price;
        this.launchDate = launchDate;
    }

    // Getters & Setters
    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public Date getLaunchDate() { return launchDate; }
    public void setLaunchDate(Date launchDate) { this.launchDate = launchDate; }
}
