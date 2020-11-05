package app.techland.zmadminapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
Button mAdminViewPanel,mAdminAddPanel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAdminViewPanel = findViewById(R.id.mAdminViewPanel);
        mAdminAddPanel = findViewById(R.id.mAdminAddPanel);
        mAdminViewPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AdminViewPanel.class));
            }
        });
        mAdminAddPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AdminAddPanel.class));

            }
        });
    }
}