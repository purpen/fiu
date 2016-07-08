package com.taihuoniao.fineix.scene;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.EditRecyclerAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.utils.GPUImageFilterTools;
import com.taihuoniao.fineix.utils.ImageUtils;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.view.GlobalTitleLayout;
import com.taihuoniao.fineix.view.LabelView;
import com.taihuoniao.fineix.view.MyImageViewTouch;
import com.taihuoniao.fineix.view.WaittingDialog;

import java.util.ArrayList;
import java.util.List;

import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageView;

/**
 * Created by taihuoniao on 2016/6/1.
 */
public class FilterActivity extends BaseActivity implements View.OnClickListener {
    private GlobalTitleLayout titleLayout;
    private RelativeLayout gpuRelative;
    private GPUImageView gpuImageView;
    private RecyclerView recyclerView;
    private SeekBar seekBar;
    private Button backBtn;
    private RelativeLayout productsRelative;
    private RelativeLayout chainingRelative;
    private RelativeLayout filterRelative;
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
    private List<LabelView> labels = new ArrayList<>();
    //工具类
    private WaittingDialog dialog;
    //编辑好的图片存储名称
    private String picName;
    public static FilterActivity instance = null;
    //图片加载完毕之后的宽高
    private int picWidth, picHeight;
    private DisplayImageOptions options;

    public FilterActivity() {
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
        Uri imageUri = getIntent().getData();
        titleLayout.setTitle(R.string.filter);
        titleLayout.setBackgroundResource(R.color.black_touming);
        titleLayout.setContinueListener(this);
        ImageLoader.getInstance().loadImage(imageUri.toString(), options, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                ToastUtils.showError("图片加载失败，请返回重试");
//                dialog.showErrorWithStatus("图片加载失败，请返回重试");
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
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                ToastUtils.showError("图片加载失败，请返回重试");
//                dialog.showErrorWithStatus("图片加载失败，请返回重试");
            }
        });
//        ImageUtils.asyncLoadImage(FilterActivity.this, imageUri, new ImageUtils.LoadImageCallback() {
//            @Override
//            public void callback(Bitmap result) {
//                currentBitmap = result;
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
//            }
//        });
        //设置布局大小一样
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        EditRecyclerAdapter recyclerAdapter = new EditRecyclerAdapter(FilterActivity.this, new GPUImageFilterTools.OnGpuImageFilterChosenListener() {
            @Override
            public void onGpuImageFilterChosenListener(GPUImageFilter filter, int position) {
//                currentFilter = filter;
                gpuImageView.setFilter(filter);
                GPUImageFilterTools.FilterAdjuster filterAdjuster = new GPUImageFilterTools.FilterAdjuster(filter);
                switch (position) {
                    //        原图、都市、摩登、日光、摩卡、佳人、 候鸟、夏日、午茶、戏剧、流年、暮光
                    case 4:
                        filterAdjuster.adjust(97);//摩卡
                        break;
                    case 11:
                        filterAdjuster.adjust(45);//暮光
                        break;
                    case 6:
                        filterAdjuster.adjust(0);//候鸟
                        break;
                    case 7:
                        filterAdjuster.adjust(40);//夏日
                        break;
                    case 1:
                        filterAdjuster.adjust(100);//都市
                        break;
                    case 5:
                        filterAdjuster.adjust(25);//佳人
                        break;
                    case 10:
                        filterAdjuster.adjust(55);//流年
                        break;
                    case 3:
                        filterAdjuster.adjust(53);//日光
                        break;
                    case 8:
                        filterAdjuster.adjust(60);//午茶
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
//        seekBar.setOnSeekBarChangeListener(this);
//        backBtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);
        productsRelative.setVisibility(View.GONE);
        chainingRelative.setVisibility(View.GONE);
        filterRelative.setVisibility(View.GONE);
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
        View activity_view = View.inflate(FilterActivity.this, R.layout.activity_edit_picture, null);
        setContentView(activity_view);
        instance = FilterActivity.this;
        titleLayout = (GlobalTitleLayout) findViewById(R.id.activity_edit_titlelayout);
        gpuRelative = (RelativeLayout) findViewById(R.id.activity_edit_main_area);
        gpuImageView = (GPUImageView) findViewById(R.id.activity_edit_gpuimage);
        LinearLayout recyclerLinear = (LinearLayout) findViewById(R.id.activity_edit_recycler_linear);
        recyclerView = (RecyclerView) findViewById(R.id.activity_edit_recycler);
        seekBar = (SeekBar) findViewById(R.id.activity_edit_progress);
        backBtn = (Button) findViewById(R.id.activity_edit_back);
        LinearLayout bottomLinear = (LinearLayout) findViewById(R.id.activity_edit_bottom_linear);
//        bottomLinear.setVisibility(View.GONE);
        ViewGroup.LayoutParams l = bottomLinear.getLayoutParams();
        l.height = 1;
        bottomLinear.setLayoutParams(l);
        productsRelative = (RelativeLayout) findViewById(R.id.activity_edit_products_relative);
        chainingRelative = (RelativeLayout) findViewById(R.id.activity_edit_chaining_relative);
        filterRelative = (RelativeLayout) findViewById(R.id.activity_edit_filter_relative);
        TextView productsTv = (TextView) findViewById(R.id.activity_edit_products_tv);
        TextView chainingTv = (TextView) findViewById(R.id.activity_edit_chaining_tv);
        TextView filterTv = (TextView) findViewById(R.id.activity_edit_filter_tv);
        TextView productsRedline = (TextView) findViewById(R.id.activity_edit_products_redline);
        TextView chainingRedline = (TextView) findViewById(R.id.activity_edit_chaining_redline);
        TextView filterRedline = (TextView) findViewById(R.id.activity_edit_filter_redline);
        dialog = new WaittingDialog(FilterActivity.this);
        ImageLoader imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.default_background_750_1334)
                .showImageForEmptyUri(R.mipmap.default_background_750_1334)
                .showImageOnFail(R.mipmap.default_background_750_1334)
                .cacheInMemory(false)
                .cacheOnDisk(false).considerExifParams(true)
                .build();
        recyclerLinear.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_continue:
                dialog.show();
                savePicture();
                break;
            case R.id.activity_edit_back:
                recyclerView.setVisibility(View.VISIBLE);
                seekBar.setVisibility(View.GONE);
                backBtn.setVisibility(View.GONE);
                break;

        }
    }

    //保存图片

    private void savePicture() {
        if (gpuImageView.getWidth() <= 0 || gpuImageView.getHeight() <= 0) {
            return;
        }
        //加滤镜
        final Bitmap newBitmap = Bitmap.createBitmap(gpuImageView.getWidth(), gpuImageView.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas cv = new Canvas(newBitmap);
        RectF dst = new RectF(0, 0, gpuImageView.getWidth(), gpuImageView.getHeight());
        try {
            cv.drawBitmap(gpuImageView.capture(), null, dst, null);
        } catch (InterruptedException e) {
            e.printStackTrace();
            ToastUtils.showError("图片处理异常，请重试");
//            dialog.showErrorWithStatus("图片处理异常，请重试");
//            Toast.makeText(FilterActivity.this, "图片处理异常，请重试", Toast.LENGTH_SHORT).show();
            return;
        }
        //加商品
//        EffectUtil.applyOnSave(cv, mImageView);
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
                fileName = ImageUtils.saveToFile(MainApplication.filterPicPath, false, bitmap);
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
                ToastUtils.showError("图片处理错误,请清理缓存后重试");
//                dialog.showErrorWithStatus("图片处理错误，请清理缓存后重试");
                return;
            }
            //传递数据
            Intent intent = new Intent(FilterActivity.this, CreateSceneActivity.class);
            intent.setData(Uri.parse("file://" + fileName));
            startActivity(intent);
        }
    }


}
