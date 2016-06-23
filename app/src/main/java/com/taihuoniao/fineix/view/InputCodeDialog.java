package com.taihuoniao.fineix.view;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;

import com.taihuoniao.fineix.R;

/**
 * @author lilin
 *         created at 2016/6/22 10:51
 */
public class InputCodeDialog extends DialogFragment {
    private OnCommitClickListener listener;
    public interface OnCommitClickListener {
        void execute(View v, EditText et);
    }

    public void setOnCommitClickListener(OnCommitClickListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        final View view = inflater.inflate(R.layout.dialog_input_code, container);
        view.findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.execute(v, (EditText) view.findViewById(R.id.et_code));
                }
            }
        });
        return view;
    }
}
