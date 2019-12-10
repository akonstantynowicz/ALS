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

    public void alg() {
        for (int k = 0; k < 1; k++) {
            calculateU();
            System.out.println("U");
            u.print();
            System.out.println("P");
            calculateP();
            p.print();
        }
    }

    private void calculateP() {
        for (int i = 0; i < productsAmount; i++) {
            ArrayList<Integer> ratingUsersIds = DataUtil.getRatingUserIds(userRatingsList, i);
            ArrayList<Integer> productRatings = DataUtil.getProductRatings(userRatingsList, i);

            Matrix BU = calculateXU(ratingUsersIds, u);
            BU.calculateVector(productRatings, ratingUsersIds, u, i);

            calculateColumnValues(i, BU, u);
        }
    }

    private void calculateU() {
        int userIndex = 0;
        for (List<Integer> userRatings : userRatingsList) {
            ArrayList<Integer> ratedProductIds = DataUtil.getRatedProductsIds(userRatings);

            Matrix AU = calculateXU(ratedProductIds, p);
            AU.calculateVector(userRatings, ratedProductIds, p);

            calculateColumnValues(userIndex, AU, u);
            userIndex++;
        }
    }

    private void calculateColumnValues(int i, Matrix XU, Matrix x) {
        Gauss<Double> gauss = new Gauss<>(XU.M, XU.N);
        x.swapWithSolution(gauss.PG(XU.matrix, XU.vector), i);
    }

    private Matrix calculateXU(ArrayList<Integer> ratedProductIds, Matrix x) {
        Matrix XIx = new Matrix(d, ratedProductIds.size());
        fillWithValues(ratedProductIds, XIx, x);
        Matrix XIxT = XIx.transpose();

        Matrix E = new Matrix(d, d);
        E.generateUnitMatrix();
        E.multiply(Double.valueOf(lambda));

        return XIx.multiply(XIxT).add(E);
    }

    private void fillWithValues(ArrayList<Integer> idsList, Matrix X, Matrix x) {
        for (int m = 0; m < d; m++) {
            int i = 0;
            for (int id : idsList) {
                X.matrix[m][i] = x.matrix[m][id];
                i++;
            }
        }
    }

    public void generateRandomPMatrix() {
        System.out.println("Generuje P");
        p = new Matrix(d, productsAmount);
        p.generateRandomMatrix();
        p.print();
    }

    public void generateRandomUMatrix() {
        System.out.println("Generuje U");
        u = new Matrix(d, userList.size());
        u.generateRandomMatrix();
        u.print();
    }

}
