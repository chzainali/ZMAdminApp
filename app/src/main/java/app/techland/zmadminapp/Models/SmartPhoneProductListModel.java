package app.techland.zmadminapp.Models;

public class SmartPhoneProductListModel {
    String name,falseprice,trueprice,img;

    public SmartPhoneProductListModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFalseprice() {
        return falseprice;
    }

    public void setFalseprice(String falseprice) {
        this.falseprice = falseprice;
    }

    public String getTrueprice() {
        return trueprice;
    }

    public void setTrueprice(String trueprice) {
        this.trueprice = trueprice;
    }

    public  String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public SmartPhoneProductListModel(String name, String falseprice, String trueprice, String img) {
        this.name = name;
        this.falseprice = falseprice;
        this.trueprice = trueprice;
        this.img = img;
    }
}
