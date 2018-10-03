/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.srs.model;

/**
 *
 * @author Ilman Iqbal
 */
public class CourseTM {
    private String courseName;
    private String update;
    private String remove;

    public CourseTM() {
    }

    public CourseTM(String courseName, String update, String remove) {
        this.courseName = courseName;
        this.update = update;
        this.remove = remove;
    }

    /**
     * @return the courseName
     */
    public String getCourseName() {
        return courseName;
    }

    /**
     * @param courseName the courseName to set
     */
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    /**
     * @return the update
     */
    public String getUpdate() {
        return update;
    }

    /**
     * @param update the update to set
     */
    public void setUpdate(String update) {
        this.update = update;
    }

    /**
     * @return the remove
     */
    public String getRemove() {
        return remove;
    }

    /**
     * @param remove the remove to set
     */
    public void setRemove(String remove) {
        this.remove = remove;
    }

    @Override
    public String toString() {
        return "Course{" + "courseName=" + courseName + ", update=" + update + ", remove=" + remove + '}';
    }
    
}
