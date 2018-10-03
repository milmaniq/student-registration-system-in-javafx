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
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javafx.stage.Window;
import lk.ijse.srs.db.DBConnection;
import lk.ijse.srs.model.BatchTM;


/**
 * FXML Controller class
 *
 * @author Ilman Iqbal
 */
public class EditBatchController implements Initializable {

    @FXML
    private JFXButton btnSave;
    @FXML
    private JFXButton btnClear;
    @FXML
    private JFXTextField txtBachName;
    @FXML
    private JFXTextField txtFee;
   
    @FXML
    private JFXDatePicker dateStartDate;
    @FXML
    private JFXDatePicker dateEndDate;
    @FXML
    private ImageView btnClose;
    @FXML
    private AnchorPane root;
    private BatchController batchControllerAddress;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        txtBachName.setDisable(true);
    }    
    public void initVariable(BatchTM batchTM, BatchController batchController){
        batchControllerAddress = batchController;
        txtBachName.setText(batchTM.getBatchName());
        txtFee.setText(Integer.toString(batchTM.getFee()));
        if (batchTM.getStartDate()!=null){
            dateStartDate.setValue(LocalDate.parse(batchTM.getStartDate()));
        }
        if (batchTM.getEndDate()!=null){
            dateEndDate.setValue(LocalDate.parse(batchTM.getEndDate()));
        }
        
    }
    
    @FXML
    private void btnSaveOnClick(ActionEvent event) {
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement pstm = connection.prepareStatement("UPDATE Batch SET fee=?, startDate=?, endDate=? WHERE batchName=?");
            if (!txtFee.getText().isEmpty()){
                pstm.setObject(1, txtFee.getText());
            }
            else{
                pstm.setObject(1, null);
            }
            
            if(dateStartDate.getValue() !=null){
                pstm.setObject(2, java.sql.Date.valueOf(dateStartDate.getValue()));
            }
            else{
                pstm.setObject(2, null);
            }
            if(dateEndDate.getValue() !=null){
                pstm.setObject(3, java.sql.Date.valueOf(dateEndDate.getValue()));
            }
            else{
                pstm.setObject(3, null);
            }
            pstm.setObject(4, txtBachName.getText());
            int value = pstm.executeUpdate();
    //        System.out.println("1");
            if (value>0){
                batchControllerAddress.getAllBatches();
                Alert a = new Alert(Alert.AlertType.INFORMATION, "Updated Succeddfully", ButtonType.OK);
                a.show();
                btnCloseKeyClicked(null);
            }
            else{
                Alert a = new Alert(Alert.AlertType.INFORMATION, "Failed to update", ButtonType.OK);
                a.show();
            }
            
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(EditBatchController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void btnClearOnClick(ActionEvent event) {
        txtFee.setText("");
        dateStartDate.setValue(null);
        dateEndDate.setValue(null);
    }

    @FXML
    private void btnCloseKeyClicked(MouseEvent event) {
        Stage stage = (Stage) root.getScene().getWindow();
        stage.close();
    }

    
}
