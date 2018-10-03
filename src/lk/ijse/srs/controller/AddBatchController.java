/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.srs.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.srs.db.DBConnection;

/**
 * FXML Controller class
 *
 * @author Ilman Iqbal
 */
public class AddBatchController implements Initializable {

    @FXML
    private JFXButton btnSave;
    @FXML
    private JFXButton btnClear;
    @FXML
    private JFXTextField txtBatchName;
    @FXML
    private JFXTextField txtFee;
    @FXML
    private JFXComboBox<String> cmbCourseName;
    @FXML
    private ImageView btnClose;
    @FXML
    private JFXDatePicker dateStartDate;
    @FXML
    private JFXDatePicker dateEndDate;
    @FXML
    private AnchorPane root;

    private int courseId;
    private BatchController batchControllerAddress;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
    public void initVariable(BatchController batchController){
        batchControllerAddress = batchController;
    }
    
    @FXML
    private void cmbCourseNameOnMouseClicked(MouseEvent event) {
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            Statement stm = connection.createStatement();
            ResultSet rst = stm.executeQuery("SELECT name FROM Course");
            ArrayList<String> courseName = new ArrayList<>();
            while (rst.next()){
                courseName.add(rst.getString(1));
            }
            cmbCourseName.setItems(FXCollections.observableArrayList(courseName));
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(AddBatchController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static String generateBatchName(String courseName){
        int value = 0;
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            Statement stm = connection.createStatement();
            ResultSet rst = stm.executeQuery("SELECT SUBSTRING_INDEX(batchName, '-', -1) from batch WHERE batchName like'"+courseName+"%' ORDER BY SUBSTRING_INDEX(batchName, '-', -1) DESC LIMIT 1");
            
            if (rst.next()){
//                String CourseId = rst.getString(1);
//                String[] id = CourseId.split("-");
//                return " " + id[0] + (Integer.parseInt(id[1]) + 1);
                value = rst.getInt(1) + 1;
            }
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(AddBatchController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return courseName+"-"+value;
    }
    
    @FXML
    private void cmbCourseNameOnAction(ActionEvent event) {
        
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            Statement stm = connection.createStatement();
            ResultSet rst = stm.executeQuery("SELECT courseId FROM Course WHERE name='"+cmbCourseName.getValue()+"'");
            if (rst.next()){
                courseId = rst.getInt(1);
            }   
            String generatedBatchName = generateBatchName(cmbCourseName.getValue());
            txtBatchName.setText(generatedBatchName);
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(AddBatchController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    private void btnSaveOnClick(ActionEvent event) {
        try{
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement pstm = connection.prepareStatement("INSERT INTO Batch VALUES (?,?,?,?,?)");
            if (!txtBatchName.getText().isEmpty()){
                pstm.setObject(1, txtBatchName.getText());
            }
            else{
                pstm.setObject(1, null);
            }
            
            if (!txtFee.getText().isEmpty()){
                pstm.setObject(2, txtFee.getText());
            }
            else{
                pstm.setObject(2, null);
            }
            
            if(dateStartDate.getValue() !=null){
                pstm.setObject(3, java.sql.Date.valueOf(dateStartDate.getValue()));
                //pstm.setObject(3, (LocalDate)dateStartDate.getValue());
            }
            else{
                pstm.setObject(3, null);
            }
            if(dateEndDate.getValue() !=null){
                pstm.setObject(4, java.sql.Date.valueOf(dateEndDate.getValue()));
            }
            else{
                pstm.setObject(4, null);
            }
            pstm.setObject(5, courseId);
            int value = pstm.executeUpdate();
            if (value>0){
                batchControllerAddress.getAllBatches();
                Alert a = new Alert(Alert.AlertType.INFORMATION, "Updated Succeddfully", ButtonType.OK);
                a.show();
                btnClearOnClick(null);
            }
            else{
                batchControllerAddress.getAllBatches();
                Alert a = new Alert(Alert.AlertType.INFORMATION, "Failed to update", ButtonType.OK);
                a.show();
            }
            
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(EditBatchController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void btnClearOnClick(ActionEvent event) {
        cmbCourseName.getSelectionModel().clearSelection();
        txtBatchName.setText("");
        txtFee.setText("");
        dateStartDate.setValue(null);
        dateEndDate.setValue(null);
    }


    

    @FXML
    private void btnCloseOnClick(MouseEvent event) {
        Stage stage = (Stage) root.getScene().getWindow();
        stage.close();
    }

   
    
    
}
