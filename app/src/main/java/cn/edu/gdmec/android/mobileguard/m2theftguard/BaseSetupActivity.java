package cn.edu.gdmec.android.mobileguard.m2theftguard;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.Window;
import android.widget.Toast;

import cn.edu.gdmec.android.mobileguard.R;

/**
 * Created by ASUS PRO on 2017/10/12.
 */

public abstract class BaseSetupActivity extends AppCompatActivity{
    public SharedPreferences sp;

    private GestureDetector mGestureDetector;

    public abstract void showNext();

    public abstract void showPre();

    @Override
    public boolean onTouchEvent(MotionEvent event){
        mGestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    public void startActivityAndFinishSelf(Class<?>cls){
        Intent intent = new Intent(this,cls);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        sp = getSharedPreferences("config",MODE_PRIVATE);

        mGestureDetector = new GestureDetector(this,new  GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onFling(MotionEvent e1,MotionEvent e2,float velocityX,float velocityY){
                if (Math.abs(velocityX)<200){
                    Toast.makeText(getApplicationContext(),
                            "无效动作，移动太慢",Toast.LENGTH_SHORT).show();
                    return true;
                }

                if ((e2.getRawX() - e1.getRawX())>200){
                    showPre();
                    overridePendingTransition(R.anim.pre_in,
                            R.anim.pre_out);
                    return true;
                }

                if ((e1.getRawX() - e2.getRawX())>200){
                    showNext();
                    overridePendingTransition(R.anim.next_in,
                            R.anim.next_out);
                    return true;
                }
                return super.onFling(e1,e2,velocityX,velocityY);
            }
        });
    }
}
