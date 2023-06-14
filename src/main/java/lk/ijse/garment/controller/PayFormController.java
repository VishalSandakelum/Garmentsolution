package lk.ijse.garment.controller;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

import java.net.URL;
import java.sql.*;
import java.util.Date;
import java.util.Properties;
import java.util.ResourceBundle;

public class PayFormController implements Initializable {

    private static final String URL = "jdbc:mysql://localhost:3306/garment";
    private static final Properties props = new Properties();

    static {
        props.setProperty("user", "root");
        props.setProperty("password", "1234");
    }

    public Text idtxt;
    public Text nametxt;
    public Text nictxt;
    public Text gmailtxt;
    public Text banknumbertxt;
    public Button paymentgaranted;

    public void paymentgarantedbtnonaction(ActionEvent actionEvent) {

        int paymentid = 0;

        try (Connection con = DriverManager.getConnection(URL, props)) {
            String sql = "SELECT * FROM employeepayment";
            PreparedStatement pstm = con.prepareStatement(sql);

            ResultSet resultSet = pstm.executeQuery();

            if (resultSet.next()) {
                int paytid = resultSet.getInt(3);
                paymentid  = paytid+1;
            }else{
                paymentid = 1;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        double price  = 50000;
        Timestamp currentDate = new Timestamp(System.currentTimeMillis());
        String employee_id = idtxt.getText();
        String payment_id = String.valueOf(paymentid);
        Double payment = Double.parseDouble(String.valueOf(price));


        try (Connection con = DriverManager.getConnection(URL, props)) {
            String sql = "INSERT INTO employeepayment(Date, employee_id, payment_id, payment)" +
                    "VALUES(?, ?, ?, ?)";
            PreparedStatement pstm = con.prepareStatement(sql);
            pstm.setString(1, String.valueOf(currentDate));
            pstm.setString(2, employee_id);
            pstm.setString(3, payment_id);
            pstm.setDouble(4, payment);


            int affectedRows = pstm.executeUpdate();
            if (affectedRows > 0) {
                new Alert(Alert.AlertType.CONFIRMATION,
                        " Succesfully payed!")
                        .show();
            } else {
                new Alert(Alert.AlertType.CONFIRMATION,
                        "oops)")
                        .show();
            }


        } catch (SQLException throwables) {

        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Date date = new Date();

        int month = date.getMonth() + 1;
        int day = date.getDate();

        System.out.println(month + " " + day);

        idtxt.setText(EmployeepaymenttableController.Id);
        nametxt.setText(EmployeepaymenttableController.Namee);
        nictxt.setText(EmployeepaymenttableController.Nic);
        gmailtxt.setText(EmployeepaymenttableController.Gmaiil);
        banknumbertxt.setText(String.valueOf(EmployeepaymenttableController.Bannumber));

        String employee_id = EmployeepaymenttableController.Id;
        if (month == 4 && day == 30) {
            paymentgaranted.setDisable(false);

            try (Connection con = DriverManager.getConnection(URL, props)) {
                String sql = "SELECT * FROM employeepayment WHERE employee_id = ?";
                PreparedStatement pstm = con.prepareStatement(sql);
                pstm.setString(1, employee_id);

                ResultSet resultSet = pstm.executeQuery();
                if (resultSet.next()) {
                    double payment = resultSet.getDouble(4);
                    paymentgaranted.setDisable(true);

                } else {
                    paymentgaranted.setDisable(false);
                }

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }else{
            paymentgaranted.setDisable(true);
        }
    }
}

