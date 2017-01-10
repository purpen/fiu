package com.taihuoniao.fineix;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.taihuoniao.fineix.utils.JsonUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    public void test() {
        List<Map<String, String>> list = new ArrayList<>();
        for(int i = 0; i< 3; i++) {
            Map<String, String> map = new HashMap<>();
            map.put("name", "Name" + (char)i);
            map.put("age", String.valueOf(new Random().nextInt(20) + 10));
            list.add(map);
        }
        System.out.print(list.toString());
        System.out.print(JsonUtil.list2Json(list));
    }
}