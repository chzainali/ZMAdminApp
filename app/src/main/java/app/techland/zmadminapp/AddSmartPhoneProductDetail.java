package app.techland.zmadminapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ViewUtils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import app.techland.zmadminapp.Models.SmartPhoneProductDetailTextModel;
import app.techland.zmadminapp.Models.SmartPhoneProductDetailViewFlipperModel;

public class AddSmartPhoneProductDetail extends AppCompatActivity {
    private static final int RC_NAV_PHOTO_PICKER = 345;
    private Uri selectedUri;
    ImageView mImageView;
    EditText mVFET;
    String mViewFlipperStr;
    Button mSelectVFBtn, mSubmitVFBtn;
    ProgressBar mProgressBar;
    StorageReference mVFStorageRef;
    StorageReference storageReference;
    DatabaseReference DBVFRef;

    EditText mNameET,mFPriceET,mTPriceET, mStorageET, mColorET, mFCameraET, mBCameraET, mBatteryET;
    String mNameStr,mFPriceStr,mTPriceStr, mStorageStr, mColorStr, mFCameraStr, mBCameraStr, mBatteryStr;
    Button mSubmitBtn;
    DatabaseReference DBRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_smart_phone_product_detail);
        mImageView = findViewById(R.id.mImageView);
        mVFET = findViewById(R.id.mVFET);
        mSelectVFBtn = findViewById(R.id.mSelectVFBtn);
        mSubmitVFBtn = findViewById(R.id.mSubmitVFBtn);
        mProgressBar = findViewById(R.id.mProgressBar);

        mNameET = findViewById(R.id.mNameET);
        mFPriceET = findViewById(R.id.mFPriceET);
        mTPriceET = findViewById(R.id.mTPriceET);
        mStorageET = findViewById(R.id.mStorageET);
        mColorET = findViewById(R.id.mColorET);
        mFCameraET = findViewById(R.id.mFCameraET);
        mBCameraET = findViewById(R.id.mBCameraET);
        mBatteryET = findViewById(R.id.mBatteryET);
        mSubmitBtn = findViewById(R.id.mSubmitBtn);

        //For View Flipper

        mSelectVFBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_NAV_PHOTO_PICKER);
            }
        });
        DBVFRef = FirebaseDatabase.getInstance().getReference("SmartPhoneVF").child("-MLNvOG0LIoSbVhiQfpC");
        mVFStorageRef = FirebaseStorage.getInstance().getReference("SmartPhoneVFPictures");
        mSubmitVFBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressBar.setVisibility(View.VISIBLE);
                mViewFlipperStr = mVFET.getText().toString();
                storageReference = mVFStorageRef.child(selectedUri.getLastPathSegment());
                storageReference.putFile(selectedUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String uploadImgURL = uri.toString();
                                SmartPhoneProductDetailViewFlipperModel model = new SmartPhoneProductDetailViewFlipperModel(mViewFlipperStr, uploadImgURL);
                                DBVFRef.push().setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        mProgressBar.setVisibility(View.GONE);
                                        Toast.makeText(AddSmartPhoneProductDetail.this, "Add Successfully", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(AddSmartPhoneProductDetail.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AddSmartPhoneProductDetail.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddSmartPhoneProductDetail.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

     // for Text Description

        DBRef = FirebaseDatabase.getInstance().getReference("SmartPhoneProductDetails");
        mSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressBar.setVisibility(View.VISIBLE);
                mNameStr = mNameET.getText().toString();
                mFPriceStr = mFPriceET.getText().toString();
                mTPriceStr = mTPriceET.getText().toString();
                mStorageStr = mStorageET.getText().toString();
                mColorStr = mColorET.getText().toString();
                mFCameraStr = mFCameraET.getText().toString();
                mBCameraStr = mBCameraET.getText().toString();
                mBatteryStr = mBatteryET.getText().toString();
                SmartPhoneProductDetailTextModel model1 = new SmartPhoneProductDetailTextModel(mNameStr,mFPriceStr,
                        mTPriceStr,mStorageStr,mColorStr, mFCameraStr,mBCameraStr,mBatteryStr);
                DBRef.push().setValue(model1).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                   mProgressBar.setVisibility(View.GONE);
                        Toast.makeText(AddSmartPhoneProductDetail.this, "Add Successfully", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddSmartPhoneProductDetail.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            selectedUri = data.getData();
            if (selectedUri != null) {
                mImageView.setImageURI(selectedUri);
                mImageView.setVisibility(View.VISIBLE);
            }
        }
    }
}