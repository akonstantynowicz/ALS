
//  Copyright (C) 2019 Anna Konstantynowicz, Marcin Szczepaniak, Jakub Ściślewski
//  All rights reserved

package pl.ug.edu.data;

import pl.ug.edu.gauss.Matrix;

import java.util.*;

/**
 * Utility class for operations on data.
 */
public final class DataUtil {

  /**
   * Constructor
   */
  private DataUtil() {
    throw new IllegalStateException("Utility Class");
  }

  /**
   * Get highest product id in the list of reviews.
   * @param reviewList List of reviews.
   * @return Highest product id in the list of reviews.
   */
  public static int getHighestProductId(Iterable<Review> reviewList) {
    int highestId = 0;
    for (Review review : reviewList) {
      if (review.getProduct().getProductId() > highestId) {
        highestId = review.getProduct().getProductId();
      }
    }
    return highestId;
  }

  /**
   * Generates list containing id's of products in the userRatings.
   * @param userRatings List of user ratings.
   * @return List of product id's.
   */
  public static List<Integer> getRatedProductsIds(Iterable<Integer> userRatings) {
    ArrayList<Integer> ratedProductIds = new ArrayList<>();
    int productId = 0;
    for (int rating : userRatings) {
      if (rating != 0) {
        ratedProductIds.add(productId);
      }
      productId++;
    }
    return ratedProductIds;
  }

  public static List<Integer> getRatingUserIds(List<List<Integer>> userRatingsList, int p) {
    List<Integer> ratingUsersIds = new ArrayList<>();
    for (List<Integer> userRaiting : userRatingsList) {
      if (userRaiting.get(p) != 0) {
        ratingUsersIds.add(userRatingsList.indexOf(userRaiting));
      }
    }
    return ratingUsersIds;
  }

  public static List<Integer> getProductRatings(List<List<Integer>> userRatingsList, int p) {
    ArrayList<Integer> productRatings = new ArrayList<>();
    for (List<Integer> userRating : userRatingsList) {
      productRatings.add(userRatingsList.get(userRatingsList.indexOf(userRating)).get(p));
    }
    return productRatings;
  }

  public static Map<Key,Double> getTestData(List<List<Integer>> userRatingsList, int reviewQuantity){
    Random generator = new Random();
    Map<Key,Double> testData = new HashMap<>();
    int counter=reviewQuantity/10;
    double testValue;
    while(counter>0){
      int userId = generator.nextInt(userRatingsList.size());
      int productId = generator.nextInt(userRatingsList.get(0).size());
      testValue = userRatingsList.get(userId).get(productId);
      if (testValue>0) {
        userRatingsList.get(userId).set(userRatingsList.get(userId).get(productId), 0);
        testData.put(new Key(userId, productId), testValue);
        counter--;
      }
    }
    return testData;
  }

  public static Double checkTestData (Matrix result, Map<Key,Double> testData){
    double temp,sum = 0;
    for (Map.Entry<Key,Double> value : testData.entrySet()){
      Key key = value.getKey();
      temp = result.matrix[key.getX()][key.getY()];
      /*if(temp>5){
        temp = 5;
      }
      else if(temp<1){
        temp = 1;
      }*/
      System.out.println("Hidden value = " + value.getValue() + " =? Counted value = " + temp);
      sum = sum + Math.abs(temp-value.getValue());
    }
    return sum;
  }
}
