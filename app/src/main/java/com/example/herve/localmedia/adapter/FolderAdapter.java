package com.example.herve.localmedia.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.herve.localmedia.R;
import com.example.herve.localmedia.utils.pickmedia.PickMediaTotal;

import java.util.List;

public class FolderAdapter extends BaseAdapter {

    private List<PickMediaTotal> data;
    private Context mContext;
    private String TAG = getClass().getSimpleName();

    public FolderAdapter(Context context, List<PickMediaTotal> list) {
        this.mContext = context;
        this.data = list;

    }

    public void setData(List<PickMediaTotal> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (convertView == null || convertView.getTag() == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_albumfoler, null);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.textview);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        PickMediaTotal pictureTotal = data.get(i);
        String name = pictureTotal.getFolderName();

        int count = pictureTotal.getPictureCount();

        viewHolder.textView.setText(name + "(" + count + ")");

        if (count > 0) {

            Glide.with(mContext).load(R.mipmap.ic_launcher).into(viewHolder.imageView);
//            Glide.with(mContext).load(pictureTotal.getTopPicturePath()).into(viewHolder.imageView);

        }

        return convertView;
    }

    private class ViewHolder {
        ImageView imageView;
        TextView textView;
    }

}