package com.benben.kupaizhibo.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.benben.kupaizhibo.R;
import com.benben.kupaizhibo.securityKeyboard.KeyboardType;
import com.benben.kupaizhibo.securityKeyboard.SecurityConfigure;
import com.benben.kupaizhibo.securityKeyboard.SecurityKeyboard;


/**
 * 底部弹出
 * 支付提醒
 */

public class CashPwdPopupWindow extends PopupWindow {

    private View view;
    private Activity mContext;
    private CashInterface cashInterface;
    private EditText etPwd;

    private SecurityKeyboard securityKeyboard;

    //安全键盘需要传入的，如不需要后续需要更改密码弹出框
    private View tvShowPopupWindow;

    public CashPwdPopupWindow(Activity activity, CashInterface cashInterface, View tvShowPopupWindow) {
        super(activity);
        this.mContext = activity;
        this.cashInterface = cashInterface;
        this.tvShowPopupWindow = tvShowPopupWindow;
        init();
    }

    private void init() {
        final LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.pop_cash_pwd, null);
        SecurityConfigure configure = new SecurityConfigure()
                .setDefaultKeyboardType(KeyboardType.LETTER)
                .setSymbolEnabled(false);
        securityKeyboard = new SecurityKeyboard(view.findViewById(R.id.ll_pop), configure
                ,mContext,tvShowPopupWindow);

        TextView tvTitle = view.findViewById(R.id.tv_title);
        etPwd = view.findViewById(R.id.et_pwd);
        tvTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pwd = etPwd.getText().toString().trim();
                if (TextUtils.isEmpty(pwd)) {
                    Toast.makeText(mContext, "请输入支付密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                cashInterface.PwdCallback(pwd);
            }
        });

        TextView tvCancel = view.findViewById(R.id.tv_cancel);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        // 导入布局
        this.setContentView(view);
        setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        setHeight(WindowManager.LayoutParams.MATCH_PARENT);
        // 设置可触
        setFocusable(true);
        final ColorDrawable dw = new ColorDrawable(0x0000000);
        setBackgroundDrawable(dw);
        // 单击屏幕关闭弹窗
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int height = view.findViewById(R.id.ll_pop).getTop();
                int y = (int) motionEvent.getY();
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });
    }

    public interface CashInterface {
        void PwdCallback(String pwd);
    }

}
