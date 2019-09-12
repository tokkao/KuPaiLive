package com.benben.kupaizhibo.http;

import com.google.gson.internal.$Gson$Types;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Call;


/**
 * Description:回调函数
 *
 * @author zjn
 * Email：168455992@qq.com
 * @date 2019/1/11
 */
public abstract class BaseCallBack<T> {
    public Type mType;

    static Type getSuperclassTypeParameter(Class<?> subclass) {
        Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof Class) {
            return null;
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
    }

    public BaseCallBack() {
        mType = getSuperclassTypeParameter(this.getClass());
    }

    /**
     * 成功
     *
     * @param t
     */
    public abstract void onSuccess(T t,String msg);

    /**
     * 错误代码
     *
     * @param code
     */
    public abstract void onError(int code,String msg);

    /**
     * 失败
     *
     * @param call
     * @param e
     */
    public abstract void onFailure(Call call, IOException e);
}
