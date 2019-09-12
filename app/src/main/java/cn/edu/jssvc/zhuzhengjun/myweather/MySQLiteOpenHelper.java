package cn.edu.jssvc.zhuzhengjun.myweather;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLiteOpenHelper extends SQLiteOpenHelper {

    private Context mContext;
    public static String DBNAME = "sqlite.db";

    public static String QUANGUO = "create table quanguo("
            + "country text)";

    public static String QUANGUO_MAX = "create table quanguoMax("
            + "id text,"
            + "cityCode text,"
            + "englishName text,"
            + "readmeName text,"
            + "chinsesName text,"
            + "country text)";

    public MySQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(QUANGUO);
        db.execSQL(QUANGUO_MAX);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


}
