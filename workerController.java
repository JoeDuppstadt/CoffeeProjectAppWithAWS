package sample;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.sqs.model.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.regions.Regions;

public class workerController extends Observer{

    @FXML private TableView<Drink> orderTable;

    @FXML private TableColumn<Drink, String> drinkColumn;

    @FXML private TableColumn<Drink, String> milkColumn;

    @FXML private TableColumn<Drink, String> expressoColumn;

    @FXML private TableColumn<Drink, String> sugarColumn;

    @FXML private TableColumn<Drink, String> flavorColumn;

    @FXML private TableColumn<Drink, String> priceColumn;

    @FXML private TableColumn<Drink, String> pickUpColumn;

    @FXML
    private Label messageHistory;

    @FXML
    private TextArea workerResponse;

    private String price, flavor, sugar, expresso, milkChoice, drink = "", time;
    private ObservableList<Drink> drinkOrderList = FXCollections.observableArrayList();
    private String url = "https://sqs.us-east-2.amazonaws.com/AmazonMadeMeRemoveTheID/TestQueue"; // make a new sqs queue to test
    ThreadedSingletonSubject updater;
    Thread t1;

    @FXML
    public void deleteOrderButtonPressed(ActionEvent e) {
        String tableBuilder = "";
        orderTable.getItems().removeAll(orderTable.getSelectionModel().getSelectedItem()); // removes the item from the table
        ObservableList<Drink> workerOrders = FXCollections.observableArrayList();
        workerOrders = orderTable.getItems();
        AmazonSQS sqs = AmazonSQSClientBuilder.standard()
                .withRegion(Regions.US_WEST_2).build();
        // receive messages from the queue
        List<Message> messages = sqs.receiveMessage(url).getMessages();

        // deletes all messages from the queue
        /**
         * we may need to rewrite this because you can only make this call once every 60 seconds
         * Could cause some issues when deleting multiple orders
         */
        PurgeQueueRequest purgeReq = new PurgeQueueRequest(url);
        sqs.purgeQueue(purgeReq);

        // grabs all the orders (except the deleted one) and adds it to a newly created order
        for(Object drink: workerOrders) {
            String stringDrink = drink.toString();
            if(workerOrders.size() > 1) { // if the order contains more than one drink we need to add a new line for another drink
                tableBuilder = tableBuilder + stringDrink + "\n";
            }
            else{ // no need for a new line
                tableBuilder = stringDrink;
            }
        }

        // repopulate the queue
        SendMessageRequest send_msg_request = new SendMessageRequest()
                .withQueueUrl("https://sqs.us-east-2.amazonaws.com/AmazonMadeMeRemoveTheID/TestQueue")
                .withMessageBody(tableBuilder);

        SendMessageResult result = sqs.sendMessage(send_msg_request);
        workerOrders.clear();
        getOrders();

    }

    @FXML
    public void logoutButtonPressed(ActionEvent e) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("FrenchLogin.fxml"));
        Scene workerView = new Scene(root);

        Stage workerWindow = (Stage) ((Node) e.getSource()).getScene().getWindow();
        workerWindow.setTitle("French Press Coffee");
        workerWindow.setScene(workerView);
        workerWindow.show();
        t1.interrupt();
    }

    public void initialize(){
        drinkColumn.setCellValueFactory(new PropertyValueFactory<Drink, String>("drink"));
        milkColumn.setCellValueFactory(new PropertyValueFactory<Drink, String>("milk"));
        expressoColumn.setCellValueFactory(new PropertyValueFactory<Drink, String>("expresso"));
        sugarColumn.setCellValueFactory(new PropertyValueFactory<Drink, String>("sugar"));
        flavorColumn.setCellValueFactory(new PropertyValueFactory<Drink, String>("flavor"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<Drink, String>("price"));
        pickUpColumn.setCellValueFactory(new PropertyValueFactory<Drink, String>("time"));
        getOrders(); // populates the orders in the table
        updater = ThreadedSingletonSubject.getInstance();
        updater.setRunningTrue(true);
        this.subject = updater;
        this.subject.subscribe(this);
        t1 = new Thread(updater);
        t1.start();

    }

    /**
     use this method to implement server stuff
     */
    public void getOrders() {
        List<String> orderlist = new ArrayList<>();

        try {
            AmazonSQS sqs = AmazonSQSClientBuilder.standard()
                    .withRegion(Regions.US_WEST_2)
                    .build();
            boolean flag = true;

            while(flag){
                ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(url);
                List<Message> messages = sqs.receiveMessage(receiveMessageRequest).getMessages();

                for (Message message : messages) {
                    orderlist.add(message.getBody());
                }
                if(messages.size()==0) {
                    flag = false;
                }
            }
        }
        catch (AmazonServiceException ase) {
            ase.printStackTrace();
        } catch (AmazonClientException ace) {
            ace.printStackTrace();
        }
        finally {
            for(Object x: orderlist){
                // disassembles the object and grabs each individual part of the order
                String line = x.toString(); // grabs the first line of the order (first drink)
                String[] lineArray, orderArray;

                if(line.contains("\n")) { // checks to see if there is more than one drink per order
                    orderArray = line.split("\n"); // split the order up into individual drinks
                    for (String j : orderArray) {
                        lineArray = j.split(" ");
                        buildDrink(lineArray);
                    }
                }
                else{ // only one drink in an order
                    lineArray = line.split(" ");
                    buildDrink(lineArray);
                }
            }
        }

    }

    public void buildDrink(String[] drinkArray){
        int length = drinkArray.length;
        String message = (String) drinkArray[length -1]; // store message
        time = (String) drinkArray[length - 2];  //extract time
        price = (String) drinkArray[length - 3]; // extract price
        flavor = (String) drinkArray[length - 4]; // extract flavor
        sugar = (String) drinkArray[length - 5]; // extract sugar
        expresso = (String) drinkArray[length - 6]; // extract espresso
        milkChoice = (String) drinkArray[length - 7]; // extract milk

        length -= 8;
        drink = "";

        for (int i = 0; i <= length; i++) {
            String input = drinkArray[i];
            drink += input + " ";
        }
        Drink displayDrink = new Drink(drink, milkChoice, expresso, sugar, flavor, price, time, message);
        drinkOrderList.add(displayDrink);
        orderTable.setItems(drinkOrderList);
    }

    @FXML
    void workerSendMessage(MouseEvent event) {
        if((orderTable.getSelectionModel().getSelectedItem() != null) && (workerResponse.getText() != null)){
            // modify currently selected
            Drink current = orderTable.getSelectionModel().getSelectedItem();
            messageHistory.setText(current.getMessage() + "\n" + "-------------------------" + "\n" + workerResponse.getText());
            current.setMessage(current.getMessage() + "\n" + "-------------------------" + "\n" + workerResponse.getText());


        }
    }

    @FXML
    public void orderClicked(MouseEvent event) {
        if(orderTable.getSelectionModel().getSelectedItem() != null){
            Drink current = orderTable.getSelectionModel().getSelectedItem();
            String msg = current.getMessage();
            for(int i = 0; i < msg.length(); i++){
                if(msg.charAt(i) == '-'){
                    msg = msg.substring(0,i) + ' ' + msg.substring(i+1);
                }
            }
            messageHistory.setText(msg);
            workerResponse.setText(msg);
        } else {
            messageHistory.setText("");
            workerResponse.setText("");
        }
    }

    public void update(){
        Platform.runLater(new Runnable() {
            @Override public void run() {
                ObservableList<Drink> newDrinks = subject.getNewDrinks();
                for(Drink d: newDrinks){
                    System.out.println(d.toString());
                }
                orderTable.getItems().removeAll(orderTable.getItems());
                orderTable.getItems().addAll(newDrinks);
            }
        });


    }
}
