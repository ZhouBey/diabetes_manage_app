package com.zpy.diabetes.app.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.CircularImageView;
import com.zpy.diabetes.app.R;
import com.zpy.diabetes.app.interf.OnListViewItemBtnClickListener;
import com.zpy.diabetes.app.util.TextUtil;

import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.List;
import java.util.Map;

public class AnswerListViewAdapter<T> extends ArrayAdapter<T> {
    private Context context;
    private int resource;
    private List list;
    private OnListViewItemBtnClickListener onListViewItemBtnClickListener;

    public OnListViewItemBtnClickListener getOnListViewItemBtnClickListener() {
        return onListViewItemBtnClickListener;
    }

    public void setOnListViewItemBtnClickListener(OnListViewItemBtnClickListener onListViewItemBtnClickListener) {
        this.onListViewItemBtnClickListener = onListViewItemBtnClickListener;
    }

    public AnswerListViewAdapter(Context context, int resource, List<T> objects) {
        super(context, resource, objects);
        this.context = context;
        this.list = objects;
        this.resource = resource;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = null;
        if (convertView == null) {
            view = View.inflate(context, resource, null);
        } else {
            view = convertView;
        }
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        if (viewHolder == null) {
            viewHolder = new ViewHolder();
            viewHolder.tv_answer_item_doctor_name = (TextView) view.findViewById(R.id.tv_answer_item_doctor_name);
            viewHolder.tv_answer_item_time = (TextView) view.findViewById(R.id.tv_answer_item_time);
            viewHolder.tv_answer_item_content = (TextView) view.findViewById(R.id.tv_answer_item_content);
            viewHolder.image_answer_item_doctor_photo = (CircularImageView) view.findViewById(R.id.image_answer_item_doctor_photo);
            viewHolder.layout_answer_item_doctor = (RelativeLayout) view.findViewById(R.id.layout_answer_item_doctor);
            viewHolder.layout_answer_item_doctor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onListViewItemBtnClickListener.onListViewItemBtnClick(position);
                }
            });
            view.setTag(viewHolder);
        }
        viewHolder.setAttrs((Map<String, String>) list.get(position));
        return view;
    }

    class ViewHolder {
        private TextView tv_answer_item_doctor_name,
                tv_answer_item_time,
                tv_answer_item_content;
        private CircularImageView image_answer_item_doctor_photo;
        private RelativeLayout layout_answer_item_doctor;

        public void setAttrs(Map<String, String> item) {
            tv_answer_item_time.setText(item.get("answer_time"));
            tv_answer_item_content.setText(item.get("answer_content"));
            tv_answer_item_doctor_name.setText(TextUtil.getHandleDoctorName(item.get("answer_name")));
            image_answer_item_doctor_photo.setImageResource(R.mipmap.img_default_photo_blue);
            x.image().bind(image_answer_item_doctor_photo, item.get("answer_photo"), new ImageOptions.Builder().setLoadingDrawableId(R.mipmap.img_default_photo_blue).setFailureDrawableId(R.mipmap.img_default_photo_blue).build());
        }
    }
}
