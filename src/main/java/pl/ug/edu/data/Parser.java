
//  Copyright (C) 2019 Anna Konstantynowicz, Marcin Szczepaniak, Jakub Ściślewski
//  All rights reserved

package pl.ug.edu.data;

import com.google.common.base.Splitter;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class used to parse data
 */
public class Parser {

  private static final Pattern idProductLinePattern = Pattern.compile("^Id:.*$");

  private static final Pattern titleLinePattern = Pattern.compile("title:.*");

  private static final Pattern reviewLinePattern = Pattern.compile("cutomer:.*rating:\\s*[0-9]");

  private static final Pattern categoryLinePattern = Pattern.compile("group:.*");

  private static final Pattern numberPattern = Pattern.compile("\\s\\d+");

  private static final Pattern wordPattern = Pattern.compile("\\s.*");

  private static final Pattern userIdPattern = Pattern.compile("([A-Z]|[0-9])+");

  private List<String> customerIds = new ArrayList<>();
  ;

  private List<Customer> allCustomers = new ArrayList<>();
  private List<Product> allProducts = new ArrayList<>();
  private List<Customer> topCustomers = new ArrayList<>();
  private List<Product> topProducts = new ArrayList<>();
  private List<Review> reviewList = new ArrayList<>();

  /**
   * Extracts pattern value from given line.
   *
   * @param line    String line from which pattern is extracted.
   * @param pattern Pattern which is extracted from line.
   * @return Input subsequence matched by the previous match result or null if none found.
   */
  private static String extractPatternValue(String line, Pattern pattern) {
    Matcher matcher = pattern.matcher(line);
    if (matcher.find()) {
      return (matcher.group());
    }
    return null;
  }

  public static int getAmountOfReviewsInList(Customer c, List<Product> products) {
    int counter = 0;
    for (Product p : products) {
      for (Review r : c.getCustomerReviews()) {
        if (r.getProduct().getId() == p.getId()) {
          counter++;
        }
      }
    }
    return counter;
  }

  public List<Review> getReviewList() {
    return reviewList;
  }

  public void setReviewList(List<Review> reviewList) {
    this.reviewList = reviewList;
  }

  /**
   * Parses file
   *
   * @param path Path to file.
   * @return List containing all reviews from file.
   * @throws IOException On input error.
   */
  public void parseFile(String path, int n) throws IOException {
    try  {
      BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
      parse(bufferedReader);
      setReviewList(getPreparedData(n));
    }
    catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void parse(BufferedReader bufferedReader) throws IOException {

    Product currentProduct = null;
    Customer currentCustomer = new Customer();
    Review currentReview = new Review();

    String currentLine;

    String productId;
    String productName;
    String productCategory;

    String customerId;

    String rating;

    int lineCounter = 0;
    String line = " ";
    line = bufferedReader.readLine();
    while (line != null) {
      lineCounter++;
      System.out.println("line: " + lineCounter);

      if ((currentLine = extractPatternValue(line, idProductLinePattern)) != null
          && (productId = extractPatternValue(currentLine, numberPattern)) != null) {
        if (currentProduct != null) {
          allProducts.add(currentProduct);
        }
        currentProduct = new Product();
        currentProduct.setId(Integer.parseInt(productId.trim()));
      } else if ((currentLine = extractPatternValue(line, titleLinePattern)) != null
          && (productName = extractPatternValue(currentLine, wordPattern)) != null) {
        currentProduct.setName(productName.trim());
      } else if ((currentLine = extractPatternValue(line, categoryLinePattern)) != null
          && (productCategory = extractPatternValue(currentLine, wordPattern)) != null) {
        currentProduct.setCategory(productCategory.trim());
      } else if ((currentLine = extractPatternValue(line, reviewLinePattern)) != null) {
        if ((customerId = extractPatternValue(currentLine, userIdPattern)) != null) {
          currentCustomer = getCustomerById(getReadableCustomerId(customerId));
          currentCustomer.setAmazonId(customerId);
        }
        if ((rating = extractPatternValue(currentLine, numberPattern)) != null) {
          currentReview = new Review(currentProduct, currentCustomer,
              Integer.parseInt(rating.trim()));
          setReview(currentCustomer, currentProduct, currentReview);
        }
      }
      line = bufferedReader.readLine();
    }
    if (currentProduct != null) {
      allProducts.add(currentProduct);
    }
    bufferedReader.close();
  }

  private List<Review> getPreparedData(int n) {
    getTopProducts(n);
    getTopReviewingCustomers(n);
    cleanCustomerReviews();
    cleanProductReviews();

//        System.out.println("\nAll products:\n");
//        DataUtil.printList(allProducts);
//        System.out.println("\nAll customers:\n");
//        DataUtil.printList(allCustomers);
    System.out.println("\nTop products:\n");
    DataUtil.printList(topProducts);
    System.out.println("\nTop customers:\n");
    DataUtil.printList(topCustomers);

    List<Review> topReviewsList = new ArrayList<>();
    for (Customer c : topCustomers) {
      for (Review r : c.getCustomerReviews()) {
        if (!topReviewsList.contains(r)) {
          topReviewsList.add(r);
        }
      }
    }
    return topReviewsList;
  }

  private void getTopProducts(int n) {
    Collections.sort(allProducts);

    for (int i = 0; i < n; i++) {
      if (i < allProducts.size()) {
        topProducts.add(allProducts.get(i));
      } else {
        return;
      }
    }
  }

  public void getTopReviewingCustomers(int n) {
    allCustomers.sort(new CustomersComparator(topProducts));
    for (int i = 0; i < n; i++) {
      topCustomers.add(allCustomers.get(i));
    }
  }

  private int getReadableCustomerId(String id) {
    if (customerIds.contains(id)) {
      return customerIds.indexOf(id);
    } else {
      customerIds.add(id);
      return customerIds.indexOf(id);
    }
  }

  private Customer getCustomerById(int id) {
    for (Customer c : allCustomers) {
      if (c.getId() == id) {
        return c;
      }
    }
    Customer newCustomer = new Customer(id);
    allCustomers.add(newCustomer);
    return newCustomer;
  }

  private void cleanCustomerReviews() {
    for (Customer c : topCustomers) {
      c.setCustomerReviews(getCustomerReviewsOfProducts(c, topProducts));
    }
  }

  private void cleanProductReviews() {
    for (Product p : topProducts) {
      p.setProductReviews(getProductReviewedByCustomers(p, topCustomers));
    }
  }

  private List<Review> getCustomerReviewsOfProducts(Customer c, List<Product> products) {
    List<Review> reviewed = new ArrayList<>();
    for (Review r : c.getCustomerReviews()) {
      for (Product p : products) {
        if (p.getProductReviews().contains(r) && !reviewed.contains(r)) {
          reviewed.add(r);
        }
      }
    }
    return reviewed;
  }

  private List<Review> getProductReviewedByCustomers(Product p, List<Customer> customers) {
    List<Review> reviewed = new ArrayList<>();
    for (Review r : p.getProductReviews()) {
      for (Customer c : customers) {
        if (c.getCustomerReviews().contains(r) && !reviewed.contains(r)) {
          reviewed.add(r);
        }
      }
    }
    return reviewed;
  }

  private void setReview(Customer c, Product p, Review r) {
    int index = -1;
    for (Review rr : c.getCustomerReviews()) {
      if (rr.getProduct().getId() == r.getProduct().getId()) {
        index = c.getCustomerReviews().indexOf(rr);
      }
    }
    if (index >= 0) {
      c.getCustomerReviews().remove(index);
    }

    index = -1;
    for (Review rr : p.getProductReviews()) {
      if (rr.getCustomer().getId() == r.getCustomer().getId()) {
        index = p.getProductReviews().indexOf(rr);
      }
    }
    if (index >= 0) {
      p.getProductReviews().remove(index);
    }
    c.getCustomerReviews().add(r);
    p.getProductReviews().add(r);
  }

}
