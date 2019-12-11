
//  Copyright (C) 2019 Anna Konstantynowicz, Marcin Szczepaniak, Jakub Ściślewski
//  All rights reserved

package pl.ug.edu.data;

import java.util.ArrayList;
import java.util.List;

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
      if (review.getProductId() > highestId) {
        highestId = review.getProductId();
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
}
