package sample;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class ThreadedSingletonSubject extends Subject implements Runnable{
    private static ThreadedSingletonSubject onlyInstance = null;
    private String url = "https://sqs.us-east-2.amazonaws.com/271028247314/TestQueue";
    private ObservableList<Drink> drinkOrderList;
    boolean running = false;

    private ThreadedSingletonSubject(){
        drinkOrderList = FXCollections.observableArrayList();
        running = true;
    }


    public static ThreadedSingletonSubject getInstance(){
        if (onlyInstance == null){
            onlyInstance = new ThreadedSingletonSubject();
        }
        return onlyInstance;
    }

    public void setRunningTrue(boolean b){
        running = b;
    }

    public void run(){
        while(running) {
            drinkOrderList.removeAll(drinkOrderList);
            List<String> orderlist = new ArrayList<>();
            try {
                AmazonSQS sqs = AmazonSQSClientBuilder.standard()
                        .withRegion(Regions.US_WEST_2)
                        .build();
                boolean flag = true;

                while (flag) {
                    ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(url);
                    List<Message> messages = sqs.receiveMessage(receiveMessageRequest).getMessages();

                    for (Message message : messages) {
                        orderlist.add(message.getBody());
                    }
                    if (messages.size() == 0) {
                        flag = false;
                    }
                }
            } catch (AmazonServiceException ase) {
                ase.printStackTrace();
            } catch (AmazonClientException ace) {
                ace.printStackTrace();
            } finally {
                for (Object x : orderlist) {
                    // disassembles the object and grabs each individual part of the order
                    String line = x.toString(); // grabs the first line of the order (first drink)
                    String[] lineArray, orderArray;

                    if (line.contains("\n")) { // checks to see if there is more than one drink per order
                        orderArray = line.split("\n"); // split the order up into individual drinks
                        for (String j : orderArray) {
                            lineArray = j.split(" ");
                            buildDrink(lineArray);
                        }
                    } else { // only one drink in an order
                        lineArray = line.split(" ");
                        buildDrink(lineArray);
                    }
                }
                if(drinkOrderList.size() > 0) {
                    super.addNewDrinks(drinkOrderList);
                }
            }

            //sleep thread for 60 seconds
            try {
                Thread.sleep(240000000);
                System.out.println("Thread looped");
            } catch (InterruptedException e) {
                running = false;
            }
        }
    }


    public void buildDrink(String[] drinkArray){
        int length = drinkArray.length;
        String message = (String) drinkArray[length -1]; // store message
        String time = (String) drinkArray[length - 2];  //extract time
        String price = (String) drinkArray[length - 3]; // extract price
        String flavor = (String) drinkArray[length - 4]; // extract flavor
        String sugar = (String) drinkArray[length - 5]; // extract sugar
        String expresso = (String) drinkArray[length - 6]; // extract espresso
        String milkChoice = (String) drinkArray[length - 7]; // extract milk

        length -= 8;
        String drink = "";

        for (int i = 0; i <= length; i++) {
            String input = drinkArray[i];
            drink += input + " ";
        }
        Drink displayDrink = new Drink(drink, milkChoice, expresso, sugar, flavor, price, time, message);
        drinkOrderList.add(displayDrink);
    }

}
