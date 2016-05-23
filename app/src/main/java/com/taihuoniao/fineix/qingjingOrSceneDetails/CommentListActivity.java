package com.taihuoniao.fineix.qingjingOrSceneDetails;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.CommentsListAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.base.NetBean;
import com.taihuoniao.fineix.beans.CommentsBean;
import com.taihuoniao.fineix.beans.LoginInfo;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.network.DataPaser;
import com.taihuoniao.fineix.user.OptRegisterLoginActivity;
import com.taihuoniao.fineix.view.GlobalTitleLayout;
import com.taihuoniao.fineix.view.WaittingDialog;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshBase;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by taihuoniao on 2016/4/21.
 */
public class CommentListActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    //上个界面传递过来的评论关联id
    private String target_id;
    private String type;//类型  12 场景评论
    private String target_user_id;//关联用户id
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
    private Button sendBtn;
    private PopupWindow popupWindow;
    //popupWindow下的控件
    private TextView deleteTv;
    private TextView cancelTv;
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
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        activityView = View.inflate(CommentListActivity.this, R.layout.activity_commentlist, null);
        setContentView(activityView);
        titleLayout = (GlobalTitleLayout) findViewById(R.id.activity_commentlist_titlelayout);
        pullToRefreshLayout = (PullToRefreshListView) findViewById(R.id.activity_commentlist_pulltorefreshview);
        nothingTv = (TextView) findViewById(R.id.activity_commentlist_nothing);
        progressBar = (ProgressBar) findViewById(R.id.activity_commentlist_progressbar);
        listView = pullToRefreshLayout.getRefreshableView();
        listView.setSelector(R.color.nothing);
        editText = (EditText) findViewById(R.id.activity_commentlist_edit);
        sendBtn = (Button) findViewById(R.id.activity_commentlist_send);
        dialog = new WaittingDialog(this);
        initPopupWindow();
    }

    @Override
    protected void initList() {
        target_id = getIntent().getStringExtra("target_id");
        type = getIntent().getStringExtra("type");
        target_user_id = getIntent().getStringExtra("target_user_id");
        if (target_id == null || type == null || target_user_id == null) {
            Toast.makeText(CommentListActivity.this, "数据错误", Toast.LENGTH_SHORT).show();
            if (dialog != null) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
            finish();
        }
        titleLayout.setBackgroundResource(R.color.white);
        titleLayout.setBackImg(R.mipmap.back_black);
        titleLayout.setTitle(R.string.comment, getResources().getColor(R.color.title_black));
        titleLayout.setContinueTvVisible(false);
        commentList = new ArrayList<>();
        commentsListAdapter = new CommentsListAdapter(CommentListActivity.this, commentList);
        listView.setAdapter(commentsListAdapter);
        pullToRefreshLayout.setPullToRefreshEnabled(false);
//        pullToRefreshLayout.setEmptyView(nothingTv);
        pullToRefreshLayout.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                progressBar.setVisibility(View.VISIBLE);
                currentPage++;
                DataPaser.commentsList(currentPage + "", 8 + "", target_id, null, type, handler);
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
                    if (bottom < oldBottom) {
//                        Log.e("<<<", "弹起");
                        isOpen = true;
                    } else if (bottom > oldBottom) {
//                        Log.e("<<<", "键盘隐藏");
                        isOpen = false;
                    }
                }
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
        dialog.show();
        DataPaser.commentsList(currentPage + "", 8 + "", target_id, null, type, handler);
    }

    private void initPopupWindow() {
        WindowManager windowManager = CommentListActivity.this.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        View popup_view = View.inflate(CommentListActivity.this, R.layout.popup_comment_delete, null);
        deleteTv = (TextView) popup_view.findViewById(R.id.popup_comment_delete_delete);
//        jubaoTv = (TextView) popup_view.findViewById(R.id.popup_scene_detail_more_jubao);
        cancelTv = (TextView) popup_view.findViewById(R.id.popup_scene_detail_more_cancel);
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
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
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


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DataConstants.DELETE_COMMENT:
                    popupWindow.dismiss();
                    NetBean netBean1 = (NetBean) msg.obj;
                    Toast.makeText(CommentListActivity.this, netBean1.getMessage(), Toast.LENGTH_SHORT).show();
                    if (netBean1.isSuccess()) {
                        currentPage = 1;
                        DataPaser.commentsList(currentPage + "", 8 + "", target_id, null, type, handler);
                    } else {
                        dialog.dismiss();
                    }
                    break;
                case DataConstants.SEND_COMMENT:
                    NetBean netBean = (NetBean) msg.obj;
                    Toast.makeText(CommentListActivity.this, netBean.getMessage(), Toast.LENGTH_SHORT).show();
                    if (netBean.isSuccess()) {
                        editText.setHint("评论一下");
                        is_reply = 0 + "";
                        reply_id = null;
                        reply_user_id = null;
                        editText.setText("");
                        currentPage = 1;
                        DataPaser.commentsList(currentPage + "", 8 + "", target_id, null, type, handler);
                    } else {
                        dialog.dismiss();
                        if ("0".equals(netBean.getCurrent_user_id())) {
                            MainApplication.which_activity = DataConstants.ElseActivity;
                            startActivity(new Intent(CommentListActivity.this, OptRegisterLoginActivity.class));
                        }
                    }
                    break;
                case DataConstants.COMMENTS_LIST:
                    dialog.dismiss();
                    progressBar.setVisibility(View.GONE);
                    CommentsBean netCommentsBean = (CommentsBean) msg.obj;
                    currentUserId = netCommentsBean.getCurrent_user_id();
                    if (netCommentsBean.isSuccess()) {
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
                    break;
                case DataConstants.NET_FAIL:
                    dialog.dismiss();
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(CommentListActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        //        cancelNet();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
        super.onDestroy();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.popup_comment_delete_delete:
                if (po == -1) {
                    return;
                }
                dialog.show();
                DataPaser.deleteComment(commentList.get(po).get_id(), handler);
                break;
            case R.id.popup_scene_detail_more_cancel:
                popupWindow.dismiss();
                break;
            case R.id.activity_commentlist_send:
                if (TextUtils.isEmpty(editText.getText())) {
                    return;
                }
                if (!LoginInfo.isUserLogin()) {
                    Toast.makeText(CommentListActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
                    MainApplication.which_activity = DataConstants.ElseActivity;
                    startActivity(new Intent(CommentListActivity.this, OptRegisterLoginActivity.class));
                }
                dialog.show();
                switch (is_reply) {
                    case "1":
                        if (reply_id == null || reply_user_id == null) {
                            dialog.dismiss();
                            Toast.makeText(CommentListActivity.this, "请选择回复评论", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        DataPaser.sendComment(target_id, editText.getText().toString(), type, target_user_id, is_reply, reply_id, reply_user_id, handler);
                        break;
                    default:
                        DataPaser.sendComment(target_id, editText.getText().toString(), type, target_user_id, is_reply, null, null, handler);
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
        Log.e("<<<", "currentUserId=" + currentUserId + ",user_id=" + commentList.get(position).getUser().get_id());
        if (currentUserId.equals(commentList.get(position).getUser().get_id())) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//获取状态信息
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
}
