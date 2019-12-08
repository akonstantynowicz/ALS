package pl.ug.edu.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Matrix {

  //klucz mapy to id usera, a wartosc to pozycja jaka ma w liscie ratingow
  public Map<String, Integer> userList = new TreeMap<>();

  public List<List<Integer>> userRatingsList = new ArrayList<>();
}
