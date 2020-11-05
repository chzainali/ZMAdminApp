package app.techland.zmadminapp.Models;

public class SmartPhoneCompanyListModel {
     String companyname,id;

    public SmartPhoneCompanyListModel() {
    }

    public String getCompanyname() {
        return companyname;
    }

    public void setCompanyname(String companyname) {
        this.companyname = companyname;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public SmartPhoneCompanyListModel(String companyname, String id) {
        this.companyname = companyname;
        this.id = id;
    }
}
