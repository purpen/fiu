package com.taihuoniao.fineix.scene;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.HorizontalScrollView;
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
import com.taihuoniao.fineix.adapters.AllLabelListViewAdapter;
import com.taihuoniao.fineix.adapters.AllLabelViewPagerAdapter1;
import com.taihuoniao.fineix.adapters.HotLabelViewPagerAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.AllLabel;
import com.taihuoniao.fineix.beans.AllLabelBean;
import com.taihuoniao.fineix.beans.HotLabel;
import com.taihuoniao.fineix.beans.UsedLabel;
import com.taihuoniao.fineix.beans.UsedLabelBean;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.utils.DensityUtils;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.view.CustomSlidingTab;
import com.taihuoniao.fineix.view.GlobalTitleLayout;
import com.taihuoniao.fineix.view.WaittingDialog;
import com.taihuoniao.fineix.view.WrapContentViewPager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by taihuoniao on 2016/4/8.
 */
public class AddLabelActivity extends BaseActivity implements View.OnClickListener, HotLabelViewPagerAdapter.LabelClick, AllLabelListViewAdapter.MoreClick {
    //控件
    private GlobalTitleLayout titleLayout;
    private HorizontalScrollView horizontalScrollView;
    private LinearLayout horizontalLinear;
    private ImageView deleteImg;
    private RelativeLayout usedLabelRelative;
    private RelativeLayout hotLabelRelative;
    private TextView usedLabelTv, hotLabelTv;
    private TextView usedLabelLine, hotLabelLine;
    private ViewPager labelViewPager;
    private HotLabelViewPagerAdapter hotLabelViewPagerAdapter;
    private CustomSlidingTab slidingTab;
    //    private ViewPager allLabelViewPager;
    private WrapContentViewPager allLabelViewPager;
    //    private AllLabelViewPagerAdapter allLabelViewPagerAdapter;
    private AllLabelViewPagerAdapter1 allLabelViewPagerAdapter;
    //全部标签的页码
    private int allLabelCurrentPage = 1;
    //热门标签页码
    private int hotLabelCurrentPage = 1;
    //热门标签的列表
    private List<HotLabel.HotLabelBean> hotLabelList;
    //使用过的标签列表
    private List<UsedLabelBean> usedLabelList;
    //全部标签列表
    private List<AllLabelBean> allLabelList;
    //网络请求对话框
    private WaittingDialog dialog;
    //用来存储用户选择的标签
    private List<UsedLabelBean> selectList;

    public AddLabelActivity() {
        super(0);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_add_label);
        titleLayout = (GlobalTitleLayout) findViewById(R.id.activity_add_label_titlelayout);
        horizontalScrollView = (HorizontalScrollView) findViewById(R.id.activity_add_label_horizontalscroll);
        horizontalLinear = (LinearLayout) findViewById(R.id.activity_add_label_horizontallinear);
        deleteImg = (ImageView) findViewById(R.id.activity_add_label_cancel);
        usedLabelRelative = (RelativeLayout) findViewById(R.id.activity_add_label_usedlabelrelative);
        hotLabelRelative = (RelativeLayout) findViewById(R.id.activity_add_label_hotlabelrelative);
        usedLabelTv = (TextView) findViewById(R.id.activity_add_label_usedlabel);
        hotLabelTv = (TextView) findViewById(R.id.activity_add_label_hotlabel);
        usedLabelLine = (TextView) findViewById(R.id.activity_add_label_usedlabelline);
        hotLabelLine = (TextView) findViewById(R.id.activity_add_label_hotlabelline);
        labelViewPager = (ViewPager) findViewById(R.id.activity_add_label_labelviewpager);
        slidingTab = (CustomSlidingTab) findViewById(R.id.activity_add_label_slidingtab);
//        allLabelViewPager = (ViewPager) findViewById(R.id.activity_add_label_alllabelviewpager);
        allLabelViewPager = (WrapContentViewPager) findViewById(R.id.activity_add_label_alllabelviewpager);
        dialog = new WaittingDialog(AddLabelActivity.this);
        IntentFilter filter = new IntentFilter(DataConstants.BroadLabelActivity);
        registerReceiver(labelReceiver, filter);
    }

    @Override
    protected void initList() {
        titleLayout.setBackImg(R.mipmap.back_black);
        titleLayout.setBackgroundResource(R.color.white);
        titleLayout.setTitle(R.string.add_label, getResources().getColor(R.color.black333333));
        titleLayout.setRightTv(R.string.confirm, getResources().getColor(R.color.black333333), this);
        deleteImg.setOnClickListener(this);
        usedLabelRelative.setOnClickListener(this);
        hotLabelRelative.setOnClickListener(this);
        usedLabelList = new ArrayList<>();
        hotLabelList = new ArrayList<>();

        labelViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            private void goneColor() {
                usedLabelLine.setVisibility(View.GONE);
                usedLabelTv.setTextColor(getResources().getColor(R.color.black333333));
                hotLabelLine.setVisibility(View.GONE);
                hotLabelTv.setTextColor(getResources().getColor(R.color.black333333));
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (usedLabelList.size() > 0) {
                    goneColor();
                    if (position == 0) {
                        usedLabelLine.setVisibility(View.VISIBLE);
                        usedLabelTv.setTextColor(getResources().getColor(R.color.yellow_bd8913));
                    } else if (position == 1) {
                        hotLabelTv.setTextColor(getResources().getColor(R.color.yellow_bd8913));
                        hotLabelLine.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        allLabelList = new ArrayList<>();
        slidingTab.setIndicatorColor(getResources().getColor(R.color.yellow_bd8913));
        slidingTab.setTextColor(getResources().getColor(R.color.black333333));
        slidingTab.setCurTabTextColor(getResources().getColor(R.color.yellow_bd8913));
        slidingTab.setTypeface(null, Typeface.NORMAL);
        slidingTab.setTextSize(getResources().getDimensionPixelSize(R.dimen.sp15));
//        allLabelViewPagerAdapter = new AllLabelViewPagerAdapter(AddLabelActivity.this, allLabelList, AddLabelActivity.this, AddLabelActivity.this);
        allLabelViewPagerAdapter = new AllLabelViewPagerAdapter1(getSupportFragmentManager(), AddLabelActivity.this, allLabelList, AddLabelActivity.this, AddLabelActivity.this);
        allLabelViewPager.setAdapter(allLabelViewPagerAdapter);
        selectList = new LinkedList<>();
//        if (MainApplication.selectList != null) {
//            for (UsedLabelBean usedLabelBean : MainApplication.selectList) {
//                addToLinear(usedLabelBean.getTitle_cn(), Integer.parseInt(usedLabelBean.get_id()));
//            }
//        }
    }

    @Override
    protected void requestNet() {
        if (!dialog.isShowing()) {
            dialog.show();
        }
        //需要登录
//        DataPaser.usedLabelList(handler);
        ClientDiscoverAPI.usedLabelList(new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
//                Log.e("<<<用过的标签", responseInfo.result);
//                WriteJsonToSD.writeToSD("json", responseInfo.result);
//                Message msg = handler.obtainMessage();
//                msg.what = DataConstants.USED_LABEL_LIST;
                UsedLabel usedLabel = new UsedLabel();
                try {
                    JSONObject job = new JSONObject(responseInfo.result);
                    usedLabel.setSuccess(job.optBoolean("success"));
                    usedLabel.setMessage(job.optString("message"));
//                    usedLabel.setStatus(job.optString("status"));
                    if (usedLabel.isSuccess()) {
                        JSONObject data = job.getJSONObject("data");
                        usedLabel.setHas_tag(data.optInt("has_tag"));
                        if (usedLabel.getHas_tag() != 0) {
                            List<UsedLabelBean> list = new ArrayList<UsedLabelBean>();
                            JSONArray tags = data.getJSONArray("tags");
                            for (int i = 0; i < tags.length(); i++) {
                                JSONObject ob = tags.getJSONObject(i);
                                UsedLabelBean usedLabelBean = new UsedLabelBean();
                                usedLabelBean.set_id(ob.optString("_id"));
                                usedLabelBean.setUser_id(ob.optString("user_id"));
                                usedLabelBean.setUsed_count(ob.optString("used_count"));
                                usedLabelBean.setUpdated_on(ob.optString("updated_on"));
                                usedLabelBean.setType(ob.optString("type"));
                                usedLabelBean.setTitle_en(ob.optString("title_en"));
                                usedLabelBean.setTitle_cn(ob.optString("title_cn"));
                                usedLabelBean.setStick(ob.optString("stick"));
                                usedLabelBean.setStatus(ob.optString("status"));
                                list.add(usedLabelBean);
                            }
                            usedLabel.setUsedLabelList(list);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (usedLabel.isSuccess()) {
                    if (usedLabel.getHas_tag() == 0) {
//                        Log.e("<<<", "没有用过的标签");
                        return;
                    }
                    usedLabelList.addAll(usedLabel.getUsedLabelList());
                    usedLabelRelative.setVisibility(View.VISIBLE);
                    hotLabelViewPagerAdapter = new HotLabelViewPagerAdapter(getSupportFragmentManager(), AddLabelActivity.this, usedLabelList, hotLabelList, AddLabelActivity.this);
                    labelViewPager.setAdapter(hotLabelViewPagerAdapter);
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                ToastUtils.showError("网络错误");
            }
        });
//        DataPaser.labelList(null, allLabelCurrentPage, null, 0, 0, handler);
        ClientDiscoverAPI.labelList(null, allLabelCurrentPage, null, 0, 0, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                dialog.dismiss();
//                Log.e("<<<全部标签", responseInfo.result);
//                WriteJsonToSD.writeToSD("quanbubiaoqian", responseInfo.result);
//                Message msg = handler.obtainMessage();
//                msg.what = DataConstants.LABEL_LIST;
                AllLabel allLabel = new AllLabel();
                try {
                    JSONObject job = new JSONObject(responseInfo.result);
                    allLabel.setSuccess(job.optBoolean("success"));
                    allLabel.setMessage(job.optString("message"));
//                    allLabel.setStatus(job.optString("status"));
                    if (allLabel.isSuccess()) {
                        JSONObject data = job.getJSONObject("data");
                        JSONObject object = data.getJSONObject("1");
                        if (object.optInt("children_count") > 0) {
                            JSONArray children = object.getJSONArray("children");
                            List<AllLabelBean> childrenList = new ArrayList<AllLabelBean>();
                            for (int i = 0; i < children.length(); i++) {
                                JSONObject ob = children.getJSONObject(i);
                                AllLabelBean allLabelBean = new AllLabelBean();
                                allLabelBean.set_id(ob.optString("_id"));
                                allLabelBean.setTitle_cn(ob.optString("title_cn"));
                                allLabelBean.setChildren_count(ob.optInt("children_count"));
                                if (allLabelBean.getChildren_count() > 0) {
                                    JSONArray children2 = ob.getJSONArray("children");
                                    List<AllLabelBean> children2List = new ArrayList<AllLabelBean>();
                                    for (int j = 0; j < children2.length(); j++) {
                                        JSONObject ob2 = children2.getJSONObject(j);
                                        AllLabelBean allLabelBean2 = new AllLabelBean();
                                        allLabelBean2.set_id(ob2.optString("_id"));
                                        allLabelBean2.setTitle_cn(ob2.optString("title_cn"));
                                        allLabelBean2.setChildren_count(ob2.optInt("children_count"));
                                        if (allLabelBean2.getChildren_count() > 0) {
                                            JSONArray children3 = ob2.getJSONArray("children");
                                            List<AllLabelBean> children3List = new ArrayList<AllLabelBean>();
                                            for (int k = 0; k < children3.length(); k++) {
                                                JSONObject ob3 = children3.getJSONObject(k);
                                                AllLabelBean allLabelBean3 = new AllLabelBean();
                                                allLabelBean3.set_id(ob3.optString("_id"));
                                                allLabelBean3.setTitle_cn(ob3.optString("title_cn"));
                                                children3List.add(allLabelBean3);
                                            }
                                            allLabelBean2.setChildren(children3List);
                                        }
                                        children2List.add(allLabelBean2);
                                    }
                                    allLabelBean.setChildren(children2List);
                                }
                                childrenList.add(allLabelBean);
                            }
                            allLabel.setChildren(childrenList);
                        }
                    }
//                    msg.obj = allLabel;
                } catch (JSONException e) {
                    Log.e("<<<", "解析异常");
                    e.printStackTrace();
                }
                if (allLabel.isSuccess()) {
                    if (allLabelCurrentPage == 1) {
                        allLabelList.clear();
                    }
                    allLabelList.addAll(allLabel.getChildren());
                    allLabelViewPagerAdapter.notifyDataSetChanged();
//                        allLabelViewPager.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, allLabelViewPagerAdapter.getMaxHeight()));
                    slidingTab.setViewPager(allLabelViewPager);
                }
//                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                dialog.dismiss();
                ToastUtils.showError("网络错误");
            }
        });
        ClientDiscoverAPI.labelList(null, 1, 18 + "", 3, 1, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
//                Message msg = handler.obtainMessage();
//                msg.what = DataConstants.HOT_LABEL_LIST;
                HotLabel netHotLabel = new HotLabel();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<HotLabel>() {
                    }.getType();
                    netHotLabel = gson.fromJson(responseInfo.result, type);
                } catch (JsonSyntaxException e) {
//                    Toast.makeText(AddLabelActivity.this, "数据异常", Toast.LENGTH_SHORT).show();
                }
//                handler.sendMessage(msg);
                if (netHotLabel.isSuccess()) {
                    if (hotLabelCurrentPage == 1) {
                        hotLabelList.clear();
                    }
                    hotLabelList.addAll(netHotLabel.getData().getRows());
                    hotLabelViewPagerAdapter = new HotLabelViewPagerAdapter(getSupportFragmentManager(), AddLabelActivity.this, usedLabelList, hotLabelList, AddLabelActivity.this);
                    labelViewPager.setAdapter(hotLabelViewPagerAdapter);
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
//                Log.e("<<<", "请求失败" + error.toString() + ",msg=" + msg);
                ToastUtils.showError("网络错误");
            }
        });
    }

    @Override
    public void click(Object object) {
        String str = null;
        int _id = -1;
        if (object instanceof UsedLabelBean) {
            UsedLabelBean usedLabelBean = (UsedLabelBean) object;
            str = usedLabelBean.getTitle_cn();
            _id = Integer.parseInt(usedLabelBean.get_id());
        } else if (object instanceof HotLabel.HotLabelBean) {
            HotLabel.HotLabelBean hotLabelBean = (HotLabel.HotLabelBean) object;
            str = hotLabelBean.getTitle_cn();
            _id = Integer.parseInt(hotLabelBean.get_id());
        } else if (object instanceof AllLabelBean) {
            AllLabelBean allLabelBean = (AllLabelBean) object;
            str = allLabelBean.getTitle_cn();
            _id = Integer.parseInt(allLabelBean.get_id());
        }
        addToLinear(str, _id);
    }

    private void addToLinear(String str, int _id) {
        if (_id == -1) {
            return;
        }
        for (int i = 0; i < horizontalLinear.getChildCount(); i++) {
            int id = (int) horizontalLinear.getChildAt(i).getTag();
            if (id == _id) {
                return;
            }
        }
        final View view = View.inflate(AddLabelActivity.this, R.layout.view_horizontal_label_item, null);
        TextView textView = (TextView) view.findViewById(R.id.view_horizontal_label_item_tv);
        textView.setText(str);
        view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) view.getLayoutParams();
        lp.rightMargin = DensityUtils.dp2px(AddLabelActivity.this, 10);
        view.setLayoutParams(lp);
        view.setTag(_id);
        horizontalLinear.addView(view);
        final UsedLabelBean usedLabelBean = new UsedLabelBean(_id + "", str);
        selectList.add(usedLabelBean);
//        horizontalScrollView.smoothScrollTo(horizontalLinear.getMeasuredWidth(), 0);
//        View focusView = horizontalLinear.getChildAt(horizontalLinear.getChildCount() - 1);
//        focusView.setFocusable(true);
//        focusView.setFocusableInTouchMode(true);
//        focusView.requestFocus();
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                horizontalScrollView.fullScroll(View.FOCUS_RIGHT);
            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                horizontalLinear.removeView(view);
                selectList.remove(usedLabelBean);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_add_label_hotlabelrelative:
                if (hotLabelList.size() == 0) {
                    return;
                }
                labelViewPager.setCurrentItem(1);
                break;
            case R.id.activity_add_label_usedlabelrelative:
                labelViewPager.setCurrentItem(0);
                break;
            case R.id.activity_add_label_cancel:
                if (horizontalLinear.getChildCount() <= 0) {
                    return;
                }
                horizontalLinear.removeAllViews();
                selectList.clear();
                break;
            case R.id.title_continue:
//                MainApplication.selectList = selectList;
                setResult(DataConstants.RESULTCODE_CREATESCENE_ADDLABEL, new Intent());
                finish();
                break;
        }
    }

//    private Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case DataConstants.HOT_LABEL_LIST:
////                    dialog.dismiss();
//                    HotLabel netHotLabel = (HotLabel) msg.obj;
//
//                    break;
//                case DataConstants.LABEL_LIST:
//                    dialog.dismiss();
//                    AllLabel netAllLabel = (AllLabel) msg.obj;
//
//                    break;
//                case DataConstants.USED_LABEL_LIST:
////                    dialog.dismiss();
//                    UsedLabel netUsedLabel = (UsedLabel) msg.obj;
//
//                    break;
//                case DataConstants.NET_FAIL:
//                    dialog.dismiss();
//                    break;
//            }
//        }
//    };

    @Override
    protected void onDestroy() {
        //        cancelNet();
        unregisterReceiver(labelReceiver);
//        if (handler != null) {
//            handler.removeCallbacksAndMessages(null);
//        }
        super.onDestroy();
    }


    @Override
    public void moreClick(int position1, int position2, int distance) {
        allLabelViewPager.requestLayout();
    }

    private BroadcastReceiver labelReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                int flag = intent.getIntExtra("flag", 0);
                switch (flag) {
                    case -1:
                        HotLabel.HotLabelBean hotLabelBean = (HotLabel.HotLabelBean) intent.getSerializableExtra("label");
                        click(hotLabelBean);
                        break;
                    case -2:
                        UsedLabelBean usedLabelBean = (UsedLabelBean) intent.getSerializableExtra("label");
                        click(usedLabelBean);
                        break;
                }
            }
        }
    };
}
