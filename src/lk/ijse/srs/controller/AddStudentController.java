/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.srs.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.Window;
import lk.ijse.srs.db.DBConnection;

/**
 * FXML Controller class
 *
 * @author Ilman Iqbal
 */
public class AddStudentController implements Initializable {

    @FXML
    private JFXButton btnSave;
    @FXML
    private JFXTextField txtNic;
    @FXML
    private JFXTextField txtName;
    @FXML
    private JFXTextField txtAddress;
    @FXML
    private JFXTextField txtContact;
    @FXML
    private JFXButton btnClear;
    @FXML
    private ImageView btnClose;
    
    private StudentController studentControllerAddress;
    @FXML
    private JFXComboBox<String> cmbCourse;
    @FXML
    private JFXComboBox<String> cmbBatch;
    @FXML
    private AnchorPane root;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadCourses();
    }    
    
    public void initVariable(StudentController studentController){
        studentControllerAddress = studentController;
    }
    
    private void loadCourses(){
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            Statement stm = connection.createStatement();
            ResultSet rst = stm.executeQuery("SELECT name FROM course");
            ArrayList<String> courseNames= new ArrayList<>();
            while(rst.next()){
                courseNames.add(rst.getString(1));
            }
            cmbCourse.setItems(FXCollections.observableArrayList(courseNames));
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(AddStudentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void loadBatches(){
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            Statement stm = connection.createStatement();
            ResultSet rst = stm.executeQuery("SELECT batchName FROM course c, batch b WHERE c.name = '"+cmbCourse.getValue()+"' AND c.courseId = b.courseId");
            ArrayList<String> batchNames = new ArrayList<>();
            if(rst.next()){
                batchNames.add(rst.getString(1));
            }
            cmbBatch.setItems(FXCollections.observableArrayList(batchNames));
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(AddStudentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    private void btnSaveOnClick(ActionEvent event) {
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            try {
                
                connection.setAutoCommit(false);
                PreparedStatement pstm = connection.prepareStatement("INSERT INTO student VALUES (?,?,?,?)");
                pstm.setObject(1, txtNic.getText());
                pstm.setObject(2, txtName.getText());
                pstm.setObject(3, txtAddress.getText());
                pstm.setObject(4, txtContact.getText());
                boolean transaction = pstm.executeUpdate()>0;
                if (transaction){
                    PreparedStatement pstm2 = connection.prepareStatement("INSERT INTO register VALUES (?,?)");
                    pstm2.setObject(1, txtNic.getText());
                    pstm2.setObject(2, cmbBatch.getValue());
                    boolean transaction2 = pstm2.executeUpdate()>0;
                    if (transaction2){
                        connection.commit();
                    }
                }
                connection.rollback();
                
            } 
            finally{
                connection.setAutoCommit(true);
                studentControllerAddress.getAllStudents();
                btnCloseOnMouseClicked(null);
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(AddStudentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void btnClearOnClick(ActionEvent event) {
        txtNic.setText("");
        txtName.setText("");
        txtAddress.setText("");
        txtContact.setText("");
        cmbCourse.getSelectionModel().clearSelection();
        cmbBatch.getSelectionModel().clearSelection();
    }


    @FXML
    private void btnCloseOnMouseClicked(MouseEvent event) {
        Stage stage = (Stage) root.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void cmbCourseOnAction(ActionEvent event) {
        loadBatches();
        cmbBatch.requestFocus();
    }
    
}
