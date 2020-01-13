package pl.ug.edu.data;

import java.util.Comparator;
import java.util.List;

import static pl.ug.edu.data.Parser.getAmountOfReviewsInList;

public class CustomersComparator implements Comparator<Customer> {

    private List<Product> topProducts;

    public CustomersComparator(List<Product> products) {
        topProducts = products;
    }

    @Override
    public int compare(Customer o1, Customer o2) {
        return (getAmountOfReviewsInList(o2, topProducts) - getAmountOfReviewsInList(o1, topProducts));
    }
}
