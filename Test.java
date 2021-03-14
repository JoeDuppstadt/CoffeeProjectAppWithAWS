package sample;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        String var1 = "coffee";
        String var2 = "tea";
        String var3 = "Y";
        String var4 = "N";
        Drink var5 = new Drink(var1, var3, var4, var3, var4, "seventy dollars", "I ain't picking this up anyway", "hi");
        Drink var6 = new Drink(var2, var4, var3, var4, var3, "tree fidy", "sometime in the future", "bye");
        System.out.println("Should be coffee Y N Y N seventy dollars I ain't picking this up anyway");
        System.out.println(var5.toString());
        System.out.println("Should be Tea N Y N Y tree fidy sometime in the future");
        System.out.println(var6.toString());
        var5.setPrice("70.00");
        var5.setTime("3:15");
        System.out.println("Should be coffee Y N Y N 70.00 3:15");
        System.out.println(var5.toString());

        System.out.println(var5.getDrink());
        System.out.println(var5.getExpresso());
        System.out.println(var5.getMessage());
        System.out.println(var5.getFlavor());
        System.out.print((var5.getMilk()));
        System.out.println(var5.getSugar());
        System.out.println(var5.getTime());

        AmazonSQS sqs = AmazonSQSClientBuilder.standard()
                .withRegion(Regions.US_WEST_2)
                .build();

        SendMessageRequest send_msg_request = new SendMessageRequest()
                .withQueueUrl("https://sqs.us-east-2.amazonaws.com/271028247314/TestQueue")
                .withMessageBody(var6.toString());

        SendMessageResult result = sqs.sendMessage(send_msg_request);
        System.out.println(result.getMessageId()); // used to check to see if the order was placed

        List<String> orderlist = new ArrayList<>();

        try {
            boolean flag = true;

            while(flag){
                ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest("https://sqs.us-east-2.amazonaws.com/271028247314/TestQueue");
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
            System.out.println("Message was sent and recieved");
        }

        
    }
}
