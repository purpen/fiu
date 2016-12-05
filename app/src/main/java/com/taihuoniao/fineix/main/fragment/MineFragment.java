package com.taihuoniao.fineix.main.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.PersonalCenterGVAdapter;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.beans.ImgTxtItem;
import com.taihuoniao.fineix.beans.LoginInfo;
import com.taihuoniao.fineix.beans.User;
import com.taihuoniao.fineix.interfaces.OnMessageCountChangeListener;
import com.taihuoniao.fineix.main.MainActivity;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.NetworkConstance;
import com.taihuoniao.fineix.personal.salesevice.ChargeBackAndServiceActivity;
import com.taihuoniao.fineix.user.AboutUsActivity;
import com.taihuoniao.fineix.user.CollectionsActivity;
import com.taihuoniao.fineix.user.FansActivity;
import com.taihuoniao.fineix.user.FeedbackActivity;
import com.taihuoniao.fineix.user.FindFriendsActivity;
import com.taihuoniao.fineix.user.FocusActivity;
import com.taihuoniao.fineix.user.HasLoveActivity;
import com.taihuoniao.fineix.user.MessageActivity;
import com.taihuoniao.fineix.user.OrderQJActivity;
import com.taihuoniao.fineix.user.SelectAddressActivity;
import com.taihuoniao.fineix.user.ShopOrderListActivity;
import com.taihuoniao.fineix.user.SystemSettingsActivity;
import com.taihuoniao.fineix.user.UsableRedPacketActivity;
import com.taihuoniao.fineix.user.UserCenterActivity;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.LogUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.view.CustomGridView;
import com.taihuoniao.fineix.view.CustomItemLayout;
import com.taihuoniao.fineix.view.WaittingDialog;
import com.taihuoniao.fineix.view.roundImageView.RoundedImageView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;

public class MineFragment extends MyBaseFragment {

    @Bind(R.id.gv)
    CustomGridView gv;
    @Bind(R.id.iv_detail)
    ImageView iv_detail;
    @Bind(R.id.item_about_us)
    CustomItemLayout item_about_us;
    @Bind(R.id.item_feedback)
    CustomItemLayout item_feedback;
    @Bind(R.id.item_partner)
    CustomItemLayout item_partner;
    @Bind(R.id.rl)
    RelativeLayout rl;
    @Bind(R.id.ll_box)
    LinearLayout ll_box;
    @Bind(R.id.riv)
    RoundedImageView riv;
    @Bind(R.id.tv_real)
    TextView tv_real;
    @Bind(R.id.tv_nick)
    TextView tv_nick;
    @Bind(R.id.tv_qj)
    TextView tv_qj;
    @Bind(R.id.tv_focus)
    TextView tv_focus;
    @Bind(R.id.tv_fans)
    TextView tv_fans;
    @Bind(R.id.iv_bg)
    ImageView iv_bg;
    @Bind(R.id.iv_label)
    TextView iv_label;
    @Bind(R.id.tv_auth)
    TextView tv_auth;
    @Bind(R.id.tv_lv)
    TextView tv_lv;
    @Bind(R.id.tv_label)
    TextView tv_label;
    @Bind(R.id.riv_auth)
    ImageView riv_auth;

    public static final int[] imgIds = {R.mipmap.gv_order, R.mipmap.gv_message, R.mipmap.gv_subscribe, R.mipmap.gv_collects,
            R.mipmap.gv_support, R.mipmap.gv_integral, R.mipmap.gv_coupon, R.mipmap.gv_address, R.mipmap.icon_personal_chargeback};
    public static final String[] imgTxt = MainApplication.getContext().getResources().getStringArray(R.array.mine_gv_txt);
    public static final int REQUEST_QJ = 0;
    public static final int REQUEST_CJ = 1;

    private static OnMessageCountChangeListener listener;
    private ArrayList<ImgTxtItem> gvList;
    private boolean isInitLoad = true;
    private PersonalCenterGVAdapter adapter;
    private WaittingDialog dialog;
    private DisplayImageOptions options;
    private User user;

    public static void setOnMessageCountChangeListener(OnMessageCountChangeListener listener) {
        MineFragment.listener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.user_center_bg)
                .showImageForEmptyUri(R.mipmap.user_center_bg)
                .showImageOnFail(R.mipmap.user_center_bg)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        initData();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            loadData();
        }
    }

    private void initData() {
        if (imgIds.length == imgTxt.length) {
            gvList = new ArrayList<>();
            ImgTxtItem item;
            for (int i = 0; i < imgIds.length; i++) {
                item = new ImgTxtItem();
                item.imgId = imgIds[i];
                item.txt = imgTxt[i];
                gvList.add(item);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.setFragmentLayout(R.layout.fragment_personalcenter);
        super.onCreateView(inflater, container, savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            rl.setPadding(0, getStatusBarHeight(), 0, 0);
        }
        return view;
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @Override
    protected void initParams() {
        dialog = new WaittingDialog(activity);
    }

    @Override
    protected void loadData() {
        if (!LoginInfo.isUserLogin()) {
            LogUtil.e(TAG, "isUserLogin()==false");
            return;
        }

        ClientDiscoverAPI.getUserCenterData(new RequestCallBack<String>() {
            @Override
            public void onStart() {
                if (!activity.isFinishing() && dialog != null) dialog.show();
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (!activity.isFinishing() && dialog != null) dialog.dismiss();
                try {
                    HttpResponse<User> response = JsonUtil.json2Bean(responseInfo.result, new TypeToken<HttpResponse<User>>() {
                    });
                    if (response.isSuccess()) {
                        user = response.getData();
                        refreshUIAfterNet();
                    }


                } catch (JsonSyntaxException e) {
                    LogUtil.e(TAG, e.getLocalizedMessage());
                    ToastUtils.showError(R.string.network_err);
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                if (!activity.isFinishing() && dialog != null) dialog.dismiss();
                ToastUtils.showError(R.string.network_err);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (LoginInfo.isUserLogin()) {
            if (isInitLoad) {
                isInitLoad = false;
            } else {
                if (((MainActivity) activity).getVisibleFragment() instanceof MineFragment) {
                    loadData();
                }
            }
        }
    }

    @Override
    protected void initViews() {
        if (gvList != null && gvList.size() >= 0) {
            adapter = new PersonalCenterGVAdapter(gvList, activity);
            gv.setAdapter(adapter);
        }
        item_about_us.setTVStyle(0, R.string.about_us, R.color.color_333);
        item_feedback.setTVStyle(0, R.string.feed_back, R.color.color_333);
    }

    @Override
    protected void refreshUIAfterNet() {
        if (user == null) return;
        if (user.counter != null && adapter != null && gvList != null) {
            for (int i = 0; i < gvList.size(); i++) {
                switch (i) {
                    case 0:
                        gvList.get(i).count = user.counter.order_total_count;
                        break;
                    case 1:
                        gvList.get(i).count = user.counter.message_total_count;
                        if (listener != null)
                            listener.onMessageCountChange(user.counter.message_total_count);
                        break;
                    case 6:
                        gvList.get(i).count = user.counter.fiu_bonus_count;
                        break;
                    default:
                        break;
                }

            }
            adapter.notifyDataSetChanged();
        }

        if (!TextUtils.isEmpty(user.medium_avatar_url)) {
            ImageLoader.getInstance().displayImage(user.medium_avatar_url, riv);
        }
        ImageLoader.getInstance().displayImage(user.head_pic_url, iv_bg, options);

        if (user.identify.is_expert == 1) {
            riv_auth.setVisibility(View.VISIBLE);
        } else {
            riv_auth.setVisibility(View.GONE);
        }

        if (TextUtils.isEmpty(user.summary)) {
            tv_real.setText(String.format(" | %s", "还没有个性签名！"));
        } else {
            tv_real.setText(String.format(" | %s", user.summary));
        }

        if (!TextUtils.isEmpty(user.expert_label)) {
            iv_label.setText(String.format("%s |", user.expert_label));
        } else {
            iv_label.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(user.expert_info)) {
            tv_auth.setText(user.expert_info);
        } else {
            tv_auth.setVisibility(View.GONE);
        }


        if (!TextUtils.isEmpty(user.label)) {
            tv_label.setText(String.format(" %s", user.label));
        } else {
            tv_label.setVisibility(View.GONE);
        }

        if (TextUtils.isEmpty(user.nickname)) {
            tv_nick.setVisibility(View.GONE);
        } else {
            tv_nick.setText(user.nickname);
        }

        tv_lv.setText(String.format("Lv%s", user.rank_id));
        tv_qj.setText(String.valueOf(user.sight_count)); //场景改情景
        tv_focus.setText(String.valueOf(user.follow_count));
        tv_fans.setText(String.valueOf(user.fans_count));
    }

    @OnClick({R.id.ibtn_setting, R.id.btn, R.id.ll_box, R.id.iv_detail, R.id.item_about_us, R.id.item_feedback, R.id.item_partner, R.id.rl_qj, R.id.rl_focus, R.id.ll_fans})
    protected void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.ll_box:
                startActivity(new Intent(activity, UserCenterActivity.class));
                break;
            case R.id.rl_qj:
                intent = new Intent(activity, UserCenterActivity.class);
                intent.putExtra(MineFragment.class.getSimpleName(), REQUEST_CJ);
                startActivity(intent);
                break;
            case R.id.ibtn_setting:
                startActivity(new Intent(activity, SystemSettingsActivity.class));
                break;
            case R.id.rl_focus:
                intent = new Intent(activity, FocusActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_fans:
                intent = new Intent(activity, FansActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_detail:
                startActivity(new Intent(activity, FindFriendsActivity.class));
                break;
            case R.id.item_about_us:
                intent = new Intent(activity, AboutUsActivity.class);
                intent.putExtra(AboutUsActivity.class.getSimpleName(), NetworkConstance.SETTINGS_ABOUTUS);
                intent.putExtra(AboutUsActivity.class.getName(), "关于Fiu");
                startActivity(intent);
                break;
            case R.id.item_feedback:
                startActivity(new Intent(activity, FeedbackActivity.class));
                break;
            case R.id.btn:
        }
    }

    @Override
    protected void installListener() {
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            Intent intent;

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:  //订单
                        startActivity(new Intent(activity, ShopOrderListActivity.class));
                        break;
                    case 1:
                        startActivity(new Intent(activity, MessageActivity.class));
                        break;
                    case 2:  //订阅
                        intent = new Intent(activity, OrderQJActivity.class);
                        if (user != null && user.interest_scene_cate != null) {
                            intent.putStringArrayListExtra(OrderQJActivity.class.getSimpleName(), user.interest_scene_cate);
                        }
                        startActivity(intent);
                        break;
                    case 4: //赞过
                        startActivity(new Intent(activity, HasLoveActivity.class));
                        break;
                    case 3: //收藏
                        startActivity(new Intent(activity, CollectionsActivity.class));
                        break;
                    case 5:
                        String url = NetworkConstance.BASE_URL + "/view/fiu_point?uuid=" + MainApplication.uuid + "&from_to=2&app_type=2";
                        intent = new Intent(activity, AboutUsActivity.class);
                        intent.putExtra(AboutUsActivity.class.getSimpleName(), url);
                        intent.putExtra(AboutUsActivity.class.getName(), "积分");
                        startActivity(intent);
                        break;
                    case 6: //礼券
                        startActivity(new Intent(getActivity(), UsableRedPacketActivity.class));
                        break;
                    case 7:
                        startActivity(new Intent(activity, SelectAddressActivity.class));
                        break;
                    case 8: //退款/售后
                        startActivity(new Intent(activity, ChargeBackAndServiceActivity.class));
                        break;
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        if (listener != null)
            listener = null;
        super.onDestroy();
    }
}
