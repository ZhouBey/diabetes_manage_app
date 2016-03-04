package com.zpy.diabetes.app.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.CircularImageView;
import com.zpy.diabetes.app.R;

import java.util.List;
import java.util.Map;

public class QaListViewItemAdapter<T> extends ArrayAdapter<T> {
    private Context context;
    private int resource;
    private List list;

    public QaListViewItemAdapter(Context context, int resource, List<T> objects) {
        super(context, resource, objects);
        this.context = context;
        this.list = objects;
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        if(convertView==null) {
            view = View.inflate(context,resource,null);
        } else {
            view = convertView;
        }
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        if(viewHolder==null) {
            viewHolder = new ViewHolder();
            viewHolder.tv_qa_item_phone = (TextView) view.findViewById(R.id.tv_qa_item_phone);
            viewHolder.tv_qa_item_time = (TextView) view.findViewById(R.id.tv_qa_item_time);
            viewHolder.tv_qa_item_reply_count = (TextView) view.findViewById(R.id.tv_qa_item_reply_count);
            viewHolder.tv_qa_item_question_title = (TextView) view.findViewById(R.id.tv_qa_item_question_title);
            viewHolder.tv_qa_item_question_content = (TextView) view.findViewById(R.id.tv_qa_item_question_content);
            viewHolder.image_qa_item_photo = (CircularImageView) view.findViewById(R.id.image_qa_item_photo);
            view.setTag(viewHolder);
        }
        viewHolder.setAttrs((Map<String, String>) list.get(position));
        return view;
    }

    class ViewHolder {
        private TextView tv_qa_item_phone,
                tv_qa_item_time,
                tv_qa_item_reply_count,
                tv_qa_item_question_title,
                tv_qa_item_question_content;
        private CircularImageView image_qa_item_photo;
        public void setAttrs(Map<String, String> item) {
            tv_qa_item_phone.setText(item.get("suffer_phone"));
            tv_qa_item_time.setText(item.get("question_time"));
            tv_qa_item_reply_count.setText(item.get("qa_reply_count"));
            tv_qa_item_question_title.setText(item.get("question_title"));
            tv_qa_item_question_content.setText(item.get("question_content"));
            image_qa_item_photo.setImageResource(R.mipmap.img_default_photo_blue);
        }
    }
}
