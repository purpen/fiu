package com.taihuoniao.fineix.scene;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
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
    @Bind(R.id.edit_text)
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
    }

    @Override
    protected void initList() {
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setCursorVisible(true);
                for (int i = 0; i < flowlayout.getChildCount(); i++) {
                    TextView textView1 = (TextView) flowlayout.getChildAt(i);
                    textView1.setBackgroundResource(R.drawable.tags_gray);
                    textView1.setTextColor(getResources().getColor(R.color.color_666));
                }
            }
        });
        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_UP) {
                    for(int i=0;i<flowlayout.getChildCount();i++){
                        TextView textView = (TextView) flowlayout.getChildAt(i);
                        if((Boolean)textView.getTag()){
                            flowlayout.removeView(textView);
                        }
                    }
                    return true;
                }
                return false;
            }
        });
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (AddAndEditLabelActivity.this.getCurrentFocus() != null) {
                        ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(AddAndEditLabelActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                    addToFlow(editText.getText().toString(), flowlayout);
                    editText.setText("");
                    return true;
                }
                return false;
            }
        });
        if (MainApplication.selectList == null) {
            return;
        }
        for (String label : MainApplication.selectList) {
            addToFlow(label, flowlayout);
        }
    }

    private void addToFlow(String label, final FlowLayout flowLayout) {
        final TextView textView = (TextView) View.inflate(this, R.layout.view_horizontal_label_item, null);
        textView.setText(label);
        textView.setTag(false);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < flowLayout.getChildCount(); i++) {
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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_continue:
                if (MainApplication.selectList != null) {
                    MainApplication.selectList.clear();
                } else {
                    MainApplication.selectList = new ArrayList<>();
                }
                for (int i = 0; i < flowlayout.getChildCount(); i++) {
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