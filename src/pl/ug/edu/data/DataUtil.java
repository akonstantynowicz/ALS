package pl.ug.edu.data;

import java.util.List;

public class DataUtil {
    public static int getHighestProductId(List<Review> list) {
        int highestId = 0;
        for (Review r : list) {
            if (r.getProductId() > highestId) {
                highestId = r.getProductId();
            }
        }
        return highestId + 1;
    }
}
