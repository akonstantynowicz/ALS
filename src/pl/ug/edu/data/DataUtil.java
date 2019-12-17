
//  Copyright (C) 2019 Anna Konstantynowicz, Marcin Szczepaniak, Jakub Ściślewski
//  All rights reserved

package pl.ug.edu.data;

import pl.ug.edu.gauss.Matrix;
import pl.ug.edu.generic.Double;

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

  public static Map<Key,pl.ug.edu.generic.Double> getTestData(List<List<Integer>> userRatingsList, Map<Integer, Integer> productsList){
    Random generator = new Random();
    Map<Key,pl.ug.edu.generic.Double> testData = new HashMap<>();
    int counter=5;
    Double testValue;
    while(counter>0){
      int userId = generator.nextInt(userRatingsList.size());
      int productId = generator.nextInt(userRatingsList.get(0).size());
      DataUtil.getRandomProductId(productsList);
      System.out.println("product id: " + productId);
      testValue = Double.valueOf(userRatingsList.get(userId).get(productId));
      if (testValue.isGreaterThan(Double.valueOf(0))) {
        userRatingsList.get(userId).set(userRatingsList.get(userId).get(productId), 0);
        testData.put(new Key(userId, productId), testValue);
        counter--;
      }
    }
    return testData;
  }

  public static int getRandomProductId(Map<Integer, Integer> productsList) {

    for (int i: productsList.keySet()) {
      System.out.println(productsList.get(i));
    }
    return 1;
  }

  public static Double checkTestData (Matrix result, Map<Key,Double> testData){
    Double temp,sum = Double.valueOf(0);
    for (Map.Entry<Key,Double> value : testData.entrySet()){
      Key key = value.getKey();
      temp = result.matrix[key.getX()][key.getY()];
      /*if(temp.isGreaterThan(Double.valueOf(5))){
        temp = Double.valueOf(5);
      }
      else if(Double.valueOf(1).isGreaterThan(temp)){
        temp = Double.valueOf(1);
      }*/

      System.out.println("Hidden value = " + value.getValue() + " =? Counted value = " + temp);
      sum = sum.add((temp.subtract(value.getValue())).abs());
    }
    return sum;
  }
}
