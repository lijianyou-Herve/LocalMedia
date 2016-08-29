package com.example.herve.localmedia.fragment;

import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.example.herve.localmedia.utils.log.AppLog;

import java.io.File;
import java.util.HashMap;
import java.util.Map;


/**
 * 过滤了不可读，不可写的文件 显示的文件为 .jpg .png 的图片文件和 .MP4 的视频文件 用户选择的时候会刷新媒体库，即把该文件添加到媒体库里面
 */
public class FileFragmentsMangerUtils implements FileListFragment.OnFragmentInteractionListener {

    private Map<String, FileListFragment> listFragments;

    private String firstSearchPath;
    private FragmentActivity mContext;
    private FragmentTransaction fragmentTransaction;
    private OnSelectListener onSelectListener;
    private OnCanBackListener onCanBackListener;
    private View containerView;
    private int containerViewId;

    private FileListFragment showingFragment;
    private FileListFragment parentFragment;
    private boolean isShow = false;

    public final static int MEDIA_TYPE_JPG = 0;
    public final static int MEDIA_TYPE_PNG = 1;
    public final static int MEDIA_TYPE_MP4 = 2;
    public final static int MEDIA_TYPE_MP3 = 3;
    private String TAG = getClass().getSimpleName();

    // 第二个参数是 viewgroup 的地址
    public FileFragmentsMangerUtils(FragmentActivity mContext, int containerViewId) {
        this.containerViewId = containerViewId;
        this.mContext = mContext;
        containerView = mContext.findViewById(containerViewId);
        listFragments = new HashMap<String, FileListFragment>();

    }

    // 搜素目录
    public void show(String searchPath) {
        firstSearchPath = searchPath;
        isShow = true;
        initFragmentData(searchPath);

    }

    // 隐藏
    public void hide() {
        isShow = false;
        removeAllFragments();
    }

    public void selectAll() {
        if (listFragments != null && listFragments.size() > 0) {

            if (showingFragment.isSelectAll()) {
                showingFragment.selectNone();
            } else {
                showingFragment.selectAll();

            }
        }

    }

    public boolean isShow() {
        return isShow;
    }

    public void onDestroy() {
        listFragments.clear();
        showingFragment = null;
        parentFragment = null;
    }

    public void onRestart() {
        showingFragment.refreshData();

    }

    // 放回上一级
    public void back() {
        // 有上一级才能返回
        if (listFragments.size() >= 2) {

            if (!showingFragment.getParentDir().equals(firstSearchPath) && showingFragment != parentFragment) {
                fragmentTransaction = mContext.getSupportFragmentManager().beginTransaction();
                fragmentTransaction.show(parentFragment).hide(showingFragment).commitAllowingStateLoss();
                parentFragment.refreshData();

                showingFragment = parentFragment;

                File file = new File(showingFragment.getParentDir());
                if (file.exists()) {// 不存在，则显示最初的目录
                    parentFragment = listFragments.get(file.getParentFile().getAbsolutePath());
                } else {
                    parentFragment = listFragments.get(firstSearchPath);
                }
                if (isCanBack()) {
                    setCanBackValue(true);
                } else {
                    setCanBackValue(false);
                }
            } else {
                setCanBackValue(false);
            }
        } else {
            setCanBackValue(false);
        }
    }

    private void setCanBackValue(boolean isCanBack) {
        if (onCanBackListener != null) {
            onCanBackListener.OnCanBackListener(isCanBack);
        }
    }

    public void setOnCanBackListener(OnCanBackListener onCanBackListener) {
        this.onCanBackListener = onCanBackListener;

    }

    public boolean isCanBack() {
        if (showingFragment != null) {
            if (showingFragment.getParentDir().equals(firstSearchPath)) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    // 选择的文件的类型和绝对路径
    public void setOnSelectListener(OnSelectListener onSelectListener) {

        this.onSelectListener = onSelectListener;

    }

    private void initFragmentData(String searchPath) {

        removeAllFragments();

        addNewFragment(searchPath);

        fragmentTransaction = mContext.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(containerViewId, listFragments.get(searchPath));
        fragmentTransaction.commit();
        showingFragment = listFragments.get(searchPath);
        if (containerView != null) {
            containerView.setVisibility(View.VISIBLE);
        }
    }

    private void addNewFragment(String searchPath) {

        FileListFragment fileListFragment = new FileListFragment();
        fileListFragment.setOnFragmentInteractionListener(this);

        fileListFragment.setParentDir(searchPath);

        listFragments.put(searchPath, fileListFragment);
        Bundle bundle = new Bundle();
        bundle.putString("searchPath", searchPath);
        listFragments.get(searchPath).setArguments(bundle);

    }

    @Override
    public void onFragmentGoonSearch(int position, int mediaType, String searchPath) {

        if (mediaType == -1) {
            if (listFragments.get(searchPath) == null) {
                addNewFragment(searchPath);
            }
            fragmentTransaction = mContext.getSupportFragmentManager().beginTransaction();

            if (listFragments.get(searchPath).isAdded()) {
                listFragments.get(searchPath).refreshData();
                fragmentTransaction.hide(showingFragment).show(listFragments.get(searchPath)).commitAllowingStateLoss();
            } else {
                fragmentTransaction.hide(showingFragment).add(containerViewId, listFragments.get(searchPath)).commitAllowingStateLoss();
            }

            parentFragment = showingFragment;
            showingFragment = listFragments.get(searchPath);
            if (isCanBack()) {
                setCanBackValue(true);
            } else {
                setCanBackValue(false);
            }

        } else {
            MediaScannerConnection.scanFile(mContext, new String[]{searchPath}, null, new MediaScannerConnection.OnScanCompletedListener() {
                @Override
                public void onScanCompleted(String path, Uri uri) {
                    AppLog.i("ExternalStorage", "Scanned 刷新成功:" + path + ":");
                }
            });
            if (onSelectListener != null) {

                if (mediaType == MEDIA_TYPE_MP3) {
                    onSelectListener.onSelectListenerMP3(searchPath);
                }

                if (mediaType == MEDIA_TYPE_JPG) {
                    onSelectListener.onSelectListenerJPG(searchPath);
                }
                if (mediaType == MEDIA_TYPE_PNG) {
                    onSelectListener.onSelectListenerPNG(searchPath);
                }
                if (mediaType == MEDIA_TYPE_MP4) {
                    onSelectListener.onSelectListenerMP4(searchPath);
                }
            }
        }
    }

    private void removeAllFragments() {
        if (listFragments != null && listFragments.size() > 0) {

            for (FileListFragment fragment : listFragments.values()) {
                fragmentTransaction = mContext.getSupportFragmentManager().beginTransaction();
                if (fragmentTransaction != null) {
                    fragmentTransaction.remove(fragment).commitAllowingStateLoss();
                }
            }
            listFragments.clear();
        }

        if (containerView != null) {
            containerView.setVisibility(View.GONE);
        }

    }

    public interface OnCanBackListener {
        void OnCanBackListener(boolean isCanBack);

    }

    public interface OnSelectListener {

        void onSelectListenerJPG(String JPGPath);

        void onSelectListenerPNG(String PNGPath);

        void onSelectListenerMP4(String MP4Path);

        void onSelectListenerMP3(String MP3Path);
    }

}
