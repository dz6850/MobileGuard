package cn.edu.gdmec.android.mobileguard.m3communicationguard.entity;

/**
 * Created by 杜卓 on 2017/11/2.
 */

public class BlackContactInfo {
    public String phoneNumber;
    public String contackName;
    public int mode;

    public String getModeString(int mdoe){
        switch (mode){
            case 1:
                return "电话拦截";
            case 2:
                return "短信拦截";
            case 3:
                return "电话、短信拦截";
        }
        return "";
    }
}
