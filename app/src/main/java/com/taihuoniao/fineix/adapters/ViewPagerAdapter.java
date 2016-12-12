package com.taihuoniao.fineix.adapters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.Banner;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.beans.IsInviteData;
import com.taihuoniao.fineix.beans.SubjectData;
import com.taihuoniao.fineix.main.MainActivity;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.product.BannerActivity;
import com.taihuoniao.fineix.product.BuyGoodsDetailsActivity;
import com.taihuoniao.fineix.qingjingOrSceneDetails.QJDetailActivity;
import com.taihuoniao.fineix.user.ActivityDetailActivity;
import com.taihuoniao.fineix.user.ArticalDetailActivity;
import com.taihuoniao.fineix.user.NewProductDetailActivity;
import com.taihuoniao.fineix.user.SalePromotionDetailActivity;
import com.taihuoniao.fineix.user.SubjectActivity;
import com.taihuoniao.fineix.user.UserGuideActivity;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.SPUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.view.InputCodeDialog;
import com.taihuoniao.fineix.view.dialog.WaittingDialog;

import java.util.List;

public class ViewPagerAdapter<T> extends RecyclingPagerAdapter {
    private final String TAG = getClass().getSimpleName();
    private Activity activity;
    private List<T> list;
    protected DisplayImageOptions options;
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
        this.size = list.size();
        isInfiniteLoop = false;
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.default_background_750_1334)
                .showImageForEmptyUri(R.mipmap.default_background_750_1334)
                .showImageOnFail(R.mipmap.default_background_750_1334)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        if (activity instanceof UserGuideActivity) {
            dialog = new WaittingDialog(activity);
        }
    }

    @Override
    public int getCount() {
        // Infinite loop
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
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        final T content = list.get(getPosition(position));

        if (content instanceof Banner) {
            ImageLoader.getInstance().displayImage(((Banner) content).cover_url, holder.imageView, options);
        }

        if (content instanceof Integer) {
            holder.imageView.setImageResource((Integer) content);
//            ImageLoader.getInstance().displayImage("drawable://"+(Integer) content,holder.imageView,options);
        }

        if (content instanceof String) {
            if (TextUtils.isEmpty((String) content)) {
                ToastUtils.showError("图片链接为空");
            } else {
                ImageLoader.getInstance().displayImage((String) content, holder.imageView, options);
            }
        }


        if (activity instanceof UserGuideActivity) {
            if (position == size - 1) {
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (TextUtils.isEmpty(UserGuideActivity.fromPage)) {
//                            activity.startActivity(new Intent(activity, MainActivity.class));
//                            activity.finish();
                            isNeedCode();
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
                    final Banner banner = (Banner) content;
                    Intent intent;
                    Log.e("<<<", "banner.type=" + banner.type + ",web_url=" + banner.web_url + ",title=" + banner.title);
                    switch (banner.type) {
                        case 1:      //url地址
                            Uri uri = Uri.parse(banner.web_url);
                            intent = new Intent(Intent.ACTION_VIEW, uri);
                            activity.startActivity(intent);
                            break;
                        case 2://商品
                            intent = new Intent(activity, BuyGoodsDetailsActivity.class);
                            intent.putExtra("id", banner.web_url);
                            activity.startActivity(intent);
                            break;
                        case 4://app专题
                            intent = new Intent(activity, SubjectActivity.class);
                            intent.putExtra(SubjectActivity.class.getSimpleName(), banner.web_url);
                            intent.putExtra(SubjectActivity.class.getName(), banner.title);
                            activity.startActivity(intent);
                            break;
                        case 8:     //情境
                            intent = new Intent(activity, QJDetailActivity.class);
                            intent.putExtra("id", banner.web_url);
                            activity.startActivity(intent);
                            break;
                        case 9:     //产品
                            intent = new Intent(activity, BuyGoodsDetailsActivity.class);
                            intent.putExtra("id", banner.web_url);
                            activity.startActivity(intent);
                            break;
                        case 11:    //情境专题
                            Log.e("<<<", "banner.toString=" + banner.toString());
                            ClientDiscoverAPI.getSubjectData(banner.web_url, new RequestCallBack<String>() {
                                @Override
                                public void onSuccess(ResponseInfo<String> responseInfo) {
                                    HttpResponse<SubjectData> response = JsonUtil.json2Bean(responseInfo.result, new TypeToken<HttpResponse<SubjectData>>() {
                                    });

                                    if (response.isSuccess()) {
                                        SubjectData data = response.getData();
                                        Intent intent;
                                        switch (data.type) {
                                            case 1: //文章详情
                                                intent = new Intent(activity, ArticalDetailActivity.class);
                                                intent.putExtra(ArticalDetailActivity.class.getSimpleName(), banner.web_url);
                                                activity.startActivity(intent);
                                                break;
                                            case 2: //活动详情
                                                intent = new Intent(activity, ActivityDetailActivity.class);
                                                intent.putExtra(ActivityDetailActivity.class.getSimpleName(), banner.web_url);
                                                activity.startActivity(intent);
                                                break;
                                            case 4: //新品
                                                intent = new Intent(activity, NewProductDetailActivity.class);
                                                intent.putExtra(NewProductDetailActivity.class.getSimpleName(), banner.web_url);
                                                activity.startActivity(intent);
                                                break;
                                            case 3: //促销
                                                intent = new Intent(activity, SalePromotionDetailActivity.class);
                                                intent.putExtra(SalePromotionDetailActivity.class.getSimpleName(), banner.web_url);
                                                activity.startActivity(intent);
                                                break;
                                        }
                                        return;
                                    }
                                    ToastUtils.showError(response.getMessage());
                                }

                                @Override
                                public void onFailure(HttpException e, String s) {
                                    ToastUtils.showError(R.string.network_err);
                                }
                            });
                            break;
                    }

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
        ClientDiscoverAPI.submitInviteCode(code, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (TextUtils.isEmpty(responseInfo.result)) {
                    return;
                }
                HttpResponse response = JsonUtil.fromJson(responseInfo.result, HttpResponse.class);
                if (response.isSuccess()) {
                    updateInviteCodeStatus();
                    return;
                }
                ToastUtils.showError(response.getMessage());
            }

            @Override
            public void onFailure(HttpException e, String s) {
                ToastUtils.showError("网络异常，请确保网络畅通");
            }
        });
    }

    private void updateInviteCodeStatus() {
        ClientDiscoverAPI.updateInviteCodeStatus(code, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (TextUtils.isEmpty(responseInfo.result)) {
                    return;
                }
                HttpResponse response = JsonUtil.fromJson(responseInfo.result, HttpResponse.class);
                if (response.isSuccess()) {
                    SPUtil.write(DataConstants.INVITE_CODE_TAG, false);
                    activity.startActivity(new Intent(activity, MainActivity.class));
                    activity.finish();
                    return;
                }
                ToastUtils.showError(response.getMessage());
            }

            @Override
            public void onFailure(HttpException e, String s) {
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
        ClientDiscoverAPI.isInvited(new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                dialog.dismiss();
//                ToastUtils.showError(responseInfo.result);
                if (TextUtils.isEmpty(responseInfo.result)) {
                    return;
                }
                HttpResponse<IsInviteData> response = JsonUtil.json2Bean(responseInfo.result, new TypeToken<HttpResponse<IsInviteData>>() {
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
            public void onFailure(HttpException e, String s) {
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