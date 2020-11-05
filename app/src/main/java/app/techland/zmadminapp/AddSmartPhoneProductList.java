package app.techland.zmadminapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import app.techland.zmadminapp.Models.SmartPhoneProductListModel;

public class AddSmartPhoneProductList extends AppCompatActivity {
    private static final int RC_NAV_PHOTO_PICKER = 345;
EditText mSPNameET,mSPFPriceET,mSPTPriceET;
String mSPNameStr,mSPFPriceStr,mSPTPriceStr;
ImageView mImg;
ProgressBar mProgressBar;
Button mSelecImgBtn,mBtn;

    private Uri selectedUri;
    StorageReference StrSPProductListRef;
    DatabaseReference DbSPProductListRef;
    StorageReference storageReference;

    DatabaseReference DbCompListRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_smart_phone_product_list);
        mSPNameET =findViewById(R.id.mSPNameET);
        mSPFPriceET =findViewById(R.id.mSPFPriceET);
        mSPTPriceET=findViewById(R.id.mSPTPriceET);
        mImg =findViewById(R.id.mImg);
        mSelecImgBtn =findViewById(R.id.mSelecImgBtn);
        mBtn =findViewById(R.id.mBtn);

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
        DbCompListRef = FirebaseDatabase.getInstance().getReference("SmartPhoneCompanyList");
        GetCompanyList();

        DbSPProductListRef = FirebaseDatabase.getInstance().getReference("SmartPhoneProductList");
        StrSPProductListRef = FirebaseStorage.getInstance().getReference("SmartPhoneProductPictureList");
        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressBar.setVisibility(View.VISIBLE);
                mSPNameStr = mSPNameET.getText().toString();
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
        SmartPhoneProductListModel model = new SmartPhoneProductListModel(mSPNameStr,mSPTPriceStr,mSPFPriceStr,uploadedImgURL);
DbSPProductListRef.push().setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
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
                mImg.setImageURI(selectedUri);
                mImg.setVisibility(View.VISIBLE);
            }
        }
    }
    void GetCompanyList(){
        DbCompListRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String string = dataSnapshot.child("MLLm0QcyPlZS1wq0iDt").child("companyname").toString();

                Toast.makeText(AddSmartPhoneProductList.this, string, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}