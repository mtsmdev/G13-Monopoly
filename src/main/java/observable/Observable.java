package observable;

import java.util.ArrayList;

public abstract class Observable<T> {
    protected ArrayList<Observer<T>> observers = new ArrayList<>();

    public void registerObserver(Observer<T> observer) {
        observers.add(observer);
    }

    public void deregisterOberser(Observer<T> observer) {
        observers.remove(observer);
    }

    public void updateObservers(T data) {
        for (Observer<T> observer: observers) {
            observer.update(data);
        }
    }
}
