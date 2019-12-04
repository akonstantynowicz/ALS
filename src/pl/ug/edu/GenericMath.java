public class GenericMath<T extends  Numeric<T>> {

    public T add(T first, T second){
        return first.add(second);
    }
    public T subtract(T first, T second){
        return first.subtract(second);
    }
    public T multiply(T first, T second){
        return first.multiply(second);
    }
    public T divide(T first, T second){
        return first.divide(second);
    }
    public T abs(T num){
        return num.abs();
    }
    public boolean isGreaterThan(T first, T second) {
        return first.isGreaterThan(second);
    }
}
