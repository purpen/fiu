package com.taihuoniao.fineix.main.fragment;

import android.content.Intent;
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
import com.nostra13.universalimageloader.core.ImageLoader;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.PersonalCenterGVAdapter;
import com.taihuoniao.fineix.beans.ImgTxtItem;
import com.taihuoniao.fineix.beans.LoginInfo;
import com.taihuoniao.fineix.beans.User;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.HttpResponse;
import com.taihuoniao.fineix.user.AboutUsActivity;
import com.taihuoniao.fineix.user.FeedbackActivity;
import com.taihuoniao.fineix.user.FindFriendsActivity;
import com.taihuoniao.fineix.user.FocusFansActivity;
import com.taihuoniao.fineix.user.HasLoveActivity;
import com.taihuoniao.fineix.user.MessageActivity;
import com.taihuoniao.fineix.user.OptRegisterLoginActivity;
import com.taihuoniao.fineix.user.OrderQJActivity;
import com.taihuoniao.fineix.user.SelectAddressActivity;
import com.taihuoniao.fineix.user.SystemSettingsActivity;
import com.taihuoniao.fineix.user.UserCenterActivity;
import com.taihuoniao.fineix.utils.ImageUtils;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.LogUtil;
import com.taihuoniao.fineix.utils.Util;
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
    @Bind(R.id.iv_calendar)
    ImageView iv_calendar;
    @Bind(R.id.item_about_us)
    CustomItemLayout item_about_us;
    @Bind(R.id.item_feedback)
    CustomItemLayout item_feedback;
    @Bind(R.id.item_partner)
    CustomItemLayout item_partner;
    @Bind(R.id.ll)
    LinearLayout ll;
    @Bind(R.id.ll_box)
    LinearLayout ll_box;
    @Bind(R.id.rl)
    RelativeLayout rl;
    @Bind(R.id.riv)
    RoundedImageView riv;
    @Bind(R.id.tv_real)
    TextView tv_real;
    @Bind(R.id.tv_nick)
    TextView tv_nick;
    @Bind(R.id.tv_rank)
    TextView tv_rank;
    @Bind(R.id.tv_qj)
    TextView tv_qj;
    @Bind(R.id.tv_cj)
    TextView tv_cj;
    @Bind(R.id.tv_focus)
    TextView tv_focus;
    @Bind(R.id.tv_fans)
    TextView tv_fans;
    public static final int REQUEST_QJ = 0;
    public static final int REQUEST_CJ = 1;
    public static final int REQUEST_FOCUS = 2;
    public static final int REQUEST_FANS = 3;
    private User user;
    private ArrayList<ImgTxtItem> gvList;
    private boolean isInitLoad=true;
    private ArrayList<ImgTxtItem> horizentalList; // R.mipmap.gv_collection
    private PersonalCenterGVAdapter adapter;
    public int[] imgIds = {R.mipmap.gv_order, R.mipmap.gv_message, R.mipmap.gv_subscribe, R.mipmap.gv_accout, R.mipmap.gv_support, R.mipmap.gv_integral, R.mipmap.gift_coupon, R.mipmap.consignee_address, R.mipmap.gv_service};
    public String[] imgTxt = null;
    //    public int[] partnerLogos = {R.mipmap.taobao, R.mipmap.tmall, R.mipmap.jd, R.mipmap.amzon};
//    public String[] partnerName = null;
    private WaittingDialog dialog;

    public MineFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initData();
    }



    private void initData() {
//        partnerName = activity.getResources().getStringArray(R.array.partner_name);
        imgTxt = activity.getResources().getStringArray(R.array.mine_gv_txt);
        if (imgIds.length == imgTxt.length) {
            gvList = new ArrayList<>();
            ImgTxtItem item = null;
            for (int i = 0; i < imgIds.length; i++) {
                item = new ImgTxtItem();
                item.imgId = imgIds[i];
                item.txt = imgTxt[i];
                gvList.add(item);
            }
        }

//        if (partnerLogos.length == partnerName.length) {
//            horizentalList = new ArrayList<ImgTxtItem>();
//            ImgTxtItem item = null;
//            for (int i = 0; i < partnerLogos.length; i++) {
//                item = new ImgTxtItem();
//                item.imgId = partnerLogos[i];
//                item.txt = partnerName[i];
//                horizentalList.add(item);
//            }
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.setFragmentLayout(R.layout.fragment_personalcenter);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
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
        dialog.show();
        ClientDiscoverAPI.getMineInfo(LoginInfo.getUserId() + "", new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                dialog.dismiss();
                LogUtil.e("result", responseInfo.result);
                if (responseInfo == null) {
                    return;
                }
                if (TextUtils.isEmpty(responseInfo.result)) {
                    return;
                }

                try {
                    user = JsonUtil.fromJson(responseInfo.result, new TypeToken<HttpResponse<User>>() {
                    });
                } catch (JsonSyntaxException e) {
                    LogUtil.e(TAG, e.getLocalizedMessage());
                    Util.makeToast("对不起,数据异常");
                }
                refreshUIAfterNet();
            }

            @Override
            public void onFailure(HttpException e, String s) {
                dialog.dismiss();
                if (TextUtils.isEmpty(s)) return;
                LogUtil.e(TAG, s);
                Util.makeToast("对不起,网络请求失败");
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtil.e(TAG, LoginInfo.isUserLogin() + "");
        if (LoginInfo.isUserLogin()) {
            rl.setVisibility(View.GONE);
            if (isInitLoad){
                isInitLoad=false;
            }else {
                loadData();
            }
        } else {
            rl.setVisibility(View.VISIBLE);
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
//        item_partner.setTVStyle(0, R.string.partners, R.color.color_333, false);
//        for (ImgTxtItem item : horizentalList) {
//            View view = Util.inflateView(activity, R.layout.horizontal_scroll_view_layout, null);
//            ImageView iv = (ImageView) view.findViewById(R.id.iv);
//            TextView tv = (TextView) view.findViewById(R.id.tv);
//            ImageLoader.getInstance().displayImage("drawable://" + item.imgId, iv);
//            tv.setText(item.txt);
//            ll.addView(view);
//            setOnClickListener(view, item);
//        }
    }

    @Override
    protected void refreshUIAfterNet() {
        if (user == null) return;
        if (user.counter!=null){
            if (adapter != null && gvList != null) {
                for (int i = 0; i < gvList.size(); i++) {//TODO 注意顺序和GridView位置
                    switch (i) {
                        case 0:
                            gvList.get(i).count = user.counter.order_total_count;
                            break;
                        case 1:
                            gvList.get(i).count = user.counter.message_total_count;
                            break;
                    }

                }
                adapter.notifyDataSetChanged();
            }
        }

        if (!TextUtils.isEmpty(user.medium_avatar_url)) {
            ImageLoader.getInstance().displayImage(user.medium_avatar_url, riv);
        }
        if (!TextUtils.isEmpty(user.head_pic_url)) {
            ImageUtils.loadBgImg(user.head_pic_url, ll_box);
//            Bitmap bitmap = ImageLoader.getInstance().loadImageSync(user.head_pic_url);
//            ll_box.setBackgroundDrawable(new BitmapDrawable(bitmap));
        }
        if (TextUtils.isEmpty(user.realname)) {
            tv_real.setVisibility(View.GONE);
        } else {
            tv_real.setText(user.realname);
        }

        if (TextUtils.isEmpty(user.nickname)) {
            tv_nick.setVisibility(View.GONE);
        } else {
            tv_nick.setText(user.nickname);
        }

        if (TextUtils.isEmpty(user.rank_title)) {
            tv_rank.setVisibility(View.GONE);
        } else {
            tv_rank.setText(String.format("%s | V%s", user.rank_title, user.rank_id));
        }
        tv_qj.setText(String.valueOf(user.scene_count));
        tv_cj.setText(String.valueOf(user.sight_count));
        tv_focus.setText(String.valueOf(user.follow_count));
        tv_fans.setText(String.valueOf(user.fans_count));
    }

//    private void setOnClickListener(View view, final ImgTxtItem item) {
//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                switch (item.imgId) {
//                    case R.mipmap.taobao:
//                        Util.makeToast(activity, "taobao");
//                        break;
//                    case R.mipmap.tmall:
//                        Util.makeToast(activity, "tmall");
//                        break;
//                    case R.mipmap.jd:
//                        Util.makeToast(activity, "jd");
//                        break;
//                    case R.mipmap.amzon:
//                        Util.makeToast(activity, "amzon");
//                        break;
//                }
//            }
//        });
//    }

    @OnClick({R.id.ll_box, R.id.iv_detail, R.id.item_about_us, R.id.item_feedback, R.id.item_partner, R.id.bt_register, R.id.ll_qj, R.id.ll_cj, R.id.ll_focus, R.id.ll_fans})
    protected void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.ll_box:
                startActivity(new Intent(activity, UserCenterActivity.class));
                break;
            case R.id.ll_qj:
                intent = new Intent(activity, UserCenterActivity.class);
                intent.putExtra(MineFragment.class.getSimpleName(), REQUEST_QJ);
                startActivity(intent);
                //
                break;
            case R.id.ll_cj:
                intent = new Intent(activity, UserCenterActivity.class);
                intent.putExtra(MineFragment.class.getSimpleName(), REQUEST_CJ);
                startActivity(intent);
                break;
            case R.id.ll_focus:
                intent = new Intent(activity, FocusFansActivity.class);
                intent.putExtra(FocusFansActivity.class.getSimpleName(), FocusFansActivity.FOCUS_TYPE);
                startActivity(intent);
                break;
            case R.id.ll_fans:
                intent = new Intent(activity, FocusFansActivity.class);
                intent.putExtra(FocusFansActivity.class.getSimpleName(), FocusFansActivity.FANS_TYPE);
                startActivity(intent);
                break;
            case R.id.iv_detail:
                startActivity(new Intent(activity, FindFriendsActivity.class));
                break;
//            case R.id.iv_calendar:
//                Util.makeToast(activity, "日历");
//                break;
            case R.id.item_about_us:
                startActivity(new Intent(activity, AboutUsActivity.class));
                break;
            case R.id.item_feedback:
                startActivity(new Intent(activity, FeedbackActivity.class));
                break;
//            case R.id.item_partner:
//                Util.makeToast(activity, "合作伙伴");
//                break;
            case R.id.bt_register:
                intent = new Intent(getActivity(),
                        OptRegisterLoginActivity.class);

                startActivity(intent);
                break;

        }
    }

    @Override
    protected void installListener() {
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        Util.makeToast("订单");
                        break;
                    case 1:
                        startActivity(new Intent(activity, MessageActivity.class));
                        break;
                    case 2:
                        startActivity(new Intent(activity, OrderQJActivity.class));
                        break;
                    case 4:
                        Intent intent = new Intent(getActivity(), HasLoveActivity.class);
                        intent.putExtra("user", user);
                        startActivity(intent);
                        break;
                    case 3:
                        startActivity(new Intent(activity, SystemSettingsActivity.class));
                        break;
                    case 7:
                        startActivity(new Intent(activity, SelectAddressActivity.class));
                        break;
                }
            }
        });
    }

}
