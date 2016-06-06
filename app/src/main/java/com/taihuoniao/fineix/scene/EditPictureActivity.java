package com.taihuoniao.fineix.scene;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.EditRecyclerAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.base.NetBean;
import com.taihuoniao.fineix.beans.GoodsDetailBean;
import com.taihuoniao.fineix.beans.TagItem;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.utils.EffectUtil;
import com.taihuoniao.fineix.utils.FirstInAppUtils;
import com.taihuoniao.fineix.utils.GPUImageFilterTools;
import com.taihuoniao.fineix.utils.ImageUtils;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.view.GlobalTitleLayout;
import com.taihuoniao.fineix.view.LabelView;
import com.taihuoniao.fineix.view.MyHighlightView;
import com.taihuoniao.fineix.view.MyImageViewTouch;
import com.taihuoniao.fineix.view.WaittingDialog;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageView;

/**
 * Created by taihuoniao on 2016/3/17.
 */
public class EditPictureActivity extends BaseActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    //上个界面传递过来的数据
    private Uri imageUri;
    //界面下的控件
    private View activity_view;
    private GlobalTitleLayout titleLayout;
    private RelativeLayout gpuRelative;
    private GPUImageView gpuImageView;
    private LinearLayout recyclerLinear;
    private RecyclerView recyclerView;
    private SeekBar seekBar;
    private Button backBtn;
    //    private GPUImageFilterTools.FilterList filterList;
    private RelativeLayout productsRelative;
    private TextView productsTv;
    private TextView productsRedline;
    private RelativeLayout chainingRelative;
    private TextView chainingTv;
    private TextView chainingRedline;
    private RelativeLayout filterRelative;
    private TextView filterTv;
    private TextView filterRedline;
    //popupwindow下的控件
    private PopupWindow popupWindow;
    private TextView cancelTv;
    private TextView confirmTv;
    private ImageView productImg;
    private EditText name;
    private EditText price;
    private TextView nameTv;
    private TextView priceTv;
    //    private Button editBtn;
    private Button deleteBtn;
    //当前点击的labelview
    private LabelView labelView;
    //当前filter
//    private GPUImageFilter currentFilter;
    //标记产品装载图片的容器
    private MyImageViewTouch mImageView;
    //添加链接装载链接的容器
    private List<LabelView> labels = new ArrayList<LabelView>();
    //工具类
    private WaittingDialog dialog;
    //编辑好的图片存储名称
    private String picName;
    public static EditPictureActivity instance = null;
    private ImageLoader imageLoader;
    //图片加载完毕之后的宽高
    private int picWidth, picHeight;
    private DisplayImageOptions options500_500, options750_1334;

    public EditPictureActivity() {
        super(0);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (CropPictureActivity.instance != null) {
            CropPictureActivity.instance.finish();
        }
    }

    @Override
    protected void requestNet() {

    }

    @Override
    protected void initList() {
        imageUri = getIntent().getData();
        titleLayout.setTitle(R.string.tools);
        titleLayout.setBackgroundResource(R.color.black_touming);
        titleLayout.setContinueListener(this);
        EffectUtil.clear();
        ImageLoader.getInstance().loadImage(imageUri.toString(), options750_1334, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
//                dialog.showErrorWithStatus("图片加载失败，请返回重试");
                ToastUtils.showError("图片加载失败，请重试");
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                gpuImageView.setImage(loadedImage);
                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) gpuImageView.getLayoutParams();
                if (gpuImageView.getWidth() * 16 > 9 * gpuImageView.getHeight()) {
                    int containerHeight = gpuRelative.getHeight();
                    int systemHeight = MainApplication.getContext().getScreenHeight() - getNavigationBarHeight();
                    lp.height = containerHeight > 0 ? containerHeight : systemHeight;
                    lp.width = lp.height * 9 / 16;
                } else {
                    int containerWidth = gpuRelative.getWidth();
                    int systemWidth = MainApplication.getContext().getScreenWidth();
                    lp.width = containerWidth > 0 ? containerWidth : systemWidth;
                    lp.height = lp.width * 16 / 9;
                }
//                lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
                picWidth = lp.width;
                picHeight = lp.height;
                gpuImageView.setLayoutParams(lp);
                initEditView();
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                ToastUtils.showError("图片加载失败，请重试");
//                dialog.showErrorWithStatus("图片加载失败，请返回重试");
            }
        });
//        ImageUtils.asyncLoadImage(EditPictureActivity.this, imageUri, new ImageUtils.LoadImageCallback() {
//            @Override
//            public void callback(Bitmap result) {
//                gpuImageView.setImage(result);
//                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) gpuImageView.getLayoutParams();
//                if (gpuImageView.getWidth() * 16 > 9 * gpuImageView.getHeight()) {
//                    int containerHeight = gpuRelative.getHeight();
//                    int systemHeight = MainApplication.getContext().getScreenHeight() - getNavigationBarHeight();
//                    lp.height = containerHeight > 0 ? containerHeight : systemHeight;
//                    lp.width = lp.height * 9 / 16;
//                } else {
//                    int containerWidth = gpuRelative.getWidth();
//                    int systemWidth = MainApplication.getContext().getScreenWidth();
//                    lp.width = containerWidth > 0 ? containerWidth : systemWidth;
//                    lp.height = lp.width * 16 / 9;
//                }
////                lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
//                picWidth = lp.width;
//                picHeight = lp.height;
//                gpuImageView.setLayoutParams(lp);
//                initEditView();
//
//            }
//        });
        //设置布局大小一样
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        EditRecyclerAdapter recyclerAdapter = new EditRecyclerAdapter(EditPictureActivity.this, new GPUImageFilterTools.OnGpuImageFilterChosenListener() {
            @Override
            public void onGpuImageFilterChosenListener(GPUImageFilter filter, int position) {
//                currentFilter = filter;
                gpuImageView.setFilter(filter);
                GPUImageFilterTools.FilterAdjuster filterAdjuster = new GPUImageFilterTools.FilterAdjuster(filter);
                switch (position) {
                    case 1:
                        filterAdjuster.adjust(97);
                        break;
                    case 2:
                        filterAdjuster.adjust(45);
                        break;
                    case 3:
                        filterAdjuster.adjust(0);
                        break;
                    case 5:
                        filterAdjuster.adjust(40);
                        break;
                    case 6:
                        filterAdjuster.adjust(100);
                        break;
                    case 7:
                        filterAdjuster.adjust(25);
                        break;
                    case 9:
                        filterAdjuster.adjust(55);
                        break;
                    case 10:
                        filterAdjuster.adjust(53);
                        break;
                    case 11:
                        filterAdjuster.adjust(60);
                        break;
                }
                gpuImageView.requestRender();
            }
        }, new EditRecyclerAdapter.ItemClick() {
            @Override
            public void click(int postion) {
                recyclerView.setVisibility(View.GONE);
                seekBar.setVisibility(View.VISIBLE);
                backBtn.setVisibility(View.VISIBLE);
            }
        });
        recyclerView.setAdapter(recyclerAdapter);
        seekBar.setOnSeekBarChangeListener(this);
        backBtn.setOnClickListener(this);
        productsRelative.setOnClickListener(this);
        chainingRelative.setOnClickListener(this);
        filterRelative.setOnClickListener(this);
    }

    private int getNavigationBarHeight() {
        int height = 0;
        Resources resources = getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            height = resources.getDimensionPixelSize(resourceId);
        }
//        Log.e("<<<", "工具栏 height:" + height);
        return height;
    }

    @Override
    protected void initView() {
        activity_view = View.inflate(EditPictureActivity.this, R.layout.activity_edit_picture, null);
        setContentView(activity_view);
        instance = EditPictureActivity.this;
        titleLayout = (GlobalTitleLayout) findViewById(R.id.activity_edit_titlelayout);
        gpuRelative = (RelativeLayout) findViewById(R.id.activity_edit_main_area);
        gpuImageView = (GPUImageView) findViewById(R.id.activity_edit_gpuimage);
        recyclerLinear = (LinearLayout) findViewById(R.id.activity_edit_recycler_linear);
        recyclerView = (RecyclerView) findViewById(R.id.activity_edit_recycler);
        seekBar = (SeekBar) findViewById(R.id.activity_edit_progress);
        backBtn = (Button) findViewById(R.id.activity_edit_back);
        productsRelative = (RelativeLayout) findViewById(R.id.activity_edit_products_relative);
        chainingRelative = (RelativeLayout) findViewById(R.id.activity_edit_chaining_relative);
        filterRelative = (RelativeLayout) findViewById(R.id.activity_edit_filter_relative);
        productsTv = (TextView) findViewById(R.id.activity_edit_products_tv);
        chainingTv = (TextView) findViewById(R.id.activity_edit_chaining_tv);
        filterTv = (TextView) findViewById(R.id.activity_edit_filter_tv);
        productsRedline = (TextView) findViewById(R.id.activity_edit_products_redline);
        chainingRedline = (TextView) findViewById(R.id.activity_edit_chaining_redline);
        filterRedline = (TextView) findViewById(R.id.activity_edit_filter_redline);
        dialog = new WaittingDialog(EditPictureActivity.this);
        imageLoader = ImageLoader.getInstance();
        options500_500 = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.default_background_500_500)
                .showImageForEmptyUri(R.mipmap.default_background_500_500)
                .showImageOnFail(R.mipmap.default_background_500_500)
                .cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true)
                .build();
        options750_1334 = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.default_background_750_1334)
                .showImageForEmptyUri(R.mipmap.default_background_750_1334)
                .showImageOnFail(R.mipmap.default_background_750_1334)
                .cacheInMemory(false)
                .cacheOnDisk(false).considerExifParams(true)
                .build();
        initPopupWindow();
        filterRelative.setVisibility(View.GONE);
    }

    private void initPopupWindow() {
        WindowManager windowManager = EditPictureActivity.this.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        View popup_view = View.inflate(EditPictureActivity.this, R.layout.pop_edit, null);
        cancelTv = (TextView) popup_view.findViewById(R.id.pop_edit_cancel);
        confirmTv = (TextView) popup_view.findViewById(R.id.pop_edit_confirm);
        productImg = (ImageView) popup_view.findViewById(R.id.pop_edit_img);
        name = (EditText) popup_view.findViewById(R.id.pop_edit_name);
        price = (EditText) popup_view.findViewById(R.id.pop_edit_price);
        nameTv = (TextView) popup_view.findViewById(R.id.pop_edit_name_tv);
        priceTv = (TextView) popup_view.findViewById(R.id.pop_edit_price_tv);
//        editBtn = (Button) popup_view.findViewById(R.id.pop_edit_edit);
        deleteBtn = (Button) popup_view.findViewById(R.id.pop_edit_delete);
        popupWindow = new PopupWindow(popup_view, display.getWidth(), LinearLayout.LayoutParams.WRAP_CONTENT, true);
        // 设置动画效果
        popupWindow.setAnimationStyle(R.style.popupwindow_style);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        cancelTv.setOnClickListener(this);
        confirmTv.setOnClickListener(this);
//        editBtn.setOnClickListener(this);
        deleteBtn.setOnClickListener(this);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams params = EditPictureActivity.this.getWindow().getAttributes();
                params.alpha = 1f;
//                getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                getWindow().setAttributes(params);
            }
        });
        popupWindow.setBackgroundDrawable(ContextCompat.getDrawable(EditPictureActivity.this,
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
        WindowManager.LayoutParams params = EditPictureActivity.this.getWindow().getAttributes();
        params.alpha = 0.1f;
        getWindow().setAttributes(params);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        popupWindow.showAtLocation(activity_view, Gravity.BOTTOM, 0, 0);
    }

    private void initEditView() {
        //初始化画布
        View overView = View.inflate(EditPictureActivity.this,
                R.layout.view_over, null);
        mImageView = (MyImageViewTouch) overView.findViewById(R.id.view_over_mimg);
        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(picHeight * 9 / 16, picHeight);
        p.addRule(RelativeLayout.CENTER_HORIZONTAL);
        overView.setLayoutParams(p);
        mImageView.setLayoutParams(p);
        gpuRelative.addView(overView);
        mImageView.setOnDrawableEventListener(new MyImageViewTouch.OnDrawableEventListener() {
            @Override
            public void onFocusChange(MyHighlightView newFocus, MyHighlightView oldFocus) {

            }

            @Override
            public void onDown(MyHighlightView view) {

            }

            @Override
            public void onMove(MyHighlightView view) {
            }

            @Override
            public void onClick(MyHighlightView view) {

            }

            @Override
            public void onClick(final LabelView label) {
                labelView = label;
                TagItem tagItem = label.getTagInfo();
                if (tagItem.getType() == 1) {
                    return;
                }
                ImageLoader.getInstance().displayImage(tagItem.getImagePath(), productImg, options500_500);
                name.setText(tagItem.getName());
                nameTv.setText(tagItem.getName());
                price.setText(tagItem.getPrice());
                priceTv.setText(tagItem.getPrice());
                showPopup();
            }
        });
//        mImageView.setSingleTapListener(new ImageViewTouch.OnImageViewTouchSingleTapListener() {
//            @Override
//            public void onSingleTapConfirmed() {
//                //添加标签？
//            }
//        });
    }

    //添加标签
    private void addLabel(TagItem tagItem) {
        if (labels.size() >= 3) {
            ToastUtils.showInfo("您最多可以添加三个链接");
//            dialog.showInfoWithStatus("您最多可以添加三个链接");
//            Toast.makeText(EditPictureActivity.this, R.string.more_three_chaining, Toast.LENGTH_SHORT).show();
            return;
        }
        //链接的默认位置
        int left = mImageView.getWidth() / 4;
        int top = mImageView.getHeight() / 2;
        if (left <= 0 || top <= 0) {
            left = 0;
            top = 0;
        }
        final LabelView label = new LabelView(EditPictureActivity.this);
        label.init(tagItem);
        EffectUtil.addLabelEditable(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                labelView = label;
                EditPictureActivity.this.onClick(deleteBtn);
            }
        }, mImageView, gpuRelative, label, left, top);
        labels.add(label);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_continue:
                dialog.show();
                savePicture();
                break;
            case R.id.pop_edit_delete:
                if (labelView.getTagInfo().getType() == 1) {
                    EffectUtil.removeLabelEditable(mImageView, gpuRelative, labelView);
                    labels.remove(labelView);
//                    popupWindow.dismiss();
                    return;
                }
                dialog.show();
                ClientDiscoverAPI.deleteProduct(labelView.getTagInfo().getId(), new RequestCallBack<String>() {
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
                            Log.e("<<<", "数据解析异常" + e.toString());
                        }
                        if (netBean.isSuccess()) {
                            EffectUtil.removeLabelEditable(mImageView, gpuRelative, labelView);
                            labels.remove(labelView);
                            popupWindow.dismiss();
                        } else {
                            ToastUtils.showError(netBean.getMessage());
//                            dialog.showErrorWithStatus(netBean.getMessage());
//                            Toast.makeText(EditPictureActivity.this, netBean.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        dialog.dismiss();
                        ToastUtils.showError("删除失败，请重试");
//                        dialog.showErrorWithStatus("删除失败");
//                        Toast.makeText(EditPictureActivity.this, "删除失败", Toast.LENGTH_SHORT).show();
                    }
                });

                break;
//            case R.id.pop_edit_edit:
//                startActivityForResult(new Intent(EditPictureActivity.this, SelectStoreActivity.class), DataConstants.REQUESTCODE_EDITEDIT_SEARCHSTORE);
//                break;
            case R.id.pop_edit_confirm:
                TagItem tagItem = labelView.getTagInfo();
                tagItem.setName(name.getText().toString());
                tagItem.setPrice(price.getText().toString());
                labelView.init(tagItem);
                popupWindow.dismiss();
                break;
            case R.id.pop_edit_cancel:
                popupWindow.dismiss();
                break;
            case R.id.activity_edit_back:
                recyclerView.setVisibility(View.VISIBLE);
                seekBar.setVisibility(View.GONE);
                backBtn.setVisibility(View.GONE);
                break;
            case R.id.activity_edit_products_relative:
                if (labels.size() >= 3) {
                    ToastUtils.showInfo("您最多可以添加三个产品");
//                    dialog.showInfoWithStatus("您最多可以添加三个产品");
//                    Toast.makeText(EditPictureActivity.this, R.string.more_three_products, Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(EditPictureActivity.this, AddProductActivity.class);
                startActivityForResult(intent, DataConstants.REQUESTCODE_EDIT_ADDPRODUCT);
                break;
            case R.id.activity_edit_chaining_relative:
                startActivityForResult(new Intent(EditPictureActivity.this, SelectStoreActivity.class), DataConstants.REQUESTCODE_EDIT_SEARCHSTORE);
                break;
            case R.id.activity_edit_filter_relative:
                if (recyclerLinear.getVisibility() == View.GONE) {
                    recyclerLinear.setVisibility(View.VISIBLE);
                    if (MainApplication.tag == 2) {
                        return;
                    }
                    recyclerLinear.setVisibility(View.VISIBLE);
                    filterTv.setTextColor(getResources().getColor(R.color.yellow_bd8913));
                    filterRedline.setVisibility(View.VISIBLE);
                } else {
                    filterTv.setTextColor(getResources().getColor(R.color.white));
                    filterRedline.setVisibility(View.GONE);
                    recyclerLinear.setVisibility(View.GONE);
                }
                break;
        }
    }

    //保存图片

    private void savePicture() {
        if (mImageView.getWidth() <= 0 || mImageView.getHeight() <= 0) {
            return;
        }
        //加滤镜
        final Bitmap newBitmap = Bitmap.createBitmap(mImageView.getWidth(), mImageView.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas cv = new Canvas(newBitmap);
        RectF dst = new RectF(0, 0, mImageView.getWidth(), mImageView.getHeight());
        try {
            cv.drawBitmap(gpuImageView.capture(), null, dst, null);
        } catch (InterruptedException e) {
            e.printStackTrace();
            ToastUtils.showError("图片处理异常，请重试");
//            dialog.showErrorWithStatus("图片处理异常，请重试");
//            Toast.makeText(EditPictureActivity.this, "图片处理异常，请重试", Toast.LENGTH_SHORT).show();
            return;
            //出现异常存储的是未加滤镜效果的图片
//            cv.drawBitmap(currentBitmap, null, dst, null);
        }
        //加商品
        EffectUtil.applyOnSave(cv, mImageView);
        new SavePicToFileTask().execute(newBitmap);
    }

    private class SavePicToFileTask extends AsyncTask<Bitmap, Void, String> {
        Bitmap bitmap;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(Bitmap... params) {
            String fileName = null;
            try {
                bitmap = params[0];
//                picName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                fileName = ImageUtils.saveToFile(MainApplication.editPicPath, false, bitmap);
            } catch (Exception e) {
                e.printStackTrace();
//                Toast.makeText(EditPictureActivity.this, "图片存储错误，请重试", Toast.LENGTH_SHORT).show();
            }
            return fileName;
        }

        @Override
        protected void onPostExecute(String fileName) {
            super.onPostExecute(fileName);
            dialog.dismiss();
            if (TextUtils.isEmpty(fileName)) {
                //出现问题是因为缓存目录中产生了与规定文件名称一样的文件夹，清理即可以使用
//                dialog.showErrorWithStatus("图片处理错误，请清理缓存后重试");
                ToastUtils.showError("图片处理错误，请清理缓存后重试");
//                Toast.makeText(EditPictureActivity.this, "图片处理错误，请清理缓存后重试", Toast.LENGTH_SHORT).show();
                return;
            }
            //保存标签信息
            List<TagItem> tagInfoList = new ArrayList<TagItem>();
            for (LabelView label : labels) {
                tagInfoList.add(label.getTagInfo());
            }
            MainApplication.tagInfoList = tagInfoList;
            //向图片中存储位置信息
//            ImageUtils.writeLocation(ImageUtils.picLocation(imageUri.getPath()), fileName);
            //传递数据
            Intent intent = new Intent(EditPictureActivity.this, FilterActivity.class);
            intent.setData(Uri.parse("file://" + fileName));
            startActivity(intent);
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            switch (resultCode) {
                case DataConstants.RESULTCODE_EDIT_SEARCHSTORE:
                    TagItem tagItem = (TagItem) data.getSerializableExtra("tagItem");
                    if (requestCode == DataConstants.REQUESTCODE_EDIT_SEARCHSTORE) {
                        addLabel(tagItem);
                        SharedPreferences firstInSp = getSharedPreferences(DataConstants.SHAREDPREFRENCES_FIRST_IN, Context.MODE_PRIVATE);
                        //判断是不是第一次进入Fiu界面
                        boolean isFirstIn = firstInSp.getBoolean(DataConstants.FIRST_IN_URL, true);
                        if (isFirstIn) {
                            FirstInAppUtils.showPop(EditPictureActivity.this, FirstInAppUtils.ADDURL, activity_view);
                            SharedPreferences.Editor editor = firstInSp.edit();
                            editor.putBoolean(DataConstants.FIRST_IN_URL, false);
                            editor.apply();
                        }
                    } else {
                        ImageLoader.getInstance().displayImage(tagItem.getImagePath(), productImg, options500_500);
                        name.setText(tagItem.getName());
                        nameTv.setText(tagItem.getName());
                        price.setText(tagItem.getPrice());
                        priceTv.setText(tagItem.getPrice());
                    }
                    break;
                case DataConstants.RESULTCODE_EDIT_ADDPRODUCT:
                    final GoodsDetailBean productListBean = (GoodsDetailBean) data.getSerializableExtra("product");
                    String url = productListBean.getData().getPng_asset().get(0).getUrl();

                    imageLoader.loadImage(url, new ImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String imageUri, View view) {
                            dialog.show();
                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                            dialog.dismiss();
                            ToastUtils.showError("图片加载失败");
//                            dialog.showErrorWithStatus("图片加载失败");
//                            Toast.makeText(EditPictureActivity.this, R.string.failed_loading, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            dialog.dismiss();
                            //是自动添加标签还是后添加
                            TagItem tag = new TagItem(productListBean.getData().getTitle(), productListBean.getData().getSale_price());
                            tag.setId(productListBean.getData().get_id());
                            tag.setImagePath(productListBean.getData().getPng_asset().get(0).getUrl());
                            tag.setType(1);
                            addLabel(tag);
                            EffectUtil.addStickerImage(mImageView, EditPictureActivity.this, loadedImage);
                        }

                        @Override
                        public void onLoadingCancelled(String imageUri, View view) {
                            dialog.dismiss();
//                            Toast.makeText(EditPictureActivity.this,"加载取消",Toast.LENGTH_SHORT).show();
                            ToastUtils.showError("图片加载失败");
//                            dialog.showErrorWithStatus("图片加载失败");
//                            Toast.makeText(EditPictureActivity.this, R.string.failed_loading, Toast.LENGTH_SHORT).show();
                        }
                    });
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        for (LabelView labelView : labels) {
            labelView.stopAnim();
        }
        super.onDestroy();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && MainApplication.tag == 1) {
            SharedPreferences firstInSp = getSharedPreferences(DataConstants.SHAREDPREFRENCES_FIRST_IN, Context.MODE_PRIVATE);
            //判断是不是第一次进入Fiu界面
            boolean isFirstIn = firstInSp.getBoolean(DataConstants.FIRST_IN_CREATE, true);
            if (isFirstIn) {
                FirstInAppUtils.showPop(EditPictureActivity.this, FirstInAppUtils.CREATE, activity_view);
                SharedPreferences.Editor editor = firstInSp.edit();
                editor.putBoolean(DataConstants.FIRST_IN_CREATE, false);
                editor.apply();
            }
        }
    }
}
