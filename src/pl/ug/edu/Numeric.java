public interface Numeric<T> {
    T add(T other);
    T subtract(T other);
    T multiply(T other);
    T divide(T other);
    T abs();
    Boolean isGreaterThan(T other);
}
