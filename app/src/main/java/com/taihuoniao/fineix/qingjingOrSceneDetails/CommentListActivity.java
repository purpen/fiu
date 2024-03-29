package com.taihuoniao.fineix.qingjingOrSceneDetails;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.CommentsListAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.common.GlobalDataCallBack;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.CommentsBean;
import com.taihuoniao.fineix.beans.LoginInfo;
import com.taihuoniao.fineix.main.App;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.user.OptRegisterLoginActivity;
import com.taihuoniao.fineix.user.UserCommentsActivity;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.utils.WindowUtils;
import com.taihuoniao.fineix.view.GlobalTitleLayout;
import com.taihuoniao.fineix.view.dialog.WaittingDialog;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshBase;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

/**
 * Created by taihuoniao on 2016/4/21.
 */
public class CommentListActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    //上个界面传递过来的评论关联id
    private String target_id;//情境id等
    private String type;//类型  12 场景评论
//    private String target_user_id;//关联用户id 创建这个情境的用户id
    //界面下的控件
    private View activityView;
    private GlobalTitleLayout titleLayout;
    private PullToRefreshListView pullToRefreshLayout;
    private TextView nothingTv;
    private ProgressBar progressBar;
    private ListView listView;
    private List<CommentsBean.CommentItem> commentList;
    private CommentsListAdapter commentsListAdapter;
    private EditText editText;
    private TextView sendBtn;
    private PopupWindow popupWindow;
    //网络请求
    private WaittingDialog dialog;
    private String currentUserId = null;//网络获取的当前用户id
    //评论列表页码
    private int currentPage = 1;
    //判断软键盘弹起还是隐藏
    private boolean isOpen = false;//false隐藏 true 显示

    public CommentListActivity() {
        super(0);
    }

    @Override
    protected void initView() {
        activityView = View.inflate(CommentListActivity.this, R.layout.activity_commentlist, null);
        setContentView(activityView);
        titleLayout = (GlobalTitleLayout) findViewById(R.id.activity_commentlist_titlelayout);
        pullToRefreshLayout = (PullToRefreshListView) findViewById(R.id.activity_commentlist_pulltorefreshview);
        nothingTv = (TextView) findViewById(R.id.activity_commentlist_nothing);
        progressBar = (ProgressBar) findViewById(R.id.activity_commentlist_progressbar);
        listView = pullToRefreshLayout.getRefreshableView();
        listView.setSelector(R.color.nothing);
        editText = (EditText) findViewById(R.id.activity_commentlist_edit);
        sendBtn = (TextView) findViewById(R.id.activity_commentlist_send);
        dialog = new WaittingDialog(this);
        WindowUtils.chenjin(this);
        initPopupWindow();
    }

    @Override
    protected void initList() {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
//                    sendBtn.setTextColor(getResources().getColor(R.color.black));
                    sendBtn.setEnabled(true);
                } else {
//                    sendBtn.setTextColor(getResources().getColor(R.color.color_ccc));
                    sendBtn.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        target_id = getIntent().getStringExtra("target_id");
        type = getIntent().getStringExtra("type");
//        target_user_id = getIntent().getStringExtra("target_user_id");
        if (target_id == null || type == null ) {
            ToastUtils.showError("数据错误");
            if (dialog != null) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
            finish();
        }
        titleLayout.setTitle(R.string.comment);
        titleLayout.setContinueTvVisible(false);
        commentList = new ArrayList<>();
        commentsListAdapter = new CommentsListAdapter(CommentListActivity.this, commentList);
        listView.setAdapter(commentsListAdapter);
        pullToRefreshLayout.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener() {
            @Override
            public void onRefresh() {
                currentPage = 1;
                requestNet();
            }
        });
        pullToRefreshLayout.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                progressBar.setVisibility(View.VISIBLE);
                currentPage++;
                getComments(currentPage + "", 8 + "", target_id, null, type);
            }
        });
        pullToRefreshLayout.setOnClickListener(this);
        sendBtn.setOnClickListener(this);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
        activityView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
//                Log.e("<<<评论布局改变", "left=" + left + ",top=" + top + ",right=" + right + ",bottom=" + bottom + ",oldLeft=" + oldLeft
//                        + ",oldTop=" + oldTop + ",oldRight=" + oldRight + ",oldBottom=" + oldBottom);
                if (oldRight != 0 && oldBottom != 0) {
                    if (oldBottom - bottom > App.getStatusBarHeight()) {
//                        Log.e("<<<", "弹起");
                        isOpen = true;
                    } else if (bottom - oldBottom > App.getStatusBarHeight()) {
//                        Log.e("<<<", "键盘隐藏");
                        isOpen = false;
                    }
                }
//                Log.e("<<<布局", isOpen + "");
            }
        });
        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
//                Log.e("<<<touch","event="+event.getAction()+",isopen="+isOpen);
                if (isOpen && event.getAction() == MotionEvent.ACTION_UP) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    editText.setHint("评论一下");
                    is_reply = 0 + "";
                    reply_id = null;
                    reply_user_id = null;
                    imm.hideSoftInputFromWindow(editText.getWindowToken(), 0); //强制隐藏键盘
                    return true;
                }
                return false;
            }
        });
    }


    @Override
    protected void requestNet() {
        if (!dialog.isShowing()) {
            dialog.show();
        }
        getComments(currentPage + "", 8 + "", target_id, null, type);
    }

    private void initPopupWindow() {
        WindowManager windowManager = CommentListActivity.this.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        View popup_view = View.inflate(CommentListActivity.this, R.layout.popup_comment_delete, null);
        TextView deleteTv = (TextView) popup_view.findViewById(R.id.popup_comment_delete_delete);
//        jubaoTv = (TextView) popup_view.findViewById(R.id.popup_scene_detail_more_jubao);
        TextView cancelTv = (TextView) popup_view.findViewById(R.id.popup_scene_detail_more_cancel);
        popupWindow = new PopupWindow(popup_view, display.getWidth(), ViewGroup.LayoutParams.WRAP_CONTENT, true);
        // 设置动画效果
        popupWindow.setAnimationStyle(R.style.popupwindow_style);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        deleteTv.setOnClickListener(this);
//        jubaoTv.setOnClickListener(this);
        cancelTv.setOnClickListener(this);

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams params = getWindow().getAttributes();
                params.alpha = 1f;
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                getWindow().setAttributes(params);

            }
        });
        popupWindow.setBackgroundDrawable(ContextCompat.getDrawable(CommentListActivity.this,
                R.color.white));
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
    }

    private void showPopup() {
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = 0.4f;
        getWindow().setAttributes(params);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        popupWindow.showAtLocation(activityView, Gravity.BOTTOM, 0, 0);
    }

    private Call delelteHandler;

    //删除评论
    private void deleteComment(String id) {
        HashMap<String, String> params = ClientDiscoverAPI. getdeleteCommentRequestParams(id);
        delelteHandler = HttpRequest.post(params, URL.DELETE_COMMENT, new GlobalDataCallBack(){
            @Override
            public void onSuccess(String json) {
                HttpResponse netBean = JsonUtil.fromJson(json, HttpResponse.class);
                popupWindow.dismiss();
                if (netBean.isSuccess()) {
                    currentPage = 1;
                    getComments(currentPage + "", 8 + "", target_id, null, type);
                } else {
                    dialog.dismiss();
                    ToastUtils.showError(netBean.getMessage());
                }
            }

            @Override
            public void onFailure(String error) {
                dialog.dismiss();
                ToastUtils.showError("网络错误");
            }
        });
    }

    private Call sendHandler;

    //发表评论
    private void sendComments(String target_i, String conten, String typ, String target_user_id, String is_r, String reply_i, String reply_user_i) {
        HashMap<String, String> params = ClientDiscoverAPI.getsendCommentRequestParams(target_id, conten, type, target_user_id, is_reply, reply_id, reply_user_id);
        sendHandler = HttpRequest.post(params, URL.SEND_COMMENT, new GlobalDataCallBack(){
            @Override
            public void onSuccess(String json) {
                HttpResponse netBean = JsonUtil.fromJson(json, HttpResponse.class);
//                    Toast.makeText(CommentListActivity.this, netBean.getMessage(), Toast.LENGTH_SHORT).show();
                if (netBean.isSuccess()) {
                    editText.setHint("评论一下");
                    is_reply = 0 + "";
                    reply_id = null;
                    reply_user_id = null;
                    editText.setText("");
                    currentPage = 1;
                    getComments(currentPage + "", 8 + "", target_id, null, type);
                } else {
                    dialog.dismiss();
                    ToastUtils.showError(netBean.getMessage());
//                        dialog.showErrorWithStatus(netBean.getMessage());
                    if ("0".equals(netBean.getCurrent_user_id())) {
                        MainApplication.which_activity = DataConstants.ElseActivity;
                        startActivity(new Intent(CommentListActivity.this, OptRegisterLoginActivity.class));
                    }
                }
            }

            @Override
            public void onFailure(String error) {

            }
        });
    }

//    private HttpHandler<String> commentsHander;

    //评论列表
    private void getComments(String page, String size, String target_id, String target_user_id, String type) {
        HashMap<String, String> params =ClientDiscoverAPI. getcommentsListRequestParams(page, size, target_id, target_user_id, type);
        HttpRequest.post(params,URL.COMMENTS_LIST, new GlobalDataCallBack(){
            @Override
            public void onSuccess(String json) {
                HttpResponse<CommentsBean> netCommentsBean = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<CommentsBean>>() {});
                dialog.dismiss();
                pullToRefreshLayout.onRefreshComplete();
                progressBar.setVisibility(View.GONE);
                currentUserId = netCommentsBean.getCurrent_user_id();
                if (netCommentsBean.isSuccess()) {
                    pullToRefreshLayout.setLoadingTime();
                    if (currentPage == 1) {
                        pullToRefreshLayout.lastSavedFirstVisibleItem = -1;
                        pullToRefreshLayout.lastTotalItem = -1;
                        commentList.clear();
                    }
                    commentList.addAll(netCommentsBean.getData().getRows());
                    if (commentList.size() <= 0) {
                        nothingTv.setVisibility(View.VISIBLE);
                    } else {
                        nothingTv.setVisibility(View.GONE);
                    }
                    commentsListAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(String error) {
                dialog.dismiss();
                ToastUtils.showError("网络错误");
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (commentList != null && nothingTv.getVisibility() != View.VISIBLE) {
            Intent intent = new Intent();
            intent.putExtra(CommentListActivity.class.getSimpleName(), commentList.size());
            setResult(RESULT_OK, intent);
        }
        super.onBackPressed();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.popup_comment_delete_delete:
                if (po == -1) {
                    return;
                }
                if (!dialog.isShowing()) {
                    dialog.show();
                }
//                DataPaser.deleteComment(commentList.get(po).get_id(), handler);
                deleteComment(commentList.get(po).get_id());
                break;
            case R.id.popup_scene_detail_more_cancel:
                popupWindow.dismiss();
                break;
            case R.id.activity_commentlist_send:
                if (TextUtils.isEmpty(editText.getText())) {
                    return;
                }
                if (!LoginInfo.isUserLogin()) {
                    MainApplication.which_activity = DataConstants.ElseActivity;
                    startActivity(new Intent(CommentListActivity.this, OptRegisterLoginActivity.class));
                    return;
                }
                dialog.show();
                switch (is_reply) {
                    case "1":
                        if (reply_id == null || reply_user_id == null) {
                            dialog.dismiss();
                            ToastUtils.showError("请选择回复评论");
                            return;
                        }
                        sendComments(target_id, editText.getText().toString(), type, null, is_reply, reply_id, reply_user_id);
                        break;
                    default:
                        sendComments(target_id, editText.getText().toString(), type, null, is_reply, null, null);
                        break;
                }
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (currentUserId == null) {
            currentPage = 1;
            requestNet();
            return;
        }
//        Log.e("<<<", "currentUserId=" + currentUserId + ",user_id=" + commentList.get(position).getUser().get_id());
        if (currentUserId.equals(commentList.get(position).getUser().get_id())) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//获取状态信息
//        Log.e("<<<点击", isOpen + "");
        if (!isOpen) {
            String name = commentList.get(position).getUser().getNickname();
            editText.setHint("回复  " + name + ":");
            is_reply = 1 + "";
            reply_id = commentList.get(position).get_id();
            reply_user_id = commentList.get(position).getUser().get_id();
            //2.调用showSoftInput方法显示软键盘，其中view为聚焦的view组件
            imm.showSoftInput(editText, InputMethodManager.SHOW_FORCED);
        }

    }

    private String is_reply = 0 + "";//是回复还是评论 0评论 1回复
    private String reply_id;//被回复评论id
    private String reply_user_id;//被回复人id

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        if (currentUserId == null) {
            currentPage = 1;
            requestNet();
            return false;
        }
        if (currentUserId.equals(commentList.get(position).getUser().get_id())) {
            po = position;
            showPopup();
        }
        return false;
    }

    private int po = -1;//要删除的评论
    private boolean isFocus;

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (!isFocus && hasFocus && getIntent().hasExtra(UserCommentsActivity.class.getSimpleName())) {
            reply_id = getIntent().getStringExtra("reply_id");
            reply_user_id = getIntent().getStringExtra("reply_user_id");
            if (currentUserId.equals(reply_user_id)) {
                return;
            }
            isFocus = true;
            String name = getIntent().getStringExtra(UserCommentsActivity.class.getSimpleName());
            editText.setHint("回复  " + name + ":");
            is_reply = 1 + "";
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            //2.调用showSoftInput方法显示软键盘，其中view为聚焦的view组件
            imm.showSoftInput(editText, InputMethodManager.SHOW_FORCED);
        }
    }

    @Override
    protected void onDestroy() {
//        if (commentsHander != null)
//            commentsHander.cancel();
        if (delelteHandler != null)
            delelteHandler.cancel();
        if (sendHandler != null)
            sendHandler.cancel();
        super.onDestroy();
    }
}
