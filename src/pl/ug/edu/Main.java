package pl.ug.edu;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import pl.ug.edu.data.Matrix;
import pl.ug.edu.data.Parser;
import pl.ug.edu.data.Review;

public class Main {

    public static void main(String[] args){
//        Gauss gauss = new Gauss();
//        Macierz m = new Macierz(5);
//        m.drukuj();
//        System.out.println(gauss.PG(m.macierz, m.wektor));
//        m.drukuj();

      Matrix matrix = new Matrix();
        Parser p = new Parser();

        try {
            List<Review> list = p.readData("sample.txt");

            for (Review r : list) {
                System.out.println(r);
              if (!matrix.userList.containsKey(r.getUserId())) {
                System.out.println("Dodawanie nowego usera do listy");
                matrix.userList.put(r.getUserId(), matrix.userRatingsList.size());
                matrix.userRatingsList.add(new ArrayList<>(Collections.nCopies(548552, 0)));
              } else {
                System.out.println("User już istnieje");
              }
              if (matrix.userList.containsKey(r.getUserId())) {
                matrix.userRatingsList.get(matrix.userList.get(r.getUserId()))
                    .set(r.getProductId(), r.getRating());
              }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
      System.out.println("user 0");
      for (int i = 0; i < 5; i++) {
        System.out.println(matrix.userRatingsList.get(0).get(i));
      }
      System.out.println("user 1");
      for (int i = 0; i < 5; i++) {
        System.out.println(matrix.userRatingsList.get(1).get(i));
      }


    }
}
