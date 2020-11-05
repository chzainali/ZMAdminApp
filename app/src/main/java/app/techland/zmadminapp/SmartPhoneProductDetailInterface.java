package app.techland.zmadminapp;

import java.util.List;

import app.techland.zmadminapp.Models.SmartPhoneProductDetailViewFlipperModel;

public interface SmartPhoneProductDetailInterface {
    void onFirebaseLoadSuccess(List<SmartPhoneProductDetailViewFlipperModel> imgList);

    void onFirebaseLoadFailed(String message);
}
