package app.techland.zmadminapp.Models;

public class SmartPhoneProductDetailTextModel {
   String name,fprice,tprice,storage,color,fcamera,bcamera,battery;

    public SmartPhoneProductDetailTextModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFprice() {
        return fprice;
    }

    public void setFprice(String fprice) {
        this.fprice = fprice;
    }

    public String getTprice() {
        return tprice;
    }

    public void setTprice(String tprice) {
        this.tprice = tprice;
    }

    public String getStorage() {
        return storage;
    }

    public void setStorage(String storage) {
        this.storage = storage;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getFcamera() {
        return fcamera;
    }

    public void setFcamera(String fcamera) {
        this.fcamera = fcamera;
    }

    public String getBcamera() {
        return bcamera;
    }

    public void setBcamera(String bcamera) {
        this.bcamera = bcamera;
    }

    public String getBattery() {
        return battery;
    }

    public void setBattery(String battery) {
        this.battery = battery;
    }

    public SmartPhoneProductDetailTextModel(String name, String fprice, String tprice, String storage, String color, String fcamera, String bcamera, String battery) {
        this.name = name;
        this.fprice = fprice;
        this.tprice = tprice;
        this.storage = storage;
        this.color = color;
        this.fcamera = fcamera;
        this.bcamera = bcamera;
        this.battery = battery;
    }
}