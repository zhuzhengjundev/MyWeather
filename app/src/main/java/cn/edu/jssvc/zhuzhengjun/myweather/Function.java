package cn.edu.jssvc.zhuzhengjun.myweather;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class Function {

    private MySQLiteOpenHelper mySQLiteOpenHelper;
    private SQLiteDatabase db;
    private ContentValues values;
    private ContentValues values2;

    public void openXls(Context context, Handler handler) {
        mySQLiteOpenHelper = new MySQLiteOpenHelper(context, MySQLiteOpenHelper.DBNAME, null, 1);
        db = mySQLiteOpenHelper.getWritableDatabase();

        // 创建输入流
        AssetManager assetManager = context.getAssets();
        // 获取Excel文件对象
        Workbook rwb = null;
        try {
            rwb = Workbook.getWorkbook(assetManager.open("citycode.xls"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        }
        // 获取文件的指定工作表 默认的第一个
        Sheet sheet = rwb.getSheet(0);
        Log.d("xls文件的行数", sheet.getColumns()+"");
        Log.d("xls文件的列数", sheet.getRows()+"");
        for (int i = 1; i < sheet.getRows(); i++) {
            String id = sheet.getCell(0, i).getContents();
            String cityCode = sheet.getCell(1, i).getContents();
            String englishName = sheet.getCell(2, i).getContents();
            String readmeName = sheet.getCell(3, i).getContents();
            String chinsesName = sheet.getCell(4, i).getContents();
            String country = sheet.getCell(5, i).getContents();
            Log.d("调试", id + "---" + cityCode + "---" + englishName  + "---" + readmeName  + "---" + chinsesName  + "---" + country);

            values2 = new ContentValues();
            values2.put("id",id);
            values2.put("cityCode",cityCode);
            values2.put("englishName",englishName);
            values2.put("readmeName",readmeName);
            values2.put("chinsesName",chinsesName);
            values2.put("country",country);
            db.insert("quanguoMax", null, values2);
        }
        Message message = new Message();
        message.what = 1;
        handler.sendMessage(message);
    }

    public void openXls2(Context context) {
        mySQLiteOpenHelper = new MySQLiteOpenHelper(context, MySQLiteOpenHelper.DBNAME, null, 1);
        db = mySQLiteOpenHelper.getWritableDatabase();

        // 创建输入流
        AssetManager assetManager = context.getAssets();
        // 获取Excel文件对象
        Workbook rwb = null;
        try {
            rwb = Workbook.getWorkbook(assetManager.open("shenfen.xls"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        }
        // 获取文件的指定工作表 默认的第一个
        Sheet sheet = rwb.getSheet(0);
        Log.d("xls文件的行数", sheet.getColumns()+"");
        Log.d("xls文件的列数", sheet.getRows()+"");
        for (int i = 1; i < sheet.getRows(); i++) {
            String country = sheet.getCell(5, i).getContents();

            values = new ContentValues();
            values.put("country",country);
            db.insert("quanguo", null, values);
        }
    }

}
