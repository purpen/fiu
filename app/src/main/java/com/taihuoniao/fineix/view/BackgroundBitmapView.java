package com.taihuoniao.fineix.view;

import android.os.Parcel;
import android.text.ParcelableSpan;
import android.text.TextPaint;
import android.text.style.CharacterStyle;
import android.text.style.UpdateAppearance;

/**
 * Created by taihuoniao on 2016/6/7.
 */
public class BackgroundBitmapView extends CharacterStyle
        implements UpdateAppearance, ParcelableSpan {
    private int drawble;

    public BackgroundBitmapView(int drawble) {
        this.drawble = drawble;
    }

    @Override
    public void updateDrawState(TextPaint tp) {
        tp.bgColor = drawble;
    }

    @Override
    public int getSpanTypeId() {
        return 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(drawble);
    }
}
