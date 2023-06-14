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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import lk.ijse.garment.db.DBConnection;
import lk.ijse.garment.model.ManagesupplierModel;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ManagesupplierFormController implements Initializable {


    public Pane chooselist;
    public Button backbtn;
    public Pane cottonpane;
    public Pane wollpane;
    public Button homebtn;
    public Button savesupplierbtn;
    public JFXTextField nametxt;
    public JFXTextField nictext;
    public JFXTextField gmailtext;
    public JFXTextField monumbertext;
    public JFXTextField addresstxt;
    public JFXTextField banknumbtxt;
    public JFXTextField idtext;
    public Button managesupplierbtn1;
    public Button searchbtn;
    public Button deletesupplierbtn;
    public Button updatesupplier;
    public TextField searchtxt;
    public Button checkbtn;

    public void menubtnonAction(ActionEvent actionEvent) {
        chooselist.setVisible(true);
    }

    public void btnbackbtnonacction(ActionEvent actionEvent) {
        chooselist.setVisible(false);
    }

    public void managesupplierbtnonaction(ActionEvent actionEvent) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/suppliertable_from.fxml"));
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

    public void woolmouseenter(MouseEvent mouseEvent) {
        wollpane.setVisible(true);
    }

    public void woolmouseexite(MouseEvent mouseEvent) {
        wollpane.setVisible(false);
    }

    public void cottonmouseenter(MouseEvent mouseEvent) {
        cottonpane.setVisible(true);
    }

    public void cottonmouseexite(MouseEvent mouseEvent) {
        cottonpane.setVisible(false);
    }

    public void btnseemorepanebtnonaction(ActionEvent actionEvent) {

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

    public void savesupplierbtnonaction(ActionEvent actionEvent) {
        int Id;
        String name = nametxt.getText();
        String nic = nictext.getText();
        String gmail = gmailtext.getText();
        String address = addresstxt.getText();
        Integer conumber = Integer.parseInt(monumbertext.getText());
        Integer banumber = Integer.parseInt(banknumbtxt.getText());
        String id = idtext.getText();
        if (isGmail(gmail) & isname(name) & isint(String.valueOf(conumber)) & isint(String.valueOf(banumber)) & isint(id)) {
            Boolean bool = lk.ijse.garment.model.ManagesupplierModel.savebtnAction(name,nic,gmail,address,conumber,banumber,id);

            if(bool==true){
                nametxt.setText("");
                idtext.setText("");
                nictext.setText("");
                monumbertext.setText("");
                banknumbtxt.setText("");
                addresstxt.setText("");
                gmailtext.setText("");


                try {
                    Connection con = DBConnection.getInstance().getConnection();
                    String sql = "SELECT * FROM suplier ORDER BY id DESC LIMIT 1;";
                    PreparedStatement pstm = con.prepareStatement(sql);

                    ResultSet resultSet = pstm.executeQuery();

                    if (resultSet.next()) {
                        Id = Integer.valueOf(resultSet.getString(7));
                        idtext.setText(String.valueOf(Id+1));
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }

        }else {
            new Alert(Alert.AlertType.WARNING,
                    "Please enter valide value !")
                    .show();
        }
    }

    public void searchbtnonaction(ActionEvent actionEvent) {

        String search = searchtxt.getText();

        nametxt.setText("");
        idtext.setText("");
        nictext.setText("");
        monumbertext.setText("");
        banknumbtxt.setText("");
        addresstxt.setText("");
        gmailtext.setText("");

        Boolean bool = lk.ijse.garment.model.ManagesupplierModel.searchbtnAction(search);

        if(bool == true){
            nametxt.setText(ManagesupplierModel.name);
            idtext.setText(ManagesupplierModel.id);
            nictext.setText(ManagesupplierModel.nic);
            monumbertext.setText(ManagesupplierModel.number);
            banknumbtxt.setText(ManagesupplierModel.banknumber);
            addresstxt.setText(ManagesupplierModel.address);
            gmailtext.setText(ManagesupplierModel.gmail);
        }else {
            new Alert(Alert.AlertType.WARNING, "Please check and enter correct id !").show();
        }

        searchtxt.setText("");
    }

    public void deletesupplierbtnonaction(ActionEvent actionEvent) {
        String id = idtext.getText();
        Boolean bool = lk.ijse.garment.model.ManagesupplierModel.deletebtnAction(id);
        if(bool != true){
            new Alert(Alert.AlertType.WARNING, "Something wrong !").show();
        }else{
            nametxt.setText("");
            idtext.setText("");
            nictext.setText("");
            monumbertext.setText("");
            banknumbtxt.setText("");
            addresstxt.setText("");
            gmailtext.setText("");
        }
    }

    public void updatesupplierbtn(ActionEvent actionEvent) {

        String name = nametxt.getText();
        String nic = nictext.getText();
        String gmail = gmailtext.getText();
        String address = addresstxt.getText();
        Integer conumber = Integer.parseInt(monumbertext.getText());
        Integer banumber = Integer.parseInt(banknumbtxt.getText());
        String id = idtext.getText();

        if(isGmail(gmail) & isname(name) & isint(String.valueOf(conumber)) & isint(String.valueOf(banumber)) & isint(id)){
            Boolean bool = lk.ijse.garment.model.ManagesupplierModel.updatebtnAction(name,nic,gmail,address,conumber,banumber,id);
            if(bool==true){
                nametxt.setText("");
                idtext.setText("");
                nictext.setText("");
                monumbertext.setText("");
                banknumbtxt.setText("");
                addresstxt.setText("");
                gmailtext.setText("");
            }
        }else{
            new Alert(Alert.AlertType.WARNING, "Please enter valide value !").show();
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
    public void initialize(URL url, ResourceBundle resourceBundle) {
        int id;
        try {
            Connection con = DBConnection.getInstance().getConnection();
            String sql = "SELECT * FROM suplier ORDER BY id DESC LIMIT 1;";
            PreparedStatement pstm = con.prepareStatement(sql);

            ResultSet resultSet = pstm.executeQuery();

            if (resultSet.next()) {
                id = Integer.valueOf(resultSet.getString(7));
                idtext.setText(String.valueOf(id+1));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void checkbtnonaction(ActionEvent actionEvent) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/suppliertable_from.fxml"));
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
