package com.benben.kupaizhibo.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.benben.commoncore.utils.JSONUtils;
import com.benben.commoncore.utils.LogUtils;
import com.benben.commoncore.utils.ToastUtils;
import com.benben.kupaizhibo.R;
import com.benben.kupaizhibo.adapter.AFinalRecyclerViewAdapter;
import com.benben.kupaizhibo.adapter.ViewerListAdapter;
import com.benben.kupaizhibo.api.NetUrlUtils;
import com.benben.kupaizhibo.bean.UserInfoBean;
import com.benben.kupaizhibo.bean.ViewerListBean;
import com.benben.kupaizhibo.http.BaseCallBack;
import com.benben.kupaizhibo.http.BaseOkHttpClient;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.io.IOException;

import okhttp3.Call;


/**
 * 观看人数列表
 */

public class ViewerListPopupWindow extends PopupWindow {


    private static final String TAG = "CommonReportPopupWindow";
    private View mView;
    private Activity mContext;
    private ViewerListAdapter mViewerListAdapter;
    //页数
    private int page = 1;
    //直播码
    private String stream;
    private boolean isRefresh = false;
    private ViewHolder holder;
    private String mStream;

    public ViewerListPopupWindow(Activity activity,String stream) {
        super(activity);
        this.mContext = activity;
        this.stream = stream;
        init();
    }

    private void init() {
        final LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = inflater.inflate(R.layout.pop_viewer_list, null);
        // 导入布局
        this.setContentView(mView);
        holder = new ViewHolder(mView);
        setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        setOutsideTouchable(false);
        final ColorDrawable dw = new ColorDrawable(0x00000000);
        setBackgroundDrawable(dw);

        //setFocusable(true);

        holder.rlvViewerList.setLayoutManager(new LinearLayoutManager(mContext));
        mViewerListAdapter = new ViewerListAdapter(mContext);
        holder.rlvViewerList.setAdapter(mViewerListAdapter);
        holder.refreshLayout.setPrimaryColors(mContext.getResources().getColor(R.color.transparent), mContext.getResources().getColor(R.color.white));
        holder.refreshLayout.setRefreshHeader(new ClassicsHeader(mContext));
        holder.refreshLayout.setRefreshFooter(new ClassicsFooter(mContext));

        getViewerList();
        //刷新
        holder.refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                isRefresh = false;
                page++;
                getViewerList();
            }
        });
        holder.refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                isRefresh = true;
                page = 1;
                getViewerList();
            }
        });


        //关闭popup
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
        mViewerListAdapter.setOnItemClickListener(new AFinalRecyclerViewAdapter.OnItemClickListener<ViewerListBean.ListsBean>() {
            @Override
            public void onItemClick(View view, int position, ViewerListBean.ListsBean model) {
                dismiss();
                showUserInfoDialog(model.getId());
            }

            @Override
            public void onItemLongClick(View view, int position, ViewerListBean.ListsBean model) {

            }
        });

    }

    private void showUserInfoDialog(int user_id) {

        BaseOkHttpClient.newBuilder()
                .addParam("uid", user_id)
                .url(NetUrlUtils.GET_USER_INFO)
                .post().json()
                .build().enqueue(mContext, new BaseCallBack<String>() {
            @Override
            public void onSuccess(String result, String msg) {
                UserInfoBean mUserInfoBean = JSONUtils.jsonString2Bean(result, UserInfoBean.class);
                UserInfoDialog zhuBoInfoDialog = UserInfoDialog.getInstance();
                zhuBoInfoDialog.showUserInfoDialog(mContext, mUserInfoBean,mStream);
            }

            @Override
            public void onError(int code, String msg) {
                ToastUtils.show(mContext,"获取用户信息失败，请重试");
                LogUtils.e(TAG, msg);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.e(TAG, e.getLocalizedMessage());
            }
        });
    }


    //获取观众列表
    private void getViewerList() {
        BaseOkHttpClient.newBuilder()
                .addParam("stream",stream)
                .addParam("page", page)
                .url(NetUrlUtils.LIVES_GET_USER_LIST)
                .post().json()
                .build().enqueue(mContext, new BaseCallBack<String>() {
            @Override
            public void onSuccess(String json, String msg) {
                LogUtils.e(TAG, "获取观众列表----onSuccess:" + json);
                ViewerListBean viewListBean = JSONUtils.jsonString2Bean(json, ViewerListBean.class);
                holder.tvViewerNumber.setText(mContext.getResources().getString(R.string.viewer_number, String.valueOf(viewListBean.getNums())));
                    if(isRefresh){
                        mViewerListAdapter.refreshList(viewListBean.getLists());
                    }else {
                        mViewerListAdapter.appendToList(viewListBean.getLists());
                    }
                holder.refreshLayout.finishRefresh(true);
                holder.refreshLayout.finishLoadMore(true);

            }

            @Override
            public void onError(int code, String msg) {
                LogUtils.e(TAG, "获取观众列表----onError:" + msg);
                ToastUtils.show(mContext, msg);
                holder.refreshLayout.finishRefresh(false);
                holder.refreshLayout.finishLoadMore(false);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.e(TAG, "获取观众列表----onFailure:" + e.getMessage());
                holder.refreshLayout.finishRefresh(false);
                holder.refreshLayout.finishLoadMore(false);
            }
        });


    }

    //加载弹窗
    public void showWindow(String stream) {
        mStream = stream;
        showAtLocation(mContext.getWindow().getDecorView().getRootView(), Gravity.BOTTOM, 0, 0);
    }


    public static
    class ViewHolder {
        public View rootView;
        public RecyclerView rlvViewerList;
        public SmartRefreshLayout refreshLayout;
        public LinearLayout llPop;
        public TextView tvViewerNumber;
        public RelativeLayout rlytRoot;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.rlvViewerList = (RecyclerView) rootView.findViewById(R.id.rlv_viewer_list);
            this.tvViewerNumber = (TextView) rootView.findViewById(R.id.tv_viewer_number);
            this.refreshLayout = (SmartRefreshLayout) rootView.findViewById(R.id.refresh_layout);
            this.llPop = (LinearLayout) rootView.findViewById(R.id.ll_pop);
            this.rlytRoot = (RelativeLayout) rootView.findViewById(R.id.rlyt_root);
        }

    }
}
