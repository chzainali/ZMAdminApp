package app.techland.zmadminapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AddSmartPhoneMainActivity extends AppCompatActivity {
    Button mBtn1,mBtn2,mBtn3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_smart_phone_main);
        mBtn1 = findViewById(R.id.mBtn1);
        mBtn2 = findViewById(R.id.mBtn2);
        mBtn3 = findViewById(R.id.mBtn3);
        mBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),AddSmartPhoneCompanyList.class));
            }
        });
        mBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),AddSmartPhoneProductList.class));
            }
        });
        mBtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),AddSmartPhoneProductDetail.class));
            }
        });
    }
}