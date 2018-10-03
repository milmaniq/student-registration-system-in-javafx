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
public class StudentTM {
    private String nic;
    private String name;
    private String address;
    private String contactNo;
    private String course;
    private String batch;
    private String edit;
    private String remove;

    public StudentTM(String nic, String name, String address, String contactNo, String course, String batch, String edit, String remove) {
        this.nic = nic;
        this.name = name;
        this.address = address;
        this.contactNo = contactNo;
        this.course = course;
        this.batch = batch;
        this.edit = edit;
        this.remove = remove;
    }

    public StudentTM() {
    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getEdit() {
        return edit;
    }

    public void setEdit(String edit) {
        this.edit = edit;
    }

    public String getRemove() {
        return remove;
    }

    public void setRemove(String remove) {
        this.remove = remove;
    }

    
}
