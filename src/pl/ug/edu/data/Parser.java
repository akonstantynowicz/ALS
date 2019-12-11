
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

public final class Parser {

  private static final Pattern idProductLinePattern = Pattern.compile("^Id:.*$");

  private static final Pattern reviewLinePattern = Pattern.compile("cutomer:.*rating:\\s*[0-9]");

  private static final Pattern categoryLinePattern = Pattern.compile("group:.*");

  private static final Pattern numberPattern = Pattern.compile("\\s\\d+");

  private static final Pattern categoryPattern = Pattern.compile("\\s([A-Z]|[a-z])+$");

  private static final Pattern userIdPattern = Pattern.compile("([A-Z]|[0-9])+");

  private Parser() {
    throw new IllegalStateException("Utility Class");
  }

  private static String extractPatternValue(String line, Pattern p) {

    Matcher matcher = p.matcher(line);
    if (matcher.find()) {
      return (matcher.group());
    }
    return null;
  }

  public static List<Review> parseFile(String path) throws IOException {

    List<Review> reviewList = new ArrayList<>();

    try (FileInputStream inputStream = new FileInputStream(path);
        Scanner scanner = new Scanner(inputStream, String.valueOf(StandardCharsets.UTF_8))) {
      generateReviewList(reviewList, scanner);
    }
    return reviewList;
  }

  private static void generateReviewList(List<Review> reviewList, Scanner scanner) {
    Review review = new Review();

    String productIdLine;
    String categoryLine;
    String reviewLine;
    String productId;
    String category;
    String userId;
    String rating;

    int currentProductId = -1;
    String currentCategory = null;

    while (scanner.hasNextLine()) {

      String line = scanner.nextLine();

      if ((productIdLine = extractPatternValue(line, idProductLinePattern)) != null
          && (productId = extractPatternValue(productIdLine, numberPattern)) != null) {
        currentProductId = Integer.parseInt(productId.trim());
        review.setProductId(currentProductId);
      } else if ((categoryLine = extractPatternValue(line, categoryLinePattern)) != null
          && (category = extractPatternValue(categoryLine, categoryPattern)) != null) {
        currentCategory = category.trim();
        review.setCategory(currentCategory);
      } else if ((reviewLine = extractPatternValue(line, reviewLinePattern)) != null) {
        if ((userId = extractPatternValue(reviewLine, userIdPattern)) != null) {
          review.setUserId(userId);
        }
        if ((rating = extractPatternValue(reviewLine, numberPattern)) != null) {
          review.setRating(Integer.parseInt(rating.trim()));
          reviewList.add(review);
          review = new Review();
          review.setProductId(currentProductId);
          review.setCategory(currentCategory);
        }
      }
    }
  }
}