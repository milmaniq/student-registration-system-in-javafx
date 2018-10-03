/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.srs.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.io.PrintStream;
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
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import lk.ijse.srs.db.DBConnection;
import lk.ijse.srs.model.CourseTM;

/**
 * FXML Controller class
 *
 * @author Ilman Iqbal
 */
public class CourseController implements Initializable {

    @FXML
    private JFXButton btnSave;
    @FXML
    private JFXTextField txtCourseName;
    @FXML
    private JFXButton btnClear;
    @FXML
    private ImageView btnBack;
    @FXML
    private TableView<CourseTM> tblCourses;

    private boolean isUpdateCourse;
    private int CourseId;
    @FXML
    private AnchorPane root;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        //mapping the table columns in the form with the table model
        tblCourses.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("courseName"));
        tblCourses.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("update"));
        tblCourses.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("remove"));

        getAllCourses();

        tblCourses.getSelectionModel().getSelectedCells().addListener(new ListChangeListener<TablePosition>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends TablePosition> c) {
                int column = tblCourses.getSelectionModel().getSelectedCells().get(0).getColumn();
                if (column == 1) {
                    try {
                        String courseName = tblCourses.getSelectionModel().getSelectedItem().getCourseName();
                        txtCourseName.setText(courseName);
                        Connection connection = DBConnection.getInstance().getConnection();
                        Statement stm = connection.createStatement();
                        ResultSet rst = stm.executeQuery("SELECT courseId FROM course WHERE name = '" + courseName + "'");
                        if (rst.next()) {
                            CourseId = rst.getInt("courseId");
                            isUpdateCourse = true;
                        }
                    } catch (ClassNotFoundException | SQLException ex) {
                        Logger.getLogger(CourseController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else if (column == 2) {
                    try {
                        String courseName = tblCourses.getSelectionModel().getSelectedItem().getCourseName();
                        Connection connection = DBConnection.getInstance().getConnection();
                        PreparedStatement pstm = connection.prepareStatement("DELETE FROM course WHERE name='" + courseName + "'");
                        int value = pstm.executeUpdate();
                        if (value > 0) {
                            Alert a = new Alert(Alert.AlertType.INFORMATION, "Successfully Deleted", ButtonType.OK);
                            a.show();
                        } else {
                            Alert a = new Alert(Alert.AlertType.ERROR, "Failed to Deleted", ButtonType.OK);
                            a.show();
                        }
                        getAllCourses();
                    } catch (SQLException | ClassNotFoundException ex) {
                        Logger.getLogger(CourseController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });

    }

    private void getAllCourses() {
        try {
            //the table comes with an observable list. 
            //an observable list can be regarded as rows in a column
            //initially there are no items in the table

            //ObservableList<CourseTM> items = tblCourses.getItems();
            //items.removeAll(items);
            Connection connection = DBConnection.getInstance().getConnection();
            Statement stm = connection.createStatement();
            ResultSet rst = stm.executeQuery("SELECT name FROM course");
            ArrayList<CourseTM> items = new ArrayList<>();
            while (rst.next()) {
                String courseName = rst.getString("name");

                //a new course table model is created and it is added to the observable list
                CourseTM courseTM = new CourseTM(courseName, "Update", "Remove");
                items.add(courseTM);
            }
            tblCourses.setItems(FXCollections.observableArrayList(items));
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(CourseController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    private void btnSaveOnClick(ActionEvent event) {
        String courseName = txtCourseName.getText();
        if (!isUpdateCourse) {
            try {
                Connection connection = DBConnection.getInstance().getConnection();
                PreparedStatement pstm = connection.prepareStatement("INSERT INTO course(name) VALUES (?)");
                pstm.setObject(1, courseName);
                int value = pstm.executeUpdate();
                if (value > 0) {
                    Alert a = new Alert(Alert.AlertType.INFORMATION, "Successfully Inserted", ButtonType.OK);
                    a.show();
                } else {
                    Alert a = new Alert(Alert.AlertType.ERROR, "Failed to Insert", ButtonType.OK);
                    a.show();
                }
                getAllCourses();
            } catch (ClassNotFoundException | SQLException ex) {
                Logger.getLogger(CourseController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {
                Connection connection = DBConnection.getInstance().getConnection();
                PreparedStatement pstm = connection.prepareStatement("UPDATE course SET name=? WHERE courseId=?");
                pstm.setObject(1, courseName);
                pstm.setObject(2, CourseId);
                int value = pstm.executeUpdate();
                if (value > 0) {
                    getAllCourses();
                    Alert a = new Alert(Alert.AlertType.INFORMATION, "Successfully Updated", ButtonType.OK);
                    a.show();
                } else {
                    Alert a = new Alert(Alert.AlertType.INFORMATION, "Failed to Update", ButtonType.OK);
                    a.show();
                }
                isUpdateCourse = false;
            } catch (ClassNotFoundException | SQLException ex) {
                Logger.getLogger(CourseController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        txtCourseName.setText("");
    }

    @FXML
    private void btnClearOnClick(ActionEvent event) {
        txtCourseName.setText("");
    }

    @FXML
    private void btnBackKeyClicked(MouseEvent event) {
        try {
            Parent p = FXMLLoader.load(this.getClass().getResource("/lk/ijse/srs/view/Dashboard.fxml"));
            Scene scene = new Scene(p);
            Stage stage = (Stage) root.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException ex) {
            Logger.getLogger(CourseController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void txtCourseNameOnTextChanged(KeyEvent event) {
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            Statement stm = connection.createStatement();
            ResultSet rst = stm.executeQuery("SELECT name from course WHERE name LIKE '" + txtCourseName.getText() + "%'");
            ArrayList<CourseTM> batchNames = new ArrayList<>();
            while (rst.next()) {
                CourseTM temp = new CourseTM();
                temp.setCourseName(rst.getString(1));
                System.out.println("Couse  Name:" + rst.getString(1));
                batchNames.add(temp);
            }
            
            tblCourses.setItems(FXCollections.observableArrayList(batchNames));

        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(CourseController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
