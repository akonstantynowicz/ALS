
//  Copyright (C) 2019 Anna Konstantynowicz, Marcin Szczepaniak, Jakub Ściślewski
//  All rights reserved

package pl.ug.edu.als;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import pl.ug.edu.data.DataUtil;
import pl.ug.edu.data.Key;
import pl.ug.edu.data.Parser;
import pl.ug.edu.data.Review;
import pl.ug.edu.gauss.Gauss;
import pl.ug.edu.gauss.Matrix;

public class ALS {

  private static final int NUMBER_OF_ATTEMPTS = 30;

  private static final double MILISECONDS_IN_SECOND = 100000;

  private static final int NUMBER_OF_ITERATIONS = 100;

  private final List<List<Integer>> userRatingsList;

  private final Map<String, Integer> userList;

  private final Map<Integer, Integer> productsList;

  private final List<String> productNames;

  private int d;

  private double lambda;

  private Map<Key,Double> testValues;

  private int productsAmount;

  private int reviewQuantity;

  private Matrix p;

  private Matrix u;

  private Matrix resultMatrix;


  public ALS(final int d, final double lambda) {
    this.d = d;
    this.lambda = lambda;
    userList = new TreeMap<>();
    productsList = new TreeMap<>();
    userRatingsList = new ArrayList<>();
    productNames = new ArrayList<>();
  }

  public ALS(final double lambda) {
    productsList = new TreeMap<>();
    this.lambda = lambda;
    userList = new TreeMap<>();
    userRatingsList = new ArrayList<>();
    productNames = new ArrayList<>();
    testValues = new HashMap<>();
  }

  public ALS(final int d) {
    productsList = new TreeMap<>();
    this.d = d;
    userList = new TreeMap<>();
    userRatingsList = new ArrayList<>();
    productNames = new ArrayList<>();
    testValues = new HashMap<>();
  }
  private static void calculateColumnValues(final int i, final Matrix XU, final Matrix x) {
    final Gauss gauss = new Gauss(XU.M, XU.N);
    x.swapWithSolution(gauss.PG(XU.matrix, XU.vector), i);
  }


  /**
   * Generates list of names corresponding to product id's Name is set as 'None' if there is no
   * product reviews for certain  id
   */
  private void generateProductNamesList(List<Review> reviewList) {
    int currentId = 0;
    for (Review review : reviewList) {
      if (review.getProduct().getProductId() > currentId) {
        productNames.add("None");
        currentId++;
      } else if (!productNames.contains(review.getProduct().getProductName())) {
        productNames.add(review.getProduct().getProductName());
        currentId++;
      }
    }

  }

  private void setProductsAmount(int productsAmount) {
    this.productsAmount = productsAmount;
  }

  private void addUserRating(final Review review) {
    addUserIfNotInList(review.getUserId());
    userRatingsList.get(userList.get(review.getUserId()))
            .set(review.getProduct().getProductId(), review.getRating());
  }

  public void setD(int d) {
    this.d = d;
  }

  public void setLambda(double lambda) {
    this.lambda = lambda;
  }

  public void algorithm() {
    long startTime, endTime;
    double currentGoalFunction;
    double previousGoalFunction;
    double goalFunctionDifference = 0.0;
    double sumGoalFunction = 0.0;
    double timeSum = 0.0;
    int neededIterations = 0;
    Matrix resultMatrix;
            testValues = DataUtil.getTestData(userRatingsList, reviewQuantity);

    //System.out.println("Result Matrix:");
    //System.out.println("error: " + DataUtil.checkTestData(resultMatrix, testValues));
    for (int attempt = 0; attempt < NUMBER_OF_ATTEMPTS; attempt++) {
      generateRandomPMatrix();
      generateRandomUMatrix();
      currentGoalFunction = 0.0;
      for (int i = 0; i < NUMBER_OF_ITERATIONS; i++) {
        startTime = System.currentTimeMillis();
        calculatePAndUMatrixes();
        resultMatrix = generateResultMatrix();
        endTime = System.currentTimeMillis();
        previousGoalFunction = currentGoalFunction;
        currentGoalFunction = 0.0;
        for (int n = 0; n < userList.size(); n++) {
          for (int m = 0; m < productsAmount; m++) {
            if (userRatingsList.get(n).get(m) != 0.0) {
              currentGoalFunction += Math.pow(
                      userRatingsList.get(n).get(m) - resultMatrix.matrix[n][m],
                      2);
            }
          }
        }
        double reg = 0.0;
        for (int n = 0; n < d; n++) {
          for (int m = 0; m < userList.size(); m++) {
            reg += Math.pow(u.matrix[n][m], 2);
          }
        }
        for (int n = 0; n < d; n++) {
          for (int m = 0; m < productsAmount; m++) {
            reg += Math.pow(p.matrix[n][m], 2);
          }
          if (goalFunctionDifference < 0.01) {
            neededIterations = i;
            sumGoalFunction += currentGoalFunction;
            timeSum += (endTime - startTime);
            break;
          }
        }
        reg = lambda * reg;
        currentGoalFunction += reg;
        goalFunctionDifference = Math.abs(currentGoalFunction - previousGoalFunction);
      }
    }
    System.out.println("\nd = " + d);
    System.out.println("lambda = " + lambda);
    System.out.println("Srednia ilosc iteracji do osiagniecia roznicy < 0.01: "
            + neededIterations / NUMBER_OF_ATTEMPTS);
    System.out.println("Srednia funkcja celu: " + sumGoalFunction / NUMBER_OF_ATTEMPTS);
    System.out
            .println("Sredni czas: " + (timeSum / NUMBER_OF_ATTEMPTS) / MILISECONDS_IN_SECOND + "s ");
  }

  private void calculatePAndUMatrixes() {
    calculateP();
    calculateU();
  }

  private Matrix generateResultMatrix() {
    Matrix uTransposed = u.transpose();
    return uTransposed.multiply(p);
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
    E.multiply(lambda);

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

  /**
   * This method runs all other methods in algorithm one by one.
   * @throws IOException On input error.
   * @see IOException
   */
  public void runAlsAlgorithm() throws IOException {
    prepareInitialData();
    algorithm();
  }

  public void prepareInitialData() throws IOException {
    List<Review> reviewList = Parser.parseFile("sample2.txt");
    reviewQuantity = reviewList.size();
    //System.out.println(DataUtil.getHighestProductId(reviewList));
    setProductsAmount(DataUtil.getHighestProductId(reviewList) + 1);
    generateUserRatingsFromReviewList(reviewList);
    generateProductNamesList(reviewList);
    System.out.println("Data ready");
  }

  private void generateUserRatingsFromReviewList(List<Review> reviewList) {
    for (Review review : reviewList) {
      //System.out.println(review);
      addUserRating(review);
    }
  }

  private void generateRandomPMatrix() {
    //System.out.println("Generuje P");
    p = new Matrix(d, productsAmount);
    p.generateRandomMatrix();
    // p.print();
  }

  private void generateRandomUMatrix() {
    //System.out.println("Generuje U");
    u = new Matrix(d, userList.size());
    u.generateRandomMatrix();
    //u.print();
  }

  private void addUserIfNotInList(final String userId) {
    if (!userList.containsKey(userId)) {
      addUserToList(userId);
    }
  }

  private void addUserToList(final String userId) {
    userList.put(userId, userRatingsList.size());
    userRatingsList.add(new ArrayList<>(Collections.nCopies(productsAmount, 0)));
  }
}
