package cn.edu.gdmec.android.mobileguard.m2theftguard;

import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.Toast;

import cn.edu.gdmec.android.mobileguard.R;

/**
 * Created by ASUS PRO on 2017/10/12.
 */

public class Setup1Activity extends BaseSetupActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_1);
        ((RadioButton)findViewById(R.id.rb_first)).setChecked(true);
    }
    @Override
    public void showNext() {
        startActivityAndFinishSelf(Setup2Activity.class);
    }

    @Override
    public void showPre() {
        Toast.makeText(this,"当前页面已经是第一页",Toast.LENGTH_LONG).show();
    }
}
