//package com.taihuoniao.fineix.scene;
//
//import android.content.Intent;
//import android.util.Log;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.GridView;
//
//import com.google.gson.Gson;
//import com.google.gson.JsonSyntaxException;
//import com.google.gson.reflect.TypeToken;
//import com.lidroid.xutils.exception.HttpException;
//import com.lidroid.xutils.http.ResponseInfo;
//import com.lidroid.xutils.http.callback.RequestCallBack;
//import com.taihuoniao.fineix.R;
//import com.taihuoniao.fineix.adapters.SceneCategoryAdapter;
//import com.taihuoniao.fineix.base.BaseActivity;
//import com.taihuoniao.fineix.beans.CategoryListBean;
//import com.taihuoniao.fineix.beans.QingJingItem;
//import com.taihuoniao.fineix.beans.QingJingListBean;
//import com.taihuoniao.fineix.beans.SearchBean;
//import com.taihuoniao.fineix.network.ClientDiscoverAPI;
//import com.taihuoniao.fineix.network.DataConstants;
//import com.taihuoniao.fineix.utils.ToastUtils;
//import com.taihuoniao.fineix.view.GlobalTitleLayout;
//import com.taihuoniao.fineix.view.WaittingDialog;
//
//import java.lang.reflect.Type;
//import java.util.ArrayList;
//import java.util.List;
//
//import butterknife.Bind;
//
///**
// * Created by taihuoniao on 2016/7/21.
// * 情景分类
// */
//public class CategoryActivity extends BaseActivity implements AdapterView.OnItemClickListener {
//    @Bind(R.id.title_layout)
//    GlobalTitleLayout titleLayout;
//    @Bind(R.id.grid_view)
//    GridView gridView;
//    WaittingDialog dialog;
//    private List<CategoryListBean.CategoryListItem> list;
//    private SceneCategoryAdapter sceneCategoryAdapter;
//
//    public CategoryActivity() {
//        super(R.layout.activity_category);
//    }
//
//    @Override
//    protected void initView() {
//        titleLayout.setBackgroundResource(R.color.white);
//        titleLayout.setTitle(R.string.scene_category, getResources().getColor(R.color.black333333));
//        titleLayout.setBackImg(R.mipmap.back_black);
//        dialog = new WaittingDialog(this);
//        list = new ArrayList<>();
//        sceneCategoryAdapter = new SceneCategoryAdapter(list, this);
//        gridView.setAdapter(sceneCategoryAdapter);
//        gridView.setOnItemClickListener(this);
//    }
//
//    @Override
//    protected void requestNet() {
//        if (!dialog.isShowing()) {
//            dialog.show();
//        }
//        ClientDiscoverAPI.categoryList(1 + "", 12 + "", null, new RequestCallBack<String>() {
//            @Override
//            public void onSuccess(ResponseInfo<String> responseInfo) {
//                Log.e("<<<情景分类", responseInfo.result);
//                CategoryListBean categoryListBean = new CategoryListBean();
//                try {
//                    Gson gson = new Gson();
//                    Type type = new TypeToken<CategoryListBean>() {
//                    }.getType();
//                    categoryListBean = gson.fromJson(responseInfo.result, type);
//                } catch (JsonSyntaxException e) {
//                    Log.e("<<<", "情景分类解析异常");
//                }
//                dialog.dismiss();
//                list.addAll(categoryListBean.getData().getRows());
//                sceneCategoryAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onFailure(HttpException error, String msg) {
//                dialog.dismiss();
//                ToastUtils.showError(R.string.net_fail);
//            }
//        });
//    }
//
//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        Intent intent = new Intent(this, SelectAllQingjingActivity.class);
//        intent.putExtra("pos", position);
//        startActivityForResult(intent, 0);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (data == null)
//            return;
//        switch (resultCode) {
//            case DataConstants.RESULTCODE_MAP_SELECTQJ:
//                QingJingItem qingJing = (QingJingItem) data.getSerializableExtra("qingjing");
//                if (qingJing != null) {
//                    Intent intent1 = new Intent();
//                    intent1.putExtra("qingjing", qingJing);
//                    setResult(3, intent1);
//                    finish();
//                }
//                break;
//            case DataConstants.RESULTCODE_CREATESCENE_SELECTQJ:
//                QingJingListBean.QingJingItem qingJingItem = (QingJingListBean.QingJingItem) data.getSerializableExtra("qingjing");
//                if (qingJingItem != null) {
//                    Intent intent = new Intent();
//                    intent.putExtra("qingjing", qingJingItem);
//                    setResult(4, intent);
//                    finish();
//                }
//                break;
//            case DataConstants.RESULTCODE_CREATESCENE_SEARCHQJ:
//                SearchBean.Data.SearchItem searchItem = (SearchBean.Data.SearchItem) data.getSerializableExtra("searchqj");
//                if (searchItem != null) {
//                    Intent intent = new Intent();
//                    intent.putExtra("searchqj", searchItem);
//                    setResult(5, intent);
//                    finish();
//                }
//                break;
//            case DataConstants.RESULTCODE_SELECTQJ_ALLQJ:
//                QingJingListBean.QingJingItem qingJingItem1 = (QingJingListBean.QingJingItem) data.getSerializableExtra("qingjing");
//                if (qingJingItem1 != null) {
//                    Intent intent = new Intent();
//                    intent.putExtra("qingjing", qingJingItem1);
//                    setResult(6, intent);
//                    finish();
//                }
//                break;
//        }
//    }
//}
