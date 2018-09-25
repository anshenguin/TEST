package com.kinitoapps.punjabgovttest;

/**
 * Created by HP INDIA on 14-Sep-18.
 */

public class Jobs {

    private String name,companyName,logo,info,date,ID;

    public Jobs(String name, String companyName, String logo, String ID){
        this.name = name; this.companyName = companyName;
        this.logo = logo;
        this.ID = ID;
    }

    public Jobs(String name, String companyName, String logo, String info, String date){
        this.name = name;
        this.companyName = companyName;
        this.logo = logo;
        this.info = info;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
}
