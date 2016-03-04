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

public class DoctorListViewAdapter<T> extends ArrayAdapter<T>{
    private Context context;
    private int resource;
    private List list;
    public DoctorListViewAdapter(Context context, int resource, List<T> objects) {
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
            viewHolder.tv_doctors_item_name = (TextView) view.findViewById(R.id.tv_doctors_item_name);
            viewHolder.tv_doctors_item_position = (TextView) view.findViewById(R.id.tv_doctors_item_position);
            viewHolder.tv_doctors_item_hospital = (TextView) view.findViewById(R.id.tv_doctors_item_hospital);
            viewHolder.tv_doctors_item_info = (TextView) view.findViewById(R.id.tv_doctors_item_info);
            viewHolder.image_doctors_item_photo = (CircularImageView) view.findViewById(R.id.image_doctors_item_photo);
            view.setTag(viewHolder);
        }
        viewHolder.setAttrs((Map<String, String>) list.get(position));
        return view;
    }

    class ViewHolder {
        private TextView tv_doctors_item_name,
                tv_doctors_item_position,
                tv_doctors_item_hospital,
                tv_doctors_item_info;
        private CircularImageView image_doctors_item_photo;
        public void setAttrs(Map<String, String> item) {
            tv_doctors_item_position.setText(item.get("doctor_position"));
            tv_doctors_item_hospital.setText(item.get("doctor_for_hospital"));
            tv_doctors_item_name.setText(item.get("doctor_name"));
            tv_doctors_item_info.setText(item.get("doctor_info"));
            image_doctors_item_photo.setImageResource(R.mipmap.img_default_photo_blue);
        }
    }
}
