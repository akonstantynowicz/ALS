
//  Copyright (C) 2019 Anna Konstantynowicz, Marcin Szczepaniak, Jakub Ściślewski
//  All rights reserved

package pl.ug.edu.data;

/**
 * Represents single user review
 */
public class Review {

  private Product product;

  private String userId;

  private int rating;

  private String category;

  Review() {
    this.product = new Product();
  }

  public Product getProduct() {
    return product;
  }

  public void setProduct(Product product) {
    this.product = product;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public int getRating() {
    return rating;
  }

  public void setRating(int rating) {
    this.rating = rating;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }
}
