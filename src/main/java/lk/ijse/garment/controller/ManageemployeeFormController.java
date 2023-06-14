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
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ManageemployeeFormController implements Initializable {

    private static final String URL = "jdbc:mysql://localhost:3306/garment";
    private static final Properties props = new Properties();

    static {
        props.setProperty("user", "root");
        props.setProperty("password", "1234");
    }

    public TextField searchbar;
    public Button manageemployeebtn;
    public ImageView searchimg;
    public Pane chooselist;
    public Button managesupplierbtn;
    public Button backbtn;
    public Button homebtn;
    public Button Addemployeebtn;
    public JFXTextField Nametxt;
    public JFXTextField Nictxt;
    public JFXTextField Gmailtxt;
    public JFXTextField Monumbertxt;
    public JFXTextField Addresstxt;
    public JFXTextField Idtxt;
    public JFXTextField Banumbertxt;
    public Button deletebtn;
    public Button updatebtn;
    public Button searchbtn;

    public void mouseclickedinsearchbar(MouseEvent mouseEvent) {
    }

    public void manageemployeebtnonaction(ActionEvent actionEvent) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/employeetable_from.fxml"));
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
        chooselist.setVisible(true);
    }

    public void mousedarggedformsearchbar(MouseEvent mouseEvent) {
        searchimg.setVisible(true);
    }

    public void btnbackbtnonacction(ActionEvent actionEvent) {
        chooselist.setVisible(false);
    }

    public void managesupplierbtnonaction(ActionEvent actionEvent) {
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

    public void Addemployeebtnonaction(ActionEvent actionEvent) {

        String mail = Gmailtxt.getText();
        String checknum = Nametxt.getText();
        String checkid  = Idtxt.getText();
        String checknumber = Monumbertxt.getText();

        boolean bool = isboolean(mail);

        System.out.println(bool);
        if (bool==true & checknum.matches("^[a-z]*$") & checkid.matches("^[0-9]*$") & checknumber.matches("^[0-9]*$")) {
            Timestamp currentDate = new Timestamp(System.currentTimeMillis());
            String name = Nametxt.getText();
            String nic = Nictxt.getText();
            String gmail = Gmailtxt.getText();
            Integer conumber = Integer.parseInt(Monumbertxt.getText());
            String address = Addresstxt.getText();
            String id = Idtxt.getText();
            Integer banumber = Integer.parseInt(Banumbertxt.getText());


            try (Connection con = DriverManager.getConnection(URL, props)) {
                String sql = "INSERT INTO employee(name, nic, gmail, conumber, address, id, banumber, Date)" +
                        "VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement pstm = con.prepareStatement(sql);
                pstm.setString(1, name);
                pstm.setString(2, nic);
                pstm.setString(3, gmail);
                pstm.setInt(4, conumber);
                pstm.setString(5, address);
                pstm.setString(6, id);
                pstm.setInt(7, banumber);
                pstm.setString(8, String.valueOf(currentDate));

                String massge = "Dear "+name+", welcome to our intexvog agency. You are our employee from now on and you appear here (id :" + id + ").";

                int affectedRows = pstm.executeUpdate();
                if (affectedRows > 0) {
                    new Alert(Alert.AlertType.CONFIRMATION,
                            " Employee added successfully")
                            .show();
                    JavaEmailSender j1 = new JavaEmailSender();
                    j1.send(name, massge, gmail);
                } else {
                    new Alert(Alert.AlertType.CONFIRMATION,
                            "oops)")
                            .show();
                }

                Nametxt.setText("");
                Nictxt.setText("");
                Gmailtxt.setText("");
                Monumbertxt.setText("");
                Addresstxt.setText("");
                Idtxt.setText("");
                Banumbertxt.setText("");

            } catch (SQLException throwables) {

            }
        }else {
            new Alert(Alert.AlertType.ERROR,
                    "Please enter the correct mail !")
                    .show();
        }

        int id;

        try (Connection con = DriverManager.getConnection(URL, props)) {
            String sql = "SELECT * FROM employee ORDER BY id DESC LIMIT 1;";
            PreparedStatement pstm = con.prepareStatement(sql);

            ResultSet resultSet = pstm.executeQuery();

            if (resultSet.next()) {
                id = Integer.valueOf(resultSet.getString(6));
                Idtxt.setText(String.valueOf(id+1));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void initialize(java.net.URL url, ResourceBundle resourceBundle) {

        int id;

        try (Connection con = DriverManager.getConnection(URL, props)) {
            String sql = "SELECT * FROM employee ORDER BY id DESC LIMIT 1;";
            PreparedStatement pstm = con.prepareStatement(sql);

            ResultSet resultSet = pstm.executeQuery();

            if (resultSet.next()) {
                id = Integer.valueOf(resultSet.getString(6));
                Idtxt.setText(String.valueOf(id+1));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static boolean isboolean(String email){
        String regex = "\\b[A-Za-z0-9._%+-]+@gmail\\.com\\b";
        Pattern ptn = Pattern.compile(regex);
        Matcher match = ptn.matcher(email);

        return match.matches();
    }

    public void deletebtnonaction(ActionEvent actionEvent) throws SQLException {
        String search = Idtxt.getText();

        try(Connection con = DriverManager.getConnection(URL,props)) {
            String sql = "DELETE FROM employee WHERE id = ?";
            PreparedStatement pstm = con.prepareStatement(sql);
            pstm.setString(1, search);

            if (pstm.executeUpdate() > 0) {
                new Alert(Alert.AlertType.CONFIRMATION, "Successfully employee deleted !").show();
            }

            Nametxt.setText("");
            Nictxt.setText("");
            Gmailtxt.setText("");
            Monumbertxt.setText("");
            Addresstxt.setText("");
            Idtxt.setText("");
            Banumbertxt.setText("");

            searchbar.setText("");

            int id;

            try (Connection conn = DriverManager.getConnection(URL, props)) {
                String sqql = "SELECT * FROM employee ORDER BY id DESC LIMIT 1;";
                PreparedStatement psttm = conn.prepareStatement(sqql);

                ResultSet resultSet = psttm.executeQuery();

                if (resultSet.next()) {
                    id = Integer.valueOf(resultSet.getString(6));
                    Idtxt.setText(String.valueOf(id+1));
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    public void updatebtnonaction(ActionEvent actionEvent) {
        String mail = Gmailtxt.getText();
        String checknum = Nametxt.getText();
        String checkid  = Idtxt.getText();
        String checknumber = Monumbertxt.getText();

        boolean bool = isboolean(mail);

        System.out.println(bool);
        if (bool==true & checknum.matches("^[a-z]*$") & checkid.matches("^[0-9]*$") & checknumber.matches("^[0-9]*$")) {
            String name = Nametxt.getText();
            String nic = Nictxt.getText();
            String gmail = Gmailtxt.getText();
            Integer conumber = Integer.parseInt(Monumbertxt.getText());
            String address = Addresstxt.getText();
            String id = Idtxt.getText();
            Integer banumber = Integer.parseInt(Banumbertxt.getText());


            try (Connection con = DriverManager.getConnection(URL, props)) {
                String sql = "UPDATE employee SET name = ?, nic = ?, gmail = ?, conumber = ?, address = ?, banumber = ? WHERE id = ?";

                PreparedStatement pstm = con.prepareStatement(sql);
                pstm.setString(1, name);
                pstm.setString(2, nic);
                pstm.setString(3, gmail);
                pstm.setInt(4, conumber);
                pstm.setString(5, address);
                pstm.setInt(6, banumber);
                pstm.setString(7, id);



                int affectedRows = pstm.executeUpdate();
                if (affectedRows > 0) {
                    new Alert(Alert.AlertType.CONFIRMATION,
                            " Employee updated successfully")
                            .show();
                } else {
                    new Alert(Alert.AlertType.CONFIRMATION,
                            "oops)")
                            .show();
                }

                Nametxt.setText("");
                Nictxt.setText("");
                Gmailtxt.setText("");
                Monumbertxt.setText("");
                Addresstxt.setText("");
                Idtxt.setText("");
                Banumbertxt.setText("");

                searchbar.setText("");

                int newid;

                try (Connection conn = DriverManager.getConnection(URL, props)) {
                    String sqql = "SELECT * FROM employee ORDER BY id DESC LIMIT 1;";
                    PreparedStatement psttm = conn.prepareStatement(sqql);

                    ResultSet resultSet = psttm.executeQuery();

                    if (resultSet.next()) {
                        newid = Integer.valueOf(resultSet.getString(6));
                        Idtxt.setText(String.valueOf(newid+1));
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

            } catch (SQLException throwables) {

            }
        }else {
            new Alert(Alert.AlertType.ERROR,
                    "Please enter the correct mail !")
                    .show();
        }

    }

    public void searchbtnonaction(ActionEvent actionEvent)throws SQLException {
        String search = searchbar.getText();

        try (Connection con  = DriverManager.getConnection(URL,props)){
            String sql = "SELECT *FROM employee WHERE id = ?";
            PreparedStatement pstm = con.prepareStatement(sql);
            pstm.setString(1, search);

            ResultSet resultSet = pstm.executeQuery();

            if(resultSet.next()){
                String name = resultSet.getString(1);
                String nic = resultSet.getString(2);
                String gmail = resultSet.getString(3);
                String conumber = resultSet.getString(4);
                String address = resultSet.getString(5);
                String id = resultSet.getString(6);
                String banknumber = resultSet.getString(7);

                Nametxt.setText(name);
                Nictxt.setText(nic);
                Gmailtxt.setText(gmail);
                Monumbertxt.setText(conumber);
                Addresstxt.setText(address);
                Idtxt.setText(id);
                Banumbertxt.setText(banknumber);

            }
        }
    }
}
