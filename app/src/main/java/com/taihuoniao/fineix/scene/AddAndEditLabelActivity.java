package com.taihuoniao.fineix.scene;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.view.FlowLayout;
import com.taihuoniao.fineix.view.GlobalTitleLayout;

import java.util.ArrayList;

import butterknife.Bind;

/**
 * Created by taihuoniao on 2016/7/15.
 */
public class AddAndEditLabelActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.titlelayout)
    GlobalTitleLayout titlelayout;
    @Bind(R.id.flowlayout)
    FlowLayout flowlayout;
    //    @Bind(R.id.edit_text)
    EditText editText;

    public AddAndEditLabelActivity() {
        super(R.layout.activity_add_edit_label);
    }

    @Override
    protected void initView() {
        titlelayout.setBackgroundResource(R.color.white);
        titlelayout.setBackImgVisible(false);
        titlelayout.setTitle(R.string.add_label, getResources().getColor(R.color.black333333));
        titlelayout.setRightTv(R.string.confirm, getResources().getColor(R.color.black333333), this);
        editText = (EditText) View.inflate(this, R.layout.add_label_edit_text, null);
        editText.setOnClickListener(this);
        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_UP) {
                    for (int i = 0; i < flowlayout.getChildCount(); i++) {
                        if (i == flowlayout.getChildCount() - 1) {
                            continue;
                        }
                        TextView textView = (TextView) flowlayout.getChildAt(i);
                        if (textView.getTag() == null) {
                            continue;
                        }
                        if ((Boolean) textView.getTag()) {
                            flowlayout.removeView(textView);
                            onClick(editText);
                            return true;
                        }
                    }
                    if (editText.getText().toString().length() <= 0 && flowlayout.getChildCount() > 1) {
                        TextView textView = (TextView) flowlayout.getChildAt(flowlayout.getChildCount() - 2);
                        textView.setBackgroundResource(R.drawable.tags_yellow);
                        textView.setTextColor(getResources().getColor(R.color.white));
                        textView.setTag(true);
                        editText.setCursorVisible(false);
                    }
                    return true;
                } else if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    addToFlow(editText.getText().toString(), flowlayout);
                    editText.setText("");
                    editText.setFocusable(true);
                    editText.setFocusableInTouchMode(true);
                    editText.requestFocus();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void initList() {
        if (MainApplication.selectList == null || MainApplication.selectList.size() == 0) {
            flowlayout.addView(editText);
            return;
        }
        for (String label : MainApplication.selectList) {
            addToFlow(label, flowlayout);
        }
    }

    private void addToFlow(String label, final FlowLayout flowLayout) {
        if (flowLayout.getChildCount() >= 1) {
            flowLayout.removeView(editText);
        }
        final TextView textView = (TextView) View.inflate(this, R.layout.view_horizontal_label_item, null);
        textView.setText(label);
        textView.setTag(false);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < flowLayout.getChildCount(); i++) {
                    if (i == flowLayout.getChildCount() - 1) {
                        continue;
                    }
                    TextView textView1 = (TextView) flowLayout.getChildAt(i);
                    textView1.setBackgroundResource(R.drawable.tags_gray);
                    textView1.setTextColor(getResources().getColor(R.color.color_666));
                    textView1.setTag(false);
                }
                textView.setBackgroundResource(R.drawable.tags_yellow);
                textView.setTextColor(getResources().getColor(R.color.white));
                textView.setTag(true);
                editText.setCursorVisible(false);
                ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).showSoftInput(editText, InputMethodManager.SHOW_FORCED);
            }
        });
        flowLayout.addView(textView);
        flowLayout.addView(editText);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit_text:
                editText.setCursorVisible(true);
                for (int i = 0; i < flowlayout.getChildCount(); i++) {
                    if (i == flowlayout.getChildCount() - 1) {
                        continue;
                    }
                    TextView textView1 = (TextView) flowlayout.getChildAt(i);
                    textView1.setBackgroundResource(R.drawable.tags_gray);
                    textView1.setTextColor(getResources().getColor(R.color.color_666));
                }
                break;
            case R.id.title_continue:
                if (MainApplication.selectList != null) {
                    MainApplication.selectList.clear();
                } else {
                    MainApplication.selectList = new ArrayList<>();
                }
                for (int i = 0; i < flowlayout.getChildCount(); i++) {
                    if (i == flowlayout.getChildCount() - 1) {
                        continue;
                    }
                    TextView textView = (TextView) flowlayout.getChildAt(i);
                    MainApplication.selectList.add(textView.getText().toString());
                }
                Intent intent = new Intent();
                setResult(DataConstants.RESULTCODE_CREATESCENE_ADDLABEL, intent);
                finish();
                break;
        }
    }
}
