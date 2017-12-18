package com.infoservices.lue.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AndroidImageAdapternew extends PagerAdapter {
    Context mContext;
    private ArrayList<String> ImageList=new ArrayList<String>();


    public AndroidImageAdapternew(Context context, ArrayList<String> imageList) {

        this.mContext = context;
        this.ImageList=imageList;
    }

    @Override
    public int getCount() {
        return ImageList.size();
    }

    private int[] sliderImagesId = new int[]{
            //   R.drawable.image1, R.drawable.image2, R.drawable.cat,
            //  R.drawable.image1, R.drawable.image2, R.drawable.cat,
    };

    @Override
    public boolean isViewFromObject(View v, Object obj) {

        return v == ((ImageView) obj);

    }

    @Override
    public Object instantiateItem(ViewGroup container, int i) {

        ImageView mImageView = new ImageView(mContext);
        mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Picasso.with(mContext).load(ImageList.get(i)).into(mImageView);
        Log.d("logd"," ddd"+ "  "+ImageList.size());
        // mImageView.setImageResource(Integer.parseInt(ImageList.get(i).getImage()));
        ((ViewPager) container).addView(mImageView, 0);
        return mImageView;


    }

    @Override
    public void destroyItem(ViewGroup container, int i, Object obj) {
        ((ViewPager) container).removeView((ImageView) obj);

    }

}
