package com.benben.kupaizhibo.bean;

import android.os.Parcel;
import android.os.Parcelable;

//图片上传的bean
public class UploadPictureBean implements Parcelable {
    /**
     * id : 363
     * path:http://192.168.2.134:105/uploads/add_diary_pictures/084725935442.jpg
     */

    private int id;
    private String path;

    public UploadPictureBean() {
    }

    public UploadPictureBean(int id, String path) {
        this.id = id;
        this.path = path;
    }

    protected UploadPictureBean(Parcel in) {
        id = in.readInt();
        path = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(path);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<UploadPictureBean> CREATOR = new Creator<UploadPictureBean>() {
        @Override
        public UploadPictureBean createFromParcel(Parcel in) {
            return new UploadPictureBean(in);
        }

        @Override
        public UploadPictureBean[] newArray(int size) {
            return new UploadPictureBean[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
