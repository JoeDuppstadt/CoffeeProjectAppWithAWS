package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

public abstract class Subject {
    private ArrayList<Observer> observers = new ArrayList<Observer>();
    private ObservableList<Drink> drinkOrderList = FXCollections.observableArrayList();

    public ObservableList<Drink> getNewDrinks(){
        ObservableList<Drink> ret = FXCollections.observableArrayList();
        for(Drink d: drinkOrderList){
            ret.add(drinkOrderList.get(drinkOrderList.indexOf(d)));
        }
        drinkOrderList.removeAll(drinkOrderList);
        return ret;
    }

    public void addNewDrinks(ObservableList<Drink> drinks){
        drinkOrderList.removeAll(drinkOrderList);
        drinkOrderList.addAll(drinks);
        notifyAllSubscribers();
    }

    public void subscribe(Observer o){
        observers.add(o);
    }

    public void notifyAllSubscribers(){
        for(Observer o: observers){
            o.update();
        }
    }

}
