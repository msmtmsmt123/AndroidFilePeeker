/*
 * Copyright (C) 2015 CyanFlxy <cyanflxy@163.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cyanflxy.filepeeker.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.cyanflxy.filepeeker.FilePeeker;
import com.cyanflxy.peekerui.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 文件浏览界面
 * Created by CyanFlxy on 2015/6/3.
 */
public class FileBrowserActivity extends Activity {

    private static final String ROOT_PATH = "/";

    private String mCurrentPath;

    private TextView mCurrentPathView;
    private View mUplevelBtn;

    private View mMenuView;

    private FileListAdapter mAdapter;
    private AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            File file = (File) mAdapter.getItem(position);
            if (file.isFile()) {
                // TODO 文件处理办法
            } else {
                enterSubPath(file.getName());
            }
        }
    };
    private OnClickListener mOnMenuClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mMenuView.getVisibility() == View.VISIBLE) {
                hideMenu();
            } else {
                showMenu();
            }
        }
    };
    private OnClickListener mOnMenuZoneClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            hideMenu();
        }
    };
    private OnClickListener mOnCreateFileClick = new OnClickListener() {
        private InputDialog mDialog;

        @Override
        public void onClick(View v) {
            hideMenu();

            if (mDialog == null) {
                mDialog = new InputDialog(FileBrowserActivity.this);
                mDialog.setTitle(R.string.input_file_name);
                mDialog.setOnTextResultListener(new InputDialog.OnTextResultListener() {
                    @Override
                    public void onTextResult(String result) {
                        createFile(result);
                    }
                });
            }

            mDialog.show();
        }
    };
    private OnClickListener mOnCreateFolderClick = new OnClickListener() {
        private InputDialog mDialog;

        @Override
        public void onClick(View v) {
            hideMenu();

            if (mDialog == null) {
                mDialog = new InputDialog(FileBrowserActivity.this);
                mDialog.setTitle(R.string.input_folder_name);
                mDialog.setOnTextResultListener(new InputDialog.OnTextResultListener() {
                    @Override
                    public void onTextResult(String result) {
                        createFolder(result);
                    }
                });
            }

            mDialog.show();
        }
    };
    private OnClickListener mOnUplevelClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            uplevel();
        }
    };

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_file_browser);

        findViewById(R.id.menu).setOnClickListener(mOnMenuClickListener);

        mCurrentPathView = (TextView) findViewById(R.id.path);
        mCurrentPathView.setText(mCurrentPath);

        mUplevelBtn = findViewById(R.id.uplevel);
        mUplevelBtn.setOnClickListener(mOnUplevelClickListener);

        mAdapter = new FileListAdapter();

        ListView mListView = (ListView) findViewById(R.id.list_view);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(mItemClickListener);

        addMenu();
        enter(ROOT_PATH);
    }

    @Override
    public void onBackPressed() {
        if (uplevel()) {
            return;
        }

        super.onBackPressed();
    }

    private void addMenu() {
        mMenuView = View.inflate(this, R.layout.browser_menu, null);
        mMenuView.setOnClickListener(mOnMenuZoneClick);
        mMenuView.findViewById(R.id.create_file).setOnClickListener(mOnCreateFileClick);
        mMenuView.findViewById(R.id.create_folder).setOnClickListener(mOnCreateFolderClick);
        mMenuView.setVisibility(View.GONE);

        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        addContentView(mMenuView, params);
    }

    private void enterSubPath(String subFolder) {
        enter(subPath(mCurrentPath, subFolder));
    }

    private String subPath(String current, String sub) {
        if (current.endsWith(ROOT_PATH)) {
            return current + sub;
        } else {
            return current + ROOT_PATH + sub;
        }
    }

    private boolean uplevel() {
        if (mCurrentPath.equals(ROOT_PATH)) {
            return false;
        }

        String path;
        int index = mCurrentPath.lastIndexOf(ROOT_PATH);
        if (index != 0) {
            path = mCurrentPath.substring(0, index);
        } else {
            path = ROOT_PATH;
        }

        enter(path);
        return true;
    }

    private void enter(String path) {
        mCurrentPath = path;
        mAdapter.setData(FilePeeker.listFiles(mCurrentPath));
        mCurrentPathView.setText(mCurrentPath);

        if (ROOT_PATH.equals(mCurrentPath)) {
            mUplevelBtn.setEnabled(false);
        } else {
            mUplevelBtn.setEnabled(true);
        }
    }

    private void createFile(String fileName) {
        FilePeeker.createFile(mCurrentPath, fileName);
        refreshFileList();
    }

    private void createFolder(String folderName) {
        FilePeeker.createFolder(mCurrentPath, folderName);
        refreshFileList();
    }

    private void refreshFileList() {
        mAdapter.setData(FilePeeker.listFiles(mCurrentPath));
    }

    private void showMenu() {
        mMenuView.setVisibility(View.VISIBLE);
    }

    private void hideMenu() {
        mMenuView.setVisibility(View.GONE);
    }

    private class FileListAdapter extends BaseAdapter {

        private List<File> mFileList;

        public FileListAdapter() {
            mFileList = new ArrayList<>();
        }

        public void setData(File[] fileList) {
            mFileList.clear();
            mFileList.addAll(Arrays.asList(fileList));
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mFileList.size();
        }

        @Override
        public Object getItem(int position) {
            return mFileList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder vh;
            if (convertView == null) {
                convertView = LayoutInflater.from(FileBrowserActivity.this)
                        .inflate(R.layout.file_browser_list_item, parent, false);
                vh = new ViewHolder();
                vh.mFileName = (TextView) convertView.findViewById(R.id.file_name);
                convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }

            File file = mFileList.get(position);
            vh.mFileName.setText(file.getName());

            return convertView;
        }
    }

    private class ViewHolder {
        public TextView mFileName;
    }
}
