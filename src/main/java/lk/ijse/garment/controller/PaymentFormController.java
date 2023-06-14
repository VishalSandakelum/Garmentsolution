package lk.ijse.garment.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.garment.db.DBConnection;
import lk.ijse.garment.dto.tm.Incomedetails;
import lk.ijse.garment.dto.tm.Material;
import lk.ijse.garment.dto.tm.tm.IncomedetailsTM;
import lk.ijse.garment.dto.tm.tm.MaterialTM;
import lk.ijse.garment.model.IncomedetailsModel;
import lk.ijse.garment.model.MaterialModel;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;

import net.sf.jasperreports.engine.util.JRProperties;

public class PaymentFormController implements Initializable {

    private static final String URL = "jdbc:mysql://localhost:3306/garment";
    private static final Properties props = new Properties();

    static {
        props.setProperty("user", "root");
        props.setProperty("password", "1234");
    }

    public TableView tableview;
    public TableColumn month;
    public TableColumn emptotal;
    public TableColumn cptotal;
    public TableColumn prorlose;
    public Button reportbtn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

         double emptotal = 0;
         double custotal = 0;
         double prolose = 0;


        YearMonth yearMonth = YearMonth.now();
        Month month = yearMonth.getMonth();

        LocalDate currentDate = LocalDate.now();
        int year = currentDate.getYear();
        int day = currentDate.getDayOfMonth();

        LocalDate today = LocalDate.now();
        LocalDate lastDayOfMonth = today.withDayOfMonth(today.lengthOfMonth());

        String now = year + "-" + month.getValue() + "-" + "23";

        try(Connection conn = DriverManager.getConnection(URL,props)){
            String sqli = "SELECT *FROM incomedetails WHERE month = ?";
            PreparedStatement psttm = conn.prepareStatement(sqli);

            psttm.setString(1,now);

            ResultSet resultSeat = psttm.executeQuery();

            if(resultSeat.next()){

            }else{
                if (month.getValue()==4 & day==23) {
                    try (Connection con = DriverManager.getConnection(URL, props)) {
                        String sql = "SELECT *FROM employeepayment";
                        PreparedStatement pstm = con.prepareStatement(sql);

                        ResultSet resultSet = pstm.executeQuery();

                        while (resultSet.next()) {
                            emptotal += resultSet.getDouble(4);
                        }

                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }

                    try (Connection con = DriverManager.getConnection(URL, props)) {
                        String sql = "SELECT *FROM customerincome";
                        PreparedStatement pstm = con.prepareStatement(sql);

                        ResultSet resultSet = pstm.executeQuery();

                        while (resultSet.next()) {
                            custotal += resultSet.getDouble(4);
                        }

                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }

                    prolose = custotal - emptotal;

                    try (Connection con = DriverManager.getConnection(URL, props)) {


                        String sql = "INSERT INTO incomedetails(month, empaymentotal, cuspaymentotal, proflose)" +
                                "VALUES (?, ?, ?, ?)";

                        PreparedStatement pstm = con.prepareStatement(sql);
                        pstm.setString(1, now);
                        pstm.setDouble(2, emptotal);
                        pstm.setDouble(3, custotal);
                        pstm.setDouble(4, prolose);


                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }

                }
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


        getAll();setCellValueFactory();
    }

    private void setCellValueFactory() {
        month.setCellValueFactory(new PropertyValueFactory<>("month"));
        emptotal.setCellValueFactory(new PropertyValueFactory<>("empaymenttotal"));
        cptotal.setCellValueFactory(new PropertyValueFactory<>("cuspaymentotal"));
        prorlose.setCellValueFactory(new PropertyValueFactory<>("proflose"));
    }


    private void getAll() {
        try {
            ObservableList<IncomedetailsTM> obList = FXCollections.observableArrayList();
            List<Incomedetails> incomeList = IncomedetailsModel.getAll();

            for (Incomedetails incomedetails : incomeList) {
                obList.add(new IncomedetailsTM(
                        incomedetails.getId(),
                        incomedetails.getMonth(),
                        incomedetails.getEmpaymenttotal(),
                        incomedetails.getCuspaymentotal(),
                        incomedetails.getProflose()
                ));
            }
            tableview.setItems(obList);
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "SQL Error!").show();
        }
    }

    public void reportbtnonaction(ActionEvent actionEvent) {

        try {
            JRProperties.setProperty("net.sf.jasperreports.awt.ignore.missing.font", "true");
            JasperDesign jasperDesign = JRXmlLoader.load("src/main/resources/jasper/Repor.jrxml");
            JRDesignQuery query = new JRDesignQuery();
            query.setText("select *from incomedetails");
            jasperDesign.setQuery(query);

            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,null, DBConnection.getInstance().getConnection());
            JasperViewer.viewReport(jasperPrint,false);

        } catch (JRException | SQLException e) {
            e.printStackTrace();
        }

    }
}
