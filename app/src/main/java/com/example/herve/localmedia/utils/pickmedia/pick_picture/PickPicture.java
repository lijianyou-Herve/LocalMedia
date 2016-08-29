package com.example.herve.localmedia.utils.pickmedia.pick_picture;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Message;
import android.provider.MediaStore;

import com.example.herve.localmedia.utils.pickmedia.MediaBean;
import com.example.herve.localmedia.utils.pickmedia.PickMeaid;
import com.example.herve.localmedia.utils.pickmedia.PickMediaCallBack;
import com.example.herve.localmedia.utils.pickmedia.PickMediaHandler;
import com.example.herve.localmedia.utils.pickmedia.PickMediaTotal;
import com.example.herve.localmedia.utils.pickmedia.pick_audio.SortPictureList;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created           :Herve on 2016/8/29.
 *
 * @ Author          :Herve
 * @ e-mail          :lijianyou.herve@gmail.com
 * @ LastEdit        :2016/8/29
 * @ projectName     :LocalMedia
 * @ version
 */
public class PickPicture implements PickMeaid<MediaBean> {
    private Context mContext;
    private HashMap<String, List<MediaBean>> mGroupMap = new HashMap<>();
    private List<PickMediaTotal> mPictureItems = new ArrayList<>();
    private PickPictureThread mThread;
    private PickMediaHandler mHandler;
    private PickMediaCallBack mCallback;
    private String TAG = getClass().getSimpleName();

    public PickPicture(Context context, PickMediaCallBack callback) {
        this.mContext = context;
        this.mCallback = callback;
        mThread = new PickPictureThread() {
            @Override
            public void pickPictureThreadRun() {
                readPicture();
            }
        };
        mHandler = new PickMediaHandler(mCallback);
    }

    public void start() {
        mThread.start();
    }

    private void readPicture() {
        mGroupMap.clear();
        mPictureItems.clear();
        Uri pictureUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        ContentResolver contentResolver = mContext.getContentResolver();
        //只查询jpeg和png的图片
        Cursor cursor = contentResolver.query(pictureUri, null,
                MediaStore.Images.Media.MIME_TYPE + "=? or "
                        + MediaStore.Images.Media.MIME_TYPE + "=?",
                new String[]{"image/jpeg", "image/png"}, MediaStore.Images.Media.DATE_MODIFIED);
        if (cursor == null || cursor.getCount() == 0) {
            mHandler.sendEmptyMessage(PickMediaHandler.SCAN_ERROR);
        } else {
            while (cursor.moveToNext()) {
                MediaBean mediaBean = new MediaBean();
                //获取图片的路径
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                String fileName = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));
                try {
                    //获取该图片的父路径名
                    String parentName = new File(path).getParentFile().getName();
                    mediaBean.setOriginalFilePath(path);
                    mediaBean.setFileName(fileName);

                    //根据父路径名将图片放入到groupMap中
                    if (!mGroupMap.containsKey(parentName)) {
                        List<MediaBean> chileList = new ArrayList<>();

                        chileList.add(mediaBean);
                        mGroupMap.put(parentName, chileList);
                    } else {
                        List<MediaBean> chileList = mGroupMap.get(parentName);

                        chileList.add(mediaBean);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            cursor.close();
            //通知Handler扫描图片完成
            mPictureItems = subGroupOfPicture(mGroupMap);
            Message message = mHandler.obtainMessage();
            message.obj = mPictureItems;
            message.what = PickMediaHandler.SCAN_OK;
            message.sendToTarget();
        }
    }

    /**
     * 组装分组数据源，因为我们扫描手机的时候将图片信息放在HashMap中
     * 所以需要遍历HashMap将数据组装成List
     *
     * @param groupMap
     * @return
     */
    private List<PickMediaTotal> subGroupOfPicture(HashMap<String, List<MediaBean>> groupMap) {
        List<PickMediaTotal> list = new ArrayList<>();
        if (groupMap.size() == 0) {
            return list;
        }
        Iterator<Map.Entry<String, List<MediaBean>>> it = groupMap.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry<String, List<MediaBean>> entry = it.next();
            PickMediaTotal pictureTotal = new PickMediaTotal();
            String key = entry.getKey();

            List<MediaBean> value = entry.getValue();

            SortPictureList sortList = new SortPictureList();
            Collections.sort(value, sortList);//按修改时间排序
            pictureTotal.setFolderName(key);
            pictureTotal.setPictureCount(value.size());
            pictureTotal.setTopPicturePath(value.get(value.size() - 1).getOriginalFilePath());//获取该组的第一张图片
            list.add(pictureTotal);
        }
        return list;
    }

    public List<MediaBean> getChildPathList(int position) {
        List<MediaBean> childList = new ArrayList<>();
        if (mPictureItems.size() == 0)
            return childList;
        PickMediaTotal pictureTotal = mPictureItems.get(position);
        childList = mGroupMap.get(pictureTotal.getFolderName());
        SortPictureList sortList = new SortPictureList();
        Collections.sort(childList, sortList);
        return childList;
    }
}
