package lk.ijse.garment.controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import lk.ijse.garment.db.DBConnection;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Properties;
import java.util.ResourceBundle;

public class ManageclothesFormController implements Initializable {


    public Button checkmaterial;
    public Button menu;
    public Button homebtn;
    public JFXTextField clothestypetxt;
    public JFXTextField skutxt;
    public JFXTextField amounttxt;
    public JFXTextField colortxt;
    public JFXTextField pricetxt;
    public Button addclothesbtn;
    public JFXComboBox materialidcomboboxtxt;
    public JFXTextField discounttxt;

    public void btncheckmaterialbtnonaction(ActionEvent actionEvent) {

    }

    public void menubtnonAction(ActionEvent actionEvent) {

    }

    public void homebtnonaction(ActionEvent actionEvent) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/firstpage_form.fxml"));
        Parent root1 = null;
        try {
            root1 = (Parent) fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = new Stage();
        stage.setScene(new Scene(root1));
        stage.show();

        Stage currentStage = (Stage) homebtn.getScene().getWindow();
        currentStage.close();
    }

    public void addclothesbtnonaction(ActionEvent actionEvent) {

        Timestamp currentDate = new Timestamp(System.currentTimeMillis());
        String clothes_type = clothestypetxt.getText();
        String sku = skutxt.getText();
        String color = colortxt.getText();
        String amount = amounttxt.getText();
        Double price = Double.parseDouble(pricetxt.getText());
        String material_id = (String) materialidcomboboxtxt.getValue();
        int am = Integer.parseInt(amount);
        Double Total;

        lk.ijse.garment.model.manageclothesModel.addclothes(clothes_type,sku,color,amount,price,material_id,discounttxt.getText());
        /*if(discounttxt.getText()!=null){
             double discount = ((price*am)/100)*2;
             double tot = (price*am);
             Total = tot-discount;
        }else{
             Total = (price*am);
        }

        try (Connection con = DriverManager.getConnection(URL, props)) {
            String sql = "INSERT INTO clothes(clothes_type, sku, color, amount, price, material_id, date, Total)" +
                    "VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstm = con.prepareStatement(sql);
            pstm.setString(1, clothes_type);
            pstm.setString(2, sku);
            pstm.setString(3, color);
            pstm.setString(4, amount);
            pstm.setDouble(5, price);
            pstm.setString(6,material_id);
            pstm.setString(7, String.valueOf(currentDate));
            pstm.setDouble(8,Total);

            int affectedRows = pstm.executeUpdate();
            if (affectedRows > 0) {
                new Alert(Alert.AlertType.CONFIRMATION,
                        " Clothes added successfully")
                        .show();
            }else{
                new Alert(Alert.AlertType.CONFIRMATION,
                        "oops)")
                        .show();
            }*/

            clothestypetxt.setText("");
            skutxt.setText("");
            colortxt.setText("");
            amounttxt.setText("");
            pricetxt.setText("");
            discounttxt.setText("");

       /* } catch (SQLException throwables) {

        }*/
    }

    @Override
    public void initialize(java.net.URL url, ResourceBundle resourceBundle) {

        ObservableList<String> items = FXCollections.observableArrayList();

        try  {
            Connection con = DBConnection.getInstance().getConnection();
            String sql = "SELECT * FROM material";
            PreparedStatement pstm = con.prepareStatement(sql);

            ResultSet resultSet = pstm.executeQuery();

            while (resultSet.next()) {
                String mid = resultSet.getString(2);
                items.add(mid);
            }

            materialidcomboboxtxt.setItems(items);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
