package com.benben.kupaizhibo.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class UserInfoBean implements Parcelable {


    /**
     * easemob : c18f1cfe581845a9f6c995784d4d4700
     * hospital :
     * professional :
     * background :
     * avatar_id : 0
     * invitation_code : IC20190509150936087785
     * id : 32
     * nickname : 用户22474
     * avatar :
     * sex : 0
     * birthday : 0
     * hobby :
     * mobile : 155****6808
     * last_login_time : 0
     * last_login_ip : 0
     * user_money : 0.00
     * total_consumption_money : 0.00
     * total_revenue_money : 0.00
     * user_bobi : 0.00
     * total_consumption_bobi : 0.00
     * votes_total : 0.00
     * user_integral : 0
     * count_integral : 0
     * user_level : 0
     * user_type : 0
     * create_time : 1557129700
     * update_time : 1557129700
     * is_link_mic : 1
     * is_recording : 1
     * tags : [""]
     * autograph :
     * status : 1
     * invite_code :
     * divide_into : 0
     * uuid : 19c162ac88a36b51c8410e724a35b044
     * follow : 0
     * fans : 0
     * video : 0
     */

    private String easemob;
    private String hospital;
    private String professional;
    private String background;
    private int avatar_id;
    private String invitation_code;
    private int id;
    private String nickname;
    private String avatar;
    private int sex;
    private long birthday;
    private String hobby;
    private String mobile;
    private int last_login_time;
    private int last_login_ip;
    private String user_money;
    private String total_consumption_money;
    private String total_revenue_money;
    private double user_bobi;
    private String total_consumption_bobi;
    private String votes_total;
    private int user_integral;
    private int count_integral;
    private int user_level;
    private int user_type;
    private String create_time;
    private String update_time;
    private int is_link_mic;
    private int is_recording;
    private String autograph;
    private int status;
    private String invite_code;
    private int divide_into;
    private String uuid;
    private int follow;
    private int fans;
    private int video;
    private String address;
    private String notice;
    private int is_follow;
    private List<String> tags;

    public UserInfoBean() {
    }

    protected UserInfoBean(Parcel in) {
        easemob = in.readString();
        hospital = in.readString();
        professional = in.readString();
        background = in.readString();
        avatar_id = in.readInt();
        invitation_code = in.readString();
        id = in.readInt();
        nickname = in.readString();
        avatar = in.readString();
        sex = in.readInt();
        birthday = in.readLong();
        hobby = in.readString();
        mobile = in.readString();
        last_login_time = in.readInt();
        last_login_ip = in.readInt();
        user_money = in.readString();
        total_consumption_money = in.readString();
        total_revenue_money = in.readString();
        user_bobi = in.readDouble();
        total_consumption_bobi = in.readString();
        votes_total = in.readString();
        user_integral = in.readInt();
        count_integral = in.readInt();
        user_level = in.readInt();
        user_type = in.readInt();
        create_time = in.readString();
        update_time = in.readString();
        is_link_mic = in.readInt();
        is_recording = in.readInt();
        autograph = in.readString();
        status = in.readInt();
        invite_code = in.readString();
        divide_into = in.readInt();
        uuid = in.readString();
        follow = in.readInt();
        fans = in.readInt();
        video = in.readInt();
        is_follow = in.readInt();
        tags = in.createStringArrayList();
        address =in.readString();
        notice =in.readString();
    }

    public static final Creator<UserInfoBean> CREATOR = new Creator<UserInfoBean>() {
        @Override
        public UserInfoBean createFromParcel(Parcel in) {
            return new UserInfoBean(in);
        }

        @Override
        public UserInfoBean[] newArray(int size) {
            return new UserInfoBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(easemob);
        dest.writeString(hospital);
        dest.writeString(professional);
        dest.writeString(background);
        dest.writeInt(avatar_id);
        dest.writeString(invitation_code);
        dest.writeInt(id);
        dest.writeString(nickname);
        dest.writeString(avatar);
        dest.writeInt(sex);
        dest.writeLong(birthday);
        dest.writeString(hobby);
        dest.writeString(mobile);
        dest.writeInt(last_login_time);
        dest.writeInt(last_login_ip);
        dest.writeString(user_money);
        dest.writeString(total_consumption_money);
        dest.writeString(total_revenue_money);
        dest.writeDouble(user_bobi);
        dest.writeString(total_consumption_bobi);
        dest.writeString(votes_total);
        dest.writeInt(user_integral);
        dest.writeInt(count_integral);
        dest.writeInt(user_level);
        dest.writeInt(user_type);
        dest.writeString(create_time);
        dest.writeString(update_time);
        dest.writeInt(is_link_mic);
        dest.writeInt(is_recording);
        dest.writeString(autograph);
        dest.writeInt(status);
        dest.writeString(invite_code);
        dest.writeInt(divide_into);
        dest.writeString(uuid);
        dest.writeInt(follow);
        dest.writeInt(fans);
        dest.writeInt(video);
        dest.writeInt(is_follow);
        dest.writeString(address);
        dest.writeString(notice);
        dest.writeStringList(tags);
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public String getEasemob() {
        return easemob;
    }

    public void setEasemob(String easemob) {
        this.easemob = easemob;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public String getProfessional() {
        return professional;
    }

    public void setProfessional(String professional) {
        this.professional = professional;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public int getAvatar_id() {
        return avatar_id;
    }

    public void setAvatar_id(int avatar_id) {
        this.avatar_id = avatar_id;
    }

    public String getInvitation_code() {
        return invitation_code;
    }

    public void setInvitation_code(String invitation_code) {
        this.invitation_code = invitation_code;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public long getBirthday() {
        return birthday;
    }

    public void setBirthday(long birthday) {
        this.birthday = birthday;
    }

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getLast_login_time() {
        return last_login_time;
    }

    public void setLast_login_time(int last_login_time) {
        this.last_login_time = last_login_time;
    }

    public int getLast_login_ip() {
        return last_login_ip;
    }

    public void setLast_login_ip(int last_login_ip) {
        this.last_login_ip = last_login_ip;
    }

    public String getUser_money() {
        return user_money;
    }

    public void setUser_money(String user_money) {
        this.user_money = user_money;
    }

    public String getTotal_consumption_money() {
        return total_consumption_money;
    }

    public void setTotal_consumption_money(String total_consumption_money) {
        this.total_consumption_money = total_consumption_money;
    }

    public String getTotal_revenue_money() {
        return total_revenue_money;
    }

    public void setTotal_revenue_money(String total_revenue_money) {
        this.total_revenue_money = total_revenue_money;
    }

    public double getUser_bobi() {
        return user_bobi;
    }

    public void setUser_bobi(double user_bobi) {
        this.user_bobi = user_bobi;
    }

    public String getTotal_consumption_bobi() {
        return total_consumption_bobi;
    }

    public void setTotal_consumption_bobi(String total_consumption_bobi) {
        this.total_consumption_bobi = total_consumption_bobi;
    }

    public String getVotes_total() {
        return votes_total;
    }

    public void setVotes_total(String votes_total) {
        this.votes_total = votes_total;
    }

    public int getUser_integral() {
        return user_integral;
    }

    public void setUser_integral(int user_integral) {
        this.user_integral = user_integral;
    }

    public int getCount_integral() {
        return count_integral;
    }

    public void setCount_integral(int count_integral) {
        this.count_integral = count_integral;
    }

    public int getUser_level() {
        return user_level;
    }

    public void setUser_level(int user_level) {
        this.user_level = user_level;
    }

    public int getUser_type() {
        return user_type;
    }

    public void setUser_type(int user_type) {
        this.user_type = user_type;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public int getIs_link_mic() {
        return is_link_mic;
    }

    public void setIs_link_mic(int is_link_mic) {
        this.is_link_mic = is_link_mic;
    }

    public int getIs_recording() {
        return is_recording;
    }

    public void setIs_recording(int is_recording) {
        this.is_recording = is_recording;
    }

    public String getAutograph() {
        return autograph;
    }

    public void setAutograph(String autograph) {
        this.autograph = autograph;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getInvite_code() {
        return invite_code;
    }

    public void setInvite_code(String invite_code) {
        this.invite_code = invite_code;
    }

    public int getDivide_into() {
        return divide_into;
    }

    public void setDivide_into(int divide_into) {
        this.divide_into = divide_into;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getFollow() {
        return follow;
    }

    public void setFollow(int follow) {
        this.follow = follow;
    }

    public int getFans() {
        return fans;
    }

    public void setFans(int fans) {
        this.fans = fans;
    }

    public int getVideo() {
        return video;
    }

    public void setVideo(int video) {
        this.video = video;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getIs_follow() {
        return is_follow;
    }

    public void setIs_follow(int is_follow) {
        this.is_follow = is_follow;
    }

    @Override
    public String toString() {
        return "UserInfoBean{" +
                "easemob='" + easemob + '\'' +
                ", hospital='" + hospital + '\'' +
                ", professional='" + professional + '\'' +
                ", background='" + background + '\'' +
                ", avatar_id=" + avatar_id +
                ", invitation_code='" + invitation_code + '\'' +
                ", id=" + id +
                ", nickname='" + nickname + '\'' +
                ", avatar='" + avatar + '\'' +
                ", sex=" + sex +
                ", birthday=" + birthday +
                ", hobby='" + hobby + '\'' +
                ", mobile='" + mobile + '\'' +
                ", last_login_time=" + last_login_time +
                ", last_login_ip=" + last_login_ip +
                ", user_money='" + user_money + '\'' +
                ", total_consumption_money='" + total_consumption_money + '\'' +
                ", total_revenue_money='" + total_revenue_money + '\'' +
                ", user_bobi=" + user_bobi +
                ", total_consumption_bobi='" + total_consumption_bobi + '\'' +
                ", votes_total='" + votes_total + '\'' +
                ", user_integral=" + user_integral +
                ", count_integral=" + count_integral +
                ", user_level=" + user_level +
                ", user_type=" + user_type +
                ", create_time='" + create_time + '\'' +
                ", update_time='" + update_time + '\'' +
                ", is_link_mic=" + is_link_mic +
                ", is_recording=" + is_recording +
                ", autograph='" + autograph + '\'' +
                ", status=" + status +
                ", invite_code='" + invite_code + '\'' +
                ", divide_into=" + divide_into +
                ", uuid='" + uuid + '\'' +
                ", follow=" + follow +
                ", fans=" + fans +
                ", video=" + video +
                ", address='" + address + '\'' +
                ", notice='" + notice + '\'' +
                ", tags=" + tags +
                '}';
    }
}
