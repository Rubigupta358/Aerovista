package Servlets.model;

public class Investment {
    private int invId;
    private int productId;
    private String month;
    private String investmentType;
    private double amount;

    public Investment() {}

    public Investment(int invId, int productId, String month, String investmentType, double amount) {
        this.invId = invId;
        this.productId = productId;
        this.month = month;
        this.investmentType = investmentType;
        this.amount = amount;
    }

    // Getters & Setters
    public int getInvId() { return invId; }
    public void setInvId(int invId) { this.invId = invId; }

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public String getMonth() { return month; }
    public void setMonth(String month) { this.month = month; }

    public String getInvestmentType() { return investmentType; }
    public void setInvestmentType(String investmentType) { this.investmentType = investmentType; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
}
