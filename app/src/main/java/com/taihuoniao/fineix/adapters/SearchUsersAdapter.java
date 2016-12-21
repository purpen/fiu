package com.taihuoniao.fineix.adapters;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.NetBean;
import com.taihuoniao.fineix.beans.LoginInfo;
import com.taihuoniao.fineix.beans.SearchBean;
import com.taihuoniao.fineix.main.MainActivity;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.main.fragment.IndexFragment;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.user.FocusActivity;
import com.taihuoniao.fineix.user.OptRegisterLoginActivity;
import com.taihuoniao.fineix.user.UserCenterActivity;
import com.taihuoniao.fineix.utils.DensityUtils;
import com.taihuoniao.fineix.utils.GlideUtils;
import com.taihuoniao.fineix.utils.PopupWindowUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.utils.Util;
import com.taihuoniao.fineix.view.dialog.WaittingDialog;
import com.taihuoniao.fineix.view.roundImageView.RoundedImageView;

import java.lang.reflect.Type;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by taihuoniao on 2016/8/24.
 * 搜索页面搜索用户
 */
public class SearchUsersAdapter extends BaseAdapter {
    private Activity activity;
    private List<SearchBean.Data.SearchItem> list;
    private WaittingDialog dialog;

    public SearchUsersAdapter(Activity activity, List<SearchBean.Data.SearchItem> list) {
        this.activity = activity;
        this.list = list;
        dialog = new WaittingDialog(activity);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(parent.getContext(), R.layout.item_search_users, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        GlideUtils.displayImage(list.get(position).getAvatar_url(), holder.headImg);
        if (list.get(position).getIs_export() == 1) {
            holder.vImg.setVisibility(View.VISIBLE);
        } else {
            holder.vImg.setVisibility(View.GONE);
        }
        holder.name.setText(list.get(position).getNickname());
        holder.des.setText(list.get(position).getSummary());
        holder.headImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, UserCenterActivity.class);
                long l = Long.valueOf(list.get(position).getUser_id());
                intent.putExtra(FocusActivity.USER_ID_EXTRA, l);
                activity.startActivity(intent);
            }
        });
        if (list.get(position).getIs_follow() == 1) {
            holder.focusBtn.setBackgroundResource(R.drawable.border_radius5_pressed);
            holder.focusBtn.setText("已关注");
            holder.focusBtn.setPadding(DensityUtils.dp2px(activity, 7), 0, DensityUtils.dp2px(activity, 7), 0);
            holder.focusBtn.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.focus_pic, 0, 0, 0);
            holder.focusBtn.setTextColor(activity.getResources().getColor(R.color.white));
//            setFocusBtnStyle(holder.focusBtn, activity.getResources().getDimensionPixelSize(R.dimen.dp10), R.string.focused, R.mipmap.focus_pic, android.R.color.white, R.drawable.border_radius5_pressed);
        } else {
//            setFocusBtnStyle(holder.focusBtn, activity.getResources().getDimensionPixelSize(R.dimen.dp16), R.string.focus, R.mipmap.unfocus_pic, android.R.color.black, R.drawable.shape_subscribe_theme);
            holder.focusBtn.setPadding(0, 0, 0, 0);
            holder.focusBtn.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            holder.focusBtn.setBackgroundResource(R.drawable.shape_corner_969696_nothing);
            holder.focusBtn.setText("+关注");
            holder.focusBtn.setTextColor(activity.getResources().getColor(R.color.title_black));
        }
        holder.focusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LoginInfo.isUserLogin()) {
                    //已经登录
                    if (LoginInfo.getUserId() == Long.parseLong(list.get(position).getUser_id())) {
                        //过滤自己
                        return;
                    }
                    if (list.get(position).getIs_follow() == 0) {
                        if (!dialog.isShowing()) {
                            dialog.show();
                        }
                        fllow(position, list.get(position).getUser_id(), holder);
                    } else {
                        showFocusFansConfirmView(list.get(position), holder);
                    }
                    return;
                }
                MainApplication.which_activity = DataConstants.SearchActivity;
                ((MainActivity) activity).which = IndexFragment.class.getSimpleName();
                activity.startActivity(new Intent(activity, OptRegisterLoginActivity.class));
            }
        });
        return convertView;
    }

    //关注用户
    private void fllow(final int position, String otherUserId, final ViewHolder holder) {
        ClientDiscoverAPI.focusOperate(otherUserId, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                dialog.dismiss();
                Log.e("<<<关注用户", responseInfo.result);
                NetBean netBean = new NetBean();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<NetBean>() {
                    }.getType();
                    netBean = gson.fromJson(responseInfo.result, type);
                } catch (JsonSyntaxException e) {
                    Log.e("<<<", "解析异常");
                }
                if (netBean.isSuccess()) {
                    holder.focusBtn.setBackgroundResource(R.drawable.border_radius5_pressed);
                    holder.focusBtn.setText("已关注");
                    holder.focusBtn.setPadding(DensityUtils.dp2px(activity, 7), 0, DensityUtils.dp2px(activity, 7), 0);
                    holder.focusBtn.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.focus_pic, 0, 0, 0);
                    holder.focusBtn.setTextColor(activity.getResources().getColor(R.color.white));
                    list.get(position).setIs_follow(1);
                } else {
                    ToastUtils.showError(netBean.getMessage());
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                dialog.dismiss();
                ToastUtils.showError(R.string.net_fail);
            }
        });
    }

    //取消关注弹窗
    private void showFocusFansConfirmView(final SearchBean.Data.SearchItem item, final ViewHolder holder) {
        View view = Util.inflateView(activity, R.layout.popup_focus_fans, null);
        RoundedImageView riv = (RoundedImageView) view.findViewById(R.id.riv);
        TextView tv_take_photo = (TextView) view.findViewById(R.id.tv_take_photo);
        TextView tv_album = (TextView) view.findViewById(R.id.tv_album);
        TextView tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
        ImageLoader.getInstance().displayImage(item.getAvatar_url(), riv);
        tv_take_photo.setText(String.format("取消关注" + " %s ?", item.getNickname()));
        tv_album.setText("取消关注");
        tv_album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupWindowUtil.dismiss();
                if (!dialog.isShowing()) {
                    dialog.show();
                }
                cancelFollow(item, holder);
            }
        });
        tv_album.setTag(item);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupWindowUtil.dismiss();
            }
        });
        PopupWindowUtil.show(activity, view);
    }

    //取消关注
    private void cancelFollow(final SearchBean.Data.SearchItem item, final ViewHolder holder) {
        ClientDiscoverAPI.cancelFocusOperate(item.getUser_id(), new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                dialog.dismiss();
                NetBean netBean = new NetBean();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<NetBean>() {
                    }.getType();
                    netBean = gson.fromJson(responseInfo.result, type);
                } catch (JsonSyntaxException e) {
                    Log.e("<<<", "解析异常");
                }
                if (netBean.isSuccess()) {
                    holder.focusBtn.setPadding(0, 0, 0, 0);
                    holder.focusBtn.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    holder.focusBtn.setBackgroundResource(R.drawable.shape_corner_969696_nothing);
                    holder.focusBtn.setText("+关注");
                    holder.focusBtn.setTextColor(activity.getResources().getColor(R.color.title_black));
                    item.setIs_follow(0);
                    return;
                }
                ToastUtils.showError(netBean.getMessage());
            }

            @Override
            public void onFailure(HttpException e, String s) {
                dialog.dismiss();
                ToastUtils.showError(R.string.net_fail);
            }
        });
    }

    static class ViewHolder {
        @Bind(R.id.head_img)
        RoundedImageView headImg;
        @Bind(R.id.v_img)
        RoundedImageView vImg;
        @Bind(R.id.focus_btn)
        Button focusBtn;
        @Bind(R.id.name)
        TextView name;
        @Bind(R.id.des)
        TextView des;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
