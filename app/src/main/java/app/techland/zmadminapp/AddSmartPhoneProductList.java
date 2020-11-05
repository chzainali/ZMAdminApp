package app.techland.zmadminapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

import app.techland.zmadminapp.Models.SmartPhoneCompanyListModel;
import app.techland.zmadminapp.Models.SmartPhoneProductDetailViewFlipperModel;
import app.techland.zmadminapp.Models.SmartPhoneProductListModel;

public class AddSmartPhoneProductList extends AppCompatActivity {
    private static final int RC_NAV_PHOTO_PICKER = 345;
    EditText mSPFPriceET, mSPTPriceET;
    Spinner mSPNameSpnr;
    String mSPNameStr, mSPFPriceStr, mSPTPriceStr;
    ImageView mImg;
    ProgressBar mProgressBar;
    StorageReference mVFStorageRef;
    StorageReference storageReference;
    DatabaseReference DBVFRef;
    String pushid;
    ImageView mImageView;
    EditText mVFET;
    Button mSelectVFBtn, mSubmitVFBtn;
    ProgressBar mProgressBar1;

    Button mSelecImgBtn, mBtn;
    List<String> BrandNames, BrandsIds;
    int SelectedBrandArrayPosition;

    private Uri selectedUri;
    StorageReference StrSPProductListRef;
    DatabaseReference DbSPProductListRef;
    StorageReference storageReference1;

    DatabaseReference DbCompListRef;
    private String mViewFlipperStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_smart_phone_product_list);
        DbCompListRef = FirebaseDatabase.getInstance().getReference("BrandName");
        DbSPProductListRef =FirebaseDatabase.getInstance().getReference("PhoneName");
        StrSPProductListRef = FirebaseStorage.getInstance().getReference("PhonePicture");
        pushid=DbCompListRef.push().getKey();

        mSPNameSpnr = findViewById(R.id.mSPNameSpnr);
        mSPFPriceET = findViewById(R.id.mSPFPriceET);
        mSPTPriceET = findViewById(R.id.mSPTPriceET);
        mImg = findViewById(R.id.mImg);
        mSelecImgBtn = findViewById(R.id.mSelecImgBtn);

        mImageView = findViewById(R.id.mImageView);
        mVFET = findViewById(R.id.mVFET);
        mSelectVFBtn = findViewById(R.id.mSelectVFBtn);
        mSubmitVFBtn = findViewById(R.id.mSubmitVFBtn);
        mProgressBar1 = findViewById(R.id.mProgressBar1);
        BrandNames = new ArrayList<String>();
        BrandsIds = new ArrayList<String>();
        mBtn = findViewById(R.id.mBtn);
        GetCompanyList();
        mSPNameSpnr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mSPNameStr=adapterView.getItemAtPosition(i).toString();
                SelectedBrandArrayPosition=i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mSelecImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_NAV_PHOTO_PICKER);
            }
        });

        mProgressBar = findViewById(R.id.mProgressBar);


        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressBar.setVisibility(View.VISIBLE);

                mSPTPriceStr = mSPTPriceET.getText().toString();
                mSPFPriceStr = mSPFPriceET.getText().toString();

                storageReference = StrSPProductListRef.child(selectedUri.getLastPathSegment());
                storageReference.putFile(selectedUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String uploadedImgURL = uri.toString();
                                SmartPhoneProductListModel model = new SmartPhoneProductListModel(mSPNameStr, mSPTPriceStr, mSPFPriceStr, uploadedImgURL, pushid);
                                DbSPProductListRef.child(mSPNameStr).child(pushid).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        mProgressBar.setVisibility(View.GONE);
                                        Toast.makeText(AddSmartPhoneProductList.this, "Add Successfully", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(AddSmartPhoneProductList.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AddSmartPhoneProductList.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddSmartPhoneProductList.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        mSelectVFBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_NAV_PHOTO_PICKER);
            }
        });
        DBVFRef = FirebaseDatabase.getInstance().getReference("SmartPhoneVF");
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
                                DBVFRef.child(pushid).push().setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        mProgressBar.setVisibility(View.GONE);
                                        Toast.makeText(AddSmartPhoneProductList.this, "Add Successfully", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(AddSmartPhoneProductList.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AddSmartPhoneProductList.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddSmartPhoneProductList.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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

    void GetCompanyList() {
        DbCompListRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    SmartPhoneCompanyListModel post = postSnapshot.getValue(SmartPhoneCompanyListModel.class);
                    BrandNames.add(post.getCompanyname());
                    BrandsIds.add(post.getId());

//                    Toast.makeText(AddSmartPhoneProductList.this, "" + BrandsIds, Toast.LENGTH_SHORT).show();
//            Log.e("Get Data", post.<YourMethod>());
                }
                ArrayAdapter<String> a =new ArrayAdapter<String>(AddSmartPhoneProductList.this,android.R.layout.simple_spinner_item, BrandNames);
                a.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mSPNameSpnr.setAdapter(a);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    
    
}