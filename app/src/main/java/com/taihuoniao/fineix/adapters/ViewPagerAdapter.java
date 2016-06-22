package com.taihuoniao.fineix.adapters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
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
import com.taihuoniao.fineix.beans.IsInviteData;
import com.taihuoniao.fineix.main.MainActivity;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.HttpResponse;
import com.taihuoniao.fineix.product.GoodsDetailActivity;
import com.taihuoniao.fineix.qingjingOrSceneDetails.QingjingDetailActivity;
import com.taihuoniao.fineix.qingjingOrSceneDetails.SceneDetailActivity;
import com.taihuoniao.fineix.user.SubjectActivity;
import com.taihuoniao.fineix.user.UserGuideActivity;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.view.InputCodeDialog;

import java.util.List;

public class ViewPagerAdapter<T> extends RecyclingPagerAdapter {
    private final String TAG = getClass().getSimpleName();
    private Activity activity;
    private List<T> list;
    protected DisplayImageOptions options;
    private int size;
    private boolean isInfiniteLoop;
    private boolean isNeed = false;
    private boolean isInviteFinish = false;
    public int getSize() {
        return size;
    }

    public ViewPagerAdapter(Activity activity, List<T> list) {
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
    }

    @Override
    public int getCount() {
        // Infinite loop
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
                isNeedCode();
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (TextUtils.isEmpty(UserGuideActivity.fromPage)) {
                            if (isInviteFinish) {
                                activity.startActivity(new Intent(activity, MainActivity.class));
                                activity.finish();
                            }
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
                    Banner banner = (Banner) content;
                    Intent intent;
                    switch (banner.type) {
                        case 8:     //场景详情
                            intent = new Intent(activity, SceneDetailActivity.class);
                            intent.putExtra("id", banner.web_url);
                            activity.startActivity(intent);
                            break;
                        case 9:     //产品
                            intent = new Intent(activity, GoodsDetailActivity.class);
                            intent.putExtra("id", banner.web_url);
                            activity.startActivity(intent);
                            break;
                        case 10:    //情景
                            intent = new Intent(activity, QingjingDetailActivity.class);
                            intent.putExtra("id", banner.web_url);
                            activity.startActivity(intent);
                        case 11:    //专题
                            intent = new Intent(activity, SubjectActivity.class);
                            intent.putExtra(SubjectActivity.class.getSimpleName(), banner.web_url);
                            intent.putExtra(SubjectActivity.class.getName(), banner.title);
                            activity.startActivity(intent);
                            break;
                    }

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
    private void submitCode(EditText et) {
        ClientDiscoverAPI.submitInviteCode(et.getText().toString().trim(), new RequestCallBack<String>() {
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
        ClientDiscoverAPI.updateInviteCodeStatus(new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (TextUtils.isEmpty(responseInfo.result)) {
                    return;
                }
                HttpResponse response = JsonUtil.fromJson(responseInfo.result, HttpResponse.class);
                if (response.isSuccess()) {
                    isInviteFinish = true;
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
        ClientDiscoverAPI.isInvited(new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (TextUtils.isEmpty(responseInfo.result)) {
                    return;
                }
                HttpResponse<IsInviteData> response = JsonUtil.json2Bean(responseInfo.result, new TypeToken<HttpResponse<IsInviteData>>() {
                });
                if (response.isSuccess()) {
                    if (response.getData().status == 1) {
                        InputCodeDialog dialog = new InputCodeDialog();
                        dialog.setCancelable(false);
                        dialog.setOnCommitClickListener(new InputCodeDialog.OnCommitClickListener() {
                            @Override
                            public void execute(View v, EditText et) {
                                submitCode(et);
                            }
                        });
                        dialog.show(activity.getFragmentManager(), "InputCodeDialog");
                    }
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