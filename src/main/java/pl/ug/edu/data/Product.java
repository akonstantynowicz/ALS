//  Copyright (C) 2019 Anna Konstantynowicz, Marcin Szczepaniak, Jakub Ściślewski
//  All rights reserved

package pl.ug.edu.data;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static java.util.Comparator.comparingInt;

public class Product implements Comparable<Product> {

    private int id;
    private String name;
    private String category;
    private List<Review> productReviews = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<Review> getProductReviews() {
        return productReviews;
    }

    public void setProductReviews(List<Review> productReviews) {
        this.productReviews = productReviews;
    }

    @Override
    public String toString() {
        //DataUtil.printList(productReviews);
        return "Product{" +
                "productId=" + id +
                ", productName='" + name + '\'' +
                ", productCategory='" + category + '\'' +
                ", amountOfReviews='" + productReviews.size() + '\'' +
                '}';
    }

    @Override
    public int compareTo(Product o) {
        return (o.getProductReviews().size() - this.getProductReviews().size());
    }
}
