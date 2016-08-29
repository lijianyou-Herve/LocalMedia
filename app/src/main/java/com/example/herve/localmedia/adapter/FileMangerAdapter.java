package com.example.herve.localmedia.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.herve.localmedia.R;
import com.example.herve.localmedia.bean.FileModel;
import com.example.herve.localmedia.bean.FilesGroup;

import java.util.ArrayList;
import java.util.List;


public class FileMangerAdapter extends BaseAdapter {

    private Context mContext;
    private FilesGroup filesGroup;
    private boolean selectAll;
    private List<String> mediaLists;

    public FileMangerAdapter(Context mContext, FilesGroup filesGroup) {
        this.mContext = mContext;
        this.filesGroup = filesGroup;
        mediaLists = new ArrayList<>();
    }

    public boolean isSelectAll() {
        checkIsSelactAll();
        return selectAll;

    }

    private void checkIsSelactAll() {
        int selectConut = 0;
        if (filesGroup != null && filesGroup.getData() != null) {
            for (FileModel fileModel : filesGroup.getData()) {
                if (mediaLists != null) {
                    if (mediaLists.contains(fileModel.getAbsolutePath())) {
                        selectConut++;
                    }
                }

            }
        }
        if (filesGroup != null && selectConut == filesGroup.getData().size()) {
            selectAll = true;
        } else {
            selectAll = false;
        }

    }

    public void selectAll() {
        selectAll = true;
        setSelectAll();
        notifyDataSetChanged();

    }

    public void selectNone() {
        selectAll = false;
        setSelectAll();
        notifyDataSetChanged();
    }

    private void setSelectAll() {
        if (selectAll) {
            if (filesGroup != null && filesGroup.getData() != null) {
                for (FileModel fileModel : filesGroup.getData()) {

                    if (mediaLists != null) {

                        if (!mediaLists.contains(fileModel.getAbsolutePath())) {
                            mediaLists.add(fileModel.getAbsolutePath());
                        }
                    }

                }
            }

        } else {
            if (filesGroup != null && filesGroup.getData() != null) {
                for (FileModel fileModel : filesGroup.getData()) {

                    if (mediaLists != null) {

                        if (mediaLists.contains(fileModel.getAbsolutePath())) {

                            mediaLists.remove(fileModel.getAbsolutePath());
                        }
                    }

                }
            }
        }

    }

    @Override
    public int getCount() {
        return filesGroup.getData().size();
    }

    @Override
    public Object getItem(int position) {
        return filesGroup.getData().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_albumfoler, null);
            viewHolder = new ViewHolder();
            viewHolder.ll_cb_select = (LinearLayout) convertView.findViewById(R.id.ll_cb_select);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
            viewHolder.iv_cb_select = (ImageView) convertView.findViewById(R.id.iv_cb_select);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.textview);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        FileModel fileModel = filesGroup.getData().get(position);

        // 文件夹
        if (fileModel.getType() == -1) {
            Glide.with(mContext).load(R.drawable.folder01).into(viewHolder.imageView);
        }
        // 图片文件
        else if (fileModel.getType() == 0 || fileModel.getType() == 1) {
            Glide.with(mContext).load(fileModel.getAbsolutePath()).into(viewHolder.imageView);
        }
        // 视频文件
        else if (fileModel.getType() == 2) {
            Glide.with(mContext).load(fileModel.getAbsolutePath()).asBitmap().into(viewHolder.imageView);
        }
        // 音乐文件
        else if (fileModel.getType() == 3) {
            Glide.with(mContext).load(R.drawable.mp3).into(viewHolder.imageView);
        }

        viewHolder.textView.setText(fileModel.getFileName());

        viewHolder.iv_cb_select.setImageResource(R.drawable.details_alert_chose);

        if (mediaLists != null && mediaLists.size() > 0) {

            if (mediaLists.contains(fileModel.getAbsolutePath())) {
                viewHolder.iv_cb_select.setImageResource(R.drawable.details_alert_chose_2);
            }

        }

        viewHolder.ll_cb_select.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String selectFile = filesGroup.getData().get(position).getAbsolutePath();
                if (mediaLists != null) {
                    if (mediaLists.contains(selectFile)) {
                        mediaLists.remove(selectFile);

                    } else {
                        mediaLists.add(filesGroup.getData().get(position).getAbsolutePath());

                    }
                }

                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    class ViewHolder {
        LinearLayout ll_cb_select;
        ImageView imageView, iv_cb_select;
        TextView textView;
    }
}
