package app.techland.zmadminapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.squareup.picasso.Picasso;

import java.util.List;

import app.techland.zmadminapp.Models.SmartPhoneProductDetailViewFlipperModel;
import app.techland.zmadminapp.R;

public class MyAdapter extends PagerAdapter {

    Context context;
    List<SmartPhoneProductDetailViewFlipperModel> imgList;
    LayoutInflater inflater;

    public MyAdapter(Context context, List<SmartPhoneProductDetailViewFlipperModel> imgList) {
        this.context = context;
        this.imgList = imgList;
        inflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return imgList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ((ViewPager)container).removeView((View)object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = inflater.inflate(R.layout.view_pager_item, container, false);
        ImageView mVFImg = (ImageView) view.findViewById(R.id.mVFImg);
        Picasso.get().load(imgList.get(position).getImg()).into(mVFImg);
        container.addView(view);
        return view;
    }

}
