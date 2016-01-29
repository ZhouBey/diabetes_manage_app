package com.zpy.diabetes.app.fragment;

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
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.zpy.diabetes.app.R;
import com.zpy.diabetes.app.bean.AppBean;
import com.zpy.diabetes.app.bean.ImageQiniuBean;
import com.zpy.diabetes.app.bean.ResultBean;
import com.zpy.diabetes.app.config.AppConfig;
import com.zpy.diabetes.app.interf.BaseUIInterf;
import com.zpy.diabetes.app.interf.IAppCommonBeanHolder;
import com.zpy.diabetes.app.interf.IAppUserTokenBeanHolder;
import com.zpy.diabetes.app.my.MyCommonCallbackForDrawable;
import com.zpy.diabetes.app.ui.RegisterForDoctorActivity;
import com.zpy.diabetes.app.util.ActivityUtil;
import com.zpy.diabetes.app.util.ImageTool;
import com.zpy.diabetes.app.util.TextUtil;
import com.zpy.diabetes.app.widget.LineEditText;
import com.zpy.diabetes.app.widget.SelectDialog;
import com.zpy.diabetes.app.widget.UploadPhotoDialog;
import com.zpy.diabetes.app.widget.acpf.ACProgressFlower;

import org.json.JSONObject;
import org.xutils.x;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DoctorRegisterTwoFragment extends Fragment implements BaseUIInterf, View.OnClickListener {

    private View rootView;
    private RegisterForDoctorActivity activity;
    private LineEditText et_register_doctor_password,
            et_register_doctor_confirm_password,
            et_register_doctor_hospital,
            et_register_doctor_position,
            et_register_doctor_name;
    private RelativeLayout layout_select_sex,
            layout_select_birthday;
    private TextView tv_doctor_select_sex,
            tv_doctor_select_birthday,
            tv_register_doctor;
    private int year, month, day;
    private ImageView image_upload_doctor_certificate;
    private File uploadFile;
    private ACProgressFlower loadingDialog;
    public static final String IMAGE_FILE_NAME = "tangzhushou_doctor_photocertificate";
    private EditText et_doctor_info;
    private String phone,password,hospital,position,sex,birthday,d_info,name;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.doctor_register_fragment_two, container, false);
        } else {
            ViewGroup viewGroup = (ViewGroup) rootView.getParent();
            viewGroup.removeView(rootView);
        }
        init();
        show();
        return rootView;
    }

    @Override
    public void init() {
        activity = (RegisterForDoctorActivity) getActivity();
        et_register_doctor_password = (LineEditText) rootView.findViewById(R.id.et_register_doctor_password);
        et_register_doctor_confirm_password = (LineEditText) rootView.findViewById(R.id.et_register_doctor_confirm_password);
        et_register_doctor_hospital = (LineEditText) rootView.findViewById(R.id.et_register_doctor_hospital);
        et_register_doctor_position = (LineEditText) rootView.findViewById(R.id.et_register_doctor_position);
        layout_select_sex = (RelativeLayout) rootView.findViewById(R.id.layout_select_sex);
        layout_select_sex.setOnClickListener(this);
        tv_doctor_select_sex = (TextView) rootView.findViewById(R.id.tv_doctor_select_sex);
        layout_select_birthday = (RelativeLayout) rootView.findViewById(R.id.layout_select_birthday);
        layout_select_birthday.setOnClickListener(this);
        tv_doctor_select_birthday = (TextView) rootView.findViewById(R.id.tv_doctor_select_birthday);
        image_upload_doctor_certificate = (ImageView) rootView.findViewById(R.id.image_upload_doctor_certificate);
        image_upload_doctor_certificate.setOnClickListener(this);
        loadingDialog = ActivityUtil.getLoadingDialog(activity);
        tv_register_doctor = (TextView) rootView.findViewById(R.id.tv_register_doctor);
        tv_register_doctor.setOnClickListener(this);
        et_doctor_info = (EditText) rootView.findViewById(R.id.et_doctor_info);
        et_register_doctor_name = (LineEditText) rootView.findViewById(R.id.et_register_doctor_name);
    }

    @Override
    public void show() {

    }

    @Override
    public void onClick(View v) {
        if (v == layout_select_sex) {
            showSelectSex();
        }
        if (v == layout_select_birthday) {
            final DatePickerDialog datePickerDialog = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    tv_doctor_select_birthday.setText(String.valueOf(year) + "-" + String.valueOf(monthOfYear + 1) + "-" + String.valueOf(dayOfMonth));
                }
            }, year, month, day);
            datePickerDialog.show();
        }
        if (v == image_upload_doctor_certificate) {
            final UploadPhotoDialog dialog = new UploadPhotoDialog(activity, R.style.GrayDialog);
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
                    SharedPreferences sharedPreferences = activity.getSharedPreferences("temp", Context.MODE_WORLD_WRITEABLE);
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
        if (v == tv_register_doctor) {
            if(prepareForRegister()) {
                loadingDialog.show();
                activity.getApp().getHttpApi().prepareForUploadImage(new IAppCommonBeanHolder() {
                    @Override
                    public void asynHold(AppBean bean) {
                        if (bean != null) {
                            if (AppConfig.OK.equals(bean.getCode())) {
                                ImageQiniuBean qiniuBean = (ImageQiniuBean) bean;
                                String key = qiniuBean.getQiniuKey();
                                String token = qiniuBean.getQiniuToken();
                                activity.getApp().getUploadManager().put(uploadFile, key, token,
                                        new UpCompletionHandler() {
                                            @Override
                                            public void complete(final String key, ResponseInfo info,
                                                                 final JSONObject res) {
                                                if (200 == info.statusCode) {
                                                    activity.getApp().getHttpApi().doctorRegister(name,phone, password, hospital, key, d_info, position, sex, birthday, loadingDialog, new IAppCommonBeanHolder() {
                                                        @Override
                                                        public void asynHold(AppBean bean) {
                                                            if(bean!=null) {
                                                                ResultBean resultBean = (ResultBean) bean;
                                                                if(AppConfig.OK.equals(resultBean.getCode())) {
                                                                    Toast.makeText(activity,"注册成功，请等待管理员的审核",Toast.LENGTH_SHORT).show();
                                                                    activity.finish();
                                                                } else {
                                                                    Toast.makeText(activity,resultBean.getMsg(),Toast.LENGTH_SHORT).show();
                                                                }
                                                            } else {
                                                                ActivityUtil.loadError(activity);
                                                            }
                                                        }
                                                    });
                                                } else {
                                                    Toast.makeText(activity, "注册失败!", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }, null);
                            } else {
                                Toast.makeText(activity, bean.getMsg(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            ActivityUtil.loadError(activity);
                        }
                    }
                });
            }

        }
    }

    private boolean prepareForRegister() {
        name = et_register_doctor_name.getText().toString();
        if(TextUtil.isEmpty(name)) {
            Toast.makeText(activity, "姓名不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        phone = activity.getPhone();
        if (TextUtil.isEmpty(phone)) {
            Toast.makeText(activity, "手机号不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        password = et_register_doctor_password.getText().toString();
        if (TextUtil.isEmpty(password)) {
            Toast.makeText(activity, "密码不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        String passwordConfirm = et_register_doctor_confirm_password.getText().toString();
        if (TextUtil.isEmpty(passwordConfirm)) {
            Toast.makeText(activity, "请再次输入密码核对", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!password.equals(passwordConfirm)) {
            Toast.makeText(activity, "两次密码输入不一致", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!TextUtil.passwordCheck(password)) {
            Toast.makeText(activity, "密码为6到20位数字和字母组合", Toast.LENGTH_SHORT).show();
            return false;
        }
        hospital = et_register_doctor_hospital.getText().toString();
        if (TextUtil.isEmpty(hospital)) {
            Toast.makeText(activity, "请输入您所在的医院", Toast.LENGTH_SHORT).show();
            return false;
        }
        position = et_register_doctor_position.getText().toString();
        if (TextUtil.isEmpty(position)) {
            Toast.makeText(activity, "请输入您的职位", Toast.LENGTH_SHORT).show();
            return false;
        }
        sex = tv_doctor_select_sex.getText().toString();
        if (TextUtil.isEmpty(sex)) {
            Toast.makeText(activity, "请选择您的性别", Toast.LENGTH_SHORT).show();
            return false;
        }
        birthday = tv_doctor_select_birthday.getText().toString();
        if (TextUtil.isEmpty(birthday)) {
            Toast.makeText(activity, "请选择您的生日", Toast.LENGTH_SHORT).show();
            return false;
        }
        d_info = et_doctor_info.getText().toString();
        if (TextUtil.isEmpty(d_info)) {
            Toast.makeText(activity, "请填写您的简介", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (uploadFile == null) {
            Toast.makeText(activity, "请上传您的资质证明", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    public void showSelectSex() {
        final SelectDialog dialog = new SelectDialog(activity, R.style.CustomDialog);
        dialog.show();
        TextView tv_select_title = (TextView) dialog.findViewById(R.id.tv_select_title);
        ListView listview_select_content = (ListView) dialog.findViewById(R.id.listview_select_content);
        tv_select_title.setVisibility(View.GONE);
        dialog.findViewById(R.id.view_line_select).setVisibility(View.GONE);
        final List<String> list = new ArrayList<>();
        list.add("男");
        list.add("女");
        ArrayAdapter adapterBank = new ArrayAdapter(activity, R.layout.select_list_item, R.id.tv_select_item_content, list);
        listview_select_content.setAdapter(adapterBank);
        listview_select_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tv_doctor_select_sex.setText(list.get(position));
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == activity.RESULT_OK) {
            switch (requestCode) {
                case AppConfig.CROP:
                    Uri uri = null;
                    if (data != null) {
                        uri = data.getData();
                    } else {
                        String fileName = activity.getSharedPreferences("temp", Context.MODE_WORLD_WRITEABLE).getString("tempName", "");
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
                        image_upload_doctor_certificate.setImageBitmap(photo);
                    }
                    break;
                default:
                    break;
            }
        }
    }


}
