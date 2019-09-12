package com.benben.kupaizhibo.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class ActiveLiveGridView extends GridView {

    public ActiveLiveGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ActiveLiveGridView(Context context) {
        super(context);
    }

    public ActiveLiveGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
