package pl.ug.edu.als;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import pl.ug.edu.data.DataUtil;
import pl.ug.edu.data.Review;
import pl.ug.edu.gauss.Gauss;
import pl.ug.edu.gauss.Matrix;
import pl.ug.edu.generic.Double;

public class ALS {

  private int productsAmount;

  private Map<String, Integer> userList;

  private List<List<Integer>> userRatingsList;

  private int d;

  private double lambda;

  private Matrix p;

  private Matrix u;

  public ALS(int d, double lambda, int productsAmount) {
    this.d = d;
    this.lambda = lambda;
    this.productsAmount = productsAmount;
    userList = new TreeMap<>();
    userRatingsList = new ArrayList<>();
  }

  public void addReview(Review review) {
    addUserIfNotInList(review.getUserId());
    userRatingsList.get(userList.get(review.getUserId()))
        .set(review.getProductId(), review.getRating());
  }

  private void addUserIfNotInList(String userId) {
    if (userList.containsKey(userId)) {
      System.out.println("User już istnieje");
    } else {
      addUserToList(userId);
    }
  }

  private void addUserToList(String userId) {
    System.out.println("Dodawanie nowego usera do listy");
    userList.put(userId, userRatingsList.size());
    userRatingsList.add(new ArrayList<>(Collections.nCopies(productsAmount, 0)));
  }

  public void alsAlg() {
    for (List<Integer> userRatings : userRatingsList) {
      ArrayList<Integer> ratedProductIds = DataUtil.getRatedProductsIds(userRatings);
      Matrix PIU = new Matrix(d, ratedProductIds.size());

      System.out.println("amount of rated products " + ratedProductIds.size());

      for (int m = 0; m < d; m++) {
        int i = 0;
        for (int id : ratedProductIds) {
          PIU.matrix[m][i] = p.matrix[m][id];
          i++;
        }
      }
      Matrix PIUT = PIU.transpose();
      Matrix E = new Matrix(d, d);
      E.generateUnitMatrix();
      E.multiply(Double.valueOf(lambda));
      Matrix AU = PIU.multiply(PIUT).add(E);
      AU.calculateVector(userRatings, ratedProductIds, p);
      Gauss gauss = new Gauss(AU.M, AU.N);
      u.swapWithSolution(gauss.PG(AU.matrix, AU.vector), userRatingsList.indexOf(userRatings));
    }
    u.print();
    for (int i=0;i<productsAmount;i++){
      ArrayList<Integer> ratingUsersIds = DataUtil.getRatingUserIds(userRatingsList,i);
      ArrayList<Integer> productRatings = DataUtil.getProductRatings(userRatingsList,i);
      Matrix UIP = new Matrix(d, ratingUsersIds.size());
      for (int m = 0; m < d; m++) {
        int j = 0;
        for (int id: ratingUsersIds) {
          UIP.matrix[m][j] = u.matrix[m][id];
          j++;
        }
      }

      Matrix UIPT = UIP.transpose();

      Matrix E = new Matrix(d, d);
      E.generateUnitMatrix();
      E.multiply(Double.valueOf(lambda));

      Matrix BU = UIP.multiply(UIPT).add(E);
      BU.calculateVector(productRatings, ratingUsersIds,u,i);
      Gauss gauss = new Gauss(BU.M,BU.N);

      p.swapWithSolution(gauss.PG(BU.matrix,BU.vector),i);
    }
    p.print();
  }

  public void generatePMatrix() {
    System.out.println("Generuje P");
    p = new Matrix(d, productsAmount);
    p.generateRandomMatrix();
    p.print();
  }

  public void generateUMatrix() {
    System.out.println("Generuje U");
    u = new Matrix(d, userList.size());
    u.generateRandomMatrix();
    u.print();
  }

}
