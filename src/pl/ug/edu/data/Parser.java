
//  Copyright (C) 2019 Anna Konstantynowicz, Marcin Szczepaniak, Jakub Ściślewski
//  All rights reserved

package pl.ug.edu.data;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class used to parse data
 */
public final class Parser {

  private static final Pattern idProductLinePattern = Pattern.compile("^Id:.*$");

  private static final Pattern titleLinePattern = Pattern.compile("title:.*");

  private static final Pattern reviewLinePattern = Pattern.compile("cutomer:.*rating:\\s*[0-9]");

  private static final Pattern categoryLinePattern = Pattern.compile("group:.*");

  private static final Pattern numberPattern = Pattern.compile("\\s\\d+");

  private static final Pattern wordPattern = Pattern.compile("\\s.*");

  private static final Pattern userIdPattern = Pattern.compile("([A-Z]|[0-9])+");


  private Parser() {
    throw new IllegalStateException("Utility Class");
  }

  /**
   * Extracts pattern value from given line.
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

  /**
   * Parses file
   * @param path Path to file.
   * @return List containing all reviews from file.
   * @throws IOException On input error.
   */
  public static List<Review> parseFile(String path) throws IOException {
    List<Review> reviewList;
    try (FileInputStream inputStream = new FileInputStream(path);
        Scanner scanner = new Scanner(inputStream, String.valueOf(StandardCharsets.UTF_8))) {
      reviewList = generateReviewList(scanner);
    }
    return reviewList;
  }

  /**
   * Generate list containing reviews from given scanner.
   * @param scanner Scanner containing reviews to parse.
   * @return List of reviews.
   */
  private static List<Review> generateReviewList(Scanner scanner) {
    List<Review> reviewList = new ArrayList<>();
    List<Review> productReviewList = new ArrayList<Review>();
    Review review = new Review();

    String currentLine;

    String productId;
    String title;
    String category;
    String userId;
    String rating;

    int minReviews = 5;
    int currentProductId = -1;
    String currentTitle = null;
    String currentCategory = null;

    while (scanner.hasNextLine()) {

      String line = scanner.nextLine();

      if ((currentLine = extractPatternValue(line, idProductLinePattern)) != null
          && (productId = extractPatternValue(currentLine, numberPattern)) != null) {
        currentProductId = Integer.parseInt(productId.trim());
        review.getProduct().setProductId(currentProductId);

        productReviewList = addFilteredReviews(reviewList, productReviewList, minReviews);
      } else if ((currentLine = extractPatternValue(line, titleLinePattern)) != null
              && (title = extractPatternValue(currentLine, wordPattern)) != null) {
        currentTitle = title.trim();
        review.getProduct().setProductName(currentTitle);
      } else if ((currentLine = extractPatternValue(line, categoryLinePattern)) != null
          && (category = extractPatternValue(currentLine, wordPattern)) != null) {
        currentCategory = category.trim();
        review.getProduct().setProductCategory(currentCategory);
      } else if ((currentLine = extractPatternValue(line, reviewLinePattern)) != null) {
        if ((userId = extractPatternValue(currentLine, userIdPattern)) != null) {
          review.setUserId(userId);
        }
        if ((rating = extractPatternValue(currentLine, numberPattern)) != null) {
          review.setRating(Integer.parseInt(rating.trim()));
          productReviewList.add(review);

          review = new Review();
          review.getProduct().setProductId(currentProductId);
          review.getProduct().setProductName(currentTitle);
          review.getProduct().setProductCategory(currentCategory);
        }
      }
    }
    productReviewList = addFilteredReviews(reviewList, productReviewList, minReviews);
    return reviewList;
  }

  private static List<Review> addFilteredReviews(List<Review> reviewList, List<Review> productReviewList, int minReviews) {
    if (productReviewList.size() > minReviews) {
      System.out.println("adding " + productReviewList.get(0).getProduct().getProductId());
      reviewList.addAll(productReviewList);
      productReviewList = new ArrayList<Review>();
    }
    else if (productReviewList.size() != 0){
      System.out.println("not adding " + productReviewList.get(0).getProduct().getProductId());
      productReviewList = new ArrayList<Review>();
    }
    return productReviewList;
  }
}
