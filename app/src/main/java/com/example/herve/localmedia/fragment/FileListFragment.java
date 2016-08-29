package com.example.herve.localmedia.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.herve.localmedia.R;
import com.example.herve.localmedia.adapter.FileMangerAdapter;
import com.example.herve.localmedia.bean.FilesGroup;
import com.example.herve.localmedia.utils.file_utils.FileMangerUtils;


public class FileListFragment extends Fragment {

	private Context mContext;

	private String parentDir;
	private String searchPath;
	private View rootView;
	private FilesGroup data;
	private ListView lv_files;
	private FileMangerAdapter fileMangerAdapter;
	private FileMangerUtils fileMangerUtils;
	private OnFragmentInteractionListener mListener;
	private String TAG = getClass().getSimpleName();

	public FileListFragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mContext = getActivity();

		rootView = inflater.inflate(R.layout.fragment_file_list, container, false);

		searchPath = (String) getArguments().get("searchPath");

		initView(rootView);

		initData();

		initListener();

		return rootView;
	}

	public String getParentDir() {
		return parentDir;
	}

	public void setParentDir(String parentDir) {
		this.parentDir = parentDir;
	}

	public boolean isSelectAll() {
		return fileMangerAdapter.isSelectAll();

	}

	public void selectAll() {
		fileMangerAdapter.selectAll();

	}

	public void selectNone() {
		fileMangerAdapter.selectNone();
	}

	private void initListener() {

		lv_files.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (!NoDoubleClickUtils.isDoubleClick()) {
					if (mListener != null) {
						mListener.onFragmentGoonSearch(position, data.getData().get(position).getType(), data.getData().get(position).getAbsolutePath());
					}
				}

			}
		});
	}

	private void initView(View view) {

		lv_files = (ListView) view.findViewById(R.id.lv_files);
		lv_files.setDivider(null);

	}

	public void refreshData() {
		if (fileMangerUtils == null) {
			initData();
		} else {
			data = fileMangerUtils.getData(searchPath);
			fileMangerAdapter.notifyDataSetChanged();
		}

	}

	private void initData() {

		fileMangerUtils = new FileMangerUtils();

		data = fileMangerUtils.getData(searchPath);

		fileMangerAdapter = new FileMangerAdapter(mContext, data);

		lv_files.setAdapter(fileMangerAdapter);

	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}

	public void setOnFragmentInteractionListener(FileListFragment.OnFragmentInteractionListener onFragmentInteractionListener) {
		this.mListener = onFragmentInteractionListener;

	}

	public interface OnFragmentInteractionListener {

		void onFragmentGoonSearch(int position, int type, String searchPath);
	}

	static class NoDoubleClickUtils {
		private static long lastClickTime;
		private static final int SPACE_TIME = 500;

		public static void initLastClickTime() {
			lastClickTime = 0;
		}

		public static synchronized boolean isDoubleClick() {
			long currentTime = System.currentTimeMillis();
			boolean isClick2;
			if (currentTime - lastClickTime > SPACE_TIME) {
				isClick2 = false;
			} else {
				isClick2 = true;
			}
			lastClickTime = currentTime;
			return isClick2;
		}
	}
}
