package app.techland.zmadminapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter_LifecycleAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import app.techland.zmadminapp.Models.SmartPhoneCompanyListModel;

public class SmartPhone extends AppCompatActivity {
    RecyclerView mRecView;
    DatabaseReference mDbRf;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_phone);
        mRecView = findViewById(R.id.mRecView);

        progressDialog = new ProgressDialog(SmartPhone.this) ;
        progressDialog.setMessage("Going Good(Loading)...");
        mDbRf = FirebaseDatabase.getInstance().getReference("BrandName");
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 1);
        mRecView.setLayoutManager(layoutManager);
        progressDialog.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<SmartPhoneCompanyListModel> options = new FirebaseRecyclerOptions.Builder<SmartPhoneCompanyListModel>()
                .setQuery(mDbRf, SmartPhoneCompanyListModel.class)
                .build();

        final FirebaseRecyclerAdapter<SmartPhoneCompanyListModel,SPModelViewHolder > adapter = new FirebaseRecyclerAdapter<SmartPhoneCompanyListModel, SPModelViewHolder>(options) {
        @Override
        protected void onBindViewHolder (@NonNull SPModelViewHolder viewHolder,final int i, @NonNull SmartPhoneCompanyListModel model){

            viewHolder.mTextView.setText(model.getCompanyname());
            progressDialog.dismiss();
            viewHolder.mDelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseReference ref = getRef(i);
                    ref.removeValue();
                }
            });
            viewHolder.mTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String BrandName=model.getCompanyname();
                    Intent intent=new Intent(getApplicationContext(),SmartPhoneProductList.class);
                    intent.putExtra("brand_name", BrandName);
                    startActivity(intent);
                }
            });
        }

        @NonNull
        @Override
        public SPModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dummy_smart_phone_company_list , parent, false);
            SPModelViewHolder viewHolder = new SPModelViewHolder(view);
            return viewHolder;
        }

    };
        mRecView.setAdapter(adapter);
        adapter.startListening();
}
    public static class SPModelViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView,mDelBtn;

        public SPModelViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.mTextView);
            mDelBtn = itemView.findViewById(R.id.mDelBtn);
        }
    }
}