package cn.edu.gdmec.android.mobileguard.m4appmanager.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.edu.gdmec.android.mobileguard.m4appmanager.entity.AppInfo;

/**
 * Created by ASUS PRO on 2017/11/7.
 */

public class AppInfoParser {
    //获取手机里面的所有的应用程序
    public static List<AppInfo> getAppInfos(Context context){
        //获取包管理器。
        PackageManager pm = context.getPackageManager ();
        //若要获得已安装app的签名和权限信息
        // 要在获取时传入相关flags，否则不会获取。
        //List<PackageInfo> packInfos = pm.getInstalledPackages ( 0 );
        List<PackageInfo> packInfos = pm.getInstalledPackages(PackageManager.GET_SIGNATURES
                +PackageManager.GET_PERMISSIONS+PackageManager.GET_ACTIVITIES);
        //+PackageManager.GET_PERMISSIONS);


        List<AppInfo> appinfos = new ArrayList<AppInfo> (  );
        for (PackageInfo packInfo:packInfos){
            AppInfo appinfo = new AppInfo ();
            String packname = packInfo.packageName;

            appinfo.packageName = packname;
            Drawable icon = packInfo.applicationInfo.loadIcon(pm);
            appinfo.icon = icon;
            String appname = packInfo.applicationInfo.loadLabel ( pm ).toString ();
            appinfo.appName = appname;

            //应用程序apk包的路径
            String apkpath = packInfo.applicationInfo.sourceDir;
            appinfo.apkPath = apkpath;
            File file = new File ( apkpath );
            long appSize = file.length ();
            appinfo.appSize = appSize;

            //应用程序安装的位置。
            //二进制映射
            int flags = packInfo.applicationInfo.flags;
            if ((ApplicationInfo.FLAG_EXTERNAL_STORAGE & flags)!=0){
                //外部存储
                appinfo.isInRoom = false;
            }else {
                //手机内存
                appinfo.isInRoom = true;
            }
            if ((ApplicationInfo.FLAG_SYSTEM &flags)!=0){
                //系统应用
                appinfo.isUserApp = false;
            }else {
                //用户应用
                appinfo.isUserApp = true;
            }



            appinfos.add ( appinfo );
            appinfo = null;
        }
        return appinfos;
    }
}
