package cn.edu.gdmec.android.mobileguard.m3communicationguard.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Swindler on 2017/10/30.
 */

public class BlackNumberOpenHelper extends SQLiteOpenHelper{
    private static  String DB_NAME = "my_info";
    private static  int VERSION = 1;
    public BlackNumberOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,int version){
        super(context,name,factory,version);
        this.DB_NAME = name;
        this.VERSION = version;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(" create table blacknumber "+
                " (id integer primary key autoincrement, "+
                " number varchar(20), "+
                " name varchar(255), "+
                " mode integer) ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
