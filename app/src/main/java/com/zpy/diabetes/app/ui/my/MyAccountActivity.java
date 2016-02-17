package com.zpy.diabetes.app.ui.my;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.siyamed.shapeimageview.CircularImageView;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.zpy.diabetes.app.BaseActivity;
import com.zpy.diabetes.app.R;
import com.zpy.diabetes.app.bean.AppBean;
import com.zpy.diabetes.app.bean.ImageQiniuBean;
import com.zpy.diabetes.app.bean.ResultBean;
import com.zpy.diabetes.app.bean.SuffererBean;
import com.zpy.diabetes.app.config.AppConfig;
import com.zpy.diabetes.app.interf.BaseUIInterf;
import com.zpy.diabetes.app.interf.IAppCommonBeanHolder;
import com.zpy.diabetes.app.interf.IAppUserTokenBeanHolder;
import com.zpy.diabetes.app.my.MyCommonCallbackForDrawable;
import com.zpy.diabetes.app.util.ActivityUtil;
import com.zpy.diabetes.app.util.ImageTool;
import com.zpy.diabetes.app.util.TextUtil;
import com.zpy.diabetes.app.widget.InputDialog;
import com.zpy.diabetes.app.widget.SelectDialog;
import com.zpy.diabetes.app.widget.UploadPhotoDialog;
import com.zpy.diabetes.app.widget.acpf.ACProgressFlower;

import org.json.JSONObject;
import org.xutils.common.util.LogUtil;
import org.xutils.x;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MyAccountActivity extends BaseActivity implements BaseUIInterf, View.OnClickListener {

    private ActionBar actionBar;
    private ImageView imageLeft;
    private CircularImageView image_my_account_info_photo;
    private RelativeLayout layout_account_info_name,
            layout_account_info_phone,
            layout_account_info_sex,
            layout_account_info_birthday,
            layout_account_info_suffer_date;
    private TextView tv_account_info_birthday,
            tv_account_info_suffer_date,
            tv_account_info_sex,
            tv_account_info_name,
            tv_account_info_phone,
            tv_logout;
    private Calendar calendar;
    private int year, month, day;
    private Bundle bundle;
    private SuffererBean suffererBean;
    private String name, phone, sex, suffer_date, birthday, photo;
    private String newName, newSex, newSuffererDate, newBirthday;
    //    private boolean isUpdate;
    private ACProgressFlower loadingDialog;
    private boolean isRefresh;
    private File uploadFile;
    public static final String IMAGE_FILE_NAME = "tangzhushou_sufferer_photo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_account_info);
        init();
        show();
    }

    @Override
    public void init() {
        actionBar = getSupportActionBar();
        ActivityUtil.showActionBar(myActionBar, actionBar, R.mipmap.back, -1, "我的账户");
        imageLeft = myActionBar.getImageViewLeft();
        imageLeft.setOnClickListener(this);
        bundle = getIntent().getExtras();
        suffererBean = (SuffererBean) bundle.getSerializable("sufferer");
        image_my_account_info_photo = (CircularImageView) findViewById(R.id.image_my_account_info_photo);
        image_my_account_info_photo.setOnClickListener(this);
        layout_account_info_name = (RelativeLayout) findViewById(R.id.layout_account_info_name);
        layout_account_info_name.setOnClickListener(this);
        layout_account_info_phone = (RelativeLayout) findViewById(R.id.layout_account_info_phone);
        layout_account_info_sex = (RelativeLayout) findViewById(R.id.layout_account_info_sex);
        layout_account_info_sex.setOnClickListener(this);
        layout_account_info_birthday = (RelativeLayout) findViewById(R.id.layout_account_info_birthday);
        layout_account_info_birthday.setOnClickListener(this);
        layout_account_info_suffer_date = (RelativeLayout) findViewById(R.id.layout_account_info_suffer_date);
        layout_account_info_suffer_date.setOnClickListener(this);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        tv_account_info_birthday = (TextView) findViewById(R.id.tv_account_info_birthday);
        tv_account_info_suffer_date = (TextView) findViewById(R.id.tv_account_info_suffer_date);
        tv_account_info_sex = (TextView) findViewById(R.id.tv_account_info_sex);
        tv_account_info_name = (TextView) findViewById(R.id.tv_account_info_name);
        tv_account_info_phone = (TextView) findViewById(R.id.tv_account_info_phone);
        tv_logout = (TextView) findViewById(R.id.tv_logout);
        tv_logout.setOnClickListener(this);
//        isUpdate = false;
        isRefresh = false;
        loadingDialog = ActivityUtil.getLoadingDialog(this);
    }

    @Override
    public void show() {
        if (suffererBean != null) {
            name = suffererBean.getName();
            if (!"null".equals(name)) {
                tv_account_info_name.setText(String.valueOf(name));
            }
            phone = suffererBean.getPhone();
            if (!"null".equals(phone)) {
                tv_account_info_phone.setText(String.valueOf(phone));
            }
            sex = TextUtil.getSexStr(suffererBean.getSex());
            if (!"null".equals(sex)) {
                tv_account_info_sex.setText(String.valueOf(sex));
            }
            birthday = suffererBean.getBirthday();
            if (!"1900-01-01".equals(birthday)) {
                tv_account_info_birthday.setText(String.valueOf(birthday));
            } else {
                birthday = "";
            }
            suffer_date = suffererBean.getSufferedDate();
            if (!"1900-01-01".equals(suffer_date)) {
                tv_account_info_suffer_date.setText(String.valueOf(suffer_date));
            } else {
                suffer_date = "";
            }
            photo = AppConfig.QINIU_IMAGE_URL + suffererBean.getPhoto();
            x.image().bind(image_my_account_info_photo, photo, new MyCommonCallbackForDrawable(this, image_my_account_info_photo, R.mipmap.img_default_phone_blue));
        }
    }

    @Override
    public void onClick(View v) {
        if (v == imageLeft) {
            if (isRefresh) {
                this.setResult(AppConfig.REFRESH_ACCOUNT_RESULT);
            }
            this.finish();
        }
        if (v == image_my_account_info_photo) {
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
                    SharedPreferences sharedPreferences = getSharedPreferences("temp", Context.MODE_WORLD_WRITEABLE);
                    ImageTool.deletePhotoAtPathAndName(Environment.getExternalStorageDirectory().getAbsolutePath(), sharedPreferences.getString("tempName", ""));

                    //保存本次截图临时文件名字
                    fileName = String.valueOf(System.currentTimeMillis()) + ".jpg";
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("tempName", fileName);
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
        if (v == layout_account_info_name) {
            showInputNameDialog();
        }
        if (v == layout_account_info_sex) {
            showSelectSex();
        }
        if (v == layout_account_info_birthday) {
            showDialog(AppConfig.BIRTHDAY_SELECT);
        }

        if (v == layout_account_info_suffer_date) {
            showDialog(AppConfig.SUFFER_DATE_SELECT);
        }
        if (v == tv_logout) {
            ActivityUtil.logout(getApp());
            this.setResult(AppConfig.REFRESH_ACCOUNT_RESULT);
            this.finish();
        }

    }

    private void showInputNameDialog() {
        final InputDialog dialog = new InputDialog(this, R.style.CustomDialog);
        dialog.show();
        TextView tv_input_title = (TextView) dialog.findViewById(R.id.tv_input_title);
        final EditText et_input_content = (EditText) dialog.findViewById(R.id.et_input_content);
        TextView tv_input_submit = (TextView) dialog.findViewById(R.id.tv_input_submit);
        final TextView tv_input_error_message = (TextView) dialog.findViewById(R.id.tv_input_error_message);
        tv_input_title.setText("姓名");
        et_input_content.setHint("请输入您的姓名");
        tv_input_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = et_input_content.getText().toString();
                if (TextUtil.isEmpty(content)) {
                    tv_input_error_message.setVisibility(View.VISIBLE);
                    tv_input_error_message.setText("请输入姓名");
                    return;
                }
                if (TextUtil.checkChinese(content)) {
                    dialog.dismiss();
                    update("name", content, tv_account_info_name);
                } else {
                    tv_input_error_message.setVisibility(View.VISIBLE);
                    tv_input_error_message.setText("请填写中文姓名");
                }
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == AppConfig.BIRTHDAY_SELECT) {
            return new DatePickerDialog(this, birthdayDateSetListener, year, month, day);
        }
        if (id == AppConfig.SUFFER_DATE_SELECT) {
            return new DatePickerDialog(this, sufferDateSetListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener birthdayDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
            String mouth = arg2 + 1 > 9 ? String.valueOf(arg2 + 1) : ("0" + String.valueOf(arg2 + 1));
            String day = arg3 > 9 ? String.valueOf(arg3) : ("0" + arg3);
            String date = String.valueOf(arg1) + "-" + mouth + "-" + day;
            update("birthday", date, tv_account_info_birthday);
        }
    };

    private DatePickerDialog.OnDateSetListener sufferDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
            String mouth = arg2 + 1 > 9 ? String.valueOf(arg2 + 1) : ("0" + String.valueOf(arg2 + 1));
            String day = arg3 > 9 ? String.valueOf(arg3) : ("0" + arg3);
            String date = String.valueOf(arg1) + "-" + mouth + "-" + day;
            update("sufferDate", date, tv_account_info_suffer_date);
        }
    };

    public void showSelectSex() {
        final SelectDialog dialog = new SelectDialog(this, R.style.CustomDialog);
        dialog.show();
        TextView tv_select_title = (TextView) dialog.findViewById(R.id.tv_select_title);
        ListView listview_select_content = (ListView) dialog.findViewById(R.id.listview_select_content);
        tv_select_title.setVisibility(View.GONE);
        dialog.findViewById(R.id.view_line_select).setVisibility(View.GONE);
        final List<String> list = new ArrayList<>();
        list.add("男");
        list.add("女");
        ArrayAdapter adapterBank = new ArrayAdapter(this, R.layout.select_list_item, R.id.tv_select_item_content, list);
        listview_select_content.setAdapter(adapterBank);
        listview_select_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String sex = list.get(position);
                dialog.dismiss();
                update("sex", sex, tv_account_info_sex);
            }
        });
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            LogUtil.e(String.valueOf(isRefresh));
            if (isRefresh) {
                this.setResult(AppConfig.REFRESH_ACCOUNT_RESULT);
                this.finish();
            }
        }
        return super.onKeyDown(keyCode, event);
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
                        String fileName = getSharedPreferences("temp", Context.MODE_WORLD_WRITEABLE).getString("tempName", "");
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
                                                                                            (MyAccountActivity.this, "上传成功！", Toast.LENGTH_SHORT).show();
                                                                                    isRefresh = true;

                                                                                    x.image().bind(image_my_account_info_photo, AppConfig.QINIU_IMAGE_URL + key, new MyCommonCallbackForDrawable(MyAccountActivity.this, image_my_account_info_photo, R.mipmap.img_default_phone_blue));
                                                                                } else {
                                                                                    Toast.makeText
                                                                                            (MyAccountActivity.this, resultBean.getMsg(), Toast.LENGTH_SHORT).show();
                                                                                }
                                                                            } else {
                                                                                ActivityUtil.loadError(MyAccountActivity.this);
                                                                            }
                                                                        }

                                                                        @Override
                                                                        public void overDue() {
                                                                            ActivityUtil.overdue
                                                                                    (MyAccountActivity.this, loadingDialog, true);
                                                                        }
                                                                    });
                                                        } else {
                                                            Toast.makeText(MyAccountActivity.this, "头像上传失败!", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                }, null);
                                    } else {
                                        Toast.makeText(MyAccountActivity.this, bean.getMsg(),
                                                Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    ActivityUtil.loadError(MyAccountActivity.this);
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

    private void update(String key, final String value, final TextView textView) {
        String token = getApp().getShareDataStr(AppConfig.TOKEN);
        if (!TextUtil.isEmpty(token)) {
            loadingDialog.show();
            getApp().getHttpApi().updateSuffererInfo(token, key, value, loadingDialog, new IAppUserTokenBeanHolder() {
                @Override
                public void asynHold(AppBean bean) {
                    if (bean != null) {
                        ResultBean resultBean = (ResultBean) bean;
                        if (AppConfig.OK.equals(resultBean.getCode())) {
                            Toast.makeText(MyAccountActivity.this, "保存成功！", Toast.LENGTH_SHORT).show();
                            textView.setText(value);
                            isRefresh = true;
                        } else {
                            Toast.makeText(MyAccountActivity.this, resultBean.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        ActivityUtil.loadError(MyAccountActivity.this);
                    }
                }

                @Override
                public void overDue() {
                    ActivityUtil.overdue(MyAccountActivity.this, loadingDialog, false);
                }
            });
        }
    }
}
