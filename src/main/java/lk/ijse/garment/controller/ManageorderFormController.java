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

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Properties;
import java.util.ResourceBundle;

public class ManageorderFormController implements Initializable {

    private static final String URL = "jdbc:mysql://localhost:3306/garment";
    private static final Properties props = new Properties();

    static {
        props.setProperty("user", "root");
        props.setProperty("password", "1234");
    }

    public Button menu;
    public Button manageemployeebtn;
    public Button homebtn;
    public JFXTextField orderid;
    public JFXTextField yreartxt;
    public JFXTextField monthtxt;
    public JFXTextField datetxt;
    public JFXComboBox comoboboxid;
    public JFXComboBox comoboboxtype;
    public Button addorder;
    public Button placeorder;

    public void menubtnonAction(ActionEvent actionEvent) {

    }

    public void manageemployeebtnonaction(ActionEvent actionEvent) {

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

    public void addorderbtnonaction(ActionEvent actionEvent) {

        if (comoboboxid!=null||comoboboxtype!=null) {
            Timestamp currentDate = new Timestamp(System.currentTimeMillis());
            String customer_id = (String) comoboboxid.getValue();
            String type = (String) comoboboxtype.getValue();
            String year = yreartxt.getText();
            String month = monthtxt.getText();
            String date = datetxt.getText();
            String dead_date = year + "-" + month + "-" + date;
            String order_id = orderid.getText();

            try (Connection con = DriverManager.getConnection(URL, props)) {
                String sql = "INSERT INTO ordertable(customer_id, type, order_date, dead_date, order_id)" +
                        "VALUES(?, ?, ?, ?, ?)";
                PreparedStatement pstm = con.prepareStatement(sql);
                pstm.setString(1, customer_id);
                pstm.setString(2, type);
                pstm.setString(3, String.valueOf(currentDate));
                pstm.setString(4, dead_date);
                pstm.setString(5, order_id);

                int affectedRows = pstm.executeUpdate();
                if (affectedRows > 0) {
                    new Alert(Alert.AlertType.CONFIRMATION,
                            " Clothes added successfully")
                            .show();
                } else {
                    new Alert(Alert.AlertType.CONFIRMATION,
                            "oops)")
                            .show();
                }

                datetxt.setText("");
                monthtxt.setText("");
                yreartxt.setText("");
                orderid.setText("");
                comoboboxid.setValue(null);
                comoboboxtype.setValue(null);

            } catch (SQLException throwables) {

            }

            int Id;

            try (Connection con = DriverManager.getConnection(URL, props)) {
                String sql = "SELECT * FROM ordertable ORDER BY order_id DESC LIMIT 1;";
                PreparedStatement pstm = con.prepareStatement(sql);

                ResultSet resultSet = pstm.executeQuery();

                if (resultSet.next()) {
                    Id = Integer.valueOf(resultSet.getString(5));
                    orderid.setText(String.valueOf(Id + 1));
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }


        }
    }

    public void checkorderbtnonaction(ActionEvent actionEvent) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ObservableList<String> id = FXCollections.observableArrayList();
        ObservableList<String> type = FXCollections.observableArrayList();

        try (Connection con = DriverManager.getConnection(URL, props)) {
            String sql = "SELECT * FROM customertable";
            PreparedStatement pstm = con.prepareStatement(sql);

            ResultSet resultSet = pstm.executeQuery();

            while (resultSet.next()) {
                String mid = resultSet.getString(2);
                id.add(mid);
            }

            comoboboxid.setItems(id);


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        try(Connection con = DriverManager.getConnection(URL, props)){
            String sql = "SELECT * FROM clothes";
            PreparedStatement pstm = con.prepareStatement(sql);

            ResultSet resultSet = pstm.executeQuery();

            while (resultSet.next()) {
                String mid = resultSet.getString(1);
                type.add(mid);
            }

            comoboboxtype.setItems(type);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        int Id;

        try (Connection con = DriverManager.getConnection(URL, props)) {
            String sql = "SELECT * FROM ordertable ORDER BY order_id DESC LIMIT 1;";
            PreparedStatement pstm = con.prepareStatement(sql);

            ResultSet resultSet = pstm.executeQuery();

            if (resultSet.next()) {
                Id = Integer.valueOf(resultSet.getString(5));
                orderid.setText(String.valueOf(Id+1));

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    public void placeorderbtnonaction(ActionEvent actionEvent) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/placeorder_form.fxml"));
        Parent root1 = null;
        try {
            root1 = (Parent) fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = new Stage();
        stage.setScene(new Scene(root1));
        stage.show();
    }
}
