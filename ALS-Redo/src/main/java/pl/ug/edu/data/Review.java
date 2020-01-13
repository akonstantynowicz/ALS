
//  Copyright (C) 2019 Anna Konstantynowicz, Marcin Szczepaniak, Jakub Ściślewski
//  All rights reserved

package pl.ug.edu.data;

/**
 * Represents single user review
 */
public class Review {

    private Product product;
    private Customer customer;
    private int rating;

    Review() {}

    public Review(Product product, Customer customer, int rating) {
        this.product = product;
        this.customer = customer;
        this.rating = rating;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "Review{" +
                "product=" + product.getId() +
                ", customer=" + customer.getId() +
                ", rating=" + rating +
                '}';
    }
}
