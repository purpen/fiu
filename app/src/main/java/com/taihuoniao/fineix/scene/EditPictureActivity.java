package com.taihuoniao.fineix.scene;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.EditRecyclerAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.utils.EffectUtil;
import com.taihuoniao.fineix.utils.GPUImageFilterTools;
import com.taihuoniao.fineix.utils.ImageUtils;
import com.taihuoniao.fineix.view.WaittingDialog;
import com.taihuoniao.fineix.view.GlobalTitleLayout;
import com.taihuoniao.fineix.view.LabelView;
import com.taihuoniao.fineix.view.MyHighlightView;
import com.taihuoniao.fineix.view.MyImageViewTouch;

import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageView;

/**
 * Created by taihuoniao on 2016/3/17.
 */
public class EditPictureActivity extends BaseActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    //上个界面传递过来的数据
    private Uri imageUri;
    //界面下的控件
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
    //当前filter
    private GPUImageFilter currentFilter;
    //标记产品装载图片的容器
    private MyImageViewTouch mImageView;
    //工具类
    private WaittingDialog dialog;

    public EditPictureActivity() {
        super(R.layout.activity_edit_picture);
    }

    @Override
    protected void requestNet() {

    }

    @Override
    protected void initList() {
        imageUri = getIntent().getData();
        titleLayout.setTitle(R.string.tools);
        EffectUtil.clear();
        ImageUtils.asyncLoadImage(EditPictureActivity.this, imageUri, new ImageUtils.LoadImageCallback() {
            @Override
            public void callback(Bitmap result) {
                gpuImageView.setImage(result);
                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) gpuImageView.getLayoutParams();
                RelativeLayout.LayoutParams containerLp = (RelativeLayout.LayoutParams) gpuRelative.getLayoutParams();
                if (gpuImageView.getWidth() / gpuImageView.getHeight() > 3 / 4) {
                    lp.width = gpuImageView.getHeight() * 3 / 4;
                    containerLp.width = gpuRelative.getHeight() * 3 / 4;
                } else {
                    lp.height = gpuImageView.getWidth() * 4 / 3;
                    containerLp.height = gpuRelative.getWidth() * 4 / 3;
                }
                gpuImageView.setLayoutParams(lp);
                gpuRelative.setLayoutParams(containerLp);
                initEditView();

            }
        });
        //设置布局大小一样
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        EditRecyclerAdapter recyclerAdapter = new EditRecyclerAdapter(EditPictureActivity.this, new GPUImageFilterTools.OnGpuImageFilterChosenListener() {
            @Override
            public void onGpuImageFilterChosenListener(GPUImageFilter filter) {
                currentFilter = filter;
                gpuImageView.setFilter(filter);
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

    @Override
    protected void initView() {
//        setContentView(R.layout.activity_edit_picture);
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
    }

    private void initEditView() {
        //初始化画布
        View overView = View.inflate(EditPictureActivity.this,
                R.layout.view_over, null);
        mImageView = (MyImageViewTouch) overView.findViewById(R.id.view_over_mimg);
        ViewGroup.LayoutParams params = gpuImageView.getLayoutParams();
        overView.setLayoutParams(params);
        mImageView.setLayoutParams(params);
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
            public void onClick(LabelView label) {

            }
        });
//        mImageView.setSingleTapListener(new ImageViewTouch.OnImageViewTouchSingleTapListener() {
//            @Override
//            public void onSingleTapConfirmed() {
//                //添加标签？
//            }
//        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_edit_back:
                recyclerView.setVisibility(View.VISIBLE);
                seekBar.setVisibility(View.GONE);
                backBtn.setVisibility(View.GONE);
                break;
            case R.id.activity_edit_products_relative:
                if (EffectUtil.size() >= 3) {
                    Toast.makeText(EditPictureActivity.this, R.string.more_three_products, Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(EditPictureActivity.this, AddProductActivity.class);
                startActivityForResult(intent, DataConstants.REQUESTCODE_EDIT_ADDPRODUCT);
                break;
            case R.id.activity_edit_chaining_relative:
                startActivity(new Intent(EditPictureActivity.this, SelectStoreActivity.class));
                break;
            case R.id.activity_edit_filter_relative:
                if (recyclerLinear.getVisibility() == View.GONE) {
                    filterTv.setTextColor(getResources().getColor(R.color.red));
                    filterRedline.setVisibility(View.VISIBLE);
                    recyclerLinear.setVisibility(View.VISIBLE);
                } else {
                    filterTv.setTextColor(getResources().getColor(R.color.white));
                    filterRedline.setVisibility(View.GONE);
                    recyclerLinear.setVisibility(View.GONE);
                }
                break;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        GPUImageFilterTools.FilterAdjuster filterAdjuster = new GPUImageFilterTools.FilterAdjuster(currentFilter);
        filterAdjuster.adjust(progress);
        gpuImageView.requestRender();
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
                case DataConstants.RESULTCODE_EDIT_ADDPRODUCT:
                    String url = data.getStringExtra("url");
                    final ImageView proImg = new ImageView(EditPictureActivity.this);
                    proImg.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    ImageLoader.getInstance().displayImage(url, proImg, new ImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String imageUri, View view) {
                            dialog.show();
                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                            dialog.dismiss();
                            Toast.makeText(EditPictureActivity.this, R.string.failed_loading, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            dialog.dismiss();
                            EffectUtil.addStickerImage(mImageView, EditPictureActivity.this, loadedImage);
                        }

                        @Override
                        public void onLoadingCancelled(String imageUri, View view) {
                            dialog.dismiss();
                            Toast.makeText(EditPictureActivity.this, R.string.failed_loading, Toast.LENGTH_SHORT).show();
                        }
                    });
                    break;
            }
        }
    }
}
