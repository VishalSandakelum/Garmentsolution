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
import lk.ijse.garment.model.ManagematerialModel;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ManagematerialFormController implements Initializable {



    public Button menu;
    public Button managesupplierbtn11;
    public Button checkmaterial;
    public Button homebtn;
    public JFXTextField nametxt;
    public JFXTextField idtxt;
    public JFXTextField sidtxt;
    public JFXTextField amounttxt;
    public JFXTextField pricetxt;
    public Button addmaterialbtn;
    public Button updatebtn;
    public Button deletebtn;
    public Button searchbtn;
    public TextField searchtxt;

    public void menubtnonAction(ActionEvent actionEvent) {

    }


    public void btncheckmaterialbtnonaction(ActionEvent actionEvent) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/materialtable_form.fxml"));
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

    public void homebtnonAction(ActionEvent actionEvent) {
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

    public void savematerialonaction(ActionEvent actionEvent) {
        String materialname = nametxt.getText();
        String material_id = idtxt.getText();
        String id = sidtxt.getText();
        String amount = amounttxt.getText();
        double price = Double.parseDouble(pricetxt.getText());


        Boolean bool = lk.ijse.garment.model.ManagematerialModel.savematerialbtnAction(materialname,material_id,id,amount,price);

        if(bool==true){
            nametxt.setText("");
            idtxt.setText("");
            sidtxt.setText("");
            amounttxt.setText("");
            pricetxt.setText("");
        }

            int newid;
            try {
                Connection newcon = DBConnection.getInstance().getConnection();
                String newsql = "SELECT * FROM material ORDER BY material_id DESC LIMIT 1;";
                PreparedStatement pstmt = newcon.prepareStatement(newsql);

                ResultSet resultSet = pstmt.executeQuery();

                if (resultSet.next()) {
                    newid = Integer.valueOf(resultSet.getString(2));
                    idtxt.setText(String.valueOf(newid+1));
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }


    }

    @Override
    public void initialize(java.net.URL url, ResourceBundle resourceBundle) {
        int newid;
        try  {
            Connection newcon = DBConnection.getInstance().getConnection();
            String newsql = "SELECT * FROM material ORDER BY material_id DESC LIMIT 1;";
            PreparedStatement pstmt = newcon.prepareStatement(newsql);

            ResultSet resultSet = pstmt.executeQuery();

            if (resultSet.next()) {
                newid = Integer.valueOf(resultSet.getString(2));
                idtxt.setText(String.valueOf(newid+1));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void updatebtnonaction(ActionEvent actionEvent) {

        String materialname = nametxt.getText();
        String material_id = idtxt.getText();
        String id = sidtxt.getText();
        String amount = amounttxt.getText();
        double price = Double.parseDouble(pricetxt.getText());

        if(isname(materialname) & isint(material_id) & isint(id) ){
            Boolean bool = lk.ijse.garment.model.ManagematerialModel.updatebtnAction(materialname,material_id,id,amount,price);

            if(bool == true){
                nametxt.setText("");
                idtxt.setText("");
                sidtxt.setText("");
                amounttxt.setText("");
                pricetxt.setText("");
            }

        }else {
            new Alert(Alert.AlertType.WARNING, "Please enter valide value !").show();
        }

    }

    public void deletebtnonaction(ActionEvent actionEvent) {
        String mat_id = idtxt.getText();
        Boolean bool = lk.ijse.garment.model.ManagematerialModel.deletebtnAction(mat_id);

        if(bool == true){
            nametxt.setText("");
            idtxt.setText("");
            sidtxt.setText("");
            amounttxt.setText("");
            pricetxt.setText("");
        }

    }

    public static boolean isname(String name) {
        String regex = "^[a-z]*$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(name);
        return matcher.matches();
    }
    public static boolean isint(String intnum) {
        String regex = "^[0-9]*$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(intnum);
        return matcher.matches();
    }

    public void searchbtnonaction(ActionEvent actionEvent) {

        String material_id = searchtxt.getText();

        Boolean bool = lk.ijse.garment.model.ManagematerialModel.searchbtnAction(material_id);

        if(bool == true){
            nametxt.setText(ManagematerialModel.materialname);
            idtxt.setText(ManagematerialModel.materialid);
            sidtxt.setText(ManagematerialModel.ID);
            amounttxt.setText(ManagematerialModel.Amount);
            pricetxt.setText(ManagematerialModel.PRICE);

            searchtxt.setText("");

        }else {
            nametxt.setText("");
            idtxt.setText("");
            sidtxt.setText("");
            amounttxt.setText("");
            pricetxt.setText("");
            searchtxt.setText("");
        }

    }
}
