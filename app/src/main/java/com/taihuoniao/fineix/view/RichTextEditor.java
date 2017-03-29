package com.taihuoniao.fineix.view;

import android.animation.LayoutTransition;
import android.animation.LayoutTransition.TransitionListener;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.blurview.BlurView;
import com.taihuoniao.fineix.utils.GlideUtils;
import com.taihuoniao.fineix.utils.ImageUtils;
import com.taihuoniao.fineix.utils.LogUtil;
import com.taihuoniao.fineix.utils.Util;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.taihuoniao.fineix.R.dimen.dp30;
import static com.taihuoniao.fineix.R.dimen.dp5;
import static com.taihuoniao.fineix.R.id.edit_imageView;

/**
 * 亮点编辑器
 */
public class RichTextEditor extends ScrollView {
    private ImageQueue<DataImageView> imageQueue;
    public float scale = 0.7f;
    public static final String TAG = "RichTextEditor";
    public static final String HINT = "添加文字或图片";
    private int viewTagIndex = 1; // 新生的view都会打一个tag，对每个view来说，这个tag是唯一的。
    private LinearLayout allLayout; // 这个是所有子view的容器，scrollView内部的唯一一个ViewGroup
    private LayoutInflater inflater;
    private OnKeyListener keyListener; // 所有EditText的软键盘监听器
    private OnFocusChangeListener focusListener; // 所有EditText的焦点监听listener
    private EditText lastFocusEdit; // 最近被聚焦的EditText
    private LayoutTransition mTransitioner; // 只在图片View添加或remove时，触发transition动画
    private int disappearingImageIndex = 0;
    private int imageHeight;
    public RichTextEditor(Context context) {
        this(context, null);
    }

    public RichTextEditor(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RichTextEditor(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflater = LayoutInflater.from(context);
        allLayout = new LinearLayout(context);
        allLayout.setOrientation(LinearLayout.VERTICAL);
        allLayout.setBackgroundColor(Color.WHITE);
        imageHeight = getResources().getDimensionPixelSize(R.dimen.dp195);
        imageQueue = new ImageQueue<>();
        setupLayoutTransitions();
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);

        addView(allLayout, layoutParams);

        // 2. 初始化键盘退格监听
        // 主要用来处理点击回删按钮时，view的一些列合并操作
        keyListener = new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN
                        && event.getKeyCode() == KeyEvent.KEYCODE_DEL) {
                    EditText edit = (EditText) v;
                    onBackspacePress(edit);
                }
                return false;
            }
        };

        focusListener = new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    lastFocusEdit = (EditText) v;
                }
            }
        };
        initEditText();
    }

    /**
     * 图片缩放动画
     * @param v
     */
    private void onImageScaleClick(final View v) {
        LinearLayout.LayoutParams layoutParams;
        if (v.getHeight()==imageHeight){
            layoutParams = new LinearLayout.LayoutParams((int) (v.getWidth() * 0.7), (int) (imageHeight * 0.7));
        }else {
            layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,imageHeight);
        }
        v.setLayoutParams(layoutParams);
    }

    private void initEditText() {
        EditText firstEdit = createEditText("",HINT,0);
        firstEdit.clearFocus();
        allLayout.addView(firstEdit);
        lastFocusEdit = firstEdit;
    }


    /**
     * 处理软键盘backSpace回退事件
     *
     * @param editTxt 光标所在的文本输入框
     */
    private void onBackspacePress(EditText editTxt) {
        int startSelection = editTxt.getSelectionStart();
        // 只有在光标已经顶到文本输入框的最前方，在判定是否删除之前的图片，或两个View合并
        if (startSelection == 0) {
            int editIndex = allLayout.indexOfChild(editTxt);
            View preView = allLayout.getChildAt(editIndex - 1); // 如果editIndex-1<0,
            // 则返回的是null
            if (null != preView) {
                if (preView instanceof RelativeLayout) {
                    // 光标EditText的上一个view对应的是图片
                    onImageCloseClick(preView);
                } else if (preView instanceof EditText) {
                    // 光标EditText的上一个view对应的还是文本框EditText
                    String str1 = editTxt.getText().toString();
                    EditText preEdit = (EditText) preView;
                    String str2 = preEdit.getText().toString();

                    // 合并文本view时，不需要transition动画
                    allLayout.setLayoutTransition(null);
                    allLayout.removeView(editTxt);
                    allLayout.setLayoutTransition(mTransitioner); // 恢复transition动画

                    // 文本合并
                    preEdit.setText(str2 + str1);
                    preEdit.requestFocus();
                    preEdit.setSelection(str2.length(), str2.length());
                    lastFocusEdit = preEdit;
                }
            }
        }
    }

    /**
     * 删除图片合并文本
     */
    private void onImageCloseClick(View view) {
        if (!mTransitioner.isRunning()) {
            disappearingImageIndex = allLayout.indexOfChild(view);
            allLayout.removeView(view);
            imageQueue.remove((DataImageView) view.findViewById(R.id.edit_imageView));
            mergeEditText();
        }
    }

    public void insertEditText(int index, String content) {
        if (index == 0) {
            View view = allLayout.getChildAt(index);
            if (view instanceof EditText) {
                ((EditText) view).setText(content);
            }
        } else {
            View view = allLayout.getChildAt(index);
            if (view instanceof EditText) { //判断即将插入的是否是EditText
                EditText et = (EditText) view;
                if (TextUtils.isEmpty(et.getText().toString().trim())) {//再判断内容是否为空
                    ((EditText) view).setText(content);
                } else {
                    EditText editText = createEditText(content, HINT, getResources().getDimensionPixelSize(dp5));
                    allLayout.addView(editText);
                }
            } else { //不是编辑框直接创建
                EditText editText = createEditText(content, HINT, getResources().getDimensionPixelSize(dp5));
                allLayout.addView(editText);
            }

        }

    }

    /**
     * 生成文本输入框
     */
    private EditText createEditText(String content, String hint, int padding) {
        EditText editText = (EditText) inflater.inflate(R.layout.edit_item, null);
        editText.setOnKeyListener(keyListener);
        editText.setTag(viewTagIndex++);
        padding = getResources().getDimensionPixelSize(R.dimen.dp15);
        editText.setPadding(0, padding,0,padding);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER_VERTICAL;
        editText.setLayoutParams(params);
        editText.setLineSpacing(1,1.3f);
        editText.setMinHeight(getResources().getDimensionPixelSize(R.dimen.dp30));
        editText.setHint(hint);
        editText.setText(content);
        editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        editText.setTextColor(getResources().getColor(R.color.color_222));
        editText.setOnFocusChangeListener(focusListener);
        return editText;
    }

    /**
     * 生成图片
     */
    private RelativeLayout createImageLayout() {
        RelativeLayout layout = (RelativeLayout) inflater.inflate(
                R.layout.edit_imageview_item, null);
        layout.setTag(viewTagIndex++);
        BlurView blurView = (BlurView)layout.findViewById(R.id.blurView);
        final Drawable windowBackground = ((Activity)getContext()).getWindow().getDecorView().getBackground();
        blurView.setupWith(layout)
                .windowBackground(windowBackground)
                .blurRadius(25);
        View closeView = layout.findViewById(R.id.image_close);
        View imageScale = layout.findViewById(R.id.image_scale);
        closeView.setTag(layout.getTag());
        setClickListener(closeView,layout);
        setClickListener(imageScale,layout);
        return layout;
    }

    private void setClickListener(View v,final RelativeLayout layout) {
        v.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.image_close:
                        onImageCloseClick(layout);
                        break;
                    case R.id.image_scale:
                        onImageScaleClick(layout);
                        break;
                    default:
                        break;
                }
            }
        });
    }


    /**
     * 根据绝对路径添加view
     *
     * @param imagePath
     */
    public void insertImage(String imagePath) {
        Bitmap bmp = ImageUtils.getSmallBitmap(imagePath);
        insertImage(bmp, imagePath);
    }

    /**
     * 插入某处插入一张图片
     */
    public void insertImageAtIndex(int index, String imagePath) {
        if (TextUtils.isEmpty(imagePath)) return;
        index = allLayout.getChildCount();
        if (imagePath.contains("http")) {
            addImageViewAtIndex(index, imagePath);
        } else {
            Bitmap bitmap = ImageUtils.getSmallBitmap(imagePath);
            addImageViewAtIndex(index, bitmap, imagePath);
        }
        addEditTextAtIndex(index + 1, "");
    }

    /**
     * 插入一张图片
     */
    private void insertImage(Bitmap bitmap, String imagePath) {
        String lastEditStr = lastFocusEdit.getText().toString();
        //最近获得焦点的EditText的光标位置
        int cursorIndex = lastFocusEdit.getSelectionStart();
        //最近获得焦点EditText光标之前内容长度
        String editStr1 = lastEditStr.substring(0, cursorIndex).trim();
        //最近获得焦点EditText索引
        int lastEditIndex = allLayout.indexOfChild(lastFocusEdit);
        lastFocusEdit.setText(editStr1);
        //最近获得焦点EditText光标之后的内容长度
        String editStr2 = lastEditStr.substring(cursorIndex).trim();
        if (allLayout.getChildCount() - 1 == lastEditIndex
                || editStr2.length() >= 0) {
            addEditTextAtIndex(lastEditIndex + 1, editStr2);
        }

        addImageViewAtIndex(lastEditIndex + 1, bitmap, imagePath);
        lastFocusEdit.requestFocus();
        lastFocusEdit.setSelection(editStr1.length(), editStr1.length());
        hideKeyBoard();
    }


    /**
     * 隐藏小键盘
     */
    public void hideKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(lastFocusEdit.getWindowToken(), 0);
    }

    /**
     * 在特定位置插入EditText
     *
     * @param index   位置
     * @param editStr EditText显示的文字
     */
    public void addEditTextAtIndex(final int index, String editStr) {
        EditText editText2 = createEditText(editStr, HINT, getResources()
                .getDimensionPixelSize(R.dimen.dp5));
        // 请注意此处，EditText添加、或删除不触动Transition动画
        allLayout.setLayoutTransition(null);
        allLayout.addView(editText2, index);
        allLayout.setLayoutTransition(mTransitioner); // remove之后恢复transition动画
    }


    /**
     * 添加网络图片
     *
     * @param index
     * @param imagePath
     */
    private void addImageViewAtIndex(int index, String imagePath) {
        final RelativeLayout relativeLayout = createImageLayout();
        DataImageView imageView = (DataImageView) relativeLayout.findViewById(edit_imageView);
        GlideUtils.displayImageNoFading(imagePath, imageView);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                (int)((Util.getScreenWidth()-getResources().getDimensionPixelSize(dp30))*scale), (int)(imageHeight*scale));
        relativeLayout.setLayoutParams(lp);
        imageView.setAbsolutePath(imagePath);
        imageView.isUpload = true;
        imageQueue.add(imageView);
        allLayout.addView(relativeLayout, index);
    }


    /**
     * 添加本地图片
     *
     * @param index
     * @param bmp
     * @param imagePath
     */
    private void addImageViewAtIndex(final int index, Bitmap bmp,
                                     String imagePath) {
        if (null == bmp) {
            LogUtil.e("addImageViewAtIndex参数bmp为空");
            return;
        }
        RelativeLayout relativeLayout = createImageLayout();
        DataImageView imageView = (DataImageView) relativeLayout
                .findViewById(edit_imageView);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        GlideUtils.displayImageNoFading(imagePath, imageView);
        imageView.setBitmap(bmp);
        imageView.setAbsolutePath(imagePath);
        imageView.isUpload = false;
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                (int)((Util.getScreenWidth()-getResources().getDimensionPixelSize(dp30))*scale), (int)(imageHeight*scale));
        relativeLayout.setLayoutParams(lp);
        imageQueue.add(imageView);
        allLayout.addView(relativeLayout, index);
    }

    /**
     * 初始化transition动画
     */
    private void setupLayoutTransitions() {
        mTransitioner = new LayoutTransition();
        allLayout.setLayoutTransition(mTransitioner);
        mTransitioner.addTransitionListener(new TransitionListener() {

            @Override
            public void startTransition(LayoutTransition transition,
                                        ViewGroup container, View view, int transitionType) {

            }

            @Override
            public void endTransition(LayoutTransition transition,
                                      ViewGroup container, View view, int transitionType) {
            }
        });
        mTransitioner.setDuration(300);
    }

    /**
     * 图片删除的时候，如果上下方都是EditText，则合并处理
     */
    private void mergeEditText() {
        View preView = allLayout.getChildAt(disappearingImageIndex - 1);
        View nextView = allLayout.getChildAt(disappearingImageIndex);
        if (preView != null && null != nextView && preView instanceof EditText
                && nextView instanceof EditText) {
            EditText preEdit = (EditText) preView;
            EditText nextEdit = (EditText) nextView;
            String str1 = preEdit.getText().toString();
            String str2 = nextEdit.getText().toString();
            String mergeText = "";
            if (str2.length() > 0) {
                mergeText = str1 + "\n" + str2;
            } else {
                mergeText = str1;
            }

            allLayout.setLayoutTransition(null);
            //合并后内容展示在前一个上显示并删除最后一个EditText
            allLayout.removeView(nextEdit);
            preEdit.setText(mergeText);
            preEdit.requestFocus();
            preEdit.setSelection(str1.length(), str1.length());
            allLayout.setLayoutTransition(mTransitioner);
        }
    }

    /**
     * 获得编辑器中图片的数量
     *
     * @return
     */
    public int getImageViewCount() {
        int count = 0;
        int num = allLayout.getChildCount();
        for (int index = 0; index < num; index++) {
            View itemView = allLayout.getChildAt(index);
            if (itemView instanceof RelativeLayout) {
                count++;
            }
        }
        return count;
    }

    /**
     * 生成编辑数据上传
     */
    public List<EditData> buildEditData() {
        List<EditData> dataList = new ArrayList<>();
        int num = allLayout.getChildCount();
        for (int index = 0; index < num; index++) {
            View itemView = allLayout.getChildAt(index);
            EditData itemData = new EditData();
            if (itemView instanceof EditText) {
                EditText item = (EditText) itemView;
                itemData.inputStr = item.getText().toString().trim();
                itemData.tagIndex = (int) itemView.getTag();
            } else if (itemView instanceof RelativeLayout) {
                DataImageView item = (DataImageView) itemView.findViewById(edit_imageView);
                itemData.imagePath = item.getAbsolutePath();
                itemData.bitmap = item.getBitmap();
                itemData.tagIndex = (int) itemView.getTag();
            }
            //如果是本地图片或者网络图片或是文本
            if (null != itemData.bitmap || !TextUtils.isEmpty(itemData.imagePath) || !TextUtils.isEmpty(itemData.inputStr))
                dataList.add(itemData);
        }
        return dataList;
    }

    public static class EditData {
        public String inputStr;
        public String imagePath;
        public Bitmap bitmap;
        public int tagIndex;
    }

    /**
     * 获得待上传图片列表
     *
     * @return
     */
    public ImageQueue<DataImageView> getImageQueue() {
        ImageQueue<DataImageView> iq = new ImageQueue<>();
        for (DataImageView item : imageQueue.list) {
            if (null != item.getBitmap() && !item.getAbsolutePath().contains("http")) {//说明是待上传图片
                iq.add(item);
            }
        }
        return iq;
    }

    /**
     * 判断是否所有图片都上传完
     *
     * @return
     */
    public boolean isAllImageUploaded() {
        boolean uploaded = true;
        int count = allLayout.getChildCount();
        for (int i = 0; i < count; i++) {
            View itemView = allLayout.getChildAt(i);
            if (itemView instanceof RelativeLayout) {
                DataImageView item = (DataImageView) itemView.findViewById(edit_imageView);
                if (!item.isUpload) {
                    uploaded = false;
                }
            }

        }
        return uploaded;
    }

    public static class ImageQueue<T> {

        private LinkedList<T> list;

        public ImageQueue() {
            list = new LinkedList();
        }


        public void add(T t) {
            list.offer(t);
        }

        public void remove(T t) {
            list.remove(t);
        }

        public T remove() {
            return list.poll();
        }

        public int size() {
            return list.size();
        }

    }
}
