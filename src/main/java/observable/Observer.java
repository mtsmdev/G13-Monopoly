package observable;

public interface Observer<T> {
    void update(T data);
}