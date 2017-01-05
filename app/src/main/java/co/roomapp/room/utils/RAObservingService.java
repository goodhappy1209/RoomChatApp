package co.roomapp.room.utils;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by drottemberg on 9/16/14.
 */
public class RAObservingService {
    HashMap<String, RAObservable> observables;
    private final static RAObservingService instance = new RAObservingService();


    public static RAObservingService getInstance(){
        return instance;
    }

    public RAObservingService() {
        observables = new HashMap<String, RAObservable>();
    }

    public void addObserver(String notification, Observer observer) {
        RAObservable observable = observables.get(notification);
        if (observable==null) {
            observable = new RAObservable();
            observables.put(notification, observable);
        }
        observable.addObserver(observer);
    }

    public void removeObserver(String notification, Observer observer) {
        Observable observable = observables.get(notification);
        if (observable!=null) {
            observable.deleteObserver(observer);
        }
    }

    public void postNotification(String notification, Object object) {
        HashMap<String, Object> dict = new HashMap<String, Object>();
        dict.put(RAConstant.kRANotification,notification);
        dict.put(RAConstant.kRAObject,object);
        RAObservable observable = observables.get(notification);
        if (observable!=null) {
            observable.setChanged();
            observable.notifyObservers(dict);
        }
    }
}