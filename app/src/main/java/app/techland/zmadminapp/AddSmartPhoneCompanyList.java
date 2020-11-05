package app.techland.zmadminapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

import app.techland.zmadminapp.Models.SmartPhoneCompanyListModel;

public class AddSmartPhoneCompanyList extends AppCompatActivity {
    EditText mCompanyListET;
    Button mCompanyListBtn;
    ProgressBar mProgressBar;
    DatabaseReference DbCompListRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_smart_phone_company_list);
        mCompanyListET = findViewById(R.id.mCompanyListET);
        mCompanyListBtn = findViewById(R.id.mCompanyListBtn);
        mProgressBar = findViewById(R.id.mProgressBar);
        DbCompListRef = FirebaseDatabase.getInstance().getReference("SmartPhoneCompanyList");

        mCompanyListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressBar.setVisibility(View.VISIBLE);
                String mCompanyListStr = mCompanyListET.getText().toString();
                String idStr = DbCompListRef.push().getKey(); //getTaskId();
                SmartPhoneCompanyListModel model = new SmartPhoneCompanyListModel(mCompanyListStr,idStr);
                DbCompListRef.child(idStr).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        mProgressBar.setVisibility(View.GONE);
                        Toast.makeText(AddSmartPhoneCompanyList.this, "Add Successfully", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddSmartPhoneCompanyList.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}