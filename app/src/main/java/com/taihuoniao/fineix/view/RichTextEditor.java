package com.taihuoniao.fineix.view;
import android.animation.LayoutTransition;
import android.animation.LayoutTransition.TransitionListener;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.utils.ImageUtils;
import com.taihuoniao.fineix.utils.Util;

import java.util.ArrayList;
import java.util.List;

import static com.taihuoniao.fineix.R.dimen.dp5;

/**
 * 亮点编辑器
 */
public class RichTextEditor extends ScrollView {
    public static final String TAG = "RichTextEditor";
    public static final String HINT = "input here";
    private int viewTagIndex = 1; // 新生的view都会打一个tag，对每个view来说，这个tag是唯一的。
    private LinearLayout allLayout; // 这个是所有子view的容器，scrollView内部的唯一一个ViewGroup
    private LayoutInflater inflater;
    private OnKeyListener keyListener; // 所有EditText的软键盘监听器
    private OnClickListener btnListener; // 图片右上角红叉按钮监听器
    private OnFocusChangeListener focusListener; // 所有EditText的焦点监听listener
    private EditText lastFocusEdit; // 最近被聚焦的EditText
    private LayoutTransition mTransitioner; // 只在图片View添加或remove时，触发transition动画
    private int editNormalPadding = 0; //
    private int disappearingImageIndex = 0;

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
        setupLayoutTransitions();
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
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

        // 3. 图片叉掉处理
        btnListener = new OnClickListener() {

            @Override
            public void onClick(View v) {
                RelativeLayout parentView = (RelativeLayout) v.getParent();
                onImageCloseClick(parentView);
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


    private void initEditText() {
        LinearLayout.LayoutParams firstEditParam = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        editNormalPadding = getResources().getDimensionPixelSize(dp5);
        EditText firstEdit = createEditText("", HINT,
                editNormalPadding);
        firstEdit.clearFocus();
        allLayout.addView(firstEdit, firstEditParam);
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
                EditText et=(EditText) view;
                if (TextUtils.isEmpty(et.getText().toString().trim())){//再判断内容是否为空
                    ((EditText) view).setText(content);
                } else {
                    EditText editText = createEditText(content, HINT, getResources().getDimensionPixelSize(dp5));
                    allLayout.addView(editText);
                }
            }else { //不是编辑框直接创建
                EditText editText = createEditText(content, HINT, getResources().getDimensionPixelSize(dp5));
                allLayout.addView(editText);
            }

        }

    }

    /**
     * 生成文本输入框
     */
    private EditText createEditText(String content, String hint, int paddingTop) {
        EditText editText = (EditText) inflater.inflate(R.layout.edit_item, null);
        editText.setOnKeyListener(keyListener);
        editText.setTag(viewTagIndex++);
        editText.setPadding(editNormalPadding, paddingTop, editNormalPadding, 0);
        editText.setHint(hint);
        editText.setText(content);
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
        View closeView = layout.findViewById(R.id.image_close);
        closeView.setTag(layout.getTag());
        closeView.setOnClickListener(btnListener);
        return layout;
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
        index = allLayout.getChildCount();
        Bitmap bitmap = ImageUtils.getSmallBitmap(imagePath);
        addImageViewAtIndex(index, bitmap, imagePath);
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
     * 在特定位置添加ImageView
     */
    private void addImageViewAtIndex(final int index, Bitmap bmp,
                                     String imagePath) {
        final RelativeLayout relativeLayout = createImageLayout();
        DataImageView imageView = (DataImageView) relativeLayout
                .findViewById(R.id.edit_imageView);
        imageView.setImageBitmap(bmp);
        imageView.setBitmap(bmp);
        imageView.setAbsolutePath(imagePath);
        // 调整imageView的高度
        int imageHeight = Util.getScreenWidth() * bmp.getHeight() / bmp.getWidth();
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, imageHeight);
        imageView.setLayoutParams(lp);

//		 onActivityResult无法触发动画，此处post处理
//        allLayout.postDelayed(new Runnable() {
//            @Override
//            public void run() { //在指定index位置添加View
        allLayout.addView(relativeLayout, index);
        allLayout.invalidate();
//            }
//        }, 200);
    }

    /**
     * 根据view的宽度，动态缩放bitmap尺寸
     *
     * @param width view的宽度
     */
//    private Bitmap getScaledBitmap(String filePath, int width) {
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inJustDecodeBounds = true;
//        BitmapFactory.decodeFile(filePath, options);
//        //如果原图的宽度大于RichEditor宽度
//        int sampleSize = options.outWidth > width ? options.outWidth / width
//                + 1 : 1;
//        options.inJustDecodeBounds = false;
//        options.inSampleSize = sampleSize;
//        return BitmapFactory.decodeFile(filePath, options);
//    }

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
                if (!transition.isRunning()
                        && transitionType == LayoutTransition.CHANGE_DISAPPEARING) {
                    // transition动画结束，合并EditText
                    // mergeEditText();
                }
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
     * 对外提供的接口, 生成编辑数据上传
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
            } else if (itemView instanceof RelativeLayout) {
                DataImageView item = (DataImageView) itemView
                        .findViewById(R.id.edit_imageView);
                itemData.imagePath = item.getAbsolutePath();
                itemData.bitmap = item.getBitmap();
            }
            if (null != itemData.bitmap || !TextUtils.isEmpty(itemData.inputStr))
                dataList.add(itemData);
        }

        return dataList;
    }

    public static class EditData {
        public String inputStr;
        public String imagePath;
        public Bitmap bitmap;
    }
}
