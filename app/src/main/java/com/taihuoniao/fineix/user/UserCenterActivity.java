package com.taihuoniao.fineix.user;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.FocusFansAdapter;
import com.taihuoniao.fineix.adapters.UserCJListAdapter;
import com.taihuoniao.fineix.adapters.UserQJListAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.LoginInfo;
import com.taihuoniao.fineix.beans.QingJingListBean;
import com.taihuoniao.fineix.beans.SceneListBean;
import com.taihuoniao.fineix.beans.User;
import com.taihuoniao.fineix.beans.UserCJListData;
import com.taihuoniao.fineix.main.fragment.MineFragment;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.HttpResponse;
import com.taihuoniao.fineix.qingjingOrSceneDetails.SceneDetailActivity;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.LogUtil;
import com.taihuoniao.fineix.utils.PopupWindowUtil;
import com.taihuoniao.fineix.utils.Util;
import com.taihuoniao.fineix.view.WaittingDialog;
import com.taihuoniao.fineix.view.roundImageView.RoundedImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author lilin
 *         created at 2016/4/26 17:43
 */
public class UserCenterActivity extends BaseActivity implements View.OnClickListener {
    private int curPage = 1;
    private static final String PAGE_SIZE = "10";
    private UserCJListAdapter adapterCJ;
    private UserQJListAdapter adapterQJ;
    private List<SceneListBean> mSceneList = new ArrayList<>();
    private List<QingJingListBean.QingJingItem> mQJList = new ArrayList<>();
    private LinearLayout ll_box;
    private LinearLayout ll_btn_box;
    @Bind(R.id.tv_title)
    TextView tv_title;
    private RoundedImageView riv;
    private TextView tv_real;
    private TextView tv_nick;
    private TextView tv_rank;
    private TextView tv_qj;
    private TextView tv_cj;
    private TextView tv_focus;
    private TextView tv_fans;
    private Button bt_focus;
    private Button bt_msg;
    private ImageButton ibtn;
    private ImageView iv_bg;
    private TextView tv_tag;
    @Bind(R.id.iv_detail)
    ImageButton iv_detail;
    @Bind(R.id.iv_right)
    ImageButton iv_right;
    private LinearLayout ll_focus;
    private LinearLayout ll_fans;
    private LinearLayout ll_qj;
    private LinearLayout ll_cj;
    private User user;
    private int which = MineFragment.REQUEST_CJ;
    private long userId = LoginInfo.getUserId();
    private static final int REQUEST_CODE_PICK_IMAGE = 100;
    private static final int REQUEST_CODE_CAPTURE_CAMERA = 101;
    private WaittingDialog dialog;
    public static final Uri imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),"temp.jpg"));
    @Bind(R.id.lv_cj)
    ListView lv_cj;
    @Bind(R.id.lv_qj)
    ListView lv_qj;

    public UserCenterActivity() {
        super(R.layout.activity_user_center);
    }

    @Override
    protected void getIntentData() {
        Intent intent = getIntent();
        if (intent.hasExtra(MineFragment.class.getSimpleName())) {
            which = intent.getIntExtra(MineFragment.class.getSimpleName(), MineFragment.REQUEST_CJ);
        }

        if (intent.hasExtra(FocusFansActivity.USER_ID_EXTRA)) {
            userId = intent.getLongExtra(FocusFansActivity.USER_ID_EXTRA, LoginInfo.getUserId());
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        requestNet();
    }

    @Override
    protected void initView() {
        View headView = Util.inflateView(activity, R.layout.user_center_headview, null);
//        iv_detail = ButterKnife.findById(headView, R.id.iv_detail);
        iv_bg = ButterKnife.findById(headView, R.id.iv_bg);
        tv_tag = ButterKnife.findById(headView,R.id.tv_tag);
        riv = ButterKnife.findById(headView, R.id.riv);
        tv_nick = ButterKnife.findById(headView, R.id.tv_nick);
        tv_real = ButterKnife.findById(headView, R.id.tv_real);
        tv_rank = ButterKnife.findById(headView, R.id.tv_rank);
//        tv_title = ButterKnife.findById(headView, R.id.tv_title);
        ibtn = ButterKnife.findById(headView, R.id.ibtn);
        ll_btn_box = ButterKnife.findById(headView, R.id.ll_btn_box);
        tv_cj = ButterKnife.findById(headView, R.id.tv_cj);
        tv_qj = ButterKnife.findById(headView, R.id.tv_qj);
        tv_focus = ButterKnife.findById(headView, R.id.tv_focus);
        tv_fans = ButterKnife.findById(headView, R.id.tv_fans);
        bt_focus = ButterKnife.findById(headView, R.id.bt_focus);
        bt_msg = ButterKnife.findById(headView, R.id.bt_msg);
        ll_box = ButterKnife.findById(headView, R.id.ll_box);
//        iv_right = ButterKnife.findById(headView, R.id.iv_right);
        ll_focus = ButterKnife.findById(headView, R.id.ll_focus);
        ll_fans = ButterKnife.findById(headView, R.id.ll_fans);
        ll_cj = ButterKnife.findById(headView, R.id.ll_cj);
        ll_qj = ButterKnife.findById(headView, R.id.ll_qj);

        dialog = new WaittingDialog(this);

        lv_cj.addHeaderView(headView);
        lv_qj.addHeaderView(headView);
        lv_cj.setAdapter(adapterCJ);
        lv_qj.setAdapter(adapterQJ);
        if (userId == LoginInfo.getUserId()) {
            ll_btn_box.setVisibility(View.GONE);
//            ibtn.setVisibility(View.VISIBLE);
        } else {
            ll_btn_box.setVisibility(View.VISIBLE);
//            ibtn.setVisibility(View.GONE);
        }
    }

    @Override
    protected void requestNet() {
        if (userId <= 0) {
            LogUtil.e(TAG, "userId<=0");
            return;
        }
        LogUtil.e(TAG, "requestNet==" + userId);
        ClientDiscoverAPI.getMineInfo(userId + "", new RequestCallBack<String>() {
            @Override
            public void onStart() {
                if (dialog!=null&& !activity.isFinishing()) dialog.show();
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (dialog!=null) dialog.dismiss();
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
                refreshUI();
            }

            @Override
            public void onFailure(HttpException e, String s) {
                if (dialog!=null) dialog.dismiss();
                if (TextUtils.isEmpty(s))
                    LogUtil.e(TAG, s);
                Util.makeToast(s);
            }
        });

        switch (which) {
            case MineFragment.REQUEST_CJ:
                lv_qj.setVisibility(View.GONE);
                lv_cj.setVisibility(View.VISIBLE);
                loadCJData();
                break;
            case MineFragment.REQUEST_QJ:
                lv_cj.setVisibility(View.GONE);
                lv_qj.setVisibility(View.VISIBLE);
                loadQJData();
                break;
        }
    }

    /**
     * 加载场景数据
     */
    private void loadCJData() {
        ClientDiscoverAPI.getSceneList(String.valueOf(curPage), PAGE_SIZE, String.valueOf(userId), new RequestCallBack<String>() {
            @Override
            public void onStart() {
                if (dialog != null && !activity.isFinishing()) {
                    if (curPage == 1) dialog.show();
                }
                curPage++;
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (dialog!=null) dialog.dismiss();
                if (responseInfo == null) return;
                if (TextUtils.isEmpty(responseInfo.result)) return;
                LogUtil.e("getSceneList", responseInfo.result);
                HttpResponse response = JsonUtil.fromJson(responseInfo.result, HttpResponse.class);
                if (response.isSuccess()) {
                    UserCJListData listBean = JsonUtil.fromJson(responseInfo.result, new TypeToken<HttpResponse<UserCJListData>>() {
                    });
                    List list = listBean.rows;
                    refreshCJUI(list);
                    return;
                }
                Util.makeToast(response.getMessage());
            }

            @Override
            public void onFailure(HttpException e, String s) {
                if (dialog!=null) dialog.dismiss();
                if (TextUtils.isEmpty(s)) return;
                Util.makeToast(s);
            }
        });
    }


    /**
     * 刷新场景数据
     *
     * @param list
     */
    private void refreshCJUI(List<SceneListBean> list) {
        if (list == null) return;
        if (list.size() == 0) return;
        if (adapterCJ == null) {
            mSceneList.addAll(list);
            adapterCJ = new UserCJListAdapter(mSceneList, activity);
            lv_cj.setAdapter(adapterCJ);
        } else {
            mSceneList.addAll(list);
            adapterCJ.notifyDataSetChanged();
        }

    }

    /**
     * 加载情景数据
     */
    private void loadQJData() {
        LogUtil.e("loadQJData",String.format("curPage==%s;;PAGE_SIZE==%s;;userId==%s",curPage,PAGE_SIZE,userId));
        ClientDiscoverAPI.getQJList(String.valueOf(curPage), PAGE_SIZE, String.valueOf(userId), new RequestCallBack<String>() {
            @Override
            public void onStart() {
                if (dialog != null && !activity.isFinishing()) {
                    if (curPage == 1) dialog.show();
                }
                curPage++;
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (dialog!=null) dialog.dismiss();
                if (responseInfo == null) return;
                if (TextUtils.isEmpty(responseInfo.result)) return;
                LogUtil.e("getQJList", responseInfo.result);
                QingJingListBean listBean = JsonUtil.fromJson(responseInfo.result, QingJingListBean.class);
                if (listBean.isSuccess()) {
                    List list = listBean.getData().getRows();
                    LogUtil.e("每次请求==",list.size()+"");
                    refreshQJUI(list);
                    return;
                }
                Util.makeToast(listBean.getMessage());
            }

            @Override
            public void onFailure(HttpException e, String s) {
                if (dialog!=null) dialog.dismiss();
                if (TextUtils.isEmpty(s)) return;
                Util.makeToast(s);
            }
        });
    }


    //更新情景UI
    private void refreshQJUI(List<QingJingListBean.QingJingItem> list) {
        if (list == null) return;
        if (list.size() == 0) return;
        if (adapterQJ == null) {
            mQJList.addAll(list);
            adapterQJ = new UserQJListAdapter(mQJList, activity);
            lv_qj.setAdapter(adapterQJ);
        } else {
            mQJList.addAll(list);
            adapterQJ.notifyDataSetChanged();
        }
    }


    /**
     * 更新headview的UI
     */
    @Override
    protected void refreshUI() {
        if (user == null) {
            return;
        }
        LogUtil.e(TAG, "refreshUI===" + user._id);
        if (user.is_love == FocusFansAdapter.NOT_LOVE) {
            bt_focus.setText("关注");
        } else {
            bt_focus.setText("已关注");
        }
        if (!TextUtils.isEmpty(user.nickname)) {
            if (LoginInfo.getUserId() != userId) {
                tv_title.setVisibility(View.VISIBLE);
                tv_title.setText(user.nickname);
            } else {
                tv_title.setVisibility(View.GONE);
            }
        }
        if (!TextUtils.isEmpty(user.medium_avatar_url)) {
            ImageLoader.getInstance().displayImage(user.medium_avatar_url, riv);
        }
        if (!TextUtils.isEmpty(user.head_pic_url)) {
            ImageLoader.getInstance().displayImage(user.head_pic_url, iv_bg);
        }

        if (TextUtils.isEmpty(user.summary)) {
            tv_real.setVisibility(View.GONE);
        } else {
            tv_real.setText(user.summary);
        }


        if (TextUtils.isEmpty(user.nickname) || LoginInfo.getUserId() != userId) {
            tv_nick.setVisibility(View.GONE);
        } else {
            tv_nick.setText(user.nickname);
        }

        if (!TextUtils.isEmpty(user.label)){
            if (user.identify.is_expert==0){
                tv_tag.setText(String.format("%s | ",user.label));
            }else {
                tv_tag.setText(String.format("%s | ",user.label));
                tv_tag.setBackgroundColor(Color.GREEN);
            }
        }
        tv_rank.setText(String.format("V%s", user.rank_id));
        tv_qj.setText(String.valueOf(user.scene_count));
        tv_cj.setText(String.valueOf(user.sight_count));
        tv_focus.setText(String.valueOf(user.follow_count));
        tv_fans.setText(String.valueOf(user.fans_count));
    }

    private View initPopView(int layout,String title) {
        View view = Util.inflateView(this, layout, null);
        ((TextView)view.findViewById(R.id.tv_title)).setText(title);
        View iv_take_photo = view.findViewById(R.id.tv_take_photo);
        View iv_take_album = view.findViewById(R.id.tv_album);
        View iv_close = view.findViewById(R.id.tv_cancel);
        iv_take_photo.setOnClickListener(this);
        iv_take_album.setOnClickListener(this);
        iv_close.setOnClickListener(this);
        return view;
    }

    @Override
    protected void installListener() {
        ll_box.setOnClickListener(this);
        iv_detail.setOnClickListener(this);
        iv_right.setOnClickListener(this);
        bt_focus.setOnClickListener(this);
        ll_focus.setOnClickListener(this);
        ll_fans.setOnClickListener(this);
        bt_msg.setOnClickListener(this);
        ibtn.setOnClickListener(this);
        ll_cj.setOnClickListener(this);
        ll_qj.setOnClickListener(this);

        lv_cj.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (mSceneList==null) return;
                if (mSceneList.size()==0) return;
                SceneListBean item=mSceneList.get(i-1);
                Intent intent = new Intent(activity, SceneDetailActivity.class);
                intent.putExtra("id", item.get_id());
                startActivity(intent);
            }
        });

        lv_cj.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                if (i == SCROLL_STATE_IDLE ||i==SCROLL_STATE_FLING) {
                    if (absListView.getLastVisiblePosition() == mSceneList.size()) {
                        LogUtil.e("curPage==",curPage+"");
                        loadCJData();
                    }
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
            }
        });

        lv_qj.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                if (i == SCROLL_STATE_IDLE || i==SCROLL_STATE_FLING) {
                    LogUtil.e("getLastVisiblePosition",absListView.getLastVisiblePosition()+"");
                    LogUtil.e("mQJList.size",mQJList.size()+"");
                    if (mQJList.size()%2==0){
                        if (absListView.getLastVisiblePosition() == mQJList.size()/2) {
                            LogUtil.e("curPage==偶数",curPage+"");
                            loadQJData();
                        }
                    }else {
                        if (absListView.getLastVisiblePosition() == mQJList.size()/2+1) {
                            LogUtil.e("curPage==奇数",curPage+"");
                            loadQJData();
                        }
                    }
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.tv_take_photo:
                PopupWindowUtil.dismiss();
                getImageFromCamera();
                break;
            case R.id.tv_album:
                PopupWindowUtil.dismiss();
                getImageFromAlbum();
                break;
            case R.id.tv_cancel:
                PopupWindowUtil.dismiss();
                break;
            case R.id.ll_box:
                if (LoginInfo.getUserId() != userId) return;
                PopupWindowUtil.show(activity, initPopView(R.layout.popup_upload_avatar,"更换背景封面"));
                break;
            case R.id.iv_right:
                startActivity(new Intent(activity, EditUserInfoActivity.class));
                break;
            case R.id.iv_detail:
                finish();
                break;
            case R.id.ll_focus:
                intent = new Intent(activity, FocusFansActivity.class);
                intent.putExtra(FocusFansActivity.class.getSimpleName(), FocusFansActivity.FOCUS_TYPE);
                intent.putExtra(FocusFansActivity.USER_ID_EXTRA, userId);
                startActivity(intent);
                break;
            case R.id.ll_fans:
                intent = new Intent(activity, FocusFansActivity.class);
                intent.putExtra(FocusFansActivity.class.getSimpleName(), FocusFansActivity.FANS_TYPE);
                intent.putExtra(FocusFansActivity.USER_ID_EXTRA, userId);
                startActivity(intent);
                break;
            case R.id.bt_msg:
                intent = new Intent(activity, PrivateMessageActivity.class);
                intent.putExtra(UserCenterActivity.class.getSimpleName(), user);
                startActivity(intent);
                break;
            case R.id.bt_focus:
                bt_focus.setEnabled(false);
                if (user.is_love == FocusFansAdapter.NOT_LOVE) {
                    ClientDiscoverAPI.focusOperate(userId + "", new RequestCallBack<String>() {
                        @Override
                        public void onSuccess(ResponseInfo<String> responseInfo) {
                            bt_focus.setEnabled(true);
                            if (responseInfo == null) return;
                            if (TextUtils.isEmpty(responseInfo.result)) return;
                            LogUtil.e("focusOperate", responseInfo.result);
                            HttpResponse response = JsonUtil.fromJson(responseInfo.result, HttpResponse.class);
                            if (response.isSuccess()) {
                                user.is_love = FocusFansAdapter.LOVE;
                                bt_focus.setText("已关注");
                                Util.makeToast(response.getMessage());
                                return;
                            }

                            Util.makeToast(response.getMessage());
                        }

                        @Override
                        public void onFailure(HttpException e, String s) {
                            bt_focus.setEnabled(true);
                            Util.makeToast(s);
                        }
                    });
                } else {
                    ClientDiscoverAPI.cancelFocusOperate(userId + "", new RequestCallBack<String>() {
                        @Override
                        public void onSuccess(ResponseInfo<String> responseInfo) {
                            bt_focus.setEnabled(true);
                            PopupWindowUtil.dismiss();
                            if (responseInfo == null) return;
                            if (TextUtils.isEmpty(responseInfo.result)) return;
                            LogUtil.e("cancelFocusOperate", responseInfo.result);
                            HttpResponse response = JsonUtil.fromJson(responseInfo.result, HttpResponse.class);
                            if (response.isSuccess()) {
                                user.is_love = FocusFansAdapter.NOT_LOVE;
                                bt_focus.setText("关注");
                                Util.makeToast(response.getMessage());
                                return;
                            }

                            Util.makeToast(response.getMessage());
                        }

                        @Override
                        public void onFailure(HttpException e, String s) {
                            bt_focus.setEnabled(true);
                            PopupWindowUtil.dismiss();
                            Util.makeToast(s);
                        }
                    });
                }
                break;
            case R.id.ibtn:
                Util.makeToast("认证");
                break;
            case R.id.ll_cj:
                if (which==MineFragment.REQUEST_CJ) return;
                showCj();
                break;
            case R.id.ll_qj:
                if (which==MineFragment.REQUEST_QJ) return;
                showQJ();
                break;
        }
    }

    private void showCj(){
        lv_cj.setVisibility(View.VISIBLE);
        lv_qj.setVisibility(View.GONE);
        which=MineFragment.REQUEST_CJ;
        curPage=1;
        mSceneList.clear();
        adapterCJ=null;
        loadCJData();
    }

    private void showQJ(){
        lv_qj.setVisibility(View.VISIBLE);
        lv_cj.setVisibility(View.GONE);
        which=MineFragment.REQUEST_QJ;
        curPage=1;
        LogUtil.e("清除前==",mQJList.size()+"");
        mQJList.clear();
        LogUtil.e("清除后==",mQJList.size()+"");
        adapterQJ=null;
        loadQJData();
    }

    protected void getImageFromAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");//相片类型
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
    }

    protected void getImageFromCamera() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(intent, REQUEST_CODE_CAPTURE_CAMERA);
        } else {
            Util.makeToast("请确认已经插入SD卡");
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Intent intent=null;
            File file = null;
            switch (requestCode) {
                case REQUEST_CODE_PICK_IMAGE:
                    Uri uri = data.getData();
                    if (uri != null) {
//                        Bitmap bitmap = ImageUtils.decodeUriAsBitmap(uri);
//                        mClipImageLayout.setImageBitmap(bitmap);
                        toCropActivity(uri);
                    } else {
                        Util.makeToast("抱歉，从相册获取图片失败");
                    }
                    break;
                case REQUEST_CODE_CAPTURE_CAMERA:
//                    Bitmap bitmap =ImageUtils.decodeUriAsBitmap(imageUri);
                    if (imageUri!=null){
//                        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,MediaStore.Images.Media.EXTERNAL_CONTENT_URI));
                        toCropActivity(imageUri);
                    }
                    break;
            }
        }
    }

    private void toCropActivity(Uri uri){
        Intent intent=new Intent(activity,ImageCropActivity.class);
        intent.putExtra(ImageCropActivity.class.getSimpleName(),uri);
        intent.putExtra(TAG,TAG);
        startActivity(intent);
    }

}
