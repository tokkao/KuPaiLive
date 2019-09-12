package com.benben.commoncore.utils;

import android.os.Handler;
import android.view.View;

/**
 * 功能:Scrollview 工具类
 * 作者：zjn on 2017/7/12 16:58
 */

public class ScrollviewUtils {

    /**
     * 功能:scrollview 滚动到底部
     * @param scroll
     * @param inner
     */
    public static void scrollToBottom(final View scroll, final View inner) {

        Handler mHandler = new Handler();

        mHandler.post(new Runnable() {
            public void run() {
                if (scroll == null || inner == null) {
                    return;
                }
                int offset = inner.getMeasuredHeight();
                if (offset < 0) {
                    offset = 0;
                }

                scroll.scrollTo(0, offset);
            }
        });
    }
}
