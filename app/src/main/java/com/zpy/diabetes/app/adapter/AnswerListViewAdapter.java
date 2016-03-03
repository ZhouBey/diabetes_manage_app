package com.zpy.diabetes.app.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.CircularImageView;
import com.zpy.diabetes.app.R;

import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.List;
import java.util.Map;

public class AnswerListViewAdapter<T> extends ArrayAdapter<T> {
    private Context context;
    private int resource;
    private List list;

    public AnswerListViewAdapter(Context context, int resource, List<T> objects) {
        super(context, resource, objects);
        this.context = context;
        this.list = objects;
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        if (convertView == null) {
            view = View.inflate(context, resource, null);
        } else {
            view = convertView;
        }
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        if (viewHolder == null) {
            viewHolder = new ViewHolder();
            viewHolder.tv_answer_item_doctor_phone = (TextView) view.findViewById(R.id.tv_answer_item_doctor_phone);
            viewHolder.tv_answer_item_time = (TextView) view.findViewById(R.id.tv_answer_item_time);
            viewHolder.tv_answer_item_content = (TextView) view.findViewById(R.id.tv_answer_item_content);
            viewHolder.image_answer_item_doctor_photo = (CircularImageView) view.findViewById(R.id.image_answer_item_doctor_photo);
            view.setTag(viewHolder);
        }
        viewHolder.setAttrs((Map<String, String>) list.get(position));
        return view;
    }

    class ViewHolder {
        private TextView tv_answer_item_doctor_phone,
                tv_answer_item_time,
                tv_answer_item_content;
        private CircularImageView image_answer_item_doctor_photo;

        public void setAttrs(Map<String, String> item) {
            tv_answer_item_time.setText(item.get("answer_time"));
            tv_answer_item_content.setText(item.get("answer_content"));
            tv_answer_item_doctor_phone.setText(item.get("answer_phone"));
            image_answer_item_doctor_photo.setImageResource(R.mipmap.img_default_photo_gray);
            x.image().bind(image_answer_item_doctor_photo, item.get("answer_photo"), new ImageOptions.Builder().setLoadingDrawableId(R.mipmap.img_default_photo_gray).setFailureDrawableId(R.mipmap.img_default_photo_gray).build());
        }
    }
}
