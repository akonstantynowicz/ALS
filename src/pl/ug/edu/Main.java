package pl.ug.edu;

import java.io.IOException;
import java.util.List;

import pl.ug.edu.als.ALS;
import pl.ug.edu.data.DataUtil;
import pl.ug.edu.data.Parser;
import pl.ug.edu.data.Review;
import pl.ug.edu.gauss.Matrix;

public class Main {

    public static void main(String[] args) {

        Parser parser = new Parser();
        try {
            List<Review> list = parser.readData("sample.txt");
            System.out.println(DataUtil.getHighestProductId(list));
            ALS als = new ALS(3, 0.1, DataUtil.getHighestProductId(list));
            for (Review r : list) {
                System.out.println(r);
                als.addReview(r);
            }

            als.generatePMatrix();
            als.generateUMatrix();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
