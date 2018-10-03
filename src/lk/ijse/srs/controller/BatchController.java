/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.srs.controller;

import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import lk.ijse.srs.db.DBConnection;
import lk.ijse.srs.model.BatchTM;

/**
 * FXML Controller class
 *
 * @author Ilman Iqbal
 */
public class BatchController implements Initializable {

    @FXML
    private JFXTextField txtSearchBatch;
    @FXML
    private TableView<BatchTM> tblBatch;
    @FXML
    private ImageView btnAddBatch;
    @FXML
    private ImageView btnBack;
    
    @FXML
    private AnchorPane root;
    
    BatchController batchControllerAddress;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        batchControllerAddress = this;
        
        tblBatch.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("batchName"));
        tblBatch.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("fee"));
        tblBatch.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("startDate"));
        tblBatch.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("endDate"));
        tblBatch.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("edit"));
        tblBatch.getColumns().get(5).setCellValueFactory(new PropertyValueFactory<>("remove"));
        
        getAllBatches();
        
        tblBatch.getSelectionModel().getSelectedCells().addListener(new ListChangeListener<TablePosition>(){
            @Override
            public void onChanged(ListChangeListener.Change<? extends TablePosition> c) {
                int column = tblBatch.getSelectionModel().getSelectedCells().get(0).getColumn();
                if (column == 4){
                    BatchTM batchTM = tblBatch.getSelectionModel().getSelectedItem();
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/lk/ijse/srs/view/EditBatch.fxml"));
                        Parent p = loader.load();
                        EditBatchController controller = loader.<EditBatchController>getController();
                        controller.initVariable(batchTM, batchControllerAddress);
                        
                        Scene scene = new Scene(p);
                        Stage stage = new Stage();
                        stage.initStyle(StageStyle.UNDECORATED);
                        stage.setScene(scene);
                        stage.show();
                    } catch (IOException ex) {
                        Logger.getLogger(BatchController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                }
                else if (column == 5){
                    String batchName = tblBatch.getSelectionModel().getSelectedItem().getBatchName();
                    try {
                        Connection connection = DBConnection.getInstance().getConnection();
                        PreparedStatement pstm = connection.prepareStatement("DELETE FROM Batch WHERE batchName='"+batchName+"'");
                        int value = pstm.executeUpdate();
                        if (value>0){
                            Alert a = new Alert(Alert.AlertType.INFORMATION, "Deleted Successfully", ButtonType.OK);
                            a.show();
                        }
                        else{
                            Alert a = new Alert(Alert.AlertType.INFORMATION, "Failed to Delete", ButtonType.OK);
                            a.show();
                        }
                        getAllBatches();
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(BatchController.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (SQLException ex) {
                        Logger.getLogger(BatchController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            
        });
    }    

    public void getAllBatches(){
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            Statement stm = connection.createStatement();
            ResultSet rst = stm.executeQuery("SELECT * FROM batch");
            ArrayList<BatchTM> batchList = new ArrayList<>();
            
            while (rst.next()){
                String batchName = rst.getString(1);
                int fee = rst.getInt(2);
                String startDate = rst.getString(3);
                String endDate = rst.getString(4);
                BatchTM batchTM= new BatchTM(batchName, fee, startDate, endDate, "Edit", "Remove");
                batchList.add(batchTM);
            }
            tblBatch.setItems(FXCollections.observableArrayList(batchList));
        } catch (SQLException ex) {
            Logger.getLogger(BatchController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(BatchController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    @FXML
    private void txtSearchBatchOnTextChanged(InputMethodEvent event) {
        
    }

    @FXML
    private void btnAddBatchMouseClick(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/lk/ijse/srs/view/AddBatch.fxml"));
            Parent p = loader.load();
            AddBatchController controller = loader.<AddBatchController>getController();
            controller.initVariable(batchControllerAddress);

            Scene scene = new Scene(p);
            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(BatchController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void btnBackMouseClick(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/lk/ijse/srs/view/Dashboard.fxml"));
            Parent parent = loader.load();
            Scene scene = new Scene(parent);
            Stage stage = (Stage) root.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException ex) {
            Logger.getLogger(BatchController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
}
