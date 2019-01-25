package com.aomc.coop.model;

import java.sql.Timestamp;

public abstract class TimeStampClass {

    private Timestamp reg_date;
    private Timestamp access_date;
    private Timestamp update_date;

    public Timestamp getReg_date() {
        return reg_date;
    }
    public void setReg_date(Timestamp reg_date) {
        this.reg_date = reg_date;
    }
    public Timestamp getAccess_date() {
        return access_date;
    }
    public void setAccess_date(Timestamp access_date) {
        this.access_date = access_date;
    }
    public Timestamp getUpdate_date() {
        return update_date;
    }
    public void setUpdate_date(Timestamp update_date) {
        this.update_date = update_date;
    }

}
