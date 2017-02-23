package com.taihuoniao.fineix.home;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

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

    public static void goToIntent(final Context context, int type, final String url){
        Intent intent;
        switch (type) {
            case 1:      //url地址
                Uri uri = Uri.parse(url);
                intent = new Intent(Intent.ACTION_VIEW, uri);
                context.startActivity(intent);
                break;
            case 2://商品
                intent = new Intent(context, BuyGoodsDetailsActivity.class);
                intent.putExtra("id", url);
                context.startActivity(intent);
                break;
            case 4://app专题
                intent = new Intent(context, SubjectActivity.class);
                intent.putExtra(SubjectActivity.class.getSimpleName(), url);
                intent.putExtra(SubjectActivity.class.getName(), url);
                context.startActivity(intent);
                break;
            case 8:     //情境
                intent = new Intent(context, QJDetailActivity.class);
                intent.putExtra("id", url);
                context.startActivity(intent);
                break;
            case 9:     //产品
                intent = new Intent(context, BuyGoodsDetailsActivity.class);
                intent.putExtra("id", url);
                context.startActivity(intent);
                break;
            case 11:    //情境专题
                HashMap<String, String> params = ClientDiscoverAPI.getgetSubjectDataRequestParams(url);
                HttpRequest.post(params, URL.SCENE_SUBJECT_VIEW, new GlobalDataCallBack(){
                    @Override
                    public void onSuccess(String json) {
                        HttpResponse<SubjectData> response = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<SubjectData>>() {});
                        if (response.isSuccess()) {
                            SubjectData data = response.getData();
                            goNext(context, data.type, url);
                            return;
                        }
                        ToastUtils.showError(response.getMessage());
                    }

                    @Override
                    public void onFailure(String error) {
                        ToastUtils.showError(R.string.network_err);
                    }
                });
                break;
            case 12: //地盘详情
                intent = new Intent(context, ZoneDetailActivity.class);
                intent.putExtra("id", url);
                context.startActivity(intent);
                break;
        }
    }

    public static void goNext(Context context, int type, String url){
        Intent intent;
        switch (type) {
            case 1: //文章详情
                intent = new Intent(context, ArticalDetailActivity.class);
                intent.putExtra(ArticalDetailActivity.class.getSimpleName(), url);
                context.startActivity(intent);
                break;
            case 2: //活动详情
                intent = new Intent(context, ActivityDetailActivity.class);
                intent.putExtra(ActivityDetailActivity.class.getSimpleName(), url);
                context.startActivity(intent);
                break;
            case 4: //新品
                intent = new Intent(context, NewProductDetailActivity.class);
                intent.putExtra(NewProductDetailActivity.class.getSimpleName(), url);
                context.startActivity(intent);
                break;
            case 3: //促销
                intent = new Intent(context, SalePromotionDetailActivity.class);
                intent.putExtra(SalePromotionDetailActivity.class.getSimpleName(), url);
                context.startActivity(intent);
                break;
        }
    }
}
