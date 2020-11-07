package app.techland.zmadminapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
import app.techland.zmadminapp.Models.SmartPhoneCompanyListModel;
import app.techland.zmadminapp.Models.SmartPhoneProductDetailViewFlipperModel;
import app.techland.zmadminapp.Models.SmartPhoneProductListModel;
import app.techland.zmadminapp.Transformer.DepthPageTransformer;
import me.relex.circleindicator.CircleIndicator;

public class SmartPhoneProductList extends AppCompatActivity implements SmartPhoneProductDetailInterface {
    RecyclerView mRecView;
    DatabaseReference mDbRf;
    String getBrandName;
    private Dialog dialog;
    ViewPager viewPager;
    MyAdapter adapter;
    DatabaseReference dbRef;
    StorageReference storageReference;
    CircleIndicator indicator;
    SmartPhoneProductDetailInterface iFirebaseLoadDone;


    List<SmartPhoneProductDetailViewFlipperModel> imgList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_phone_product_list);
        mRecView = findViewById(R.id.mRecView);
        dbRef = FirebaseDatabase.getInstance().getReference("SmartPhoneVF");
        dbRef = FirebaseDatabase.getInstance().getReference("SmartPhoneVF");
        storageReference = FirebaseStorage.getInstance().getReference("PhonePicture");
        iFirebaseLoadDone = this;


        getBrandName = getIntent().getStringExtra("brand_name");
//        Toast.makeText(this, getBrandName, Toast.LENGTH_SHORT).show();

        mDbRf = FirebaseDatabase.getInstance().getReference("PhoneName").child(getBrandName);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 2);
        mRecView.setLayoutManager(layoutManager);
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
//                modelViewHolder.mNameTV.setText(smartPhoneProductListModel.getName());
                modelViewHolder.mFPriceET.setText(smartPhoneProductListModel.getTrueprice());
                modelViewHolder.mTPriceET.setText(smartPhoneProductListModel.getFalseprice());
                Glide.with(getApplicationContext()).load(smartPhoneProductListModel.getImg()).into(modelViewHolder.mImageView);
                modelViewHolder.mMain.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openWallDialog(smartPhoneProductListModel.getTrueprice(), smartPhoneProductListModel.getFalseprice(), smartPhoneProductListModel.getId());
//                        startActivity(new Intent(getApplicationContext(), SmartPhoneProductDetail.class));
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
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dummy_smart_phone_product_list, parent, false);
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
//            mNameTV = itemView.findViewById(R.id.mNameTV);
            mFPriceET = itemView.findViewById(R.id.mFPriceET);
            mTPriceET = itemView.findViewById(R.id.mTPriceET);
            mDelBtn = itemView.findViewById(R.id.mDelBtn);
            mMain = itemView.findViewById(R.id.mMain);
        }
    }

    private void openWallDialog(String Fprice, String Tprice, String id) {
        dialog = new Dialog(SmartPhoneProductList.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        dialog.setContentView(R.layout.mobile_detail_dialog_layout);
        viewPager = dialog.findViewById(R.id.view_pager);
        indicator =  dialog.findViewById(R.id.indicator);

        loadImages(id);
        //for transformation when image slided
        viewPager.setPageTransformer(true, new DepthPageTransformer());
        viewPager.setCurrentItem(0);
        dialog.setTitle("Add Plant");
        dialog.getWindow().getAttributes().windowAnimations = R.style.Theme_AppCompat_DayNight_Dialog_Alert;
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        dialog.setCancelable(true);
        TextView mobileNameTV = dialog.findViewById(R.id.mobileNameTV);
        mobileNameTV.setText(Fprice);
        TextView TruePriceTV = dialog.findViewById(R.id.TruePriceTV);
        TruePriceTV.setText(Tprice);
        dialog.show();
    }

    private void loadImages( String productid) {

        dbRef.child(productid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot bikeSnapShot : dataSnapshot.getChildren())
                    imgList.add(bikeSnapShot.getValue(SmartPhoneProductDetailViewFlipperModel.class));
                iFirebaseLoadDone.onFirebaseLoadSuccess(imgList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Something wrong ....", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onFirebaseLoadSuccess(List<SmartPhoneProductDetailViewFlipperModel> imgList) {
        adapter = new MyAdapter(getApplicationContext(), imgList);
        viewPager.setAdapter(adapter);

        //for circle indicator
        indicator.setViewPager(viewPager);
    }

    @Override
    public void onFirebaseLoadFailed(String message) {
        Toast.makeText(getApplicationContext(), "" + message, Toast.LENGTH_SHORT).show();
    }


}