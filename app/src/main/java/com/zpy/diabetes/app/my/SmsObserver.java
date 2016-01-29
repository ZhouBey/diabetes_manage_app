package com.zpy.diabetes.app.my;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;

import com.zpy.diabetes.app.config.AppConfig;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SmsObserver extends ContentObserver {
    private Context context;
    private Handler handler;

    public SmsObserver(Context context, Handler handler) {
        super(handler);
        this.context = context;
        this.handler = handler;
    }

    @Override
    public void onChange(boolean selfChange, Uri uri) {
        super.onChange(selfChange, uri);
        String uriStr = uri.toString();
        if ("content://sms/raw".equals(uriStr)) {
            return;
        }
        Uri inboxUri = Uri.parse("content://sms/inbox");
        Cursor cursor = context.getContentResolver().query(inboxUri, null, null, null, "date desc");
        if (cursor == null) {
            return;
        }
        if (cursor.moveToFirst()) {
            String address = cursor.getString(cursor.getColumnIndex("address"));
            String body = cursor.getString(cursor.getColumnIndex("body"));
            if ("10657120610111".equals(address)) {
                Message msg = handler.obtainMessage();
                msg.what = AppConfig.READ_MSG;
                Pattern pattern = Pattern.compile("(\\d{4})");
                Matcher matcher = pattern.matcher(body);
                if (matcher.find()) {
                    msg.obj = matcher.group(0);
                    handler.sendMessage(msg);
                }
            }
        }
    }
}
