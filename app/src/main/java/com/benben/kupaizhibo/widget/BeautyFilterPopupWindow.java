package com.benben.kupaizhibo.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.benben.kupaizhibo.R;


/**
 * 美颜弹窗
 */

public class BeautyFilterPopupWindow extends PopupWindow {


    private View mView;
    private Context mContext;
    private OnProgressChangeListener mProgressChangeListener;
    private int whiteningLevel;
    private int beautyLevel;

    public void setOnProgressChangeListener(OnProgressChangeListener onButtonClickListener) {
        this.mProgressChangeListener = onButtonClickListener;
    }

    public BeautyFilterPopupWindow(Activity activity, int whiteningLevel, int beautyLevel) {
        super(activity);
        this.mContext = activity;
        this.whiteningLevel = whiteningLevel;
        this.beautyLevel = beautyLevel;

        init();
    }

    private void init() {
        final LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = inflater.inflate(R.layout.pop_beauty_filter, null);
        // 导入布局
        this.setContentView(mView);
        ViewHolder holder = new ViewHolder(mView);
        setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        setOutsideTouchable(true);
        setFocusable(true);
        final ColorDrawable dw = new ColorDrawable(0x00000000);
        setBackgroundDrawable(dw);

        holder.sb_beauty.setMax(90);
        holder.sb_whitening.setMax(90);

        holder.sb_beauty.setProgress(beautyLevel);
        holder.sb_whitening.setProgress(whiteningLevel);


        holder.sb_whitening.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (mProgressChangeListener != null) {
                    mProgressChangeListener.OnWhiteningLevelChangeListener(i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        holder.sb_beauty.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (mProgressChangeListener != null) {
                    mProgressChangeListener.OnBeautyLevelChangeListener(i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        holder.btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isShowing())
                    dismiss();
            }
        });

        holder.rlytRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isShowing()) {
                    dismiss();
                }
            }
        });
        holder.ll_pop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    //加载弹窗，并更新二维码内容
    public void showWindow(View parent) {

        showAtLocation(parent, Gravity.BOTTOM, 0, 0);
    }


    public interface OnProgressChangeListener {
        void OnWhiteningLevelChangeListener(int level);

        void OnBeautyLevelChangeListener(int level);

    }

    public Bitmap getShareContent() {
        /*llytShareContent.setDrawingCacheEnabled(true);
        llytShareContent.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        llytShareContent.layout(0, 0, llytShareContent.getMeasuredWidth(), llytShareContent.getMeasuredHeight());
        Bitmap bitmap = Bitmap.createBitmap(llytShareContent.getDrawingCache());
        llytShareContent.setDrawingCacheEnabled(false);*/
        return null;
    }


    public static
    class ViewHolder {
        public View rootView;
        public SeekBar sb_whitening;
        public SeekBar sb_beauty;
        public LinearLayout ll_pop;
        public Button btn_cancel;
        public RelativeLayout rlytRoot;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.sb_whitening = (SeekBar) rootView.findViewById(R.id.sb_whitening);
            this.sb_beauty = (SeekBar) rootView.findViewById(R.id.sb_beauty);
            this.ll_pop = (LinearLayout) rootView.findViewById(R.id.ll_pop);
            this.btn_cancel = (Button) rootView.findViewById(R.id.btn_cancel);
            this.rlytRoot = (RelativeLayout) rootView.findViewById(R.id.rlyt_root);
        }

    }
}
