package com.taihuoniao.fineix.user;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.SalePromotionDetailAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.beans.ShareContent;
import com.taihuoniao.fineix.beans.SubjectData;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.qingjingOrSceneDetails.CommentListActivity;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.PopupWindowUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.utils.Util;
import com.taihuoniao.fineix.view.CustomHeadView;
import com.taihuoniao.fineix.view.CustomShareView;
import com.taihuoniao.fineix.view.WaittingDialog;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author lilin  文章和新品是H5，每个item去购买是促销
 *         created at 2016/8/23 10:28
 */
public class SalePromotionDetailActivity extends BaseActivity {
    public static final int REQUEST_COMMENT = 100;
    @Bind(R.id.custom_head)
    CustomHeadView custom_head;
    @Bind(R.id.ibtn_favorite)
    TextView ibtnFavorite;
    @Bind(R.id.ibtn_comment)
    TextView ibtnComment;
    @Bind(R.id.ibtn_share)
    TextView ibtnShare;
    @Bind(R.id.lv)
    ListView lv;
    private ImageView iv_banner;
    private TextView tv_title;
    private TextView tv_during;
    private WaittingDialog dialog;
    private String id;
    private SubjectData data;

    public SalePromotionDetailActivity() {
        super(R.layout.activity_sale_promotion_detail);
    }

    @Override
    protected void getIntentData() {
        Intent intent = getIntent();
        if (intent.hasExtra(TAG)) {
            id = intent.getStringExtra(TAG);
        }
    }

    @Override
    protected void initView() {
        custom_head.setHeadCenterTxtShow(true, "促销详情");
        dialog = new WaittingDialog(this);
        View view = Util.inflateView(R.layout.head_sale_promotion_detail, null);
        iv_banner = ButterKnife.findById(view, R.id.iv_banner);
        tv_title = ButterKnife.findById(view, R.id.tv_title);
        tv_during = ButterKnife.findById(view, R.id.tv_during);
        lv.addHeaderView(view);
    }


    @OnClick({R.id.ibtn_share, R.id.ibtn_comment})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.ibtn_share: //分享
                ShareContent content = new ShareContent();
                content.shareTxt = data.title;
                content.url = data.content_view_url;
                content.titleUrl = data.share_view_url;
                content.site = getResources().getString(R.string.app_name);
                content.siteUrl = "http://www.taihuoniao.com/";
                CustomShareView customShareView = new CustomShareView(activity, content);
                customShareView.setOnShareSuccessListener(new CustomShareView.OnShareSuccessListener() {
                    @Override
                    public void onSuccess() {
                        ibtnShare.setText(String.valueOf(data.share_count + 1));
                    }
                });
                PopupWindowUtil.show(activity, customShareView);
                break;
            case R.id.ibtn_comment: //去评论
                if (data == null) return;
                Intent intent = new Intent(activity, CommentListActivity.class);
                intent.putExtra("target_id", String.valueOf(data._id)); //专题id
                intent.putExtra("type", String.valueOf(13));
                intent.putExtra("target_user_id", String.valueOf(data.user_id));
                startActivityForResult(intent, REQUEST_COMMENT);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode) {
            switch (requestCode) {
                case REQUEST_COMMENT:
                    int count = data.getIntExtra(CommentListActivity.class.getSimpleName(), this.data.comment_count);
                    ibtnComment.setText(String.valueOf(count));
                    break;
            }
        }
    }


    @Override
    protected void requestNet() {
        ClientDiscoverAPI.getSubjectData(id, new RequestCallBack<String>() {
            @Override
            public void onStart() {
                if (dialog != null) dialog.show();
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (dialog != null) dialog.dismiss();
                if (TextUtils.isEmpty(responseInfo.result)) return;
                HttpResponse<SubjectData> response = JsonUtil.json2Bean(responseInfo.result, new TypeToken<HttpResponse<SubjectData>>() {
                });

                if (response.isSuccess()) {
                    data = response.getData();
                    refreshUI();
                    return;
                }
                ToastUtils.showError(response.getMessage());
            }

            @Override
            public void onFailure(HttpException e, String s) {
                if (dialog != null) dialog.dismiss();
                ToastUtils.showError(R.string.network_err);
            }
        });
    }

    @Override
    protected void refreshUI() {
        ibtnComment.setText(String.valueOf(data.comment_count));
        ibtnShare.setText(String.valueOf(data.share_count));
        ibtnFavorite.setText(String.valueOf(data.view_count));
        tv_title.setText(data.title);
        tv_during.setText(String.format("促销时间：%s-%s", data.begin_time_at, data.end_time_at));
        ImageLoader.getInstance().displayImage(data.banner_url, iv_banner);
        if (data.products == null) return;
        SalePromotionDetailAdapter adapter = new SalePromotionDetailAdapter(data.products, activity);
        lv.setAdapter(adapter);
    }

}