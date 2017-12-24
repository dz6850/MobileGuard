package cn.edu.gdmec.android.mobileguard;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SdkSuppress;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiScrollable;
import android.support.test.uiautomator.UiSelector;
import android.support.test.uiautomator.Until;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.lang.Thread.sleep;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
@SdkSuppress(minSdkVersion = 18)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ExampleInstrumentedTest {
    //包名
    private static final String BASIC_SAMPLE_PACKAGE
            = "cn.edu.gdmec.android.mobileguard";
    //超时时间
    private static final int LAUNCH_TIMEOUT = 5000;
    //设备实例
    private UiDevice mDevice;
    private String str,str1,str2;
    private UiObject result;
    @Before
    public void startMainActivityFromHomeScreen()  {
        // 初始化 UiDevice 实例
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

        // 按home键，返回到主界面
        mDevice.pressHome();

        // 等待应用装载运行
        final String launcherPackage = mDevice.getLauncherPackageName();
        assertThat(launcherPackage, notNullValue());
        mDevice.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)),
                LAUNCH_TIMEOUT);

        // 启动应用
        Context context = InstrumentationRegistry.getContext();

        final Intent intent = context.getPackageManager()
                .getLaunchIntentForPackage(BASIC_SAMPLE_PACKAGE);
        // 开始新的acivity，移除以前的所有实例
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);

        // 等待应用启动
        mDevice.wait(Until.hasObject(By.pkg(BASIC_SAMPLE_PACKAGE).depth(0)),
                LAUNCH_TIMEOUT);
    }

    public boolean waitForUiObject(UiSelector selector,double timeOut) {//等待对象出现
        Date start = new Date();
        boolean result = false;
        while(!result){
            try {
                sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            UiObject it = mDevice.findObject(selector);
            if (it.exists()) {
                result = true;
                break;
            }
            Date end = new Date();
            long time = end.getTime() - start.getTime();
            if (time>timeOut) {
                break;
            }
        }
        return result;
    }
    //@Test
    public  void t0SetupPermission() throws UiObjectNotFoundException, InterruptedException {
        UiDevice.getInstance().pressHome();
        sleep(200);
        mDevice.findObject(new UiSelector().textStartsWith("设置")).clickAndWaitForNewWindow();
        mDevice.swipe(700,700,700,0,100);
        mDevice.swipe(700,700,700,0,100);
        mDevice.findObject(new UiSelector().textStartsWith("应用")).clickAndWaitForNewWindow();
        mDevice.findObject(new UiSelector().textStartsWith("MobileGuard")).clickAndWaitForNewWindow();
        mDevice.findObject(new UiSelector().textStartsWith("权限")).clickAndWaitForNewWindow();

        List<UiObject2> results;
        results = mDevice.findObjects(By.clazz(CheckBox.class));
        for(UiObject2 item : results){
            item.click();
        }
        UiDevice.getInstance().pressHome();

        //可listview滚动搜索
//        UiScrollable settingsItem = new UiScrollable(new UiSelector()
//                .className("android.widget.ListView"));
//        UiObject about = settingsItem.getChildByText(new UiSelector()
//                .className("android.widget.LinearLayout"), "About tablet");
//        about.click();
    }
    //@Test
    public void t01ShowUpdateDialog() throws UiObjectNotFoundException {
        UiObject result = mDevice.findObject(new UiSelector().textContains("2.0"));
        String str = null;
        str = result.getText();
        assertNotNull("检查到新版本",result);
    }

    //@Test
    public void t02ShowMainActivity() throws UiObjectNotFoundException {
        // 使用UIselector找到包含『版本号』文字的UI组件
        UiObject result = mDevice.findObject(new UiSelector().textStartsWith("暂不升级"));
        result.clickAndWaitForNewWindow();
        result = mDevice.findObject(new UiSelector().textStartsWith("手机防盗"));
        String str = result.getText();
        assertNotNull("出现主界面手机防盗",result);
    }

    //@Test
    public void t03SetupPwd() throws Exception{
        UiObject result = mDevice.findObject(new UiSelector().textStartsWith("暂不升级"));
        result.clickAndWaitForNewWindow();
        result = mDevice.findObject(new UiSelector().textStartsWith("手机防盗"));
        result.clickAndWaitForNewWindow();
        List<UiObject2> results;
        results = mDevice.findObjects(By.clazz(EditText.class));
        UiObject2 pwd1 = results.get(0);
        pwd1.setText("1");
        UiObject2 pwd2 = results.get(1);
        pwd2.setText("1");
        result = mDevice.findObject(new UiSelector().textStartsWith("确认"));
        result.clickAndWaitForNewWindow();
    }

    //@Test
    public void t04EnterPwd() throws Exception{
        UiObject result = mDevice.findObject(new UiSelector().textStartsWith("暂不升级"));
        result.clickAndWaitForNewWindow();
        result = mDevice.findObject(new UiSelector().textStartsWith("手机防盗"));
        result.clickAndWaitForNewWindow();
        List<UiObject2> results;
        results = mDevice.findObjects(By.clazz(EditText.class));
        UiObject2 pwd1 = results.get(0);
        pwd1.setText("1");
        result = mDevice.findObject(new UiSelector().textStartsWith("确认"));
        result.clickAndWaitForNewWindow();
    }

    //@Test
    public void t05SetupFling() throws Exception{
        UiObject result = mDevice.findObject(new UiSelector().textStartsWith("暂不升级"));
        result.clickAndWaitForNewWindow();
        result = mDevice.findObject(new UiSelector().textStartsWith("手机防盗"));
        result.clickAndWaitForNewWindow();
        List<UiObject2> results;
        results = mDevice.findObjects(By.clazz(EditText.class));
        UiObject2 pwd1 = results.get(0);
        pwd1.setText("1");
        result = mDevice.findObject(new UiSelector().textStartsWith("确认"));
        result.clickAndWaitForNewWindow();
        mDevice.wait(Until.hasObject(By.textStartsWith("手机防盗向导")),LAUNCH_TIMEOUT);
        mDevice.swipe(400,300,0,300,100);
        mDevice.wait(Until.hasObject(By.textStartsWith("SIM卡绑定")),LAUNCH_TIMEOUT);
        mDevice.swipe(400,300,0,300,100);
        mDevice.wait(Until.hasObject(By.textStartsWith("选择安全联系人")),LAUNCH_TIMEOUT);
        mDevice.swipe(400,300,0,300,100);
        result = mDevice.findObject(new UiSelector().textStartsWith("恭喜"));
        String str = result.getText();
        assertNotNull("手机防盗向导",result);
    }

    //@Test
    public void t06BindSimNotYet() throws UiObjectNotFoundException {
        UiObject result = mDevice.findObject(new UiSelector().textStartsWith("暂不升级"));
        result.clickAndWaitForNewWindow();
        result = mDevice.findObject(new UiSelector().textStartsWith("手机防盗"));
        result.clickAndWaitForNewWindow();
        List<UiObject2> results;
        results = mDevice.findObjects(By.clazz(EditText.class));
        UiObject2 pwd1 = results.get(0);
        pwd1.setText("1");
        UiObject2 pwd2 = results.get(1);
        pwd2.setText("1");
        result = mDevice.findObject(new UiSelector().textStartsWith("确认"));
        result.clickAndWaitForNewWindow();

        results = mDevice.findObjects(By.clazz(EditText.class));
        UiObject2 pwd = results.get(0);
        pwd.setText("1");
        result = mDevice.findObject(new UiSelector().textStartsWith("确认"));
        result.clickAndWaitForNewWindow();
        mDevice.wait(Until.hasObject(By.textStartsWith("手机防盗向导")),LAUNCH_TIMEOUT);
        mDevice.swipe(400,300,0,300,100);
        mDevice.swipe(400,300,0,300,100);
        mDevice.swipe(400,300,0,300,100);
        mDevice.swipe(400,300,0,300,100);
        mDevice.wait(Until.hasObject(By.textStartsWith("SIM卡绑定")),LAUNCH_TIMEOUT);
        result = mDevice.findObject(new UiSelector().textStartsWith("SIM卡绑定"));
        String str = result.getText();
    }

    //@Test
    public void t07BindSimOk() throws UiObjectNotFoundException {
        UiObject result = mDevice.findObject(new UiSelector().textStartsWith("暂不升级"));
        result.clickAndWaitForNewWindow();
        result = mDevice.findObject(new UiSelector().textStartsWith("手机防盗"));
        result.clickAndWaitForNewWindow();
        List<UiObject2> results;
        results = mDevice.findObjects(By.clazz(EditText.class));
        UiObject2 pwd = results.get(0);
        pwd.setText("1");
        result = mDevice.findObject(new UiSelector().textStartsWith("确认"));
        result.clickAndWaitForNewWindow();
        mDevice.wait(Until.hasObject(By.textStartsWith("手机防盗向导")),LAUNCH_TIMEOUT);
        mDevice.swipe(400,300,0,300,100);
        mDevice.wait(Until.hasObject(By.textStartsWith("SIM卡绑定")),LAUNCH_TIMEOUT);
        results = mDevice.findObjects(By.clazz(Button.class));
        UiObject2 btn = results.get(0);
        assertTrue("bind sim button is enabled",btn.isEnabled());
        btn.click();
        assertFalse("bind sim button is not enabled ",btn.isEnabled());
    }
    //@Test
    public void t08SelectSecurityContacts() throws UiObjectNotFoundException {
        UiObject result = mDevice.findObject(new UiSelector().textStartsWith("暂不升级"));
        result.clickAndWaitForNewWindow();
        result = mDevice.findObject(new UiSelector().textStartsWith("手机防盗"));
        result.clickAndWaitForNewWindow();
        List<UiObject2> results;
        results = mDevice.findObjects(By.clazz(EditText.class));
        UiObject2 pwd = results.get(0);
        pwd.setText("1");
        result = mDevice.findObject(new UiSelector().textStartsWith("确认"));
        result.clickAndWaitForNewWindow();
        mDevice.wait(Until.hasObject(By.textStartsWith("手机防盗向导")),LAUNCH_TIMEOUT);
        mDevice.swipe(400,300,0,300,100);
        mDevice.wait(Until.hasObject(By.textStartsWith("SIM卡绑定")),LAUNCH_TIMEOUT);
        mDevice.swipe(400,300,0,300,100);
        mDevice.wait(Until.hasObject(By.textStartsWith("选择安全联系人")),LAUNCH_TIMEOUT);
        result = mDevice.findObject(new UiSelector().className("android.widget.Button"));
        result.clickAndWaitForNewWindow();
        UiScrollable contactList = new UiScrollable( new UiSelector().className("android.widget.ListView"));
        UiObject note = contactList.getChildByText(new UiSelector().className("android.widget.TextView"), "York Cui", true);
        note.clickAndWaitForNewWindow();
        result = mDevice.findObject(new UiSelector().className("android.widget.EditText"));
        String str = result.getText();
        assertEquals("security contact phonenumber",str,"1 376-079-5885");
    }

    //@Test
    public void t09ConfirmDeviceAdmin() throws UiObjectNotFoundException {
        UiObject result = mDevice.findObject(new UiSelector().textStartsWith("激活此设备管理员"));
        String str = result.getText();
        result.clickAndWaitForNewWindow();
        result = mDevice.findObject(new UiSelector().textStartsWith("手机防盗"));
        str = result.getText();
    }
    //@Test
    public void t10SetupTheftGuard() throws UiObjectNotFoundException {
        UiObject result = mDevice.findObject(new UiSelector().textStartsWith("手机防盗"));
        result.clickAndWaitForNewWindow();
        List<UiObject2> results;
        results = mDevice.findObjects(By.clazz(EditText.class));
        UiObject2 pwd1 = results.get(0);
        pwd1.setText("1");
        UiObject2 pwd2 = results.get(1);
        pwd2.setText("1");
        result = mDevice.findObject(new UiSelector().textStartsWith("确认"));
        result.clickAndWaitForNewWindow();

        results = mDevice.findObjects(By.clazz(EditText.class));
        UiObject2 pwd = results.get(0);
        pwd.setText("1");
        result = mDevice.findObject(new UiSelector().textStartsWith("确认"));
        result.clickAndWaitForNewWindow();
        mDevice.wait(Until.hasObject(By.textStartsWith("手机防盗向导")),LAUNCH_TIMEOUT);
        mDevice.swipe(400,300,0,300,50);
        mDevice.wait(Until.hasObject(By.textStartsWith("SIM卡绑定")),LAUNCH_TIMEOUT);
        results = mDevice.findObjects(By.clazz(Button.class));
        UiObject2 btn = results.get(0);
        btn.click();
        mDevice.swipe(400,300,0,300,50);
        result = mDevice.findObject(new UiSelector().className("android.widget.EditText"));
        result.setText("110");
        mDevice.swipe(400,300,0,300,50);
        result = mDevice.findObject(new UiSelector().textStartsWith("防盗保护已经开启"));
        String str = result.getText();
        result = mDevice.findObject(new UiSelector().className("android.widget.ToggleButton"));
        result.click();
        result = mDevice.findObject(new UiSelector().textStartsWith("防盗保护没有开启"));
        str = result.getText();
        result = mDevice.findObject(new UiSelector().className("android.widget.ToggleButton"));
        result.click();
        mDevice.swipe(400,300,0,300,50);
    }
    //@Test
    public void t11ReSetupTheftGuard() throws UiObjectNotFoundException {
        UiObject result = mDevice.findObject(new UiSelector().textStartsWith("手机防盗"));
        result.clickAndWaitForNewWindow();
        List<UiObject2> results;
        results = mDevice.findObjects(By.clazz(EditText.class));
        UiObject2 pwd = results.get(0);
        pwd.setText("1");
        result = mDevice.findObject(new UiSelector().textStartsWith("确认"));
        result.clickAndWaitForNewWindow();
        result = mDevice.findObject(new UiSelector().textStartsWith("重新进入设置向导"));
        result.click();
        result = mDevice.findObject(new UiSelector().textStartsWith("手机防盗向导"));
        String str = result.getText();
    }
    //@Test
    public void t12CommunicationGuardWithoutTitleBar() throws Exception {
        result = mDevice.findObject(new UiSelector().textStartsWith("激活此设备管理员"));
        result.clickAndWaitForNewWindow();
        result = mDevice.findObject(new UiSelector().textStartsWith("通讯卫士"));
        result.clickAndWaitForNewWindow();
        result = mDevice.findObject(new UiSelector().textStartsWith("MobileGuard"));
        if(result.exists()){
            throw new Exception("Found Titlebar in SecurityPhoneActivity.");
        }

    }
    //@Test
    public void t13AddBlackCannotNull() throws Exception {
        UiObject result = mDevice.findObject(new UiSelector().textStartsWith("通讯卫士"));
        result.clickAndWaitForNewWindow();
        result = mDevice.findObject(new UiSelector().className("android.widget.Button"));
        result.clickAndWaitForNewWindow();
        result = mDevice.findObject(new UiSelector().className("android.widget.Button"));
        result.clickAndWaitForNewWindow();
        result = mDevice.findObject(new UiSelector().textStartsWith("通讯卫士"));
        if(result.exists()){
            throw new Exception("No implement of valid function for phonenumber and name in AddBlakNumberActivity .");
        }
    }
    //@Test
    public void t14AddContactName() throws Exception {
        UiObject result = mDevice.findObject(new UiSelector().textStartsWith("通讯卫士"));
        result.clickAndWaitForNewWindow();
        result = mDevice.findObject(new UiSelector().className("android.widget.Button"));
        result.clickAndWaitForNewWindow();
        List<UiObject2> results;
        results = mDevice.findObjects(By.clazz(EditText.class));
        UiObject2 name = results.get(1);
        name.setText("");
        results = mDevice.findObjects(By.clazz(Button.class));
        UiObject2 addFromContact = results.get(1);
        addFromContact.click();
        UiScrollable contactList = new UiScrollable( new UiSelector().className("android.widget.ListView"));
        UiObject note = contactList.getChildByText(new UiSelector().className("android.widget.TextView"), "York Cui", true);
        note.clickAndWaitForNewWindow();
        results = mDevice.findObjects(By.clazz(EditText.class));
        name = results.get(1);
        String str = name.getText();
        assertEquals("get name from contact list",str,"York Cui");
    }
    //@Test
    public void t15BlacknameListViewScroll() throws Exception {
        UiObject result = mDevice.findObject(new UiSelector().textStartsWith("通讯卫士"));
        result.clickAndWaitForNewWindow();
        UiScrollable  blackList = new UiScrollable(new UiSelector().className("android.widget.ListView"));
        result = blackList.getChildByInstance(new UiSelector().className("android.widget.TextView"), 0);
        str = result.getText();
        mDevice.swipe(200,600,200,200,50);
        sleep(50);
        mDevice.swipe(200,600,200,200,50);
        sleep(50);
        result = blackList.getChildByInstance(new UiSelector().className("android.widget.TextView"), 0);
        str1 = result.getText();
        if (str1.equals(str)){
            throw new Exception("Blacklist can't be scrolled to load more items.");
        }
    }
    //@Test
    public void t16AppManager() throws Exception {
        result = mDevice.findObject(new UiSelector().textStartsWith("激活此设备管理员"));
        result.clickAndWaitForNewWindow();
        result = mDevice.findObject(new UiSelector().textStartsWith("软件管家"));
        result.clickAndWaitForNewWindow();
        result = mDevice.findObject(new UiSelector().textStartsWith("App1"));
        result.clickAndWaitForNewWindow();
        result = mDevice.findObject(new UiSelector().textStartsWith("启动"));
        result.clickAndWaitForNewWindow();
        result = mDevice.findObject(new UiSelector().textStartsWith("Hello World!"));
        if(!result.exists()){
            throw new Exception("AppManager can't startup app .");
        }
    }
    //@Test
    public void t17AppManagerAboutButton() throws Exception {
        result = mDevice.findObject(new UiSelector().textStartsWith("软件管家"));
        result.clickAndWaitForNewWindow();
        result = mDevice.findObject(new UiSelector().textStartsWith("App1"));
        result.clickAndWaitForNewWindow();
        result = mDevice.findObject(new UiSelector().textStartsWith("关于"));
        if(!result.exists()){
            throw new Exception("Can't find about button in AppManager.");
        }
    }

    //@Test
    public void t18AppManagerAboutVersion() throws Exception {
        result = mDevice.findObject(new UiSelector().textStartsWith("软件管家"));
        result.clickAndWaitForNewWindow();
        result = mDevice.findObject(new UiSelector().textStartsWith("App1"));
        result.clickAndWaitForNewWindow();
        result = mDevice.findObject(new UiSelector().textStartsWith("关于"));
        result.clickAndWaitForNewWindow();
        result = mDevice.findObject(new UiSelector().textContains("1.3"));
        if(!result.exists()){
            throw new Exception("Can't get app version.");
        }
    }

    //@Test
    public void t19AppManagerAboutVersion() throws Exception {
        result = mDevice.findObject(new UiSelector().textStartsWith("软件管家"));
        result.clickAndWaitForNewWindow();
        result = mDevice.findObject(new UiSelector().textStartsWith("App1"));
        result.clickAndWaitForNewWindow();
        result = mDevice.findObject(new UiSelector().textStartsWith("关于"));
        result.clickAndWaitForNewWindow();
        result = mDevice.findObject(new UiSelector().textContains("CN=York"));
        if(!result.exists()){
            throw new Exception("Can't get app signature issuer message.");
        }
    }
    //@Test
    public void t20AppManagerAboutVersion() throws Exception {
        result = mDevice.findObject(new UiSelector().textStartsWith("软件管家"));
        result.clickAndWaitForNewWindow();
        result = mDevice.findObject(new UiSelector().textStartsWith("App1"));
        result.clickAndWaitForNewWindow();
        result = mDevice.findObject(new UiSelector().textStartsWith("关于"));
        result.clickAndWaitForNewWindow();
        result = mDevice.findObject(new UiSelector().textContains("android.permission"));
        if(!result.exists()){
            throw new Exception("Can't get app request permissions.");
        }
    }
    //@Test
    public void t21VirusScan() throws Exception {
        result = mDevice.findObject(new UiSelector().textStartsWith("激活此设备管理员"));
        result.clickAndWaitForNewWindow();
        result = mDevice.findObject(new UiSelector().textStartsWith("手机杀毒"));
        result.clickAndWaitForNewWindow();
        UiObject result = mDevice.findObject(new UiSelector().textStartsWith("暂不升级"));
        result.clickAndWaitForNewWindow();
        result = mDevice.findObject(new UiSelector().textStartsWith("病毒查杀"));
        if(!result.exists()){
            throw new Exception("Can't open VirusScan Activity.");
        }
    }
    //@Test
    public void t22VirusDbUpdate() throws Exception {
        result = mDevice.findObject(new UiSelector().textStartsWith("手机杀毒"));
        result.clickAndWaitForNewWindow();
        UiObject result = mDevice.findObject(new UiSelector().textStartsWith("立刻升级"));
        result.clickAndWaitForNewWindow();
        result = mDevice.findObject(new UiSelector().textContains("2017.1.1"));
        if(!result.exists()){
            throw new Exception("Can't update Virus Database.");
        }
    }

    //@Test
    public void t23StopAndRestartScan() throws Exception {
        result = mDevice.findObject(new UiSelector().textStartsWith("手机杀毒"));
        result.clickAndWaitForNewWindow();
        result = mDevice.findObject(new UiSelector().textStartsWith("全盘扫描"));
        result.clickAndWaitForNewWindow();
        result = mDevice.findObject(new UiSelector().className("android.widget.Button"));
        result.clickAndWaitForNewWindow();
        UiScrollable  appList = new UiScrollable(new UiSelector().className("android.widget.ListView"));
        appList.flingToBeginning(5);
        UiObject result = appList.getChildByText(new UiSelector().className("android.widget.TextView"),"App1(课程测试案例)",true);
        if(!result.exists()){
            throw new Exception("Can't find virus.");
        }
    }

    //@Test
    public void t24EnterCleanCache() throws Exception {
        result = mDevice.findObject(new UiSelector().textStartsWith("激活此设备管理员"));
        result.clickAndWaitForNewWindow();
        result = mDevice.findObject(new UiSelector().textStartsWith("缓存清理"));
        result.clickAndWaitForNewWindow();
        result = mDevice.findObject(new UiSelector().textStartsWith("缓存扫描"));
        if(!result.exists()){
            throw new Exception("Can't enter CacheScanActivity.");
        }
    }
    //@Test
    public void t25ScanCache() throws Exception {
        result = mDevice.findObject(new UiSelector().textStartsWith("缓存清理"));
        result.clickAndWaitForNewWindow();
        sleep(5000);
        result = mDevice.findObject(new UiSelector().textStartsWith("已扫描"));
        str = result.getText();
        System.out.println(str);
        if(!result.exists()){
            throw new Exception("Can't enter CacheScanActivity.");
        }
    }
    //@Test
    public void t26CleanCache() throws Exception {
        result = mDevice.findObject(new UiSelector().textStartsWith("缓存清理"));
        result.clickAndWaitForNewWindow();
        sleep(5000);
        result = mDevice.findObject(new UiSelector().className("android.widget.Button"));
        result.clickAndWaitForNewWindow();
        result = mDevice.findObject(new UiSelector().textStartsWith("成功清理"));
        str = result.getText();
        if(!result.exists()){
            throw new Exception("Can't enter CacheScanActivity.");
        }
    }
    @Test
    public void t27EnterTrafficMonitoring() throws Exception {
        result = mDevice.findObject(new UiSelector().textStartsWith("关闭"));
        result.clickAndWaitForNewWindow();
        result = mDevice.findObject(new UiSelector().textStartsWith("允许访问使用记录"));
        result.clickAndWaitForNewWindow();
        mDevice.pressBack();
        mDevice.pressBack();
        result = mDevice.findObject(new UiSelector().textStartsWith("暂不升级"));
        result.clickAndWaitForNewWindow();
        result = mDevice.findObject(new UiSelector().textStartsWith("激活此设备管理员"));
        result.clickAndWaitForNewWindow();
        result = mDevice.findObject(new UiSelector().textStartsWith("流量统计"));
        result.clickAndWaitForNewWindow();
        result = mDevice.findObject(new UiSelector().textStartsWith("中国移动"));
        if(!result.exists()){
            throw new Exception("Can't enter TrafficMonitoringActivity.");
        }
    }
    @Test
    public void t28SetupOperator() throws Exception {
        result = mDevice.findObject(new UiSelector().textStartsWith("暂不升级"));
        result.clickAndWaitForNewWindow();
        result = mDevice.findObject(new UiSelector().textStartsWith("流量统计"));
        result.clickAndWaitForNewWindow();
        result = mDevice.findObject(new UiSelector().className("android.widget.Button"));
        result.clickAndWaitForNewWindow();
        result = mDevice.findObject(new UiSelector().textStartsWith("本日已用"));
            if(!result.exists()){
            throw new Exception("Can't setup operator.");
        }
    }
    @Test
    public void t29CorrectFlow() throws Exception {
        result = mDevice.findObject(new UiSelector().textStartsWith("暂不升级"));
        result.clickAndWaitForNewWindow();
        result = mDevice.findObject(new UiSelector().textStartsWith("流量统计"));
        result.clickAndWaitForNewWindow();
        result = mDevice.findObject(new UiSelector().className("android.widget.Button"));
        result.clickAndWaitForNewWindow();
        result = mDevice.findObject(new UiSelector().textStartsWith("发送"));
        if(!result.exists()){
            throw new Exception("Can't correct flow.");
        }
        result.clickAndWaitForNewWindow();
    }
    @Test
    public void t30ResetOperator() throws Exception {
        result = mDevice.findObject(new UiSelector().textStartsWith("暂不升级"));
        result.clickAndWaitForNewWindow();
        result = mDevice.findObject(new UiSelector().textStartsWith("流量统计"));
        result.clickAndWaitForNewWindow();
        result = mDevice.findObject(new UiSelector().textStartsWith("流量监控"));
        Rect viewRect = result.getBounds();//获取坐标
        int x = viewRect.centerX();
        int y = viewRect.centerY();
        System.out.println(x+":"+y);
        mDevice.click(445,y);
        sleep(500);
        result = mDevice.findObject(new UiSelector().textStartsWith("运营商信息"));
        if(!result.exists()){
            throw new Exception("Can't reset opeartor.");
        }
    }
    @Test
    public void t31EnterAdvancedTool() throws Exception {
        result = mDevice.findObject(new UiSelector().textStartsWith("暂不升级"));
        result.clickAndWaitForNewWindow();
        result = mDevice.findObject(new UiSelector().textStartsWith("高级工具"));
        result.clickAndWaitForNewWindow();
        result = mDevice.findObject(new UiSelector().textStartsWith("号码归属地查询"));
        if(!result.exists()){
            throw new Exception("Can't enter AdvancedTool.");
        }
    }
    @Test
    public void t32NumberBelongTo() throws Exception {
        result = mDevice.findObject(new UiSelector().textStartsWith("暂不升级"));
        result.clickAndWaitForNewWindow();
        result = mDevice.findObject(new UiSelector().textStartsWith("高级工具"));
        result.clickAndWaitForNewWindow();
        result = mDevice.findObject(new UiSelector().textStartsWith("号码归属地查询"));
        result.clickAndWaitForNewWindow();
        result = mDevice.findObject(new UiSelector().className("android.widget.EditText"));
        result.setText("13760795885");
        result = mDevice.findObject(new UiSelector().className("android.widget.Button"));
        result.click();
        result = mDevice.findObject(new UiSelector().textContains("广东广州移动"));
        if(!result.exists()){
            throw new Exception("Can't find phone number belong to.");
        }
    }
    @Test
    public void t33AppLock() throws Exception {
        result = mDevice.findObject(new UiSelector().textStartsWith("暂不升级"));
        result.clickAndWaitForNewWindow();
        result = mDevice.findObject(new UiSelector().textStartsWith("高级工具"));
        result.clickAndWaitForNewWindow();
        result = mDevice.findObject(new UiSelector().textStartsWith("程序锁"));
        if(!result.exists()){
            throw new Exception("Can't find App lock.");
        }
    }
    @Test
    public void t34AddAppLock() throws Exception {
        result = mDevice.findObject(new UiSelector().textStartsWith("暂不升级"));
        result.clickAndWaitForNewWindow();
        boolean findApp1 = false;
        result = mDevice.findObject(new UiSelector().textStartsWith("高级工具"));
        result.clickAndWaitForNewWindow();
        result = mDevice.findObject(new UiSelector().textStartsWith("程序锁"));
        result.clickAndWaitForNewWindow();
        UiScrollable  appList = new UiScrollable(new UiSelector().className("android.widget.ListView"));
        appList.flingToBeginning(5);
        result = appList.getChildByText(new UiSelector().className("android.widget.TextView"),"App1",true);
        result.clickAndWaitForNewWindow();
        try {
            result = appList.getChildByText(new UiSelector().className("android.widget.TextView"),"App1",true);
            findApp1 = true;
        } catch (UiObjectNotFoundException e) {
        }
        if(findApp1){
            throw new Exception("Can't add App lock.");
        }
    }
    @Test
    public void t35RemoveAppLock() throws Exception {
        result = mDevice.findObject(new UiSelector().textStartsWith("暂不升级"));
        result.clickAndWaitForNewWindow();
        boolean findApp1=false;
        result = mDevice.findObject(new UiSelector().textStartsWith("高级工具"));
        result.clickAndWaitForNewWindow();
        result = mDevice.findObject(new UiSelector().textStartsWith("程序锁"));
        result.clickAndWaitForNewWindow();
        result = mDevice.findObject(new UiSelector().textStartsWith("已加锁"));
        result.clickAndWaitForNewWindow();
        UiScrollable  appList = new UiScrollable(new UiSelector().className("android.widget.ListView"));
        appList.flingToBeginning(5);
        result = appList.getChildByText(new UiSelector().className("android.widget.TextView"),"App1",true);
        result.clickAndWaitForNewWindow();
        try {
            result = appList.getChildByText(new UiSelector().className("android.widget.TextView"),"App1",true);
            findApp1 = true;
        } catch (UiObjectNotFoundException e) {
        }
        if(findApp1){
            throw new Exception("Can't remove App lock.");
        }
    }
    @Test
    public void t36LockApp1() throws Exception {
        result = mDevice.findObject(new UiSelector().textStartsWith("暂不升级"));
        result.clickAndWaitForNewWindow();
        //给App1加锁
        result = mDevice.findObject(new UiSelector().textStartsWith("高级工具"));
        result.clickAndWaitForNewWindow();
        result = mDevice.findObject(new UiSelector().textStartsWith("程序锁"));
        result.clickAndWaitForNewWindow();
        UiScrollable  appList = new UiScrollable(new UiSelector().className("android.widget.ListView"));
        appList.flingToBeginning(5);
        result = appList.getChildByText(new UiSelector().className("android.widget.TextView"),"App1",true);
        result.clickAndWaitForNewWindow();
        mDevice.pressBack();
        mDevice.pressBack();

        Context context = InstrumentationRegistry.getContext();

        //启动AppLockService
        final Intent intentservice = new Intent();
        ComponentName componentName = new ComponentName("cn.edu.gdmec.android.mobileguard"
                ,"cn.edu.gdmec.android.mobileguard.m9advancedtools.service.AppLockService");
        intentservice.setComponent(componentName);
        context.startService(intentservice);

        //设置手机防盗密码
        result = mDevice.findObject(new UiSelector().textStartsWith("手机防盗"));
        result.clickAndWaitForNewWindow();
        List<UiObject2> results;
        results = mDevice.findObjects(By.clazz(EditText.class));
        UiObject2 pwd1 = results.get(0);
        pwd1.setText("1");
        UiObject2 pwd2 = results.get(1);
        pwd2.setText("1");
        result = mDevice.findObject(new UiSelector().textStartsWith("确认"));
        result.clickAndWaitForNewWindow();
        results = mDevice.findObjects(By.clazz(EditText.class));
        UiObject2 pwd = results.get(0);
        pwd.setText("1");
        result = mDevice.findObject(new UiSelector().textStartsWith("确认"));
        result.clickAndWaitForNewWindow();
        mDevice.pressBack();

        //验证app1程序锁
        mDevice.pressHome();
        result = mDevice.findObject(new UiSelector().textStartsWith("App1"));
        result.clickAndWaitForNewWindow();
        result = mDevice.findObject(new UiSelector().className("android.widget.EditText"));
        result.setText("1");
        result = mDevice.findObject(new UiSelector().className("android.widget.ImageView").instance(2));
        result.click();
        sleep(1000);
        result = mDevice.findObject(new UiSelector().className("android.widget.EditText"));
        result.setText("1");
        result = mDevice.findObject(new UiSelector().className("android.widget.ImageView").instance(2));
        result.click();
        sleep(1000);
        result = mDevice.findObject(new UiSelector().className("android.widget.EditText"));
        result.setText("1");
        result = mDevice.findObject(new UiSelector().className("android.widget.ImageView").instance(2));
        result.click();
        sleep(1000);
        result = mDevice.findObject(new UiSelector().textStartsWith("手机防盗"));
        if(!result.exists()){
            throw new Exception("Can't lock App1.");
        }
    }
    @Test
    public void t37BlackNameListType() throws Exception {
        result = mDevice.findObject(new UiSelector().textStartsWith("暂不升级"));
        result.clickAndWaitForNewWindow();
        result = mDevice.findObject(new UiSelector().textStartsWith("通讯卫士"));
        result.clickAndWaitForNewWindow();
        result = mDevice.findObject(new UiSelector().className("android.widget.Button"));
        result.clickAndWaitForNewWindow();
        List<UiObject2> results;
        results = mDevice.findObjects(By.clazz(EditText.class));
        UiObject2 edit = results.get(0);
        edit.setText("999");
        edit = results.get(1);
        edit.setText("aaa");
        edit = results.get(2);
        edit.setText("whynot");
        results = mDevice.findObjects(By.clazz(Button.class));
        edit = results.get(0);
        edit.click();
        sleep(500);
        mDevice.pressBack();
        result = mDevice.findObject(new UiSelector().textStartsWith("通讯卫士"));
        result.clickAndWaitForNewWindow();
        result = mDevice.findObject(new UiSelector().textStartsWith("whynot"));
        if(!result.exists()){
            throw new Exception("Can't find App lock.");
        }
    }
    @Test
    public void t38AppManagerActivities() throws Exception {
        result = mDevice.findObject(new UiSelector().textStartsWith("暂不升级"));
        result.clickAndWaitForNewWindow();
        result = mDevice.findObject(new UiSelector().textStartsWith("软件管家"));
        result.clickAndWaitForNewWindow();
        UiScrollable  appList = new UiScrollable(new UiSelector().className("android.widget.ListView"));
        appList.scrollForward(30);
        result = mDevice.findObject(new UiSelector().textStartsWith("App1"));
        result.clickAndWaitForNewWindow();
        result = mDevice.findObject(new UiSelector().textStartsWith("活动"));
        result.clickAndWaitForNewWindow();
        result = mDevice.findObject(new UiSelector().textContains("android.gdmec.edu.cn.app1.MainActivity"));
        if(!result.exists()){
            throw new Exception("Can't get Activities.");
        }
    }
    @Test
    public void t39CloudScanVirus() throws Exception {
        result = mDevice.findObject(new UiSelector().textStartsWith("暂不升级"));
        result.clickAndWaitForNewWindow();
        result = mDevice.findObject(new UiSelector().textStartsWith("手机杀毒"));
        result.clickAndWaitForNewWindow();
        sleep(1000);
        result = mDevice.findObject(new UiSelector().textStartsWith("暂不升级"));
        result.clickAndWaitForNewWindow();
        result = mDevice.findObject(new UiSelector().textStartsWith("云查杀"));
        result.clickAndWaitForNewWindow();
        //result = mDevice.findObject(new UiSelector().className("android.widget.Button"));
        //result.clickAndWaitForNewWindow();
        UiScrollable  appList = new UiScrollable(new UiSelector().className("android.widget.ListView"));
        appList.flingToBeginning(30);
        UiObject result = appList.getChildByText(new UiSelector().className("android.widget.TextView"),"App2(fake virus.)",true);
        if(!result.exists()){
            throw new Exception("Can't find cloud scan virus.");
        }
    }
    @Test
    public void t40CorrectFlowChinaUnicom() throws Exception {
        result = mDevice.findObject(new UiSelector().textStartsWith("暂不升级"));
        result.clickAndWaitForNewWindow();
        result = mDevice.findObject(new UiSelector().textStartsWith("流量统计"));
        result.clickAndWaitForNewWindow();
        result = mDevice.findObject(new UiSelector().textStartsWith("流量监控"));
        Rect viewRect = result.getBounds();//获取坐标
        int x = viewRect.centerX();
        int y = viewRect.centerY();
        System.out.println(x+":"+y);
        mDevice.click(445,y);
        sleep(500);
        result = mDevice.findObject(new UiSelector().className("android.widget.Spinner"));
        result.click();
        result = mDevice.findObject(new UiSelector().textStartsWith("中国联通"));
        result.click();
        result = mDevice.findObject(new UiSelector().className("android.widget.Button"));
        result.clickAndWaitForNewWindow();
        result = mDevice.findObject(new UiSelector().className("android.widget.Button"));
        result.clickAndWaitForNewWindow();
        result = mDevice.findObject(new UiSelector().textContains("10010"));
        if(!result.exists()){
            throw new Exception("Can't correct flow.");
        }
        result = mDevice.findObject(new UiSelector().textStartsWith("取消"));
        result.clickAndWaitForNewWindow();
    }
}