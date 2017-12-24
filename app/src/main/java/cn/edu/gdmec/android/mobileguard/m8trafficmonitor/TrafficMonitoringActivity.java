package cn.edu.gdmec.android.mobileguard.m8trafficmonitor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.edu.gdmec.android.mobileguard.R;
import cn.edu.gdmec.android.mobileguard.m8trafficmonitor.db.dao.TrafficDao;
import cn.edu.gdmec.android.mobileguard.m8trafficmonitor.service.TrafficMonitoringService;
import cn.edu.gdmec.android.mobileguard.m8trafficmonitor.utils.SystemInfoUtils;

/**
 * Created by Swindler on 2017/11/29.
 */

public class TrafficMonitoringActivity extends AppCompatActivity implements View.OnClickListener{
    private SharedPreferences mSP;
    private Button mCorrectFlowBtn;
    private TextView mTotalTV;
    private TextView mUsedTV;
    private TextView mToDayTV;
    private TrafficDao dao;
    private ImageView mRemindIMGV;
    private TextView mRemindTV;
    private CorrectFlowReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_triffic_monitor);
        mSP = getSharedPreferences("config", MODE_PRIVATE);
        boolean flag = mSP.getBoolean("isset_operator", false);
        // 如果没有设置运营商信息则进入信息设置页面
        if (!flag) {
            startActivity(new Intent(this, OpenratorSetActivity.class));
            finish();
        }
        if (!SystemInfoUtils
                .isServiceRunning(this,
                        "cn.edu.gdmec.android.mobileguard.m8trafficmonitor.service.TrafficMonitoringService")) {
            startService(new Intent(this, TrafficMonitoringService.class));
        }
        initView();
        registReceiver();
        initData();
    }

    private void initView() {
        findViewById(R.id.rl_titlebar).setBackgroundColor(
                getResources().getColor(R.color.light_green));
        ImageView mLeftImgv = (ImageView) findViewById(R.id.imgv_leftbtn);
        ((TextView) findViewById(R.id.tv_title)).setText("流量监控");
        mLeftImgv.setOnClickListener(this);
        mLeftImgv.setImageResource(R.drawable.back);
        ImageView mRightImgv = (ImageView ) findViewById ( R.id.imgv_righttbtn );
        mRightImgv.setImageResource ( R.drawable.processmanager_setting_icon );
        mRightImgv.setOnClickListener ( this );

        mCorrectFlowBtn = (Button) findViewById(R.id.btn_correction_flow);
        mCorrectFlowBtn.setOnClickListener(this);

        mTotalTV = (TextView) findViewById(R.id.tv_month_totalgprs);
        mUsedTV = (TextView) findViewById(R.id.tv_month_usedgprs);
        mToDayTV = (TextView) findViewById(R.id.tv_today_gprs);
        mRemindIMGV = (ImageView) findViewById(R.id.imgv_traffic_remind);
        mRemindTV = (TextView) findViewById(R.id.tv_traffic_remind);
    }

    private void initData() {
        long totalflow = mSP.getLong("totalflow", 0);
        long usedflow = mSP.getLong("usedflow", 0);
        if (totalflow > 0 & usedflow >= 0) {
            float scale = usedflow / totalflow;
            if (scale > 0.9) {
                mRemindIMGV.setEnabled(false);
                mRemindTV.setText("您的套餐流量即将用完！");
            } else {
                mRemindIMGV.setEnabled(true);
                mRemindTV.setText("本月流量充足请放心使用");
            }
        }
        mTotalTV.setText("本月流量：" + Formatter.formatFileSize(this, totalflow));
        mUsedTV.setText("本月已用：" + Formatter.formatFileSize(this, usedflow));
        dao = new TrafficDao(this);
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dataString = sdf.format(date);
        long moblieGPRS = dao.getMoblieGPRS(dataString);
        if (moblieGPRS < 0) {
            moblieGPRS = 0;
        }
        mToDayTV.setText("本日已用：" + Formatter.formatFileSize(this, moblieGPRS));
    }

    private void registReceiver() {
        receiver = new CorrectFlowReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(receiver, filter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgv_leftbtn:
                finish();
                break;

            //m8xin
            case R.id.imgv_righttbtn:
                startActivity ( new Intent ( this,OpenratorSetActivity.class ) );
                break;

            case R.id.btn_correction_flow:
                // 首先判断是哪个运营商，
                int i = mSP.getInt("operator", 0);
                SmsManager smsManager = SmsManager.getDefault();
                switch (i) {
                    case 0:
                        // 没有设置运营商
                        Toast.makeText(this, "您还没有设置运营商信息", 0).show();
                        break;
                    case 1:
                        // 中国移动
                        // 发送cxll至10086
                        // 获取系统默认的短信管理器
                        smsManager.sendTextMessage("10086", null, "CXLL", null, null);
                        break;
                    case 2:
                        // 中国联通
                        // 发送cxll至10010
                        // 获取系统默认的短信管理器
                        smsManager.sendTextMessage("10010", null, "CXLL", null, null);
                        break;
                    case 3:
                        // 中国电信
                        smsManager.sendTextMessage("10001",null, "108", null ,null);
                        break;
                }
        }
    }

    class CorrectFlowReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Object[] objs = (Object[]) intent.getExtras().get("pdus");
            for (Object obj : objs) {
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) obj);
                String body = smsMessage.getMessageBody();
                String address = smsMessage.getOriginatingAddress();
                // 以下短信分割只针对中国移动用户
                if (address.equals("10086")) {
                    String[] split = body.split("，");

//                m8
                    System.out.println (split[0]);
                    // 本月剩余流量
                    long left = 0;
                    // 本月已用流量
                    long used = 0;
                    // 本月超出流量
                    long beyond = 0;
                    for (int i = 0; i < split.length; i++) {
                        if (split[i].contains("当月常用流量已用")) {
                            // 套餐总量
                            String usedflow = split[i].substring(9,
                                    split[i].length());
                            used = getStringTofloat(usedflow);
                        } else if (split[i].contains("可用")) {
                            String leftflow = split[i].substring(3,
                                    split[i].length());
                            left = getStringTofloat(leftflow);
                        } else if (split[i].contains("套餐外流量")) {
                            String beyondflow = split[i].substring(6,
                                    split[i].length());
                            beyond = getStringTofloat(beyondflow);
                        }
                    }
                    SharedPreferences.Editor edit = mSP.edit();
//                M8
                    System.out.println ("-----"+left);

                    edit.putLong("totalflow", used + left);
                    edit.putLong("usedflow", used + beyond);
                    edit.commit();
                    mTotalTV.setText("本月流量："
                            + Formatter.formatFileSize(context, (used + left)));
                    mUsedTV.setText("本月已用："
                            + Formatter.formatFileSize(context, (used + beyond)));
                }else if (address.equals("10010")){
                    String[] split = body.split("，");

//                m8
                    System.out.println (split[0]);
                    // 本月剩余流量
                    long left = 0;
                    // 本月已用流量
                    long used = 0;
                    // 本月超出流量
                    long beyond = 0;
                    for (int i = 0; i < split.length; i++) {
                        if (split[i].contains("本月总流量已用")) {
                            // 套餐总量
                            String usedflow = split[i].substring(8,
                                    split[i].length());
                            used = getStringTofloat(usedflow);
                        } else if (split[i].contains("本地流量已用")) {
                            String leftflow = split[i].substring(6,
                                    split[i].length());
                            left = getStringTofloat(leftflow);
                        } else if (split[i].contains("剩余")) {
                            String beyondflow = split[i].substring(3,
                                    split[i].length());
                            beyond = getStringTofloat(beyondflow);
                        }
                    }
                    SharedPreferences.Editor edit = mSP.edit();
//                M8
                    System.out.println ("-----"+left);

                    edit.putLong("totalflow", used + left);
                    edit.putLong("usedflow", used + beyond);
                    edit.commit();
                    mTotalTV.setText("本月流量："
                            + Formatter.formatFileSize(context, (used + left)));
                    mUsedTV.setText("本月已用："
                            + Formatter.formatFileSize(context, (used + beyond)));
                }else if(address.equals("10001")){
                    String[] split = body.split("，");

//                m8
                    System.out.println (split[0]);
                    // 本月剩余流量
                    long left = 0;
                    // 本月已用流量
                    long used = 0;
                    // 本月超出流量
                    long beyond = 0;
                    for (int i = 0; i < split.length; i++) {
                        if (split[i].contains("您当前已使用省内流量")) {
                            // 套餐总量
                            String usedflow = split[i].substring(11,
                                    split[i].length());
                            used = getStringTofloat(usedflow);
                        } else if (split[i].contains("省外流量")) {
                            String leftflow = split[i].substring(4,
                                    split[i].length());
                            left = getStringTofloat(leftflow);
                        } else if (split[i].contains("剩余")) {
                            String beyondflow = split[i].substring(3,
                                    split[i].length());
                            beyond = getStringTofloat(beyondflow);
                        }
                    }
                    SharedPreferences.Editor edit = mSP.edit();
//                M8
                    System.out.println ("-----"+left);

                    edit.putLong("totalflow", used + left);
                    edit.putLong("usedflow", used + beyond);
                    edit.commit();
                    mTotalTV.setText("本月流量："
                            + Formatter.formatFileSize(context, (used + left)));
                    mUsedTV.setText("本月已用："
                            + Formatter.formatFileSize(context, (used + beyond)));
                }

            }
        }
    }

    /** 将字符串转化成Float类型数据 **/

    private long getStringTofloat(String str) {
        long flow = 0;
        if (!TextUtils.isEmpty(str)) {
            if (str.contains("K")) {
                String[] split = str.split("K");
                float m = Float.parseFloat(split[0]);
                flow = (long) (m * 1024);
            } else if (str.contains("M")) {
                String[] split = str.split("M");
                float m = Float.parseFloat(split[0]);
                flow = (long) (m * 1024 * 1024);
            } else if (str.contains("G")) {
                String[] split = str.split("G");
                float m = Float.parseFloat(split[0]);
                flow = (long) (m * 1024 * 1024 * 1024);
            }
        }
        return flow;
    }

    @Override
    public void onDestroy() {
        if (receiver != null) {
            unregisterReceiver(receiver);
            receiver = null;
        }
        super.onDestroy();
    }
}

