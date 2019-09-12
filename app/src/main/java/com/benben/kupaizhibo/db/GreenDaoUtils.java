package com.benben.kupaizhibo.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * 功能:greenDao 数据库的工具类
 * 作者：zjn on 2017/1/17 13:32
 */

public class GreenDaoUtils {
    private DaoMaster.DevOpenHelper mHelper;
    private SQLiteDatabase db;
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;

    private static GreenDaoUtils greenDaoUtils;
    private Context mContext;

    private GreenDaoUtils(Context ctx) {
        initGreenDao(ctx);
    }

    public static GreenDaoUtils getSingleTon(Context context) {
        if (greenDaoUtils == null) {
            greenDaoUtils = new GreenDaoUtils(context);
        }
        return greenDaoUtils;
    }

    private void initGreenDao(Context ctx) {
        mHelper = new DaoMaster.DevOpenHelper(ctx, "zhixing-db", null);
        db = mHelper.getWritableDatabase();
        mDaoMaster = new DaoMaster(db);
        mDaoSession = mDaoMaster.newSession();
    }

    public DaoSession getDaoSession(Context ctx) {
        if (mDaoMaster == null) {
            initGreenDao(ctx);
        }
        return mDaoSession;
    }

    public SQLiteDatabase getDb(Context ctx) {
        if (db == null) {
            initGreenDao(ctx);
        }
        return db;
    }

}