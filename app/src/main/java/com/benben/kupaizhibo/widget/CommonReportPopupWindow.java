package com.benben.kupaizhibo.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.benben.commoncore.utils.JSONUtils;
import com.benben.commoncore.utils.LogUtils;
import com.benben.commoncore.utils.StringUtils;
import com.benben.commoncore.utils.ToastUtils;
import com.benben.kupaizhibo.R;
import com.benben.kupaizhibo.adapter.AFinalRecyclerViewAdapter;
import com.benben.kupaizhibo.adapter.ReportTypeListAdapter;
import com.benben.kupaizhibo.api.NetUrlUtils;
import com.benben.kupaizhibo.bean.ReportListBean;
import com.benben.kupaizhibo.http.BaseCallBack;
import com.benben.kupaizhibo.http.BaseOkHttpClient;
import com.kongzue.dialog.v3.MessageDialog;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;


/**
 * 通用的举报面板
 */

public class CommonReportPopupWindow extends PopupWindow {


    private static final String TAG = "CommonReportPopupWindow";
    private View mView;
    private Activity mContext;
    private ReportTypeListAdapter mReportTypeListAdapter;
    private int mReportId = -1;
    private int userId;
    private ViewHolder holder;
    private String mSteamCode;


    public CommonReportPopupWindow(Activity activity, int userId) {
        super(activity);
        this.mContext = activity;
        this.userId = userId;
        init();
    }

    private void init() {
        final LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = inflater.inflate(R.layout.pop_common_report, null);
        // 导入布局
        this.setContentView(mView);
        holder = new ViewHolder(mView);
        setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        setOutsideTouchable(true);
        final ColorDrawable dw = new ColorDrawable(0x00000000);
        setBackgroundDrawable(dw);
        //获取焦点
        setFocusable(true);//这里必须设置为true才能点击区域外或者消失
        setTouchable(true);//这个控制PopupWindow内部控件的点击事件
        setOutsideTouchable(true);
        update();


        holder.rlvReportList.setLayoutManager(new GridLayoutManager(mContext, 2));
        mReportTypeListAdapter = new ReportTypeListAdapter(mContext);
        holder.rlvReportList.setAdapter(mReportTypeListAdapter);
        //获取举报列表
        getReportList();

        //item点击事件
        mReportTypeListAdapter.setOnItemClickListener(new AFinalRecyclerViewAdapter.OnItemClickListener<ReportListBean>() {
            @Override
            public void onItemClick(View view, int position, ReportListBean model) {
                mReportId = model.getId();
            }

            @Override
            public void onItemLongClick(View view, int position, ReportListBean model) {

            }
        });

        //提交举报
        holder.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmReportInfo();
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

    //提交举报
    private void confirmReportInfo() {
        if (mReportId == -1) {
            ToastUtils.show(mContext, mContext.getResources().getString(R.string.not_yet_select_report_type));
            return;
        }
        if (StringUtils.isEmpty(holder.edtContent.getText().toString().trim())) {
            ToastUtils.show(mContext, mContext.getResources().getString(R.string.report_content_not_null));
            return;
        }
        BaseOkHttpClient.newBuilder()
                .addParam("classify", mReportId) //直播举报分类id
                .addParam("user_id", userId) //用户id值
                .addParam("room_id", mSteamCode) //推流码
                .addParam("content", holder.edtContent.getText().toString()) //举报内容
                .url(NetUrlUtils.LIVES_REPORT_ADD)
                .post().json()
                .build().enqueue(mContext, new BaseCallBack<String>() {
            @Override
            public void onSuccess(String json, String msg) {
                LogUtils.e(TAG, "举报----onSuccess:" + json);
                MessageDialog.show((AppCompatActivity) mContext, mContext.getResources().getString(R.string.report_successful), mContext.getResources().getString(R.string.report_successful_description), mContext.getResources().getString(R.string.positive), mContext.getResources().getString(R.string.negative))
                        .setOnOkButtonClickListener((baseDialog, v) -> {
                            baseDialog.doDismiss();
                            dismiss();
                            return false;
                        }).show();
            }

            @Override
            public void onError(int code, String msg) {
                LogUtils.e(TAG, msg);
                ToastUtils.show(mContext, msg);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.e(TAG, e.getLocalizedMessage());
            }
        });


    }

    //获取举报列表
    private void getReportList() {
        /*ArrayList<ReportTypeListBean> reportTypeListBeans = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            ReportTypeListBean reportTypeListBean = new ReportTypeListBean();
            reportTypeListBean.setName("违法犯罪" + i);
            reportTypeListBean.setReport_id(i);
            reportTypeListBean.setSelect(false);
            reportTypeListBeans.add(reportTypeListBean);
        }

        mReportTypeListAdapter.appendToList(reportTypeListBeans);*/
        BaseOkHttpClient.newBuilder()
                .url(NetUrlUtils.LIVES_REPORT_CLASSIFY)
                .post().json()
                .build().enqueue(mContext, new BaseCallBack<String>() {
            @Override
            public void onSuccess(String json, String msg) {
                LogUtils.e(TAG, "获取举报列表----onSuccess:" + json);
                List<ReportListBean> reportTypeList = JSONUtils.jsonString2Beans(json, ReportListBean.class);
                mReportTypeListAdapter.appendToList(reportTypeList);

            }

            @Override
            public void onError(int code, String msg) {
                LogUtils.e(TAG, "获取举报列表----onError:" + msg);
                ToastUtils.show(mContext, msg);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.e(TAG, "获取举报列表----onFailure:" + e.getMessage());
            }
        });


    }

    //加载弹窗
    public void showWindow(String steamCode) {
        this.mSteamCode = steamCode;
        showAtLocation(mContext.getWindow().getDecorView().getRootView(), Gravity.BOTTOM, 0, 0);
    }


    public static
    class ViewHolder {
        public View rootView;
        public RecyclerView rlvReportList;
        public EditText edtContent;
        public Button btnConfirm;
        public RelativeLayout rlytRoot;
        public LinearLayout llPop;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.rlvReportList = (RecyclerView) rootView.findViewById(R.id.rlv_report_list);
            this.edtContent = (EditText) rootView.findViewById(R.id.edt_content);
            this.btnConfirm = (Button) rootView.findViewById(R.id.btn_confirm);
            this.rlytRoot = (RelativeLayout) rootView.findViewById(R.id.rlyt_root);
            this.llPop = (LinearLayout) rootView.findViewById(R.id.ll_pop);
        }

    }
}
