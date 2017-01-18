package com.taihuoniao.fineix.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.taihuoniao.fineix.utils.LogUtil;

/**
 * Created by Stephen on 2016/12/2 15:21
 * Email: 895745843@qq.com
 */

public class Base2Fragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        LogUtil.e("taihuoniao", "---------->onCreate()" + getClass().getSimpleName());
        super.onCreate(savedInstanceState);
    }
}
