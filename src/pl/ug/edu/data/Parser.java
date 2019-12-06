package pl.ug.edu.data;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

    private Matcher m;

    private Pattern idProductLinePattern = Pattern.compile("^Id:.*$");
    private Pattern reviewLinePattern = Pattern.compile("cutomer:.*rating:\\s*[0-9]");
    private Pattern categoryLinePattern = Pattern.compile("group:.*");

    private Pattern numberPattern = Pattern.compile("\\s\\d+");
    private Pattern categoryPattern = Pattern.compile("\\s([A-Z]|[a-z])+$");
    private Pattern userIdPattern = Pattern.compile("([A-Z]|[0-9])+");


    //
    public List<Review> readData(String path) throws IOException {
        FileInputStream inputStream = null;
        Scanner sc = null;

        List<Review> reviewList = new ArrayList<Review>();

        try {

            inputStream = new FileInputStream(path);
            sc = new Scanner(inputStream, "UTF-8");
            Review review = new Review();
            int currentProductId = -1;
            String currentCategory = null;

            while (sc.hasNextLine()) {
                String line = sc.nextLine();

                //productId

                String productIdLine = extractPatternValue(line, idProductLinePattern);
                if (productIdLine != null) {
                    String productId = extractPatternValue(productIdLine, numberPattern);
                    if (productId != null) {
                        currentProductId = Integer.parseInt(productId.trim());
                        review.setProductId(currentProductId);
                    }
                }

                String categoryLine = extractPatternValue(line, categoryLinePattern);
                if (categoryLine != null) {
                    String category = extractPatternValue(categoryLine, categoryPattern);
                    if (category != null) {
                        currentCategory = category.trim();
                        review.setCategory(currentCategory);
                    }
                }

                String reviewLine = extractPatternValue(line, reviewLinePattern);
                if (reviewLine != null) {
                    String userId = extractPatternValue(reviewLine, userIdPattern);
                    if (userId != null) {
                        review.setUserId(userId);
                    }
                    String rating = extractPatternValue(reviewLine, numberPattern);
                    if (rating != null) {
                        review.setRating(Integer.parseInt(rating.trim()));
                        reviewList.add(review);
                        review = new Review();
                        review.setProductId(currentProductId);
                        review.setCategory(currentCategory);
                    }
                }

            }
            if (sc.ioException() != null) {
                throw sc.ioException();
            }
        } finally {

            if (inputStream != null) {
                inputStream.close();
            }
            if (sc != null) {
                sc.close();
            }
        }

        return reviewList;
    }

    private String extractPatternValue(String line, Pattern p) {
        m = p.matcher(line);
        if (m.find()) {
            return (m.group());
        }
        return null;
    }


}
