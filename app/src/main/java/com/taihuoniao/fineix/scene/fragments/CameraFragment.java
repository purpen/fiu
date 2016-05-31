package com.taihuoniao.fineix.scene.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseFragment;
import com.taihuoniao.fineix.beans.PhotoItem;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.utils.ImageUtils;
import com.taihuoniao.fineix.view.GlobalTitleLayout;
import com.taihuoniao.fineix.view.WaittingDialog;
import com.taihuoniao.fineix.view.svprogress.SVProgressHUD;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by taihuoniao on 2016/3/14.
 */
public class CameraFragment extends BaseFragment implements View.OnClickListener, View.OnTouchListener {
    // 最小预览界面的分辨率
    private static final int MIN_PREVIEW_PIXELS = 480 * 320;
    private static final int FOCUS = 0;
    private static final int ZOOM = 1;
    //    private ImageView titleCancel;
//    private ImageView titleFlash;
    private GlobalTitleLayout titleLayout;
    private SurfaceView surfaceView;
    private RelativeLayout bottomRelative;
    private Button takePicture;
    private View focus;
    private WaittingDialog dialog;
    //相机工具
    private Camera cameraInst;
    private Camera.Parameters parameters;
    private Camera.Size adapterSize = null;
    private Camera.Size previewSize = null;
    private float pointX, pointY;//点击预览界面时的坐标
    private int mode;//判断点击预览界面的状态 FOCUS聚焦 ZOOM放大或缩小
    private float dist;//两点间距
    //放大缩小
    private int curZoomValue = 0;
    private Handler handler = new Handler();
    private Bundle bundle;//存储图片
//    private int PHOTO_SIZE = 2000;


    @Override
    protected void requestNet() {
    }

    @Override
    protected void initList() {
        titleLayout.setTitle("拍照");
        titleLayout.setBackListener(this);
        titleLayout.setContinueTvVisible(false);
        titleLayout.setFlashImgListtener(this);
        bottomRelative.setOnClickListener(this);
        takePicture.setOnClickListener(this);
        surfaceView.setOnClickListener(this);
        surfaceView.setOnTouchListener(this);
    }

    @Override
    protected View initView() {
        View view = View.inflate(getActivity(), R.layout.fragment_camera, null);
        titleLayout = (GlobalTitleLayout) view.findViewById(R.id.fragment_camera_titlelayout);
        surfaceView = (SurfaceView) view.findViewById(R.id.fragment_camera_surface);
        bottomRelative = (RelativeLayout) view.findViewById(R.id.fragment_camera_bottomrelative);
        takePicture = (Button) view.findViewById(R.id.fragment_camera_takepic);
        focus = view.findViewById(R.id.fragment_camera_focus);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceHolder.setKeepScreenOn(true);
        surfaceView.setFocusable(true);
        surfaceView.setBackgroundColor(android.content.ComponentCallbacks2.TRIM_MEMORY_BACKGROUND);
        surfaceView.getHolder().addCallback(new SurfaceCallback());
        dialog = new WaittingDialog(getActivity());
        return view;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                getActivity().finish();
                break;
            case R.id.title_flash:
                turnLight(cameraInst);
                break;
            case R.id.fragment_camera_bottomrelative:
                //防止焦点放生在此区域
                break;
            case R.id.fragment_camera_takepic:
                try {
                    cameraInst.takePicture(new Camera.ShutterCallback() {
                        @Override
                        public void onShutter() {

                        }
                    }, null, new Camera.PictureCallback() {
                        @Override
                        public void onPictureTaken(byte[] data, Camera camera) {
                            bundle = new Bundle();
                            bundle.putByteArray("bytes", data); //将图片字节数据保存在bundle当中，实现数据交换
                            new SavePicTask(data).execute();
//                            camera.startPreview(); // 拍完照后，重新开始预览
                        }
                    });
                } catch (Throwable t) {
                    t.printStackTrace();
                    Toast.makeText(getActivity(), "拍照失败，请返回重试！", Toast.LENGTH_SHORT).show();
                    try {
                        cameraInst.startPreview();
                    } catch (Throwable e) {

                    }
                }
                break;
            case R.id.fragment_camera_surface:
                try {
                    pointFocus((int) pointX, (int) pointY);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                RelativeLayout.LayoutParams layout = new RelativeLayout.LayoutParams(focus.getLayoutParams());
                layout.setMargins((int) pointX - 60, (int) pointY - 60, 0, 0);
                focus.setLayoutParams(layout);
                focus.setVisibility(View.VISIBLE);
                ScaleAnimation sa = new ScaleAnimation(3f, 1f, 3f, 1f,
                        ScaleAnimation.RELATIVE_TO_SELF, 0.5f, ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
                sa.setDuration(800);
                focus.startAnimation(sa);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        focus.setVisibility(View.INVISIBLE);
                    }
                }, 800);
                break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            // 主点按下
            case MotionEvent.ACTION_DOWN:
                pointX = event.getX();
                pointY = event.getY();
                mode = FOCUS;
                break;
            // 副点按下
            case MotionEvent.ACTION_POINTER_DOWN:
                dist = spacing(event);
                // 如果连续两点距离大于10，则判定为多点模式
                if (spacing(event) > 10f) {
                    mode = ZOOM;
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
                mode = FOCUS;
                break;
            case MotionEvent.ACTION_MOVE:
                if (mode == ZOOM) {
                    float newDist = spacing(event);
                    if (newDist > 10f) {
                        float tScale = (newDist - dist) / dist;
                        if (tScale < 0) {
                            tScale = tScale * 10;
                        }
                        addZoomIn((int) tScale);
                    }
                }
                break;
        }
        return false;
    }

    private void initCamera() {
        parameters = cameraInst.getParameters();
        parameters.setPictureFormat(PixelFormat.JPEG);
//        if (adapterSize == null) {
        setUpPicSize(parameters);
        setUpPreviewSize(parameters);
//        }
        if (adapterSize != null) {
            parameters.setPictureSize(adapterSize.width, adapterSize.height);
        }
        if (previewSize != null) {
            parameters.setPreviewSize(previewSize.width, previewSize.height);

//            parameters.setPreviewSize(surfaceView.getWidth(), surfaceView.getHeight());
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) surfaceView.getLayoutParams();
            if (previewSize.width != 0) {
                lp.width = surfaceView.getWidth();
                lp.height = lp.width * previewSize.width / previewSize.height;
                surfaceView.setLayoutParams(lp);
                Log.e("<<<最终的预览参数", "width=" + previewSize.width + ",height=" + previewSize.height + ",surface.width="
                        + surfaceView.getWidth() + ",surface.height=" + surfaceView.getHeight());
            }
        }


        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);//1连续对焦
//        cameraInst.setDisplayOrientation(180);
        setDispaly(parameters, cameraInst);
        try {
            cameraInst.setParameters(parameters);
        } catch (Exception e) {
            e.printStackTrace();
        }
        cameraInst.startPreview();
        cameraInst.cancelAutoFocus();// 2如果要实现连续的自动对焦，这一句必须加上
    }

    private void setUpPicSize(Camera.Parameters parameters) {

        if (adapterSize != null) {
            return;
        } else {
            adapterSize = findBestPictureResolution();
            return;
        }
    }

    private void setUpPreviewSize(Camera.Parameters parameters) {

        if (previewSize != null) {
            return;
        } else {
            previewSize = findBestPreviewResolution();
//            previewSize = getPreviews();
//            Log.e("<<<", "预览像素=" + previewSize.height + "," + previewSize.width + ",预览界面大小=" + surfaceView.getHeight() + "," + surfaceView.getWidth());
        }
    }


    //控制图像的正确显示方向
    private void setDispaly(Camera.Parameters parameters, Camera camera) {
        setDisplayOrientation(camera, 90);
    }

    //实现的图像的正确显示
    private void setDisplayOrientation(Camera camera, int i) {
        Method downPolymorphic;
        try {
            downPolymorphic = camera.getClass().getMethod("setDisplayOrientation",
                    int.class);
            if (downPolymorphic != null) {
                Log.e("<<<", "调整方向");
                downPolymorphic.invoke(camera, i);
            }
        } catch (Exception e) {
            Log.e("<<<", "图像出错");
        }
    }

    /**
     * 找出最适合的预览界面分辨率
     *
     * @return
     */
    private Camera.Size findBestPreviewResolution() {
        //保证宽大于高
        boolean isPortrait = surfaceView.getWidth() < surfaceView.getHeight();//判断手机是不是竖屏
        int currentWidht = isPortrait ? surfaceView.getHeight() : surfaceView.getWidth();
        int currentHeight = isPortrait ? surfaceView.getWidth() : surfaceView.getHeight();
        Camera.Parameters cameraParameters = cameraInst.getParameters();
        Camera.Size defaultPreviewResolution = cameraParameters.getPreviewSize();
        List<Camera.Size> rawSupportedSizes = cameraParameters.getSupportedPreviewSizes();
        if (rawSupportedSizes == null) {
            return defaultPreviewResolution;
        }

        // 按照分辨率从大到小排序
        List<Camera.Size> supportedPreviewResolutions = new ArrayList<Camera.Size>(rawSupportedSizes);
        Collections.sort(supportedPreviewResolutions, new Comparator<Camera.Size>() {
            @Override
            public int compare(Camera.Size a, Camera.Size b) {
                int aPixels = a.height * a.width;
                int bPixels = b.height * b.width;
                if (bPixels < aPixels) {
                    return -1;
                }
                if (bPixels > aPixels) {
                    return 1;
                }
                return 0;
            }
        });

//        StringBuilder previewResolutionSb = new StringBuilder();
//        for (Camera.Size supportedPreviewResolution : supportedPreviewResolutions) {
//            previewResolutionSb.append(supportedPreviewResolution.width).append('x').append(supportedPreviewResolution.height)
//                    .append(' ');
//        }
//        Log.e("<<<支持的预览界面", "Supported preview resolutions: " + previewResolutionSb);
        //如果有正好的尺寸直接返回
        for (Camera.Size size : supportedPreviewResolutions) {
            if (size.width == currentWidht && size.height == currentHeight) {
                return size;
            }
        }
        //如果没有的话找一个比例差最小的返回
        //临时size值
        Camera.Size nowSize = defaultPreviewResolution;
        //最小差值
        double minCha = Double.MAX_VALUE;
        //surfaceview的宽高比
        double surfaceB = ((double) currentWidht) / ((double) currentHeight);
        for (Camera.Size size : supportedPreviewResolutions) {
            //获得当前size的宽高比
            double sizeB = ((double) size.width) / ((double) size.height);

            if (size.height <= currentHeight && Math.abs(sizeB - surfaceB) < minCha) {
                minCha = Math.abs(sizeB - surfaceB);
                nowSize = size;
            }
        }
        return nowSize;

    }

    private Camera.Size findBestPictureResolution() {
        Camera.Parameters cameraParameters = cameraInst.getParameters();
        List<Camera.Size> supportedPicResolutions = cameraParameters.getSupportedPictureSizes(); // 至少会返回一个值

//        StringBuilder picResolutionSb = new StringBuilder();
//        for (Camera.Size supportedPicResolution : supportedPicResolutions) {
//            picResolutionSb.append(supportedPicResolution.width).append('x')
//                    .append(supportedPicResolution.height).append(" ");
//        }


        Camera.Size defaultPictureResolution = cameraParameters.getPictureSize();
        Log.e("<<<", "default picture resolution " + defaultPictureResolution.width + "x"
                + defaultPictureResolution.height);

        // 排序
        List<Camera.Size> sortedSupportedPicResolutions = new ArrayList<Camera.Size>(
                supportedPicResolutions);
        Collections.sort(sortedSupportedPicResolutions, new Comparator<Camera.Size>() {
            @Override
            public int compare(Camera.Size a, Camera.Size b) {
                int aPixels = a.height * a.width;
                int bPixels = b.height * b.width;
                if (bPixels < aPixels) {
                    return -1;
                }
                if (bPixels > aPixels) {
                    return 1;
                }
                return 0;
            }
        });
        // 如果没有找到合适的，并且还有候选的像素，对于照片，则取其中最大比例的，而不是选择与屏幕分辨率相同的
        if (!sortedSupportedPicResolutions.isEmpty()) {
            Log.e("<<<拍照图片最大像素", "预览像素=" + sortedSupportedPicResolutions.get(0).height + "," + sortedSupportedPicResolutions.get(0).width
                    + ",预览界面大小=" + surfaceView.getHeight() + "," + surfaceView.getWidth());
            return sortedSupportedPicResolutions.get(0);
        }

        // 没有找到合适的，就返回默认的
        Log.e("<<<拍照图片默认像素", "预览像素=" + defaultPictureResolution.height + "," + defaultPictureResolution.width
                + ",预览界面大小=" + surfaceView.getHeight() + "," + surfaceView.getWidth());
        return defaultPictureResolution;
    }

    //实现自动对焦
    private void autoFocus() {
        new Thread() {
            @Override
            public void run() {
                try {
                    sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (cameraInst == null) {
                    return;
                }
                cameraInst.autoFocus(new Camera.AutoFocusCallback() {
                    @Override
                    public void onAutoFocus(boolean success, Camera camera) {
                        if (success) {
                            initCamera();//实现相机的参数初始化
                        }
                    }
                });
            }
        };
    }

    //两点间距
    private float spacing(MotionEvent event) {
        if (event == null) {
            return 0;
        }
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return FloatMath.sqrt(x * x + y * y);
    }

    private void addZoomIn(int delta) {
        try {
            Camera.Parameters params = cameraInst.getParameters();
            if (!params.isZoomSupported()) {
                return;
            }
            curZoomValue += delta;
            if (curZoomValue < 0) {
                curZoomValue = 0;
            } else if (curZoomValue > params.getMaxZoom()) {
                curZoomValue = params.getMaxZoom();
            }

            if (!params.isSmoothZoomSupported()) {
                params.setZoom(curZoomValue);
                cameraInst.setParameters(params);
            } else {
                cameraInst.startSmoothZoom(curZoomValue);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //定点对焦的代码
    private void pointFocus(int x, int y) {
        cameraInst.cancelAutoFocus();
        parameters = cameraInst.getParameters();
        showPoint(x, y);
        cameraInst.setParameters(parameters);
        autoFocus();
    }

    private void showPoint(int x, int y) {
        if (parameters.getMaxNumMeteringAreas() > 0) {
            List<Camera.Area> areas = new ArrayList<Camera.Area>();
            //xy变换了
            int rectY = -x * 2000 / MainApplication.getContext().getScreenWidth() + 1000;
            int rectX = y * 2000 / MainApplication.getContext().getScreenHeight() - 1000;

            int left = rectX < -900 ? -1000 : rectX - 100;
            int top = rectY < -900 ? -1000 : rectY - 100;
            int right = rectX > 900 ? 1000 : rectX + 100;
            int bottom = rectY > 900 ? 1000 : rectY + 100;
            Rect area1 = new Rect(left, top, right, bottom);
            areas.add(new Camera.Area(area1, 800));
            parameters.setMeteringAreas(areas);
        }

        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
    }

    private void turnLight(Camera mCamera) {
        if (mCamera == null || mCamera.getParameters().getSupportedFlashModes() == null) {
            return;
        }
        Camera.Parameters parameters = mCamera.getParameters();
        String flashMode = mCamera.getParameters().getFlashMode();
        List<String> supportedModes = mCamera.getParameters().getSupportedFlashModes();
        if (Camera.Parameters.FLASH_MODE_OFF.equals(flashMode)
                && supportedModes.contains(Camera.Parameters.FLASH_MODE_ON)) {//关闭状态
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
            mCamera.setParameters(parameters);
            titleLayout.setFlashImgResource(R.mipmap.flash_on);
        } else if (Camera.Parameters.FLASH_MODE_ON.equals(flashMode) && supportedModes.contains(Camera.Parameters.FLASH_MODE_OFF)) {//开启状态
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            titleLayout.setFlashImgResource(R.mipmap.flash_off);
            mCamera.setParameters(parameters);
        }
    }

    private BitmapFactory.Options options = null;

    private String saveToSDCard(byte[] data) throws IOException {
        Bitmap croppedImage;

        //获得图片大小
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(data, 0, data.length, options);
        this.options = options;
        options.inJustDecodeBounds = false;
        Rect r = new Rect(0, 0, options.outWidth, options.outHeight);
        try {
            croppedImage = decodeRegionCrop(data, r);
            Log.e("<<<", "拍照保存的图片=" + croppedImage.getWidth() + "," + croppedImage.getHeight());
        } catch (Exception e) {
            return null;
        }
        String imagePath = ImageUtils.saveToFile(MainApplication.systemPhotoPath, true,
                croppedImage);
        croppedImage.recycle();
        return imagePath;
    }

    private Bitmap decodeRegionCrop(byte[] data, Rect rect) {
        InputStream is = null;
//        System.gc();
        Bitmap croppedImage = null;
        try {
            is = new ByteArrayInputStream(data);
            BitmapRegionDecoder decoder = BitmapRegionDecoder.newInstance(is, false);
            try {
                croppedImage = decoder.decodeRegion(rect, new BitmapFactory.Options());
            } catch (IllegalArgumentException e) {
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        Matrix m = new Matrix();
        m.setRotate(90, options.outWidth, options.outHeight);
        Log.e("<<<图片大小", "图片=" + data.length);
        Bitmap rotatedImage = null;
        try {
            rotatedImage = Bitmap.createBitmap(croppedImage, 0, 0, options.outWidth, options.outHeight, m, true);
        } catch (OutOfMemoryError e) {
            System.gc();
            Log.e("<<<内存溢出", e.toString());
        }

        if (rotatedImage != croppedImage && croppedImage != null)
            croppedImage.recycle();
        return rotatedImage;
    }

    private class SurfaceCallback implements SurfaceHolder.Callback {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            if (null == cameraInst) {
                Log.e("<<<", "surfaceCreate");
                try {
                    cameraInst = Camera.open();
                    cameraInst.setPreviewDisplay(holder);
                    initCamera();

                    cameraInst.startPreview();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            Log.e("<<<", "surfaceChanged" + ",width=" + width + ",height=" + height + ",surfaceView.width=" + surfaceView.getWidth()
                    + ",surfaceView.height=" + surfaceView.getHeight());
            autoFocus();
            Log.e("<<<显示出来的预览尺寸", ">>>width=" + cameraInst.getParameters().getPreviewSize().width + ",height=" + cameraInst.getParameters().getPreviewSize().height);
//            parameters.setPreviewSize(surfaceView.getWidth(), surfaceView.getHeight());
//            parameters.setPictureSize(surfaceView.getWidth(), surfaceView.getHeight());
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            Log.e("<<<", "surfaceDestroyed");
            try {
                if (cameraInst != null) {
                    cameraInst.stopPreview();
                    cameraInst.release();
                    cameraInst = null;
                }
            } catch (Exception e) {
                Log.e("<<<", "相机已经关闭");
            }

        }
    }

    private class SavePicTask extends AsyncTask<Void, Void, String> {
        private byte[] data;

        SavePicTask(byte[] data) {
            this.data = data;
        }

        protected void onPreExecute() {
            dialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                return saveToSDCard(data);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            dialog.dismiss();
            if (result != null) {
                ImageUtils.processPhotoItem(getActivity(), new PhotoItem(result, System.currentTimeMillis()));
            } else {
                new SVProgressHUD(getActivity()).showErrorWithStatus("拍照失败");
                cameraInst.startPreview();
            }
        }
    }

}
