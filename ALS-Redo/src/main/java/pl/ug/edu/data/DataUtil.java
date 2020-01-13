package pl.ug.edu.data;

import java.util.List;

public class DataUtil {

    public static <T> void printList(List<T> list) {
        for (T o:list) {
            System.out.println(o);
        }
    }

}
