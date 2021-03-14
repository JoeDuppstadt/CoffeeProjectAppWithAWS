package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


public class LoginController {
    @FXML
    private TextField UserTextField;

    @FXML
    private TextField PassTextField;

    @FXML
    private Button LoginButton;

    String workerUserName = "worker";
    String workerPassword = "1234";
    String customerUserName = "customer";
    String customerPassword = "1234";


    @FXML
    public void LogInButtonClicked(ActionEvent e) throws IOException {
        String userName = UserTextField.getText();
        String password = PassTextField.getText();

        Account loginAttempt = new Account();
        String role = loginAttempt.accountExists(userName, password);
        if(role.contains("worker")){
            // for data passing
            FXMLLoader loader = new FXMLLoader(getClass().getResource("WorkerInterface.fxml"));

            Parent root = FXMLLoader.load(getClass().getResource("WorkerInterface.fxml"));
            Scene workerView = new Scene(root);

            Stage workerWindow = (Stage) ((Node) e.getSource()).getScene().getWindow();
            workerWindow.setTitle("Worker interface");
            workerWindow.setScene(workerView);
            workerWindow.show();
        } else if(role.contains("customer")){
            // make messages available in later screens
            Main.messages = loginAttempt.getMessages();
            Main.user = userName;
            Parent root = FXMLLoader.load(getClass().getResource("menu.fxml"));
            Scene workerView = new Scene(root);

            Stage workerWindow = (Stage) ((Node) e.getSource()).getScene().getWindow();
            workerWindow.setTitle("Customer interface");
            workerWindow.setScene(workerView);
            workerWindow.show();
        }

        /*
        if(userName.equals(workerUserName) && password.equals(workerPassword)){
            Parent root = FXMLLoader.load(getClass().getResource("WorkerInterface.fxml"));
            Scene workerView = new Scene(root);

            Stage workerWindow = (Stage) ((Node) e.getSource()).getScene().getWindow();
            workerWindow.setTitle("Worker interface");
            workerWindow.setScene(workerView);
            workerWindow.show();
        }

        else if (userName.equals(customerUserName) && password.equals(customerPassword)){
            Parent root = FXMLLoader.load(getClass().getResource("menu.fxml"));
            Scene workerView = new Scene(root);

            Stage workerWindow = (Stage) ((Node) e.getSource()).getScene().getWindow();
            workerWindow.setTitle("Customer interface");
            workerWindow.setScene(workerView);
            workerWindow.show();
        }
         */

    }
}
