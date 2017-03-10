package com.taihuoniao.fineix.main.fragment;

import android.content.Intent;
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
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.PersonalCenterGVAdapter;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.beans.ImgTxtItem;
import com.taihuoniao.fineix.beans.LoginInfo;
import com.taihuoniao.fineix.beans.User;
import com.taihuoniao.fineix.common.GlobalDataCallBack;
import com.taihuoniao.fineix.interfaces.OnMessageCountChangeListener;
import com.taihuoniao.fineix.main.App;
import com.taihuoniao.fineix.main.MainActivity;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.personal.AllianceRequstDeal;
import com.taihuoniao.fineix.personal.alliance.MyAccountActivity;
import com.taihuoniao.fineix.personal.alliance.bean.MyAccountBean;
import com.taihuoniao.fineix.personal.salesevice.ChargeBackAndServiceActivity;
import com.taihuoniao.fineix.scene.SelectPhotoOrCameraActivity;
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
import com.taihuoniao.fineix.utils.GlideUtils;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.LogUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.utils.Util;
import com.taihuoniao.fineix.view.CustomGridView;
import com.taihuoniao.fineix.view.CustomItemLayout;
import com.taihuoniao.fineix.view.dialog.WaittingDialog;
import com.taihuoniao.fineix.view.roundImageView.RoundedImageView;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionNo;
import com.yanzhenjie.permission.PermissionYes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

import static com.taihuoniao.fineix.utils.Constants.REQUEST_CODE_SETTING;
import static com.taihuoniao.fineix.utils.Constants.REQUEST_PHONE_STATE_CODE;

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
    @Bind(R.id.tv_bonus)
    TextView tvBonus;
    @Bind(R.id.rl_bonus)
    RelativeLayout rlBonus;
    public static final int[] imgIds = {/*R.mipmap.gv_order,*/ R.mipmap.gv_message, R.mipmap.gv_subscribe, R.mipmap.gv_collects,
            R.mipmap.gv_support, R.mipmap.gv_integral, R.mipmap.gv_coupon, R.mipmap.gv_address,R.mipmap.gv_qj/*, R.mipmap.icon_personal_chargeback,
            R.mipmap.icon_personal_geren_tuikuanshouhou*/};
    public static final String[] imgTxt = MainApplication.getContext().getResources().getStringArray(R.array.mine_gv_txt);
    public static final int REQUEST_QJ = 0;
    public static final int REQUEST_CJ = 1;

    private static OnMessageCountChangeListener listener;
    private ArrayList<ImgTxtItem> gvList;
    private boolean isInitLoad = true;
    private PersonalCenterGVAdapter adapter;
    private WaittingDialog dialog;
    private User user;

    public static void setOnMessageCountChangeListener(OnMessageCountChangeListener listener) {
        MineFragment.listener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
            int loadCount = imgIds.length - (TextUtils.isEmpty(AllianceRequstDeal.getAllianceValue()) ? 1 : 0);
            for (int i = 0; i < loadCount; i++) {
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
            rl.setPadding(0, App.getStatusBarHeight(), 0, 0);
        }
        return view;
    }

    @Override
    protected void initParams() {
//        dialog = new WaittingDialog(activity);
    }

    @Override
    protected void loadData() {
        if (!LoginInfo.isUserLogin()) {
            LogUtil.e(TAG, "isUserLogin()==false");
            return;
        }
        HttpRequest.post(URL.USER_CENTER, new GlobalDataCallBack() {
            @Override
            public void onStart() {
//                if (!activity.isFinishing() && dialog != null) dialog.show();
            }

            @Override
            public void onSuccess(String json) {
//                if (!activity.isFinishing() && dialog != null) dialog.dismiss();
                try {
                    HttpResponse<User> response = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<User>>() {
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
            public void onFailure(String error) {
                ToastUtils.showError(R.string.network_err);
            }
        });

        HashMap<String, String> hashMap = ClientDiscoverAPI.getallianceAccount();
        HttpRequest.post(hashMap, URL.ALLIANCE_ACCOUNT, new GlobalDataCallBack() {
            @Override
            public void onSuccess(String json) {
                HttpResponse<MyAccountBean> httpResponse = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<MyAccountBean>>() {
                });
                if (httpResponse.isSuccess()) {
                    tvBonus.setText(httpResponse.getData().getWait_cash_amount()+"元");
                }
            }

            @Override
            public void onFailure(String error) {
                LogUtil.e(error);
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
        item_partner.setTVStyle(0,R.string.company_dz,R.color.color_333);
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
            GlideUtils.displayImage(user.medium_avatar_url, riv);
        }
        GlideUtils.displayImage(user.head_pic_url, iv_bg);

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

    @OnClick({R.id.iv_plan,R.id.rl_bonus, R.id.tv_all_order, R.id.tv_wait_pay, R.id.tv_wait_send, R.id.tv_wait_receive, R.id.tv_wait_comment, R.id.tv_wait_shouhou, R.id.ibtn_setting, R.id.btn, R.id.ll_box, R.id.iv_detail, R.id.item_about_us, R.id.item_feedback, R.id.item_partner, R.id.rl_qj, R.id.rl_focus, R.id.ll_fans})
    protected void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.iv_plan:
                intent = new Intent(activity, AboutUsActivity.class);
                intent.putExtra(AboutUsActivity.class.getSimpleName(), URL.COMPANY_PARTNER_URL);
                intent.putExtra(AboutUsActivity.class.getName(), getString(R.string.company_partner));
                startActivity(intent);
                break;
            case R.id.rl_bonus:
                //我的分成界面
                startActivity(new Intent(activity, MyAccountActivity.class));
                break;
            case R.id.tv_all_order:
                startActivity(new Intent(activity, ShopOrderListActivity.class));
                break;
            case R.id.tv_wait_pay:
                toOrderListPage(R.string.wait_pay);
                break;
            case R.id.tv_wait_send:
                toOrderListPage(R.string.wait_send);
                break;
            case R.id.tv_wait_receive:
                toOrderListPage(R.string.wait_receive);
                break;
            case R.id.tv_wait_comment:
                toOrderListPage(R.string.wait_comment);
                break;
            case R.id.tv_wait_shouhou:
                startActivity(new Intent(activity, ChargeBackAndServiceActivity.class));
                break;
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
                intent.putExtra(AboutUsActivity.class.getSimpleName(), URL.SETTINGS_ABOUTUS);
                intent.putExtra(AboutUsActivity.class.getName(), "关于Fiu");
                startActivity(intent);
                break;
            case R.id.item_feedback:
                startActivity(new Intent(activity, FeedbackActivity.class));
                break;
            case R.id.item_partner:
                intent = new Intent(activity, AboutUsActivity.class);
                intent.putExtra(AboutUsActivity.class.getSimpleName(), URL.COMPANY_DZ_URL);
                intent.putExtra(AboutUsActivity.class.getName(), getString(R.string.company_dz));
                startActivity(intent);
                break;
//            case R.id.btn:
//                startActivity(new Intent(activity, ZoneDetailActivity.class));
//                break;
            default:
                break;
        }
    }

    private void toOrderListPage(int res) {
        Intent intent;
        intent = new Intent(activity, ShopOrderListActivity.class);
        String string = getResources().getString(res);
        intent.putExtra("orderStatus", string);
        startActivity(intent);
    }

    @Override
    protected void installListener() {
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            Intent intent;

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:  //订单
//                        startActivity(new Intent(activity, ShopOrderListActivity.class));
                        startActivity(new Intent(activity, MessageActivity.class));
                        break;
                    case 1: //订阅情境
                        intent = new Intent(activity, OrderQJActivity.class);
                        if (user != null && user.interest_scene_cate != null) {
                            intent.putStringArrayListExtra(OrderQJActivity.class.getSimpleName(), user.interest_scene_cate);
                        }
                        startActivity(intent);
                        break;
                    case 2: //收藏
                        startActivity(new Intent(activity, CollectionsActivity.class));
                        break;
                    case 3: //赞过
                        startActivity(new Intent(activity, HasLoveActivity.class));
                        break;
                    case 4:
                        String url = URL.BASE_URL + "/view/fiu_point?uuid=" + Util.getUUID(activity) + "&from_to=2&app_type=2";
                        intent = new Intent(activity, AboutUsActivity.class);
                        intent.putExtra(AboutUsActivity.class.getSimpleName(), url);
                        intent.putExtra(AboutUsActivity.class.getName(), "积分");
                        startActivity(intent);
                        break;
                    case 5: //礼券
                        startActivity(new Intent(getActivity(), UsableRedPacketActivity.class));
                        break;
                    case 6:
                        startActivity(new Intent(activity, SelectAddressActivity.class));
                        break;
                    case 7:
                        if (AndPermission.hasPermission(activity,android.Manifest.permission.CAMERA)){
                            startActivity(new Intent(activity, SelectPhotoOrCameraActivity.class));
                        }else {
                            // 申请权限。
                            AndPermission.with(activity)
                                    .requestCode(REQUEST_PHONE_STATE_CODE)
                                    .permission(android.Manifest.permission.CAMERA)
                                    .send();

                        }

                        break;
//                    case 8: //退款/售后
//                        startActivity(new Intent(activity, ChargeBackAndServiceActivity.class));
//                        break;
//                    case 9: //分成管理
//                        startActivity(new Intent(activity, MyAccountActivity.class));
//                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // 只需要调用这一句，第一个参数是当前Acitivity/Fragment，回调方法写在当前Activity/Framgent。
        AndPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    // 成功回调的方法，用注解即可，里面的数字是请求时的requestCode。
    @PermissionYes(REQUEST_PHONE_STATE_CODE)
    private void getPhoneStatusYes(List<String> grantedPermissions) {
        startActivity(new Intent(activity, SelectPhotoOrCameraActivity.class));
    }

    // 失败回调的方法，用注解即可，里面的数字是请求时的requestCode。
    @PermissionNo(REQUEST_PHONE_STATE_CODE)
    private void getPhoneStatusNo(List<String> deniedPermissions) {
        // 用户否勾选了不再提示并且拒绝了权限，那么提示用户到设置中授权。
        if (AndPermission.hasAlwaysDeniedPermission(this, deniedPermissions)) {
            // 第一种：用默认的提示语。
            AndPermission.defaultSettingDialog(this,REQUEST_CODE_SETTING).show();
        }else {
            activity.finish();
        }
    }

    @Override
    public void onDestroy() {
        if (listener != null)
            listener = null;
        super.onDestroy();
    }
}
