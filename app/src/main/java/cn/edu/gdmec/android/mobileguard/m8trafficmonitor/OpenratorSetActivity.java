package cn.edu.gdmec.android.mobileguard.m8trafficmonitor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import cn.edu.gdmec.android.mobileguard.R;


public class OpenratorSetActivity extends AppCompatActivity implements View.OnClickListener{
    private Spinner mSelectSP;
    private String[] operators = {"中国移动","中国联通","中国电信"};
    private ArrayAdapter mSelectadapter;
    private SharedPreferences msp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_operator_set);
        msp = getSharedPreferences("config",MODE_PRIVATE);
        initView();
    }

    private void initView(){
        findViewById(R.id.rl_titlebar).setBackgroundColor(getResources().getColor(R.color.light_green));
        ImageView mLeftImgw = (ImageView) findViewById(R.id.imgv_leftbtn);
        ((TextView) findViewById(R.id.tv_title)).setText("运营商信息设置");
        mLeftImgw.setOnClickListener(this);
        mLeftImgw.setImageResource(R.drawable.back);
        mSelectSP = (Spinner) findViewById(R.id.spinner_operator_select);
        mSelectadapter = new ArrayAdapter(this, R.layout.item_spinner_operatorset, R.id.tv_provice,operators);
        mSelectSP.setAdapter(mSelectadapter);
        findViewById(R.id.btn_operator_finish).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        SharedPreferences.Editor editor = msp.edit();
        switch (v.getId()){
            case R.id.imgv_leftbtn:
                editor.putBoolean("isset_operator",false);
                finish();
                break;
            case R.id.btn_operator_finish:
                editor.putInt("operator",mSelectSP.getSelectedItemPosition() + 1);
                editor.putBoolean("isset_operator",true);
                editor.commit();
                startActivity(new Intent(this,TrafficMonitoringActivity.class));
                finish();
                break;
        }
    }
}














