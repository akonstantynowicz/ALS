package pl.ug.edu.als;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import pl.ug.edu.data.Review;

public class ALS {

  private static final int PRODUCTS_AMOUNT = 548552;

  private Map<String, Integer> userList;

  private List<List<Integer>> userRatingsList;

  private int d;

  private double lambda;

  private double[][] p;

  private double[][] u;

  public ALS(int d, double lambda) {
    this.d = d;
    this.lambda = lambda;
    userList = new TreeMap<>();
    userRatingsList = new ArrayList<>();
  }

  public void addReview(Review review) {
    if (!userList.containsKey(review.getUserId())) {
      System.out.println("Dodawanie nowego usera do listy");
      userList.put(review.getUserId(), userRatingsList.size());
      userRatingsList.add(new ArrayList<>(Collections.nCopies(PRODUCTS_AMOUNT, 0)));
    } else {
      System.out.println("User już istnieje");
    }
    if (userList.containsKey(review.getUserId())) {
      userRatingsList.get(userList.get(review.getUserId()))
          .set(review.getProductId(), review.getRating());
    }
  }

  public void generatePMatrix() {
    p = new double[d][PRODUCTS_AMOUNT];
    for (int i = 0; i < d; i++) {
      for (int j = 0; j < PRODUCTS_AMOUNT; j++) {
        p[i][j] = Math.random();
        // System.out.println(p[i][j]);
      }
    }
  }

  public void generateUMatrix() {
    u = new double[d][userList.size()];
    for (int i = 0; i < d; i++) {
      for (int j = 0; j < userList.size(); j++) {
        u[i][j] = Math.random();
        // System.out.println(p[i][j]);
      }
    }
  }
}
