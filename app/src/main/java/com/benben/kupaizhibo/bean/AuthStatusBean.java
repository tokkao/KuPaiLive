package com.benben.kupaizhibo.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Author:zhn
 * Time:2019/6/4 0004 17:55
 */
public class AuthStatusBean implements Serializable {


    /**
     * sfz : 412723199208115862
     * time : 1563330594
     * id : 2
     * file_id : 1293,1294
     * name : 卡通连体恐龙
     * success_time : 1563962700
     * uid : 33
     * type : -1
     * img : [{"id":"1293","path":"http://bjzb.hncjne.com/uploads/images/20190717/102954539488.jpg"},{"id":"1294","path":"http://bjzb.hncjne.com/uploads/images/20190717/102954559233.jpg"}]
     */

    private String sfz;
    private String time;
    private int id;
    private String file_id;
    private String name;
    private String success_time;
    private int uid;
    private int type;
    private List<ImgBean> img;

    public String getSfz() {
        return sfz;
    }

    public void setSfz(String sfz) {
        this.sfz = sfz;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFile_id() {
        return file_id;
    }

    public void setFile_id(String file_id) {
        this.file_id = file_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSuccess_time() {
        return success_time;
    }

    public void setSuccess_time(String success_time) {
        this.success_time = success_time;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<ImgBean> getImg() {
        return img;
    }

    public void setImg(List<ImgBean> img) {
        this.img = img;
    }

    public static class ImgBean implements Serializable{
        /**
         * id : 1293
         * path : http://bjzb.hncjne.com/uploads/images/20190717/102954539488.jpg
         */

        private String id;
        private String path;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }
    }
}
