package Servlets.model;

import java.sql.Date;

public class Sale {
    private int saleId;
    private int productId;
    private int quantity;
    private double revenue;
    private Date saleDate;

    public Sale() {}

    public Sale(int saleId, int productId, int quantity, double revenue, Date saleDate) {
        this.saleId = saleId;
        this.productId = productId;
        this.quantity = quantity;
        this.revenue = revenue;
        this.saleDate = saleDate;
    }

    // Getters and Setters
    public int getSaleId() { return saleId; }
    public void setSaleId(int saleId) { this.saleId = saleId; }

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getRevenue() { return revenue; }
    public void setRevenue(double revenue) { this.revenue = revenue; }

    public Date getSaleDate() { return saleDate; }
    public void setSaleDate(Date saleDate) { this.saleDate = saleDate; }
}
