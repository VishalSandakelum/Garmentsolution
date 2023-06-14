package lk.ijse.garment.controller;

import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lk.ijse.garment.db.DBConnection;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ManagecustomerFormController implements Initializable {


    public Button manageemployeebtn;
    public Button menu;
    public Button homebtn;
    public Button savecustomerbtn;
    public JFXTextField fullnametext;
    public JFXTextField nicnumbertext;
    public JFXTextField gmailtext;
    public JFXTextField numbertext;
    public JFXTextField addresstext;
    public JFXTextField idtext;
    public JFXTextField banknumbertext;
    public Button deletebtn;
    public Button updatebtn;
    public Button searchbtn;
    public TextField searchtxt;

    public void manageemployeebtnonaction(ActionEvent actionEvent) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/customertable_from.fxml"));
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

        FirstpageFormController.bool = false;


    }

    public void savecustomerbtnonaction(ActionEvent actionEvent) {

        String checknum = fullnametext.getText();
        String id  = idtext.getText();
        String number = numbertext.getText();
        String checkgmail = gmailtext.getText();

        if(isGmail(checkgmail) & isname(checknum)) {
            if(isint(id) & isint(number)) {
                try {
                    Timestamp Date = new Timestamp(System.currentTimeMillis());
                    String name = fullnametext.getText();
                    String customer_id = idtext.getText();
                    String nic_number = nicnumbertext.getText();
                    Integer contact_number = Integer.parseInt(numbertext.getText());
                    String address = addresstext.getText();
                    Integer bank_number = Integer.parseInt(banknumbertext.getText());
                    String gmail = gmailtext.getText();


                    try  {
                        Connection con = DBConnection.getInstance().getConnection();
                        String sql = "INSERT INTO customertable(name, customer_id, nic_number, contact_number, address, bank_number, gmail, Date)" +
                                "VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
                        PreparedStatement pstm = con.prepareStatement(sql);
                        pstm.setString(1, name);
                        pstm.setString(2, customer_id);
                        pstm.setString(3, nic_number);
                        pstm.setInt(4, contact_number);
                        pstm.setString(5, address);
                        pstm.setInt(6, bank_number);
                        pstm.setString(7, gmail);
                        pstm.setString(8, String.valueOf(Date));


                        String massge = "Dear customer, welcome to our intexvog agency. You are our user from now on and you appear here (id :" + customer_id + ").";

                        int affectedRows = pstm.executeUpdate();
                        if (affectedRows > 0) {
                            new Alert(Alert.AlertType.CONFIRMATION,
                                    " Customer added successfully")
                                    .show();
                            JavaEmailSender j1 = new JavaEmailSender();
                            j1.send("Dear Customer ", massge, gmail);
                        } else {
                            new Alert(Alert.AlertType.CONFIRMATION,
                                    "oops)")
                                    .show();
                        }

                        fullnametext.setText("");
                        idtext.setText("");
                        nicnumbertext.setText("");
                        numbertext.setText("");
                        addresstext.setText("");
                        banknumbertext.setText("");
                        gmailtext.setText("");

                        int newid;
                        try  {
                            Connection newcon = DBConnection.getInstance().getConnection();
                            String newsql = "SELECT * FROM customertable ORDER BY customer_id DESC LIMIT 1;";
                            PreparedStatement pstmt = newcon.prepareStatement(newsql);

                            ResultSet resultSet = pstmt.executeQuery();

                            if (resultSet.next()) {
                                newid = Integer.valueOf(resultSet.getString(2));
                                idtext.setText(String.valueOf(newid+1));
                            }
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }

                    } catch (SQLException throwables) {

                    }
                } catch (NumberFormatException e) {
                    new Alert(Alert.AlertType.WARNING,
                            "Enter correct value !")
                            .show();
                }
            }else {
                new Alert(Alert.AlertType.ERROR, "Please enter valide value").show();
            }
        }else {
            new Alert(Alert.AlertType.ERROR, "Please enter valide value").show();
        }
    }

    public void deletebtnonaction(ActionEvent actionEvent)  {

        String search = idtext.getText();

        try{
            Connection con = DBConnection.getInstance().getConnection();
            String sql = "DELETE FROM customertable WHERE customer_id = ?";
            PreparedStatement pstm = con.prepareStatement(sql);
            pstm.setString(1, search);

            if(pstm.executeUpdate() > 0){
                new Alert(Alert.AlertType.CONFIRMATION, "Successfully customer deleted !").show();
            }
            fullnametext.setText("");
            idtext.setText("");
            nicnumbertext.setText("");
            numbertext.setText("");
            addresstext.setText("");
            banknumbertext.setText("");
            gmailtext.setText("");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void updatebtnonaction(ActionEvent actionEvent) {

        String name = fullnametext.getText();
        String customer_id = idtext.getText();
        String nic_number = nicnumbertext.getText();
        Integer contact_number = Integer.parseInt(numbertext.getText());
        String address = addresstext.getText();
        Integer bank_number = Integer.parseInt(banknumbertext.getText());
        String gmail = gmailtext.getText();

        try{
            Connection con = DBConnection.getInstance().getConnection();
            String sql = "UPDATE customertable SET name = ?, nic_number = ?, contact_number = ?, address = ?, bank_number = ?, gmail = ? WHERE customer_id = ?";
            PreparedStatement pstm = con.prepareStatement(sql);

            pstm.setString(1, name);
            pstm.setString(2, nic_number);
            pstm.setInt(3, contact_number);
            pstm.setString(4, address);
            pstm.setInt(5, bank_number);
            pstm.setString(6, gmail);
            pstm.setString(7, customer_id);

            if(pstm.executeUpdate() > 0){
                new Alert(Alert.AlertType.CONFIRMATION, "Customer updated successfully !").show();
            }else{
                new Alert(Alert.AlertType.ERROR, "Please check and enter the correct value !").show();
            }

            fullnametext.setText("");
            idtext.setText("");
            nicnumbertext.setText("");
            numbertext.setText("");
            addresstext.setText("");
            banknumbertext.setText("");
            gmailtext.setText("");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    public void searchbtnonaction(ActionEvent actionEvent) {
        String search = searchtxt.getText();

        try {
            Connection con = DBConnection.getInstance().getConnection();
            String sql = "SELECT *FROM customertable WHERE customer_id = ?";
            PreparedStatement pstm = con.prepareStatement(sql);

            pstm.setString(1, search);

            ResultSet resultSet = pstm.executeQuery();
            if(resultSet.next()){
                String name = resultSet.getString(1);
                String custid = resultSet.getString(2);
                String nicnum = resultSet.getString(3);
                String contactnum = resultSet.getString(4);
                String address = resultSet.getString(5);
                String banknum = resultSet.getString(6);
                String gmail = resultSet.getString(7);

                fullnametext.setText(name);
                idtext.setText(custid);
                nicnumbertext.setText(nicnum);
                numbertext.setText(contactnum);
                addresstext.setText(address);
                banknumbertext.setText(banknum);
                gmailtext.setText(gmail);

                searchtxt.setText("");

            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    public void searchtxt(ActionEvent actionEvent) {
        String search = searchtxt.getText();

        try{
            Connection con = DBConnection.getInstance().getConnection();
            String sql = "SELECT *FROM customertable WHERE customer_id = ?";
            PreparedStatement pstm = con.prepareStatement(sql);

            pstm.setString(1, search);

            ResultSet resultSet = pstm.executeQuery();
            if(resultSet.next()){
                String name = resultSet.getString(1);
                String custid = resultSet.getString(2);
                String nicnum = resultSet.getString(3);
                String contactnum = resultSet.getString(4);
                String address = resultSet.getString(5);
                String banknum = resultSet.getString(6);
                String gmail = resultSet.getString(7);

                fullnametext.setText(name);
                idtext.setText(custid);
                nicnumbertext.setText(nicnum);
                numbertext.setText(contactnum);
                addresstext.setText(address);
                banknumbertext.setText(banknum);
                gmailtext.setText(gmail);

                searchtxt.setText("");

            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    private static boolean isGmail(String email) {
        String regex = "\\b[A-Za-z0-9._%+-]+@gmail\\.com\\b";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    private static boolean isname(String name) {
        String regex = "^[a-z]*$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(name);
        return matcher.matches();
    }
    private static boolean isint(String intnum) {
        String regex = "^[0-9]*$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(intnum);
        return matcher.matches();
    }

    @Override
    public void initialize(java.net.URL url, ResourceBundle resourceBundle) {
        int id;
        try  {
            Connection con = DBConnection.getInstance().getConnection();
            String sql = "SELECT * FROM customertable ORDER BY customer_id DESC LIMIT 1;";
            PreparedStatement pstm = con.prepareStatement(sql);

            ResultSet resultSet = pstm.executeQuery();

            if (resultSet.next()) {
                id = Integer.valueOf(resultSet.getString(2));
                idtext.setText(String.valueOf(id+1));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
