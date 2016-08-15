package com.taihuoniao.fineix.adapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.FocusFansItem;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.beans.LoginInfo;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.LogUtil;
import com.taihuoniao.fineix.utils.PopupWindowUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.utils.Util;
import com.taihuoniao.fineix.view.roundImageView.RoundedImageView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author lilin
 *         created at 2016/4/22 19:00
 */
public class FocusAdapter extends CommonBaseAdapter<FocusFansItem> implements View.OnClickListener {
    private ImageLoader imageLoader;
    private static final int TYPE1 = 1; //单向关注
    private static final int TYPE2 = 2; //互向关注
    public static final int NOT_LOVE = 0; //别人的粉丝列表和LoginInfo.getUserId()的关系
    public static final int LOVE = 1;
    private long userId;
    public FocusAdapter(List<FocusFansItem> list, Activity activity, long userId) {
        super(list, activity);
        this.imageLoader = ImageLoader.getInstance();
        this.userId = userId;
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.default_focus_head)
                .showImageForEmptyUri(R.mipmap.default_focus_head)
                .showImageOnFail(R.mipmap.default_focus_head)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .delayBeforeLoading(0)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final FocusFansItem item = list.get(position);
        ViewHolder holder;
        if (convertView == null) {
            convertView = Util.inflateView(R.layout.item_focus_fans, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        imageLoader.displayImage(item.follows.avatar_url, holder.riv);
        if (item.follows.is_expert == 1) {
            holder.riv_auth.setVisibility(View.VISIBLE);
        } else {
            holder.riv_auth.setVisibility(View.GONE);
        }
        holder.tv_name.setText(item.follows.nickname);
        if (!TextUtils.isEmpty(item.follows.expert_label) && !TextUtils.isEmpty(item.follows.expert_info)) {
            holder.tv_desc.setText(String.format("%s | %s", item.follows.expert_label, item.follows.expert_info));
        } else {
            holder.tv_desc.setText(item.follows.summary);
        }

        //关注界面
        if (userId == LoginInfo.getUserId()) { //是自己
            if (item.focus_flag) {
//                holder.btn.setText("关注");
//                holder.btn.setTextColor(activity.getResources().getColor(R.color.color_333));
//                holder.btn.setBackgroundResource(R.drawable.border_radius5);
                setFocusBtnStyle(holder.btn, activity.getResources().getDimensionPixelSize(R.dimen.dp16), R.string.focus, R.mipmap.unfocus_pic, android.R.color.black, R.drawable.shape_subscribe_theme);
                holder.btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {//关注
                        doFocus(item, view);
                    }
                });
            } else {
//                holder.btn.setText("已关注");
//                holder.btn.setTextColor(activity.getResources().getColor(android.R.color.white));
//                holder.btn.setBackgroundResource(R.drawable.border_radius5_pressed);
                setFocusBtnStyle(holder.btn, activity.getResources().getDimensionPixelSize(R.dimen.dp10), R.string.focused, R.mipmap.focus_pic, android.R.color.white, R.drawable.border_radius5_pressed);
                holder.btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {//取消关注
                        showFocusFansConfirmView(item, "停止关注");
                    }
                });
            }
        } else { //处理别人的关注列表
            dealOthersFoucsFansStyle(item, holder);
        }
        return convertView;
    }

    private void doFocus(final FocusFansItem item, final View view) {
        if (userId == LoginInfo.getUserId()) { //关注列表做关注操作
            if (item == null) return;
            if (item.focus_flag) {
                ClientDiscoverAPI.focusOperate(item.follows.user_id + "", new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        view.setEnabled(true);
                        PopupWindowUtil.dismiss();
                        if (responseInfo == null) return;
                        if (TextUtils.isEmpty(responseInfo.result)) return;
                        HttpResponse response = JsonUtil.fromJson(responseInfo.result, HttpResponse.class);
                        if (response.isSuccess()) {
                            item.focus_flag = false;
                            notifyDataSetChanged();
//                            Util.makeToast(response.getMessage());
                            return;
                        }
                        ToastUtils.showError(response.getMessage());

                    }

                    @Override
                    public void onFailure(HttpException e, String s) {
                        view.setEnabled(true);
                        PopupWindowUtil.dismiss();
                        ToastUtils.showError("网络异常，请确认网络畅通");
                    }
                });
            }
        } else {
            dealOthersFocus(item, view);
        }
    }

    private void showFocusFansConfirmView(FocusFansItem item, String tips) {
        View view = Util.inflateView(activity, R.layout.popup_focus_fans, null);
        RoundedImageView riv = (RoundedImageView) view.findViewById(R.id.riv);
        TextView tv_take_photo = (TextView) view.findViewById(R.id.tv_take_photo);
        TextView tv_album = (TextView) view.findViewById(R.id.tv_album);
        TextView tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
        ImageLoader.getInstance().displayImage(item.follows.avatar_url, riv, options);
        tv_take_photo.setText(String.format(tips + " %s ?", item.follows.nickname));
        tv_album.setText(tips);
        tv_album.setOnClickListener(this);
        tv_album.setTag(item);
        tv_cancel.setOnClickListener(this);
        PopupWindowUtil.show(activity, view);
    }

    @Override
    public void onClick(final View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:
                PopupWindowUtil.dismiss();
                break;
            case R.id.tv_album:
                view.setEnabled(false);
                final FocusFansItem item = (FocusFansItem) view.getTag();
                if (userId == LoginInfo.getUserId()) { //关注列表做取消关注操作
                    if (item == null) return;
                    if (item.focus_flag) {
                        ClientDiscoverAPI.focusOperate(item.follows.user_id + "", new RequestCallBack<String>() {
                            @Override
                            public void onSuccess(ResponseInfo<String> responseInfo) {
                                view.setEnabled(true);
                                PopupWindowUtil.dismiss();
                                if (responseInfo == null) return;
                                if (TextUtils.isEmpty(responseInfo.result)) return;
                                HttpResponse response = JsonUtil.fromJson(responseInfo.result, HttpResponse.class);
                                if (response.isSuccess()) {
                                    item.focus_flag = false;
                                    notifyDataSetChanged();
                                    return;
                                }
                                ToastUtils.showError(response.getMessage());
                            }

                            @Override
                            public void onFailure(HttpException e, String s) {
                                view.setEnabled(true);
                                PopupWindowUtil.dismiss();
                                ToastUtils.showError("网络异常，请确认网络畅通");
//                                svProgressHUD.showErrorWithStatus("网络异常，请确认网络畅通");
                            }
                        });
                    } else {
                        ClientDiscoverAPI.cancelFocusOperate(item.follows.user_id + "", new RequestCallBack<String>() {
                            @Override
                            public void onSuccess(ResponseInfo<String> responseInfo) {
                                view.setEnabled(true);
                                PopupWindowUtil.dismiss();
                                if (responseInfo == null) return;
                                if (TextUtils.isEmpty(responseInfo.result)) return;
                                HttpResponse response = JsonUtil.fromJson(responseInfo.result, HttpResponse.class);
                                if (response.isSuccess()) {
//                                    list.remove(item);
                                    item.focus_flag = true;  //变为可关注
                                    notifyDataSetChanged();
                                    ToastUtils.showSuccess("已取消关注");
                                    return;
                                }
                                ToastUtils.showError(response.getMessage());
                            }

                            @Override
                            public void onFailure(HttpException e, String s) {
                                view.setEnabled(true);
                                PopupWindowUtil.dismiss();
                                ToastUtils.showError("网络异常，请确认网络畅通");
                            }
                        });
                    }
                } else { //处理别人的关注和粉丝列表
                    dealOthersFocus(item, view);
                }
                break;
        }
    }


    /**
     * 需求只考虑LoginInfo.getUserId()与别人的关注用户和粉丝的关系
     * @param item
     * @param holder
     */
    private void dealOthersFoucsFansStyle(final FocusFansItem item, final ViewHolder holder) {
        LogUtil.e("dealOthersFoucsFansStyle", "is_love===" + item.follows.is_love);
        if (item.follows.is_love == NOT_LOVE) {
//            holder.btn.setText("关注");
//            holder.btn.setTextColor(activity.getResources().getColor(R.color.color_333));
//            holder.btn.setBackgroundResource(R.drawable.border_radius5);
            setFocusBtnStyle(holder.btn, activity.getResources().getDimensionPixelSize(R.dimen.dp16), R.string.focus, R.mipmap.unfocus_pic, android.R.color.black, R.drawable.shape_subscribe_theme);
            holder.btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    showFocusFansConfirmView(item,"开始关注");
                    doFocus(item, view);
                }
            });
        } else if (item.follows.is_love == LOVE) {
//            holder.btn.setText("已关注");
//            holder.btn.setTextColor(activity.getResources().getColor(android.R.color.white));
//            holder.btn.setBackgroundResource(R.drawable.border_radius5_pressed);
            setFocusBtnStyle(holder.btn, activity.getResources().getDimensionPixelSize(R.dimen.dp10), R.string.focused, R.mipmap.focus_pic, android.R.color.white, R.drawable.border_radius5_pressed);
            holder.btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {//取消关注
                    showFocusFansConfirmView(item, "停止关注");
                }
            });
        }
    }

    private void setFocusBtnStyle(Button bt_focus, int dimensionPixelSize, int focus, int unfocus_pic, int color, int drawable) {
        bt_focus.setPadding(dimensionPixelSize, 0, dimensionPixelSize, 0);
        bt_focus.setText(focus);
        bt_focus.setTextColor(activity.getResources().getColor(color));
        bt_focus.setBackgroundResource(drawable);
        bt_focus.setCompoundDrawablesWithIntrinsicBounds(unfocus_pic, 0, 0, 0);
    }

    private void dealOthersFocus(final FocusFansItem item, final View view) {
        LogUtil.e("dealOthersFocusFans", "is_love===" + item.follows.is_love);
        if (item.follows.is_love == NOT_LOVE) { //别人的关注列表做关注操作
            ClientDiscoverAPI.focusOperate(item.follows.user_id + "", new RequestCallBack<String>() {
                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    view.setEnabled(true);
                    PopupWindowUtil.dismiss();
                    if (responseInfo == null) return;
                    if (TextUtils.isEmpty(responseInfo.result)) return;
                    HttpResponse response = JsonUtil.fromJson(responseInfo.result, HttpResponse.class);
                    if (response.isSuccess()) {
                        item.follows.is_love = LOVE;
                        notifyDataSetChanged();
                        return;
                    }
                    ToastUtils.showError(response.getMessage());
                }

                @Override
                public void onFailure(HttpException e, String s) {
                    view.setEnabled(true);
                    PopupWindowUtil.dismiss();
                    ToastUtils.showError(R.string.network_err);
                }
            });
        } else if (item.follows.is_love == LOVE) {
            ClientDiscoverAPI.cancelFocusOperate(item.follows.user_id + "", new RequestCallBack<String>() {
                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    view.setEnabled(true);
                    PopupWindowUtil.dismiss();
                    if (responseInfo == null) return;
                    if (TextUtils.isEmpty(responseInfo.result)) return;
                    HttpResponse response = JsonUtil.fromJson(responseInfo.result, HttpResponse.class);
                    if (response.isSuccess()) {
                        item.follows.is_love = NOT_LOVE;
                        notifyDataSetChanged();
                        ToastUtils.showSuccess("已取消关注");
                        return;
                    }
                    ToastUtils.showError(response.getMessage());
                }

                @Override
                public void onFailure(HttpException e, String s) {
                    view.setEnabled(true);
                    PopupWindowUtil.dismiss();
                    ToastUtils.showError("网络异常，请确认网络畅通");
                }
            });
        }

    }

    static class ViewHolder {
        @Bind(R.id.riv)
        ImageView riv;
        @Bind(R.id.riv_auth)
        RoundedImageView riv_auth;
        @Bind(R.id.tv_name)
        TextView tv_name;
        @Bind(R.id.tv_desc)
        TextView tv_desc;
        @Bind(R.id.btn)
        Button btn;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
