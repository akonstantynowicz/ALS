
//  Copyright (C) 2019 Anna Konstantynowicz, Marcin Szczepaniak, Jakub Ściślewski
//  All rights reserved

package pl.ug.edu.data;

import java.util.ArrayList;
import java.util.List;

public final class DataUtil {

  private DataUtil() {
    throw new IllegalStateException("Utility Class");
  }

  public static int getHighestProductId(Iterable<Review> reviewList) {
    int highestId = 0;
    for (Review review : reviewList) {
      if (review.getProductId() > highestId) {
        highestId = review.getProductId();
      }
    }
    return highestId;
  }

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
    ArrayList<Integer> ratingUsersIds = new ArrayList<>();
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
