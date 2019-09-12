package com.benben.kupaizhibo.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.benben.kupaizhibo.R;


/**
 * 通用的分享面板
 */

public class CommonSharePopupWindow extends PopupWindow {


    private View mView;
    private Context mContext;
    private OnButtonClickListener mOnButtonClickListener;

    public void setOnButtonClickListener(OnButtonClickListener onButtonClickListener) {
        this.mOnButtonClickListener = onButtonClickListener;
    }

    public CommonSharePopupWindow(Activity activity) {
        super(activity);
        this.mContext = activity;
        init();
    }

    private void init() {
        final LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = inflater.inflate(R.layout.pop_common_share, null);
        // 导入布局
        this.setContentView(mView);
        ViewHolder holder = new ViewHolder(mView);
        setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        setOutsideTouchable(true);
        setFocusable(true);
        final ColorDrawable dw = new ColorDrawable(0x00000000);
        setBackgroundDrawable(dw);


        holder.rlytFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (mOnButtonClickListener == null) {
                    return;
                }
                mOnButtonClickListener.OnClickShareType(2);
            }
        });
        holder.rlytWeChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (mOnButtonClickListener == null) {
                    return;
                }
                mOnButtonClickListener.OnClickShareType(1);
            }
        });
        holder.rlytWeibo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (mOnButtonClickListener == null) {
                    return;
                }
                mOnButtonClickListener.OnClickShareType(5);
            }
        });
        holder.rlytQq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (mOnButtonClickListener == null) {
                    return;
                }
                mOnButtonClickListener.OnClickShareType(3);
            }
        });
        holder.rlytQqZone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (mOnButtonClickListener == null) {
                    return;
                }
                mOnButtonClickListener.OnClickShareType(4);
            }
        });

        holder.btnCancel.setOnClickListener(new View.OnClickListener() {
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
        holder.llPop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    //加载弹窗，并更新二维码内容
    public void showWindow(View parent) {

        showAtLocation(parent, Gravity.BOTTOM, 0, 0);
    }


    public interface OnButtonClickListener {
        void OnClickShareType(int type);
    }


    public static
    class ViewHolder {
        public View rootView;
        public ImageView ivFriend;
        public RelativeLayout rlytFriend;
        public ImageView ivWeChat;
        public RelativeLayout rlytWeChat;
        public ImageView ivQq;
        public RelativeLayout rlytQq;
        public ImageView ivQqZone;
        public RelativeLayout rlytQqZone;
        public ImageView ivWeibo;
        public RelativeLayout rlytWeibo;
        public RelativeLayout rlytRoot;
        public LinearLayout llPop;
        public Button btnCancel;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.ivFriend = (ImageView) rootView.findViewById(R.id.iv_friend);
            this.rlytFriend = (RelativeLayout) rootView.findViewById(R.id.rlyt_friend);
            this.ivWeChat = (ImageView) rootView.findViewById(R.id.iv_we_chat);
            this.rlytWeChat = (RelativeLayout) rootView.findViewById(R.id.rlyt_we_chat);
            this.rlytRoot = (RelativeLayout) rootView.findViewById(R.id.rlyt_root);
            this.ivQq = (ImageView) rootView.findViewById(R.id.iv_qq);
            this.rlytQq = (RelativeLayout) rootView.findViewById(R.id.rlyt_qq);
            this.ivQqZone = (ImageView) rootView.findViewById(R.id.iv_qq_zone);
            this.rlytQqZone = (RelativeLayout) rootView.findViewById(R.id.rlyt_qq_zone);
            this.ivWeibo = (ImageView) rootView.findViewById(R.id.iv_weibo);
            this.rlytWeibo = (RelativeLayout) rootView.findViewById(R.id.rlyt_weibo);
            this.llPop = (LinearLayout) rootView.findViewById(R.id.ll_pop);
            this.btnCancel = (Button) rootView.findViewById(R.id.btn_cancel);
        }

    }
}
