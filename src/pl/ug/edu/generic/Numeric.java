package pl.ug.edu.generic;

public interface Numeric<T> {

    T add(T other);

    T subtract(T other);

    T multiply(T other);

    T divide(T other);

    T abs();

    T getZero();

    Boolean isGreaterThan(T other);
}
