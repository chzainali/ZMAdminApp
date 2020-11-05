package app.techland.zmadminapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import app.techland.zmadminapp.Adapter.MyAdapter;
import app.techland.zmadminapp.Interface.IFirebaseLoadDone;
import app.techland.zmadminapp.Models.SmartPhoneCompanyListModel;
import app.techland.zmadminapp.Models.SmartPhoneProductDetailTextModel;
import app.techland.zmadminapp.Models.SmartPhoneProductDetailViewFlipperModel;
import app.techland.zmadminapp.Transformer.DepthPageTransformer;
import me.relex.circleindicator.CircleIndicator;

public class SmartPhoneProductDetail extends AppCompatActivity implements SmartPhoneProductDetailInterface {

    ViewPager viewPager;
    MyAdapter adapter;
    DatabaseReference dbRef;
    StorageReference storageReference;
    SmartPhoneProductDetailInterface iFirebaseLoadDone;
    ProgressDialog progressDialog;

    RecyclerView mRecView;
    DatabaseReference mDbRf;

    List<SmartPhoneProductDetailViewFlipperModel> imgList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_phone_product_detail);

        dbRef = FirebaseDatabase.getInstance().getReference("SmartPhoneVF");
        storageReference = FirebaseStorage.getInstance().getReference("SmartPhoneVFPictures");
        progressDialog = new ProgressDialog(SmartPhoneProductDetail.this);
        progressDialog.setMessage("Going Good(Loading)...");
        progressDialog.show();
        iFirebaseLoadDone = this;
        loadBikes();
        viewPager = findViewById(R.id.view_pager);
        //for transformation when image slided
        viewPager.setPageTransformer(true, new DepthPageTransformer());
        viewPager.setCurrentItem(0);

        mRecView = findViewById(R.id.mRecView);
        mDbRf = FirebaseDatabase.getInstance().getReference("SmartPhoneProductDetails");
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 1);
        mRecView.setLayoutManager(layoutManager);
    }

    private void loadBikes(){

        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot bikeSnapShot : dataSnapshot.getChildren())
                    imgList.add(bikeSnapShot.getValue(SmartPhoneProductDetailViewFlipperModel.class));
                iFirebaseLoadDone.onFirebaseLoadSuccess(imgList);
                progressDialog.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Something wrong ....", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    @Override
    public void onFirebaseLoadSuccess(List<SmartPhoneProductDetailViewFlipperModel> imgList) {
        adapter = new MyAdapter(getApplicationContext(), imgList);
        viewPager.setAdapter(adapter);

        //for circle indicator
        CircleIndicator indicator = (CircleIndicator)findViewById(R.id.indicator);
        indicator.setViewPager(viewPager);
    }

    @Override
    public void onFirebaseLoadFailed(String message) {
        Toast.makeText(getApplicationContext(), "" + message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<SmartPhoneProductDetailTextModel> options = new FirebaseRecyclerOptions.Builder<SmartPhoneProductDetailTextModel>()
                .setQuery(mDbRf, SmartPhoneProductDetailTextModel.class)
                .build();

        final FirebaseRecyclerAdapter<SmartPhoneProductDetailTextModel, SPDetailModelViewHolder> adapter = new FirebaseRecyclerAdapter<SmartPhoneProductDetailTextModel, SPDetailModelViewHolder>(options) {
            @Override
            protected void onBindViewHolder (@NonNull SPDetailModelViewHolder viewHolder, final int i, @NonNull SmartPhoneProductDetailTextModel model){

                viewHolder.mName.setText(model.getName());
                viewHolder.mFPrice.setText(model.getFprice());
                viewHolder.mTPrice.setText(model.getTprice());
                viewHolder.mStorge.setText(model.getStorage());
                viewHolder.mColor.setText(model.getColor());
                viewHolder.mFCamera.setText(model.getFcamera());
                viewHolder.mBCamera.setText(model.getBcamera());
                viewHolder.mBattery.setText(model.getBattery());
                viewHolder.mDelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatabaseReference ref = getRef(i);
                        ref.removeValue();
                    }
                });

            }

            @NonNull
            @Override
            public SPDetailModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dummy_smart_phone_product_detail , parent, false);
                SPDetailModelViewHolder viewHolder = new SPDetailModelViewHolder(view);
                return viewHolder;
            }

        };
        mRecView.setAdapter(adapter);
        adapter.startListening();
    }
    public static class SPDetailModelViewHolder extends RecyclerView.ViewHolder {
        TextView mName,mFPrice,mTPrice,mStorge,mColor,mFCamera,mBCamera,mBattery;
        Button mDelBtn;

        public SPDetailModelViewHolder(@NonNull View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.mName);
            mFPrice = itemView.findViewById(R.id.mFPrice);
            mTPrice = itemView.findViewById(R.id.mTPrice);
            mStorge = itemView.findViewById(R.id.mStorge);
            mColor = itemView.findViewById(R.id.mColor);
            mFCamera = itemView.findViewById(R.id.mFCamera);
            mBCamera = itemView.findViewById(R.id.mBCamera);
            mBattery = itemView.findViewById(R.id.mBattery);
            mDelBtn = itemView.findViewById(R.id.mDelBtn);
        }
    }
    }
