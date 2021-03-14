package sample;

public class Drink {
    private String drink, milk, expresso, sugar, flavor, price, hotCoffee, coldCoffee, hotDrink, coldDrink, time, message;

    // user for ordering and displaying the order in the worker UI
    public Drink(String d, String m, String e, String s, String f, String p, String t, String mess){
        this.drink = d;
        this.milk = m;
        this.expresso = e;
        this.sugar = s;
        this.flavor = f;
        this.price = p;
        this.time = t;
        this.message = mess;
    }



    // used for populating the customer menu
    public Drink(String hc, String cc, String hd, String cd){
        hotCoffee = hc;
        coldCoffee = cc;
        hotDrink = hd;
        coldDrink = cd;
    }

    @Override
    public String toString() {
        return drink + " " +  milk + " " +expresso + " " + sugar + " " +flavor + " " +price + " " + time + " " + message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


    public String getHotCoffee() {
        return hotCoffee;
    }

    public void setHotCoffee(String hotCoffee) {
        this.hotCoffee = hotCoffee;
    }


    public String getColdCoffee() {
        return coldCoffee;
    }

    public void setColdCoffee(String coldCoffee) {
        this.coldCoffee = coldCoffee;
    }


    public String getHotDrink() {
        return hotDrink;
    }

    public void setHotDrink(String hotDrink) {
        this.hotDrink = hotDrink;
    }


    public String getColdDrink() {
        return coldDrink;
    }

    public void setColdDrink(String coldDrink) {
        this.coldDrink = coldDrink;
    }



    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }


    public String getDrink() {
        return drink;
    }

    public void setDrink(String drink) {
        this.drink = drink;
    }


    public String getMilk() {
        return milk;
    }

    public void setMilk(String milk) {
        this.milk = milk;
    }


    public String getExpresso() {
        return expresso;
    }

    public void setExpresso(String expresso) {
        this.expresso = expresso;
    }


    public String getSugar() {
        return sugar;
    }

    public void setSugar(String sugar) {
        this.sugar = sugar;
    }


    public String getFlavor() {
        return flavor;
    }

    public void setFlavor(String flavor) {
        this.flavor = flavor;
    }

    public void setMessage(String msg){
        //replace all spaces in msg with '-'
        for(int i =0; i < msg.length(); i++){
            if(msg.charAt(i) == ' '){
                msg = msg.substring(0,i) + '-' +msg.substring(i+1);
            }
        }
        message = Main.messages + "-" + msg;
    }
    public String getMessage(){
        return message;
    }


}
