package pl.ug.edu.data;

import java.util.ArrayList;
import java.util.List;

public class Customer {
    private int id;
    private String amazonId;
    private List<Review> customerReviews = new ArrayList<>();

    public Customer() {

    }

    public Customer(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAmazonId() {
        return amazonId;
    }

    public void setAmazonId(String amazonId) {
        this.amazonId = amazonId;
    }

    public List<Review> getCustomerReviews() {
        return customerReviews;
    }

    public void setCustomerReviews(List<Review> customerReviews) {
        this.customerReviews = customerReviews;
    }

    public List<Integer> getReviewedProductIds() {
        List<Integer> reviewedProductIds = new ArrayList<>();
        for (Review r:customerReviews) {
            if (!reviewedProductIds.contains(r.getProduct().getId())) {
                reviewedProductIds.add(r.getProduct().getId());
            }
        }
        return reviewedProductIds;
    }

    @Override
    public String toString() {
        //DataUtil.printList(customerReviews);
        return "Customer{" +
                "id=" + id +
                ", amazonId='" + amazonId + '\'' +
                ", amountOfReviews='" + getCustomerReviews().size() + '\'' +
                '}';
    }
}
