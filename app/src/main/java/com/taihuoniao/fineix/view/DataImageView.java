package com.taihuoniao.fineix.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

public class DataImageView extends AppCompatImageView {
	
	private String absolutePath;

	private Bitmap bitmap;

	public boolean isUpload; //判断是否上传完毕

	public DataImageView(Context context) {
		this(context, null);
	}

	public DataImageView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public DataImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public String getAbsolutePath() {
		return absolutePath;
	}

	public void setAbsolutePath(String absolutePath) {
		this.absolutePath = absolutePath;
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

}
