package com.taihuoniao.fineix.scene.fragments;

import android.app.Activity;
import android.content.Intent;
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
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseFragment;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.scene.PictureEditActivity;
import com.taihuoniao.fineix.utils.DensityUtils;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.view.GlobalTitleLayout;
import com.taihuoniao.fineix.view.dialog.WaittingDialog;
import com.yanzhenjie.permission.AndPermission;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by taihuoniao on 2016/3/14.
 */
public class CameraFragment extends BaseFragment implements View.OnClickListener, View.OnTouchListener {
    private static final int REQUEST_CODE = 100;
    private static final int FOCUS = 0;
    private static final int ZOOM = 1;
    private GlobalTitleLayout titleLayout;
    private SurfaceView surfaceView;
    private RelativeLayout bottomRelative;
    private ImageView takePicture;
    private View focus;
    private ImageView flashImg;
    private ImageView selfTakeImg;
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

    @Override
    protected void requestNet() {
    }

    @Override
    protected void initList() {
        titleLayout.setTitle("拍照");
        titleLayout.setBackImgVisible(false);
        titleLayout.setCancelImgVisible(true);
        titleLayout.setContinueTvVisible(false);
        bottomRelative.setOnClickListener(this);
        takePicture.setOnClickListener(this);
        surfaceView.setOnClickListener(this);
        surfaceView.setOnTouchListener(this);
        flashImg.setOnClickListener(this);
        selfTakeImg.setOnClickListener(this);
    }

    @Override
    protected View initView() {
        View view = View.inflate(getActivity(), R.layout.fragment_camera, null);
        flashImg = (ImageView) view.findViewById(R.id.flash);
        selfTakeImg = (ImageView) view.findViewById(R.id.self_take);
        titleLayout = (GlobalTitleLayout) view.findViewById(R.id.fragment_camera_titlelayout);
        surfaceView = (SurfaceView) view.findViewById(R.id.fragment_camera_surface);
        RelativeLayout.LayoutParams lp1 = (RelativeLayout.LayoutParams) surfaceView.getLayoutParams();
        lp1.width = MainApplication.getContext().getScreenWidth();
        lp1.height = (int) ((double) lp1.width * 4 / 3);
        surfaceView.setLayoutParams(lp1);
//        Log.e("<<<最终改变的surfaceView尺寸", "width=" + surfaceView.getMeasuredWidth() + ",height=" + surfaceView.getMeasuredHeight());
        bottomRelative = (RelativeLayout) view.findViewById(R.id.fragment_camera_bottomrelative);
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) bottomRelative.getLayoutParams();
        lp.height = MainApplication.getContext().getScreenHeight() - DensityUtils.dp2px(getActivity(), 90) - MainApplication.getContext().getScreenWidth();
        bottomRelative.setLayoutParams(lp);
        takePicture = (ImageView) view.findViewById(R.id.fragment_camera_takepic);
        focus = view.findViewById(R.id.fragment_camera_focus);
//        if (AndPermission.hasPermission(activity, Manifest.permission.CAMERA)){
//            requestCameraPermission();
//        } else {
//             AndPermission.with(this)
//                    .requestCode(REQUEST_CODE)
//                    .permission(Manifest.permission.CAMERA)
//                    .send();
//        }
        requestCameraPermission();
        dialog = new WaittingDialog(getActivity());
        return view;
    }

    private void requestCameraPermission(){
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceHolder.setKeepScreenOn(true);
        surfaceView.setFocusable(true);
        surfaceView.setBackgroundColor(android.content.ComponentCallbacks2.TRIM_MEMORY_BACKGROUND);
        SurfaceCallback surfaceCallback = new SurfaceCallback();
        surfaceView.getHolder().addCallback(surfaceCallback);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // 只需要调用这一句，第一个参数是当前Acitivity/Fragment，回调方法写在当前Activity/Framgent。
        AndPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

//    // 成功回调的方法，用注解即可，里面的数字是请求时的requestCode。
//    @PermissionYes(REQUEST_CODE)
//    private void getRequestYes(List<String> grantedPermissions) {
//        requestCameraPermission();
//        LogUtil.e("getRequestYes");
//    }

//    // 失败回调的方法，用注解即可，里面的数字是请求时的requestCode。
//    @PermissionNo(REQUEST_CODE)
//    private void getPhoneStatusNo(List<String> deniedPermissions) {
//        // 用户否勾选了不再提示并且拒绝了权限，那么提示用户到设置中授权。
//        if (AndPermission.hasAlwaysDeniedPermission(this, deniedPermissions)) {
//            // 第一种：用默认的提示语。
//            AndPermission.defaultSettingDialog(this,REQUEST_CODE_SETTING).show();
//        }else {
//            activity.finish();
//        }
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.self_take:
                if (cameraInst!=null){
                    cameraInst.stopPreview();//停掉原来摄像头的预览
                    cameraInst.release();//释放资源
                    cameraInst = null;//取消原来摄像头
                }
                if (camera_facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                    camera_facing = Camera.CameraInfo.CAMERA_FACING_FRONT;
                } else {
                    camera_facing = Camera.CameraInfo.CAMERA_FACING_BACK;
                }
//                Log.e("<<<", "surfaceCreate");
                try {
                    int numberOfCameras = Camera.getNumberOfCameras();
                    Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
                    for (int i = 0; i < numberOfCameras; i++) {
                        Camera.getCameraInfo(i, cameraInfo);
                        if (cameraInfo.facing == camera_facing) {
                            cameraInst = Camera.open(i);
                            cameraId = i;
                        }
                    }
                    cameraInst.setPreviewDisplay(surfaceHolder);
                    initCamera();

                    cameraInst.startPreview();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                break;
            case R.id.title_back:
                getActivity().finish();
                break;
            case R.id.flash:
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
                        }
                    });
                } catch (Throwable t) {
                    t.printStackTrace();
                    ToastUtils.showError("拍照失败，请返回重试!");
                    try {
                        cameraInst.startPreview();
                    } catch (Throwable e) {
                        e.printStackTrace();
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

    private int cameraId;

    private void initCamera() {
        parameters = cameraInst.getParameters();
        parameters.setPictureFormat(PixelFormat.JPEG);
        setUpPreviewSize();//选择预览尺寸
        setUpPicSize();//选择图片尺寸
        if (adapterSize != null) {
            parameters.setPictureSize(adapterSize.width, adapterSize.height);
        }
        if (previewSize != null) {
            parameters.setPreviewSize(previewSize.width, previewSize.height);

        }
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);//1连续对焦
        setCameraDisplayOrientation(getActivity(), cameraId, cameraInst);
        try {
            cameraInst.setParameters(parameters);
        } catch (Exception e) {
            e.printStackTrace();
        }
        cameraInst.startPreview();
        cameraInst.cancelAutoFocus();// 2如果要实现连续的自动对焦，这一句必须加上
    }

    private void setUpPicSize() {
        if (adapterSize != null) {
            return;
        }
        adapterSize = findBestPictureResolution();
    }

    private void setUpPreviewSize() {
        if (previewSize != null) {
            return;
        }
        previewSize = findBestPreviewResolution();
    }

    public static void setCameraDisplayOrientation(Activity activity,
                                                   int cameraId, android.hardware.Camera camera) {
        android.hardware.Camera.CameraInfo info =
                new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        int degrees;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
            default:
                degrees = 0;
                break;
        }
        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }

    /**
     * 找出最适合的预览界面分辨率
     *
     * @return
     */
    private Camera.Size findBestPreviewResolution() {
        Camera.Parameters cameraParameters = cameraInst.getParameters();
        Camera.Size defaultPreviewResolution = cameraParameters.getPreviewSize();
        List<Camera.Size> rawSupportedSizes = cameraParameters.getSupportedPreviewSizes();
        if (rawSupportedSizes == null) {
            return defaultPreviewResolution;
        }
        // 按照分辨率从大到小排序
        List<Camera.Size> supportedPreviewResolutions = new ArrayList<>(rawSupportedSizes);
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
        StringBuilder previewResolutionSb = new StringBuilder();
        for (Camera.Size supportedPreviewResolution : supportedPreviewResolutions) {
            previewResolutionSb.append("width=").append(supportedPreviewResolution.width).append('x').append("height=").append(supportedPreviewResolution.height)
                    .append(",");
        }
        //如果有正好的尺寸直接返回
        for (Camera.Size size : supportedPreviewResolutions) {
            if ((double) size.width / (double) size.height <= (double) surfaceView.getMeasuredHeight() / (double) surfaceView.getMeasuredWidth() && (double) size.width / (double) size.height >= 1) {
                return size;
            }
        }
        //如果没有的话找一个比例差最小的返回
        //临时size值
        Camera.Size nowSize = defaultPreviewResolution;
        //最小差值
        double minCha = Double.MAX_VALUE;
        //surfaceview的宽高比
        double surfaceB = ((double) surfaceView.getMeasuredWidth()) / ((double) surfaceView.getMeasuredHeight());
        for (Camera.Size size : supportedPreviewResolutions) {
            //获得当前size的宽高比
            double sizeB = ((double) size.height) / ((double) size.width);
            if (Math.abs(sizeB - surfaceB) < minCha) {
                minCha = Math.abs(sizeB - surfaceB);
                nowSize = size;
            }
        }
        return nowSize;

    }

    //    发现问题。surfaceview，预览尺寸，图片尺寸的比例应保持一致。否侧会产生变形
    private Camera.Size findBestPictureResolution() {
        Camera.Parameters cameraParameters = cameraInst.getParameters();
        Camera.Size defaultPictureResolution = cameraParameters.getPictureSize();
        List<Camera.Size> supportedPicResolutions = cameraParameters.getSupportedPictureSizes(); // 至少会返回一个值
        if (supportedPicResolutions == null) {
            return defaultPictureResolution;
        }
        // 排序
        List<Camera.Size> sortedSupportedPicResolutions = new ArrayList<>(supportedPicResolutions);
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
        StringBuilder previewResolutionSb = new StringBuilder();
        for (Camera.Size supportedPreviewResolution : sortedSupportedPicResolutions) {
            previewResolutionSb.append("width=").append(supportedPreviewResolution.width).append('x').append("height=").append(supportedPreviewResolution.height)
                    .append(",");
        }
        if (previewSize != null) {
            //先找到与预览尺寸比例一致的
            for (Camera.Size size : sortedSupportedPicResolutions) {
                if (size.width * previewSize.height == size.height * previewSize.width) {
                    return size;
                }
            }
            //如果没有与预览尺寸一致的，找与预览尺寸比例差最小的
            //临时size值
            Camera.Size nowSize = previewSize;
            //最小差值
            double minCha = Double.MAX_VALUE;
            //预览尺寸的宽高比
            double preB = ((double) previewSize.height) / ((double) previewSize.width);
            for (Camera.Size size : sortedSupportedPicResolutions) {
                //获得当前size的宽高比
                double sizeB = ((double) size.height) / ((double) size.width);
                if (Math.abs(sizeB - preB) < minCha) {
                    minCha = Math.abs(sizeB - preB);
                    nowSize = size;
                }
            }
            return nowSize;
        } else {
            //找到与surfaceview比例一致的
            for (Camera.Size size : sortedSupportedPicResolutions) {
                if ((double) size.width / (double) size.height <= (double) surfaceView.getMeasuredHeight() / (double) surfaceView.getMeasuredWidth()
                        && (double) size.width / (double) size.height >= 1) {
                    return size;
                }
            }
            //如果没有的话找一个比例差最小的返回
            //临时size值
            Camera.Size nowSize = defaultPictureResolution;
            //最小差值
            double minCha = Double.MAX_VALUE;
            //surfaceview的宽高比
            double surfaceB = ((double) surfaceView.getMeasuredWidth()) / ((double) surfaceView.getMeasuredHeight());
            for (Camera.Size size : sortedSupportedPicResolutions) {
                //获得当前size的宽高比
                double sizeB = ((double) size.height) / ((double) size.width);
                if (Math.abs(sizeB - surfaceB) < minCha) {
                    minCha = Math.abs(sizeB - surfaceB);
                    nowSize = size;
                }
            }
            return nowSize;
        }
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
        return (float) Math.sqrt(x * x + y * y);
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
            List<Camera.Area> areas = new ArrayList<>();
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
            flashImg.setImageResource(R.mipmap.flash_on);
        } else if (Camera.Parameters.FLASH_MODE_ON.equals(flashMode) && supportedModes.contains(Camera.Parameters.FLASH_MODE_OFF)) {//开启状态
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            flashImg.setImageResource(R.mipmap.flash_off);
            mCamera.setParameters(parameters);
        }
    }

    private BitmapFactory.Options options = null;

    private Bitmap saveToSDCard(byte[] data) throws IOException {
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
        } catch (Exception e) {
            return null;
        }
//        String imagePath = ImageUtils.saveToFile(MainApplication.systemPhotoPath, true,
//                croppedImage);
//        croppedImage.recycle();
        return croppedImage;
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
                e.printStackTrace();
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
        if (camera_facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
            m.setRotate(90, options.outWidth, options.outHeight);
        } else {
            m.setScale(-1, 1);
            m.preRotate(-90, options.outWidth, options.outHeight);
//            m.setTranslate();
        }
        int w, h;
        if (options.outWidth < options.outHeight) {
            w = options.outWidth;
            h = options.outWidth;
        } else {
            w = options.outHeight;
            h = options.outHeight;
        }
        Bitmap rotatedImage = null;
        try {
            if (camera_facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                rotatedImage = Bitmap.createBitmap(croppedImage, 0, 0, w, h, m, true);
            } else {
                if (options.outHeight < options.outWidth) {
                    rotatedImage = Bitmap.createBitmap(croppedImage, options.outWidth - options.outHeight, 0, w, h, m, true);
                } else {
                    rotatedImage = Bitmap.createBitmap(croppedImage, options.outHeight - options.outWidth, 0, w, h, m, true);
                }
            }
        } catch (OutOfMemoryError e) {
            System.gc();
//            Log.e("<<<内存溢出", e.toString());
        }

        if (rotatedImage != croppedImage && croppedImage != null)
            croppedImage.recycle();
        return rotatedImage;
    }

    private int camera_facing = Camera.CameraInfo.CAMERA_FACING_BACK;
    private SurfaceHolder surfaceHolder;

    private class SurfaceCallback implements SurfaceHolder.Callback {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            surfaceHolder = holder;
            if (null == cameraInst) {
                try {
                    int numberOfCameras = Camera.getNumberOfCameras();
                    Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
                    for (int i = 0; i < numberOfCameras; i++) {
                        Camera.getCameraInfo(i, cameraInfo);
                        if (cameraInfo.facing == camera_facing) {
                            cameraInst = Camera.open(i);
                            cameraId = i;
                        }
                    }
                    cameraInst.setPreviewDisplay(holder);
                    initCamera();

                    cameraInst.startPreview();
                } catch (Throwable e) {
                    ToastUtils.showError("相机打开失败，请重试");
                    getActivity().finish();
                }
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
//            Log.e("<<<", "surface=height=" + surfaceView.getHeight() + ",width=" + surfaceView.getWidth());
//            Log.e("<<<", "预览尺寸=height=" + previewSize.height + ",width=" + previewSize.width);
//            Log.e("<<<", "图片尺寸=height=" + adapterSize.height + ",width=" + adapterSize.width);
//            Log.e("<<<", "surfaceChanged" + ",width=" + width + ",height=" + height + ",surfaceView.width=" + surfaceView.getWidth()
//                    + ",surfaceView.height=" + surfaceView.getHeight());
            autoFocus();
//            Log.e("<<<显示出来的预览尺寸", ">>>width=" + cameraInst.getParameters().getPreviewSize().width + ",height=" + cameraInst.getParameters().getPreviewSize().height);
//            parameters.setPreviewSize(surfaceView.getWidth(), surfaceView.getHeight());
//            parameters.setPictureSize(previewSize.width, previewSize.height);
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
//            Log.e("<<<", "surfaceDestroyed");
            try {
                if (cameraInst != null) {
                    cameraInst.stopPreview();
                    cameraInst.release();
                    cameraInst = null;
                }
            } catch (Exception e) {
            }

        }
    }

    private class SavePicTask extends AsyncTask<Void, Void, Bitmap> {
        private byte[] data;

        SavePicTask(byte[] data) {
            this.data = data;
        }

        protected void onPreExecute() {
            if (!dialog.isShowing()) {
                dialog.show();
            }
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            try {
                return saveToSDCard(data);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            dialog.dismiss();
            if (result != null) {
                //剪切完的图片
                MainApplication.cropBitmap = result;
//                ImageUtils.location = ImageUtils.picLocation(result.getImageUri());
                Intent intent = new Intent(activity, PictureEditActivity.class);
                activity.startActivity(intent);
//                ImageUtils.processPhotoItem(getActivity(), new PhotoItem(result, System.currentTimeMillis()));
            } else {
                ToastUtils.showError("拍照失败");
                cameraInst.startPreview();
            }
        }
    }

}
