package com.kinitoapps.punjabgovttest;

/**
 * Created by HP INDIA on 14-Sep-18.
 */

public class Jobs {

    private String name,companyName;

    public Jobs(String name, String companyName){
        this.name = name; this.companyName = companyName;
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
}
