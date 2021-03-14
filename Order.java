package sample;

import java.util.ArrayList;

public class Order {
    ArrayList<Drink> drinks;
    String messageHistory;

    public void addDrink(Drink newDrink){
        if(drinks ==null){
            drinks = new ArrayList<Drink>();
        }
        drinks.add(newDrink);
        drinks.trimToSize();
    }
    public void setMessageHistory(String msg){
        messageHistory = messageHistory + "\n" + "-----------------------------" + "\n" + msg;
    }
    public  String getMessageHistory(){
        return messageHistory;
    }
    // message, space, drink(s) space seperated
    public String toString(){
        String ret = messageHistory +  " ";
        for(Drink d: drinks){
            ret = ret + " " + d.toString();
        }
        return ret;
    }
}
