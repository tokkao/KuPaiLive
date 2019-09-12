package com.benben.kupaizhibo.ui.mine;

import android.content.Intent;
import android.graphics.Color;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.benben.commoncore.utils.LogUtils;
import com.benben.kupaizhibo.BaseActivity;
import com.benben.kupaizhibo.R;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.view.TimePickerView;
import com.hyphenate.helper.StatusBarUtils;

import java.util.Calendar;

import butterknife.BindView;

/**
 * Created by: wanghk 2019-07-04.
 * Describe: 选择性别
 */
public class SelectSexActivity extends BaseActivity {

    private static final String TAG = "SelectSexActivity";
    @BindView(R.id.switch_sex)
    Switch switchSex;
    @BindView(R.id.iv_woman)
    ImageView ivWoman;
    @BindView(R.id.tv_woman)
    TextView tvWoman;
    @BindView(R.id.iv_man)
    ImageView ivMan;
    @BindView(R.id.tv_man)
    TextView tvMan;
    @BindView(R.id.flt_date_picker)
    FrameLayout fltDatePicker;
    @BindView(R.id.tv_next_step)
    TextView tvNextStep;
    private TimePickerView pvTime;
    private String birthday;
    private int sex; //1男 2女
    private Calendar currentDate;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_select_sex;
    }

    @Override
    protected void initData() {

        initTitle("");
        initTimePicker();
        switchSex.setOnCheckedChangeListener((compoundButton, b) -> {
            sex = b ? 1 : 2;
            if (b) { //男生
                ivWoman.setImageResource(R.mipmap.icon_woman_unselect);
                ivMan.setImageResource(R.mipmap.icon_man_select);
                tvWoman.setTextColor(getResources().getColor(R.color.color_666666));
                tvMan.setTextColor(getResources().getColor(R.color.color_FFFFFF));
            } else {//女生
                ivMan.setImageResource(R.mipmap.icon_man_unselect);
                ivWoman.setImageResource(R.mipmap.icon_woman_select);
                tvMan.setTextColor(getResources().getColor(R.color.color_666666));
                tvWoman.setTextColor(getResources().getColor(R.color.color_FFFFFF));
            }
        });
        tvNextStep.setOnClickListener(view -> startActivity(new Intent(mContext, SelectAvatarActivity.class).putExtra("sex", sex).putExtra("birthday", birthday)));
    }

    private void initTimePicker() {
        //控制时间范围(如果不设置范围，则使用默认时间1900-2100年，此段代码可注释)
        //因为系统Calendar的月份是从0-11的,所以如果是调用Calendar的set方法来设置时间,月份的范围也要是从0-11
        Calendar startDate = Calendar.getInstance();
        startDate.set(1970, 0, 1);
        Calendar endDate = Calendar.getInstance();
        //时间选择器
        pvTime = new TimePickerBuilder(mContext, (date, v) -> {//选中事件回调
            // 这里回调过来的v,就是show()方法里面所添加的 View 参数，如果show的时候没有添加参数，v则为null
            /*btn_Time.setText(getTime(date));*/

        })
                .setLayoutRes(R.layout.layout_time_picker_view, v -> {
                    //pvTime.returnData();
                })
                .setTimeSelectChangeListener(date -> {
                    LogUtils.e(TAG, "onTimeSelectChanged: " + date);
                    Calendar instance = Calendar.getInstance();
                    instance.setTime(date);
                    int year = instance.get(Calendar.YEAR);
                    int month = instance.get(Calendar.MONTH) + 1;
                    int day = instance.get(Calendar.DAY_OF_MONTH);
                    birthday = year + "-" + month + "-" + day;
                    // toast(birthday);
                })
                .setType(new boolean[]{true, true, true, false, false, false})
                .setLabel("", "", "", "", "", "") //设置空字符串以隐藏单位提示   hide label
                .setContentTextSize(25)
                .setTextColorCenter(Color.parseColor("#333333"))
                .setDividerColor(Color.parseColor("#CCCCCC"))
                .setDate(currentDate)
                .setDividerColor(getResources().getColor(R.color.transparent))
                .setRangDate(startDate, endDate)
                .setDecorView(fltDatePicker)//非dialog模式下,设置ViewGroup, pickerView将会添加到这个ViewGroup中
                .setOutSideColor(0x00000000)
                .setOutSideCancelable(false)
                .build();

        pvTime.setKeyBackCancelable(false);//系统返回键监听屏蔽掉

        pvTime.show(tvNextStep);
    }


    @Override
    protected void setStatusBar() {
        StatusBarUtils.setStatusBarColor(mContext, R.color.color_FFFFFF);
        StatusBarUtils.setAndroidNativeLightStatusBar(mContext, true);
    }


}
