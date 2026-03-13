package observable;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class ObservableTest {

    @Test
    public void ObservableTest_update() {
        ObservableImpl observable = new ObservableImpl();
        ObserverImpl observer = new ObserverImpl();

        observable.registerObserver(observer);
        observable.updateObservers(true);

        Boolean result = observer.updated;
        assertTrue(result);
    }
}

class ObservableImpl extends Observable<Boolean> {}

class ObserverImpl implements Observer<Boolean> {
    public Boolean updated = false;

    @Override
    public void update(Boolean data) {
        this.updated = data;
    }
}
