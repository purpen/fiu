package com.taihuoniao.fineix.scene;

import android.content.Intent;
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
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
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
import com.taihuoniao.fineix.beans.TagItem;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.utils.EffectUtil;
import com.taihuoniao.fineix.utils.GPUImageFilterTools;
import com.taihuoniao.fineix.utils.ImageUtils;
import com.taihuoniao.fineix.view.GlobalTitleLayout;
import com.taihuoniao.fineix.view.LabelView;
import com.taihuoniao.fineix.view.MyHighlightView;
import com.taihuoniao.fineix.view.MyImageViewTouch;
import com.taihuoniao.fineix.view.WaittingDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    private EditText nameTv;
    private EditText priceTv;
    private Button editBtn;
    private Button deleteBtn;
    //当前图片的bitmap
    private Bitmap currentBitmap;
    //当前点击的labelview
    private LabelView labelView;
    //当前filter
    private GPUImageFilter currentFilter;
    //标记产品装载图片的容器
    private MyImageViewTouch mImageView;
    //添加链接装载链接的容器
    private List<LabelView> labels = new ArrayList<LabelView>();
    //工具类
    private WaittingDialog dialog;
    //编辑好的图片存储名称
    private String picName;

    public EditPictureActivity() {
        super(0);
    }


    @Override
    protected void requestNet() {

    }

    @Override
    protected void initList() {
        imageUri = getIntent().getData();
        titleLayout.setTitle(R.string.tools);
        titleLayout.setContinueListener(this);
        EffectUtil.clear();
        ImageUtils.asyncLoadImage(EditPictureActivity.this, imageUri, new ImageUtils.LoadImageCallback() {
            @Override
            public void callback(Bitmap result) {
                currentBitmap = result;
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
        activity_view = View.inflate(EditPictureActivity.this, R.layout.activity_edit_picture, null);
        setContentView(activity_view);
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
        initPopupWindow();
    }

    private void initPopupWindow() {
        WindowManager windowManager = EditPictureActivity.this.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        View popup_view = View.inflate(EditPictureActivity.this, R.layout.pop_edit, null);
        cancelTv = (TextView) popup_view.findViewById(R.id.pop_edit_cancel);
        confirmTv = (TextView) popup_view.findViewById(R.id.pop_edit_confirm);
        productImg = (ImageView) popup_view.findViewById(R.id.pop_edit_img);
        nameTv = (EditText) popup_view.findViewById(R.id.pop_edit_name);
        priceTv = (EditText) popup_view.findViewById(R.id.pop_edit_price);
        editBtn = (Button) popup_view.findViewById(R.id.pop_edit_edit);
        deleteBtn = (Button) popup_view.findViewById(R.id.pop_edit_delete);
        popupWindow = new PopupWindow(popup_view, display.getWidth(), LinearLayout.LayoutParams.WRAP_CONTENT, true);
        // 设置动画效果
        popupWindow.setAnimationStyle(R.style.popupwindow_style);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        cancelTv.setOnClickListener(this);
        confirmTv.setOnClickListener(this);
        editBtn.setOnClickListener(this);
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
            public void onClick(final LabelView label) {
                labelView = label;
                TagItem tagItem = label.getTagInfo();
                ImageLoader.getInstance().displayImage(tagItem.getImagePath(), productImg);
                nameTv.setText(tagItem.getName());
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
            Toast.makeText(EditPictureActivity.this, R.string.more_three_chaining, Toast.LENGTH_SHORT).show();
            return;
        }
        //链接的默认位置
        int left = mImageView.getMeasuredWidth() / 2;
        int top = mImageView.getMeasuredHeight() / 2;
        if (labels.size() == 0 && left == 0 && top == 0) {
            left = mImageView.getWidth() / 2 - 10;
            top = mImageView.getWidth() / 2;
        }
        LabelView label = new LabelView(EditPictureActivity.this);
        label.init(tagItem);
        EffectUtil.addLabelEditable(mImageView, gpuRelative, label, left, top);
        labels.add(label);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_continue:
//                Toast.makeText(EditPictureActivity.this, "把图片施加滤镜效果存到内存中，并传递标签数据到下个界面", Toast.LENGTH_SHORT).show();
                dialog.show();
                savePicture();
                break;
            case R.id.pop_edit_delete:
                EffectUtil.removeLabelEditable(mImageView, gpuRelative, labelView);
                labels.remove(labelView);
                Log.e("<<<", "labels.size = " + labels.size());
                popupWindow.dismiss();
                break;
            case R.id.pop_edit_edit:
                startActivityForResult(new Intent(EditPictureActivity.this, SelectStoreActivity.class), DataConstants.REQUESTCODE_EDITEDIT_SEARCHSTORE);
                break;
            case R.id.pop_edit_confirm:
                TagItem tagItem = labelView.getTagInfo();
                tagItem.setName(nameTv.getText().toString());
                tagItem.setPrice(priceTv.getText().toString());
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
                if (EffectUtil.size() >= 3) {
                    Toast.makeText(EditPictureActivity.this, R.string.more_three_products, Toast.LENGTH_SHORT).show();
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

    //保存图片
    private void savePicture() {
        //加滤镜
        final Bitmap newBitmap = Bitmap.createBitmap(mImageView.getWidth(), mImageView.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas cv = new Canvas(newBitmap);
        RectF dst = new RectF(0, 0, mImageView.getWidth(), mImageView.getHeight());
        try {
            cv.drawBitmap(gpuImageView.capture(), null, dst, null);
        } catch (InterruptedException e) {
            e.printStackTrace();
            //出现异常存储的是未加滤镜效果的图片
            cv.drawBitmap(currentBitmap, null, dst, null);
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
                picName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                fileName = ImageUtils.saveToFile(MainApplication.editPicPath + "/" + picName, false, bitmap);
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
                return;
            }

            //保存标签信息
            List<TagItem> tagInfoList = new ArrayList<TagItem>();
            for (LabelView label : labels) {
                tagInfoList.add(label.getTagInfo());
            }
            MainApplication.tagInfoList = tagInfoList;
            //传递数据
            Intent intent = new Intent(EditPictureActivity.this, CreateSceneActivity.class);
            intent.setData(Uri.parse("file://" + MainApplication.editPicPath + "/" + picName));
            startActivity(intent);
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
                case DataConstants.RESULTCODE_EDIT_SEARCHSTORE:
                    TagItem tagItem = (TagItem) data.getSerializableExtra("tagItem");
                    if (requestCode == DataConstants.REQUESTCODE_EDIT_SEARCHSTORE) {
                        addLabel(tagItem);
                    } else {
                        ImageLoader.getInstance().displayImage(tagItem.getImagePath(), productImg);
                        nameTv.setText(tagItem.getName());
                        priceTv.setText(tagItem.getPrice());
                    }
                    break;
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
