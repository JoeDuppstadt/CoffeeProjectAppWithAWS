package sample;

import com.amazonaws.services.sqs.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Popup;
import javafx.stage.Stage;
import java.sql.Time;
import java.text.DecimalFormat;
import java.time.LocalTime;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;

import java.util.Date;
import software.amazon.awssdk.regions.Region;
import com.amazonaws.regions.Regions;

public class orderController {
    // add menu items here and update the string array below
    Drink row1 = new Drink("Blonde Roast", "Honey Almondmilk Cold Brew", "Toasted White Hot Chocolate", "Star Drink");
    Drink row2 = new Drink("Caffè Misto", "Irish Cream Cold Brew", "Peppermint White Hot Chocolate", "Kiwi Starfruit Starbucks Refreshers® Beverage");
    Drink row3 = new Drink("Pike Place® Roast", "Cold Brew with Cinnamon Almondmilk Foam", "Hot Chocolate", "Kiwi Starfruit Lemonade Starbucks Refreshers® Beverage");
    Drink row4 = new Drink("Cappuccino", "Iced Coffee", "Steamed Apple Juice", "Dragon Drink");
    Drink row5 = new Drink("Pistachio Latte", "Nitro Flat White", "Pistachio Crème", "Mango Dragonfruit Starbucks Refreshers® Beverage");
    Drink row6 = new Drink("Eggnog Latte", "Iced Flat White", "Eggnog Crème", "Mango Dragonfruit Lemonade Starbucks Refreshers® Beverage");
    Drink row7 = new Drink("Honey Oatmilk Latte", "Iced Honey Almondmilk Flat White", "Cinnamon Dolce Crème", "Strawberry Açaí Lemonade Starbucks Refreshers® Beverage");
    Drink row8 = new Drink("Caffè Latte", "Iced Pistachio Latte", "Vanilla Crème", "Very Berry Hibiscus Lemonade Starbucks Refreshers® Beverage");
    Drink row9 = new Drink("Starbucks Reserve® Latte", "Iced Caramel Brulée Latte", "Chestnut Praline Crème", "Pink Drink");

    // populate the comboboxes
    private String[] hotCoffee = {row1.getHotCoffee(), row2.getHotCoffee(), row3.getHotCoffee(), row4.getHotCoffee(), row5.getHotCoffee(), row6.getHotCoffee(), row7.getHotCoffee(), row8.getHotCoffee(), row9.getHotCoffee()};
    private String[] coldCoffee = {row1.getColdCoffee(), row2.getColdCoffee(), row3.getColdCoffee(), row4.getColdCoffee(), row5.getColdCoffee(), row6.getColdCoffee(), row7.getColdCoffee(), row8.getColdCoffee(), row9.getColdCoffee()};
    private String[] hotDrinks = {row1.getHotDrink(), row2.getHotDrink(), row3.getHotDrink(), row4.getHotDrink(), row5.getHotDrink(), row6.getHotDrink(), row7.getHotDrink(), row8.getHotDrink(), row9.getHotDrink()};
    private String[] coldDrinks = {row1.getColdDrink(), row2.getColdDrink(), row3.getColdDrink(), row4.getColdDrink(), row5.getColdDrink(), row6.getColdDrink(), row7.getColdDrink(), row8.getColdDrink(), row9.getColdDrink()};
    private String[] milkChoice = {"Almond", "Whole", "2%", "HeavyCream", "1%", "Oatmilk", "FatFree", "Soy"};
    private String[] flavorChoice = {"Caramel", "Mocha", "Pistachio", "Hazelnut", "Peppermint", "Pineapple", "Raspberry", "Vanilla"};
    private String[] timeChoice = {"15min", "30min", "45min","60min"};
    private String milk;

    ObservableList<String> hotCoffeeList = FXCollections.observableArrayList(hotCoffee);
    ObservableList<String> coldCoffeeList = FXCollections.observableArrayList(coldCoffee);
    ObservableList<String> hotDrinksList = FXCollections.observableArrayList(hotDrinks);
    ObservableList<String> coldDrinksList = FXCollections.observableArrayList(coldDrinks);
    ObservableList<String> milkList = FXCollections.observableArrayList(milkChoice);
    ObservableList<String> flavorList = FXCollections.observableArrayList(flavorChoice);
    ObservableList<String> timeList = FXCollections.observableArrayList(timeChoice);
    private List orderList = new ArrayList<Drink>();




    @FXML
    private ComboBox<String> hotCoffeeComboBox;

    @FXML
    private ComboBox<String> coldCoffeeComboBox;

    @FXML
    private ComboBox<String> hotDrinksComboBox;

    @FXML
    private ComboBox<String> coldDrinksComboBox;

    @FXML
    private CheckBox milkCheckBox;

    @FXML
    private CheckBox expressoCheckBox;

    @FXML
    private CheckBox sugarCheckBox;

    @FXML
    private CheckBox flavorCheckBox;

    @FXML
    private ComboBox<String> milkComboBox;

    @FXML
    private ComboBox<String> flavorComboBox;

    @FXML
    private ComboBox<String> pickupTime;

    @FXML
    private Label messageHistory;

    @FXML
    private TextArea newMessage;

    @FXML
    private Button addMessageButton;

    public void addToOrderButtonPressed(ActionEvent e){
        // this is to make sure only 1 drink is selected
        int count = 0;
        if(!(hotCoffeeComboBox.getValue() == null)){
            count++;
        }
        if(!(coldCoffeeComboBox.getValue() == null)){
            count++;
        }
        if(!(hotDrinksComboBox.getValue() == null)){
            count++;
        }
        if(!(coldDrinksComboBox.getValue() == null)){
            count++;
        }
        if(count > 1){
            throwError("You can only select one drink");
        }
        if(count == 0){
            throwError("You must select a drink");
        }


        // end error checking
        String milkChoice;
        String drink;
        String flavor;
        String expresso;
        String sugar;
        String time = null;
        drink = getDrink();
        boolean expressoAddIn = false; // Expresso - .45
        boolean milkAddIn = false; // Milk - .55
        boolean sugarAddIn = false; // Sugar - .25
        boolean flavorAddIn = false; //  Flavor - .75
        double price = 4.0;

        if(expressoCheckBox.isSelected()){
            expresso = "Y";
            expressoAddIn = true;
        } else{
            expresso = "N";
        }
        if(sugarCheckBox.isSelected()){
            sugar = "Y";
            sugarAddIn = true;
        } else{
            sugar = "N";
        }
        if(!(milkComboBox.getValue() == null)){
            milkChoice = milkComboBox.getValue();
            milkAddIn = true;
        } else{
            milkChoice = "N";
        }
        if(!(flavorComboBox.getValue() == null)){
            flavor = flavorComboBox.getValue();
            flavorAddIn = true;
        } else{
            flavor = "N";
        }
            // if statment to calculate price
        if (expressoAddIn && milkAddIn == true && sugarAddIn == true && flavorAddIn == true) {
            //This is if our customer gets everything.
            price = price + 2.00;
        } else if (expressoAddIn && milkAddIn == true && sugarAddIn == true && flavorAddIn == false) {
            //This is if our customer gets everything but flavor.
            price = price + 1.25;
        } else if (expressoAddIn && milkAddIn == true && sugarAddIn == false && flavorAddIn == true) {
            //This is if our customer gets everything but sugar.
            price = price + 1.75;
        } else if (expressoAddIn && milkAddIn == true && sugarAddIn == false && flavorAddIn == false){
            //This is if our customer gets everything but sugar and flavor
            price = price + 1.00;
        } else if (expressoAddIn && milkAddIn == false && sugarAddIn == true && flavorAddIn == true){
            //This is if our customer gets everything but milk.
            price = price + 1.45;
        } else if (expressoAddIn && milkAddIn == false && sugarAddIn == true && flavorAddIn == false) {
            //This is if our customer gets everything but milk and flavor.
            price = price + 0.70;
        } else if (expressoAddIn && milkAddIn == false && sugarAddIn == false && flavorAddIn == true){
            //This is if our customer gets everything but milk and sugar.
            price = price + 1.20;
        } else if (expressoAddIn && milkAddIn == false && sugarAddIn == false && flavorAddIn == false){
            //This is if our customer only gets expresso.
            price = price + .45;
        } else if (expressoAddIn  && milkAddIn == true && sugarAddIn == true && flavorAddIn == true) {
            //This is if our customer gets everything but expresso.
            price = price + 1.55;
        } else if (expressoAddIn  && milkAddIn == true && sugarAddIn == true && flavorAddIn == false) {
            //This is if our customer gets everything but flavor and expresso.
            price = price + 0.80;
        } else if (expressoAddIn && milkAddIn == true && sugarAddIn == false && flavorAddIn == true) {
            //This is if our customer gets everything but expresso and sugar.
            price = price + 1.30;
        } else if (expressoAddIn && milkAddIn == true && sugarAddIn == false && flavorAddIn == false) {
            //This is if our customer gets only milk.
            price = price + 0.55;
        } else if (expressoAddIn && milkAddIn == false && sugarAddIn == true && flavorAddIn == true) {
            //This is if our customer gets everything but expresso and milk.
            price = price + 1.00;
        } else if (expressoAddIn && milkAddIn == false && sugarAddIn == true && flavorAddIn == false) {
            //This is if our customer gets only sugar.
            price = price + 0.25;
        } else if (expressoAddIn && milkAddIn == false && sugarAddIn == false && flavorAddIn == true) {
            //This is if our customer gets only flavor.
            price = price + 0.75;
        } else {
            //Do nothing, we just have a base drink so we really don't do anything here.
        }

        if(!(pickupTime.getValue() == null)) {
            time = pickupTime.getValue();
        }
        else{
            throwError("Choose a Pickup Time");
        }

       DecimalFormat df2 = new DecimalFormat("#.00");
        String returnPrice = df2.format(price);
        //System.out.println(returnPrice);

        Drink d = new Drink(drink, milkChoice, expresso, sugar, flavor, returnPrice, time, null);
        
        if(drink != null) {
            orderList.add(d);
        }
        resetComboBoxes();
    }

    @FXML
    public void placeOrderButtonPressed(ActionEvent e) throws FileNotFoundException {
        if(orderList.size() == 0){
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Input not valid");
            errorAlert.setContentText("You need to add a drink to your order first");
            errorAlert.showAndWait();
        }
        if(orderList.size() > 0) {
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setContentText("Order Placed");
            a.show();
        }
        placeOrder();
    }

    public void milkCheckBoxChecked(ActionEvent e){
        if(milkCheckBox.isSelected()) {
            milkComboBox.setVisible(true);
        }
        else{
            milkComboBox.setVisible(false);
        }
    }

    public void flavorCheckBoxChecked(ActionEvent e){
        if(flavorCheckBox.isSelected()) {
            flavorComboBox.setVisible(true);
        }
        else{
            flavorComboBox.setVisible(false);
        }
    }

    public void goBackButtonPressed(ActionEvent e) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("menu.fxml"));
        Scene workerView = new Scene(root);
        Stage menuWindow = (Stage) ((Node) e.getSource()).getScene().getWindow();
        menuWindow.setTitle("Menu");
        menuWindow.setScene(workerView);
        menuWindow.show();
    }

    /**
     use this method to implement server stuff
     */
    public void placeOrder(){
        Runnable orderPlacer = new Runnable() {
            @Override
            public void run() {
                AmazonSQS sqs = AmazonSQSClientBuilder.standard()
                        .withRegion(Regions.US_WEST_2)
                        .build();

                String orderBuilder = ""; // add multiple drinks to one string so the server only needs called once instead of once for every drink
                for(Object drink: orderList) {
                    // get current time
                    long curTime = System.currentTimeMillis();
                    String objTime = ((Drink) drink).getTime();
                    // extract delay from string multiply by ms in a  minute
                    long userWait = Integer.parseInt(objTime.substring(0, 2)) * 60000;
                    // set the order time to the current time + delay
                    Time t = new Time(curTime + userWait);
                    ((Drink) drink).setTime(t.toString());
                    ((Drink) drink).setMessage(newMessage.getText());

                    String stringDrink = drink.toString();
                    if(orderList.size() > 1) { // if the order contains more than one drink we need to add a new line for another drink
                        orderBuilder = orderBuilder + stringDrink + "\n";
                    }
                    else{ // no need for a new line
                        orderBuilder = stringDrink;
                    }
                }
                SendMessageRequest send_msg_request = new SendMessageRequest()
                        .withQueueUrl("https://sqs.us-east-2.amazonaws.com/amazonMadeMeTakeTheIDOut/TestQueue") // you can make a simple sqs queue to test this
                        .withMessageBody(orderBuilder);
                //.withDelaySeconds(5)

                SendMessageResult result = sqs.sendMessage(send_msg_request);
                //System.out.println(result.getMessageId()); // used to check to see if the order was placed
                orderList.clear();
            }
        };
        orderPlacer.run();

    }

    public void initialize(){
        hotCoffeeComboBox.setItems(hotCoffeeList);
        coldCoffeeComboBox.setItems(coldCoffeeList);
        hotDrinksComboBox.setItems(hotDrinksList);
        coldDrinksComboBox.setItems(coldDrinksList);
        pickupTime.setItems(timeList);
        milkComboBox.setItems(milkList);
        flavorComboBox.setItems(flavorList);
        milkComboBox.setVisible(false);
        flavorComboBox.setVisible(false);
        pickupTime.getSelectionModel().selectFirst();
        messageHistory.setText(Main.messages);

    }

    public void resetComboBoxes(){
        hotCoffeeComboBox.getSelectionModel().select(-1);
        coldCoffeeComboBox.getSelectionModel().select(-1);
        hotDrinksComboBox.getSelectionModel().select(-1);
        coldDrinksComboBox.getSelectionModel().select(-1);
        hotCoffeeComboBox.setValue(null);
        coldCoffeeComboBox.setValue(null);
        hotDrinksComboBox.setValue(null);
        coldDrinksComboBox.setValue(null);
        milkComboBox.setValue(null);
        flavorComboBox.setValue(null);
        milkComboBox.setVisible(false);
        flavorComboBox.setVisible(false);
        milkCheckBox.setSelected(false);
        sugarCheckBox.setSelected(false);
        flavorCheckBox.setSelected(false);
        expressoCheckBox.setSelected(false);
    }

    public String getDrink(){
        if(!(hotCoffeeComboBox.getValue() == null)){
            return hotCoffeeComboBox.getValue();
        }
        if(!(coldCoffeeComboBox.getValue() == null)){
            return coldCoffeeComboBox.getValue();
        }
        if(!(hotDrinksComboBox.getValue() == null)){
            return hotDrinksComboBox.getValue();
        }
        if(!(coldDrinksComboBox.getValue() == null)){
            return coldDrinksComboBox.getValue();
        }
        return null;
    }

    public void throwError(String s){
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText("Input not valid");
        errorAlert.setContentText(s);
        errorAlert.showAndWait();
        // the comboboxes need to be reset to default
        resetComboBoxes();
    }


}
