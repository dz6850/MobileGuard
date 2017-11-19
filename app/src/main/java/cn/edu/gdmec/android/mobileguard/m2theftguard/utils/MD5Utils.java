package cn.edu.gdmec.android.mobileguard.m2theftguard.utils;

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by ASUS PRO on 2017/9/27.
 */

public class MD5Utils {
    //md5摘要的算法
    public static String encode(String text){
        try{
            MessageDigest digest = MessageDigest.getInstance ( "md5" );
            byte[] result = digest.digest (text.getBytes ());
            StringBuilder sb = new StringBuilder (  );
            for (byte b : result){
                int number = b&0xff;
                String hex = Integer.toHexString ( number );
                if(hex.length ()==1){
                    sb.append ( "0"+hex );
                }else {
                    sb.append ( hex );
                }
            }
            return sb.toString ();
        }catch (NoSuchAlgorithmException e){
            e.printStackTrace ();
            return "";
        }
    }
    //获取文件的md5值
    public static String getFileMd5(String path) {
        try {
            MessageDigest digest = MessageDigest.getInstance ( "md5" );
            File file = new File ( path );
            FileInputStream fis = new FileInputStream ( file );
            byte[] buffer = new byte[1024];
            int len = -1;
            while ((len = fis.read (buffer)) != -1){
                digest.update ( buffer,0,len );
            }
            byte[] result = digest.digest ();
            StringBuilder sb = new StringBuilder (  );
            for (byte b : result ){
                int number = b&0xff;
                String hex = Integer.toHexString ( number );
                if (hex.length () == 1){
                    sb.append ( "0"+hex );
                }else {
                    sb.append ( hex );
                }
            }
            return sb.toString ();
        }catch (Exception e){
            e.printStackTrace ();
            return null;
        }
    }
}
