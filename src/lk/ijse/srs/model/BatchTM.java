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
public class BatchTM {
    private String batchName;
    private int fee;
    private String startDate;
    private String endDate;
    private String edit;
    private String remove;

    public BatchTM(String batchName, int fee, String startDate, String endDate, String edit, String remove) {
        this.batchName = batchName;
        this.fee = fee;
        this.startDate = startDate;
        this.endDate = endDate;
        this.edit = edit;
        this.remove = remove;
    }

    public BatchTM() {
    }

    /**
     * @return the batchName
     */
    public String getBatchName() {
        return batchName;
    }

    /**
     * @param batchName the batchName to set
     */
    public void setBatchName(String batchName) {
        this.batchName = batchName;
    }

    /**
     * @return the fee
     */
    public int getFee() {
        return fee;
    }

    /**
     * @param fee the fee to set
     */
    public void setFee(int fee) {
        this.fee = fee;
    }

    /**
     * @return the startDate
     */
    public String getStartDate() {
        return startDate;
    }

    /**
     * @param startDate the startDate to set
     */
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    /**
     * @return the endDate
     */
    public String getEndDate() {
        return endDate;
    }

    /**
     * @param endDate the endDate to set
     */
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    /**
     * @return the edit
     */
    public String getEdit() {
        return edit;
    }

    /**
     * @param edit the edit to set
     */
    public void setEdit(String edit) {
        this.edit = edit;
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
    
}
