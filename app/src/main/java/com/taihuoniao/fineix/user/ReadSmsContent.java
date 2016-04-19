package com.taihuoniao.fineix.user;

import android.app.Activity;
import android.content.ContentValues;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 读取短信验证码，并设置验证码
 * 2016/1/19.
 */
public class ReadSmsContent extends ContentObserver {
    private Cursor cursor = null;
    private Activity mActivity;
    private EditText mEditText;

    public ReadSmsContent(Handler handler, Activity activity, EditText editText) {
        super(handler);
        this.mActivity = activity;
        this.mEditText = editText;
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
        Log.e("tag", "onChange");
        // 读取收件箱中指定号码的短信
        cursor = mActivity.managedQuery(Uri.parse("content://sms/inbox"), new String[]{"_id", "address", "body", "read"}, null, null, "_id desc");

        if (cursor != null && cursor.getCount() > 0) {
            ContentValues values = new ContentValues();
            values.put("read", "1");        //修改短信为已读模式
            cursor.moveToNext();
            int smsbodyColumn = cursor.getColumnIndex("body");
            String smsBody = cursor.getString(smsbodyColumn);
            Log.e("399", "smsBody :" + smsBody);
            String verifyCode = getDynamicPassword(smsBody);
            if(TextUtils.isEmpty(verifyCode)){
                return;
            }
            if(mEditText == null) {
                throw new RuntimeException("你传的EditText为空");
            }
            mEditText.setText(verifyCode);
            //EditText获取焦点，3个属性必须同时设置
            mEditText.setFocusable(true);
            mEditText.setFocusableInTouchMode(true);
            mEditText.requestFocus();
            mEditText.setSelection(verifyCode.length());//设置光标位置
        }
        //在用managedQuery的时候，不能主动调用close()方法， 否则在Android 4.0+的系统上，         会发生崩溃
        if (Build.VERSION.SDK_INT < 14) {
            cursor.close();
        }
    }

    /**
     * 从字符串中截取连续4位数字
     * 用于从短信中获取动态密码
     *
     * @param str 短信内容
     * @return 截取得到的6位动态密码
     */
    public String getDynamicPassword(String str) {
        Pattern continuousNumberPattern = Pattern.compile("[0-9\\.]+");
        Matcher m = continuousNumberPattern.matcher(str);
        String dynamicPassword = "";
        while (m.find()) {
            if (m.group().length() == 6) {
                dynamicPassword = m.group();
            }
        }

        return dynamicPassword;
    }
}
