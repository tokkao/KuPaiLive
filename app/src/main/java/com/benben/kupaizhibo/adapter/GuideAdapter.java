package com.benben.kupaizhibo.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;

import com.benben.commoncore.utils.ImageUtils;
import com.benben.kupaizhibo.R;

import java.util.List;

public class GuideAdapter extends PagerAdapter {

    private Activity context;
    private List<String> img;

    public GuideAdapter(Activity context, List<String> img) {
        this.context = context;
        this.img = img;
    }

    @Override
    public int getCount() {
        return img.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        ImageUtils.getPic(img.get(position),imageView,context, R.mipmap.banner_default);

//        if (position == 2) {
//            imageView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(context, MainActivity.class);
//                    context.startActivity(intent);
//                    context.finish();
//                }
//            });
//        }
        container.addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        super.destroyItem(container, position, object);
    }

    @Override
    public void destroyItem(View container, int position, Object object) {
//        super.destroyItem(container, position, object);
    }
}
