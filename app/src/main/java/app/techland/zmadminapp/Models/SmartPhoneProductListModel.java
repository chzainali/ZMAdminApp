package app.techland.zmadminapp.Models;

public class SmartPhoneProductListModel {
    String name;
    String falseprice;
    String trueprice;


    public SmartPhoneProductListModel() {
    }

    public SmartPhoneProductListModel(String name, String falseprice, String trueprice, String img, String id) {
        this.name = name;
        this.falseprice = falseprice;
        this.trueprice = trueprice;
        this.img = img;
        this.id = id;
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

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    String img;
    String id;


}
