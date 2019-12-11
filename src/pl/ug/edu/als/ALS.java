/*******************************************************************************
 *  Copyright (C) 2019 Anna Konstantynowicz, Marcin Szczepaniak, Jakub Ściślewski
 *  All rights reserved
 ******************************************************************************/

package pl.ug.edu.als;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import pl.ug.edu.data.DataUtil;
import pl.ug.edu.data.Parser;
import pl.ug.edu.data.Review;
import pl.ug.edu.gauss.Gauss;
import pl.ug.edu.gauss.Matrix;
import pl.ug.edu.generic.Double;

public class ALS {

  private final Map<String, Integer> userList;

  private final List<List<Integer>> userRatingsList;

  private final int d;

  private final double lambda;

  private int productsAmount;

  private Matrix p;

  private Matrix u;

  public ALS(final int d, final double lambda) {
    this.d = d;
    this.lambda = lambda;
    userList = new TreeMap<>();
    userRatingsList = new ArrayList<>();
  }

  /**
   * This method runs all other methods one by one.
   * @throws IOException On input error.
   * @see IOException
   */
  public void runAlsAlgorithm() throws IOException {
    Parser parser = new Parser();
    List<Review> reviewList = parser.readData("sample.txt");
    System.out.println(DataUtil.getHighestProductId(reviewList));
    setProductsAmount(DataUtil.getHighestProductId(reviewList) + 1);

    for (Review review : reviewList) {
      System.out.println(review);
      addReview(review);
    }
    generateRandomPMatrix();
    generateRandomUMatrix();
    alg();
  }

  private static void calculateColumnValues(final int i, final Matrix XU, final Matrix x) {
    final Gauss<Double> gauss = new Gauss<>(XU.M, XU.N);
    x.swapWithSolution(gauss.PG(XU.matrix, XU.vector), i);
  }

  private void setProductsAmount(int productsAmount) {
    this.productsAmount = productsAmount;
  }

  private void addReview(final Review review) {
    addUserIfNotInList(review.getUserId());
    userRatingsList.get(userList.get(review.getUserId()))
        .set(review.getProductId(), review.getRating());
  }

  private void addUserIfNotInList(final String userId) {
    if (userList.containsKey(userId)) {
      System.out.println("User już istnieje");
    } else {
      addUserToList(userId);
    }
  }

  private void addUserToList(final String userId) {
    System.out.println("Dodawanie nowego usera do listy");
    userList.put(userId, userRatingsList.size());
    userRatingsList.add(new ArrayList<>(Collections.nCopies(productsAmount, 0)));
  }

  private void alg() {
    for (int k = 0; k < 100; k++) {
      calculateP();
      System.out.println("U");
      u.print();
      System.out.println("P");
      calculateU();
      p.print();
    }
      Matrix U_TRANS = u.transpose();
      Matrix R = U_TRANS.multiply(p);
      System.out.println("R");
      R.print();
  }

  private void calculateP() {
    for (int i = 0; i < productsAmount; i++) {
      final List<Integer> ratingUsersIds = DataUtil.getRatingUserIds(userRatingsList, i);
      final List<Integer> productRatings = DataUtil
          .getProductRatings(userRatingsList, i);

      final Matrix BU = calculateXU(ratingUsersIds, u);
      BU.calculateVector(productRatings, ratingUsersIds, u, i);

      calculateColumnValues(i, BU, p);
    }
  }

  private void calculateU() {
    int userIndex = 0;
    for (final List<Integer> userRatings : userRatingsList) {
      final ArrayList<Integer> ratedProductIds = (ArrayList<Integer>) DataUtil
          .getRatedProductsIds(userRatings);

      Matrix AU = calculateXU(ratedProductIds, p);
      AU.calculateVector(userRatings, ratedProductIds, p);

      calculateColumnValues(userIndex, AU, u);
      userIndex++;
    }
  }

  private Matrix calculateXU(List<Integer> ratedProductIds, final Matrix x) {
    final Matrix XIx = new Matrix(d, ratedProductIds.size());
    fillWithValues(ratedProductIds, XIx, x);
    final Matrix XIxT = XIx.transpose();

    final Matrix E = new Matrix(d, d);
    E.generateUnitMatrix();
    E.multiply(Double.valueOf(lambda));

    return XIx.multiply(XIxT).add(E);
  }

  private void fillWithValues(List<Integer> idsList, Matrix X, Matrix x) {
    for (int m = 0; m < d; m++) {
      int i = 0;
      for (int id : idsList) {
        X.matrix[m][i] = x.matrix[m][id];
        i++;
      }
    }
  }

  private void generateRandomPMatrix() {
    System.out.println("Generuje P");
    p = new Matrix(d, productsAmount);
    p.generateRandomMatrix();
    p.print();
  }

  private void generateRandomUMatrix() {
    System.out.println("Generuje U");
    u = new Matrix(d, userList.size());
    u.generateRandomMatrix();
    u.print();
  }

}
