package com.benben.kupaizhibo.http;

import java.io.File;

/**
 * Description:参数
 *
 * @author zjn
 * Email：168455992@qq.com
 * @date 2019/1/11
 */
public class RequestParameter {
    private String key;
    private Object obj;
    private File file;

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public RequestParameter(String key, Object obj, File file) {
        this.key = key;
        this.obj = obj;
        this.file = file;
    }

    public RequestParameter(String key, Object obj) {
        this.key = key;
        this.obj = obj;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }
}
