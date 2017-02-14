package com.taihuoniao.fineix.adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.user.bean.OrderDetailBean;
import com.taihuoniao.fineix.utils.GlideUtils;

import java.util.HashMap;
import java.util.List;

/**
 * Created by android on 2016/3/21.
 */
public class EvaluateAdapter extends THNBaseAdapter {
    private String editContent = "";
    //定义一个HashMap，用来存放EditText的值，Key是position
    HashMap<Integer, String> hashMap = new HashMap<>();
    HashMap<Integer, String> hashMapRatingBar = new HashMap<>();
    private LayoutInflater inflater = null;
    private List<OrderDetailBean.ItemsBean> list = null;
    private OnTwoClickedListener listener = null;//用来传星条数和评价内容两个

    public interface OnTwoClickedListener {
        void onLetterCliced(HashMap<Integer, String> hashMapRatingBar, HashMap<Integer, String> hashMap);
    }

    public void setOnTwoClickedListener(OnTwoClickedListener listener) {
        // 如果监听到了的话，这一句就使得顶上全局变量listener不再为空了
        this.listener = listener;
    }


    public EvaluateAdapter(List<OrderDetailBean.ItemsBean> list, Context context) {
        super(list, context);
        this.list = list;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getItemView(final int position, View convertView, ViewGroup parent) {
        ViewHolder mHolder;
        if (convertView == null) {
            mHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_publish_evaluate, parent, false);
            mHolder.mRatingBar = (RatingBar) convertView.findViewById(R.id.ratingbar_evaluate);
            mHolder.mEditText = (EditText) convertView.findViewById(R.id.et_evaluate);
            mHolder.mImage = (ImageView) convertView.findViewById(R.id.image_evaluate);
            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }
        GlideUtils.displayImage(list.get(position).getCover_url(), mHolder.mImage);
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void afterTextChanged(Editable s) {
                //将editText中改变的值设置的HashMap中
                hashMap.put(position, s + "");
                listener.onLetterCliced(hashMapRatingBar, hashMap);
            }
        };
        mHolder.mEditText.addTextChangedListener(textWatcher);
        hashMapRatingBar.put(position, 5 + "");
        if (listener != null) {
            listener.onLetterCliced(hashMapRatingBar, hashMap);
        }
        mHolder.mRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                int num = (int) ratingBar.getRating();
                if (num < 1) {
                    ratingBar.setRating(1);
                }
                switch ((int) rating) {
                    case 1:
                        hashMapRatingBar.put(position, 1 + "");
                        if (listener != null) {
                            listener.onLetterCliced(hashMapRatingBar, hashMap);
                        }
                        break;
                    case 2:
                        hashMapRatingBar.put(position, 2 + "");
                        if (listener != null) {
                            listener.onLetterCliced(hashMapRatingBar, hashMap);
                        }
                        break;
                    case 3:
                        hashMapRatingBar.put(position, 3 + "");
                        if (listener != null) {
                            listener.onLetterCliced(hashMapRatingBar, hashMap);
                        }
                        break;
                    case 4:
                        hashMapRatingBar.put(position, 4 + "");
                        if (listener != null) {
                            listener.onLetterCliced(hashMapRatingBar, hashMap);
                        }
                        break;
                    case 5:
                        hashMapRatingBar.put(position, 5 + "");
                        if (listener != null) {
                            listener.onLetterCliced(hashMapRatingBar, hashMap);
                        }
                        break;
                    default:
                        break;
                }
            }
        });
        return convertView;
    }

    class ViewHolder {
        private RatingBar mRatingBar;
        private EditText mEditText;
        private ImageView mImage;
    }
}
