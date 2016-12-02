package com.taihuoniao.fineix.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.taihuoniao.fineix.utils.LogUtil;

/**
 * Created by Stephen on 2016/12/2 13:08
 * Email: 895745843@qq.com
 */

public class Base2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LogUtil.e("taihuoniao", "---------->onCreate()" + getClass().getSimpleName());
        super.onCreate(savedInstanceState);
    }
}
