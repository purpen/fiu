package com.taihuoniao.fineix.scene;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Rect;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.SearchViewPagerAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.ActiveTagsBean;
import com.taihuoniao.fineix.beans.CategoryListBean;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.qingjingOrSceneDetails.fragment.SearchFragment;
import com.taihuoniao.fineix.scene.fragments.AddEnvirFragment;
import com.taihuoniao.fineix.utils.DensityUtils;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.view.GlobalTitleLayout;
import com.taihuoniao.fineix.view.WaittingDialog;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

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
    @Bind(R.id.gone_demo_label)
    TextView goneDemoLabel;
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
        goneDemoLabel.setOnClickListener(this);
        deleteTitle.setOnClickListener(this);
        deleteDes.setOnClickListener(this);
        title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    if (s.length() > 20) {
                        title.setText(s.subSequence(0, 20));
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
                Log.e("<<<", "before:s=" + s + ",start=" + start + ",count=" + count + ",after=" + after);

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    deleteDes.setVisibility(View.VISIBLE);
                } else {
                    deleteDes.setVisibility(View.GONE);
                }
                Log.e("<<<", "change:s=" + s + ",start=" + start
                        + ",before=" + before + ",count=" + count);
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
                Log.e("<<<", "after:s=" + s);
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
                Intent intent = new Intent();
                intent.putExtra("title", title.getText().toString());
                intent.putExtra("des", des.getText().toString());
                intent.putExtra("activeBean", activeTagsBean);
                setResult(2, intent);
                onBackPressed();
                break;
            case R.id.gone_label:
                if (des.hasFocus()) {
                    des.getText().insert(des.getSelectionStart(), goneLabel.getText());
                }
                break;
            case R.id.gone_demo_label:
                if (des.hasFocus()) {
                    des.getText().insert(des.getSelectionStart(), goneDemoLabel.getText());
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
        Log.e("<<<", "跟布局onLayoutChange");
        Rect r = new Rect();
        activityView.getWindowVisibleDisplayFrame(r);
        boolean isShow = false;
        if (r.bottom < activityView.getBottom() * 3 / 4) {
            isShow = true;
        } else if (r.bottom == activityView.getBottom()) {
            isShow = false;
        }
        if (isShow) {
            Log.e("<<<", "显示软键盘");
            goneLinear.setBottom(r.bottom);
            goneLinear.setTop(r.bottom - DensityUtils.dp2px(AddEnvirActivity.this, 44));
            goneLinear.setAlpha(0f);
            goneLinear.setVisibility(View.VISIBLE);
            ObjectAnimator.ofFloat(goneLinear, "alpha", 0, 1).start();
        } else {
            Log.e("<<<", "隐藏软键盘");
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
        Log.e("<<<", "goneLinear,top=" + goneLinear.getTop() + ",bottom=" + goneLinear.getBottom());
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
                    intent.putExtra("activeBean", activeTagsBean);
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
        ClientDiscoverAPI.categoryList(1 + "", 11 + "", 1 + "", new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
//                dialog.dismiss();
                CategoryListBean categoryListBean = new CategoryListBean();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<CategoryListBean>() {
                    }.getType();
                    categoryListBean = gson.fromJson(responseInfo.result, type);
                } catch (JsonSyntaxException e) {
                    Log.e("<<<分类列表", "数据解析异常" + e.toString());
                }
                if (categoryListBean.isSuccess()) {
                    fragmentList.clear();
                    titleList.clear();
                    for (CategoryListBean.CategoryListItem categoryListItem : categoryListBean.getData().getRows()) {
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
            public void onFailure(HttpException error, String msg) {
                dialog.dismiss();
                ToastUtils.showError(R.string.net_fail);
            }
        });
    }

    private ActiveTagsBean activeTagsBean;//活动标签

    //获取活动标签
    private void activeTags() {
        ClientDiscoverAPI.activeTags(new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("<<<活动标签", responseInfo.result);
//                dialog.dismiss();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<ActiveTagsBean>() {
                    }.getType();
                    activeTagsBean = gson.fromJson(responseInfo.result, type);
                } catch (JsonSyntaxException e) {
                    Log.e("<<<", "解析异常" + e.toString());
                }
                if (activeTagsBean.isSuccess()) {
                    try {
                        goneDemoLabel.setText(" #" + activeTagsBean.getData().getItems().get(0).get(0) + " ");
                    } catch (Exception e) {
                        Log.e("<<<", "没有活动");
                    }
                } else {
                    goneDemoLabel.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
//                dialog.dismiss();
                goneDemoLabel.setVisibility(View.GONE);
            }
        });
    }
}
