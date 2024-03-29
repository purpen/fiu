package com.taihuoniao.fineix.scene;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Rect;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.SearchViewPagerAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.common.GlobalDataCallBack;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.ActiveTagsBean;
import com.taihuoniao.fineix.beans.CategoryListBean;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.qingjingOrSceneDetails.fragment.SearchFragment;
import com.taihuoniao.fineix.scene.fragments.AddEnvirFragment;
import com.taihuoniao.fineix.utils.DensityUtils;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.LogUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.view.GlobalTitleLayout;
import com.taihuoniao.fineix.view.dialog.WaittingDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import okhttp3.Call;

/**
 * Created by taihuoniao on 2016/9/2.
 */
public class AddEnvirActivity extends BaseActivity implements View.OnClickListener, View.OnLayoutChangeListener {
    //上个界面传递过来的数据
    private String titleIntent, desIntent;
    private View activityView;
    @Bind(R.id.background_img)
    ImageView backgroundImg;
    @Bind(R.id.title_layout)
    GlobalTitleLayout titleLayout;
    @Bind(R.id.title)
    public EditText title;
    @Bind(R.id.delete_title)
    ImageView deleteTitle;
    @Bind(R.id.des)
    public EditText des;
    @Bind(R.id.delete_des)
    ImageView deleteDes;
    @Bind(R.id.search_linear)
    LinearLayout searchLinear;
    @Bind(R.id.tab_layout)
    TabLayout tabLayout;
    @Bind(R.id.view_pager)
    ViewPager viewPager;
    @Bind(R.id.gone_linear)
    RelativeLayout goneLinear;
    @Bind(R.id.gone_label)
    TextView goneLabel;
    public WaittingDialog dialog;
    private List<SearchFragment> fragmentList;
    private List<String> titleList;
    private SearchViewPagerAdapter searchViewPagerAdapter;

    @Override
    protected void getIntentData() {
        titleIntent = getIntent().getStringExtra("title");
        desIntent = getIntent().getStringExtra("des");
        if (titleIntent == null) {
            titleIntent = "";
        }
        if (desIntent == null) {
            desIntent = "";
        }
    }

    public AddEnvirActivity() {
        super(0);
    }

    @Override
    protected void setContenttView() {
        activityView = View.inflate(this, R.layout.activity_add_envir, null);
        setContentView(activityView);
    }

    @Override
    protected void initView() {
        backgroundImg.setImageBitmap(MainApplication.blurBitmap);
        titleLayout.setBackImgVisible(false);
        titleLayout.setCancelImgVisible(true);
        titleLayout.setCancelListener(this);
        titleLayout.setTitle(R.string.add_envir, getResources().getColor(R.color.white));
        titleLayout.setRightTv(R.string.confirm, getResources().getColor(R.color.white), this);
        activityView.addOnLayoutChangeListener(this);
        goneLabel.setOnClickListener(this);
        deleteTitle.setOnClickListener(this);
        deleteDes.setOnClickListener(this);
        title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                Log.e("<<<", "length=" + s.length() + ",start=" + start + ",before=" + before + ",count=" + count);
                if (s.length() > 0) {
                    if (s.length() > 20) {
                        ToastUtils.showInfo("标题不能超过20字");
                    }
                    deleteTitle.setVisibility(View.VISIBLE);
                } else {
                    deleteTitle.setVisibility(View.GONE);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        des.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    deleteDes.setVisibility(View.VISIBLE);
                } else {
                    deleteDes.setVisibility(View.GONE);
                }
                if (count == before + 1 && before == 0 && s.toString().charAt(start) == '#') {
                    String s1 = s.toString().substring(0, start);
                    String s2 = "";
                    if (s.toString().length() > start + 1) {
                        s2 = s.toString().substring(start + 1, s.length());
                    }
                    des.setText(s1 + s2);
                    des.setSelection(start);
                    Intent intent = new Intent(AddEnvirActivity.this, AddLabelActivity.class);
                    if (activeTagsBean != null) {
                        intent.putExtra(CreateQJActivity.class.getSimpleName(), activeTagsBean);
                    }
                    startActivityForResult(intent, 1);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        title.setText(titleIntent);
        des.setText(desIntent);
        searchLinear.setOnClickListener(this);
        dialog = new WaittingDialog(this);
        fragmentList = new ArrayList<>();
        titleList = new ArrayList<>();
    }

    @Override
    protected void requestNet() {
        if (!dialog.isShowing()) {
            dialog.show();
        }
        activeTags();
        categoryList();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_cancel:
                onBackPressed();
                break;
            case R.id.title_continue:
                String str = title.getText().toString();
                if (str.length() > 20) {
                    str = str.substring(0, 20);
                }
                Intent intent = new Intent();
                intent.putExtra("title", str);
                intent.putExtra("des", des.getText().toString());
                setResult(2, intent);
                onBackPressed();
                break;
            case R.id.gone_label:
                if (des.hasFocus()) {
                    des.getText().insert(des.getSelectionStart(), goneLabel.getText());
                }
                break;
            case R.id.delete_title:
                title.setText("");
                break;
            case R.id.search_linear:
                Intent intent1 = new Intent(this, SearchEnvirActivity.class);
                startActivityForResult(intent1, 1);
                break;
            case R.id.delete_des:
                des.setText("");
                break;
        }
    }

    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        Rect r = new Rect();
        activityView.getWindowVisibleDisplayFrame(r);
        boolean isShow = false;
        if (r.bottom < activityView.getBottom() * 3 / 4) {
            isShow = true;
        } else if (r.bottom == activityView.getBottom()) {
            isShow = false;
        }
        if (isShow) {
            LogUtil.e("<<<", "显示软键盘");
            goneLinear.setBottom(r.bottom);
            goneLinear.setTop(r.bottom - DensityUtils.dp2px(AddEnvirActivity.this, 44));
            goneLinear.setAlpha(0f);
            goneLinear.setVisibility(View.VISIBLE);
            ObjectAnimator.ofFloat(goneLinear, "alpha", 0, 1).start();
        } else {
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(goneLinear, "alpha", 1, 0);
            objectAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    goneLinear.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            objectAnimator.start();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.abc_fade_in, R.anim.up_to_bottom);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.abc_fade_in, R.anim.up_to_bottom);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            switch (resultCode) {
                case 3:
                    String titleIntent = data.getStringExtra("title");
                    String desIntent = data.getStringExtra("des");
                    Intent intent = new Intent();
                    intent.putExtra("title", titleIntent);
                    intent.putExtra("des", desIntent);
                    setResult(2, intent);
                    finish();
                    break;
                case 1:
                    String str = data.getStringExtra(AddLabelActivity.class.getSimpleName());
                    if (!str.startsWith(" ")) {
                        str = " " + str;
                    }
                    des.getText().insert(des.getSelectionStart(), str);
                    break;
            }
        }
    }

    //分类列表
    private void categoryList() {
        HashMap<String, String> params = ClientDiscoverAPI.getcategoryListRequestParams(1 + "", 11 + "", 1 + "");
        Call httpHandler=HttpRequest.post(params, URL.CATEGORY_LIST, new GlobalDataCallBack(){
            @Override
            public void onSuccess(String json) {
                HttpResponse<CategoryListBean> categoryListBean = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<CategoryListBean>>() {});
                if (categoryListBean.isSuccess()) {
                    fragmentList.clear();
                    titleList.clear();
                    for (CategoryListBean.RowsEntity categoryListItem : categoryListBean.getData().getRows()) {
                        fragmentList.add(AddEnvirFragment.newInstance(categoryListItem.get_id()));
                        titleList.add(categoryListItem.getTitle());
                    }
                    searchViewPagerAdapter = new SearchViewPagerAdapter(getSupportFragmentManager(), fragmentList, titleList);
                    viewPager.setAdapter(searchViewPagerAdapter);
                    viewPager.setOffscreenPageLimit(fragmentList.size());
                    tabLayout.setupWithViewPager(viewPager);
                } else {
                    dialog.dismiss();
                    ToastUtils.showError(categoryListBean.getMessage());
                }
            }

            @Override
            public void onFailure(String error) {
                dialog.dismiss();
                ToastUtils.showError(R.string.net_fail);
            }
        });
        addNet(httpHandler);
    }

    private ActiveTagsBean activeTagsBean;//活动标签

    //获取活动标签
    private void activeTags() {
        HashMap<String, String> params = ClientDiscoverAPI.getactiveTagsRequestParams();
        Call httpHandler = HttpRequest.post(params, URL.SCENE_SIGHT_STICK_ACTIVE_TAGS, new GlobalDataCallBack(){
            @Override
            public void onSuccess(String json) {
                HttpResponse<ActiveTagsBean> activeTagsBeanHttpResponse = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<ActiveTagsBean>>(){});
                if (activeTagsBeanHttpResponse.isSuccess()) {
                    activeTagsBean = activeTagsBeanHttpResponse.getData();
                }
            }

            @Override
            public void onFailure(String error) {
            }
        });
        addNet(httpHandler);
    }
}
