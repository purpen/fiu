package com.taihuoniao.fineix.zone.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;

import com.taihuoniao.fineix.zone.bean.BrightItemBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lilin on 2017/3/21.
 * id   	is_image   image  content
 * 0          类型		图片   文本内容
 */

public class ZoneBrightSqliteOpenHelper extends SQLiteOpenHelper {
    private static final String TAG = "SqliteOpenHelper";
    private static final String TABLE_NAME="bright";
    public ZoneBrightSqliteOpenHelper(Context context) {
        super(context, "zone_bright.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "CREATE TABLE "+TABLE_NAME+"(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,img TEXT,content TEXT)";
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    //插入图片或文字
    public void insert(Bitmap bitmap, String content) {
        SQLiteDatabase db = getWritableDatabase();
        if (null == bitmap && TextUtils.isEmpty(content)) return;
        ContentValues contentValues = new ContentValues();
//        float i = bitmap.getRowBytes()*bitmap.getHeight()/1024/1024;
//        Log.e(TAG,"内存大小为："+i);
//        if (bitmap != null) contentValues.put("img",Util.bitmap2ByteArray(bitmap));
        if (!TextUtils.isEmpty(content)) contentValues.put("content", content);
        long insert = db.insert(TABLE_NAME, null, contentValues);
        Log.e(TAG,insert+"");
    }

    //插入图片或文字
    public void insert(String path, String content) {
        SQLiteDatabase db = getWritableDatabase();
        if (TextUtils.isEmpty(path) && TextUtils.isEmpty(content)) return;
        ContentValues contentValues = new ContentValues();
        if (!TextUtils.isEmpty(path)) contentValues.put("img",path);
        if (!TextUtils.isEmpty(content)) contentValues.put("content", content);
        db.insert(TABLE_NAME, null, contentValues);
    }


    //删除某条图片或文字记录
    public void delete(int id) {
        SQLiteDatabase db = getWritableDatabase();
        String whereClause = "id=?";
        String[] whereArgs = {String.valueOf(id)};
        db.delete(TABLE_NAME, whereClause, whereArgs);
    }

    //更新数据
    public void update(int id, byte[] img, String content) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        if (img != null) {
            values.put("img", img);
        } else if (!TextUtils.isEmpty(content)) {
            values.put("content", content);
        }
        String whereClause = "id=?";
        String[] whereArgs = {String.valueOf(id)};
        db.update(TABLE_NAME, values, whereClause, whereArgs);
    }

    //查询全部文字或图片
    public List<BrightItemBean> query() {
        SQLiteDatabase db = getReadableDatabase();
        List<BrightItemBean> list = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from "+TABLE_NAME, null);
        while (cursor.moveToNext()) {
//            int id = cursor.getInt(cursor.getColumnIndex("_id"));
           String content = cursor.getString(cursor.getColumnIndex("content"));
           String img = cursor.getString(cursor.getColumnIndex("img"));
            BrightItemBean itemBean = new BrightItemBean();
//            itemBean.id = id;
            itemBean.img = img;
            itemBean.content = content;
            list.add(itemBean);
        }
        return list;
    }

    //删除表
    public void drop() {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "DROP TABLE "+TABLE_NAME+" IF EXISTS";
        db.execSQL(sql);
    }

    //清空表
    public void resetTable(){
        SQLiteDatabase db = getWritableDatabase();
        String sql = "DELETE FROM "+TABLE_NAME;
        db.execSQL(sql);
        String clearIndexSql = "DELETE FROM sqlite_sequence";
        db.execSQL(clearIndexSql);
    }
}
