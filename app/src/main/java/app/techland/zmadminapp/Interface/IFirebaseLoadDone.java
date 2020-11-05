package app.techland.zmadminapp.Interface;

import java.util.List;

import app.techland.zmadminapp.Models.SmartPhoneProductDetailViewFlipperModel;

public interface IFirebaseLoadDone {
    void onFirebaseLoadSuccess(List<SmartPhoneProductDetailViewFlipperModel> imgList);
    void onFirebaseLoadFailed(String message);
}
