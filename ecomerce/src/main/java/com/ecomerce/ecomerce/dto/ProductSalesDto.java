package com.ecomerce.ecomerce.dto;


public class ProductSalesDto {
    
    private Long productId;
    private String productName;
    private Long totalItemsSold;
    private double totalSold;

    // Constructor
    public ProductSalesDto(Long productId, String productName, Long totalItemsSold, double totalSold) {
        this.productId = productId;
        this.productName = productName;
        this.totalItemsSold = totalItemsSold;
        this.totalSold = totalSold;
    }

    // Getters and Setters
    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Long getTotalItemsSold() {
        return totalItemsSold;
    }

    public void setTotalItemsSold(Long totalItemsSold) {
        this.totalItemsSold = totalItemsSold;
    }

    public double getTotalSold() {
        return totalSold;
    }

    public void setTotalSold(double totalSold) {
        this.totalSold = totalSold;
    }
}
