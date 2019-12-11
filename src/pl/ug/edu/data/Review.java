/*******************************************************************************
 *  Copyright (C) 2019 Anna Konstantynowicz, Marcin Szczepaniak, Jakub Ściślewski
 *  All rights reserved
 ******************************************************************************/

package pl.ug.edu.data;

public class Review {

  private String userId;

  private int productId;

  private int rating;

  private String category;

  Review() { }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public int getProductId() {
    return productId;
  }

  public void setProductId(int productId) {
    this.productId = productId;
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

  @Override
  public String toString() {
    return "Review{" +
        "idUser='" + userId + '\'' +
        ", idProduct='" + productId + '\'' +
        ", rating=" + rating +
        ", category='" + category + '\'' +
        '}';
  }
}
