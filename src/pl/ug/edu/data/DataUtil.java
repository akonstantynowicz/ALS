package pl.ug.edu.data;

import java.util.ArrayList;
import java.util.List;

public class DataUtil {
    public static int getHighestProductId(List<Review> list) {
        int highestId = 0;
        for (Review r : list) {
            if (r.getProductId() > highestId) {
                highestId = r.getProductId();
            }
        }
        return highestId;
    }

    public static ArrayList<Integer> getRatedProductsIds(List<Integer> userRatings) {
        ArrayList<Integer> ratedProductIds = new ArrayList<Integer>();
        int productId = 0;
        for (int rating : userRatings) {
            if (rating != 0) {
                ratedProductIds.add(productId);
            }
            productId++;
        }
        return ratedProductIds;
    }
}
