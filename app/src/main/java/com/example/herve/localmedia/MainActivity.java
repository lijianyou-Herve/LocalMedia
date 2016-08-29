package com.example.herve.localmedia;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.herve.localmedia.adapter.ChildFileAdapter;
import com.example.herve.localmedia.adapter.FolderAdapter;
import com.example.herve.localmedia.fragment.FileFragmentsMangerUtils;
import com.example.herve.localmedia.utils.file_utils.StorageList;
import com.example.herve.localmedia.utils.pickmedia.MediaBean;
import com.example.herve.localmedia.utils.pickmedia.PickMediaCallBack;
import com.example.herve.localmedia.utils.pickmedia.PickMediaHelper;
import com.example.herve.localmedia.utils.pickmedia.PickMediaTotal;
import com.example.herve.localmedia.widget.FilterImageView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity {

    private Spinner spinner;
    private List<String> sdCards;
    private String[] paths;
    private ListView listView;
    private ListView newListView;
    private ProgressBar progress;
    private FilterImageView camera;
    private FolderAdapter folderAdapter2;
    private Context mContext;
    private String TAG = getClass().getSimpleName();
    private PickMediaHelper pickPictureHelper;

    private int isfirst = 0;
    private FileFragmentsMangerUtils fileFragmentsMangerUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;

        findViewById();

        initData();

        initListener();

    }

    ChildFileAdapter childAdapter;

    private void initListener() {

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final List<MediaBean> childBeans = pickPictureHelper.getChildPathList(position);

                Log.e(TAG, "onItemClick: childBeans=" + childBeans.size());
                if (newListView == null) {
                    newListView = new ListView(mContext);
                    LinearLayout.LayoutParams lauout = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    newListView.setLayoutParams(lauout);
                    childAdapter = new ChildFileAdapter(mContext, childBeans);
                    newListView.setAdapter(childAdapter);

                } else {
                    childAdapter.setData(childBeans);
                }

                newListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        Toast.makeText(mContext, childBeans.get(position).getOriginalFilePath() + "", Toast.LENGTH_SHORT).show();
                    }
                });

                if (listView.getParent() instanceof ViewGroup) {
                    if (newListView.getParent() == null) {
                        Log.e(TAG, "onItemClick: 添加");
                        ((ViewGroup) listView.getParent()).addView(newListView);
                    }
                    listView.setVisibility(View.GONE);
                }

                listView.setVisibility(View.GONE);

                newListView.setVisibility(View.VISIBLE);

            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                try {
                    Field field = AdapterView.class.getDeclaredField("mOldSelectedPosition");
                    field.setAccessible(true);  //设置mOldSelectedPosition可访问
                    field.setInt(spinner, AdapterView.INVALID_POSITION); //设置mOldSelectedPosition的值

                } catch (Exception e) {
                    e.printStackTrace();
                }

                Log.e(TAG, "onItemSelected: ");

                if (isfirst < 2) {
                    isfirst++;
                } else {
                    if (fileFragmentsMangerUtils.isShow()) {
                        fileFragmentsMangerUtils.hide();
                    } else {

                        fileFragmentsMangerUtils.show(paths[i]);
                    }

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                refReshPicture();

            }
        });

    }


    private void findViewById() {
        listView = (ListView) findViewById(R.id.local_album_list);
        camera = (FilterImageView) findViewById(R.id.loacal_album_camera);
        progress = (ProgressBar) findViewById(R.id.progress_bar);
        spinner = (Spinner) findViewById(R.id.spinner);
    }

    private void initData() {
        fileFragmentsMangerUtils = new FileFragmentsMangerUtils(this, R.id.fm01id);
        fileFragmentsMangerUtils.setOnSelectListener(new FileFragmentsMangerUtils.OnSelectListener() {
            @Override
            public void onSelectListenerJPG(String JPGPath) {
                Toast.makeText(mContext, "JPGPath" + "=" + JPGPath, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onSelectListenerPNG(String PNGPath) {
                Toast.makeText(mContext, "PNGPath" + "=" + PNGPath, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onSelectListenerMP4(String MP4Path) {
                Toast.makeText(mContext, "MP4Path" + "=" + MP4Path, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onSelectListenerMP3(String MP3Path) {
                Toast.makeText(mContext, "MP3Path" + "=" + MP3Path, Toast.LENGTH_SHORT).show();

            }
        });
        refReshPicture();
        initSpannerData();

    }

    private void refReshPicture() {
        pickPictureHelper = PickMediaHelper.readPicture(this, new PickMediaCallBack() {
            @Override
            public void onStart() {
                Log.e("refReshPicture", " onStart() 开始获取媒体库");

                //显示进度条
                ((View) progress.getParent()).setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
                progress.setVisibility(View.VISIBLE);

            }

            @Override
            public void onSuccess(List<PickMediaTotal> list) {
                Log.e("refReshPicture", " onSuccess() 开始获取媒体库");

                progress.setVisibility(View.GONE);
                ((View) progress.getParent()).setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);

                if (folderAdapter2 == null) {
                    folderAdapter2 = new FolderAdapter(mContext, list);
                    listView.setAdapter(folderAdapter2);
                } else {
                    folderAdapter2.setData(list);
                }

                //读取成功，返回 list，直接丢入到 ListView 适配器中
            }

            @Override
            public void onError() {
                Log.e("refReshPicture", " onError() 获取媒体库失败");

                progress.setVisibility(View.GONE);

            }
        });
    }

    private void initSpannerData() {

        StorageList storageList = new StorageList(mContext);
        paths = storageList.getVolumePaths();
        sdCards = new ArrayList<String>();

        if (paths != null && paths.length > 0) {

            sdCards.add(new String("内置SD卡 "));

            if (paths.length >= 2) {
                sdCards.add(new String("外置SD卡"));
            }
        }

        //  建立Adapter绑定数据源
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, sdCards);
        //绑定Adapter
        spinner.setAdapter(arrayAdapter);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if (newListView != null) {

                if (newListView.isShown()) {
                    newListView.setVisibility(View.GONE);
                    listView.setVisibility(View.VISIBLE);
                }
            }

            fileFragmentsMangerUtils.back();
        }

        return false;
    }


}
