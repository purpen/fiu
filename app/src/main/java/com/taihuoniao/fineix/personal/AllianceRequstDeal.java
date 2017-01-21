package com.taihuoniao.fineix.personal;

import android.text.TextUtils;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.taihuoniao.fineix.common.GlobalCallBack;
import com.taihuoniao.fineix.common.GlobalDataCallBack;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.beans.User;
import com.taihuoniao.fineix.main.App;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.SPUtil;

import java.util.HashMap;

/**
 * 处理太火鸟联盟账户
 * Created by Stephen on 2017/1/18 15:43
 * Email: 895745843@qq.com
 */

public class AllianceRequstDeal {
    private static final String ALLIANCE_IDENTIFY = "alliance_id";

    public static void requestAllianceIdentify(final GlobalCallBack globalCallBack){
        HashMap<String, String> map = ClientDiscoverAPI.getgetUserCenterDataRequestParams();
        HttpRequest.post(map, URL.USER_CENTER, new GlobalDataCallBack() {
            @Override
            public void onSuccess(String json) {
                saveAllianceValue(json);
                if (globalCallBack != null) {
                    globalCallBack.callBack(json);
                }
            }

            @Override
            public void onFailure(String error) {
                System.err.print(error);
                if (globalCallBack != null) {
                    globalCallBack.callBack(error);
                }
            }
        });
    }

    private static void saveAllianceValue(String json){
        if (!TextUtils.isEmpty(json)) {
            HttpResponse<User> userHttpResponse = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<User>>() {
            });
            if (userHttpResponse != null && (!TextUtils.isEmpty(userHttpResponse.getData().identify.alliance_id))) {
                SPUtil.write(ALLIANCE_IDENTIFY, userHttpResponse.getData().identify.alliance_id);
            }
        }
    }

    public static void removeAllianceValue(){
        SPUtil.remove(ALLIANCE_IDENTIFY);
    }

    public static String getAllianceValue(){
        return SPUtil.read(ALLIANCE_IDENTIFY);
    }
}
