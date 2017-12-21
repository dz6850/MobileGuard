package cn.edu.gdmec.android.mobileguard;

import android.app.AppOpsManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Process;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.TextView;

import cn.edu.gdmec.android.mobileguard.m1home.HomeActivity;
import cn.edu.gdmec.android.mobileguard.m1home.utils.MyUtils;
import cn.edu.gdmec.android.mobileguard.m1home.utils.VersionUpdateUtils;

public class SplashActivity extends AppCompatActivity {
    private TextView mTvVersion;
    private String mVersion;
    //模块增加
    private static final int MY_PERMISSIONS_REQUEST_PACKAGE_USAGE_STATS = 1101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //11.21
        requestWindowFeature( Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();
        mVersion = MyUtils.getVersion(getApplicationContext());
        mTvVersion = (TextView)findViewById(R.id.tv_splash_version);
        mTvVersion.setText("版本号："+mVersion);
        //
        if (!hasPermission()){
            //
            startActivityForResult (
                    new Intent ( Settings.ACTION_USAGE_ACCESS_SETTINGS ),
                    MY_PERMISSIONS_REQUEST_PACKAGE_USAGE_STATS
            );
        }



        //更新apk
        VersionUpdateUtils.DownloadCallback downloadCallback = new VersionUpdateUtils.DownloadCallback() {
            @Override
            public void afterDownload(String filename) {
                MyUtils.installApk(SplashActivity.this,filename);
            }
        };
        final VersionUpdateUtils versionUpdateUtils=new VersionUpdateUtils(mVersion,SplashActivity.this,downloadCallback,HomeActivity.class);


        new Thread(){
            @Override
            public void run(){
                super.run();
                versionUpdateUtils.getCloudVersion("http://android2017.duapp.com/updateinfo.html");

            }
        }.start();

        //final VersionUpdateUtils versionUpdateUtils = new VersionUpdateUtils(mVersion,SplashActivity.this);
/*        new Thread(){
            @Override
            public void run(){
                super.run();
                versionUpdateUtils.getCloudVersion();
                versionUpdateUtils.getCloudVersion("http://android2017.duapp.com/updateinfo.html");
            }
        }.start();*/

        //老师
        /*startActivity(new Intent (this, HomeActivity.class));
        finish();*/
    }
    private boolean hasPermission(){
        AppOpsManager appOps = (AppOpsManager)
                getSystemService ( APP_OPS_SERVICE );
        int mode = 0;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT){
            mode = appOps.checkOpNoThrow ( AppOpsManager.OPSTR_GET_USAGE_STATS,
                    Process.myUid(), getPackageName ());
        }
        return mode == AppOpsManager.MODE_ALLOWED;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data){
        if (requestCode == MY_PERMISSIONS_REQUEST_PACKAGE_USAGE_STATS){
            if (!hasPermission ()){
                startActivityForResult (
                        new Intent ( Settings.ACTION_USAGE_ACCESS_SETTINGS ),
                        MY_PERMISSIONS_REQUEST_PACKAGE_USAGE_STATS
                );
            }
        }
    }
}