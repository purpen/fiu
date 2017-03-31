package com.taihuoniao.fineix.home;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.beans.SubjectData;
import com.taihuoniao.fineix.common.GlobalDataCallBack;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.product.BuyGoodsDetailsActivity;
import com.taihuoniao.fineix.qingjingOrSceneDetails.QJDetailActivity;
import com.taihuoniao.fineix.user.ActivityDetailActivity;
import com.taihuoniao.fineix.user.ArticalDetailActivity;
import com.taihuoniao.fineix.user.NewProductDetailActivity;
import com.taihuoniao.fineix.user.QJSubjectDetailActivity;
import com.taihuoniao.fineix.user.SalePromotionDetailActivity;
import com.taihuoniao.fineix.user.SubjectActivity;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.zone.ZoneDetailActivity;

import java.util.HashMap;

/**
 * Created by Stephen on 2017/2/17 14:30
 * Email: 895745843@qq.com
 */

public class GoToNextUtils {

    /**
     * 专题详情
     */
    public static void goToIntent(final Context context, int type, final String id){
        Intent intent = null;
        switch (type) {
            case 1:     //url地址
                Uri uri = Uri.parse(id);
                intent = new Intent(Intent.ACTION_VIEW, uri);
                break;
            case 2:     //商品
                intent = new Intent(context, BuyGoodsDetailsActivity.class);
                intent.putExtra("id", id);
                break;
            case 4:     //app专题
                intent = new Intent(context, SubjectActivity.class);
                intent.putExtra(SubjectActivity.class.getSimpleName(), id);
                intent.putExtra(SubjectActivity.class.getName(), id);
                break;
            case 8:     //情境
                intent = new Intent(context, QJDetailActivity.class);
                intent.putExtra("id", id);
                break;
            case 9:     //产品
                intent = new Intent(context, BuyGoodsDetailsActivity.class);
                intent.putExtra("id", id);
                break;
            case 11:    //专题
                jump2ThemeDetail(context, id, false);
                break;
            case 12:   //地盘详情
                intent = new Intent(context, ZoneDetailActivity.class);
                intent.putExtra("id", id);
                break;
        }
        if (intent != null) {
            context.startActivity(intent);
        }
    }

    /**
     * 直接跳详情
     * @param context context
     * @param type  type
     * @param id id
     */
    public static void goNext(Context context, int type, String id){
        goNext(context, type, id, false);
    }

    public static void goNext(Context context, int type, String url, boolean hasFlag) {
        Intent intent = null;
        switch (type) {
            case 1: //文章详情
                intent = new Intent(context, ArticalDetailActivity.class);
                intent.putExtra(ArticalDetailActivity.class.getSimpleName(), url);
                break;
            case 2: //活动详情
                intent = new Intent(context, ActivityDetailActivity.class);
                intent.putExtra(ActivityDetailActivity.class.getSimpleName(), url);
                break;
            case 4: //新品详情
                intent = new Intent(context, NewProductDetailActivity.class);
                intent.putExtra(NewProductDetailActivity.class.getSimpleName(), url);
                break;
            case 3: //促销详情
                intent = new Intent(context, SalePromotionDetailActivity.class);
                intent.putExtra(SalePromotionDetailActivity.class.getSimpleName(), url);
                break;
            case 5: // 同3 好货
                intent = new Intent(context, SalePromotionDetailActivity.class);
                intent.putExtra(SalePromotionDetailActivity.class.getSimpleName(), url);
                intent.putExtra("title","好货合集");
                break;
            case 6: //情境专题详情
                intent = new Intent(context, QJSubjectDetailActivity.class);
                intent.putExtra(QJSubjectDetailActivity.class.getSimpleName(), url);
                break;
        }
        if (intent != null) {
            if(hasFlag){
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        }
    }

    public static void jump2ThemeDetail(final Context activity, final String id, final boolean hasFlag) {
        if (TextUtils.isEmpty(id)) return;
        HashMap<String, String> params = ClientDiscoverAPI.getgetSubjectDataRequestParams(id);
        HttpRequest.post(params,URL.SCENE_SUBJECT_VIEW, new GlobalDataCallBack(){
            @Override
            public void onSuccess(String json) {
                HttpResponse<SubjectData> response = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<SubjectData>>() { });
                if (response.isSuccess()) {
                    SubjectData data = response.getData();
                    if (data == null) {
                        return;
                    }
                    goNext(activity, data.type, id, hasFlag);
                    return;
                }
                ToastUtils.showError(response.getMessage());
            }

            @Override
            public void onFailure(String error) {
                ToastUtils.showError(R.string.network_err);
            }
        });
    }
}
