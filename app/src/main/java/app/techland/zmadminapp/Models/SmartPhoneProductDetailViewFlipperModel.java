package app.techland.zmadminapp.Models;

public class SmartPhoneProductDetailViewFlipperModel {
String text,img;

    public SmartPhoneProductDetailViewFlipperModel() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public SmartPhoneProductDetailViewFlipperModel(String text, String img) {
        this.text = text;
        this.img = img;
    }
}
