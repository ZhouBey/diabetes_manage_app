package com.zpy.diabetes.app.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.siyamed.shapeimageview.CircularImageView;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.zpy.diabetes.app.BaseActivity;
import com.zpy.diabetes.app.R;
import com.zpy.diabetes.app.bean.AppBean;
import com.zpy.diabetes.app.bean.DoctorBean;
import com.zpy.diabetes.app.bean.ImageQiniuBean;
import com.zpy.diabetes.app.bean.ResultBean;
import com.zpy.diabetes.app.config.AppConfig;
import com.zpy.diabetes.app.interf.BaseUIInterf;
import com.zpy.diabetes.app.interf.IAppCommonBeanHolder;
import com.zpy.diabetes.app.interf.IAppUserTokenBeanHolder;
import com.zpy.diabetes.app.util.ActivityUtil;
import com.zpy.diabetes.app.util.ImageTool;
import com.zpy.diabetes.app.util.TextUtil;
import com.zpy.diabetes.app.widget.UploadPhotoDialog;
import com.zpy.diabetes.app.widget.acpf.ACProgressFlower;

import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class DoctorInfoActivity extends BaseActivity implements BaseUIInterf, View.OnClickListener {

    private ActionBar actionBar;
    private ImageView leftImage,
            image_doctor_info_certificate;
    private CircularImageView image_doctors_info_photo;
    private Bundle bundle;
    private DoctorBean doctorBean;
    private TextView tv_doctors_info_name,
            tv_doctors_info_position,
            tv_doctors_info_hospital,
            tv_doctors_info_info;
    private TextView tv_doctor_logout;
    private boolean isLogout;
    private File uploadFile;
    public static final String IMAGE_FILE_NAME = "tangzhushou_doctor_photo";
    private ACProgressFlower loadingDialog;
    private boolean isRefresh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_info);
        init();
        show();
    }

    @Override
    public void init() {
        actionBar = getSupportActionBar();
        ActivityUtil.showActionBar(myActionBar, actionBar, R.mipmap.back, -1, "");
        leftImage = myActionBar.getImageViewLeft();
        leftImage.setOnClickListener(this);
        image_doctor_info_certificate = (ImageView) findViewById(R.id.image_doctor_info_certificate);
        bundle = getIntent().getExtras();
        if (bundle != null) {
            doctorBean = (DoctorBean) bundle.getSerializable("doctor");
            isLogout = bundle.getBoolean("isLogout");
            if (doctorBean != null) {
                myActionBar.setActionBarTitle(doctorBean.getName());
            }
        }
        image_doctors_info_photo = (CircularImageView) findViewById(R.id.image_doctors_info_photo);
        image_doctors_info_photo.setOnClickListener(this);
        tv_doctors_info_name = (TextView) findViewById(R.id.tv_doctors_info_name);
        tv_doctors_info_position = (TextView) findViewById(R.id.tv_doctors_info_position);
        tv_doctors_info_hospital = (TextView) findViewById(R.id.tv_doctors_info_hospital);
        tv_doctors_info_info = (TextView) findViewById(R.id.tv_doctors_info_info);
        tv_doctor_logout = (TextView) findViewById(R.id.tv_doctor_logout);
        if (isLogout) {
            tv_doctor_logout.setVisibility(View.VISIBLE);
            image_doctors_info_photo.setClickable(true);
        } else {
            tv_doctor_logout.setVisibility(View.INVISIBLE);
            image_doctors_info_photo.setClickable(false);
        }
        tv_doctor_logout.setOnClickListener(this);
        loadingDialog = ActivityUtil.getLoadingDialog(this);
        isRefresh = false;
    }

    @Override
    public void show() {
        if (doctorBean != null) {
            x.image().bind(image_doctors_info_photo, AppConfig.QINIU_IMAGE_URL + doctorBean.getPhoto(),
                    new ImageOptions.Builder().setLoadingDrawableId(R.mipmap.img_default_photo_gray).setFailureDrawableId(R.mipmap.img_default_photo_gray).build());
            tv_doctors_info_name.setText(doctorBean.getName());
            tv_doctors_info_position.setText(doctorBean.getPost());
            tv_doctors_info_hospital.setText(doctorBean.getHospital());
            tv_doctors_info_info.setText(doctorBean.getInfo());
            x.image().bind(image_doctor_info_certificate, AppConfig.QINIU_IMAGE_URL + doctorBean.getCertificateImage(), new Callback.CommonCallback<Drawable>() {
                @Override
                public void onSuccess(Drawable result) {
                    Bitmap bitmap = ((BitmapDrawable) result).getBitmap();
                    fitWidth(bitmap, image_doctor_info_certificate);
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    Bitmap bitmap = BitmapFactory.decodeResource(DoctorInfoActivity.this.getResources(), R.drawable.empty_photo);
                    image_doctor_info_certificate.setImageBitmap(bitmap);
                }

                @Override
                public void onCancelled(CancelledException cex) {

                }

                @Override
                public void onFinished() {

                }
            });
        }
    }

    private void fitWidth(Bitmap bitmap, ImageView imageView) {
        int orgWid = bitmap.getWidth();
        int orgHeight = bitmap.getHeight();

        int screenWidth = getApp().getDisplayWidth() - TextUtil.dip2px(this, 40);   // 屏幕宽（像素，如：480px）

        int newWid = screenWidth;
        int newHeight = (int) ((double) newWid / (double) orgWid * (double) orgHeight);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(newWid, newHeight);
        params.setMargins(TextUtil.dip2px(this, 20), TextUtil.dip2px(this, 7), TextUtil.dip2px(this, 20), 0);
        imageView.setLayoutParams(params);
        imageView.setImageBitmap(bitmap);
    }

    @Override
    public void onClick(View v) {
        if (v == leftImage) {
            Intent intent_refresh_account = new Intent(AppConfig.REFRESH_ACCOUNT_ACTION);
            sendBroadcast(intent_refresh_account);
            this.finish();
        }
        if (v == tv_doctor_logout) {
            ActivityUtil.logout(getApp());
            Intent intent_refresh_account = new Intent(AppConfig.REFRESH_ACCOUNT_ACTION);
            sendBroadcast(intent_refresh_account);
            this.finish();
        }
        if (v == image_doctors_info_photo) {
            final UploadPhotoDialog dialog = new UploadPhotoDialog(this, R.style.GrayDialog);
            dialog.getWindow().setGravity(Gravity.CENTER);
            dialog.show();
            TextView tv_upload_from_photo_album = (TextView) dialog.findViewById(R.id.tv_upload_from_photo_album);
            TextView tv_upload_camera = (TextView) dialog.findViewById(R.id.tv_upload_camera);
            TextView tv_upload_photo_cancel = (TextView) dialog.findViewById(R.id.tv_upload_photo_cancel);
            tv_upload_from_photo_album.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    openAlbumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                    startActivityForResult(openAlbumIntent, AppConfig.CROP);
                    dialog.dismiss();
                }
            });
            tv_upload_camera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri imageUri = null;
                    String fileName = null;
                    Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    //删除上一次截图的临时文件
                    SharedPreferences sharedPreferences = getSharedPreferences("temp_doctor", Context.MODE_WORLD_WRITEABLE);
                    ImageTool.deletePhotoAtPathAndName(Environment.getExternalStorageDirectory().getAbsolutePath(), sharedPreferences.getString("tempName", ""));

                    //保存本次截图临时文件名字
                    fileName = String.valueOf(System.currentTimeMillis()) + ".jpg";
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("tempDoctorName", fileName);
                    editor.commit();

                    imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), fileName));
                    //指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
                    openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    startActivityForResult(openCameraIntent, AppConfig.CROP);
                    dialog.dismiss();
                }
            });
            tv_upload_photo_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case AppConfig.CROP:
                    Uri uri = null;
                    if (data != null) {
                        uri = data.getData();
                    } else {
                        String fileName = getSharedPreferences("temp_doctor", Context.MODE_WORLD_WRITEABLE).getString("tempDoctorName", "");
                        uri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), fileName));
                    }
                    Intent intent = ImageTool.cropImage(uri, 200, 200);
                    startActivityForResult(intent, AppConfig.CROP_PICTURE);
                    break;

                case AppConfig.CROP_PICTURE:
                    Bitmap photo = null;
                    Uri photoUri = data.getData();
                    if (photoUri != null) {
                        photo = BitmapFactory.decodeFile(photoUri.getPath());
                    }
                    if (photo == null) {
                        Bundle extra = data.getExtras();
                        if (extra != null) {
                            photo = (Bitmap) extra.get("data");
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        }
                    }
                    String uploadFilePath = Environment.getExternalStorageDirectory().toString();
                    uploadFile = ImageTool.savePhotoToSDCard(photo, uploadFilePath, IMAGE_FILE_NAME);
                    if (uploadFile != null) {
                        loadingDialog.show();
                        getApp().getHttpApi().prepareForUploadImage(new IAppCommonBeanHolder() {
                            @Override
                            public void asynHold(AppBean bean) {
                                if (bean != null) {
                                    if (AppConfig.OK.equals(bean.getCode())) {
                                        ImageQiniuBean qiniuBean = (ImageQiniuBean) bean;
                                        String key = qiniuBean.getQiniuKey();
                                        String token = qiniuBean.getQiniuToken();
                                        getApp().getUploadManager().put(uploadFile, key, token,
                                                new UpCompletionHandler() {
                                                    @Override
                                                    public void complete(final String key, ResponseInfo info,
                                                                         JSONObject res) {
                                                        if (200 == info.statusCode) {
                                                            String token = getApp().getShareDataStr(AppConfig.TOKEN);
                                                            getApp().getHttpApi().afterUploadPhoto
                                                                    (token, key, loadingDialog, new IAppUserTokenBeanHolder() {
                                                                        @Override
                                                                        public void asynHold(AppBean bean) {
                                                                            loadingDialog.dismiss();
                                                                            if (bean != null) {
                                                                                ResultBean resultBean = (ResultBean)
                                                                                        bean;
                                                                                if (AppConfig.OK.equals
                                                                                        (resultBean.getCode())) {
                                                                                    Toast.makeText
                                                                                            (DoctorInfoActivity.this, "上传成功！", Toast.LENGTH_SHORT).show();

                                                                                    x.image().bind(image_doctors_info_photo, AppConfig.QINIU_IMAGE_URL + key, new ImageOptions.Builder().setLoadingDrawableId(R.mipmap.img_default_photo_blue).setFailureDrawableId(R.mipmap.img_default_photo_blue).build());
                                                                                    isRefresh = true;
                                                                                } else {
                                                                                    Toast.makeText
                                                                                            (DoctorInfoActivity.this, resultBean.getMsg(), Toast.LENGTH_SHORT).show();
                                                                                }
                                                                            } else {
                                                                                ActivityUtil.loadError(DoctorInfoActivity.this);
                                                                            }
                                                                        }

                                                                        @Override
                                                                        public void overDue() {
                                                                            ActivityUtil.overdue
                                                                                    (DoctorInfoActivity.this, loadingDialog, true);
                                                                        }
                                                                    });
                                                        } else {
                                                            Toast.makeText(DoctorInfoActivity.this, "头像上传失败!", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                }, null);
                                    } else {
                                        Toast.makeText(DoctorInfoActivity.this, bean.getMsg(),
                                                Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    ActivityUtil.loadError(DoctorInfoActivity.this);
                                }
                            }
                        });
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
