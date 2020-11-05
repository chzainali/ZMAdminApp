package app.techland.zmadminapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import app.techland.zmadminapp.Models.SmartPhoneCompanyListModel;
import app.techland.zmadminapp.Models.SmartPhoneProductListModel;

public class SmartPhoneProductList extends AppCompatActivity {
    RecyclerView mRecView;
    DatabaseReference mDbRf;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_phone_product_list);
        mRecView = findViewById(R.id.mRecView);

        progressDialog = new ProgressDialog(SmartPhoneProductList.this) ;
        progressDialog.setMessage("Going Good(Loading)...");
        mDbRf = FirebaseDatabase.getInstance().getReference("SmartPhoneProductList");
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 2);
        mRecView.setLayoutManager(layoutManager);
        progressDialog.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<SmartPhoneProductListModel> options = new FirebaseRecyclerOptions.Builder<SmartPhoneProductListModel>()
                .setQuery(mDbRf, SmartPhoneProductListModel.class)
                .build();

        final FirebaseRecyclerAdapter<SmartPhoneProductListModel, SPModelViewHolder> adapter = new FirebaseRecyclerAdapter<SmartPhoneProductListModel, SPModelViewHolder>(options) {
            @Override
                        protected void onBindViewHolder(@NonNull SPModelViewHolder modelViewHolder, int i, @NonNull SmartPhoneProductListModel smartPhoneProductListModel) {
                modelViewHolder.mNameTV.setText(smartPhoneProductListModel.getName());
                modelViewHolder.mFPriceET.setText(smartPhoneProductListModel.getTrueprice());
                modelViewHolder.mTPriceET.setText(smartPhoneProductListModel.getFalseprice());
                Glide.with(getApplicationContext()).load(smartPhoneProductListModel.getImg()).into(modelViewHolder.mImageView);
                progressDialog.dismiss();
                modelViewHolder.mMain.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getApplicationContext(),SmartPhoneProductDetail.class));
                    }
                });
                modelViewHolder.mDelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatabaseReference ref = getRef(i);
                        ref.removeValue();
                    }
                });
            }

            @NonNull
            @Override
            public SPModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dummy_smart_phone_product_list , parent, false);
                SPModelViewHolder modelViewHolder = new SPModelViewHolder(view);
                return modelViewHolder;
            }
        };
        mRecView.setAdapter(adapter);
        adapter.startListening();
    }

    public static class SPModelViewHolder extends RecyclerView.ViewHolder {
        TextView mNameTV, mFPriceET, mTPriceET;
        Button mDelBtn;
        ImageView mImageView;
        LinearLayout mMain;
        public SPModelViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.mImageView);
            mNameTV = itemView.findViewById(R.id.mNameTV);
            mFPriceET = itemView.findViewById(R.id.mFPriceET);
            mTPriceET = itemView.findViewById(R.id.mTPriceET);
            mDelBtn = itemView.findViewById(R.id.mDelBtn);
            mMain = itemView.findViewById(R.id.mMain);
        }
    }
}