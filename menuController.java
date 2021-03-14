package sample;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;

public class menuController {
    @FXML private TableView<Drink> menuTable;

    @FXML
    private TableColumn<Drink, String> hotDrinkColumn;

    @FXML
    private TableColumn<Drink, String> coldDrinkColumn;

    @FXML
    private TableColumn<Drink, String> hotCoffeeColumn;

    @FXML
    private TableColumn<Drink, String> coldCoffeeColumn;

    public void logoutButtonPressed(ActionEvent e) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("FrenchLogin.fxml"));
        Scene workerView = new Scene(root);

        Stage menuWindow = (Stage) ((Node) e.getSource()).getScene().getWindow();

        menuWindow.setTitle("French Press Coffee");
        menuWindow.setScene(workerView);
        menuWindow.show();
    }

    public void orderButtonPressed(ActionEvent e) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Order.fxml"));
        Scene workerView = new Scene(root);

        Stage menuWindow = (Stage) ((Node) e.getSource()).getScene().getWindow();

        menuWindow.setTitle("Order");
        menuWindow.setScene(workerView);
        menuWindow.show();
    }


    public void initialize(){
        hotCoffeeColumn.setCellValueFactory(new PropertyValueFactory<Drink, String>("HotCoffee"));
        coldCoffeeColumn.setCellValueFactory(new PropertyValueFactory<Drink, String>("ColdCoffee"));
        hotDrinkColumn.setCellValueFactory(new PropertyValueFactory<Drink, String>("HotDrink"));
        coldDrinkColumn.setCellValueFactory(new PropertyValueFactory<Drink, String>("ColdDrink"));
        menuTable.setItems(getDrink());
    }

    public ObservableList<Drink> getDrink(){
        ObservableList<Drink> d = FXCollections.observableArrayList();

        Drink row1 = new Drink("Blonde Roast", "Honey Almondmilk Cold Brew", "Toasted White Hot Chocolate", "Star Drink");
        Drink row2 = new Drink("Caffè Misto", "Irish Cream Cold Brew", "Peppermint White Hot Chocolate", "Kiwi Starfruit Starbucks Refreshers® Beverage");
        Drink row3 = new Drink("Pike Place® Roast", "Cold Brew with Cinnamon Almondmilk Foam", "Hot Chocolate", "Kiwi Starfruit Lemonade Starbucks Refreshers® Beverage");
        Drink row4 = new Drink("Cappuccino", "Iced Coffee", "Steamed Apple Juice", "Dragon Drink");
        Drink row5 = new Drink("Pistachio Latte", "Nitro Flat White", "Pistachio Crème", "Mango Dragonfruit Starbucks Refreshers® Beverage");
        Drink row6 = new Drink("Eggnog Latte", "Iced Flat White", "Eggnog Crème", "Mango Dragonfruit Lemonade Starbucks Refreshers® Beverage");
        Drink row7 = new Drink("Honey Oatmilk Latte", "Iced Honey Almondmilk Flat White", "Cinnamon Dolce Crème", "Strawberry Açaí Lemonade Starbucks Refreshers® Beverage");
        Drink row8 = new Drink("Caffè Latte", "Iced Pistachio Latte", "Vanilla Crème", "Very Berry Hibiscus Lemonade Starbucks Refreshers® Beverage");
        Drink row9 = new Drink("Starbucks Reserve® Latte", "Iced Caramel Brulée Latte", "Chestnut Praline Crème", "Pink Drink");
        // if you add something here also add it in orderController.java
        d.add(row1); d.add(row2); d.add(row3); d.add(row4); d.add(row5); d.add(row6); d.add(row7);d.add(row8);d.add(row9);
        return d;
    }




}
