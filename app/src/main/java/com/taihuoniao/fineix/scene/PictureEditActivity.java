package com.taihuoniao.fineix.scene;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.EditRecyclerAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.BuyGoodDetailsBean;
import com.taihuoniao.fineix.beans.TagItem;
import com.taihuoniao.fineix.blurview.BlurView;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.utils.EffectUtil;
import com.taihuoniao.fineix.utils.GPUImageFilterTools;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.view.GlobalTitleLayout;
import com.taihuoniao.fineix.view.LabelView;
import com.taihuoniao.fineix.view.MyHighlightView;
import com.taihuoniao.fineix.view.MyImageViewTouch;
import com.taihuoniao.fineix.view.WaittingDialog;
import com.taihuoniao.fineix.view.imageViewTouch.ImageViewTouch;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageView;

/**
 * Created by taihuoniao on 2016/8/12.
 * 图片编辑：添加商品。合成图片，添加滤镜。添加链接
 */
public class PictureEditActivity extends BaseActivity implements View.OnClickListener, GPUImageFilterTools.OnGpuImageFilterChosenListener {
    @Bind(R.id.img)
    GPUImageView img;
    @Bind(R.id.title_layout)
    GlobalTitleLayout titleLayout;
    @Bind(R.id.add_product)
    TextView addProduct;
    @Bind(R.id.filter)
    TextView filter;
    @Bind(R.id.image_view_touch)
    MyImageViewTouch imageViewTouch;
    @Bind(R.id.container)
    RelativeLayout container;
    @Bind(R.id.bottom_relative)
    RelativeLayout bottomRelative;
    @Bind(R.id.hint)
    TextView hintTv;
    @Bind(R.id.filter_recycler)
    RecyclerView filterRecycler;
    private View activity_view;
    //添加产品popwindow
    private PopupWindow productPop;
    private BlurView blurView;
    private RelativeLayout addProductRelative;
    private EditText brandTv, productName;
    private ImageView deleteBrand, deleteProduct;
    private TextView cancel, confirm;
    private WaittingDialog dialog;
    private List<LabelView> labels = new ArrayList<>();//添加的产品信息
    //当前获取焦点的labelview
    private LabelView labelView;
    private GPUImageFilter currentFilter;//当前滤镜
    private int currentPosition;//滤镜位置

    public PictureEditActivity() {
        super(0);
    }

    @Override
    protected void setContenttView() {
        activity_view = View.inflate(this, R.layout.activity_pic_edit, null);
        setContentView(activity_view);
    }

    @Override
    protected void initView() {
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) img.getLayoutParams();
        lp.height = MainApplication.getContext().getScreenWidth();
        img.setLayoutParams(lp);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) imageViewTouch.getLayoutParams();
        layoutParams.height = lp.height;
        imageViewTouch.setLayoutParams(layoutParams);
        img.setImage(MainApplication.cropBitmap);
        initProductPop();
        dialog = new WaittingDialog(this);
    }

    private boolean isClick;//判断是点击屏幕还是单击商品的事件判断

    @Override
    protected void initList() {
        titleLayout.setContinueListener(this);
        titleLayout.setTitle(R.string.biaoji_chanpin, getResources().getColor(R.color.white));
        imageViewTouch.setSingleTapListener(new ImageViewTouch.OnImageViewTouchSingleTapListener() {
            @Override
            public void onSingleTapConfirmed() {
                if (isClick) {
                    isClick = false;
                    return;
                }
                if (bottomRelative.getTranslationY() > 0) {
                    return;
                }
                showProductPop();
            }
        });
        imageViewTouch.setOnDrawableEventListener(new MyImageViewTouch.OnDrawableEventListener() {
            @Override
            public void onFocusChange(MyHighlightView newFocus, MyHighlightView oldFocus) {
                Log.e("<<<图片编辑", "onFocusChange");
            }

            @Override
            public void onDown(MyHighlightView view) {
                Log.e("<<<图片编辑", "onDown");
            }

            @Override
            public void onMove(MyHighlightView view) {
                Log.e("<<<图片编辑", "onMove");
            }

            @Override
            public void onClick(MyHighlightView view) {
                Log.e("<<<图片编辑", "onClick");
                isClick = true;
            }

            @Override
            public void onClick(final LabelView label) {
                Log.e("<<<标签", "onClick");
                labelView = label;
                labelView.setLeftOrRight();
            }

        });
        addProduct.setOnClickListener(this);
        filter.setOnClickListener(this);
        filterRecycler.setHasFixedSize(true);
        filterRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        EditRecyclerAdapter recyclerAdapter = new EditRecyclerAdapter(this, this, new EditRecyclerAdapter.ItemClick() {
            @Override
            public void click(int postion) {
                //当点击同一滤镜时，做滤镜调节时使用
            }
        });
        filterRecycler.setAdapter(recyclerAdapter);
        filterRecycler.post(new Runnable() {
            @Override
            public void run() {
                filterRecycler.setTranslationY(bottomRelative.getMeasuredHeight());
                filterRecycler.setVisibility(View.VISIBLE);
            }
        });
        currentFilter = new GPUImageFilter();
        currentPosition = 0;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (labels != null && labels.size() > 0) {
            for (LabelView labelView : labels) {
                labelView.wave();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (labels != null && labels.size() > 0) {
            for (LabelView labelView : labels) {
                labelView.stopAnim();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_product:
                if (bottomRelative.getTranslationY() == 0) {
                    return;
                }
                addProduct.setTextColor(getResources().getColor(R.color.yellow_bd8913));
                filter.setTextColor(getResources().getColor(R.color.white));
                bottomRelative.setTranslationY(bottomRelative.getMeasuredHeight());
                ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(filterRecycler, "translationY", filterRecycler.getMeasuredHeight());
                final ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(bottomRelative, "translationY", 0);
                objectAnimator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        objectAnimator1.start();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                objectAnimator1.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        try {
                            blur(myShot(), 25f);
                        } catch (Exception e) {
                            productPopView.setBackgroundResource(R.color.black_blur);
                        }
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                objectAnimator.start();
                break;
            case R.id.filter:
                if (filterRecycler.getVisibility() == View.VISIBLE && filterRecycler.getTranslationY() == 0) {
                    return;
                }
                filter.setTextColor(getResources().getColor(R.color.yellow_bd8913));
                addProduct.setTextColor(getResources().getColor(R.color.white));
                ObjectAnimator objectAnimator2 = ObjectAnimator.ofFloat(bottomRelative, "translationY", bottomRelative.getMeasuredHeight());
                final ObjectAnimator objectAnimator3 = ObjectAnimator.ofFloat(filterRecycler, "translationY", 0);
                objectAnimator2.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        objectAnimator3.start();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                objectAnimator2.start();
                break;
            case R.id.cancel:
                productPop.dismiss();
                break;
            case R.id.confirm:
                if (TextUtils.isEmpty(productName.getText())) {
                    productPop.dismiss();
                } else {
                    productPop.dismiss();
                    TagItem tagItem = new TagItem();
                    tagItem.setId(productId);
                    tagItem.setType(type);
                    tagItem.setName(brandTv.getText().toString() + productName.getText().toString());
                    addLabel(tagItem);
                    brandTv.setText("");
                    productName.setText("");
                }
                break;
            case R.id.delete_brand:
                brandTv.setText("");
                break;
            case R.id.delete_product:
                productName.setText("");
                break;
            case R.id.brand_tv:
                Intent intent1 = new Intent(this, SearchBrandActivity.class);
                intent1.putExtra(PictureEditActivity.class.getSimpleName(), false);
                if (brandTv.getText().toString().length() > 0) {
                    intent1.putExtra("brand", brandTv.getText().toString());
                }
                startActivityForResult(intent1, 1);
                break;
            case R.id.product_tv:
                Intent intent2 = new Intent(this, SearchBrandActivity.class);
                if (brandTv.getText().toString().length() > 0) {
                    intent2.putExtra("brand", brandTv.getText().toString());
                    if (brandId != null) {
                        intent2.putExtra("brandId", brandId);
                    }
                }
                if (productName.getText().toString().length() > 0) {
                    intent2.putExtra("product", productName.getText().toString());
                    Log.e("<<<", "传递产品名称=" + productName.getText().toString());
                }
                startActivityForResult(intent2, 1);
                break;
            case R.id.add_product_relative:
                productPop.dismiss();
                Intent intent = new Intent(this, AddProductActivity.class);
                startActivityForResult(intent, DataConstants.REQUESTCODE_EDIT_ADDPRODUCT);
                break;
            case R.id.title_continue:
                if (!dialog.isShowing()) {
                    dialog.show();
                }
                savePicture();
                break;
        }
    }

    private void savePicture() {
        if (img.getWidth() <= 0 || img.getHeight() <= 0) {
            dialog.dismiss();
            ToastUtils.showError("图片信息错误，请重试");
            return;
        }
        int w = MainApplication.getContext().getScreenWidth();
        //加滤镜
        final Bitmap newBitmap = Bitmap.createBitmap(w, w,
                Bitmap.Config.ARGB_8888);
        Canvas cv = new Canvas(newBitmap);
        RectF dst = new RectF(0, 0, w, w);
        cv.drawBitmap(MainApplication.cropBitmap, null, dst, null);
        //加商品
        EffectUtil.applyOnSave(cv, imageViewTouch);
        img.setImage(newBitmap);
        onGpuImageFilterChosenListener(currentFilter, currentPosition);
        Bitmap bitmap = null;
        try {
            bitmap = img.capture();
        } catch (InterruptedException e) {
            img.setImage(MainApplication.cropBitmap);
            dialog.dismiss();
            ToastUtils.showError("图片信息错误，请重试");
            return;
        }
        if (bitmap == null) {
            img.setImage(MainApplication.cropBitmap);
            dialog.dismiss();
            ToastUtils.showError("图片信息错误，请重试");
            return;
        }
        MainApplication.editBitmap = bitmap;
        //保存标签信息
        List<TagItem> tagInfoList = new ArrayList<>();
        for (LabelView label : labels) {
            tagInfoList.add(label.getTagInfo());
        }
        MainApplication.tagInfoList = tagInfoList;
        Log.e("<<<上传之前", tagInfoList.toString());
        img.setImage(MainApplication.cropBitmap);
        dialog.dismiss();
        Intent intent = new Intent(this, CreateQJActivity.class);
        intent.putExtra(PictureEditActivity.class.getSimpleName(), false);
        startActivity(intent);
    }

    private View productPopView;

    private void initProductPop() {
        productPopView = View.inflate(this, R.layout.pop_add_product, null);
        blurView = (BlurView) productPopView.findViewById(R.id.blur_view);
        addProductRelative = (RelativeLayout) productPopView.findViewById(R.id.add_product_relative);
        brandTv = (EditText) productPopView.findViewById(R.id.brand_tv);
        productName = (EditText) productPopView.findViewById(R.id.product_tv);
        deleteBrand = (ImageView) productPopView.findViewById(R.id.delete_brand);
        deleteProduct = (ImageView) productPopView.findViewById(R.id.delete_product);
        cancel = (TextView) productPopView.findViewById(R.id.cancel);
        confirm = (TextView) productPopView.findViewById(R.id.confirm);
        productPop = new PopupWindow(productPopView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        productPop.setAnimationStyle(R.style.alpha);
        productPop.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        productPop.setBackgroundDrawable(ContextCompat.getDrawable(this,
                R.color.nothing));
        productPop.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });
        productPop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {

            }
        });
        brandTv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    deleteBrand.setVisibility(View.VISIBLE);
                } else {
                    deleteBrand.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        productName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    deleteProduct.setVisibility(View.VISIBLE);
                } else {
                    deleteProduct.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        addProductRelative.setOnClickListener(this);
        brandTv.setOnClickListener(this);
        productName.setOnClickListener(this);
        deleteBrand.setOnClickListener(this);
        deleteProduct.setOnClickListener(this);
        cancel.setOnClickListener(this);
        confirm.setOnClickListener(this);
    }


    public Bitmap myShot() throws InterruptedException {
        // 获取windows中最顶层的view
        View view = getWindow().getDecorView();
        view.buildDrawingCache();
        // 获取状态栏高度
        Rect rect = new Rect();
        view.getWindowVisibleDisplayFrame(rect);
        // 获取屏幕宽和高
        int widths = MainApplication.getContext().getScreenWidth();
        int heights = MainApplication.getContext().getScreenHeight();
        // 允许当前窗口保存缓存信息
        view.setDrawingCacheEnabled(true);
        // 去掉状态栏
        Bitmap bmp = Bitmap.createBitmap(view.getDrawingCache(), 0,
                0, widths, heights);
        Canvas canvas = new Canvas(bmp);
        canvas.drawBitmap(img.capture(), 0, titleLayout.getMeasuredHeight(), new Paint());
        // 销毁缓存信息
        view.destroyDrawingCache();
        return bmp;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void blur(Bitmap bkg, float radius) throws Exception {
        Bitmap overlay = Bitmap.createBitmap(bkg.getWidth(), bkg.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(overlay);
        canvas.drawBitmap(bkg, -productPopView.getLeft(), -productPopView.getTop(), null);
        RenderScript rs = RenderScript.create(this);
        Allocation overlayAlloc = Allocation.createFromBitmap(rs, overlay);
        ScriptIntrinsicBlur blur;
        blur = ScriptIntrinsicBlur.create(rs, overlayAlloc.getElement());
        blur.setInput(overlayAlloc);
        blur.setRadius(radius);
        blur.forEach(overlayAlloc);
        overlayAlloc.copyTo(overlay);
        productPop.setBackgroundDrawable(new BitmapDrawable(getResources(), overlay));
        rs.destroy();
    }

    private void showProductPop() {
        productPop.showAtLocation(activity_view, Gravity.CENTER, 0, 0);
    }

    //添加标签
    private void addLabel(TagItem tagItem) {
        if (labels.size() >= 3) {
            ToastUtils.showInfo("您最多可以添加三个链接");
            return;
        }
        //链接的默认位置
        int left = imageViewTouch.getWidth() / 4;
        int top = imageViewTouch.getHeight() / 2;
        if (left <= 0 || top <= 0) {
            left = 0;
            top = 0;
        }
        final LabelView label = new LabelView(this);
        label.init(tagItem);
        label.setDeleteVisible(true);
        EffectUtil.addLabelEditable(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                labelView = label;
                EffectUtil.removeLabelEditable(imageViewTouch, container, labelView);
                labels.remove(labelView);
                setHint();
            }
        }, imageViewTouch, container, label, left, top);
        labels.add(label);
        if (labels != null && labels.size() > 0) {
            for (LabelView labelView : labels) {
                labelView.wave();
            }
        }
        setHint();
    }

    private void setHint() {
        switch (labels.size()) {
            case 0:
                hintTv.setText("点击图片标记相关信息");
                break;
            case 1:
                hintTv.setText("您还可以继续标记2个产品(￣▽￣)");
                break;
            case 2:
                hintTv.setText("您还可以继续标记1个产品(￣▽￣)");
                break;
            case 3:
                hintTv.setText("最多标记三个产品哦(⌒▽⌒)");
                break;
        }
    }

    private boolean isIn;

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (!isIn) {
            try {
                blur(myShot(), 25f);
            } catch (Exception e) {
                productPopView.setBackgroundResource(R.color.black_blur);
            }
            isIn = true;
        }
    }

    private String brandId;
    private String productId;
    private int type = 1;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            switch (resultCode) {
                case 1:
                    String brand = data.getStringExtra("brand");
                    brandId = data.getStringExtra("brandId");
                    String product = data.getStringExtra("product");
                    productId = data.getStringExtra("productId");
                    type = data.getIntExtra("type", 1);
                    if (brand != null) {
                        brandTv.setText(brand);
                    }
                    if (product != null) {
                        productName.setText(product);
                    }
                    break;
                case DataConstants.RESULTCODE_EDIT_ADDPRODUCT:
                    final BuyGoodDetailsBean productListBean = (BuyGoodDetailsBean) data.getSerializableExtra("product");
                    //是自动添加标签还是后添加
                    TagItem tag = new TagItem(productListBean.getData().getTitle(), productListBean.getData().getSale_price()+"");
                    tag.setId(productListBean.getData().get_id());
                    tag.setLoc(2);
                    tag.setType(1);
                    addLabel(tag);
                    try {
                        String url = productListBean.getData().getPng_asset().get(0).getUrl();
                        ImageLoader.getInstance().loadImage(url, new ImageLoadingListener() {
                            @Override
                            public void onLoadingStarted(String imageUri, View view) {
                                if (!dialog.isShowing()) {
                                    dialog.show();
                                }
                            }

                            @Override
                            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                                dialog.dismiss();
                                ToastUtils.showError("图片加载失败");
                            }

                            @Override
                            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                                dialog.dismiss();

                                EffectUtil.addStickerImage(imageViewTouch, PictureEditActivity.this, loadedImage);
                            }

                            @Override
                            public void onLoadingCancelled(String imageUri, View view) {
                                dialog.dismiss();
                                ToastUtils.showError("图片加载失败");
                            }
                        });
                    } catch (Exception e) {
                        Log.e("<<<", "没有褪底图");
                    }

                    break;
            }
        }
    }

    @Override
    public void onGpuImageFilterChosenListener(GPUImageFilter filter, int position) {
        currentFilter = filter;
        currentPosition = position;
        img.setFilter(filter);
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
        img.requestRender();
    }
}
