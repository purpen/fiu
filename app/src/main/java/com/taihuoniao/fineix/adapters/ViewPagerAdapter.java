package com.taihuoniao.fineix.adapters;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.gson.reflect.TypeToken;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.beans.IsInviteData;
import com.taihuoniao.fineix.common.GlobalDataCallBack;
import com.taihuoniao.fineix.common.bean.BannerBean;
import com.taihuoniao.fineix.home.GoToNextUtils;
import com.taihuoniao.fineix.main.MainActivity;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.product.BannerActivity;
import com.taihuoniao.fineix.product.BuyGoodsDetailsActivity;
import com.taihuoniao.fineix.user.UserGuideActivity;
import com.taihuoniao.fineix.utils.GlideUtils;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.SPUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.view.InputCodeDialog;
import com.taihuoniao.fineix.view.dialog.WaittingDialog;

import java.util.HashMap;
import java.util.List;

public class ViewPagerAdapter<T> extends RecyclingPagerAdapter {
    private final String TAG = getClass().getSimpleName();
    private Activity activity;
    private List<T> list;
    private int size;
    private boolean isInfiniteLoop;
    private String code;
    private WaittingDialog dialog;

    public int getSize() {
        return size;
    }

    public ViewPagerAdapter(final Activity activity, List<T> list) {
        this.activity = activity;
        this.list = list;
        this.size = (list == null ? 0 : list.size());
        isInfiniteLoop = false;
        if (activity instanceof UserGuideActivity) {
            dialog = new WaittingDialog(activity);
        }
    }

    @Override
    public int getCount() {
        if (size == 0) {
            return 0;
        }
        return isInfiniteLoop ? Integer.MAX_VALUE : size;
    }

    /**
     * get really position
     *
     * @param position
     * @return
     */
    private int getPosition(int position) {
        return isInfiniteLoop ? position % size : position;
    }

    @Override
    public View getView(int position, View view, ViewGroup container) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = holder.imageView = new ImageView(activity);
            holder.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            view.setTag(R.id.glide_image_tag, holder);
        } else {
            holder = (ViewHolder) view.getTag(R.id.glide_image_tag);
        }
        final T content = list.get(getPosition(position));

        if (content instanceof BannerBean.RowsBean) {
            GlideUtils.displayImage(((BannerBean.RowsBean) content).cover_url, holder.imageView);
        }

        if (content instanceof Integer) {
            holder.imageView.setImageResource((Integer) content);
        }

        if (content instanceof String) {
            if (TextUtils.isEmpty((String) content)) {
                ToastUtils.showError("图片链接为空");
            } else {
                GlideUtils.displayImage(content, holder.imageView);
            }
        }


        if (activity instanceof UserGuideActivity) {
            if (position == size - 1) {
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (TextUtils.isEmpty(UserGuideActivity.fromPage)) {
                            activity.startActivity(new Intent(activity, MainActivity.class));
                            activity.finish();
//                            isNeedCode();
                        } else {
                            UserGuideActivity.fromPage = null;
                            activity.finish();
                        }
                    }
                });
            }
        }

        if (activity instanceof MainActivity) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final BannerBean.RowsBean banner = (BannerBean.RowsBean) content;
                    GoToNextUtils.goToIntent(activity, Integer.valueOf(banner.type), banner.web_url);

                }
            });
        }
        if (activity instanceof BuyGoodsDetailsActivity) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainApplication.picList = (List<String>) list;
                    Intent intent = new Intent(activity, BannerActivity.class);
                    activity.startActivity(intent);
                }
            });
        }

        return view;
    }


    /**
     * 提交邀请码
     *
     * @param et
     */
    private void submitCode(final EditText et) {
        code = et.getText().toString().trim();
        if (TextUtils.isEmpty(code)) {
            ToastUtils.showError("请输入邀请码");
            return;
        }
        HashMap<String, String> params = ClientDiscoverAPI.getsubmitInviteCodeRequestParams(code);
        HttpRequest.post(params, URL.GATEWAY_VALIDE_INVITE_CODE , new GlobalDataCallBack(){
            @Override
            public void onSuccess(String json) {
                if (TextUtils.isEmpty(json)) {
                    return;
                }
                HttpResponse response = JsonUtil.fromJson(json, HttpResponse.class);
                if (response.isSuccess()) {
                    updateInviteCodeStatus();
                    return;
                }
                ToastUtils.showError(response.getMessage());
            }

            @Override
            public void onFailure(String error) {
                ToastUtils.showError("网络异常，请确保网络畅通");
            }
        });
    }

    private void updateInviteCodeStatus() {
        HashMap<String, String> params = ClientDiscoverAPI.getupdateInviteCodeStatusRequestParams(code);
        HttpRequest.post(params, URL.GATEWAY_DEL_INVITE_CODE , new GlobalDataCallBack(){
            @Override
            public void onSuccess(String json) {
                if (TextUtils.isEmpty(json)) {
                    return;
                }
                HttpResponse response = JsonUtil.fromJson(json, HttpResponse.class);
                if (response.isSuccess()) {
                    SPUtil.write(DataConstants.INVITE_CODE_TAG, false);
                    activity.startActivity(new Intent(activity, MainActivity.class));
                    activity.finish();
                    return;
                }
                ToastUtils.showError(response.getMessage());
            }

            @Override
            public void onFailure(String error) {
                ToastUtils.showError("网络异常，请确保网络畅通");
            }
        });
    }

    /**
     * 判断是否需要输入邀请码
     */
    private void isNeedCode() {
        if (!dialog.isShowing()) {
            dialog.show();
        }
        HashMap<String, String> params = ClientDiscoverAPI.getDefaultParams();
        HttpRequest.post(params,URL.GATEWAY_IS_INVITED, new GlobalDataCallBack(){
            @Override
            public void onSuccess(String json) {
                dialog.dismiss();
                if (TextUtils.isEmpty(json)) {
                    return;
                }
                HttpResponse<IsInviteData> response = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<IsInviteData>>() {
                });
                if (response.isSuccess()) {
                    if (response.getData().status == 1) {
                        SPUtil.write(DataConstants.INVITE_CODE_TAG, true);
                        InputCodeDialog dialog = new InputCodeDialog();
                        dialog.setCancelable(false);
                        dialog.setOnCommitClickListener(new InputCodeDialog.OnCommitClickListener() {
                            @Override
                            public void execute(View v, EditText et) {
                                submitCode(et);
                            }
                        });
                        dialog.show(activity.getFragmentManager(), "InputCodeDialog");
                    } else {
                        activity.startActivity(new Intent(activity, MainActivity.class));
                        activity.finish();
                    }
                    return;
                }
                ToastUtils.showError(response.getMessage());
            }

            @Override
            public void onFailure(String error) {
                dialog.dismiss();
                ToastUtils.showError("网络异常，请确保网络畅通");
            }
        });
    }

    private static class ViewHolder {
        ImageView imageView;
    }

    /**
     * @return the isInfiniteLoop
     */
    public boolean isInfiniteLoop() {
        return isInfiniteLoop;
    }

    /**
     * @param isInfiniteLoop the isInfiniteLoop to set
     */
    public ViewPagerAdapter setInfiniteLoop(boolean isInfiniteLoop) {
        this.isInfiniteLoop = isInfiniteLoop;
        return this;
    }
}