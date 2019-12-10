package pl.ug.edu.als;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.sun.javafx.tk.quantum.MasterTimer;
import pl.ug.edu.data.DataUtil;
import pl.ug.edu.data.Review;
import pl.ug.edu.gauss.Matrix;

public class ALS {

    private int productsAmount = 0;

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
        if (!userList.containsKey(review.getUserId())) {
            System.out.println("Dodawanie nowego usera do listy");
            userList.put(review.getUserId(), userRatingsList.size());
            userRatingsList.add(new ArrayList<>(Collections.nCopies(productsAmount, 0)));
        } else {
            System.out.println("User już istnieje");
        }
        if (userList.containsKey(review.getUserId())) {
            userRatingsList.get(userList.get(review.getUserId()))
                    .set(review.getProductId(), review.getRating());
        }
    }

    public void alg() {
        for (List<Integer> userRatings : userRatingsList) {
            ArrayList<Integer> ratedProductIds = DataUtil.getRatedProductsIds(userRatings);
            Matrix PIU = new Matrix(d, ratedProductIds.size());

            System.out.println("amount of rated products " + ratedProductIds.size());;

            for (int m = 0; m < d; m++) {
                int i = 0;
                for (int id: ratedProductIds) {
                    PIU.matrix[m][i] = p.matrix[m][id];
                    i++;
                }
            }
            PIU.transpose();
            PIU.print();

            Matrix E = new Matrix(d, d);
            E.generateUnitMatrix();
        }
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
