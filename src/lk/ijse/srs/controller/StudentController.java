/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.srs.controller;

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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lk.ijse.srs.db.DBConnection;
import lk.ijse.srs.model.StudentTM;

/**
 * FXML Controller class
 *
 * @author Ilman Iqbal
 */
public class StudentController implements Initializable {

    @FXML
    private ImageView btnBack;
    @FXML
    private ImageView btnAddStudent;
    @FXML
    private TableView<StudentTM> tblStudent;
    
    private StudentController studentControllerAddress;
    @FXML
    private AnchorPane root;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        studentControllerAddress = this;
        
        tblStudent.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("nic"));
        tblStudent.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("name"));
        tblStudent.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("address"));
        tblStudent.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("contactNo"));
        tblStudent.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("course"));
        tblStudent.getColumns().get(5).setCellValueFactory(new PropertyValueFactory<>("batch"));
        tblStudent.getColumns().get(6).setCellValueFactory(new PropertyValueFactory<>("edit"));
        tblStudent.getColumns().get(7).setCellValueFactory(new PropertyValueFactory<>("remove"));
        
        getAllStudents();
        
         tblStudent.getSelectionModel().getSelectedCells().addListener(new ListChangeListener<TablePosition>(){
            @Override
            public void onChanged(ListChangeListener.Change<? extends TablePosition> c) {
                int column = tblStudent.getSelectionModel().getSelectedCells().get(0).getColumn();
                System.out.println(column);
                if (column == 6){
                    try {
                        StudentTM studentTM = tblStudent.getSelectionModel().getSelectedItem();
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/lk/ijse/srs/view/EditStudent.fxml"));
                        Parent p = loader.load();
                        EditStudentController controller = loader.<EditStudentController>getController();
                        controller.initVariable(studentTM, studentControllerAddress);
                        
                        Scene scene = new Scene(p);
                        Stage stage = new Stage();
                        stage.setScene(scene);
                        stage.show();
                    } catch (IOException ex) {
                        Logger.getLogger(StudentController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                else if (column == 7){
                    try {
                        String nic = tblStudent.getSelectionModel().getSelectedItem().getNic();
                        Connection connection = DBConnection.getInstance().getConnection();
                        try {
                            connection.setAutoCommit(false);
                            PreparedStatement pstm = connection.prepareStatement("DELETE FROM register WHERE nic='"+nic+"'");
                            boolean transaction = pstm.executeUpdate()>0;
                            if (transaction){
                                PreparedStatement pstm2 = connection.prepareStatement("DELETE FROM student WHERE nic='"+nic+"'");
                                boolean transaction2 = pstm2.executeUpdate()>0;
                                if (transaction2){
                                    connection.commit();
                                    Alert a = new Alert(Alert.AlertType.INFORMATION, "Deleted Successfully", ButtonType.OK);
                                    a.show();
                                }
                                else{
                                    Alert a = new Alert(Alert.AlertType.INFORMATION, "Failed to Delete", ButtonType.OK);
                                    a.show();
                                }
                                connection.rollback();
                                getAllStudents();
                            }
                        } 
                        finally{
                            connection.setAutoCommit(true);
                        }
                    } catch (ClassNotFoundException | SQLException ex) {
                        Logger.getLogger(StudentController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
         });
    }    
    
    public void getAllStudents(){
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            Statement stm = connection.createStatement();
            ResultSet rst = stm.executeQuery("SELECT s.nic, s.name, address, contactNum, c.name, b.batchName FROM student s, register r, batch b, course c WHERE s.nic = r.nic AND r.batchName=b.batchName AND b.courseId = c.courseId");
            ArrayList<StudentTM> allStudents = new ArrayList<>(); 
            while(rst.next()){
                StudentTM studentTM = new StudentTM(rst.getString(1), rst.getString(2), rst.getString(3), rst.getString(4), rst.getString(5),rst.getString(6), "Edit", "Remove");
                allStudents.add(studentTM);
            }
            tblStudent.setItems(FXCollections.observableArrayList(allStudents));
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(StudentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void btnBackKeyClicked(MouseEvent event) {
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

    @FXML
    private void btnAddStudentOnMouseClicked(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/lk/ijse/srs/view/AddStudent.fxml"));
            Parent p = loader.load();
            AddStudentController controller = loader.<AddStudentController>getController();
            controller.initVariable(studentControllerAddress);

            Scene scene = new Scene(p);
            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(StudentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
