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
import lk.ijse.srs.db.DBConnection;
import lk.ijse.srs.model.StudentTM;

/**
 * FXML Controller class
 *
 * @author Ilman Iqbal
 */
public class EditStudentController implements Initializable {

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

    @FXML
    private JFXComboBox<String> cmbCourse;
    @FXML
    private JFXComboBox<String> cmbBatch;

    @FXML
    private AnchorPane root;

    private StudentController studentControllerAddress;
    private int courseId;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        txtNic.setDisable(true);
        cmbCourse.setEditable(true);
        cmbBatch.setEditable(true);        
    }

    public void initVariable(StudentTM studentTM, StudentController studentController) {
        studentControllerAddress = studentController;

        txtNic.setText(studentTM.getNic());
        txtName.setText(studentTM.getName());
        txtAddress.setText(studentTM.getAddress());
        txtContact.setText(studentTM.getContactNo());
        cmbCourse.setValue(studentTM.getCourse());
        cmbBatch.setValue(studentTM.getBatch());
        
        loadCourses();
        loadBatches();
    }
    
    private void loadCourses() {
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            Statement stm = connection.createStatement();
            ResultSet rst = stm.executeQuery("SELECT name FROM Course");
            ArrayList<String> courseName = new ArrayList<>();
            while (rst.next()) {
                courseName.add(rst.getString(1));
            }
            if (!cmbCourse.getValue().isEmpty()) {
                //courseName.remove(cmbCourse.getValue());
            }
            cmbCourse.setItems(FXCollections.observableArrayList(courseName));

        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(AddBatchController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadBatches() {
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            Statement stm = connection.createStatement();
            ResultSet rst = stm.executeQuery("SELECT courseId FROM Course WHERE name='" + cmbCourse.getValue() + "'");
            if (rst.next()) {
                courseId = rst.getInt("courseId");
                Statement stm2 = connection.createStatement();
                ResultSet rst2 = stm2.executeQuery("SELECT batchName FROM batch WHERE courseId = '" + courseId + "'");
                ArrayList<String> batchNames = new ArrayList<>();
                while (rst2.next()) {
                    batchNames.add(rst2.getString("batchName"));
                }
                cmbBatch.setItems(FXCollections.observableArrayList(batchNames));
            }
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(EditStudentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void btnSaveOnClick(ActionEvent event) {
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            try {
                connection.setAutoCommit(false);
                PreparedStatement pstm1 = connection.prepareStatement("UPDATE student SET name=?, address=?, contactNum=? WHERE NIC=?");
                pstm1.setObject(1, txtName.getText());
                pstm1.setObject(2, txtAddress.getText());
                pstm1.setObject(3, txtContact.getText());
                pstm1.setObject(4, txtNic.getText());
                boolean transaction1 = pstm1.executeUpdate() > 0;
                if (transaction1) {
                    PreparedStatement pstm2 = connection.prepareStatement("UPDATE register SET batchName = ? WHERE nic = ?");
                    pstm2.setObject(1, cmbBatch.getSelectionModel().getSelectedItem());
                    pstm2.setObject(2, txtNic.getText());
                    boolean transacion2 = pstm2.executeUpdate() > 0;
                    if (transacion2) {
                        connection.commit();
                    }
                }
                connection.rollback();
            } finally {
                connection.setAutoCommit(true);
                studentControllerAddress.getAllStudents();
                btnCloseOnMouseClicked(null);
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(EditStudentController.class.getName()).log(Level.SEVERE, null, ex);
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
    }


    


}
