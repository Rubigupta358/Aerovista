package Servlets.model;

public class Feedback {
    private int feedbackId;
    private int productId;
    private String featureName;
    private int rating;
    private String feedbackText;

    public Feedback() {}

    public Feedback(int feedbackId, int productId, String featureName, int rating, String feedbackText) {
        this.feedbackId = feedbackId;
        this.productId = productId;
        this.featureName = featureName;
        this.rating = rating;
        this.feedbackText = feedbackText;
    }

    // Getters & Setters
    public int getFeedbackId() { return feedbackId; }
    public void setFeedbackId(int feedbackId) { this.feedbackId = feedbackId; }

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public String getFeatureName() { return featureName; }
    public void setFeatureName(String featureName) { this.featureName = featureName; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public String getFeedbackText() { return feedbackText; }
    public void setFeedbackText(String feedbackText) { this.feedbackText = feedbackText; }
}
