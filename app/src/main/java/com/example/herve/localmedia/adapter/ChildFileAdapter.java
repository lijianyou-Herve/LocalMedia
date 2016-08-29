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
import com.example.herve.localmedia.utils.pickmedia.MediaBean;
import com.example.herve.localmedia.utils.pickmedia.pick_audio.model.SongBean;

import java.util.List;

public class ChildFileAdapter<T extends MediaBean> extends BaseAdapter {

    private List<T> data;
    private Context mContext;
    private String TAG = getClass().getSimpleName();

    public ChildFileAdapter(Context context, List<T> list) {
        this.mContext = context;
        this.data = list;

    }

    public void setData(List<T> list) {
        this.data = list;
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_child_file, null);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.textview);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        MediaBean mediaBean = data.get(i);
        String name = mediaBean.getFileName();

        viewHolder.textView.setText(name);

        Glide.with(mContext).load(mediaBean.getOriginalFilePath()).into(viewHolder.imageView).onLoadFailed(new Exception(), mContext.getResources().getDrawable(R.mipmap.ic_launcher));

        return convertView;
    }

    private class ViewHolder {
        ImageView imageView;
        TextView textView;
    }

}